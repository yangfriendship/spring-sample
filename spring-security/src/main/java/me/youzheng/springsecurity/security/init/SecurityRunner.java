package me.youzheng.springsecurity.security.init;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.entity.Menu;
import me.youzheng.springsecurity.menu.entity.MenuType;
import me.youzheng.springsecurity.menu.repository.MenuRepository;
import me.youzheng.springsecurity.security.factorybean.UrlResourceMapFactoryBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

/**
 * RequestMappingHandlerMapping 을 가져와 DB 에 등록되지 않은 url 을 자동으로 등록한다.
 */
@Component
@RequiredArgsConstructor
public class SecurityRunner implements ApplicationRunner {

    private final List<RequestMappingHandlerMapping> requestMappingHandlerMappings;
    private final MenuRepository menuRepository;
    private final UrlResourceMapFactoryBean urlResourceMapFactoryBean;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (RequestMappingHandlerMapping mapping : requestMappingHandlerMappings) {
            Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
            Set<RequestMappingInfo> requestMappingInfos = handlerMethods.keySet();
            for (RequestMappingInfo mappingInfo : requestMappingInfos) {
                for (RequestMethod requestMethod : mappingInfo.getMethodsCondition().getMethods()) {
                    String httpMethod = requestMethod.name();
                    for (PathPattern pathPatterns : mappingInfo.getPathPatternsCondition()
                            .getPatterns()) {
                        String pattern = this.replacePathPathVariable(pathPatterns.getPatternString());
                        if (this.menuRepository.existsByHttpMethodAndUrl(httpMethod,
                                pattern)) {
                            continue;
                        }
                        Menu menu = Menu.builder()
                                .httpMethod(httpMethod)
                                .menuType(MenuType.SERVER)
                                .url(pattern)
                                .build();
                        this.menuRepository.save(menu);
                    }
                }
            }
        }
        this.urlResourceMapFactoryBean.getObject();
    }

    private String replacePathPathVariable(String url) {
        char[] chars = url.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        boolean canWrite = true;
        for (int i = 0; i < chars.length; i++) {
            char target = chars[i];
            if (target == '{') {
                stringBuilder.append("**");
                canWrite = false;
            } else if (target == '}') {
                canWrite = true;
            } else {
                if (canWrite) {
                    stringBuilder.append(target);
                }
            }
        }

        return stringBuilder.toString();
    }

}
