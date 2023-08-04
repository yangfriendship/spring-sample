package me.youzheng.apiserver.config.security;

import me.youzheng.core.domain.user.repository.UserRepository;
import me.youzheng.core.security.UserContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class ApiServerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApiServerUserDetailService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return this.userRepository.findWithRolesByEmail(email)
                .map(UserContext::of)
                .orElse(null);
    }

}