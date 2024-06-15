package ua.thecompany.eloguru.model;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken extends BaseEntity {

    @Column(unique = true)
    private String RefreshToken;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
