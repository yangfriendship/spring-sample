package me.youzheng.core.domain.userrole.entity;

import lombok.*;
import me.youzheng.core.domain.common.BaseEntity;

import javax.persistence.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_roles")
@Entity
@IdClass(UserRolePk.class)
@EqualsAndHashCode(of = {"userId", "roleId"}, callSuper = false)
public class UserRole extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "is_use")
    private Boolean use;

    @Builder
    public static UserRole of(final Long userId, final Long roleId, final boolean use) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId 가 잘못되었습니다. userId: " + userId);
        }
        if (roleId == null || roleId <= 0) {
            throw new IllegalArgumentException("roleId 가 잘못되었습니다. roleId: " + roleId);
        }
        return new UserRole(userId, roleId, use);
    }

    public boolean matchRoleId(final Long roleId) {
        return this.roleId.equals(roleId);
    }

}