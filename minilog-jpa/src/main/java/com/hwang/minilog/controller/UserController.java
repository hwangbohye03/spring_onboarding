package com.hwang.minilog.controller;

import com.hwang.minilog.dto.UserRequestDto;
import com.hwang.minilog.dto.UserResponseDto;
import com.hwang.minilog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    - 엔드포인트 : '/api/v1/user`
    - 사용자 생성, 조회, 수정, 삭제 기능 제공
*/

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  @Operation(summary = "사용자 목록 조회")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  public ResponseEntity<Iterable<UserResponseDto>> getUsers() { // Iterable: 자바 컬렉션 프레임워크의 최상위 인터페이스
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping("/{userId}")
  @Operation(summary = "사용자 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
    return ResponseEntity.ok(userService.getUserById(userId));
  }

  @PostMapping
  @Operation(summary = "사용자 생성")
  @ApiResponses({@ApiResponse(responseCode = "202", description = "성공")})
  public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
    UserResponseDto createdUser = userService.createUser(user);
    return ResponseEntity.ok(createdUser);
  }

  @PutMapping("/{userId}")
  @Operation(summary = "사용자 수정")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable Long userId, @RequestBody UserRequestDto user) {
    UserResponseDto updatedUser = userService.updateUser(userId, user);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "사용자 삭제")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }
}
