package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

public record FeedbackInitDto(@NotNull @Max(5) @Min(1) Integer rating, @NotNull String text,
                              @NotNull(message = "Feedback should have course reference!") Long courseId) implements Serializable {
}
