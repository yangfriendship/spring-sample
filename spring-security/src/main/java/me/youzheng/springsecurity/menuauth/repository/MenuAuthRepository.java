package me.youzheng.springsecurity.menuauth.repository;

import java.util.List;
import me.youzheng.springsecurity.menuauth.entity.MenuAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MenuAuthRepository extends JpaRepository<MenuAuth, Long> {

    @Query("select ma, m, ma.menuAuthGroup.menuAuthGroupNo from MenuAuth ma join fetch Menu m on ma.menu = m")
    List<MenuAuth> findAllWithMenu();

}