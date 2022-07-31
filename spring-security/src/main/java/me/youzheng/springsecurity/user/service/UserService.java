package me.youzheng.springsecurity.user.service;

import me.youzheng.springsecurity.user.dto.SignupRequest;
import me.youzheng.springsecurity.user.dto.UserResponse;

public interface UserService {


    UserResponse signup(SignupRequest request);
}
