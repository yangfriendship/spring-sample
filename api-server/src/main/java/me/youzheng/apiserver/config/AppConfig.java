package me.youzheng.apiserver.config;

import me.youzheng.apiserver.sessionlog.mapper.SessionLogMapper;
import me.youzheng.apiserver.user.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
public class AppConfig {

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    public SessionLogMapper sessionLogMapper() {
        return SessionLogMapper.INSTANCE;
    }

}