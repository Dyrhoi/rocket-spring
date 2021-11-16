package github.dyrhoi.rocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @Email
    @Column(unique = true)
    private String email;

    @Size(min = 3, max=16)
    @Column(unique = true)
    private String username;

    @NotEmpty
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = {
                @JoinColumn(name = "USER_ID")
        },
        inverseJoinColumns = {
                @JoinColumn(name = "ROLE_ID")
        }
    )
    private Set<Role> authorities;
}
