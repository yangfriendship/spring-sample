package me.youzheng.core.domain.linstner;

import me.youzheng.core.security.UserContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(this::isUserContext)
                .map(this::convertToUserContext)
                .map(this::getUserId);
    }

    private UserContext convertToUserContext(final Object source) {
        return (UserContext) source;
    }

    private Long getUserId(final UserContext userContext) {
        return userContext.getUserId();
    }

    private boolean isUserContext(final Object source) {
        return source instanceof UserContext;
    }

}