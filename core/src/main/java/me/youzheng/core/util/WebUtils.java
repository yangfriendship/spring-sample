package me.youzheng.core.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@UtilityClass
@Slf4j
public class WebUtils {

    public static final String DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

    public static void setDefaultContentType(final ServletResponse response) {
        setContentType(response, DEFAULT_CONTENT_TYPE);
    }

    /**
     * Response 객체에 ContentType 을 설정
     * @param response Response 객체
     * @param mediaType 적용할 MediaType
     */
    public static void setContentType(final ServletResponse response, final String mediaType) {
        response.setContentType(mediaType);
    }

    /**
     * Response 내부에 있는 OutputStream 이 committed 되지 않았다면 stream 을 초기화
     *
     * @param response 버퍼를 초기화할 Response
     */
    public static void resetBuffer(final ServletResponse response) {
        if (!response.isCommitted()) {
            response.resetBuffer();
        }
    }

    /**
     * RequestContextHolder 에서 현재 스레드의 Request 를 가져옴
     */
    public static Optional<HttpServletRequest> getCurrentRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(WebUtils::isServletRequestAttributes)
                .map(WebUtils::convertToServletRequestAttributes)
                .map(ServletRequestAttributes::getRequest);
    }

    private static ServletRequestAttributes convertToServletRequestAttributes(final RequestAttributes requestAttributes) {
        return (ServletRequestAttributes) requestAttributes;
    }

    private static boolean isServletRequestAttributes(Object source) {
        return source instanceof ServletRequestAttributes;
    }

}