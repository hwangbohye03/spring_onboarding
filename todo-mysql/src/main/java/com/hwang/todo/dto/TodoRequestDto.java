package com.hwang.todo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TodoRequestDto { // 요청 경우 -> 조회, 생성, 수정, 삭제
    // 필드
    @NonNull private String title; // 제목
    @NonNull private String description; // 설명
    private boolean completed = false; // 수행여부

    // 생성자
    public TodoRequestDto(String title, String description, boolean completed){
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // 생성자
    public TodoRequestDto(String title, String description){
        this(title, description, false);
    }
}
