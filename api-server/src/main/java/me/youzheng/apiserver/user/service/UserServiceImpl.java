package me.youzheng.apiserver.user.service;

import lombok.RequiredArgsConstructor;
import me.youzheng.apiserver.user.event.UserCreateEvent;
import me.youzheng.apiserver.user.mapper.UserMapper;
import me.youzheng.apiserver.user.payload.*;
import me.youzheng.core.domain.user.dto.UserDto;
import me.youzheng.core.domain.user.dto.UserCriteria;
import me.youzheng.core.domain.user.entity.User;
import me.youzheng.core.domain.user.repository.UserRepository;
import me.youzheng.core.exception.BadRequestException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service(value = "userService")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    private final EntityManager entityManager;

    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(final UserCreateRequest request) {
        if (!request.isMatchPassword()) {
            throw new BadRequestException("확인 패스워드가 일치하지 않습니다.");
        }

        this.checkDuplicatedAttribute(request);
        final User user = this.userMapper.from(request);
        this.bindEncodedPassword(user);

        final User save = this.userRepository.save(user);
        this.entityManager.flush();
        this.eventPublisher.publishEvent(UserCreateEvent.of(save));
        return this.userMapper.toUserDto(save);
    }

    @Override
    public UserDetailResponse fetch(final UserFetchRequest request) {
        if (request == null) {
            return null;
        }
        UserCriteria userCriteria = this.userMapper.criteriaWith(request);
        final UserDto userDto = this.userRepository.findOneBy(userCriteria);
        return this.userMapper.toDetailResponse(userDto);
    }

    @Override
    public boolean checkEmail(final String email) {
        final UserFetchRequest request = UserFetchRequest.builder()
                .email(email)
                .build();
        return this.fetch(request) != null;
    }

    @Override
    public boolean checkNickname(final String nickname) {
        final UserFetchRequest request = UserFetchRequest.builder()
                .nickname(nickname)
                .build();
        return this.fetch(request) != null;
    }

    /**
     * User 객체에 있는 password 를 인코딩하여 User 객체에 다시 넣는다.
     */
    private void bindEncodedPassword(final User user) {
        if (user == null || !StringUtils.hasLength(user.getPassword())) {
            throw new BadRequestException("Password 는 필수값입니다.");
        }
        final String rawPassword = user.getPassword();
        final String encode = this.passwordEncoder.encode(rawPassword);
        user.changePassword(encode);
    }

    /**
     * 이메일, 닉네임 중복체크
     *
     * @param request
     * @throws BadRequestException 이미 사용중인 프로퍼티가 있다면 예외가 발생
     */
    private void checkDuplicatedAttribute(final UserCreateRequest request) {
        if (this.isAlreadyUsedEmail(request.getEmail())) {
            throw new BadRequestException("이미 사용중인 이메일입니다.");
        }
        if (this.isAlreadyUsedNickname(request.getNickname())) {
            throw new BadRequestException("이미 사용중인 닉네임입니다.");
        }
    }

    private boolean isAlreadyUsedEmail(final String email) {
        return this.userRepository.existsByEmail(email);
    }

    private boolean isAlreadyUsedNickname(final String nickname) {
        return this.userRepository.existsByNickname(nickname);
    }

}