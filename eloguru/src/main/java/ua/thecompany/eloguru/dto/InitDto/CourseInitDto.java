package ua.thecompany.eloguru.dto.InitDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import ua.thecompany.eloguru.model.EnumeratedCategories;

import java.io.Serializable;
import java.util.Date;

public record CourseInitDto(@NotBlank(message = "Cannot be blank!") String header,
                            EnumeratedCategories categories,
                            String description,
                            @Positive Integer durationDays,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
                            @Nullable MultipartFile photo) implements Serializable {}
