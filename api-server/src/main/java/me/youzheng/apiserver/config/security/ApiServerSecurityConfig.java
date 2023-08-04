package me.youzheng.apiserver.config.security;

import me.youzheng.core.configure.security.AuthServerConfigureAdapter;
import me.youzheng.core.configure.security.PreExceptionFilterAdapter;
import me.youzheng.core.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ApiServerSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.apply(PreExceptionFilterAdapter.create());
        http.apply(AuthServerConfigureAdapter.create());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(final UserRepository userRepository) {
        return new ApiServerUserDetailService(userRepository);
    }

    @Bean
    public LoginRequestAuthenticationProvider loginRequestAuthenticationProvider(final UserDetailsService userDetailsService
            , final PasswordEncoder passwordEncoder) {
        return new LoginRequestAuthenticationProvider(userDetailsService, passwordEncoder);
    }

}