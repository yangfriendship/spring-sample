package me.youzheng.springsecurity.menu.service;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;



}
