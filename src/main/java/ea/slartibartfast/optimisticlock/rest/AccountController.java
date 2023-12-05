package ea.slartibartfast.optimisticlock.rest;

import ea.slartibartfast.optimisticlock.api.AccountApi;
import ea.slartibartfast.optimisticlock.model.dto.AccountDto;
import ea.slartibartfast.optimisticlock.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class AccountController implements AccountApi {

    private final AccountService accountService;

    @Override
    public AccountDto retrieveAccount(String accountId) {
        return accountService.getAccount(accountId);
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @Override
    public AccountDto updateAccount(AccountDto accountDto) {
        return accountService.updateAccount(accountDto);
    }
}
