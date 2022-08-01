package me.youzheng.springsecurity.ipaddress.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.youzheng.springsecurity.ipaddress.dto.IpAddressDto;
import me.youzheng.springsecurity.ipaddress.respository.IpAddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IpAddressServiceImpl implements IpAddressService {

    private final IpAddressRepository ipAddressRepository;

    @Transactional(readOnly = true)
    @Override
    public List<IpAddressDto> findAllActive() {
        return this.ipAddressRepository.findAllActive();
    }

}
