package me.youzheng.springsecurity.menuauthgroup.repository;

import java.util.Optional;
import me.youzheng.springsecurity.menuauthgroup.entity.MenuAuthGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuAuthGroupRepository extends JpaRepository<MenuAuthGroup, Long> {

    Optional<MenuAuthGroup> findFirstByGroupName(String menuName);

}
