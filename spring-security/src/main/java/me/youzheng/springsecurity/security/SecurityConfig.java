package me.youzheng.springsecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.ipaddress.service.IpAddressService;
import me.youzheng.springsecurity.property.PropertiesConfig.JwtProperties;
import me.youzheng.springsecurity.property.PropertiesConfig.SecurityProperties;
import me.youzheng.springsecurity.menuauth.repository.MenuAuthRepository;
import me.youzheng.springsecurity.security.factorybean.UrlResourceMapFactoryBean;
import me.youzheng.springsecurity.security.filter.JwtAuthenticationFilter;
import me.youzheng.springsecurity.security.filter.JwtLoginProcessorFilter;
import me.youzheng.springsecurity.security.filter.PermitAllFilter;
import me.youzheng.springsecurity.security.handler.JwtAuthenticationSuccessHandler;
import me.youzheng.springsecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import me.youzheng.springsecurity.security.provider.UserAuthenticationProvider;
import me.youzheng.springsecurity.security.util.JwtProvider;
import me.youzheng.springsecurity.security.voter.IpAddressAccessVoter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CharacterEncodingFilter characterEncodingFilter;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtProperties jwtProperties;
    private final SecurityProperties securityProperties;
    private final MenuAuthRepository menuAuthRepository;
    private final IpAddressService ipAddressService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .disable()
                .csrf()
                .disable()
                .formLogin()
                .disable();

        http.addFilterBefore(this.characterEncodingFilter, CsrfFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(this.jwtLoginProcessorFilter(),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(this.filterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() {
        try {
            return super.authenticationManagerBean();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(this.jwtProperties.getSecretKey(),
                this.jwtProperties.getExpiredSeconds());
    }

    @Bean
    public UserAuthenticationProvider userAuthenticationProvider() {
        UserAuthenticationProvider provider = new UserAuthenticationProvider(
                this.userDetailsService, this.passwordEncoder);
        return provider;
    }

    @Bean
    public GrantedAuthoritiesMapper authoritiesMapper() {
        SimpleAuthorityMapper mapper = new SimpleAuthorityMapper();
        mapper.setPrefix("MENU_AUTH_GROUP_");
        mapper.setConvertToUpperCase(true);
        return mapper;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(this.jwtProvider());
    }

    @Bean
    public JwtLoginProcessorFilter jwtLoginProcessorFilter() {
        JwtLoginProcessorFilter loginProcessorFilter = new JwtLoginProcessorFilter(
                this.securityProperties.getLoginUrl(),
                this.objectMapper, this.authenticationManagerBean(), this.authoritiesMapper());
        loginProcessorFilter.setAuthenticationSuccessHandler(
                this.jwtAuthenticationSuccessHandler());

        return loginProcessorFilter;
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler(this.objectMapper, this.jwtProvider());
    }

    /**
     * 인가 담당 객체
     *
     * @return
     */
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        PermitAllFilter securityInterceptor = new PermitAllFilter("/api/menus/ok");

        securityInterceptor.setSecurityMetadataSource(
                this.urlFilterInvocationSecurityMetadataSource());

        securityInterceptor.setAuthenticationManager(this.authenticationManagerBean());
        securityInterceptor.setAccessDecisionManager(this.affirmativeBased());
        return securityInterceptor;
    }

    /**
     * 인가 로직
     * AffirmativeBased: Voter 중 하나라도 AccessDenied 라면 접근 거절된다.
     *
     * @return
     */
    @Bean
    public AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(
                ImmutableList.of(this.ipAddressAccessVoter(),
                        roleVoter()));
    }

    @Bean
    public AccessDecisionVoter<Object> roleVoter() {
        RoleVoter voter = new RoleVoter();
        return voter;
    }

    @Bean
    public UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        try {
            return new UrlFilterInvocationSecurityMetadataSource(
                    this.urlResourceMapFactoryBean());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Bean
    public UrlResourceMapFactoryBean urlResourceMapFactoryBean() {
        return new UrlResourceMapFactoryBean(this.menuAuthRepository);
    }

    @Bean
    public IpAddressAccessVoter ipAddressAccessVoter() {
        return new IpAddressAccessVoter(this.ipAddressService);
    }

}