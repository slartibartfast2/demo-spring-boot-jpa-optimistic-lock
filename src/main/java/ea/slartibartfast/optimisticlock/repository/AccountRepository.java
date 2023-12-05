package ea.slartibartfast.optimisticlock.repository;

import ea.slartibartfast.optimisticlock.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
