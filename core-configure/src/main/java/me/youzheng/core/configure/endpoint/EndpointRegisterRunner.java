package me.youzheng.core.configure.endpoint;

import me.youzheng.core.domain.endpoint.entity.Endpoint;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EndpointRegisterRunner implements ApplicationRunner, EnvironmentAware, ApplicationContextAware {

    public static final String APPLICATION_NAME_PROPERTY = "spring.application.name";

    private ApplicationContext applicationContext;

    private final EndpointInfoRegister endpointInfoRegister;

    private String applicationName;

    public EndpointRegisterRunner(final EndpointInfoRegister endpointInfoRegister) {
        this.endpointInfoRegister = endpointInfoRegister;
    }

    @Override
    public void run(final ApplicationArguments args) {
        final List<Endpoint> result = new ArrayList<>();

        final Map<String, Object> controllers = this.applicationContext.getBeansWithAnnotation(Controller.class);
        final Collection<Object> controllerBeans = controllers.values();

        for (final Object controllerBean : controllerBeans) {
            final String pathPrefix = getPathPrefix(controllerBean);
            final Method[] endpointMethods = ReflectionUtils.getAllDeclaredMethods(controllerBean.getClass());
            final List<Endpoint> endpoints = Stream.of(endpointMethods)
                    .filter(EndpointRegisterRunner::hasRequestMapping)
                    .map(EndpointRegisterRunner::getRequestMapping)
                    .map(a -> this.createEndpointInfo(pathPrefix, a))
                    .distinct()
                    .collect(Collectors.toList());
            result.addAll(endpoints);
        }

        this.endpointInfoRegister.register(result);
    }

    private String getPathPrefix(final Object controllerBean) {
        String pathPrefix = "";
        final RequestMapping classRequestMapping =
                AnnotationUtils.findAnnotation(controllerBean.getClass(), RequestMapping.class);
        if (classRequestMapping != null) {
            pathPrefix = getFirstPath(classRequestMapping);
        }
        return pathPrefix;
    }

    private static RequestMapping getRequestMapping(final Method method) {
        return AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
    }

    private static boolean hasRequestMapping(final Method controllerMethod) {
        return getRequestMapping(controllerMethod) != null;
    }

    /**
     * path, httpMethod 가 배열로 들어오더라도 첫번째 값을 이용하여 Endpoint 인스턴스를 생성함.
     *
     * @param prefixPath     Class 에 정의된 RequestMapping path 정보
     * @param requestMapping Method 에 정의된 RequestMapping 정보
     */
    protected Endpoint createEndpointInfo(final String prefixPath, final RequestMapping requestMapping) {
        final RequestMethod[] method = requestMapping.method();
        return Endpoint.builder()
                .httpMethod(getFirstHttpMethod(method))
                .path(prefixPath + getFirstPath(requestMapping))
                .applicationName(this.applicationName)
                .build();
    }

    private HttpMethod getFirstHttpMethod(final RequestMethod[] method) {
        return HttpMethod.valueOf(method[0].name());
    }

    private String getFirstPath(final RequestMapping requestMapping) {
        return requestMapping.path()[0];
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.applicationName = environment.getProperty(APPLICATION_NAME_PROPERTY);
    }

}