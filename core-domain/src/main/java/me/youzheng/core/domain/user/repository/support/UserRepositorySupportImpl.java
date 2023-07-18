package me.youzheng.core.domain.user.repository.support;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.youzheng.core.domain.user.dto.UserDto;
import me.youzheng.core.domain.user.dto.UserCriteria;
import me.youzheng.core.domain.user.entity.QUser;
import me.youzheng.core.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class UserRepositorySupportImpl implements UserRepositorySupport {

    public static final QUser USER = QUser.user;

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserId(final Long userId) {
        return this.existsBy(USER.userId, userId);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return this.existsBy(USER.email, email);

    }

    @Override
    public boolean existsByUsername(final String username) {
        return this.existsBy(USER.username, username);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        if (!StringUtils.hasLength(email)) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                this.queryFactory.select(USER)
                        .from(USER)
                        .where(USER.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<User> findWithRolesByEmail(final String email) {
        if (!StringUtils.hasLength(email)) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                this.queryFactory.select(USER)
                        .distinct()
                        .from(USER)
                        .leftJoin(USER.roles).fetchJoin()
                        .where(USER.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public Optional<User> findByNickname(final String nickname) {
        if (!StringUtils.hasLength(nickname)) {
            return Optional.empty();
        }
        return Optional.ofNullable(this.queryFactory.select(USER)
                .from(USER)
                .where(USER.email.eq(nickname))
                .fetchOne());
    }

    @Override
    public boolean existsByNickname(final String nickname) {
        return existsBy(USER.nickname, nickname);
    }

    @Override
    public Optional<User> findWithRoles(final Long userId) {
        return Optional.ofNullable(
                this.queryFactory.select(USER)
                        .from(USER)
                        .join(USER.roles).fetchJoin()
                        .where(USER.userId.eq(userId))
                        .fetchOne());
    }

    @Override
    public List<UserDto> findAllBy(final UserCriteria userCriteria, final Pageable pageable) {
        return this.queryFactory.select(
                        Projections.fields(UserDto.class
                                , USER.userId
                                , USER.email
                                , USER.username
                                , USER.nickname
                        ))
                .from(USER)
                .where(createBy(userCriteria))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanBuilder createBy(final UserCriteria userCriteria) {
        final BooleanBuilder builder = new BooleanBuilder();
        if (userCriteria.getUserId() != null) {
            builder.and(USER.userId.eq(userCriteria.getUserId()));
        }
        if (userCriteria.getEmail() != null) {
            builder.and(USER.email.eq(userCriteria.getEmail()));
        }
        if (userCriteria.getNickname() != null) {
            builder.and(USER.nickname.eq(userCriteria.getNickname()));
        }
        return builder;
    }

    private boolean existsBy(StringPath stringPath, final String value) {
        return this.queryFactory.select(USER.userId)
                .from(USER)
                .where(stringPath.eq(value))
                .fetchOne() != null;
    }

    private <T extends Number & Comparable<?>> boolean existsBy(final NumberPath<T> numberPath, final T value) {
        return this.queryFactory.select(USER.userId)
                .from(USER)
                .where(numberPath.eq(value))
                .fetchOne() != null;
    }

}