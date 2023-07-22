package me.youzheng.core.util;

import me.youzheng.core.security.UserContext;

import java.util.Optional;

/**
 * {@link SecurityUtils} 의 기능을 실제로 구현하는 전략 인터페이스
 */
public interface SecurityUtilStrategy {

    Optional<UserContext> getUserContext();

    boolean isAuthenticated();

}