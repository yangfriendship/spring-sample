package me.youzheng.core.configure.web;

import lombok.RequiredArgsConstructor;
import me.youzheng.core.exception.ApiErrorCode;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.Annotation;
import java.util.Optional;

@RequiredArgsConstructor
public class ApiErrorCodeExceptionHandlerService implements ExceptionHandlerService {

    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.BAD_REQUEST;
    public static final String DEFAULT_ERROR_CODE = ApiErrorCode.DEFAULT_ERROR_CODE;

    @Override
    public HttpStatus findHttpStatus(final Exception exception) {
        return Optional.ofNullable(exception)
                .map(this::findResponseStatus)
                .map(this::getHttpStatus)
                .orElse(DEFAULT_HTTP_STATUS);
    }

    @Override
    public String findErrorCode(final Exception exception) {
        return Optional.ofNullable(exception)
                .map(this::findApiErrorCode)
                .map(this::getErrorCode)
                .orElse(DEFAULT_ERROR_CODE);
    }

    private <A extends Annotation> A findAnnotation(final Exception exception, final Class<A> target) {
        return AnnotationUtils.findAnnotation(exception.getClass(), target);
    }

    private ResponseStatus findResponseStatus(final Exception exception) {
        return this.findAnnotation(exception, ResponseStatus.class);
    }

    private HttpStatus getHttpStatus(final ResponseStatus responseStatus) {
        return responseStatus.code();
    }

    private ApiErrorCode findApiErrorCode(final Exception exception) {
        return this.findAnnotation(exception, ApiErrorCode.class);
    }

    private String getErrorCode(final ApiErrorCode apiErrorCode) {
        return apiErrorCode.code();
    }
}