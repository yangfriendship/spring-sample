package me.youzheng.springsecurity.service;

import java.util.Optional;
import me.youzheng.springsecurity.entity.MenuAuthGroup;

public interface MenuAuthGroupService {

    Optional<MenuAuthGroup> fetchByMenuName(String menuName);
}
