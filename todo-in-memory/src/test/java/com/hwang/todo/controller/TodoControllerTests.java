package com.hwang.todo.controller;

import com.hwang.todo.controller.TodoController;
import com.hwang.todo.model.Todo;
import com.hwang.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) // 1. JUnit5가 스프링의 테스트 기능(@Autowired, @MockitoBean 등)을 사용할 수 있게 연동
@WebMvcTest(TodoController.class) // 2. 서버 전체를 띄우지 않고, TodoController 관련 웹 계층 빈만 가볍게 테스트 환경으로 로드
public class TodoControllerTests {
    // 3. 실제 톰캣 서버 없이 가상의 HTTP 요청(GET/POST 등)을 보낼 수 있는 가상 전송기 주입
    @Autowired private MockMvc mockMvc;
    // 4. 실제 서비스 및 DB 로직을 대신할 '가짜 서비스(Mock)'를 만들어 스프링에 주입
    @MockBean private TodoService todoService;


    // 5. JUnit에 이 메서드가 실행 가능한 테스트 케이스임을 명시
    @Test
    public void testGetTodoById() throws Exception {
        // Given: 테스트 데이터 생성 및 가짜 행동 사전 준비
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo); // given의 매개변수 호출 시, willReturn의 매개변수 반환하라고 mocking 지정

        // When & Then: HTTP 요청 실행 및 결과 검증 // 실제 실행 값과 의도 값 비교 테스트
        mockMvc.perform(get("/api/todos/v1/1")      // [When] MockMvc로 GET 요청 전송
                        .accept(MediaType.APPLICATION_JSON))  // [When] 응답 형식으로 JSON으로 헤더 설정
                .andExpect(status().isOk())    // [Then] HTTP 응답 상태 코드가 200 OK 인지 검증
                .andExpect(jsonPath("$.id").value(1L)) // [Then] 응답 'id' 필드값 검증
                .andExpect(jsonPath("$.title").value("Test Todo")); // [Then] 응답 'title' 필드값 검증
    }


    @Test
    public void testGetAllTodos() throws Exception{
        // todo 존재하지 않는 경우 테스트
        given(todoService.findAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get("/api/todos/v1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // todo 존재하는 경우 테스트
        given(todoService.findAll())
                .willReturn(
                        Collections.singletonList(
                                new Todo(1L, "Test Todo", "Description", false) ));

        mockMvc.perform(get("/api/todos/v1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Todo"));
    }

    @Test
    public void testCreateTodo() throws Exception{
        // Given: 테스트 데이터 생성 및 가짜 행동 사전 준비
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("New Todo");

        /*
         [Mockito stubbing 시 any(Todo.class)를 사용하는 이유]
           1. given(todoService.save(todo))처럼 특정 객체를 지정하면, Mockito는 메모리 주소(참조)까지 동일한 객체일 때만 동작함
           2. 하지만 mockMvc.perform() 실행 시 컨트롤러 내부에서 JSON을 파싱하여 '새로운 Todo 인스턴스'를 따로 생성함
           3. 테스트 코드의 todo와 컨트롤러 내부의 todo는 서로 참조 주소가 다른 별개 객체이므로 매칭에 실패하여 null이 반환됨
           4. 따라서 특정 인스턴스 대신 any(Todo.class)를 사용하여 "Todo 타입이기만 하면 무조건 이 결과를 반환하라"고 지정해야 함
        */
        given(todoService.save(any(Todo.class))).willReturn(todo);

        // When & Then: POST 요청을 전송하고(When), 응답 상태코드 및 JSON 데이터 검증(Then)
        mockMvc.perform(post("/api/todos/v1") // URL로 HTTP POST 요청 전송
                .contentType(MediaType.APPLICATION_JSON) // 요청 본문(Body) 데이터 타입이 JSON임을 명시
                .content("{\"title\": \"New Todo\"}")) // 요청 본문 (생성할 데이터 정보)
                .andExpect(status().isCreated()) // 상태 코드 검증
                .andExpect(jsonPath("$.id").value(1L)) // id 필드값 검증
                .andExpect(jsonPath("$.title").value("New Todo")); // title 필드값 검증
    }

    @Test
    public void testUpdateTodo() throws Exception{
        Todo existingTodo = new Todo();
        existingTodo.setId(1L);
        existingTodo.setTitle("Existing Todo");

        Todo updatedTodo = new Todo();
        updatedTodo.setId(1L);
        updatedTodo.setTitle("Updated Todo");

        given(todoService.findById(1L)).willReturn(existingTodo);
        given(todoService.update( anyLong(), any(Todo.class) )).willReturn(updatedTodo);

        mockMvc.perform(put("/api/todos/v1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Todo\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Updated Todo"));
    }

    @Test void testDeleteTodo() throws Exception{
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);
        //given(...).willReturn(...) 문법은 반환값이 있는 메서드의 결과값을 지정할 때 사용
        //따라서, delete()처럼 리턴값이 없는 void 메서드는
        // Mockito가 기본적으로 "Do Nothing"하도록 알아서 모킹해 둠

        mockMvc.perform(delete("/api/todos/v1/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }





}
