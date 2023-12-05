package ea.slartibartfast.optimisticlock.service;

import ea.slartibartfast.optimisticlock.model.dto.AccountDto;
import ea.slartibartfast.optimisticlock.model.mapper.AccountMapper;
import ea.slartibartfast.optimisticlock.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountDto getAccount(String id) {
        return accountRepository.findById(id).map(accountMapper::mapToDto).orElseThrow(EntityNotFoundException::new);
    }

    public AccountDto createAccount(AccountDto accountDto) {
        var account = accountMapper.mapToEntity(accountDto);
        return Optional.of(accountRepository.save(account)).map(accountMapper::mapToDto).get();
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        var account = accountRepository.findById(accountDto.getId()).orElseThrow(EntityNotFoundException::new);
        account.setBalance(BigDecimal.valueOf(accountDto.getBalance()));
        log.info("Account new balance: {}", account.getBalance());
        return Optional.of(accountRepository.save(account)).map(accountMapper::mapToDto).get();
    }
}
