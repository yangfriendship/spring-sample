package me.youzheng.springsecurity.menuauth.entity;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.youzheng.springsecurity.menuauthgroup.entity.MenuAuthGroup;
import me.youzheng.springsecurity.menu.entity.Menu;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuAuthNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuNo")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuAuthGroupNo")
    private MenuAuthGroup menuAuthGroup;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
            o)) {
            return false;
        }
        MenuAuth menuAuth = (MenuAuth) o;
        return menuAuthNo != null && Objects.equals(menuAuthNo, menuAuth.menuAuthNo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}