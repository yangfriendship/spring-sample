package me.youzheng.springsecurity.service;

import me.youzheng.springsecurity.entity.dto.SignupRequest;
import me.youzheng.springsecurity.entity.dto.UserResponse;

public interface UserService {


    UserResponse signup(SignupRequest request);
}
