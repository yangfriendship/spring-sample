package me.youzheng.springsecurity.menuauth.repository.querydsl;

public interface MenuAuthRepositorySupporter {

    long updateAllUseByMenuNo(Long menuNo, boolean isUse);
}
