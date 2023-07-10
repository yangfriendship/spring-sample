package me.youzheng.core.domain.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.youzheng.core.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserContext implements UserDetails {

    private final User user;

    protected UserContext(final User user) {
        if (user == null || user.getId() == null) {
            throw new RuntimeException();
        }
        this.user = user;
    }

    public static UserContext of(final User user) {
        return new UserContext(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getLock() != null && this.user.getLock();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.isPasswordExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.isAccountNonLocked()
                && this.isCredentialsNonExpired();
    }

    @JsonIgnore
    public Long getUserId() {
        return this.user.getId();
    }

}
