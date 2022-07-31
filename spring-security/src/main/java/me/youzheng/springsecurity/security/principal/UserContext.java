package me.youzheng.springsecurity.security.principal;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserContext implements UserDetails {

    public static final String ROLE_PREFIX = "ROLE_";
    private final UserInfo user;

    public UserContext(UserInfo user) {
        if (user == null || user.getUserNo() == null) {
            throw new IllegalArgumentException();
        }
        this.user = user;
    }

    public UserInfo getUserInfo() {
        return this.user.copy();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return ImmutableList.of(
            new SimpleGrantedAuthority(ROLE_PREFIX + this.user.getMenuAuthGroupNo().toString()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * @Method Name : clear
     * @작성일 : 2022. 07. 30.
     * @작성자 : W.J.YANG
     * @변경이력 :
     * @Method 설명 : 인증 후, 민감한 정보를 저장하지 않기 위해 정보를 초기화
     */
    public void clear() {
        this.user.removePassword();
    }

}