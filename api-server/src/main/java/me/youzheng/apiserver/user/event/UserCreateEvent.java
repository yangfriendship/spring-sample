package me.youzheng.apiserver.user.event;

import com.sun.istack.NotNull;
import lombok.*;
import me.youzheng.core.domain.user.entity.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
@ToString
public class UserCreateEvent {

    private Long id;

    private String nickname;

    private String email;

    private String username;

    @NotNull
    public static UserCreateEvent of(final User createdUser) {
        if (createdUser == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        if (createdUser.getUserId() == null) {
            throw new IllegalArgumentException("Require EntityManager Flush");
        }
        return UserCreateEvent.builder()
                .id(createdUser.getUserId())
                .email(createdUser.getEmail())
                .nickname(createdUser.getNickname())
                .build();
    }

}