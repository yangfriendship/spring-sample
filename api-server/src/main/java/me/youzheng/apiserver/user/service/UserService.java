package me.youzheng.apiserver.user.service;

import me.youzheng.apiserver.user.payload.*;
import me.youzheng.core.domain.user.dto.UserDto;

public interface UserService {

    UserDto create(final UserCreateRequest request);

    UserDetailResponse fetch(UserFetchRequest request);

    boolean checkEmail(final String email);
    boolean checkNickname(final String nickname);

}