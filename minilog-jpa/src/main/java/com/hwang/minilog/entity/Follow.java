package com.hwang.minilog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity // 엔티티 클래스로 선언
@Table(
    name = "follows", // 매핑 DB 테이블명 명시
    indexes = { // 인덱스 설정
      @Index(name = "idx_follower_id", columnList = "follower_id"),
      @Index(name = "idx_followee_id", columnList = "followee_id")
    },
    uniqueConstraints = { // 유니크 제약 조건 설정
      @UniqueConstraint(columnNames = {"follower_id", "followee_id"})
    })
@Data // 롬복 Getter, Setter 등 자동 생성 기
@Builder // 빌더 패턴으로 객체 생성 가능하게 설정
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 감사 기능 사용 선언
public class Follow {
  // PK
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 DB 위임
  private Long id;

  // 팔로위 (FK) // follow 당한 사람 (ex: 스타)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followee_id", nullable = false)
  private User followee;

  // 팔로워 (FK) // follow 건 사람 (ex: 팬)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "follower_id", nullable = false)
  private User follower;

  // 생성 시간
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  // 마지막 수정 시간
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;
}
