package me.youzheng.core.configure.security;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.code.ErrorCode;
import me.youzheng.core.security.ExceptionHttpStatusResolver;
import me.youzheng.core.web.ApiError;
import me.youzheng.core.web.ApiResponse;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DefaultExceptionHandler implements ExceptionHandler {

    private final ExceptionHttpStatusResolver exceptionHttpStatusResolver;
    private final ObjectMapper objectMapper;

    public DefaultExceptionHandler(final ExceptionHttpStatusResolver exceptionHttpStatusResolver, final ObjectMapper objectMapper) {
        this.exceptionHttpStatusResolver = exceptionHttpStatusResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(final ServletRequest request, final ServletResponse response, final Exception exception) throws IOException {
        handleInternal((HttpServletRequest) request, (HttpServletResponse) response, exception);
    }

    private void handleInternal(final HttpServletRequest request, final HttpServletResponse response, final Exception exception) throws IOException {
        final HttpStatus httpStatus = this.exceptionHttpStatusResolver.resolve(exception);
        response.setStatus(httpStatus.value());

        final ApiError apiError = ApiError.builder()
                .message(exception.getMessage())
                .code(ErrorCode.FAIL.getCode())
                .build();

        final ApiResponse<?> apiResponse = ApiResponse.of(false, null, apiError);
        final JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
        this.objectMapper.writer().writeValue(jsonGenerator, apiResponse);
    }

    @Override
    public Class<? extends Throwable> getType() {
        return Exception.class;
    }

}