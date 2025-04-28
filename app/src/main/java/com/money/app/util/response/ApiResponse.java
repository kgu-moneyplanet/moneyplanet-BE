package com.money.app.util.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int statusCode; //HTTP 상태 코드
    private String message; //상태 메세지
    private T data; //응답 데이터
}
