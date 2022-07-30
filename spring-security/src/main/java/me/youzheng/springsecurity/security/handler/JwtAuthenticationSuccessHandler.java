package me.youzheng.springsecurity.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.security.principal.UserContext;
import me.youzheng.springsecurity.security.util.JwtProvider;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        String jwt = this.jwtProvider.createToken(authentication);
        Map<String, Object> result = new HashMap<>();
        result.put("token", jwt);

        UserContext currentUser = (UserContext) authentication.getPrincipal();
        result.put("user", currentUser.getUserInfo());
        response.getWriter().write(this.objectMapper.writeValueAsString(result));
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

}
