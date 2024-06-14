package ua.thecompany.eloguru.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.thecompany.eloguru.model.RefreshToken;

@Repository
interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "select CASE when COUNT(RefreshToken) > 0 THEN true ELSE false END from RefreshToken where RefreshToken = :refreshToken")
    boolean findRefreshTokenByRefreshToken(String refreshToken);

    @Modifying
    @Query(value = "update RefreshToken r set r.RefreshToken = :newToken where r.user.id = :id")
    void updateRefreshTokenById(Long id, String newToken);
}
