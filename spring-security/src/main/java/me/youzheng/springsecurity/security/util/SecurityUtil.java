package me.youzheng.springsecurity.security.util;


import me.youzheng.springsecurity.security.principal.UserContext;

public interface SecurityUtil {

    UserContext getCurrentUser();

    Long getUserPrimaryKey();

    boolean isOwner(Long userNo);

}