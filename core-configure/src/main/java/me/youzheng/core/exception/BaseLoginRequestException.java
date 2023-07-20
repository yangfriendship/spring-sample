package me.youzheng.core.exception;

public class BaseLoginRequestException extends BadRequestException {

    public static final String DEFAULT_MESSAGE = "아이디 또는 패스워드가 잘못되었습니다.";

    public BaseLoginRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public BaseLoginRequestException(final String message) {
        super(message);
    }

}
