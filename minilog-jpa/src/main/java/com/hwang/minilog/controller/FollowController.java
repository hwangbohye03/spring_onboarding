package com.hwang.minilog.controller;

import com.hwang.minilog.dto.FollowRequestDto;
import com.hwang.minilog.dto.FollowResponseDto;
import com.hwang.minilog.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    - 엔드포인트 : '/api/v1/follow`
    - 사용자의 팔로우, 언팔로우, 팔로우 목록 조회 기능 제공
*/

@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {
  private final FollowService followService;

  @Autowired
  public FollowController(FollowService followService) {
    this.followService = followService;
  }

  @PostMapping
  @Operation(summary = "팔로우")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<FollowResponseDto> follow(@RequestBody FollowRequestDto request) {
    FollowResponseDto follow =
        followService.follow(request.getFollowerId(), request.getFolloweeId());
    return ResponseEntity.ok(follow);
  }

  @DeleteMapping("/{followerId}/{followeeId}")
  @Operation(summary = "언팔로우")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<Void> unfollow(
      @PathVariable Long followerId, @PathVariable Long followeeId) {
    followService.unfollow(followerId, followeeId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{followerId}")
  @Operation(summary = "팔로잉 목록 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<List<FollowResponseDto>> getFollowList(@PathVariable Long followerId) {
    List<FollowResponseDto> followList = followService.getFollowList(followerId);
    return ResponseEntity.ok(followList);
  }
}
