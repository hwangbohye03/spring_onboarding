package com.hwang.minilog.exception;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* 전역 예외 처리 클래스 선언 */
    // 애플리케이션의 모든 컨트롤러에서 발생 예외 감지
    // 예외 발생 시 응답을 클라이언트에게 전달
@ControllerAdvice
public class GlobalExceptionHandler {
    // Spring은 예외 발생 시,가장 구체적으로 매칭되는 예외 핸들러를 최우선으로 선택
        // 1순위 (완전 일치) ➔ 2순위 (부모 타입) ➔ ... (조상 타입)

    // 사용자를 못 찾을 경우
    @ApiResponses(value = { // Swagger 문서용 설정
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400",description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ExceptionHandler(UserNotFoundException.class) // 지정 예외 발생 시, 해당 메소드 실행 설정
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // 게시글을 못 찾을 경우
    @ApiResponses(value ={
            @ApiResponse(responseCode = "404", description = "Article not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ExceptionHandler(ArticleNotFoundException.class) // 지정 예외 발생 시, 해당 메소드 실행 설정
    public ResponseEntity<String> handleArticleNotFoundException(ArticleNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    // 잘못된 인자가 들어왔을 경우
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ExceptionHandler(IllegalArgumentException.class) // 지정 예외 발생 시, 해당 메소드 실행 설정
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    // 위에서 걸러지지 않은 모든 예외
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ExceptionHandler(Exception.class) // 지정 예외 발생 시, 해당 메소드 실행 설정
    public ResponseEntity<String> handleException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
