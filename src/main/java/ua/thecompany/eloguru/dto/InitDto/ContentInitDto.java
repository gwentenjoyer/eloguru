package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record ContentInitDto(@NotBlank String label,
                             @NotBlank String essence,
                             String type) implements Serializable {
}
