package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import ua.thecompany.eloguru.model.EnumeratedCategories;

import java.io.Serializable;
import java.util.Date;

public record CourseInitDto(@NotBlank(message = "Cannot be blank!") String header,
                            EnumeratedCategories categories,
                            String description,
                            @Positive Integer durationDays,
                            Date startDate) implements Serializable {}
