package me.youzheng.springsecurity.security.principal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo implements Cloneable {

    private Long userNo;

    private String userId;

    @JsonIgnore
    private String password;

    private Long menuAuthGroupNo;

    public UserInfo copy() {
        try {
            return (UserInfo) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return UserInfo.builder()
            .userNo(this.userNo)
            .userId(this.userId)
            .password(this.password)
            .menuAuthGroupNo(this.menuAuthGroupNo)
            .build();
    }

    public void removePassword() {
        this.password = null;
    }
}