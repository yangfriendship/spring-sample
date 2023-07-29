package me.youzheng.apiserver;

import me.youzheng.core.configure.jpa.CoreDomainConfig;
import me.youzheng.core.configure.datasource.DataSourceConfig;
import me.youzheng.core.configure.security.SecurityConfig;
import me.youzheng.core.configure.session.RedisSessionConfig;
import me.youzheng.core.configure.web.WebMvcConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

@SpringBootApplication(
        scanBasePackageClasses = ApiServerApplication.class
)
@Import({
        DataSourceConfig.class
        , CoreDomainConfig.class
        , WebMvcConfig.class
        , SecurityConfig.class
        , RedisSessionConfig.class
})
public class ApiServerApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = new SpringApplicationBuilder(ApiServerApplication.class)
                .run(args);
    }

    @Bean
    public ApplicationRunner applicationRunnerForDebugging(final ApplicationContext ctx) {
        return (args) -> {
//            ctx.getBean(SessionRepository.class);
            ctx.getBean(RedisIndexedSessionRepository.class);
            System.out.println("");
        };
    }

}