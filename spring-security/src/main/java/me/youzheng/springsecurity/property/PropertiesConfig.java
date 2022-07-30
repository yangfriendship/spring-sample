package me.youzheng.springsecurity.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.property.PropertiesConfig.JwtProperties;
import me.youzheng.springsecurity.property.PropertiesConfig.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {JwtProperties.class, SecurityProperties.class})
public class PropertiesConfig {

    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    @ConfigurationProperties("security")
    public static final class SecurityProperties {

        private final String loginUrl;
    }

    @Getter
    @RequiredArgsConstructor
    @ConstructorBinding
    @ConfigurationProperties("security.jwt")
    public static final class JwtProperties {

        private final String secretKey;
        private final long expiredSeconds;
    }

}