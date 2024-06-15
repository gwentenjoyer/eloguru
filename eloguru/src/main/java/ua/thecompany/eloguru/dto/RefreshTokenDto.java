package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.model.TokenType;

import java.io.Serializable;
import java.time.LocalDateTime;

public record RefreshTokenDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active,
                              String RefreshToken, TokenType tokenType, Account user) implements Serializable {
}
