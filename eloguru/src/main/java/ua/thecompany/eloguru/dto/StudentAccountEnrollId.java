package ua.thecompany.eloguru.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public record StudentAccountEnrollId(@NotNull Long studentAccountId) implements Serializable {
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AccountRequest {

        private String fullname;
        private String email;
        private String password;
    }
}
