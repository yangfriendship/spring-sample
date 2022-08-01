package me.youzheng.springsecurity.security.voter;

import java.util.Collection;
import java.util.List;

import com.google.common.net.InetAddresses;
import me.youzheng.springsecurity.ipaddress.dto.IpAddressDto;
import me.youzheng.springsecurity.ipaddress.service.IpAddressService;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class IpAddressAccessVoter implements AccessDecisionVoter<Object> {

    private final IpAddressService ipAddressService;

    public IpAddressAccessVoter(IpAddressService ipAddressService) {
        this.ipAddressService = ipAddressService;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // TODO 클래스 변수로 추출하고, IpAddress 테이블에 변화가 있을때 값을 변환하도록 변경해야된다. 테스트 진행을 위해 매 요청 마다 조회하도록 만듬
        List<IpAddressDto> ipAddressDtos = this.ipAddressService.findAllActive();

        FilterInvocation filterInvocation = (FilterInvocation) object;
        HttpServletRequest httpRequest = filterInvocation.getHttpRequest();
        String ipAddress = httpRequest.getRemoteAddr();

        int result = ACCESS_ABSTAIN;
        for (IpAddressDto ipAddressDto : ipAddressDtos) {
            if (ipAddressDto.isMatchIp(ipAddress)) {
                result = ACCESS_DENIED;
                break;
            }
        }
        if (result == ACCESS_DENIED) {
            throw new AccessDeniedException("IP 접근 제한");
        }

        return result;
    }

}