package me.youzheng.core.domain.linstner;

import me.youzheng.core.domain.security.UserContext;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            return Optional.empty();
        }
        final Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        final Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserContext)) {
            return Optional.empty();
        }
        final UserContext userContext = (UserContext) principal;
        return Optional.ofNullable(userContext.getUserId());
    }

}