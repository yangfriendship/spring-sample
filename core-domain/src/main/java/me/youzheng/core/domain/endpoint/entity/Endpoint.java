package me.youzheng.core.domain.endpoint.entity;

import lombok.*;
import me.youzheng.core.domain.common.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.HttpMethod;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@EqualsAndHashCode(of = {"httpMethod", "path", "applicationName"}, callSuper = false)
public class Endpoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long endPointInfoId;

    private HttpMethod httpMethod;

    private String path;

    private String applicationName;

}