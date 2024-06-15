package ua.thecompany.eloguru.repositories;

import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Account> findByActivationCode(String activationCode);

}
