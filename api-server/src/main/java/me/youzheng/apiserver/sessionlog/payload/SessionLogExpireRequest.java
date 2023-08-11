package me.youzheng.apiserver.sessionlog.payload;

import lombok.*;
import me.youzheng.core.domain.sessionlog.entity.SessionLogType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SessionLogExpireRequest extends SessionLogCreateRequest {

    public SessionLogExpireRequest(@NotNull final Long userId, @NotEmpty final String sessionId) {
        super(userId, sessionId, SessionLogType.EXPIRED);
    }

    public static SessionLogExpireRequest of(@NotNull final Long userId, @NotEmpty final String sessionId) {
        return new SessionLogExpireRequest(userId, sessionId);
    }

}