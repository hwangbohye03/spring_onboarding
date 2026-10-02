package com.hwang.minilog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwang.minilog.dto.UserRequestDto;
import com.hwang.minilog.dto.UserResponseDto;
import com.hwang.minilog.exception.UserNotFoundException;
import com.hwang.minilog.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class) // JUnit5와 스프링 테스트 컨텍스트 통합
@WebMvcTest(UserController.class) // 웹 레이어만 테스트 -> Service, Repository 등 다른 빈은 로드X
@MockBean(
    JpaMetamodelMappingContext
        .class) // @EnableJpaAuditing(감사 기능) 처리에 내부적으로 필요한 JpaMetamodelMappingContext 빈 Mock 빈으로 등록
public class UserControllerTest {
  // 테스트용 HTTP 요청&응답 처리 및 검증할 도구
  @Autowired private MockMvc mockMvc;

  // 컨트롤러의 의존 서비스 빈 Mock 객체로 주입 (서비스 로직 실행 대신 동작 정의해 테스트 활용)
  @MockBean private UserService userService;

  // 자바 객체 <-> JSON 문자열 변환기 (사용자 Request Body 셋팅용)
  private ObjectMapper objectMapper = new ObjectMapper();

  // 각 @Test 메서드가 실행되기 직전에 매번 실행
  @BeforeEach
  public void setup() {
    // 선언된 @Mock 같은 Mockito 어노테이션 필드 초기화 작업
    // (@MockBean 사용 시 스프링이 자동 초기화하므로 생략 가능)
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetUser() throws Exception {
    List<UserResponseDto> responseDtoList =
        List.of(
            UserResponseDto.builder().id(1L).username("Test User").build(),
            UserResponseDto.builder().id(2L).username("Test User 2").build());

    when(userService.getUsers()).thenReturn(responseDtoList);

    mockMvc
        .perform(get("/api/v1/user"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].username").value("Test User"))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].username").value("Test User 2"));
  }

  @Test
  public void testGetUserById() throws Exception {
    UserResponseDto responseDto = UserResponseDto.builder().id(1L).username("Test User").build();

    when(userService.getUserById(anyLong())).thenReturn(responseDto);

    mockMvc
        .perform(get("/api/v1/user/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("Test User"));
  }

  @Test
  public void testCreateUser() throws Exception {
    UserRequestDto requestDto =
        UserRequestDto.builder().username("Test User").password("password").build();
    UserResponseDto responseDto = UserResponseDto.builder().id(1L).username("Test User").build();

    when(userService.createUser(any(UserRequestDto.class))).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("Test User"));
  }

  @Test
  public void testUpdateUser() throws Exception {
    UserRequestDto requestDto =
        UserRequestDto.builder().username("Test User").password("password").build();
    UserResponseDto responseDto = UserResponseDto.builder().id(1L).username("Test User").build();

    when(userService.updateUser(anyLong(), any(UserRequestDto.class))).thenReturn(responseDto);

    mockMvc
        .perform(
            put("/api/v1/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("Test User"));
  }

  @Test
  public void testDeleteUser() throws Exception {
    mockMvc.perform(delete("/api/v1/user/1")).andExpect(status().isNoContent());
  }

  // @ControllerAdvice나 @RestControllerAdvice로 구현된 전역 예외 처리기는 웹 레이어(Spring MVC) 영역
  // 서비스 계층에서 예외가 발생(throw)했을 때, Spring MVC가 이를 낚아채서 HTTP 응답 생성
  @Test
  public void testGlobalExceptionHandler() throws Exception {
    // UserNotFoundException 테스트
    when(userService.getUserById(anyLong()))
        .thenThrow(new UserNotFoundException("Test UserNotFound Exception"));

    mockMvc
        .perform(get("/api/v1/user/999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Test UserNotFound Exception"));
  }
}
