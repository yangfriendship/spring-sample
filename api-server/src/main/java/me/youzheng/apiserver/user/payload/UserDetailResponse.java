package me.youzheng.apiserver.user.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class UserDetailResponse {

    private Long userId;

    private String nickname;

    private String email;

    private String username;

}
