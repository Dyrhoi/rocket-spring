package github.dyrhoi.rocket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import github.dyrhoi.rocket.repository.RoleRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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

    @ManyToMany
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

    @Component
    public static class UserBuilder {

        public UserBuilder withEncoder(PasswordEncoder encoder) {
            this.password = encoder.encode(password);
            return this;
        }
    }
}
