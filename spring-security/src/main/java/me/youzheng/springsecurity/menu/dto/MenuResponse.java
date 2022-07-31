package me.youzheng.springsecurity.menu.dto;

import lombok.Data;
import me.youzheng.springsecurity.menu.entity.MenuType;

@Data
public class MenuResponse {

    private Long menuNo;

    private String menuName;

    private String httpMethod;

    private String url;

    private MenuType menuType;

}
