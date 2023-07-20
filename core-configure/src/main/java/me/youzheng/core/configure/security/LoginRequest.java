package me.youzheng.core.configure.security;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.youzheng.core.exception.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest implements Authentication {

    public static final String ROLE_NAME = "REQUEST_TOKEN";

    @JsonAlias({"email"})
    private String principal;

    @JsonAlias("password")
    private String credential;

    public static LoginRequest of(final String principal, final String credential) {
        return new LoginRequest(principal, credential);
    }

    public void validate() throws BadRequestException {
        if (!StringUtils.hasLength(this.principal)) {
            throw new BadRequestException("이메일을 입력해주세요.");
        }
        if (!StringUtils.hasLength(this.credential)) {
            throw new BadRequestException("패스워드를 입력해주세요.");
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_REQUEST_TOKEN");
    }

    @Override
    public Object getCredentials() {
        return this.credential;
    }

    @Override
    public Object getDetails() {
        return this.principal;
    }

    /**
     * 인증 프로세스에서 사용되는 객체이므로 isAuthenticated() 는 항상 false 를 반환
     * 인증에 성공하면 다른 객체로 변경하여 SecurityContextHolder 에 넣어준다.
     */
    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * 인증 과정에서만 사용되기 때문에 authenticated 값을 변경할 수 없다.
     */
    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            return;
        }
        throw new UnsupportedOperationException("LoginRequest 는 인증이 완료된 후 다른 인증 객체로 변환해야 합니다.");
    }

    @Override
    public String getName() {
        return this.principal;
    }

}