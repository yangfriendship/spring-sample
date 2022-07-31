package me.youzheng.springsecurity.security.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JwtLoginProcessorFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    private final GrantedAuthoritiesMapper authoritiesMapper;

    public JwtLoginProcessorFilter(String url, ObjectMapper objectMapper,
        AuthenticationManager authenticationManager,
        GrantedAuthoritiesMapper authoritiesMapper) {
        super(new AntPathRequestMatcher(url), authenticationManager);
        this.objectMapper = objectMapper;
        this.authoritiesMapper = authoritiesMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException {
        BufferedReader reader = request.getReader();
        Map<String, Object> form;
        try {
            form = this.objectMapper.readValue(reader, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            form = ImmutableMap.of("userId", "", "password", "");
        }

        return getAuthenticationManager().authenticate(
            new UsernamePasswordAuthenticationToken(form.get("userId"), form.get("password")));
    }

}