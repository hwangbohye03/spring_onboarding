package com.hwang.minilog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class FollowResponseDto { // 응답
  @NonNull private Long followerId; // 팔로우 id
  @NonNull private Long followeeId; // 팔로위 id
}
