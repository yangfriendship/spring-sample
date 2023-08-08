package me.youzheng.core.domain.sessionlog.repository.support;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.domain.sessionlog.entity.QSessionLog;
import me.youzheng.core.domain.sessionlog.entity.SessionLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionLogRepositorySupportImpl implements SessionLogRepositorySupport {

    public static final QSessionLog SESSION_LOG = QSessionLog.sessionLog;

    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<SessionLog> findBySessionId(final String sessionId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(SESSION_LOG)
                        .where(SESSION_LOG.sessionId.eq(sessionId))
                        .fetchOne()
        );
    }

}