package me.youzheng.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.domain.user.entity.User;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class SecurityMixinModules {

    public static void addMixin(final Jackson2ObjectMapperBuilder builder) {
        builder.mixIn(UserContext.class, UserContextMixin.class)
                .mixIn(AuthenticatedUser.class, AuthenticatedUserMixin.class)
                .mixIn(User.class, UserMixin.class);
    }

    public static void addMixin(final ObjectMapper objectMapper) {
        objectMapper.addMixIn(UserContext.class, UserContextMixin.class)
                .addMixIn(AuthenticatedUser.class, AuthenticatedUserMixin.class)
                .addMixIn(User.class, UserMixin.class);
    }

}