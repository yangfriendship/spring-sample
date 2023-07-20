package me.youzheng.core.configure.security.handler;

import lombok.RequiredArgsConstructor;
import me.youzheng.core.configure.security.ExceptionHandler;
import me.youzheng.core.util.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ApiAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ExceptionHandler exceptionHandler;

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException {
        WebUtils.setDefaultContentType(response);
        exceptionHandler.handle(request, response, exception);
    }

}