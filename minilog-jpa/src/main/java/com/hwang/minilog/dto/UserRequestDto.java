package com.hwang.minilog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UserRequestDto { // 등록&수정 요청
  @NonNull private String username; // 사용자 이름
  @NonNull private String password; // 사용자 비밀번호
}
