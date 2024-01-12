package ua.thecompany.eloguru.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tokens")
public class Token extends BaseEntity {

    @Column(unique = true)
    public String token;

    @OneToOne
    @JoinColumn(name = "account")
    public Account account;
}
