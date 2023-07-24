package me.youzheng.core.configure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.security.AnnotationHttpStatusResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @ConditionalOnMissingBean(name = "defaultPasswordEncoder")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @ConditionalOnMissingBean(name = "defaultExceptionHandler")
    @Bean
    public ExceptionHandler defaultExceptionHandler(final ObjectMapper objectMapper) {
        return new DefaultExceptionHandler(new AnnotationHttpStatusResolver(), objectMapper);
    }
    
}