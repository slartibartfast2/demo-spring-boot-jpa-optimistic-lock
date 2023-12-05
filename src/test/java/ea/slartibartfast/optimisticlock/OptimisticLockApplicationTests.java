package ea.slartibartfast.optimisticlock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ea.slartibartfast.optimisticlock.model.dto.AccountDto;
import ea.slartibartfast.optimisticlock.model.entity.Account;
import ea.slartibartfast.optimisticlock.repository.AccountRepository;
import ea.slartibartfast.optimisticlock.service.AccountService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OptimisticLockApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private AccountRepository accountRepository;

    @SpyBean
    private AccountService accountService;

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void should_handle_create_new_account() {
        // given
        var createDto =
                new AccountDto(UUID.randomUUID().toString(), "John Doe", 0.0);

        // when
        JsonNode jsonNode = given().body(createDto)
                                   .contentType(ContentType.JSON)
                                   .post("/api/account")
                                   .then()
                                   .statusCode(HttpStatus.OK.value())
                                   .extract()
                                   .body().as(JsonNode.class);

        AccountDto createdDto = mapper.convertValue(jsonNode, new TypeReference<>(){});

        // then
        assertThat(createdDto).usingRecursiveComparison().ignoringFields("version").isEqualTo(createDto);
        assertThat(createdDto.getVersion()).isZero();
    }

    @Test
    void should_handle_single_update_account() {
        // given
        var createEntity = Account.builder().id(UUID.randomUUID().toString()).name("John Cusack").balance(BigDecimal.valueOf(1000)).build();
        var createdEntity = accountRepository.save(createEntity);

        // when
        var updateDto = AccountDto.builder().id(createEntity.getId()).name(createdEntity.getName()).balance(900).build();
        JsonNode jsonNode = given().body(updateDto)
                                   .contentType(ContentType.JSON)
                                   .put("/api/account")
                                   .then()
                                   .statusCode(HttpStatus.OK.value())
                                   .extract()
                                   .body().as(JsonNode.class);

        AccountDto updatedDto = mapper.convertValue(jsonNode, new TypeReference<>(){});

        // then
        assertThat(updatedDto).usingRecursiveComparison().ignoringFields("version").isEqualTo(updateDto);
        assertThat(updatedDto.getVersion()).isEqualTo(1);
    }

    @Test
    void should_handle_concurrent_account_updates() throws InterruptedException {
        // given
        var createEntity = Account.builder().id(UUID.randomUUID().toString()).name("John Cusack").balance(BigDecimal.valueOf(1000)).build();
        var createdEntity = accountRepository.save(createEntity);

        // when
        final ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i<5; i++) {
            var updateDto =
                    AccountDto.builder().name(createdEntity.getName()).id(createEntity.getId()).balance(900d).build();

            executor.execute(() -> {
                try {
                    given().body(updateDto)
                           .contentType(ContentType.JSON)
                           .put("/api/account")
                           .then()
                           .statusCode(HttpStatus.OK.value())
                           .extract()
                           .body().as(JsonNode.class);
                } catch (Exception e) {
                    // ignore optimistic lock exceptions
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // then
        var account = accountRepository.findById(createEntity.getId());
        assertThat(account).isPresent();
        assertThat(account.get().getVersion()).isEqualTo(1);

        assertAll(
                () -> assertEquals(1, account.get().getVersion()),
                () -> assertEquals(BigDecimal.valueOf(900).setScale(2, RoundingMode.DOWN), account.get().getBalance()),
                () -> verify(accountService, times(5)).updateAccount(any(AccountDto.class))
        );
    }
}

