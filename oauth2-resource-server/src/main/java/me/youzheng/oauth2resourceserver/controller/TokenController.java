package me.youzheng.oauth2resourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private static final String URL_PREFIX = "/tokens";

    /**
     * UserDetails
     * @param jwt
     * @return
     */
    @GetMapping(URL_PREFIX)
    public ResponseEntity<Map<String, Object>> getToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(Collections.singletonMap("principal", jwt));
    }

}