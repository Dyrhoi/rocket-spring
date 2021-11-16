package github.dyrhoi.rocket.controller;

import github.dyrhoi.rocket.security.JwtHelper;
import github.dyrhoi.rocket.security.LoginResult;
import github.dyrhoi.rocket.security.SecurityConfig;
import github.dyrhoi.rocket.service.UserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
public class AuthController {

    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

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
}