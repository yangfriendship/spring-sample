package me.youzheng.springsecurity.security.metadatasource;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class UrlFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

    private LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap;

    public UrlFilterInvocationSecurityMetadataSource(
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> requestMap) {
        this.requestMap = requestMap;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
        throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest request = filterInvocation.getRequest();

        if (requestMap != null) {
            Set<Entry<RequestMatcher, List<ConfigAttribute>>> entries = requestMap.entrySet();
            for (Entry<RequestMatcher, List<ConfigAttribute>> entry : entries) {
                RequestMatcher matcher = entry.getKey();
                if (matcher.matches(request)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> result = new HashSet<>();
        Set<Entry<RequestMatcher, List<ConfigAttribute>>> entries = requestMap.entrySet();
        for (Entry<RequestMatcher, List<ConfigAttribute>> entry : entries) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
