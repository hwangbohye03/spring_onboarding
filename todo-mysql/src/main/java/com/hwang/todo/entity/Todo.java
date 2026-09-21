package com.hwang.todo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    // 기본키 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @NonNull                                  // lombok 애노테이션으로, 자바 코드 단에서 검증 처리
    @Column(nullable = false, name = "title") // JPA 애노테이션으로, DB단에서 검증 처리
    private String title;

    // 설명
    @Column(name = "description")
    private String description;

    // 수행 여부
    @Column(nullable = false, name="completed")
    private boolean completed;

    // 생성 시점
    @Column(nullable = false,
            name = "created_at",
            insertable = false, // INSERT 쿼리 실행 시 이 필드를 제외
            updatable = false,  // UPDATE 쿼리 실행 시 이 필드를 제외
            columnDefinition = "TIMESTAMP DEFAUlT CURRENT_TIMESTAMP") // DB 서버 기준 지정
    private LocalDateTime createAt;
}
