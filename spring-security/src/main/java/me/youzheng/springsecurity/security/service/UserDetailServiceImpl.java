package me.youzheng.springsecurity.security.service;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.entity.MenuAuthGroup;
import me.youzheng.springsecurity.entity.User;
import me.youzheng.springsecurity.repository.UserRepository;
import me.youzheng.springsecurity.security.principal.UserContext;
import me.youzheng.springsecurity.security.principal.UserInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> find = this.userRepository.findByUserId(userId);
        if (!find.isPresent()) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }

        User user = find.get();
        UserInfo userInfo = UserInfo.builder()
            .userNo(user.getUserNo())
            .userId(user.getUserId())
            .password(user.getPassword())
            .menuAuthGroupNo(user.getMenuAuthGroup().getMenuAuthGroupNo())
            .build();
        return new UserContext(userInfo);
    }

}