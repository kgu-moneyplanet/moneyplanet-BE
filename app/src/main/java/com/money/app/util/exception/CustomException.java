package com.money.app.util.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {  //런타임 시전 중 발생하는 에러처리
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}