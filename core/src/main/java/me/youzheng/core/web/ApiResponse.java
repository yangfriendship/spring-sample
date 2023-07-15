package me.youzheng.core.web;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ApiResponse<T> {

    private final T body;

    private final ApiError error;

    private final boolean success;

    @Builder
    public static <T> ApiResponse<T> of(final boolean success, final T body, final ApiError apiError) {
        return ApiResponse.<T>builder()
                .body(body)
                .success(success)
                .error(apiError)
                .build();
    }

    public static <T> ApiResponse<T> success(final T body) {
        return of(true, body, null);
    }

    public static <T> ApiResponse<T> fail(final ApiError apiError) {
        return of(false, null, apiError);
    }

    public static <T> ApiResponse<T> fail(final Exception exception) {
        return of(false, null, ApiError.builder()
                .message(exception.getMessage())
                .build());
    }

    public static ApiResponse<CheckResult> checkResult(final String property, final boolean isUsed) {
        return success(CheckResult.builder()
                .property(property)
                .used(isUsed)
                .build());
    }

}