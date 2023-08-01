package me.youzheng.apiserver.user.payload;

import lombok.*;
import me.youzheng.core.domain.user.entity.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class UserCreateRequest {

    private String email;

    private String nickname;

    private String username;

    private String password;

    private String confirmPassword;


    public boolean isMatchPassword() {
        if (this.password == null || this.confirmPassword == null) {
            return false;
        }
        return this.password.equals(this.confirmPassword);
    }

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .build();
    }

}