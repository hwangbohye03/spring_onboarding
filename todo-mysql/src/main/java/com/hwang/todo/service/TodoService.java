package com.hwang.todo.service;

import com.hwang.todo.dto.TodoRequestDto;
import com.hwang.todo.dto.TodoResponseDto;
import com.hwang.todo.entity.Todo;
import com.hwang.todo.repository.TodoRepository;
import java.util.List;
import java.util.stream.Collectors;

import com.hwang.todo.util.EntityDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    @Autowired // 의존성 주입 (생성자 주입)
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Transactional(readOnly = true) // 읽기 전용 트랜젝션
    public List<TodoResponseDto> findAll() {
        return todoRepository.findAll().stream(). // DB상 Todo엔티티 리스트 가져와 스트림 처리
                map(EntityDtoMapper::toDto).      // 각 엔티티를 DTO로 변환 // (class::method)
                collect(Collectors.toList());     // 변환된 DTO들 List 형식으로 묶어 반환
    }

    @Transactional(readOnly = true)
    public TodoResponseDto findById(Long id) {
        // 비권장 방식: 데이터가 없어 null인 경우에도 DTO 변환 메서드를 호출해, NPE 발생 위험
        // return EntityDtoMapper.toDto(todoRepository.findById(id).orElse(null));

        // 권장 방식: 데이터가 있을 때만 DTO 변환을 수행하고, 없으면 변환을 건너뛰어 NPE 예방
        return todoRepository.findById(id).map(EntityDtoMapper::toDto).orElse(null);
    }

    @Transactional
    public TodoResponseDto save(TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        Todo savedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(savedTodo);
    }

    @Transactional
    public TodoResponseDto update(Long id, TodoRequestDto todoRequestDto) {
        Todo todo = EntityDtoMapper.toEntity(todoRequestDto);
        todo.setId(id);
        Todo updatedTodo = todoRepository.save(todo);
        return EntityDtoMapper.toDto(updatedTodo);
    }

    @Transactional
    public void delete(Long id) {
        todoRepository.deleteById(id);
    }
}
