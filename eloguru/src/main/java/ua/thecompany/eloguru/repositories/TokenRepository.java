package ua.thecompany.eloguru.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.thecompany.eloguru.model.Token;

public interface TokenRepository extends BaseRepository<Token, Long> {

    @Modifying
    @Query(value = "update #{#entityName} t set t.token = :newToken where t.account.id = :id")
    void updateTokenById(Long id, String newToken);

    Optional<Token> findByToken(String token);
}
