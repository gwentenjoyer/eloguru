package ua.thecompany.eloguru.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String generateToken(UserDetails userDetails);
    String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    );

    boolean isTokenGetSign(String token);
    String getAccessTokenFromCookie(HttpServletRequest request);
    void setJwtCookies(HttpServletResponse response, String accessToken);
    void setJwtCookies(HttpServletResponse response, String accessToken, String refreshToken);
    boolean isTokenValid(String token, UserDetails userDetails);
    void deleteJwtCookies(HttpServletResponse response);
}
