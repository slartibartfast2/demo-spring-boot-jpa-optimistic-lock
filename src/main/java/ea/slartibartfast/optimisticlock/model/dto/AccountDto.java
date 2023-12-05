package ea.slartibartfast.optimisticlock.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String id;
    private String name;
    private double balance;
    private Long version;

    public AccountDto(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }
}
