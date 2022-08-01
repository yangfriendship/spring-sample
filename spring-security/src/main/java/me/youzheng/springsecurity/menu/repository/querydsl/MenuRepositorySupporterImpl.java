package me.youzheng.springsecurity.menu.repository.querydsl;

import static me.youzheng.springsecurity.menu.entity.QMenu.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.menu.dto.MenuDto;

@RequiredArgsConstructor
public class MenuRepositorySupporterImpl implements
    MenuRepositorySupporter {

    private final JPAQueryFactory query;

    @Override
    public long updateUseState(Long menuNo, boolean isUse) {
        return this.query.update(menu)
            .where(menu.menuNo.eq(menuNo))
            .set(menu.isUse, isUse)
            .execute();
    }

    @Override
    public boolean existsByHttpMethodAndUrl(String httpMethod, String patternString) {
        return this.query.selectOne()
            .from(menu)
            .where(menu.url.eq(patternString)
                .and(menu.httpMethod.eq(httpMethod)))
            .fetchFirst() != null;
    }

    @Override
    public List<MenuDto> findActiveMenus() {
        return this.query.select(Projections.fields(
                MenuDto.class,
                menu.menuNo,
                menu.menuName,
                menu.httpMethod,
                menu.url,
                menu.menuType,
                menu.isUse
            ))
            .from(menu)
            .where(menu.isUse.isTrue())
            .fetch();
    }

}
