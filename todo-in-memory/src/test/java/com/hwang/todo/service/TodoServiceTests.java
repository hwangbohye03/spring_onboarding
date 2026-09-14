package com.hwang.todo.service;

import com.hwang.todo.model.Todo;
import com.hwang.todo.repository.TodoInMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TodoServiceTests {
    @Autowired private TodoService todoService;

    @BeforeEach
    void setUp(){
        todoService = new TodoService(new TodoInMemoryRepository());
        todoService.save(new Todo(null, "Test Todo 1", "Description 1", false));
        todoService.save(new Todo(null, "Test Todo 2", "Description 2", false));
    }

    @Test
    void testFindAll(){
        // 1. 서비스 실행
        List<Todo> todos = todoService.findAll();
        // 2. 결과 검증
        assertThat(todos).hasSize(2);
    }


    @Test
    void testSaveTodo(){
        // 1. 서비스 실행
        Todo todo = new Todo(null, "New Todo", "New Description", false);
        todoService.save(todo);
        // 2. 결과 검증
        assertThat(todoService.findAll()).hasSize(3);
    }

    @Test
    void testFindById(){
        // 1. 서비스 실행
        Todo todo = todoService.findById(1L);
        // 2. 결과 검증
        assertThat(todo).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    @Test
    void testUpdateTodo(){
        // 1. 서비스 실행
        Todo updatedTodo = new Todo(1L, "Updated Todo", "Updated Description", true);
        todoService.update(1L, updatedTodo);
        // 2. 결과 검증
        Todo todo = todoService.findById(1L);
        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        assertThat(todo.isCompleted()).isEqualTo(true);
    }

    @Test
    void testDeleteTodo(){
        // 1. 서비스 실행
        todoService.delete(1L);
        // 2. 결과 검증
        assertThat(todoService.findAll()).hasSize(1);
        assertThat(todoService.findById(1L)).isNull();
    }

}
