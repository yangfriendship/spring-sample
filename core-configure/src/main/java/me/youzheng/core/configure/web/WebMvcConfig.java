package me.youzheng.core.configure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@ComponentScan(
        basePackageClasses = WebMvcConfig.class
)
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    @Primary
    @ConditionalOnMissingBean(name = "defaultObjectMapper")
    @Bean(name = {"defaultObjectMapper", "objectMapper"})
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .build();
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this.pageableHandlerMethodArgumentResolver());
    }

    @Bean
    public ExceptionHandlerService exceptionHandlerService() {
        return new ApiErrorCodeExceptionHandlerService();
    }

    protected HandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
        final PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
        return pageableHandlerMethodArgumentResolver;
    }

}