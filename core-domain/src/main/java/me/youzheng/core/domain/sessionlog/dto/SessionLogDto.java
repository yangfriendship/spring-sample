package me.youzheng.core.domain.sessionlog.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.youzheng.core.domain.sessionlog.entity.SessionLogType;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class SessionLogDto {

    private Long sessionLogId;

    private Long userId;

    private String sessionId;

    private SessionLogType type;

}