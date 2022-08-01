package me.youzheng.springsecurity.menu.repository;

import me.youzheng.springsecurity.menu.entity.Menu;
import me.youzheng.springsecurity.menu.repository.querydsl.MenuRepositorySupporter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositorySupporter {

}