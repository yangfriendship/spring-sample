package me.youzheng.core.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import java.io.OutputStream;

@UtilityClass
public class TestUtils {

    private static ObjectMapper OBJECT_MAPPER = new Jackson2ObjectMapperBuilder().build();

    public static DelegatingServletOutputStream extractServletOutputStream(final MockHttpServletResponse response) {
        Assert.notNull(response, () -> "MockHttpServletResponse can't be null");
        final ServletOutputStream outputStream = response.getOutputStream();
        if (outputStream instanceof DelegatingServletOutputStream) {
            return (DelegatingServletOutputStream) outputStream;
        }
        throw new IllegalArgumentException("DelegatingServletOutputStream 가 존재하지 않습니다.");
    }

    public static OutputStream extractOutputStream(final MockHttpServletResponse response) {
        return extractServletOutputStream(response).getTargetStream();
    }

    public static void writeContent(final MockHttpServletRequest request, final Object source) {
        try {
            request.setContent(OBJECT_MAPPER.writeValueAsBytes(source));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}