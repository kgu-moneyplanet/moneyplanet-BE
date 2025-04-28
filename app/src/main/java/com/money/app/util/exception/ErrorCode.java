package com.money.app.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus; //Http 상태코드 저장된 Enum코드

@Getter
public enum ErrorCode {
    //User 관련

    //Tx 관련

    //Category 관련
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
