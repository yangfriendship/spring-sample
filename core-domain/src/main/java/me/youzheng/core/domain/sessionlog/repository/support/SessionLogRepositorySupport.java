package me.youzheng.core.domain.sessionlog.repository.support;

import me.youzheng.core.domain.sessionlog.entity.SessionLog;

import java.util.Optional;

public interface SessionLogRepositorySupport {

    Optional<SessionLog> findBySessionId(final String sessionId);

}