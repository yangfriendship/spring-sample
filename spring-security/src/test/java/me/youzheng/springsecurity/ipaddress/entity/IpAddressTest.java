package me.youzheng.springsecurity.ipaddress.entity;

import javax.persistence.EntityManager;
import me.youzheng.springsecurity.util.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IpAddressTest {

    @Autowired
    EntityManager entityManager;

    @Rollback(value = false)
    @Test
    public void ipAddressConvertTest_string_to_integer() {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("127.0.0.1");
        ipAddress.setUse(true);
        this.entityManager.persist(ipAddress);
        System.out.println();
    }

    @Test
    public void ipAddressConvertTest_integer_to_string() {

    }
}