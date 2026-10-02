package com.hwang.minilog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserResponseDto { // 응답
  @NonNull private Long id; // PK
  @NonNull private String username; // 사용자 이름
}
