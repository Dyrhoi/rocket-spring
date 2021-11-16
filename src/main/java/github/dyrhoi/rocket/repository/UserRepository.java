package github.dyrhoi.rocket.repository;

import github.dyrhoi.rocket.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String s);
}
