package me.youzheng.core.domain.userrole.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class UserRolePk implements Serializable {

    private Long userId;

    private Long roleId;

}