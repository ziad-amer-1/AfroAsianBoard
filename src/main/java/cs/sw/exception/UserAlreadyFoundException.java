package cs.sw.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserAlreadyFoundException extends RuntimeException {
    public UserAlreadyFoundException(String message) {
        super(message);
    }
}
