package me.youzheng.springsecurityredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
public class RedisConfig {

    /**
     * application.yml 으로 설정할 수 있다.
     * @return
     */
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("127.0.0.1");
        config.setPort(6379);
        config.setPassword("dnwjd123");
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry(RedisIndexedSessionRepository redisIndexedSessionRepository) {
        return new SpringSessionBackedSessionRegistry<>(redisIndexedSessionRepository);
    }

}