package me.youzheng.springsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.property.PropertiesConfig.JwtProperties;
import me.youzheng.springsecurity.property.PropertiesConfig.SecurityProperties;
import me.youzheng.springsecurity.security.filter.JwtAuthenticationFilter;
import me.youzheng.springsecurity.security.filter.JwtLoginProcessorFilter;
import me.youzheng.springsecurity.security.handler.JwtAuthenticationSuccessHandler;
import me.youzheng.springsecurity.security.provider.UserAuthenticationProvider;
import me.youzheng.springsecurity.security.util.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CharacterEncodingFilter characterEncodingFilter;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
            .disable()
            .csrf()
            .disable()
            .formLogin()
            .disable();

        http.addFilterBefore(this.characterEncodingFilter, CsrfFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(this.jwtLoginProcessorFilter(),
            UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(this.jwtProperties.getSecretKey(),
            this.jwtProperties.getExpiredSeconds());
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
        return new UserAuthenticationProvider(this.userDetailsService, this.passwordEncoder);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(this.jwtProvider());
    }

    @Bean
    public JwtLoginProcessorFilter jwtLoginProcessorFilter() throws Exception {
        JwtLoginProcessorFilter loginProcessorFilter = new JwtLoginProcessorFilter(
            this.securityProperties.getLoginUrl(),
            this.objectMapper, this.authenticationManagerBean());
        loginProcessorFilter.setAuthenticationSuccessHandler(
            this.jwtAuthenticationSuccessHandler());

        return loginProcessorFilter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(this.objectMapper, this.jwtProvider());
    }

}