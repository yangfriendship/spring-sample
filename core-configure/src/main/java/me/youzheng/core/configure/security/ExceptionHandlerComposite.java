package me.youzheng.core.configure.security;

import org.springframework.core.ExceptionDepthComparator;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExceptionHandlerComposite implements ExceptionHandler {

    private final Map<Class<? extends Throwable>, ExceptionHandler> exceptionHandlerCache = new ConcurrentHashMap<>(128);

    public ExceptionHandlerComposite(final List<ExceptionHandler> exceptionHandlers) {
        for (final ExceptionHandler exceptionHandler : exceptionHandlers) {
            isAlreadyRegistered(exceptionHandler);
            this.exceptionHandlerCache.put(exceptionHandler.getType(), exceptionHandler);
        }
    }

    public ExceptionHandlerComposite(final Map<Class<? extends Throwable>, ExceptionHandler> exceptionHandlers) {
        this.exceptionHandlerCache.putAll(exceptionHandlers);
    }

    private void isAlreadyRegistered(final ExceptionHandler exceptionHandler) {
        if (this.exceptionHandlerCache.containsKey(exceptionHandler.getType())) {
            throw new IllegalArgumentException("이미 등록된 Exception 입니다. " + exceptionHandler.getType().getName());
        }
    }

    public void registerHandler(final ExceptionHandler handler) {
        isAlreadyRegistered(handler);
        this.exceptionHandlerCache.put(handler.getType(), handler);
    }

    @Override
    public void handle(final ServletRequest request, final ServletResponse response, final Exception exception) throws IOException {
        final ExceptionHandler exceptionHandler = this.getExceptionHandler(exception);
        if (exceptionHandler == null) {
            throw new NullPointerException(); // TODO create custom exception!!
        }
        exceptionHandler.handle(request, response, exception);
    }

    /**
     * @param exception 처리할 예외
     * @return 예외를 처리할 ExceptionHandler 구현체
     */
    private ExceptionHandler getExceptionHandler(final Exception exception) {
        ExceptionHandler exceptionHandler = this.exceptionHandlerCache.get(exception.getClass());
        if (exceptionHandler != null) {
            return exceptionHandler;
        }

        final List<Class<? extends Throwable>> candidateHandlers = this.getCandidateExceptions(exception);
        final Class<? extends Throwable> handleType = this.getHandleTypes(exception, candidateHandlers);
        this.exceptionHandlerCache.put(exception.getClass(), this.exceptionHandlerCache.get(handleType));
        return this.exceptionHandlerCache.get(exception.getClass());
    }

    private Class<? extends Throwable> getHandleTypes(final Exception exception, final List<Class<? extends Throwable>> matches) {
        matches.sort(new ExceptionDepthComparator(exception));
        return matches.get(0);
    }

    /**
     * 예외를 처리할 수 있는 핸들러 후보목록 반환
     *
     * @param exception 처리할 예외
     * @return 예외를 처리할 수 있는 ExceptionHandler 구현체 목록
     */
    private List<Class<? extends Throwable>> getCandidateExceptions(final Exception exception) {
        final List<Class<? extends Throwable>> matches = new ArrayList<>();
        for (Map.Entry<Class<? extends Throwable>, ExceptionHandler> entry : this.exceptionHandlerCache.entrySet()) {
            if (!entry.getValue().support(exception)) {
                continue;
            }
            matches.add(entry.getKey());
        }
        return matches;
    }

    @Override
    public Class<? extends Throwable> getType() {
        return Exception.class;
    }

}