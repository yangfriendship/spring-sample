package me.youzheng.core.configure.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import me.youzheng.core.configure.security.exception.TestException;
import me.youzheng.core.security.ExceptionHttpStatusResolver;
import me.youzheng.core.test.TestUtils;
import me.youzheng.core.web.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.json.JsonContentAssert;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultExceptionHandlerTest {

    DefaultExceptionHandler exceptionHandler;
    ExceptionHttpStatusResolver exceptionHttpStatusResolver;

    ObjectMapper objectMapper;

    MockHttpServletRequest request;

    MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        this.exceptionHttpStatusResolver = mock(ExceptionHttpStatusResolver.class);
        this.objectMapper = spy(new Jackson2ObjectMapperBuilder().build());
        this.exceptionHandler = new DefaultExceptionHandler(this.exceptionHttpStatusResolver,
                this.objectMapper);

        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @ParameterizedTest
    @MethodSource("me.youzheng.core.configure.security.exception.TestException#exceptionMethodSources")
    void 예외처리_핸들링_테스트_성공(final Exception exception, final HttpStatus httpStatus, final String exceptionMessage) throws IOException {
        when(this.exceptionHttpStatusResolver.resolve(any())).thenReturn(httpStatus);
        this.exceptionHandler.handle(this.request, this.response, exception);

        assertThat(this.response.getStatus()).isEqualTo(httpStatus.value());

        final String result = TestUtils.extractJsonBody(this.response);

        // ApiResponse.class 에 랩핑되어 결과가 반환된다.
        final JsonContentAssert jsonContentAssert = new JsonContentAssert(ApiResponse.class, result);
        jsonContentAssert.extractingJsonPathMapValue("body").isNull();
        jsonContentAssert.extractingJsonPathBooleanValue("success").isFalse();
        jsonContentAssert.extractingJsonPathMapValue("error").isNotNull();
        jsonContentAssert.extractingJsonPathStringValue("error.code").isNotNull();
        jsonContentAssert.extractingJsonPathStringValue("error.message").isEqualTo(exceptionMessage);
    }

    @Test
    void IOException_핸들링_테스트() throws IOException {
        final ObjectWriter mockObjectWriter = mock(ObjectWriter.class);
        when(this.exceptionHttpStatusResolver.resolve(any())).thenReturn(HttpStatus.OK);
        when(this.objectMapper.writer()).thenReturn(mockObjectWriter);
        final IOException ioException = mock(IOException.class);
        doThrow(ioException).when(mockObjectWriter)
                .writeValue(any(JsonGenerator.class), any(Object.class));

        Assertions.assertDoesNotThrow(() ->
                this.exceptionHandler.handle(this.request, this.response, new TestException.DontHaveResponseStatusException())
        );

        // 예외 메시지를 생성하는 과정에서 IOException 이 발생할 경우 500 예외로 반환하며 별다른 메시지는 남기지 않는다.
        final String result = TestUtils.extractJsonBody(this.response);
        assertThat(result).isEmpty();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

        // stackTrace 를 출력
        verify(ioException, times(1)).printStackTrace();
    }

}