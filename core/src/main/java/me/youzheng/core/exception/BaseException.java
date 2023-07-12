package me.youzheng.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseException extends RuntimeException {

    public BaseException(final String message) {
        super(message);
    }

    public BaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BaseException(final Throwable cause) {
        super(cause);
    }

    public BaseException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseException() {
    }

}