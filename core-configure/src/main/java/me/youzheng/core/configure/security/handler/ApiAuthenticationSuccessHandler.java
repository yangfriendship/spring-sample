package me.youzheng.core.configure.security.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.domain.user.entity.User;
import me.youzheng.core.exception.UnauthorizedException;
import me.youzheng.core.security.*;
import me.youzheng.core.util.WebUtils;
import me.youzheng.core.web.ApiResponse;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class ApiAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public ApiAuthenticationSuccessHandler() {
        this.objectMapper = new Jackson2ObjectMapperBuilder()
                .build();
        SecurityMixinModules.addMixin(this.objectMapper);
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        WebUtils.setDefaultContentType(response);

        if (!authentication.isAuthenticated() || !(authentication instanceof AuthenticatedUser)) {
            throw new UnauthorizedException();
        }
        final User user = unwrapUser(authentication);

        final JsonGenerator jsonGenerator = this.objectMapper.createGenerator(response.getOutputStream());
        this.objectMapper.writer().writeValue(jsonGenerator, ApiResponse.success(user));
    }

    private User unwrapUser(final Authentication authentication) {
        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication;
        final UserContext userContext = authenticatedUser.getUserContext();
        return userContext.getUser();
    }

}