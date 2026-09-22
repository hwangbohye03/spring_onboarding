package com.hwang.minilog.exception;

/* 사용자 정의 예외 클래스 */
public class ArticleNotFoundException extends RuntimeException{
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
