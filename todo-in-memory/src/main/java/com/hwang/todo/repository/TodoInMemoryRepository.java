package com.hwang.todo.repository;

import com.hwang.todo.model.Todo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class TodoInMemoryRepository {
    // DB를 사용하지 않고 메모리에 Map컨테이너 활용해 Todo를 저장하는 방식
    private final Map<Long, Todo> todoMap = new HashMap<>();

    // Todo의 ID 자동 생성 (새로운 Todo가 저장될 때마다 순서대로 ID를 만듦)
    private final AtomicLong counter = new AtomicLong();

    // 모든 todo 조회
    public List<Todo> findAll() {
        return new ArrayList<>(todoMap.values());
    }

    // id를 통한 단일 todo 조회
    public Todo findById(Long id) {
        return todoMap.get(id);
    }

    // todo 저장
    public Todo save(Todo todo) {
        // 아직 ID가 없는 새로운 Todo (생성 저장)
        if (todo.getId() == null) {
            todo.setId(counter.incrementAndGet());
        }
        // else // 기존 존재하던 ID의 Todo (수정 저장)

        todoMap.put(todo.getId(), todo);
        return todo;
    }

    // id를 통한 todo 삭제
    public void deleteById(Long id) {
        todoMap.remove(id);
    }
}
