package me.youzheng.core.domain.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.domain.linstner.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
@RequiredArgsConstructor
public class JpaConfig {

    @Bean
    public AuditorAware<Long> securityAuditorAware() {
        return new SecurityAuditorAware();
    }

    @Bean
    public JPAQueryFactory queryFactory(final EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
