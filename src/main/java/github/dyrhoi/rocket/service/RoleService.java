package github.dyrhoi.rocket.service;

import github.dyrhoi.rocket.model.Role;
import github.dyrhoi.rocket.model.User;
import github.dyrhoi.rocket.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleService {
    @Autowired RoleRepository roleRepository;

    public Set<Role> getAllOrCreate(String... roles) {
        Set<Role> _roles = Stream.of(roles).map(role -> roleRepository.findRoleByName(role).orElse(new Role(role))).collect(Collectors.toSet());
        roleRepository.saveAll(_roles);

        System.out.println(_roles);
        return _roles;
    }
}
