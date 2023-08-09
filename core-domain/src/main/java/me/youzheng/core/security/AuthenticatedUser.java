package me.youzheng.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;

public class AuthenticatedUser implements Authentication, CredentialsContainer {

    private UserContext userContext;

    private boolean authenticated;

    private boolean isErasedCredential = false;

    private String sessionId;

    protected AuthenticatedUser() {
    }

    protected AuthenticatedUser(final UserContext userContext) {
        this.userContext = userContext;
    }

    public static AuthenticatedUser authenticate(final UserContext userContext) {
        if (userContext == null) {
            throw new IllegalArgumentException("'UserContext' 는 필수입니다.");
        }
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(userContext);
        authenticatedUser.setAuthenticated(true);
        return authenticatedUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(this.userContext.getAuthorities())) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(this.userContext.getAuthorities());
    }

    @Override
    public Object getCredentials() {
        if (this.isErasedCredential) {
            return "";
        }
        return this.userContext.getPassword();
    }

    public UserContext getUserContext() {
        return this.userContext;
    }

    @Override
    public Object getDetails() {
        return this.userContext;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext.getUserId();
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return String.valueOf(this.getPrincipal());
    }

    @Override
    public void eraseCredentials() {
        this.isErasedCredential = true;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

}