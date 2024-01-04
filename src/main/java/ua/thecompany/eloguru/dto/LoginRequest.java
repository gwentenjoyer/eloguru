package ua.thecompany.eloguru.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record LoginRequest(@NotBlank(message = "Cannot be blank!") String email,
                           @NotBlank(message = "Cannot be blank!") String password) implements Serializable {
}
