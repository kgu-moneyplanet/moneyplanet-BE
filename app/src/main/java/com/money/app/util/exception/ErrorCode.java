package com.money.app.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus; //Http 상태코드 저장된 Enum코드

@Getter
public enum ErrorCode {
    //Login Logout 관련
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    PASSWORD_NOT_CORRECT(HttpStatus.CONFLICT,"비밀번호가 틀렸습니다"),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 없습니다"),
    TOKEN_TIME_OUT(HttpStatus.UNAUTHORIZED, "토큰 시간 만료"),

    //User 관련
    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 ID 입니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    USER_CELLPHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 전화번호 입니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "이메일이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."),
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
