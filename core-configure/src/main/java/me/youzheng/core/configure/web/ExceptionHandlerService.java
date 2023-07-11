package me.youzheng.core.configure.web;

import org.springframework.http.HttpStatus;

public interface ExceptionHandlerService {

    HttpStatus findHttpStatus(final Exception exception);

    String findErrorCode(final Exception exception);

}
