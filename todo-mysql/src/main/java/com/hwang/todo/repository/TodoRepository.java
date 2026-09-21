package com.hwang.todo.repository;

import com.hwang.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo,Long> {} // <엔티티 타입, 기본키 타입>

// 스프링 JPA 사용 시 JpaRepository 인터페이스를 확장하여 리포지토리 정의하면,
// 별도 구현 클래스 생성 없이 스프링 데이터 Jpa가 자동으로 구현체를 생성해줌