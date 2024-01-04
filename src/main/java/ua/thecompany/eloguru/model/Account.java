package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="accounts")
public class Account extends BaseEntity implements UserDetails {

    @Column(name="fullname")
    private String fullname;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="pwhash")
    private String pwhash;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private EnumeratedRole role = EnumeratedRole.STUDENT;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return pwhash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive();
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

    @Column(name="country", nullable = true)
    private String country;

    @Column(name="phone", nullable = true)
    private String phone;

    @OneToOne(mappedBy = "account")
    private Token token;
}
