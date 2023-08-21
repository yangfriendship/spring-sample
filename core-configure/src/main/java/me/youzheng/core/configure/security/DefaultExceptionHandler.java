package me.youzheng.core.configure.security;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.code.ErrorCode;
import me.youzheng.core.security.ExceptionHttpStatusResolver;
import me.youzheng.core.util.WebUtils;
import me.youzheng.core.web.ApiError;
import me.youzheng.core.web.ApiResponse;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DefaultExceptionHandler implements ExceptionHandler {

    private final ExceptionHttpStatusResolver exceptionHttpStatusResolver;
    private final ObjectMapper objectMapper;

    public DefaultExceptionHandler(final ExceptionHttpStatusResolver exceptionHttpStatusResolver, final ObjectMapper objectMapper) {
        this.exceptionHttpStatusResolver = exceptionHttpStatusResolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(final ServletRequest request, final ServletResponse response, final Exception exception) {
        handleInternal((HttpServletRequest) request, (HttpServletResponse) response, exception);
    }

    private void handleInternal(final HttpServletRequest request, final HttpServletResponse response, final Exception exception) {
        final HttpStatus httpStatus = this.exceptionHttpStatusResolver.resolve(exception);
        response.setStatus(httpStatus.value());

        try {
            final ApiResponse<?> apiResponse = createApiResponse(exception);
            final JsonGenerator jsonGenerator = this.objectMapper.getFactory().createGenerator(response.getOutputStream(), JsonEncoding.UTF8);
            this.objectMapper.writer().writeValue(jsonGenerator, apiResponse);
        } catch (IOException ioException) {
            writeInternalServerError(response, ioException);
        }
    }
    
    private void writeInternalServerError(final HttpServletResponse response, final IOException ioException) {
        WebUtils.resetBuffer(response);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ioException.printStackTrace();
    }

    private ApiResponse<?> createApiResponse(final Exception exception) {
        final ApiError apiError = this.createApiError(exception);
        return ApiResponse.of(false, null, apiError);
    }

    private ApiError createApiError(final Exception exception) {
        return ApiError.builder()
                .message(exception.getMessage())
                .code(ErrorCode.FAIL.getCode())
                .build();
    }

    /**
     * {@link DefaultExceptionHandler} 는 모든 예외 클래스에 대해 처리하기 때문에 {@link ExceptionHandlerComposite} 에 추가한다면
     * List 에 가장 뒤에 위치하게 하여 사용한다.
     */
    @Override
    public Class<? extends Throwable> getType() {
        return Exception.class;
    }

}