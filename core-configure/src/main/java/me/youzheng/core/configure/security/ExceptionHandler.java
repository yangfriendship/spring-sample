package me.youzheng.core.configure.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ExceptionHandler {

    void handle(final ServletRequest request, final ServletResponse response, final Exception exception);

    default boolean support(Exception exception) {
        return getType().isAssignableFrom(exception.getClass());
    }

    Class<? extends Throwable> getType();

}
