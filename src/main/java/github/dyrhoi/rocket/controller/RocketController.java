package github.dyrhoi.rocket.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String user() {
        return "Only users can access this resource.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String secret() {
        return "Only Admins can access this resource.";
    }
}
