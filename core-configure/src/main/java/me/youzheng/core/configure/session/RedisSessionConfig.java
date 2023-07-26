package me.youzheng.core.configure.session;

import me.youzheng.core.configure.redis.RedisConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RedisConfig.class)
public class RedisSessionConfig {


}
