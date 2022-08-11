package me.youzheng.springsecurityredis.config;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurityredis.provider.MapAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
        ;
//                .sessionRegistry(this.springSessionBackedSessionRegistry);
        http
                .csrf().disable()
                .formLogin()
                .usernameParameter("loginId")
                .passwordParameter("password")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager managerBean = super.authenticationManagerBean();

        return managerBean;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new MapAuthenticationProvider();
    }

}