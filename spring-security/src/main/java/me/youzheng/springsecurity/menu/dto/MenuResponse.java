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

    private boolean isUse;

    public static MenuResponse from(MenuDto source) {
        MenuResponse result = new MenuResponse();
        result.setMenuNo(source.getMenuNo());
        result.setMenuName(source.getMenuName());
        result.setMenuType(source.getMenuType());
        result.setUrl(source.getUrl());
        result.setUse(source.isUse());
        result.setHttpMethod(source.getHttpMethod());
        return result;
    }
}