package me.youzheng.core.configure.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.youzheng.core.configure.redis.RedisConfig;
import me.youzheng.core.configure.redis.RedisSerializerModules;
import me.youzheng.core.security.SecurityMixinModules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.session.DelegatingIndexResolver;
import org.springframework.session.PrincipalNameIndexResolver;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * Redis Session 설정 클래스. {@link EnableRedisHttpSession} 사용 시 충돌이 발생하므로 세부 설정은 yml 파일을 통해 명시해야한다.
 */
@Configuration
@RequiredArgsConstructor
@Import(RedisConfig.class)
public class RedisSessionConfig extends RedisHttpSessionConfiguration {

    private final HttpSessionIdResolver httpSessionIdResolver;

    private final RedisTemplate<Object, Object> redisSessionRedisTemplate;

    private final RedisSerializer<Object> springSessionDefaultRedisSerializer;

    @Bean
    @Override
    public <S extends Session> SessionRepositoryFilter<? extends Session> springSessionRepositoryFilter(
            SessionRepository<S> sessionRepository) {   // Intellij 에서 나타내는 경고
        SessionRepositoryFilter<S> sessionRepositoryFilter = new SessionRepositoryFilter<>(sessionRepository);
        sessionRepositoryFilter.setHttpSessionIdResolver(this.httpSessionIdResolver);
        return sessionRepositoryFilter;
    }

    @Bean
    @Override
    public RedisIndexedSessionRepository sessionRepository() {
        final RedisIndexedSessionRepository redisIndexedSessionRepository = new RedisIndexedSessionRepository(this.redisSessionRedisTemplate);
        redisIndexedSessionRepository.setDefaultSerializer(this.springSessionDefaultRedisSerializer);
        redisIndexedSessionRepository.setIndexResolver(new DelegatingIndexResolver<>(new PrincipalNameIndexResolver<>()));
        return redisIndexedSessionRepository;
    }

    /**
     * {@link EnableRedisHttpSession} 설정 값을 읽어들이는 메소드. EnableRedisHttpSession 로 설정파일을 읽지 않으므로 사용하지 않는다.
     */
    @Override
    public void setImportMetadata(final AnnotationMetadata importMetadata) {
        // do not something..
    }

    /**
     * {@link RedisHttpSessionConfiguration} 에서 필요한 Bean 을 등록하는 설정 클래스.
     * {@link RedisSessionConfig } 에 직접 등록 시 순환참조 예외 발생하기 때문에 내부 클래스에 따로 등록함.
     */
    @Configuration
    static class RedisSessionSessionExtensionConfig {

        @Bean
        public SpringSessionBackedSessionRegistry<? extends Session> springSessionBackedSessionRegistry(
                final RedisIndexedSessionRepository repository) {
            return new SpringSessionBackedSessionRegistry<>(repository);
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
            final ObjectMapper redisSessionObjectMapper = this.copyObjectMapper(objectMapper);
            SecurityMixinModules.addMixin(redisSessionObjectMapper);
            RedisSerializerModules.setDefaultResolverBuilder(redisSessionObjectMapper);
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

        /**
         * HttpServletRequest 에서 SessionCookie 를 추출하는 리졸버.
         * 현재 Session 을 Cookie 에 담아 저장하고 있다. Header 를 통해 세션을 전달한다면
         * {@link org.springframework.session.web.http.HeaderHttpSessionIdResolver } 를 설정하여 등록한다.
         * SCG, Nginx 또는 RequestInterceptor 에서 Session 을 사용하고 있다면 함께 변경해줘야한다.
         */
        @Bean
        public HttpSessionIdResolver httpSessionIdResolver(@Value("${server.servlet.session.cookie.name:YSESSION}") final String cookieName) {
            final CookieHttpSessionIdResolver cookieHttpSessionIdResolver = new CookieHttpSessionIdResolver();
            final DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
            cookieSerializer.setCookieName(cookieName);
            cookieHttpSessionIdResolver.setCookieSerializer(cookieSerializer);
            return cookieHttpSessionIdResolver;
        }

        /**
         * RedisSession 에서만 사용되는 RedisTemplate.
         */
        @Bean("redisSessionRedisTemplate")
        public RedisTemplate<Object, Object> redisSessionRedisTemplate(final RedisConnectionFactory redisConnectionFactory
                , final RedisSerializer<Object> springSessionDefaultRedisSerializer
        ) {
            final RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            final RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
            redisTemplate.setDefaultSerializer(springSessionDefaultRedisSerializer);
            redisTemplate.setKeySerializer(stringRedisSerializer);
            redisTemplate.setValueSerializer(springSessionDefaultRedisSerializer);
            redisTemplate.setHashKeySerializer(stringRedisSerializer);
            redisTemplate.setHashValueSerializer(springSessionDefaultRedisSerializer);

            return redisTemplate;
        }

    }

}