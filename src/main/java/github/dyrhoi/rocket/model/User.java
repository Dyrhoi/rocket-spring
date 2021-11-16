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
    @Column(unique = true, length = 20)
    private String email;

    @Size(min = 3, max=16)
    @Column(unique = true, length = 20)
    private String username;

    @NotEmpty
    @JsonIgnore
    @ToString.Exclude
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public static class UserBuilder {
        public UserBuilder withEncoder(PasswordEncoder encoder) {
            this.password = encoder.encode(password);
            return this;
        }
    }
}
