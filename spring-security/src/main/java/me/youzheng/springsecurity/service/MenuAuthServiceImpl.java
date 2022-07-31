package me.youzheng.springsecurity.service;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.repository.MenuAuthRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuAuthServiceImpl implements MenuAuthService {

    private final MenuAuthRepository menuAuthRepository;

}
