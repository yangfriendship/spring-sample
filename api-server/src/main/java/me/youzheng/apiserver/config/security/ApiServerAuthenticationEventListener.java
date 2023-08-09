package me.youzheng.apiserver.config.security;

import lombok.RequiredArgsConstructor;
import me.youzheng.apiserver.sessionlog.payload.SessionLogCreateRequest;
import me.youzheng.apiserver.sessionlog.service.SessionLogService;
import me.youzheng.core.domain.sessionlog.entity.SessionLogType;
import me.youzheng.core.security.AuthenticatedUser;
import me.youzheng.core.security.UserContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiServerAuthenticationEventListener {

    private final SessionLogService sessionLogService;

    /**
     * {@link AbstractAuthenticationProcessingFilter} 인증이 끝난 후 발생한 이벤트를 처리.
     *
     * @param event
     */
    @EventListener(InteractiveAuthenticationSuccessEvent.class)
    public void handleAuthenticationSuccessEvent(final InteractiveAuthenticationSuccessEvent event) {
        if (!isSupportedEvent(event)) {
            return;
        }
        final SessionLogCreateRequest createRequest = getSessionLogCreateRequest(event);
        this.sessionLogService.create(createRequest);
    }

    private SessionLogCreateRequest getSessionLogCreateRequest(final InteractiveAuthenticationSuccessEvent event) {
        final AuthenticatedUser authentication = (AuthenticatedUser) event.getAuthentication();
        final UserContext userContext = authentication.getUserContext();
        final SessionLogCreateRequest createRequest = SessionLogCreateRequest.builder()
                .userId(userContext.getUserId())
                .sessionId(authentication.getSessionId())
                .type(SessionLogType.CREATED)
                .build();
        return createRequest;
    }

    private boolean isSupportedEvent(final InteractiveAuthenticationSuccessEvent event) {
        return !(event == null || !(event.getAuthentication() instanceof AuthenticatedUser));
    }

}
