package me.youzheng.core.configure.security.filter;

import me.youzheng.core.configure.security.ExceptionHandler;
import me.youzheng.core.util.WebUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * <p>
 * {@link org.springframework.security.web.access.ExceptionTranslationFilter} 이전에 발생하는 예외를 처리하는 필터 <br/>
 * {@link me.youzheng.core.web.ApiResponse} 로 랩핑되어 반환된다.
 * </p>
 */
public class PreExceptionFilter extends GenericFilterBean {

    private final ExceptionHandler exceptionHandler;

    public PreExceptionFilter(final ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception exception) {
            WebUtils.setDefaultContentType(response);
            WebUtils.resetBuffer(response);

            this.exceptionHandler.handle(request, response, exception);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}