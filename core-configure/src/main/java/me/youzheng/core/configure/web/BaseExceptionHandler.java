package me.youzheng.core.configure.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.youzheng.core.exception.BaseException;
import me.youzheng.core.web.ApiError;
import me.youzheng.core.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * DispatcherServlet 에서 발생한 예외를 처리하는 기본 ExceptionHandler.
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class BaseExceptionHandler {

    private final ExceptionHandlerService exceptionHandlerService;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBaseException(final Exception exception) {
        final HttpStatus httpStatus = this.exceptionHandlerService.findHttpStatus(exception);
        return ResponseEntity.status(httpStatus)
                .body(createBody(exception));
    }

    /**
     * HttpMessageConverter 에서 발생한 HttpMessageNotWritableException 를 처리한다. HttpServletResponse 의 OutputStream 을
     * 초기화한 후 실패 메시지를 작성함. Response 가 Committed 상태라면 비정상적인 JSON 형태로 출력됨.
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<ApiResponse<?>> handlerHttpMessageNotWritableException(final HttpMessageNotWritableException exception, final HttpServletResponse response) {
        if (!response.isCommitted()) {
            response.resetBuffer();
        } else {
            log.warn("Response buffer already committed!!");
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createBody(exception));
    }

    protected ApiResponse<?> createBody(final Exception exception) {
        final ApiError apiError = ApiError.builder()
                .code(this.exceptionHandlerService.findErrorCode(exception))
                .message(exception.getMessage())
                .build();
        return ApiResponse.fail(apiError);
    }

}