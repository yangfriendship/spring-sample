package me.youzheng.core.configure.endpoint;

import me.youzheng.core.domain.endpoint.entity.Endpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.List;

import static me.youzheng.core.configure.endpoint.EndpointRegisterRunner.APPLICATION_NAME_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class EndpointRegisterRunnerTest {

    static final int ALL_ENDPOINTS_COUNT = 4;

    static final String APPLICATION_NAME = "testWebApplication";

    static final String PROPERTY_NAME = APPLICATION_NAME_PROPERTY;

    public static final String PREFIX_PATH = "/api/test";

    EndpointInfoRegister endpointInfoRegister;

    @RestController
    @RequestMapping(name = "testController", path = PREFIX_PATH)
    static class TestController {

        @PostMapping("/post")
        public void post() {
        }

        @GetMapping("/get")
        public void get() {
        }

        @RequestMapping(value = "/request-mapping", method = RequestMethod.GET)
        public void userRequestMapping() {
        }

    }

    @RestController
    @RequestMapping(name = "testController", path = PREFIX_PATH + "2")
    static class TestController2 {

        // post1, post2 는 동일한 엔드포인트로 인식한다.
        @PostMapping("/post")
        public void post() {
        }

        @PostMapping(value = "/post", params = "name=woojung")
        public void post2() {
        }

        public void nothing() {

        }
    }

    @RestController
    @RequestMapping("/api")
    static class NoneEndPointController {

        public void nothing() {
        }

    }

    EndpointRegisterRunner runner;

    ArgumentCaptor<List<Endpoint>> argumentCaptor;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        final AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(TestController.class);
        ctx.register(TestController2.class);
        ctx.refresh();

        final MockEnvironment environment = new MockEnvironment();
        environment.setProperty(PROPERTY_NAME, APPLICATION_NAME);

        this.endpointInfoRegister = mock(EndpointInfoRegister.class);
        this.runner = new EndpointRegisterRunner(this.endpointInfoRegister);

        this.runner.setEnvironment(environment);
        this.runner.setApplicationContext(ctx);

        final ArgumentCaptor<?> wirdCardArgumentCaptor = ArgumentCaptor.forClass(List.class);
        this.argumentCaptor = (ArgumentCaptor<List<Endpoint>>) wirdCardArgumentCaptor;
    }

    @DisplayName("[SUCCESS] 컨트롤러 정보를 통해 엔드포인트르 생성해야한다.")
    @Test
    void 컨트롤러_정보_엔드포인트_변환_테스트() {
        this.runner.run(null);

        verify(this.endpointInfoRegister, times(1)
                .description("생성된 'Endpoint' 를 'EndpointRegister' 를 통해 저장해야한다."))
                .register(this.argumentCaptor.capture());
        final List<Endpoint> createdEndpoints = this.argumentCaptor.getValue();

        assertThat(createdEndpoints).isNotNull()
                .describedAs("null 또는 빈 리스트를 반환해서는 안된다.")
                .hasSize(ALL_ENDPOINTS_COUNT)
                .describedAs("'%s' 의  중복이 제외된 %s' 개를 반환해야한다.", TestController2.class.getSimpleName()
                        , ALL_ENDPOINTS_COUNT - 1)
                .allMatch(this::hasPath)
                .allMatch(this::startWithPrefixPath)
                .allMatch(this::hasApplicationName)
                .allMatch(this::hasHttpMethod);
    }

    @DisplayName("[SUCCESS] 등록할 엔드포인트가 없더라도 예외를 발생시키지 않아야 한다.")
    @Test
    void 예외_발생유무_테스트() {

        final AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(NoneEndPointController.class);
        ctx.refresh();
        this.runner.setApplicationContext(ctx);

        this.runner.run(null);
        verify(this.endpointInfoRegister, times(1)
                .description("생성된 'Endpoint' 를 'EndpointRegister' 를 통해 저장해야한다."))
                .register(this.argumentCaptor.capture());
        final List<Endpoint> createdEndpoints = this.argumentCaptor.getValue();

        assertThat(createdEndpoints).isEmpty();
    }

    /**
     * Controller 에 정의된 RequestMapping 정보가 바인딩 되었는지 확인
     */
    private boolean startWithPrefixPath(final Endpoint endpoint) {
        return endpoint.getPath().startsWith(PREFIX_PATH);
    }

    /**
     * appllication.yml 에 정의된 어플리케이션명이 바인딩 되었는지 확인
     */
    protected boolean hasApplicationName(final Endpoint endpoint) {
        return endpoint != null && StringUtils.hasLength(endpoint.getApplicationName());
    }

    /**
     * HttpMethod 가 바인딩 되었는지 확인
     */
    protected boolean hasHttpMethod(final Endpoint endpoint) {
        return endpoint != null && endpoint.getHttpMethod() != null;
    }

    /**
     * RequestMapping.path() 정보가 바인딩 되었는지 확인
     */
    protected boolean hasPath(final Endpoint endpoint) {
        return endpoint != null && StringUtils.hasLength(endpoint.getPath());
    }

}