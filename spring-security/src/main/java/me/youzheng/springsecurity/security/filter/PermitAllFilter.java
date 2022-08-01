package me.youzheng.springsecurity.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PermitAllFilter extends FilterSecurityInterceptor {

    private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
    private boolean observeOncePerRequest = true;

    private List<RequestMatcher> requestMatchers;

    public PermitAllFilter(String... patterns) {
        this.requestMatchers = new ArrayList<>(patterns.length);
        for (String pattern : patterns) {
            this.requestMatchers.add(new AntPathRequestMatcher(pattern));
        }
    }

    @Override
    protected InterceptorStatusToken beforeInvocation(Object object) {
        boolean permitAll = false;
        FilterInvocation invocation = (FilterInvocation) object;
        HttpServletRequest request = invocation.getRequest();
        for (RequestMatcher matcher : this.requestMatchers) {
            if (matcher.matches(request)) {
                permitAll = true;
            }
        }
        if (permitAll) {
            return null;    // null 반환하면 인가 처리를 더이상 진행하지 않는다. 슈퍼클래스의 인가 확인 절차를 거치지 않고 바로 null 을 반환
        } else {
            return super.beforeInvocation(object);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        invoke(new FilterInvocation(request, response, chain));
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        if (isApplied(filterInvocation) && this.observeOncePerRequest) {
            filterInvocation.getChain()
                .doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
            return;
        }
        // first time this request being called, so perform security checking
        if (filterInvocation.getRequest() != null && this.observeOncePerRequest) {
            filterInvocation.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }
        InterceptorStatusToken token = beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain()
                .doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } finally {
            super.finallyInvocation(token);
        }
        super.afterInvocation(token, null);
    }

    /**
     * super class 에서 가져옴
     *
     * @param filterInvocation
     * @return
     */
    private boolean isApplied(FilterInvocation filterInvocation) {
        return (filterInvocation.getRequest() != null)
            && (filterInvocation.getRequest().getAttribute(FILTER_APPLIED) != null);
    }

}
