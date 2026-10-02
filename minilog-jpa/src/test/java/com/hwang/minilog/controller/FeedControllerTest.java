package com.hwang.minilog.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.service.ArticleService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
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
@WebMvcTest(FeedController.class) // 웹 레이어만 테스트 -> Service, Repository 등 다른 빈은 로드X
@MockBean(
    JpaMetamodelMappingContext
        .class) // @EnableJpaAuditing(감사 기능) 처리에 내부적으로 필요한 JpaMetamodelMappingContext 빈 Mock 빈으로 등록
public class FeedControllerTest {

  // 테스트용 HTTP 요청&응답 처리 및 검증할 도구
  @Autowired private MockMvc mockMvc;

  // 컨트롤러의 의존 서비스 빈 Mock 객체로 주입 (서비스 로직 실행 대신 동작 정의해 테스트 활용)
  @MockBean private ArticleService articleService;

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
  public void testGetFeedList() throws Exception {
    ArticleResponseDto responseDto =
        ArticleResponseDto.builder()
            .articleId(1L)
            .content("Test Content")
            .authorId(1L)
            .authorName("Test User")
            .createdAt(fixtureDateTime)
            .build();

    when(articleService.getFeedListByFollowerId(anyLong()))
        .thenReturn(Collections.singletonList(responseDto));

    mockMvc
        .perform(
            get("/api/v1/feed?followerId=1")) // get("/api/v1/feed").param("followerId", "1")도 가능
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].articleId").value(1L))
        .andExpect(jsonPath("$[0].content").value("Test Content"))
        .andExpect(jsonPath("$[0].authorId").value(1L))
        .andExpect(jsonPath("$[0].authorName").value("Test User"))
        .andExpect(jsonPath("$[0].createdAt").value(formattedFixtureDateTime));
  }
}
