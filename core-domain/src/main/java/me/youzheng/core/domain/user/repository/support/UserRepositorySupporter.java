package me.youzheng.core.domain.user.repository.support;

public interface UserRepositorySupporter {

    boolean existsByUserId(final Long userId);

    boolean existsByEmail(final String email);

    boolean existsByUsername(final String username);

}