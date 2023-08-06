package me.youzheng.core.configure.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import lombok.experimental.UtilityClass;

/**
 * RedisSession 직렬화 관련 설정 유틸 클래스
 *
 * @author yang friendship
 */
@UtilityClass
public class RedisSerializerModules {

    /**
     * 기본 StdTypeResolver 설정. GenericJackson2JsonRedisSerializer 등 직렬화 과정에 Class 정보를 남겨야할 때 ObjectMapper 에 필요한
     * TypeResolverBuilder 를 생성. GenericJackson2JsonRedisSerializer 기본 설정과 동일함.
     * @author yang friendship
     */
    public static StdTypeResolverBuilder createDefaultResolverBuilder(final ObjectMapper objectMapper) {
        final StdTypeResolverBuilder resolverBuilder
                = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL
                , objectMapper.getPolymorphicTypeValidator());

        return resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .typeProperty("@class");
    }

    /**
     * 인자로 넘어온 ObjectMapper 에 {@link #createDefaultResolverBuilder(ObjectMapper)} 를 적용한다.
     *
     * @param target StdTypeResolverBuilder 를 적용할 ObjectMapper 객체
     * @author yang friendship
     */
    public static void setDefaultResolverBuilder(final ObjectMapper target) {
        target.setDefaultTyping(createDefaultResolverBuilder(target));
    }

}