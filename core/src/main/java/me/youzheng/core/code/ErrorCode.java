package me.youzheng.core.code;

import lombok.Getter;

/**
 * 공통코드 테이블에 등록된 ErrorCode 목록, ROOT Code = ERROR0000
 */
@Getter
public enum ErrorCode {

    FAIL("ERROR0001", "실패하였습니다.")
    , UNAUTHENTICATED("ERROR0002", "인증이 필요합니다.")
    , FORBIDDEN("ERROR0003", "접근 권한이없습니다.")
    ;

    private final String code;

    private final String message;

    ErrorCode(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

}