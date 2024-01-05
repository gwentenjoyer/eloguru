package ua.thecompany.eloguru.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ContentDto(Long id, LocalDateTime lastModifiedDate,
                         String label, String essence, String type) implements Serializable {
}
