package me.youzheng.apiserver.sessionlog.service;

import me.youzheng.apiserver.sessionlog.payload.SessionLogCreateRequest;
import me.youzheng.core.domain.sessionlog.dto.SessionLogDto;
import org.springframework.transaction.annotation.Transactional;

public interface SessionLogService {

    SessionLogDto create(SessionLogCreateRequest sessionLogCreateRequest);

    SessionLogDto expire(String sessionId);
}
