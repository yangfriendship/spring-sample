package me.youzheng.springsecurity.menuauthgroup.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menuauthgroup.entity.MenuAuthGroup;
import me.youzheng.springsecurity.menuauthgroup.repository.MenuAuthGroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuAuthGroupServiceImpl implements MenuAuthGroupService {

    private final MenuAuthGroupRepository menuAuthGroupRepository;

    @Override
    public Optional<MenuAuthGroup> fetchByMenuName(String menuName) {
        return this.menuAuthGroupRepository.findFirstByGroupName(menuName);
    }

}
