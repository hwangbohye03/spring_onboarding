package com.hwang.minilog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/* http://localhost:8080/swagger-ui/index.html */

@Configuration // Spring의 설정 클래스임을 명시 (애플리케이션 실행 시 스프링 컨테이너가 로딩)
public class ApiDocumentationConfig {
  // 다른 코드에 DI 하려고 Bean 등록하는 게 아니라,
  // SpringDoc 라이브러리가 설정을 낚아채서 쓰도록 통로를 열어주는 용도
  @Bean // 메서드 반환 객체(OpenAPI)를 스프링 빈(Bean)으로 등록
  public OpenAPI customOpenAPI() {
    // Swagger UI 페이지 상단에 노출될 메타데이터(제목, 버전, 설명) 설정 객체 반환
    return new OpenAPI()
        .info(
            new Info()
                .title("minilog API")
                .version("1.0")
                .description("API documentation for the Minilog project"));
  }
}
