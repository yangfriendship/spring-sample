package me.youzheng.core.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotationHttpStatusResolverTest {

    AnnotationHttpStatusResolver annotationHttpStatusResolver;

    @BeforeEach
    void setUp() {
        this.annotationHttpStatusResolver = new AnnotationHttpStatusResolver();
    }



    @ParameterizedTest(name = "예외 클래스에 명시된 HttpStatus 추출 테스트. {0} 예외에서 {1} 를 얻어야한다.")
    @MethodSource("me.youzheng.core.configure.security.exception.TestException#exceptionMethodSources")
    void 예외클래스_상태코드_추출_테스트(final Exception exception, final HttpStatus expected) {
        final HttpStatus result = this.annotationHttpStatusResolver.resolve(exception);
        assertThat(result).isEqualTo(expected);
    }

}