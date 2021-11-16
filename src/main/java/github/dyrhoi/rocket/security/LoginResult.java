package github.dyrhoi.rocket.security;

import lombok.*;

@Data
@RequiredArgsConstructor
public class LoginResult {

    @NonNull
    private String token;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class LoginDto {
        private String username;
        private String password;
    }
}
