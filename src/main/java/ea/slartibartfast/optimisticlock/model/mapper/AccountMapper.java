package ea.slartibartfast.optimisticlock.model.mapper;

import ea.slartibartfast.optimisticlock.model.dto.AccountDto;
import ea.slartibartfast.optimisticlock.model.entity.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {

    public Account mapToEntity(AccountDto accountDto) {
        return Account.builder()
                      .balance(BigDecimal.valueOf(accountDto.getBalance()))
                      .id(accountDto.getId())
                      .name(accountDto.getName())
                      .build();
    }

    public AccountDto mapToDto(Account account) {
        return AccountDto.builder()
                         .id(account.getId())
                         .version(account.getVersion())
                         .balance(account.getBalance()
                         .doubleValue())
                         .name(account.getName())
                         .build();
    }
}
