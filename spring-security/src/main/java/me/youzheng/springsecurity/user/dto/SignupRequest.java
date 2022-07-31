package me.youzheng.springsecurity.user.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String userId;

    private String password;

}
