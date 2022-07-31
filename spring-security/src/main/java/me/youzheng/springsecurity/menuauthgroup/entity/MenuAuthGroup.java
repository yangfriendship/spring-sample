package me.youzheng.springsecurity.menuauthgroup.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.youzheng.springsecurity.user.entity.User;
import me.youzheng.springsecurity.menuauth.entity.MenuAuth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuAuthGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuAuthGroupNo;

    @Column(length = 50)
    private String groupName;

    @Column(nullable = false)
    private boolean isUse;

    @Column(length = 100)
    private String description;

    @OneToMany(mappedBy = "menuAuthGroup")
    private List<MenuAuth> menuAuths;

    @OneToMany(mappedBy = "menuAuthGroup")
    private List<User> users;

}