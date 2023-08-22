package me.youzheng.core.configure.security.filter;

import me.youzheng.core.configure.security.ExceptionHandler;
import me.youzheng.core.test.MockSecurityContextHolderStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PreExceptionFilterTest {

    PreExceptionFilter preExceptionFilter;
    ExceptionHandler exceptionHandler;
    FilterChain mockFilterChain;

    MockHttpServletRequest request;

    MockHttpServletResponse response;
    MockSecurityContextHolderStrategy mockSecurityContextHolderStrategy;

    @BeforeEach
    void setUp() {
        this.exceptionHandler = mock(ExceptionHandler.class);
        this.preExceptionFilter = new PreExceptionFilter(this.exceptionHandler);
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.mockFilterChain = mock(FilterChain.class);
        this.mockSecurityContextHolderStrategy = new MockSecurityContextHolderStrategy();
    }

    @DisplayName("[SUCCESS] 필터 체이닝 중 예외가 발생하면 'OutputStream' 을 초기화한다.")
    @Test
    void 예외처리필터_테스트() throws ServletException, IOException {
        doThrow(IOException.class).when(this.mockFilterChain)
                .doFilter(any(), any());

        SecurityContextHolder.setContextHolderStrategy(mockSecurityContextHolderStrategy);

        this.preExceptionFilter.doFilter(request, response, mockFilterChain);

        assertThat(this.mockSecurityContextHolderStrategy.getClearContextCallCount())
                .isEqualTo(1)
                .describedAs("예외 발생 여부에 상관없이 'SecurityContext' 는 초기화되어야 한다.");

        // 예외에 대한 처리는 'ExceptionHandler' 에 위임한다.
        verify(this.exceptionHandler, times(1))
                .handle(any(), any(), any());
    }

}