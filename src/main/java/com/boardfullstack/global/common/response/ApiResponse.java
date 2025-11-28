package com.boardfullstack.global.common.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final ErrorResponse error;
    private final LocalDateTime timeStamp;

    // 외부에서 임의로 생성하는 것을 막기 위해
    private ApiResponse(boolean success, T data, ErrorResponse error, LocalDateTime timeStamp) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timeStamp = timeStamp;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(ErrorResponse error) {
        return new ApiResponse<>(false, null, error, LocalDateTime.now());
    }
}
