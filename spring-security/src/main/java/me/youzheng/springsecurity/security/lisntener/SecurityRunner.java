package me.youzheng.springsecurity.security.lisntener;

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
                        if (this.menuRepository.existsByHttpMethodAndUrl(httpMethod,
                            pathPatterns.getPatternString())) {
                            continue;
                        }
                        Menu menu = Menu.builder()
                            .httpMethod(httpMethod)
                            .menuType(MenuType.SERVER)
                            .url(pathPatterns.getPatternString())
                            .build();
                        this.menuRepository.save(menu);
                    }
                }
            }
        }
        this.urlResourceMapFactoryBean.getObject();
    }
}
