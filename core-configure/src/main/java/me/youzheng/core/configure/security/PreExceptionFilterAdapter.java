package me.youzheng.core.configure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.configure.security.filter.PreExceptionFilter;
import me.youzheng.core.security.AnnotationHttpStatusResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FilterChainProxy 에서 발생하는 예외를 처리하기 위한 필터를 등록 ({@link PreExceptionFilter})
 */
public class PreExceptionFilterAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private ApplicationContext applicationContext;

    private Map<Class<? extends Throwable>, ExceptionHandler> exceptionHandlers;


    protected PreExceptionFilterAdapter() {
    }

    public static PreExceptionFilterAdapter create() {
        return new PreExceptionFilterAdapter();
    }

    @Override
    public void init(final HttpSecurity http) {
        this.applicationContext = http.getSharedObject(ApplicationContext.class);

        http.addFilterAfter(new PreExceptionFilter(this.createExceptionHandlerComposite())
                , DisableEncodeUrlFilter.class);
    }

    public PreExceptionFilterAdapter addExceptionHandler(ExceptionHandler... exceptionHandlers) {
        Assert.notNull(exceptionHandlers, () -> "ExceptionHandler can't be null");
        if (this.exceptionHandlers == null) {
            this.exceptionHandlers = new HashMap<>();
        }
        for (final ExceptionHandler exceptionHandler : exceptionHandlers) {
            this.exceptionHandlers.put(exceptionHandler.getType(), exceptionHandler);
        }
        return this;
    }

    protected ExceptionHandler createExceptionHandlerComposite() {
        if (!CollectionUtils.isEmpty(this.exceptionHandlers)) {
            return new ExceptionHandlerComposite(new ArrayList<>(exceptionHandlers.values()));
        }

        final Map<String, ExceptionHandler> exceptionHandlers = this.applicationContext.getBeansOfType(ExceptionHandler.class);
        if (exceptionHandlers.isEmpty()) {
            this.putDefaultExceptionHandler(exceptionHandlers);
        }
        return new ExceptionHandlerComposite(new ArrayList<>(exceptionHandlers.values()));
    }

    private void putDefaultExceptionHandler(final Map<String, ExceptionHandler> exceptionHandlers) {
        exceptionHandlers.put("defaultExceptionHandler", new DefaultExceptionHandler(new AnnotationHttpStatusResolver()
                , this.applicationContext.getBean(ObjectMapper.class)));
    }

}