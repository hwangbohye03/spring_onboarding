package com.hwang.minilog.repository;

import com.hwang.minilog.entity.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  // 특정 작성자가 작성한 모든 게시글 조회
  List<Article> findAllByAuthorId(Long authorId);

  // 팔로우 관계 기반 특정 팔로워(followerId)가 팔로위(follower의 스타)들이 작성한 게시글을 최신순 조회
  // ⭕ `JOIN a.author u`: Article(기준) 엔티티의 연관관계 필드(author)를 통해 User와 올바르게 명시적 조인
  // ❌ `JOIN User u`: Article(기준) 엔티티와의 연관관계 필드 경로가 아닌 엔티티명을 직접 조인하면 JPQL 파싱 에러 발생
  @Query(
      "SELECT a "
          +
          // FROM Article a JOIN Follow f ON a.author.id = f.followee.id // 대체 가능
          "From Article a JOIN a.author u JOIN Follow f ON u.id = f.followee.id "
          + "WHERE f.follower.id = :followerId "
          + "ORDER BY a.createdAt DESC")
  List<Article> findAllByFollowerId(@Param("followerId") Long followerId);
}
