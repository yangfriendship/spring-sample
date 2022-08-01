package me.youzheng.springsecurity.menu.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.dto.MenuDto;
import me.youzheng.springsecurity.menu.dto.MenuRegisterRequest;
import me.youzheng.springsecurity.menu.dto.MenuResponse;
import me.youzheng.springsecurity.menu.entity.Menu;
import me.youzheng.springsecurity.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public MenuDto register(MenuRegisterRequest request) {
        Menu menu = request.toEntity();
        Menu save = this.menuRepository.save(menu);
        return MenuDto.from(save);
    }

    @Override
    public List<MenuResponse> fetchAll() {
        List<MenuDto> menus = this.menuRepository.findActiveMenus();
        return menus.stream().map(MenuResponse::from)
            .collect(Collectors.toList());
    }

}