package me.youzheng.apiserver.user.controller;

import lombok.RequiredArgsConstructor;
import me.youzheng.apiserver.user.payload.UserCreateRequest;
import me.youzheng.apiserver.user.payload.UserDetailResponse;
import me.youzheng.apiserver.user.payload.UserFetchRequest;
import me.youzheng.apiserver.user.service.UserService;
import me.youzheng.core.domain.user.dto.UserDto;
import me.youzheng.core.web.ApiResponse;
import me.youzheng.core.web.CheckResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    public static final String BASE_URL = "/api/users";

    private final UserService userService;

    @PostMapping(BASE_URL)
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Validated @RequestBody final UserCreateRequest request) {
        final UserDto userDto = this.userService.create(request);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }

    @GetMapping(BASE_URL + "/{userId}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> fetchByUserId(@PathVariable("userId") final Long userId) {
        final UserFetchRequest fetchRequest = UserFetchRequest.builder()
                .userId(userId)
                .build();
        final UserDetailResponse userDetailResponse = this.userService.fetch(fetchRequest);
        return ResponseEntity.ok(
                ApiResponse.success(userDetailResponse)
        );
    }

    @GetMapping(BASE_URL + "/check-email/{email}")
    public ResponseEntity<ApiResponse<CheckResult>> checkEmailIsAlreadyUsed(@PathVariable("email") final String email) {
        final boolean isUsed = this.userService.checkEmail(email);
        return ResponseEntity.ok(
                ApiResponse.checkResult("email", isUsed)
        );
    }

    @GetMapping(BASE_URL + "/check-nickname/{nickname}")
    public ResponseEntity<ApiResponse<CheckResult>> checkNicknameIsAlreadyUsed(@PathVariable("nickname") final String nickname) {
        final boolean isUsed = this.userService.checkNickname(nickname);
        return ResponseEntity.ok(
                ApiResponse.checkResult("nickname", isUsed)
        );
    }

}
