package me.youzheng.springsecurity.menuauthgroup.service;

import java.util.Optional;
import me.youzheng.springsecurity.menuauthgroup.entity.MenuAuthGroup;

public interface MenuAuthGroupService {

    Optional<MenuAuthGroup> fetchByMenuName(String menuName);
}
