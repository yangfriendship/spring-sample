package me.youzheng.springsecurity.controller;

import java.util.Collections;
import java.util.List;
import me.youzheng.springsecurity.entity.dto.MenuResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> fetchMenuList() {
        return ResponseEntity.ok(Collections.emptyList());
    }

}