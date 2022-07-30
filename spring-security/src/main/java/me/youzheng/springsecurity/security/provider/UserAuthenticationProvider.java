package me.youzheng.springsecurity.security.provider;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.security.principal.UserContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException {

        String loginId = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(loginId);
        if (!(userDetails instanceof UserContext)) {
            throw new BadCredentialsException("[ERROR] 로그인 실패, 존재하지 않는 계정");
        }

        UserContext userContext = (UserContext) userDetails;
        if (!this.passwordEncoder.matches(password, userContext.getPassword())) {
            throw new BadCredentialsException("[ERROR] 로그인 실패, BadCredential");
        }
        // clear password, set null
        userContext.clear();
        return new UsernamePasswordAuthenticationToken(userContext, userContext.getPassword(),
            userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}