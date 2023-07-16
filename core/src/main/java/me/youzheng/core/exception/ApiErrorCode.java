package me.youzheng.core.exception;

import me.youzheng.core.code.ErrorCode;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiErrorCode {

    ErrorCode DEFAULT_ERROR_CODE = ErrorCode.FAIL;

    ErrorCode code() default ErrorCode.FAIL;

}