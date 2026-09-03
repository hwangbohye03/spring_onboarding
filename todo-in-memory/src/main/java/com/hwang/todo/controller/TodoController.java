package com.hwang.todo.controller;

import com.hwang.todo.model.Todo;
import com.hwang.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // REST 컨틀롤러 빈 선언
@RequestMapping("/api/todos/v1") // HTTP 메서드의 공통 URL 지정
public class TodoController {

    @Autowired // 의존성 주입 (필드 주입)
    private TodoService todoService;

    // 모든 Todo 항목 조회 API
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "전체 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "204", description = "내용 없음")
    })
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.findAll();
        if (todos == null || todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(todos);
    }

    // 특정 ID의 Todo 항목 조회 API
    @GetMapping("/{id}")
    @Operation(summary = "작업 조회", description = "ID로 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음"),
    })
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todo);
    }

    // Todo 항목 생성
    @PostMapping
    @Operation(summary = "작업 생성", description = "새로운 작업 생성")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "생성됨")})
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(201).body(todoService.save(todo));
    }

    // 기존 Todo 항목 수정
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "ID로 작업 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todoService.findById(id);

        if (existingTodo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todoService.update(id, todo));
    }

    // 특정 ID의 Todo 항목 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "ID로 작업 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "내용 없음"),
        @ApiResponse(responseCode = "404", description = "작업 없음"),
    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Todo todo = todoService.findById(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
