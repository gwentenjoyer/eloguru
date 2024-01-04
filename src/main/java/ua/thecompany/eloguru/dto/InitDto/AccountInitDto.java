package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AccountInitDto(@NotBlank(message = "Cannot be blank!") String email,
                             @NotBlank(message = "Cannot be blank!") String password,
                             @NotBlank(message = "Cannot be blank!") String phone,
                             String country, String fullname) implements Serializable {}
