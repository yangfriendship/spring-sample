package me.youzheng.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TokenParsingFailException extends InternalAuthenticationServiceException {

    public TokenParsingFailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TokenParsingFailException(final String message) {
        super(message);
    }

}