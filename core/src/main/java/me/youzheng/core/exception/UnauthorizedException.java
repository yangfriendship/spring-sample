package me.youzheng.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super("인증이 필요합니다.");
    }

    public UnauthorizedException(final String message) {
        super(message);
    }

    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(final Throwable cause) {
        super(cause);
    }

}