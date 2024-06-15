package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record AccountInitDto(
//        @NotBlank(message = "Cannot be blank!") @Email
                             String email,
//                             @NotBlank(message = "Cannot be blank!")
                             String password,
                             String phone,
                             String country,
                             String fullname
) implements Serializable {}
