package me.youzheng.core.configure.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(final RedisConfiguration configuration) {
        return new LettuceConnectionFactory(configuration);
    }

    @Profile("local")
    @Bean
    public RedisConfiguration localRedisConfiguration(final RedisProperties redisProperties) {
        final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());
        redisStandaloneConfiguration.setUsername(redisProperties.getUsername());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        return redisStandaloneConfiguration;
    }


    @ConditionalOnMissingBean(name = {"defaultRedisTemplate"})
    @Bean({"defaultRedisTemplate"})
    public RedisTemplate<String, Object> defaultRedisTemplate(final ObjectMapper objectMapper) {
        return new RedisTemplate<>();
    }

}