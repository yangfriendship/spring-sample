package me.youzheng.core.domain.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class UserCriteria {

    private Long userId;

    private String email;

    private String nickname;

}