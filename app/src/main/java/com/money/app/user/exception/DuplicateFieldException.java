package com.money.app.user.exception;

public class DuplicateFieldException extends RuntimeException{
    public DuplicateFieldException(String field, String message) {
        super(field+" 중복 오류: "+message);
    }
}
