package me.youzheng.core.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

@JsonIncludeProperties(value = {"user"})
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
public abstract class UserContextMixin {

}
