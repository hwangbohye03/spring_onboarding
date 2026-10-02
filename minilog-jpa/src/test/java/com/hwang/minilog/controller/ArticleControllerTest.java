package com.hwang.minilog.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwang.minilog.dto.ArticleRequestDto;
import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.exception.ArticleNotFoundException;
import com.hwang.minilog.service.ArticleService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
@WebMvcTest(ArticleController.class) // 웹 레이어만 테스트 -> Service, Repository 등 다른 빈은 로드X
@MockBean(
    JpaMetamodelMappingContext
        .class) // @EnableJpaAuditing(감사 기능) 처리에 내부적으로 필요한 JpaMetamodelMappingContext 빈 Mock 빈으로 등록
public class ArticleControllerTest {

  // 테스트용 HTTP 요청&응답 처리 및 검증할 도구
  @Autowired private MockMvc mockMvc;

  // 컨트롤러의 의존 서비스 빈 Mock 객체로 주입 (서비스 로직 실행 대신 동작 정의해 테스트 활용)
  @MockBean private ArticleService articleService;

  // 자바 객체 <-> JSON 문자열 변환기 (사용자 Request Body 셋팅용)
  private ObjectMapper objectMapper = new ObjectMapper();

  // 테스트에 공통으로 사용할 가짜 시간 데이터 설정
  LocalDateTime fixtureDateTime = LocalDateTime.of(2026, 9, 30, 0, 0, 0);
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
  String formattedFixtureDateTime = fixtureDateTime.format(formatter);

  // 각 @Test 메서드가 실행되기 직전에 매번 실행
  @BeforeEach
  public void setup() {
    // 선언된 @Mock 같은 Mockito 어노테이션 필드 초기화 작업
    // (@MockBean 사용 시 스프링이 자동 초기화하므로 생략 가능)
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateArticle() throws Exception {
    ArticleRequestDto requestDto =
        ArticleRequestDto.builder().authorId(1L).content("Test Content").build();

    ArticleResponseDto responseDto =
        ArticleResponseDto.builder()
            .articleId(1L)
            .content("Test Content")
            .authorId(1L)
            .authorName("Test User")
            .createdAt(fixtureDateTime)
            .build();

    when(articleService.createArticle(any(String.class), anyLong())).thenReturn(responseDto);

    mockMvc
        .perform(
            post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.articleId").value(1L))
        .andExpect(jsonPath("$.content").value("Test Content"))
        .andExpect(jsonPath("$.authorId").value(1L))
        .andExpect(jsonPath("$.authorName").value("Test User"))
        .andExpect(jsonPath("$.createdAt").value(formattedFixtureDateTime));
  }

  @Test
  public void testGetArticle() throws Exception {
    ArticleResponseDto responseDto =
        ArticleResponseDto.builder()
            .articleId(1L)
            .content("Test Content")
            .authorId(1L)
            .authorName("Test User")
            .createdAt(fixtureDateTime)
            .build();

    when(articleService.getArticleById(anyLong())).thenReturn(responseDto);

    mockMvc
        .perform(get("/api/v1/article/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.articleId").value(1L))
        .andExpect(jsonPath("$.content").value("Test Content"))
        .andExpect(jsonPath("$.authorId").value(1L))
        .andExpect(jsonPath("$.authorName").value("Test User"))
        .andExpect(jsonPath("$.createdAt").value(formattedFixtureDateTime));
  }

  @Test
  public void testUpdateArticle() throws Exception {
    ArticleRequestDto requestDto =
        ArticleRequestDto.builder().authorId(1L).content("Test Content").build();

    ArticleResponseDto responseDto =
        ArticleResponseDto.builder()
            .articleId(1L)
            .content("Test Content")
            .authorId(1L)
            .authorName("Test User")
            .createdAt(fixtureDateTime)
            .build();

    when(articleService.updateArticle(anyLong(), any(String.class))).thenReturn(responseDto);

    mockMvc
        .perform(
            put("/api/v1/article/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.articleId").value(1L))
        .andExpect(jsonPath("$.content").value("Test Content"))
        .andExpect(jsonPath("$.authorId").value(1L))
        .andExpect(jsonPath("$.authorName").value("Test User"))
        .andExpect(jsonPath("$.createdAt").value(formattedFixtureDateTime));
  }

  @Test
  public void testDeleteArticle() throws Exception {
    mockMvc.perform(delete("/api/v1/article/1")).andExpect(status().isNoContent());
  }

  @Test
  public void testGetArticleByUserId() throws Exception {
    ArticleResponseDto responseDto =
        ArticleResponseDto.builder()
            .articleId(1L)
            .content("Test Content")
            .authorId(1L)
            .authorName("Test User")
            .createdAt(fixtureDateTime)
            .build();
    List<ArticleResponseDto> responseList = Collections.singletonList(responseDto);
    // singletonList() : 딱 1개의 요소만 리스트로 저장하도록 최적화된 메소드

    when(articleService.getArticleListByUserId(anyLong())).thenReturn(responseList);

    mockMvc
        .perform(
            get("/api/v1/article").param("authorId", "1")) // get("/api/v1/article?authorId=1")도 가능
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].articleId").value(1L))
        .andExpect(jsonPath("$[0].content").value("Test Content"))
        .andExpect(jsonPath("$[0].authorId").value(1L))
        .andExpect(jsonPath("$[0].authorName").value("Test User"))
        .andExpect(jsonPath("$[0].createdAt").value(formattedFixtureDateTime));
  }

  // @ControllerAdvice나 @RestControllerAdvice로 구현된 전역 예외 처리기는 웹 레이어(Spring MVC) 영역
  // 서비스 계층에서 예외가 발생(throw)했을 때, Spring MVC가 이를 낚아채서 HTTP 응답 생성
  @Test
  public void testGlobalExceptionHandler() throws Exception {
    // ArticleNotFoundException 테스트
    when(articleService.getArticleById(anyLong()))
        .thenThrow(new ArticleNotFoundException("Test ArticleNotFound Exception"));

    mockMvc
        .perform(get("/api/v1/article/999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Test ArticleNotFound Exception"));
  }
}
