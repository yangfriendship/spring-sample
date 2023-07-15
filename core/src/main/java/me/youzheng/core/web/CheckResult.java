package me.youzheng.core.web;

import lombok.*;

/**
 * 이메일, 닉네임 등 중복체크에 대한 결과값을 랩핑할 때 사용
 */
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class CheckResult {

    private String property;

    private boolean used;

}