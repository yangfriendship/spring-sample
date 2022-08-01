package me.youzheng.springsecurity.menu.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.dto.MenuDto;
import me.youzheng.springsecurity.menu.dto.MenuRegisterRequest;
import me.youzheng.springsecurity.menu.dto.MenuResponse;
import me.youzheng.springsecurity.menu.service.MenuService;
import me.youzheng.springsecurity.menu.service.MenuServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> fetchMenuList() {
        List<MenuResponse> menuResponses = this.menuService.fetchAll();
        return ResponseEntity.ok(menuResponses);
    }

    @PatchMapping("/api/menus/{menuNo}")
    public ResponseEntity<?> modifyMenu(@PathVariable Long menuNo) {
        return null;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> registerMenu(@RequestBody MenuRegisterRequest request) {
        MenuDto save = this.menuService.register(request);
        MenuResponse response = MenuResponse.from(save);
        return ResponseEntity.status(201).body(response);
    }

}