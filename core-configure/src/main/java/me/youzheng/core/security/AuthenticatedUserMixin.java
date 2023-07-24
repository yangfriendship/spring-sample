package me.youzheng.core.security;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@JsonIncludeProperties(value = {"userContext", "authenticated"})
public abstract class AuthenticatedUserMixin {

}
