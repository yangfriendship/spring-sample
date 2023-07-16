package me.youzheng.core.configure.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.domain.linstner.SecurityAuditorAware;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@ComponentScan(basePackages = CoreDomainConfig.CORE_DOMAIN_PACKAGE)
@EntityScan(basePackages = {CoreDomainConfig.CORE_DOMAIN_PACKAGE})
@EnableJpaAuditing(auditorAwareRef = "securityAuditorAware")
@EnableJpaRepositories(
        repositoryBaseClass = CoreSimpleJpaRepository.class
        , basePackages = "me.youzheng.core.domain"
)
@RequiredArgsConstructor
public class CoreDomainConfig {

    public static final String CORE_DOMAIN_PACKAGE = "me.youzheng.core.domain";

    @Bean
    public AuditorAware<Long> securityAuditorAware() {
        return new SecurityAuditorAware();
    }

    @Bean
    public JPAQueryFactory queryFactory(final EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }

}
