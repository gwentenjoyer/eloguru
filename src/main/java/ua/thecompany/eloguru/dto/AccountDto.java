package ua.thecompany.eloguru.dto;

import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.model.Account;

import java.io.Serializable;
import java.time.LocalDateTime;

public record AccountDto(Long id, LocalDateTime createdDate, LocalDateTime lastModifiedDate, boolean active, String fullname,
                         String email, String pwhash, EnumeratedRole role, String country, String phone)
        implements Serializable {}
