package me.youzheng.core.configure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.*;

import javax.servlet.FilterChain;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public abstract class AbstractFilterTest {
    // mocks
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    FilterChain filterChain;

    MockHttpSession session;
    MockServletContext servletContext;
    String sessionId;

    String sessionCookieName;

    ObjectMapper objectMapper;


    @BeforeEach
    void firstSetup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.session = new MockHttpSession(this.servletContext, this.sessionId);
        this.servletContext = new MockServletContext();
        this.objectMapper = new Jackson2ObjectMapperBuilder().build();
        this.filterChain = mock(FilterChain.class);
        this.sessionId = UUID.randomUUID().toString();
        this.sessionCookieName = "YSESSION";

    }

}
