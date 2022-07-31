package me.youzheng.springsecurity.security.factorybean;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.entity.Menu;
import me.youzheng.springsecurity.entity.MenuAuth;
import me.youzheng.springsecurity.repository.MenuAuthRepository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@RequiredArgsConstructor
public class UrlResourceMapFactoryBean implements
    FactoryBean<LinkedHashMap<RequestMatcher, List<ConfigAttribute>>> {

    private final MenuAuthRepository menuAuthRepository;

    @Override
    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getObject() throws Exception {
        List<MenuAuth> menuAuths = this.menuAuthRepository.findAllWithMenu();

        Map<Menu, List<MenuAuth>> menuListMap = menuAuths.stream()
            .collect(groupingBy(MenuAuth::getMenu));

        Set<Entry<Menu, List<MenuAuth>>> entries = menuListMap.entrySet();

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();
        for (Entry<Menu, List<MenuAuth>> entry : entries) {
            Menu menu = entry.getKey();
            List<ConfigAttribute> configAttributes = new ArrayList<>();
            for (MenuAuth menuAuth : entry.getValue()) {
                configAttributes.add(
                    new SecurityConfig("ROLE_" + menuAuth.getMenuAuthNo()));
            }
            result.put(new AntPathRequestMatcher(menu.getUrl(),
                HttpMethod.valueOf(menu.getHttpMethod()).name()), configAttributes);
        }

        return result;
    }

    @Override
    public Class<?> getObjectType() {
        return LinkedHashMap.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}