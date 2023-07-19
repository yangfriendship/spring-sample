package me.youzheng.core.security;

import me.youzheng.core.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserContext implements UserDetails {

    public static final String ROLE_PREFIX = "ROLE_";
    private User user;

    /**
     * Redis 에서 Deserialize 될 때 필요. Mixin 을 따로 구현할 때까지 삭제 금지
     */
    protected UserContext() {
    }

    protected UserContext(final User user) {
        if (user == null) {
            throw new IllegalArgumentException("'User' null 이 될 수 없습니다.");
        }
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("'userId(Long)' null 이 될 수 없습니다.");
        }
        this.user = user;
    }

    public static UserContext of(final User user) {
        return new UserContext(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(this.user.getRoles())
                .map(this::convertToAuthority)
                .orElse(Collections.emptyList());
    }

    private GrantedAuthority convertToAuthority(final Object role) {
        return new SimpleGrantedAuthority(ROLE_PREFIX + role);
    }

    private List<GrantedAuthority> convertToAuthority(final Collection<Long> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(this::convertToAuthority)
                .collect(Collectors.toList());
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
        // 필요시 구현
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
        return !this.user.getLock();
    }

    public Long getUserId() {
        return this.user.getUserId();
    }

    // TODO 원본 객체를 반환하지 않고 복제본을 반환하도록 수정
    public User getUser() {
        return this.user;
    }

}