package me.youzheng.core.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class UserDto {

    private Long userId;

    private String nickname;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Boolean use;

    @JsonIgnore
    private Boolean lock;

    private LocalDateTime passwordExpireDateTime;

    private LocalDateTime lastLoginDateTime;

}