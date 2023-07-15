package me.youzheng.core.web;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ApiError {

    /**
     * CommonCode 의 Exception Code
     */
    private final String code;

    /**
     * 에러에 대한 메시지
     */
    private final String message;

}