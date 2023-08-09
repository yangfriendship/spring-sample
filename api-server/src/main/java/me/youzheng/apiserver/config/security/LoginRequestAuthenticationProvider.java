package me.youzheng.apiserver.config.security;

import lombok.RequiredArgsConstructor;
import me.youzheng.core.configure.security.LoginRequest;
import me.youzheng.core.exception.TokenParsingFailException;
import me.youzheng.core.exception.UnauthorizedException;
import me.youzheng.core.security.AuthenticatedUser;
import me.youzheng.core.security.UserContext;
import me.youzheng.core.util.WebUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Optional;

@RequiredArgsConstructor
public class LoginRequestAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final LoginRequest loginRequest = (LoginRequest) authentication;
        if (!this.isValid(loginRequest)) {
            throw new TokenParsingFailException("잘못된 로그인 요청정보입니다.");
        }
        final String userId = loginRequest.getPrincipal();

        final UserDetails userDetails = this.findUserBy(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        if (this.isNotUserContextType(userDetails)) {
            throw new UnauthorizedException("LoginRequestAuthenticationProvider 는 UserContext 타입만 지원합니다.");
        }

        if (!this.isMatchPassword(userDetails, loginRequest)) {
            throw new BadCredentialsException("패스워드가 일치하지 않습니다.");
        }

        final AuthenticatedUser authenticate = AuthenticatedUser.authenticate((UserContext) userDetails);
        this.bindSessionId(authenticate);
        return authenticate;
    }

    /**
     * HttpServletRequest 에서 SessionId 를 가져옴. 세션이 존재하지 않을 경우 세션을 생성
     */
    private void bindSessionId(final AuthenticatedUser authenticate) {
        this.forceInitSession();
        final Optional<String> sessionId = WebUtils.getSessionId();
        sessionId.ifPresent(authenticate::setSessionId);
    }

    /**
     * HttpSessionSecurityContextRepository 이전에 Session 이 필요하기 때문에 강제로 생성한다.
     */
    private void forceInitSession() {
        WebUtils.getCurrentRequest()
                .ifPresent(request -> request.getSession(true));
    }

    private boolean isMatchPassword(final UserDetails userDetails, final LoginRequest loginRequest) {
        return this.passwordEncoder.matches(loginRequest.getCredential(), userDetails.getPassword());
    }

    private boolean isNotUserContextType(final UserDetails userDetails) {
        return !(userDetails instanceof UserContext);
    }

    private Optional<UserDetails> findUserBy(final String userId) {
        if (!StringUtils.hasLength(userId)) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                this.userDetailsService.loadUserByUsername(userId)
        );
    }

    /**
     * 상속관계는 지원하지 않는다.
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return LoginRequest.class.equals(authentication);
    }

    /**
     * principal, credential 이 빈문자열인지 확인
     *
     * @param loginRequest Login 요청 객체
     */
    private boolean isValid(final LoginRequest loginRequest) {
        return StringUtils.hasLength(loginRequest.getCredential())
                && StringUtils.hasLength(loginRequest.getPrincipal());
    }
}