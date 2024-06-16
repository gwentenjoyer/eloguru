package ua.thecompany.eloguru.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.model.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends BaseRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Account> findByActivationCode(String activationCode);

//    @Override
//    @Query("select t from #{#entityName} t where t.active = true")
    Page<Account> findAll(Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from #{#entityName} t where t.id = :id")
    void forceDeleteById(Long id);

}
