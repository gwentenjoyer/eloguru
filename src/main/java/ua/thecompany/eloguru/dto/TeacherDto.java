package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.EnumeratedRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public record TeacherDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active, String fullname,
                         String email, EnumeratedRole role, String country, String phone,
                         ArrayList<Integer> teachingCoursesId) implements Serializable {}

