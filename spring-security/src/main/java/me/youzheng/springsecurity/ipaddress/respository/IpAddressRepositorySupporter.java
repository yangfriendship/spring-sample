package me.youzheng.springsecurity.ipaddress.respository;

import me.youzheng.springsecurity.ipaddress.dto.IpAddressDto;
import java.util.List;

public interface IpAddressRepositorySupporter {

    List<IpAddressDto> findAllActive();
}
