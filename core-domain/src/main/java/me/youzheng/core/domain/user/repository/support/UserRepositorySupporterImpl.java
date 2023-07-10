package me.youzheng.core.domain.user.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.domain.user.QUser;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositorySupporterImpl implements UserRepositorySupporter {

    public static final QUser USER = QUser.user;
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByUserId(final Long userId) {
        return this.queryFactory.select(USER.id)
                .from(QUser.user)
                .where(QUser.user.id.eq(userId))
                .fetchOne() != null;
    }

    @Override
    public boolean existsByEmail(final String email) {
        return this.queryFactory.select(QUser.user.id)
                .from(QUser.user)
                .where(QUser.user.email.eq(email))
                .fetchOne() != null;
    }

    @Override
    public boolean existsByUsername(final String username) {
        return this.queryFactory.select(QUser.user.id)
                .from(QUser.user)
                .where(QUser.user.username.eq(username))
                .fetchOne() != null;
    }

}
