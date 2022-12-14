package me.youzheng.springsecurity.user.service;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menuauthgroup.entity.MenuAuthGroup;
import me.youzheng.springsecurity.menuauthgroup.service.MenuAuthGroupService;
import me.youzheng.springsecurity.user.entity.User;
import me.youzheng.springsecurity.user.dto.SignupRequest;
import me.youzheng.springsecurity.user.dto.UserResponse;
import me.youzheng.springsecurity.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MenuAuthGroupService menuAuthGroupService;

    @Override
    public UserResponse signup(SignupRequest request) {
        if (isAlreadyUsedUserId(request.getUserId())) {
            throw new RuntimeException("이미 사용중인 아이디입니다.");
        }

        User user = User.builder()
            .userId(request.getUserId())
            .password(this.passwordEncoder.encode(request.getPassword()))
            .menuAuthGroup(this.menuAuthGroupService.fetchByMenuName("default_group")
                .orElseGet(() -> MenuAuthGroup.builder().menuAuthGroupNo(1L).build()))
            .build();

        User save = this.userRepository.save(user);
        return UserResponse.from(save);
    }

    private boolean isAlreadyUsedUserId(String userId) {
        return this.userRepository.existsByUserId(userId);
    }

}