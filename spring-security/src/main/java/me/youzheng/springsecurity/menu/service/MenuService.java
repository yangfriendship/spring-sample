package me.youzheng.springsecurity.menu.service;

import java.util.List;
import me.youzheng.springsecurity.menu.dto.MenuDto;
import me.youzheng.springsecurity.menu.dto.MenuRegisterRequest;
import me.youzheng.springsecurity.menu.dto.MenuResponse;

public interface MenuService {

    MenuDto register(MenuRegisterRequest request);

    List<MenuResponse> fetchAll();

}
