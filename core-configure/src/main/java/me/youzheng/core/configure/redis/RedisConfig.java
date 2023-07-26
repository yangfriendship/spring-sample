package me.youzheng.core.configure.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import me.youzheng.core.security.SecurityMixinModules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

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

    @Bean(name = "springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer(this.redisSessionObjectMapper(null));
    }

    /**
     * GenericJackson2JsonRedisSerializer 내부에서 사용될 ObjectMapper 를 직접 생성.
     */
    @Bean("redisSessionObjectMapper")
    public ObjectMapper redisSessionObjectMapper(@Autowired(required = false) ObjectMapper objectMapper) {
        ObjectMapper redisSessionObjectMapper = this.copyObjectMapper(objectMapper);
        SecurityMixinModules.addMixin(redisSessionObjectMapper);

        final StdTypeResolverBuilder resolverBuilder
                = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL
                , redisSessionObjectMapper.getPolymorphicTypeValidator());

        final StdTypeResolverBuilder init = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        final StdTypeResolverBuilder inclusion = init.inclusion(JsonTypeInfo.As.PROPERTY);

        final StdTypeResolverBuilder stdTypeResolverBuilder = inclusion.typeProperty("@class");
        redisSessionObjectMapper.setDefaultTyping(stdTypeResolverBuilder);
        return redisSessionObjectMapper;
    }

    /**
     * Bean 으로 등록된 ObjectMapper 가 있다면 Copy 해서 사용. 없다면 새로운 ObjectMapper 를 생성하여 반환한다.
     */
    private ObjectMapper copyObjectMapper(final ObjectMapper objectMapper) {
        if (objectMapper == null) {
            return new Jackson2ObjectMapperBuilder().build();
        }
        return objectMapper.copy();
    }

}