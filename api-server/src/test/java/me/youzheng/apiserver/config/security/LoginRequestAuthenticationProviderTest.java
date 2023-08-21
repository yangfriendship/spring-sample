package me.youzheng.apiserver.config.security;

import me.youzheng.core.configure.security.LoginRequest;
import me.youzheng.core.exception.TokenParsingFailException;
import me.youzheng.core.exception.UnauthorizedException;
import me.youzheng.core.security.AuthenticatedUser;
import me.youzheng.core.security.UserContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginRequestAuthenticationProviderTest {

    LoginRequestAuthenticationProvider authenticationProvider;

    UserDetailsService userDetailsService;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        this.userDetailsService = mock(UserDetailsService.class);
        this.passwordEncoder = mock(PasswordEncoder.class);
        this.authenticationProvider = new LoginRequestAuthenticationProvider(this.userDetailsService, this.passwordEncoder);
    }

    @DisplayName("[FAIL] 존재하지 않는 계정일 경우 테스트 - 'UsernameNotFoundException.class' 를 던진다.")
    @Test
    void 존재하지않는_계정일_경우_테스트() {
        final LoginRequest authentication = getMockLoginRequest();
        Assertions.assertThatThrownBy(() -> {
            this.authenticationProvider.authenticate(authentication);
        }).isExactlyInstanceOf(UsernameNotFoundException.class);
    }

    @DisplayName("[FAIL] 로그인 정보 누락 테스트 - 이메일 또는 패스워드가 잘못된 경우 'TokenParsingFailException.class' 가 발생한다.")
    @ParameterizedTest
    @MethodSource("invalidLoginRequestArguments")
    void 로그인_요청정보_누락_테스트(final LoginRequest loginRequest) {
        Assertions.assertThatThrownBy(() ->
                        this.authenticationProvider.authenticate(loginRequest))
                .isExactlyInstanceOf(TokenParsingFailException.class);
    }

    @DisplayName("[FAIL] 지원하지 않는 'UserDetails' 타입일 경우 예외가 발생한다.")
    @Test
    void 지원하지_않는_UserDetails_테스트() {
        final LoginRequest authentication = getMockLoginRequest();

        // Authentication 를 구현하였지만 'UserContext.class' 의 하위 인터페이스가 아님.
        class NotExtendUserContextUserDetailService extends User {

            public NotExtendUserContextUserDetailService() {
                super("username", "password", Collections.emptyList());
            }

        }

        when(this.userDetailsService.loadUserByUsername(anyString())).thenReturn(new NotExtendUserContextUserDetailService());
        Assertions.assertThatThrownBy(() ->
                        this.authenticationProvider.authenticate(authentication))
                .isExactlyInstanceOf(UnauthorizedException.class)
                .describedAs("'LoginRequestAuthenticationProvider' 는 'UserContext' 타입만 처리할 수 있다.");


        class ExtendUserContextUserDetailService extends UserContext {
        }

        when(this.userDetailsService.loadUserByUsername(anyString())).thenReturn(new ExtendUserContextUserDetailService());
        assertThatThrownBy(() -> {
            this.authenticationProvider.authenticate(authentication);
        }).isNotExactlyInstanceOf(UnauthorizedException.class)
                .describedAs("'UserContext' 의 하위 클래스는 모두 지원한다. 다른 코드에서 예외가 발생하지만 " +
                        "'UnauthorizedException' 가 발생해서는 안된다.");
    }

    @DisplayName("[FAIL] 패스워드가 조회해온 사용자 정보의 패스워드와 일치하지 않는 경우 'BadCredentialsException' 가 발생한다.")
    @Test
    void 패스워드가_일치하지_않는_경우_테스트() {
        final LoginRequest authentication = getMockLoginRequest();
        final UserContext mockUserContext = this.getMockUserContext();
        when(this.userDetailsService.loadUserByUsername(anyString()))
                .thenReturn(mockUserContext);

        // 패스워드가 확인 시 'false' 를 반환
        when(this.passwordEncoder.matches(any(), anyString()))
                .thenReturn(false);

        Assertions.assertThatThrownBy(() -> {
                    this.authenticationProvider.authenticate(authentication);
                }).isExactlyInstanceOf(BadCredentialsException.class)
                .describedAs("패스워드가 일치하지 않는 경우 'BadCredentialsException' 가 발생해야한다.");
    }

    @DisplayName("[SUCCESS] 로그인 인증 성공 - 인증 객체를 반환하고 Session 이 생성되어야 한다.")
    @Test
    void 인증_성공_테스트() {
        final LoginRequest authentication = getMockLoginRequest();
        final UserContext mockUserContext = getMockUserContext();
        when(this.userDetailsService.loadUserByUsername(anyString())).thenReturn(mockUserContext);
        // 패스워드가 확인 시 'true' 를 반환
        when(this.passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);

        // 인증이 완료되면 세션을 생성한다. 'HttpServletRequest' 를 'RequestContextHolder' 를 통해 접근하기 때문 목킹 필요
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(null);
        final ServletRequestAttributes attribute = new ServletRequestAttributes(request, null);
        RequestContextHolder.setRequestAttributes(attribute);

        final Authentication result = this.authenticationProvider.authenticate(authentication);

        // 인증이 완료된 객체를 반환해야한다.
        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result).isExactlyInstanceOf(AuthenticatedUser.class);

        // 세션이 강제로 생성되어야 한다.
        final HttpSession session = request.getSession(false);
        assertThat(session).isNotNull();
    }

    private UserContext getMockUserContext() {
        final UserContext mockUserContext = mock(UserContext.class);
        when(mockUserContext.getPassword()).thenReturn("password");
        return mockUserContext;
    }

    private LoginRequest getMockLoginRequest() {
        final LoginRequest authentication = mock(LoginRequest.class);
        when(authentication.getCredential()).thenReturn("password");
        when(authentication.getPrincipal()).thenReturn("email@test.com");
        return authentication;
    }

    public static Stream<Arguments> invalidLoginRequestArguments() {
        return Stream.of(
                of(LoginRequest.of("", "somePassword"))
                , of(LoginRequest.of("someEmail@email.com", ""))
                , of(LoginRequest.of(null, "somePassword"))
                , of(LoginRequest.of("someEmail@email.com", null))
        );
    }

}