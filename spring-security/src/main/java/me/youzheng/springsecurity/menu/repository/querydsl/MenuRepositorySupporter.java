package me.youzheng.springsecurity.menu.repository.querydsl;

import java.util.List;
import me.youzheng.springsecurity.menu.dto.MenuDto;

public interface MenuRepositorySupporter {

    long updateUseState(Long menuNo, boolean isUse);

    boolean existsByHttpMethodAndUrl(String httpMethod, String patternString);

    List<MenuDto> findActiveMenus();
}
