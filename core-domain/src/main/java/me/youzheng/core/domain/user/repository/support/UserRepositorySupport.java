package me.youzheng.core.domain.user.repository.support;

import me.youzheng.core.domain.user.dto.UserDto;
import me.youzheng.core.domain.user.dto.UserCriteria;
import me.youzheng.core.domain.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

public interface UserRepositorySupport {

    boolean existsByUserId(final Long userId);

    boolean existsByEmail(final String email);

    boolean existsByUsername(final String username);

    Optional<User> findByEmail(final String email);

    Optional<User> findWithRolesByEmail(String email);

    Optional<User> findByNickname(final String nickname);

    boolean existsByNickname(final String nickname);

    Optional<User> findWithRoles(final Long userId);

    List<UserDto> findAllBy(UserCriteria userCriteria, Pageable pageable);

    default UserDto findOneBy(UserCriteria userCriteria) {
        final List<UserDto> results = findAllBy(userCriteria, PageRequest.of(0, 1));
        if (!CollectionUtils.isEmpty(results)) {
            return results.get(0);
        }
        return null;
    }
}