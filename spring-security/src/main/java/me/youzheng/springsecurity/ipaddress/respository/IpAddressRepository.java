package me.youzheng.springsecurity.ipaddress.respository;

import me.youzheng.springsecurity.ipaddress.entity.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long>, IpAddressRepositorySupporter {

}