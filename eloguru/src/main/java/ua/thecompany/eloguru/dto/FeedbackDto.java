package ua.thecompany.eloguru.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record FeedbackDto(Long studentId, Long feedbackId, LocalDateTime createdDate,
                          Integer rating, String text, Long courseId, String fullname) implements Serializable {
}
