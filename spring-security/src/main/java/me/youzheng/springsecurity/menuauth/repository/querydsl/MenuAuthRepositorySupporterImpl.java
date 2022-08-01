package me.youzheng.springsecurity.menuauth.repository.querydsl;


import static me.youzheng.springsecurity.menuauth.entity.QMenuAuth.menuAuth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MenuAuthRepositorySupporterImpl implements
    MenuAuthRepositorySupporter {

    private final JPAQueryFactory query;

    @Override
    public long updateAllUseByMenuNo(Long menuNo, boolean isUse) {
        return this.query.update(menuAuth)
            .where(menuAuth.menu.menuNo.eq(menuNo))
            .set(menuAuth.isUse, isUse)
            .execute();
    }
}
