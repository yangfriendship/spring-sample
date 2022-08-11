package me.youzheng.springsecurityredis.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Hi Youzheng");
        body.put("principal", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok().body(body);
    }

}
