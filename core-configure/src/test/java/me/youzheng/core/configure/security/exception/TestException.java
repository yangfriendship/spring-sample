package me.youzheng.core.configure.security.exception;

import org.junit.jupiter.params.provider.Arguments;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Stream;

public class TestException {

    public static class DontHaveResponseStatusException extends RuntimeException {
        public DontHaveResponseStatusException() {
            super();
        }

        public DontHaveResponseStatusException(final String message) {
            super(message);
        }
    }

    @ResponseStatus
    public static class DontHaveSpecifiedStatusException extends RuntimeException {

        public DontHaveSpecifiedStatusException() {
            super();
        }

        public DontHaveSpecifiedStatusException(final String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class HaveBadRequestStatusException extends RuntimeException {

        public HaveBadRequestStatusException() {
            super();
        }

        public HaveBadRequestStatusException(final String message) {
            super(message);
        }

    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class HaveUnauthorizedStatusException extends RuntimeException {
        public HaveUnauthorizedStatusException() {
            super();
        }

        public HaveUnauthorizedStatusException(final String message) {
            super(message);
        }
    }

    public static Stream<Arguments> exceptionMethodSources() {
        return Stream.of(
                Arguments.of(new DontHaveResponseStatusException("서버 문제 발생"), HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제 발생")
                , Arguments.of(new DontHaveSpecifiedStatusException("서버 문제 발생2"), HttpStatus.INTERNAL_SERVER_ERROR, "서버 문제 발생2")
                , Arguments.of(new HaveUnauthorizedStatusException("인증 실패"), HttpStatus.UNAUTHORIZED, "인증 실패")
                , Arguments.of(new HaveBadRequestStatusException("잘못된 요청"), HttpStatus.BAD_REQUEST, "잘못된 요청")
        );
    }

}