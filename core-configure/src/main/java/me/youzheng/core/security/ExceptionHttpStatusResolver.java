package me.youzheng.core.security;

import org.springframework.http.HttpStatus;

/**
 * Exception 에 해당하는 HttpStatus 을 생성
 */
public interface ExceptionHttpStatusResolver {

    HttpStatus resolve(final Exception exception);

}
