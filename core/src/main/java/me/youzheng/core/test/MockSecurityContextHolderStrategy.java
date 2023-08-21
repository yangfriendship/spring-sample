package me.youzheng.core.test;

import lombok.Getter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;

@Getter
public class MockSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    private SecurityContext securityContext;
    private int clearContextCallCount = 0;
    private int getContextCallCount = 0;
    private int createEmptyContextCount = 0;
    private int setContextCount = 0;

    @Override
    public void clearContext() {
        this.clearContextCallCount++;
        this.securityContext = null;
    }

    @Override
    public SecurityContext getContext() {
        this.getContextCallCount++;
        return this.securityContext;
    }

    @Override
    public void setContext(final SecurityContext context) {
        this.setContextCount++;
        this.securityContext = context;
    }

    @Override
    public SecurityContext createEmptyContext() {
        this.clearContextCallCount++;
        return new SecurityContextImpl();
    }

    public void clearCount() {
        this.clearContextCallCount = 0;
        this.getContextCallCount = 0;
        this.createEmptyContextCount = 0;
        this.setContextCount = 0;
    }

}