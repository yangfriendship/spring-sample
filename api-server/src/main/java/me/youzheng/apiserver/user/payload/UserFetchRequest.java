package me.youzheng.apiserver.user.payload;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
@EqualsAndHashCode
public class UserFetchRequest {

    private Long userId;

    private String email;

    private String nickname;

}