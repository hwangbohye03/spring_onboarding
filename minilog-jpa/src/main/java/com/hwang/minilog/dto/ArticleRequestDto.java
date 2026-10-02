package com.hwang.minilog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ArticleRequestDto { // 작성&수정 요청
  @NonNull private String content; // 작성 본문
  @NonNull private Long authorId; // 작성자 ID
}
