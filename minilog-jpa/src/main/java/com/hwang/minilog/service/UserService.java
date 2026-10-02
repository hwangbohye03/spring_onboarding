package com.hwang.minilog.service;

import com.hwang.minilog.dto.UserRequestDto;
import com.hwang.minilog.dto.UserResponseDto;
import com.hwang.minilog.entity.User;
import com.hwang.minilog.exception.UserNotFoundException;
import com.hwang.minilog.repository.UserRepository;
import com.hwang.minilog.util.EntityDtoMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 클래스 내 모든 메서드에 트랜젝션 적용
public class UserService {
  private final UserRepository userRepository;

  // 생성자 (의존성 주입)
  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // GetUsers
  @Transactional(readOnly = true) // 읽기 전용 -> 성능 최적화 + 무결성 보장
  public List<UserResponseDto> getUsers() {
    return userRepository.findAll().stream()
        .map(EntityDtoMapper::toDto)
        .collect(Collectors.toList());
  }

  // GetUserById
  @Transactional(readOnly = true)
  public UserResponseDto getUserById(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 갖는 사용자를 찾을 수 없습니다.", userId)));
    return EntityDtoMapper.toDto(user);
  }

  // CreateUser
  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    // username 중복 확인 (동명이인 불허)
    if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }

    // DB 저장 처리
    User savedUser =
        userRepository.save(
            User.builder()
                .username(userRequestDto.getUsername())
                .password(userRequestDto.getPassword())
                .build());

    return EntityDtoMapper.toDto(savedUser);
  }

  // UpdateUser
  public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없수 없습니다.", userId)));

    user.setUsername(userRequestDto.getUsername());
    user.setPassword(userRequestDto.getPassword());

    User updatedUser = userRepository.save(user);

    return EntityDtoMapper.toDto(updatedUser);
  }

  // DeleteUser
  public void deleteUser(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId)));

    userRepository.deleteById(user.getId());
  }
}
