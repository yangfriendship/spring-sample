package me.youzheng.springsecurity.menu.dto;

import lombok.Data;
import me.youzheng.springsecurity.menu.entity.Menu;
import me.youzheng.springsecurity.menu.entity.MenuType;

@Data
public class MenuRegisterRequest {

    private String menuName;

    private String httpMethod;

    private String url;

    private MenuType menuType;

    private boolean isUse;

    public Menu toEntity() {
        return Menu.builder()
            .menuName(this.menuName)
            .httpMethod(this.httpMethod)
            .url(this.url)
            .isUse(this.isUse)
            .build();
    }

}