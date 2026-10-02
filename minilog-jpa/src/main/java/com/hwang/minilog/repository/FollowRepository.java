package com.hwang.minilog.repository;

import com.hwang.minilog.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
  // 특정 사용자 (followerId)가 팔로우하는 팔로우 관계들 조회
  List<Follow> findByFollowerId(Long followerId);

  // 두 대상 간 팔로우 관계가 존재하는 지 조회
  Optional<Follow> findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
