package me.youzheng.core.security;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIncludeProperties(value = {"userId", "username", "nickname", "email", "roles"})
public abstract class UserMixin {

    @JsonProperty(value = "userId")
    Long id;

}