package me.youzheng.core.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import me.youzheng.core.domain.common.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "User")
@Table(name = "users")
@DynamicUpdate
public class User extends BaseEntity implements Comparable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    @Column(name = "is_use")
    private Boolean use;

    @Column(name = "is_lock")
    private Boolean lock;

    private LocalDateTime passwordExpireDateTime;

    private LocalDateTime lastLoginDateTime;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_roles"
            , joinColumns =
    @JoinColumn(
            name = "user_id"
            , insertable = false
            , updatable = false)
    )
    @Column(name = "role_id")
    @Builder.Default
    private Set<Long> roles = new HashSet<>();

    public void passwordExpireAt(final LocalDateTime passwordExpireDateTime) {
        this.passwordExpireDateTime = passwordExpireDateTime;
    }

    @JsonIgnore
    public boolean isPasswordExpired() {
        if (this.passwordExpireDateTime == null) {
            return true;
        }
        return this.passwordExpireDateTime.isAfter(LocalDateTime.now());
    }

    @Override
    public int compareTo(final User that) {
        return this.userId.compareTo(that.userId);
    }

    public void changePassword(final String password) {
        if (!StringUtils.hasLength(password)) {
            throw new IllegalArgumentException("Password can't be empty");
        }
        this.password = password;
    }

    public Set<Long> getRoles() {
        return Collections.unmodifiableSet(this.roles);
    }

    public boolean hasRole(final Long role) {
        return this.roles.contains(role);
    }

}