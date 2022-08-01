package me.youzheng.springsecurity.ipaddress.respository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.ipaddress.dto.IpAddressDto;

import java.util.List;

import static me.youzheng.springsecurity.ipaddress.entity.QIpAddress.ipAddress;

@RequiredArgsConstructor
public class IpAddressRepositorySupporterImpl implements IpAddressRepositorySupporter {

    private final JPAQueryFactory query;

    @Override
    public List<IpAddressDto> findAllActive() {
        return this.query.select(Projections.fields(IpAddressDto.class,
                        ipAddress.ipNo,
                        ipAddress.ip,
                        ipAddress.isUse
                )).from(ipAddress)
                .where(ipAddress.isUse.isTrue())
                .fetch();
    }

}
