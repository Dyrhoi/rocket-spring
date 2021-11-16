package github.dyrhoi.rocket.execption;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationExecption extends ResponseStatusException {

    public ValidationExecption(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
