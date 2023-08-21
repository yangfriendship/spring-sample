package me.youzheng.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationHttpStatusResolverTest {

    AnnotationHttpStatusResolver annotationHttpStatusResolver;

    @BeforeEach
    void setUp() {
        this.annotationHttpStatusResolver = new AnnotationHttpStatusResolver();
    }

    static class DontHaveResponseStatusException extends RuntimeException {

    }

    @ResponseStatus
    static class DontHaveSpecifiedStatusException extends RuntimeException {

    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    static class HaveUnauthorizedStatusException extends RuntimeException {

    }

    @ParameterizedTest(name = "예외 클래스에 명시된 HttpStatus 추출 테스트. {0} 예외에서 {1} 를 얻어야한다.")
    @MethodSource("exceptionMethodSources")
    void 예외클래스_상태코드_추출_테스트(final Exception exception, final HttpStatus expected) {
        final HttpStatus result = this.annotationHttpStatusResolver.resolve(exception);
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> exceptionMethodSources() {
        return Stream.of(
                Arguments.of(new DontHaveResponseStatusException(), HttpStatus.INTERNAL_SERVER_ERROR)
                , Arguments.of(new DontHaveSpecifiedStatusException(), HttpStatus.INTERNAL_SERVER_ERROR)
                , Arguments.of(new HaveUnauthorizedStatusException(), HttpStatus.UNAUTHORIZED)
        );
    }

}