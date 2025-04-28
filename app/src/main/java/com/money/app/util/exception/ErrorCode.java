package com.money.app.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus; //Http 상태코드 저장된 Enum 코드

@Getter
public enum ErrorCode {
    //User
    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 ID 입니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    USER_CELLPHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 전화번호 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status=status;
        this.message=message;
    }
}
