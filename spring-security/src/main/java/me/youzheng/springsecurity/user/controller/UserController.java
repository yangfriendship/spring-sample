package me.youzheng.springsecurity.user.controller;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.user.dto.SignupRequest;
import me.youzheng.springsecurity.user.dto.UserResponse;
import me.youzheng.springsecurity.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    public static final String URL_PREFIX = "/api/users";

    private final UserService userService;

    @PostMapping(URL_PREFIX)
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        UserResponse userResponse = this.userService.signup(request);
        return ResponseEntity.status(201).body(userResponse);
    }

}
