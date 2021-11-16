package github.dyrhoi.rocket.repository;

import github.dyrhoi.rocket.model.Role;
import github.dyrhoi.rocket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(String s);
}
