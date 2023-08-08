package me.youzheng.core.domain.sessionlog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.youzheng.core.domain.common.CreateEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class SessionLog extends CreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionLogId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    private SessionLogType type;

}