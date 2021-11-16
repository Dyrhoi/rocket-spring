package github.dyrhoi.rocket.dto;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String email;
    private String username;
    private String password;
    private String passwordConfirm;
}
