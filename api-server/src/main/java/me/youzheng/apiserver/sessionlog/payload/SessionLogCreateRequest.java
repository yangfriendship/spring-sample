package me.youzheng.apiserver.sessionlog.payload;

import lombok.*;
import me.youzheng.core.domain.sessionlog.entity.SessionLogType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SessionLogCreateRequest {

    @NotNull
    private Long userId;

    @NotEmpty
    private String sessionId;

    @NotNull
    private SessionLogType type;

}