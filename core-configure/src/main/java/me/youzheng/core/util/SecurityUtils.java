package me.youzheng.core.util;

import lombok.experimental.UtilityClass;
import me.youzheng.core.security.AuthenticatedUser;
import me.youzheng.core.security.UserContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Security 인증 관련 유틸 클래스.
 */
@UtilityClass
public class SecurityUtils {

    private static SecurityUtilStrategy SECURITY_UTIL_STRATEGY = new DefaultSecurityUtilStrategy();

    public static void setSecurityUtilStrategy(final SecurityUtilStrategy securityUtilStrategy) {
        SECURITY_UTIL_STRATEGY = securityUtilStrategy;
    }

    /**
     * 현재 스레드의 UserContext 를 반환. 인증을 받지 않았다면 빈값을 반환함
     *
     * @return
     */
    public static Optional<UserContext> getUserContext() {
        return SECURITY_UTIL_STRATEGY.getUserContext();
    }

    /**
     * 현재 스레드가 인증을 받았는지 확인
     *
     * @return 인증여부
     */
    public static boolean isAuthenticated() {
        return SECURITY_UTIL_STRATEGY.isAuthenticated();
    }

    private final static class DefaultSecurityUtilStrategy implements SecurityUtilStrategy {

        @Override
        public Optional<UserContext> getUserContext() {
            return getAuthentication()
                    .filter(this::isAuthenticatedUser)
                    .map(this::convertToUserContext)
                    .map(AuthenticatedUser::getUserContext);
        }

        private Optional<Authentication> getAuthentication() {
            return Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .filter(Authentication::isAuthenticated);
        }

        private AuthenticatedUser convertToUserContext(final Authentication authentication) {
            return (AuthenticatedUser) authentication;
        }

        public boolean isAuthenticatedUser(final Authentication authentication) {
            return authentication instanceof AuthenticatedUser;
        }

        @Override
        public boolean isAuthenticated() {
            return getAuthentication().isPresent();
        }
    }

}