package com.hwang.minilog.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ArticleResponseDto { // 응답
  @NonNull private Long articleId; // PK
  @NonNull private String content; // 게시글 본문
  @NonNull private Long authorId; // 작성자 아이디
  @NonNull private String authorName; // 작성자 이름
  @NonNull private LocalDateTime createdAt; // 생성 시간
}
