package com.money.app.util.exception;

import com.money.app.util.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  //글로벌 예외처리기
public class Handler {

    @ExceptionHandler(CustomException.class)  //CustomException 발생시 실행됨
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();  //errorcode를 꺼내서 apiresponse에 맞춰서 출력
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ApiResponse<>(errorCode.getStatus().value(), errorCode.getMessage(), null));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleJsonParseError(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 JSON 형식입니다", null));
    }
}