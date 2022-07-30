package me.youzheng.springsecurity.security.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.youzheng.springsecurity.security.principal.JwtResult;
import me.youzheng.springsecurity.security.util.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    private final JwtProvider jwtProvider;
    private final String tokenExpiredMessage;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.tokenExpiredMessage = objectMapper.writeValueAsString(
                ImmutableMap.of("message", "만료된 토큰입니다."));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        final String value = this.extractToken(request);
        if (!checkIsBearer(value)) {
            chain.doFilter(request, response);
            return;
        }

        String token = value.substring(TOKEN_PREFIX.length());
        JwtResult jwtResult = this.jwtProvider.validate(token);
        if (jwtResult == JwtResult.VALID) {
            Authentication authentication = this.jwtProvider.resolveToken(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (jwtResult == JwtResult.EXPIRED) {
            bindingExceptionResult(request, response);
        }
        chain.doFilter(request, response);
    }

    private boolean checkIsBearer(String token) {
        return !(token == null || token.equals("undefined") || !token.startsWith(TOKEN_PREFIX));
    }

    private void bindingExceptionResult(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            response.getWriter().write(this.tokenExpiredMessage);
        } catch (IOException e) {
            log.debug("TokenAuthenticationFilter.bindingExceptionResult has error");
            // do not something
        }
    }

    protected String extractToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }

}