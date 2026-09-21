package com.hwang.todo.service;

import com.hwang.todo.dto.TodoRequestDto;
import com.hwang.todo.dto.TodoResponseDto;
import com.hwang.todo.entity.Todo;
import com.hwang.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@Testcontainers // 클래스 단 Testcontainers사용 활성화, @Container 필드 인식하고 각 테스트에 대해 컨테이너 생명주기 관리
@ExtendWith(SpringExtension.class) // JUnit5가 스프링의 테스트 (컨텍스트) 기능(@Autowired,@Transactional 등) 사용할 수 있게 연동
@SpringBootTest
public class TodoServiceTests {
    // 컨테이너 설정 추가하기
        // 테스트 환경에서 사용할 컨테이너 선언
        // 컨테이너는 테스트 실행 전 자동 시작되며, 테스트 끝나면 자동 종료됨
    @Container
    public static MySQLContainer<?> mySQLContainer =
            new MySQLContainer<>("mysql:8.0.32")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    // 동적 속성 부여 설정 추가하기
        // 테스트 실행에 필요한 속성을 동적 주입
        // ex) mySQLContainer 실행 시, 컨테이너에서 생성된 DB의 (url,사용자 이름,pw)등
        //     DB 연결에 필요 정보를 스프링 애플리케이션 컨텍스트에 자동 주입함
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Autowired private TodoService todoService;
    @Autowired private TodoRepository todoRepository;

    private Long todo1Id;
    private Long todo2Id;

    @BeforeEach
    void setUp(){
        todoRepository.deleteAll();

        todoService = new TodoService(todoRepository);

        todo1Id = todoService.save(new TodoRequestDto("Test Todo 1", "Description 1")).getId();
        todo2Id = todoService.save(new TodoRequestDto("Test Todo 2", "Description 2")).getId();
    }

    @Test
    void testFindAll(){
        // 1. 서비스 실행
        List<TodoResponseDto> todos = todoService.findAll();
        // 2. 결과 검증
        assertThat(todos).hasSize(2);
    }


    @Test
    void testSaveTodo(){
        // 1. 서비스 실행
        TodoRequestDto todo = new TodoRequestDto("New Todo", "New Description");
        todoService.save(todo);
        // 2. 결과 검증
        assertThat(todoService.findAll()).hasSize(3);
    }

    @Test
    void testFindById(){
        // 1. 서비스 실행
        TodoResponseDto todo = todoService.findById(todo1Id);
        // 2. 결과 검증
        assertThat(todo).isNotNull();
        assertThat(todo.getTitle()).isEqualTo("Test Todo 1");
    }

    @Test
    void testUpdateTodo(){
        // 1. 서비스 실행
        TodoRequestDto updatedTodo = new TodoRequestDto("Updated Todo", "Updated Description", true);
        todoService.update(todo1Id, updatedTodo);
        // 2. 결과 검증
        TodoResponseDto todo = todoService.findById(todo1Id);
        assertThat(todo.getTitle()).isEqualTo("Updated Todo");
        assertThat(todo.getDescription()).isEqualTo("Updated Description");
        assertThat(todo.isCompleted()).isEqualTo(true);
    }

    @Test
    void testDeleteTodo(){
        // 1. 서비스 실행
        todoService.delete(todo1Id);
        // 2. 결과 검증
        assertThat(todoService.findAll()).hasSize(1);
        assertThat(todoService.findById(todo1Id)).isNull();
    }

}
