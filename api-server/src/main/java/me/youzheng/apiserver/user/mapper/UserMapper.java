package me.youzheng.apiserver.user.mapper;

import me.youzheng.apiserver.user.payload.UserCreateRequest;
import me.youzheng.apiserver.user.payload.UserDetailResponse;
import me.youzheng.apiserver.user.payload.UserFetchRequest;
import me.youzheng.core.domain.user.dto.UserCriteria;
import me.youzheng.core.domain.user.dto.UserDto;
import me.youzheng.core.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User from(final UserCreateRequest request);

    UserDto toUserDto(final User user);

    UserDetailResponse toDetailResponse(UserDto userDto);


    UserCriteria criteriaWith(UserFetchRequest request);

}