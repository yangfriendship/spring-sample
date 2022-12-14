package me.youzheng.springsecurity.user.dto;

import lombok.Builder;
import lombok.Data;
import me.youzheng.springsecurity.user.entity.User;

@Data
@Builder
public class UserResponse {

    private Long userNo;

    private String userId;

    private Long menuAuthGroupNo;

    public static UserResponse from(User source) {
        return UserResponse.builder()
            .userNo(source.getUserNo())
            .userId(source.getUserId())
            .menuAuthGroupNo(source.getMenuAuthGroup().getMenuAuthGroupNo())
            .build();
    }

}
