package me.youzheng.springsecurityredis.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SessionConfigTest {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void jedisInitTest() {
        Assertions.assertNotNull(this.redisTemplate);
        boolean exposeConnection = this.redisTemplate.isExposeConnection();
        System.out.println();
    }

}