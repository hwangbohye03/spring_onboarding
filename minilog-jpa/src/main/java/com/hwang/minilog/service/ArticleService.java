package com.hwang.minilog.service;

import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.entity.Article;
import com.hwang.minilog.entity.User;
import com.hwang.minilog.exception.ArticleNotFoundException;
import com.hwang.minilog.exception.UserNotFoundException;
import com.hwang.minilog.repository.ArticleRepository;
import com.hwang.minilog.repository.UserRepository;
import com.hwang.minilog.util.EntityDtoMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
    isolation = Isolation.REPEATABLE_READ) // 클래스 모든 메서드 트랜젝션 적용 // 데이터 조회 중 변동 맊는 격리 수준 설정
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  // 생성자 (의존성 주입)
  @Autowired
  public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;
  }

  // createArticle
  public ArticleResponseDto createArticle(String content, Long userId) {
    // 작성자 유효성 확인
    User author =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId)));
    // 생성 아티클 구성
    Article article = Article.builder().author(author).content(content).build();
    // 아티클 생성
    Article savedArticle = articleRepository.save(article);
    return EntityDtoMapper.toDto(savedArticle);
  }

  // deleteArticle
  public void deleteArticle(Long articleId) {
    // 아티클 유효성 확인
    Article article =
        articleRepository
            .findById(articleId)
            .orElseThrow(
                () ->
                    new ArticleNotFoundException(
                        String.format("해당 아이디(%d)를 가진 게시글을 찾을 수 없습니다.", articleId)));
    // 아티클 삭제
    articleRepository.deleteById(articleId);
  }

  // updateArticle
  public ArticleResponseDto updateArticle(Long articleId, String content) {
    // 아티클 유효성 확인
    Article article =
        articleRepository
            .findById(articleId)
            .orElseThrow(
                () ->
                    new ArticleNotFoundException(
                        String.format("해당 아이디(%d)를 가진 게시글을 찾을 수 없습니다.", articleId)));
    // 아티클 수정 사항 반영
    article.setContent(content);
    Article updatedArticle = articleRepository.save(article);

    return EntityDtoMapper.toDto(updatedArticle);
  }

  // getArticleById
  @Transactional(readOnly = true)
  public ArticleResponseDto getArticleById(Long articleId) {
    // 아티클 유효성 확인
    Article article =
        articleRepository
            .findById(articleId)
            .orElseThrow(
                () ->
                    new ArticleNotFoundException(
                        String.format("해당 아이디(%d)를 가진 게시글을 찾을 수 없습니다.", articleId)));

    return EntityDtoMapper.toDto(article);
  }

  // getFeedListByFollowerId // user의 followee가 작성한 게시글 리스트 조회
  @Transactional(readOnly = true)
  public List<ArticleResponseDto> getFeedListByFollowerId(Long userId) {
    // 사용자 유효성 확인
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId)));

    return articleRepository.findAllByFollowerId(user.getId()).stream()
        .map(EntityDtoMapper::toDto)
        .collect(Collectors.toList());
  }

  // getArticleListByUserId // user가 작성한 게시글 리스트 조회
  @Transactional(readOnly = true)
  public List<ArticleResponseDto> getArticleListByUserId(Long userId) {
    // 사용자 유효성 확인
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    new UserNotFoundException(
                        String.format("해당 아이디(%d)를 가진 사용자를 찾을 수 없습니다.", userId)));

    return articleRepository.findAllByAuthorId(userId).stream()
        .map(EntityDtoMapper::toDto)
        .collect(Collectors.toList());
  }
}
