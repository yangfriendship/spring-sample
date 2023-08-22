package me.youzheng.core.configure.security.filter;

import me.youzheng.core.configure.security.LoginRequest;
import me.youzheng.core.exception.BadLoginRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class ApiLoginProcessFilterTest extends AbstractFilterTest {

    String AUTH_PROCESSING_URI = "/someUri";
    ApiLoginProcessFilter apiLoginProcessFilter;
    FilterChain chain;
    AuthenticationManager providerManager;

    @BeforeEach
    void setUp() {
        this.providerManager = mock(ProviderManager.class);
        this.apiLoginProcessFilter = new ApiLoginProcessFilter(AUTH_PROCESSING_URI, this.objectMapper);
        this.apiLoginProcessFilter.setAuthenticationManager(this.providerManager);
        this.request.setServletPath(this.AUTH_PROCESSING_URI);
    }

    /**
     * 실제 인증 처리는 AuthenticationProvider 구현에서 하기 때문에 ApiLoginProcessFilter 에서는 객체 생성에 대한 테스트만 진행
     */
    @DisplayName("[SUCCESS] 로그인 처리")
    @Test
    void 로그인_처리_테스트_성공() throws ServletException, IOException {
        final LoginRequest loginRequest = LoginRequest.of("someEmail", "somePassword");
        final String content = this.objectMapper.writeValueAsString(loginRequest);
        final byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        this.request.setContent(contentBytes);

        this.apiLoginProcessFilter.doFilter(this.request, this.response, this.chain);

        // AuthenticationManager 에게 위임했는지 확인
        verify(this.providerManager, times(1)).authenticate(any());
    }

    @ParameterizedTest(name = "[SUCCESS] 로그인 실패 - 잘못된 요청형식: {0}")
    @MethodSource("badLoginRequestArguments")
    void 로그인_처리_테스트_실패(final LoginRequest loginRequest, final String reason) throws IOException, ServletException {
        final String content = this.objectMapper.writeValueAsString(loginRequest);
        final byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        this.request.setContent(contentBytes);

        Assertions.assertThatThrownBy(() -> {
            this.apiLoginProcessFilter.doFilter(request, response, chain);
        }).isExactlyInstanceOf(BadLoginRequestException.class);
    }

    public static Stream<Arguments> badLoginRequestArguments() {
        return Stream.of(
                Arguments.of(LoginRequest.of("", "somePassword"), "이메일 누락")
                , Arguments.of(LoginRequest.of(null, ""), "이메일 누락")
                , Arguments.of(LoginRequest.of("someEmail@test.com", ""), "패스워드 누락")
                , Arguments.of(LoginRequest.of("someEmail@test.com", null), "패스워드 누락")
        );
    }

}