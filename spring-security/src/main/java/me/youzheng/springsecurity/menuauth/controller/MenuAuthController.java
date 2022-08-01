package me.youzheng.springsecurity.menuauth.controller;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menuauth.service.MenuAuthService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuAuthController {

    private final MenuAuthService menuAuthService;

}