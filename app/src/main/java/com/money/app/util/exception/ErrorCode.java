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
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),

    //User 관련
    USER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 ID 입니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    USER_CELLPHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 전화번호 입니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디가 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "이메일 형식이 올바르지 않습니다."),
    INVALID_BIRTH_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "생년월일이 올바르지 않습니다."),
    INVALID_GENDER_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "성별 형식이 올바르지 않습니다."),
    INVALID_CELLPHONE_FORMAT(HttpStatus.UNPROCESSABLE_ENTITY, "전화번호 형식이 올바르지 않습니다."),
    //Tx 관련
    TX_NOT_FOUNT(HttpStatus.NOT_FOUND, "TX를 찾을 수 없습니다"),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "Report를 찾을 수 없습니다"),
    TX_UPDATED_CANCEL(HttpStatus.CONFLICT, "TX 업데이트시 같은 Type으로만 가능합니다"),
    //Category 관련
    CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 카테고리입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

    //전체
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "올바른 요청형식이 아닙니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
