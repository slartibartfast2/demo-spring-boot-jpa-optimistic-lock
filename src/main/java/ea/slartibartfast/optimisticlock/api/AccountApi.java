package ea.slartibartfast.optimisticlock.api;

import ea.slartibartfast.optimisticlock.model.dto.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
public interface AccountApi {

    @GetMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
    AccountDto retrieveAccount(@RequestParam String accountId);

    @PostMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AccountDto createAccount(@RequestBody AccountDto accountDto);

    @PutMapping(value = "/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    AccountDto updateAccount(@RequestBody AccountDto accountDto);
}
