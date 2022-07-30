package me.youzheng.springsecurity.security.util;

import me.youzheng.springsecurity.security.principal.UserContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtilImpl implements SecurityUtil {

    @Override
    public UserContext getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserContext)) {
            return null;
        }
        try {
            return (UserContext) principal;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long getUserPrimaryKey() {
        UserContext currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        } else {
            return currentUser.getUserInfo().getUserNo();
        }
    }

    @Override
    public boolean isOwner(Long userNo) {
        Long currentUserNo = getUserPrimaryKey();
        return currentUserNo != null && currentUserNo.equals(userNo);
    }

}