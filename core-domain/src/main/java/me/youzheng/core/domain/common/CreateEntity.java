package me.youzheng.core.domain.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class CreateEntity {

    @CreatedBy
    private Long creatorId;

    @CreatedDate
    private LocalDateTime createdDateTime;

}
