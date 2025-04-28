package com.money.app.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 공통 API 응답 DTO
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode;  // HTTP 상태 코드
    private String message;  // 상태 메시지
    private T data;          // 응답 데이터 (없을 수도 있음)
}
