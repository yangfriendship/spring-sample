package me.youzheng.apiserver.sessionlog.mapper;

import me.youzheng.apiserver.sessionlog.payload.SessionLogCreateRequest;
import me.youzheng.core.domain.sessionlog.dto.SessionLogDto;
import me.youzheng.core.domain.sessionlog.entity.SessionLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SessionLogMapper {

    SessionLogMapper INSTANCE = Mappers.getMapper(SessionLogMapper.class);


    SessionLog from(final SessionLogCreateRequest sessionLogCreateRequest);

    SessionLogDto toDto(final SessionLog sessionLog);

}