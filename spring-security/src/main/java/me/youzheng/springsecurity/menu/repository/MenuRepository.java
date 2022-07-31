package me.youzheng.springsecurity.menu.repository;

import me.youzheng.springsecurity.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByHttpMethodAndUrl(String httpMethod, String patternString);

}
