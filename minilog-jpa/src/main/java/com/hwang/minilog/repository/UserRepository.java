package com.hwang.minilog.repository;

import com.hwang.minilog.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // 이름 기반 사용자 조회
  // Optional<User> : 동명이인 있으면 예외 발생 // List<User> : 동명이인들 모두 List로 반환
  Optional<User> findByUsername(String username);
}
