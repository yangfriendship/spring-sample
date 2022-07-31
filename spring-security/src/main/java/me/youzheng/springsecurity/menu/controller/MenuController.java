package me.youzheng.springsecurity.menu.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuController {



    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> fetchMenuList() {
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PatchMapping("/api/menus/{menuNo}")
    public ResponseEntity<?> modifyMenu(@PathVariable Long menuNo) {

        return null;
    }

}