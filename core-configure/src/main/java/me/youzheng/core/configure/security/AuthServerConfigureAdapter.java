package me.youzheng.core.configure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.youzheng.core.configure.security.filter.ApiLoginProcessFilter;
import me.youzheng.core.configure.security.filter.PreExceptionFilter;
import me.youzheng.core.configure.security.filter.SessionLogoutFilter;
import me.youzheng.core.configure.security.handler.ApiAuthenticationSuccessHandler;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Session 방식의 인증서버용 설정클래스
 */
public class AuthServerConfigureAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private ApplicationContext applicationContext;
    private String loginUrl = "/api/login";
    private String logoutUrl = "/api/logout";
    private String sessionCookieName = "YSESSION";
    private HttpSecurity httpSecurity;

    protected AuthServerConfigureAdapter() {
    }

    public static AuthServerConfigureAdapter create() {
        return new AuthServerConfigureAdapter();
    }

    public AuthServerConfigureAdapter loginUrl(final String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public AuthServerConfigureAdapter logoutUrl(final String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    public AuthServerConfigureAdapter sessionCookieName(final String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
        return this;
    }

    @Override
    public void init(final HttpSecurity http) throws Exception {
        Assert.notNull(http, "HttpSecurity can't be null!");
        disableOtherConfigure(http);

        this.applicationContext = http.getSharedObject(ApplicationContext.class);
        this.httpSecurity = http;
        Assert.notNull(this.applicationContext, "ApplicationContext can't be null!");


        http.sessionManagement(c -> {
            c.maximumSessions(2);
            c.sessionConcurrency(cs -> cs.sessionRegistry(this.applicationContext.getBean(SpringSessionBackedSessionRegistry.class)));
        });
        Filter loginFilter = this.createLoginFilter(http);
        http.addFilterAfter(loginFilter, PreExceptionFilter.class);

        Filter logoutFilter = this.createLogoutFilter();
        http.addFilterAfter(logoutFilter, PreExceptionFilter.class);
    }

    protected Filter createLogoutFilter() {
        final SessionRegistry sessionRegistry = this.applicationContext.getBean(SessionRegistry.class);
        final ObjectMapper objectMapper = this.applicationContext.getBean(ObjectMapper.class);
        return new SessionLogoutFilter(this.logoutUrl, this.sessionCookieName, sessionRegistry, objectMapper);
    }

    protected void disableOtherConfigure(final HttpSecurity http) {
        try {
            http.httpBasic().disable();
            http.formLogin().disable();
            http.cors().disable();
            http.csrf().disable();
            http.servletApi().disable();
            http.logout().disable();
            http.requestCache().disable();
        } catch (Exception e) {
            throw new BeanCreationException(this.getClass().getSimpleName() + "생성 중 예외가 발생하였습니다.", e);
        }
    }

    protected Filter createLoginFilter(final HttpSecurity http) {
        final ApiLoginProcessFilter apiLoginProcessFilter = new ApiLoginProcessFilter(this.loginUrl, this.applicationContext.getBean(ObjectMapper.class));
        final AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        apiLoginProcessFilter.setAuthenticationManager(authenticationManager);
        apiLoginProcessFilter.setAuthenticationManager(this.getAuthenticationManager(http));
        apiLoginProcessFilter.setAuthenticationSuccessHandler(getSuccessHandler());
        final SessionRegistry sessionRegistry = this.applicationContext.getBean(SessionRegistry.class);
        final CompositeSessionAuthenticationStrategy sessionAuthenticationStrategy
                = new CompositeSessionAuthenticationStrategy(
                List.of(
                        new ChangeSessionIdAuthenticationStrategy()
                        , new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry)
                )
        );

        final HttpSessionSecurityContextRepository contextRepository = getContextRepository();
        apiLoginProcessFilter.setSecurityContextRepository(contextRepository);

        apiLoginProcessFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        return apiLoginProcessFilter;
    }

    /**
     * RedisIndexedSessionRepository 에서 principalNameKey 를 FindByIndexNameSessionRepository 의 상수를 이용하도록 하드코딩되어있다.
     * RedisIndexedSessionRepository 로 세션 관리르 한다면 HttpSessionSecurityContextRepository 의  springSecurityContextKey 를 변경해서는 안된다.
     */
    protected HttpSessionSecurityContextRepository getContextRepository() {
        final HttpSessionSecurityContextRepository contextRepository
                = new HttpSessionSecurityContextRepository();
        contextRepository.setSpringSecurityContextKey("USER_CONTEXT");
        return contextRepository;
    }

    private ApiAuthenticationSuccessHandler getSuccessHandler() {
        return new ApiAuthenticationSuccessHandler();
    }

    protected AuthenticationManager getAuthenticationManager(final HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        if (!(authenticationManager instanceof ProviderManager)) {
            authenticationManager = new ProviderManager(this.getAuthenticationProviders());
        }
        final ProviderManager providerManager = (ProviderManager) authenticationManager;
        final AuthenticationEventPublisher eventPublisher = http.getSharedObject(AuthenticationEventPublisher.class);
        if (eventPublisher != null) {
            providerManager.setAuthenticationEventPublisher(eventPublisher);
        }
        return providerManager;
    }

    private List<AuthenticationProvider> getAuthenticationProviders() {
        final Map<String, AuthenticationProvider> authenticationManagers = this.applicationContext.getBeansOfType(AuthenticationProvider.class);
        if (CollectionUtils.isEmpty(authenticationManagers)) {
            throw new BeanCreationException("AuthenticatedUser Type Bean 이 존재하지 않습니다.");
        }
        return new ArrayList<>(authenticationManagers.values());
    }

}