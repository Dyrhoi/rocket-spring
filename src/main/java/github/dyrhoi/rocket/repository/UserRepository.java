package github.dyrhoi.rocket.repository;

import github.dyrhoi.rocket.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String s);
}
