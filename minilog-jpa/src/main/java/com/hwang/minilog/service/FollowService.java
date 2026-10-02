package com.hwang.minilog.service;

import com.hwang.minilog.dto.FollowResponseDto;
import com.hwang.minilog.entity.Follow;
import com.hwang.minilog.entity.User;
import com.hwang.minilog.exception.UserNotFoundException;
import com.hwang.minilog.repository.FollowRepository;
import com.hwang.minilog.repository.UserRepository;
import com.hwang.minilog.util.EntityDtoMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // 클래스 내 모든 메서드 트랜젝션 적용
public class FollowService {
  private final FollowRepository followRepository;
  private final UserRepository userRepository;

  // 생성자 (의존성 주입)
  @Autowired
  public FollowService(FollowRepository followRepository, UserRepository userRepository) {
    this.followRepository = followRepository;
    this.userRepository = userRepository;
  }

  // follow (create)
  public FollowResponseDto follow(Long followerId, Long followeeId) {
    // 팔로우 관계 성립 조건 확인
    if (followerId.equals(followeeId)) { // 불가 조건
      throw new IllegalArgumentException("자신을 팔로우할 수 없습니다.");
    }

    // 사용자 유효성 확인
    User follower =
        userRepository
            .findById(followerId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", followerId)));
    User followee =
        userRepository
            .findById(followeeId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", followeeId)));

    // 팔로우 처리 (create)
    Follow follow =
        followRepository.save(EntityDtoMapper.toEntity(follower.getId(), followee.getId()));
    return EntityDtoMapper.toDto(follow);
  }

  // unfollow (delete)
  public void unfollow(Long followerId, Long followeeId) {
    // 팔로우 관계 유효성 확인
    Follow follow =
        followRepository
            .findByFollowerIdAndFolloweeId(followerId, followeeId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format(
                            "팔로어(%d)와 팔로이(%d)을 연결하는 Follow를 찾을 수 없습니다.", followerId, followeeId)));

    // 언팔로우 처리 (delete)
    followRepository.delete(follow);
  }

  // getFollowList
  @Transactional(readOnly = true)
  public List<FollowResponseDto> getFollowList(Long userId) {
    // 사용자 유효성 확인
    if (userRepository.findById(userId).isEmpty()) { // 사용자 부재
      throw new UserNotFoundException(String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId));
    }

    // 조회 처리
    return followRepository.findByFollowerId(userId).stream()
        .map(EntityDtoMapper::toDto)
        .collect(Collectors.toList());
  }
}
