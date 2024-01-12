package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ContentInitDto(@NotBlank String label,
                             @NotBlank String essence,
                             @NotNull Long topicId,
                             String type) implements Serializable {
}
