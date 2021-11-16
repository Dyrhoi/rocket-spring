package github.dyrhoi.rocket.service;

import github.dyrhoi.rocket.model.User;
import github.dyrhoi.rocket.repository.UserRepository;
import github.dyrhoi.rocket.security.UserPrincipalImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        return new UserPrincipalImpl(user);
    }

}
