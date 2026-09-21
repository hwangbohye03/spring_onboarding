package com.hwang.todo.util;

import com.hwang.todo.dto.TodoRequestDto;
import com.hwang.todo.dto.TodoResponseDto;
import com.hwang.todo.entity.Todo;

/*  RequestDto →  Entity →  ResponseDto  */
    // RequestDto →  Entity
    // Entity →  ResponseDto
public class EntityDtoMapper {
    public static Todo toEntity(TodoRequestDto dto){
        return new Todo(null,      // 신규 생성 시 DB 기준 생성 || 수정 시 setId로 할당
                        dto.getTitle(),
                        dto.getDescription(),
                        dto.isCompleted(),
                        null); // DB 기준
    }

    public static TodoResponseDto toDto(Todo entity){
        return new TodoResponseDto(entity.getId(),
                        entity.getTitle(),
                        entity.getDescription(),
                        entity.isCompleted());
    }
}