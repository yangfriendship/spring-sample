package me.youzheng.core.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.youzheng.core.domain.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String email;

    private String username;

    private String password;

    @Column(name = "is_use")
    private Boolean use;

    @Column(name = "is_lock")
    private Boolean lock;

    private LocalDateTime passwordExpireDateTime;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void passwordExpireAt(final LocalDateTime passwordExpireDateTime) {
        this.passwordExpireDateTime = passwordExpireDateTime;
    }

    @JsonIgnore
    public boolean isPasswordExpired() {
        return this.passwordExpireDateTime.isAfter(LocalDateTime.now());
    }

}