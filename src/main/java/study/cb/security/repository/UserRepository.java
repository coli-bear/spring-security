package study.cb.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.cb.security.domain.Account;

public interface UserRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
}
