package me.youzheng.springsecurity.menu.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.youzheng.springsecurity.menu.entity.Menu;
import me.youzheng.springsecurity.menu.entity.MenuType;

@Data
@EqualsAndHashCode(of = {"menuNo"})
public class MenuDto implements Comparable<MenuDto> {

    private Long menuNo;

    private String menuName;

    private String httpMethod;

    private String url;

    private MenuType menuType;

    private boolean isUse;

    public static MenuDto from(Menu source) {
        MenuDto result = new MenuDto();
        result.setMenuNo(source.getMenuNo());
        result.setMenuName(source.getMenuName());
        result.setHttpMethod(source.getHttpMethod());
        result.setUrl(source.getUrl());
        result.setUse(source.isUse());
        result.setMenuType(source.getMenuType());
        return result;
    }

    @Override
    public int compareTo(MenuDto that) {
        return this.menuNo.compareTo(that.menuNo);
    }

}