package me.youzheng.springsecurity.ipaddress.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IpAddressDto {
    private Long ipNo;

    private String ip;

    private boolean isUse;

    @JsonIgnore
    public boolean isMatchIp(String that) {
        return ip.equals(that);
    }

}
