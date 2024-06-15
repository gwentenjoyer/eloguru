package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "select CASE when COUNT(#{#entityName}) > 0 THEN true ELSE false END from  #{#entityName} where #{#entityName} = :refreshToken")
    boolean findRefreshTokenByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = "update  #{#entityName} r set r.#{#entityName} = :newToken where r.account.id = :id")
    void updateRefreshTokenById(Long id, String newToken);
}
