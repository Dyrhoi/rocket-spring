package github.dyrhoi.rocket;

import github.dyrhoi.rocket.model.Role;
import github.dyrhoi.rocket.model.User;
import github.dyrhoi.rocket.repository.UserRepository;
import github.dyrhoi.rocket.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"github.dyrhoi.rocket.repository"})
@EntityScan("github.dyrhoi.rocket.model")
public class RocketApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired RoleService roleService;

    @PostConstruct
    public void initUsers() {
            List<User> users = Stream.of(
                    User.builder()
                            .authorities(roleService.getAllOrCreate("USER"))
                            .username("user")
                            .password("password")
                            .withEncoder(passwordEncoder)
                            .email("user@test.com")
                            .build(),
                    User.builder()
                            .authorities(roleService.getAllOrCreate("USER", "ADMIN"))
                            .username("admin")
                            .password("password")
                            .withEncoder(passwordEncoder)
                            .email("admin@test.com")
                            .build()
            ).collect(Collectors.toList());

            userRepository.saveAll(users);
    }

    public static void main(String[] args) {
        SpringApplication.run(RocketApplication.class, args);
    }

}
