package github.dyrhoi.rocket.controller;

import github.dyrhoi.rocket.dto.PrivateUserDto;
import github.dyrhoi.rocket.dto.RegisterUserDto;
import github.dyrhoi.rocket.execption.ValidationExecption;
import github.dyrhoi.rocket.model.User;
import github.dyrhoi.rocket.repository.UserRepository;
import github.dyrhoi.rocket.security.JwtHelper;
import github.dyrhoi.rocket.security.LoginResult;
import github.dyrhoi.rocket.security.SecurityConfig;
import github.dyrhoi.rocket.security.UserPrincipalImpl;
import github.dyrhoi.rocket.service.RoleService;
import github.dyrhoi.rocket.service.UserDetailsServiceImpl;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired UserRepository userRepository;
    @Autowired RoleService roleService;
    @Autowired ModelMapper modelMapper;

    public AuthController(JwtHelper jwtHelper, UserDetailsServiceImpl userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public LoginResult login(@RequestBody LoginResult.LoginDto login) {

        System.out.println(login.getUsername() + " " + login.getPassword());

        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(login.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (passwordEncoder.matches(login.getPassword(), userDetails.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", login.getUsername());

            String authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));
            claims.put(SecurityConfig.AUTHORITIES_CLAIM_NAME, authorities);
            claims.put("userId", String.valueOf(1));

            String jwt = jwtHelper.createJwtForClaims(login.getUsername(), claims);
            System.out.println(jwt);
            return new LoginResult(jwt);
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }

    @PostMapping("/register")
    public LoginResult register(@RequestBody RegisterUserDto user) {
        try {
            if(!user.getPassword().equals(user.getPasswordConfirm())) {
                throw new ValidationExecption("Passwords do not match.");
            }

            userRepository.save(
                    User.builder()
                            .authorities(roleService.getAllOrCreate("USER"))
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .withEncoder(passwordEncoder)
                            .email(user.getEmail())
                            .build()
            );

            return login(new LoginResult.LoginDto(user.getUsername(), user.getPassword()));
        } catch (HibernateException e) {
            throw new ValidationExecption("Username or Email is already in use");
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PrivateUserDto> getAuthenticatedUser(Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName());
        return new ResponseEntity<>(modelMapper.map(user, PrivateUserDto.class), HttpStatus.OK);

    }
}