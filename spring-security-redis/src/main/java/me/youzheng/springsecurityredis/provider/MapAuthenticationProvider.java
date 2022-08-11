package me.youzheng.springsecurityredis.provider;

import com.sun.tools.javac.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class MapAuthenticationProvider implements AuthenticationProvider {

    final static Map<String, String> userStore = new ConcurrentHashMap<>();

    static {
        userStore.put("admin", "dnwjd123");
        userStore.put("user1", "dnwjd123");
        userStore.put("user2", "dnwjd123");
        userStore.put("user3", "dnwjd123");
        userStore.put("user4", "dnwjd123");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (!userStore.containsKey(loginId)) {
            throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
        }
        String storePassword = userStore.get(loginId);
        if (storePassword == null || storePassword.length() == 0 || !storePassword.equals(password)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        UserContext userContext = new UserContext(loginId, password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        return new UsernamePasswordAuthenticationToken(userContext, "", userContext.getAuthorities());
    }

    @Getter
    @Setter
    public static class UserContext extends User {

        public UserContext(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
