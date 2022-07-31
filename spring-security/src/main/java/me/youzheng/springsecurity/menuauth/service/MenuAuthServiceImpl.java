package me.youzheng.springsecurity.menuauth.service;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menuauth.repository.MenuAuthRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuAuthServiceImpl implements MenuAuthService {

    private final MenuAuthRepository menuAuthRepository;

}
