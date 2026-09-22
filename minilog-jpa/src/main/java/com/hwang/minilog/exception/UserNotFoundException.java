package com.hwang.minilog.exception;

/* 사용자 정의 예외 클래스 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
