package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.EnumeratedRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public record StudentDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active, String fullname,
                         String email, String pwhash, EnumeratedRole role, String country, String phone,
                         ArrayList<CourseDto> courses) implements Serializable {}
