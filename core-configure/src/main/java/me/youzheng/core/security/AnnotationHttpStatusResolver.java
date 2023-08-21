package me.youzheng.core.security;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Exception 의 정의된 {@link ResponseStatus} 을 기준으로 HttpStatus 를 생성.
 */
public class AnnotationHttpStatusResolver implements ExceptionHttpStatusResolver {

    private static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    private final Map<Class<? extends Exception>, HttpStatus> httpStatusCache = new ConcurrentHashMap<>();

    @Override
    public HttpStatus resolve(final Exception exception) {
        this.httpStatusCache.computeIfAbsent(exception.getClass(), this::getHttpStatus);
        return this.httpStatusCache.get(exception.getClass());
    }

    /**
     * Exception Class 의 {@link ResponseStatus} 에 정의되어있는 HttpStatus 를 반환함. 설정된 값이 없다면 INTERNAL_SERVER_ERROR(500)
     * 을 기본값으로 반환함.
     */
    private HttpStatus getHttpStatus(final Class<? extends Exception> clazz) {
        return Optional.ofNullable(AnnotationUtils.findAnnotation(clazz, ResponseStatus.class))
                .map(ResponseStatus::code)
                .orElse(DEFAULT_HTTP_STATUS);
    }

}