package me.youzheng.core.configure.security.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import me.youzheng.core.configure.security.LoginRequest;
import me.youzheng.core.exception.BaseLoginRequestException;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Login 처리 필터.
 */
public class ApiLoginProcessFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public ApiLoginProcessFilter(final String processesUrl, final ObjectMapper objectMapper) {
        super(processesUrl);
        this.objectMapper = objectMapper;
    }

    public ApiLoginProcessFilter(final String processesUrl) {
        super(processesUrl);
        this.objectMapper = new Jackson2ObjectMapperBuilder().build();
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException, IOException {
        final LoginRequest loginRequest = this.getLoginRequest(request);
        return this.getAuthenticationManager().authenticate(loginRequest);
    }

    /**
     * ContentBody 에서 사용자가 보낸 Login 정보를 {@link LoginRequest} 로 변환한다. 요청정보 변환에서 문제가 발생 또는 잘못된 요청정보일 경우
     * {@link BaseLoginRequestException} 를 발생시킨다.
     * @return 인증 정보가 맵핑된 객체
     */
    private LoginRequest getLoginRequest(final HttpServletRequest request) {
        return Try.of(() -> {
                    final InputStream inputStream = StreamUtils.nonClosing(request.getInputStream());
                    final JsonParser jsonParser = this.objectMapper.getFactory().createParser(inputStream);
                    if (jsonParser.nextToken() != JsonToken.START_OBJECT) {
                        throw new BaseLoginRequestException();
                    }
                    final LoginRequest loginRequest = jsonParser.readValueAs(LoginRequest.class);
                    loginRequest.validate();
                    return loginRequest;
                }).onFailure(JsonParseException.class, e -> {
                    throw new BaseLoginRequestException();
                })
                .get();
    }

}