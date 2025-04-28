package com.money.app.util.exception;

import com.money.app.util.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {
    @ExceptionHandler(CustomException.class) //CustomException 발생시 실행됨
    public ResponseEntity<ApiResponse<Void>> handlerCustomException(CustomException ex) {
        ErrorCode errorCode=ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ApiResponse<>(errorCode.getStatus().value(),errorCode.getMessage(),null));
    }
}
