package com.hwang.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private Long id;

    @NonNull private String title; // 제목

    private String description; // 설명

    private boolean completed; // 수행 여부
}
