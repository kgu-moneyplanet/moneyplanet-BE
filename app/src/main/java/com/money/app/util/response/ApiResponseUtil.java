package com.money.app.util.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; //status code, header, body를 조정하는 용도

public class ApiResponseUtil {
    // 성공 (상태코드 + 메시지)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(status.value(), message, null)
        );
    }

    // 성공 (상태코드 + 메시지 + 데이터)
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(status.value(), message, data)
        );
    }

    // 성공 (데이터만, 200 OK, 기본 메시지)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "요청 성공", data)
        );
    }

    // 실패 응답
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(
                new ApiResponse<>(status.value(), message, null)
        );
    }
}