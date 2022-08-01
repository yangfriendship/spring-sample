package me.youzheng.springsecurity.ipaddress.converter;

import com.google.common.net.InetAddresses;
import javax.persistence.AttributeConverter;

public class IpAddressConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String ipAddressAsString) {
        return InetAddresses.coerceToInteger(InetAddresses.forString(ipAddressAsString));
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return InetAddresses.fromInteger(dbData).getHostAddress();
    }

}