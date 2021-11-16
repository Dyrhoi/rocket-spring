package github.dyrhoi.rocket.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rocket")
public class RocketController {
    @GetMapping
    public String rocket() {
        return "Hello";
    }

    @GetMapping("/secret")
    @PreAuthorize("hasAuthority('USER')")
    public String secret() {
        return "a secret";
    }
}
