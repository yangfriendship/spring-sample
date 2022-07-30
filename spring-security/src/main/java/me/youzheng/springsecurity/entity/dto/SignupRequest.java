package me.youzheng.springsecurity.entity.dto;

import lombok.Data;

@Data
public class SignupRequest {

    private String userId;

    private String password;

}
