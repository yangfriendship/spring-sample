package me.youzheng.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadLoginRequestException extends BadRequestException {

    public static final String DEFAULT_MESSAGE = "아이디 또는 패스워드가 잘못되었습니다.";

    public BadLoginRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public BadLoginRequestException(final String message) {
        super(message);
    }

}
