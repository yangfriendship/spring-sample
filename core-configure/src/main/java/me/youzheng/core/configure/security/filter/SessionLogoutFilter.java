package me.youzheng.core.configure.security.filter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.web.ApiError;
import me.youzheng.core.web.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SessionLogoutFilter extends GenericFilterBean {

    private final AntPathRequestMatcher antPathRequestMatcher;

    private final String sessionCookieName;

    private final SessionRegistry sessionRegistry;

    private final ObjectMapper objectMapper;

    private static final Map<String, Object> LOGOUT_SUCCESS_MESSAGE = Map.of("message", "로그아웃되었습니다.");

    public SessionLogoutFilter(final AntPathRequestMatcher antPathRequestMatcher, final String sessionCookieName
            , final SessionRegistry sessionRegistry, final ObjectMapper objectMapper) {
        this.antPathRequestMatcher = antPathRequestMatcher;
        this.sessionCookieName = sessionCookieName;
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    public SessionLogoutFilter(final String pattern, final String sessionCookieName, final SessionRegistry sessionRegistry, final ObjectMapper objectMapper) {
        this.antPathRequestMatcher = new AntPathRequestMatcher(pattern);
        this.sessionCookieName = sessionCookieName;
        this.sessionRegistry = sessionRegistry;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws ServletException, IOException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain) throws ServletException, IOException {
        if (!isLogoutRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        final String sessionId = request.getRequestedSessionId();
        if (!StringUtils.hasLength(sessionId)) {
            writeBadRequest(response);
            return;
        }

        this.sessionRegistry.removeSessionInformation(sessionId);
        final Cookie emptyCookie = createEmptyCookie();
        response.addCookie(emptyCookie);
        writeSuccessMessage(response);
    }

    private void writeBadRequest(final HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        final JsonGenerator jsonGenerator = this.objectMapper.createGenerator(response.getOutputStream());
        final ApiError apiError = ApiError.builder()
                .code("ERROR0001")
                .message("잘못된 요청입니다.").build();
        this.objectMapper.writer().writeValue(jsonGenerator, ApiResponse.fail(apiError));
    }

    private void writeSuccessMessage(final HttpServletResponse response) throws IOException {
        final JsonGenerator jsonGenerator = this.objectMapper.createGenerator(response.getOutputStream());
        this.objectMapper.writer().writeValue(jsonGenerator, ApiResponse.success(LOGOUT_SUCCESS_MESSAGE));
    }

    /**
     * 만료일자 및 쿠키 정보 초기화된 세션 쿠키를 생성
     */
    private Cookie createEmptyCookie() {
        final Cookie cookie = new Cookie(this.sessionCookieName, "");
        cookie.setMaxAge(0);
        return cookie;
    }

    private boolean isLogoutRequest(final HttpServletRequest request) {
        return this.antPathRequestMatcher.matcher(request).isMatch();
    }

}
