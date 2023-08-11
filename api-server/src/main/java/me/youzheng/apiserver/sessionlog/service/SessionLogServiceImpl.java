package me.youzheng.apiserver.sessionlog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.youzheng.apiserver.sessionlog.mapper.SessionLogMapper;
import me.youzheng.apiserver.sessionlog.payload.SessionLogCreateRequest;
import me.youzheng.apiserver.sessionlog.payload.SessionLogExpireRequest;
import me.youzheng.core.domain.sessionlog.dto.SessionLogDto;
import me.youzheng.core.domain.sessionlog.entity.SessionLog;
import me.youzheng.core.domain.sessionlog.repository.SessionLogRepository;
import me.youzheng.core.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class SessionLogServiceImpl implements SessionLogService {

    private final SessionLogMapper sessionLogMapper;

    private final SessionLogRepository sessionLogRepository;

    @Transactional
    @Override
    public SessionLogDto create(final SessionLogCreateRequest sessionLogCreateRequest) {
        final SessionLog sessionLog = this.sessionLogMapper.from(sessionLogCreateRequest);
        final SessionLog save = this.sessionLogRepository.save(sessionLog);
        return this.sessionLogMapper.toDto(save);
    }

    @Override
    @Transactional
    public SessionLogDto expire(final String sessionId) {
        if (!StringUtils.hasLength(sessionId)) {
            throw new IllegalArgumentException("SessionId 는 빈문자열일 수 없습니다.");
        }

        final SessionLog sessionLog = this.sessionLogRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new BadRequestException("존재하지 않는 세션입니다."));
        final SessionLogExpireRequest expireRequest = SessionLogExpireRequest.of(sessionLog.getUserId(), sessionId);
        return this.create(expireRequest);
    }

}