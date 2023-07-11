package me.youzheng.core.configure.web;

import me.youzheng.core.exception.ApiErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorCodeExceptionHandlerServiceTest {

    ExceptionHandlerService exceptionHandlerService;

    @ResponseStatus(HttpStatus.CONFLICT)
    static class HasResponseStatusException extends RuntimeException {

    }

    @ResponseStatus
    static class DefaultResponseStatusException extends RuntimeException {

    }

    static class NoAnnotationException extends RuntimeException {

    }

    @ApiErrorCode(code = "ERROR0002")
    static class HasApiErrorCodeAnnotationException extends RuntimeException {

    }

    @BeforeEach
    void setUp() {
        this.exceptionHandlerService = new ApiErrorCodeExceptionHandlerService();
    }

    @DisplayName("@ResponseException 예외 추룰 테스트")
    @Test
    void 예외_클래스에서_ResponseException_추출_테스트() {
        assertThat(this.exceptionHandlerService.findHttpStatus(new HasResponseStatusException()))
                .describedAs("@ResponseStatus 에 정의된 'HttpStatus.CONFLICT' 를 반환해야한다.")
                .isEqualTo(HttpStatus.CONFLICT);

        assertThat(this.exceptionHandlerService.findHttpStatus(new DefaultResponseStatusException()))
                .describedAs("@ResponseStatus 의 기본 HttpStatus 를 반환해야한다.")
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(this.exceptionHandlerService.findHttpStatus(new NoAnnotationException()))
                .describedAs("@ResponseStatus 이 없기 때문에 기본 HttpStatus '%s'를 반환해야한다.", ApiErrorCodeExceptionHandlerService.DEFAULT_HTTP_STATUS)
                .isEqualTo(ApiErrorCodeExceptionHandlerService.DEFAULT_HTTP_STATUS);
//                .isEqualTo(HttpStatus.CONFLICT);
    }

    @DisplayName("@ApiErrorCode 코드 추출 테스트")
    @Test
    void 예외_클래스에서_ApiErrorCode_어노테이션_추출_테스트() {
        assertThat(this.exceptionHandlerService.findErrorCode(new HasApiErrorCodeAnnotationException()))
                .describedAs("@ApiErrorCode 에 정의된 code 값을 가져와야한다.")
                .isEqualTo("ERROR0002");

        assertThat(this.exceptionHandlerService.findErrorCode(new NoAnnotationException()))
                .describedAs("@ApiErrorCode 가 없기 때문에 기본값 '%s' 을 가져와야한다", ApiErrorCodeExceptionHandlerService.DEFAULT_ERROR_CODE)
                .isEqualTo(ApiErrorCodeExceptionHandlerService.DEFAULT_ERROR_CODE);
    }

}