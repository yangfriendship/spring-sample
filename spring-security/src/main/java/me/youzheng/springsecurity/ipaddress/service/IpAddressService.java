package me.youzheng.springsecurity.ipaddress.service;

import me.youzheng.springsecurity.ipaddress.dto.IpAddressDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
public interface IpAddressService {

    @Transactional(readOnly = true)
    List<IpAddressDto> findAllActive();
}
