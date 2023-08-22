package me.youzheng.core.configure.security.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.session.SessionRegistry;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SessionLogoutFilterTest extends AbstractFilterTest {

    // dependency objects
    SessionLogoutFilter sessionLogoutFilter;
    String pattern;
    SessionRegistry sessionRegistry;

    // mock
    @BeforeEach
    void setUp() {
        this.pattern = "/api/logout";
        this.sessionRegistry = mock(SessionRegistry.class);
        this.sessionLogoutFilter = new SessionLogoutFilter(pattern, sessionCookieName, sessionRegistry, objectMapper);
    }

    @DisplayName("[FAIL] 세션정보가 없는 요청 로그아웃 테스트")
    @Test
    void 세션정보가_없는_요청_로그아웃_테스트() throws ServletException, IOException {
        this.request.setServletPath(this.pattern);
        // request 에 session 정보가 존재하지 않는다.
        this.request.setRequestedSessionId(null);
        this.request.setSession(null);

        this.sessionLogoutFilter.doFilter(this.request, this.response, this.filterChain);

        final HttpStatus expectedHttpStatus = HttpStatus.BAD_REQUEST;
        assertThat(this.response).extracting(MockHttpServletResponse::getStatus)
                .describedAs("잘못된 로그아웃 요청에는 %s[%d] 을 반환해야한다.", expectedHttpStatus.name(), expectedHttpStatus.value())
                .isEqualTo(expectedHttpStatus.value());

        verify(this.sessionRegistry, never()
                .description("Session 정보가 존재하지 않기 때문에 세션 삭제 호출이 발생하면 안된다."))
                .removeSessionInformation(any());
    }

    @DisplayName("[SUCCESS] 로그아웃 테스트 - 세션쿠키 삭제, 세션저장소 호출 등 로직 테스트")
    @Test
    void 로그아웃_테스트() throws ServletException, IOException {
        this.request.setServletPath(this.pattern);
        this.request.setRequestedSessionId(this.sessionId);
        this.request.setSession(this.session);

        this.sessionLogoutFilter.doFilter(this.request, this.response, this.filterChain);

        final Cookie cookie = this.response.getCookie(this.sessionCookieName);

        final HttpStatus expectedHttpStatus = HttpStatus.OK;
        assertThat(this.response).extracting(MockHttpServletResponse::getStatus)
                .describedAs("로그아웃 결과로는 %s[%d] 을 반환해야한다.", expectedHttpStatus.name(), expectedHttpStatus.value())
                .isEqualTo(expectedHttpStatus.value());

        assertThat(cookie).extracting(Cookie::getMaxAge)
                .isEqualTo(0)
                .describedAs("사용자의 쿠키를 삭제해야한다.");

        verify(this.sessionRegistry, times(1)
                .description("'SessionRegistry' 를 통해 세션 ID 에 해당하는 세션 데이터를 삭제해야한다."))
                .removeSessionInformation(this.sessionId);
    }

}