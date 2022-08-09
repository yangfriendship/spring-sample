package me.youzheng.oauth2resourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final String URL_PREFIX = "/users";

    @GetMapping("/status/check")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("OK");
    }

}