package com.hwang.minilog.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity // 엔티티 클래스로 선언
@Table(name = "users") // 매핑 DB 테이블명 명시
@Data // 롬복 Getter, Setter 등 자동 생성 기능
@Builder // 빌더 패턴으로 객체 생성 가능하게 설정
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 감사 기능 사용 선언
public class User {
  // pk
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 DB 위임
  private Long id;

  // 사용자 이름
  @Column(nullable = false, unique = true) // 중복 값 불허
  private String username;

  // 사용자 계정 비밀번호
  @Column(nullable = false)
  private String password;

  // 생성시간
  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false) // 수정 시 값 변경되지 않도록 보호
  private LocalDateTime createdAt;

  // 마지막 수정시간
  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // 작성 게시글
  @OneToMany(
      mappedBy = "author", // Article (연관)엔티티의 author (fk)필드와 매핑
      cascade = CascadeType.ALL, // User의 생성·수정·삭제 상태 변경을 Article에게 전파
      orphanRemoval = true, // 부모와 관계가 끊어진 고아 객체(Article)를 DB에서 자동 삭제
      fetch = FetchType.LAZY // 지연 로딩 (User 조회시 Article 로딩 X → 필요할 때 로딩)
      )
  private List<Article> articles;
}
