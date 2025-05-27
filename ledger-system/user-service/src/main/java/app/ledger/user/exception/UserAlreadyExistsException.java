package app.ledger.user.exception;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden // Swagger 문서에는 노출되지 않음
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
