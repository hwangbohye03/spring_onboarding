package controller;

import com.hwang.todo.controller.TodoController;
import com.hwang.todo.model.Todo;
import com.hwang.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class) // 1. JUnit5가 스프링의 테스트 기능(@Autowired, @MockitoBean 등)을 사용할 수 있게 연동
@WebMvcTest(TodoController.class) // 2. 서버 전체를 띄우지 않고, TodoController 관련 웹 계층 빈만 가볍게 테스트 환경으로 로드
public class TodoControllerTests {
    // 3. 실제 톰캣 서버 없이 가상의 HTTP 요청(GET/POST 등)을 보낼 수 있는 가상 전송기 주입
    @Autowired private MockMvc mockMvc;
    // 4. 실제 서비스 및 DB 로직을 대신할 '가짜 서비스(Mock)'를 만들어 스프링에 주입
    @MockitoBean private TodoService todoService;

    // 5. JUnit에 이 메서드가 실행 가능한 테스트 케이스임을 명시
    @Test
    public void testGetTodoById() throws Exception {
        // Given: 테스트 데이터 및 가짜 행동 사전 준비
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");

        given(todoService.findById(1L)).willReturn(todo);

        // When & Then: HTTP 요청 실행 및 결과 검증
        mockMvc.perform(get("/api/todos/v1/1")      // [When] MockMvc로 GET 요청 전송
                        .accept(MediaType.APPLICATION_JSON))  // [When] 응답 형식으로 JSON으로 헤더 설정
                .andExpect(status().isOk())    // [Then] HTTP 응답 상태 코드가 200 OK 인지 검증
                .andExpect(jsonPath("$.id").value(1L)) // [Then] 응답 'id' 필드값 검증
                .andExpect(jsonPath("$.title").value("Test Todo")); // [Then] 응답 'title' 필드값 검증
    }





}
