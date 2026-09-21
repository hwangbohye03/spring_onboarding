package com.hwang.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {
    @NonNull private Long id; // 아이디 (pk)
    private String title; // 제목
    private String description; // 설명
    private boolean completed; // 완료 여부
}
