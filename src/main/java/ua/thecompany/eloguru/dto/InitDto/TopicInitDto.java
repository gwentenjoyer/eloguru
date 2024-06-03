package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record TopicInitDto(@NotBlank(message = "Cannot be blank!") String label,
                           @NotNull Long courseId,
                           String description) implements Serializable {}
