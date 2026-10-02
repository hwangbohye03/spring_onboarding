package com.hwang.minilog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.entity.Article;
import com.hwang.minilog.entity.Follow;
import com.hwang.minilog.entity.User;
import com.hwang.minilog.repository.ArticleRepository;
import com.hwang.minilog.repository.FollowRepository;
import com.hwang.minilog.repository.UserRepository;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers // 클래스 단 Testcontainers사용 활성화, @Container 필드 인식하고 각 테스트에 대해 컨테이너 생명주기 관리
@SpringBootTest // JUnit5가 스프링의 테스트 (컨텍스트) 기능(@Autowired,@Transactional 등) 사용할 수 있게 연동
@ExtendWith(SpringExtension.class)
public class ArticleServiceTest {
  // 컨테이너 설정 추가하기
  // 테스트 환경에서 사용할 컨테이너 선언
  // 컨테이너는 테스트 실행 전 자동 시작되며, 테스트 끝나면 자동 종료됨
  @Container
  public static MySQLContainer<?> mysqlContainer =
      new MySQLContainer<>("mysql:8.0.32")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  // 동적 속성 부여 설정 추가하기
  // 테스트 실행에 필요한 속성을 동적 주입
  // ex) mySQLContainer 실행 시, 컨테이너에서 생성된 DB의 (url,사용자 이름,pw)등
  //     DB 연결에 필요 정보를 스프링 애플리케이션 컨텍스트에 자동 주입함
  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("Spring.datasource.url", mysqlContainer::getJdbcUrl);
    registry.add("Spring.datasource.username", mysqlContainer::getUsername);
    registry.add("Spring.datasource.password", mysqlContainer::getPassword);
  }

  private ArticleService articleService;

  @Autowired private ArticleRepository articleRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private FollowRepository followRepository;

  User user1;
  User user2;
  Article article1;
  Article article2;
  Follow follow;

  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @BeforeEach
  @Transactional
  void setup() {
    articleRepository.deleteAll();
    userRepository.deleteAll();
    followRepository.deleteAll();

    articleService = new ArticleService(articleRepository, userRepository);

    user1 = userRepository.save(User.builder().username("user1").password("user1").build());
    user2 = userRepository.save(User.builder().username("user2").password("user2").build());

    article1 =
        articleRepository.save(
            Article.builder()
                .content("Test article1")
                .author(
                    userRepository
                        .findById(user1.getId())
                        .get()) // 레포 조회 결과 Optional<User>이므로 get으로 Optional 벗겨서 가져오기
                .build());
    article2 =
        articleRepository.save(
            Article.builder()
                .content("Test article2")
                .author(
                    userRepository
                        .findById(user2.getId())
                        .get()) // 레포 조회 결과 Optional<User>이므로 get으로 Optional 벗겨서 가져오기
                .build());

    follow = followRepository.save(Follow.builder().followee(user1).follower(user2).build());
  }

  @Test
  @Transactional
  void testCreateArticle() {
    ArticleResponseDto article = articleService.createArticle("Test Article3", user1.getId());

    assertThat(article.getContent()).isEqualTo("Test Article3");
    assertThat(article.getAuthorId()).isEqualTo(user1.getId());
    assertThat(articleRepository.findAll().size()).isEqualTo(3);
  }

  @Test
  @Transactional
  void testGetArticleById() {
    ArticleResponseDto article = articleService.getArticleById(article1.getId());

    assertThat(article.getArticleId()).isEqualTo(article1.getId());
    assertThat(article.getContent()).isEqualTo(article1.getContent());
    assertThat(article.getAuthorId()).isEqualTo(article1.getAuthor().getId());
    assertThat(article.getAuthorName()).isEqualTo(article1.getAuthor().getUsername());
    assertThat(dateTimeFormatter.format(article.getCreatedAt()))
        .isEqualTo(dateTimeFormatter.format(article1.getCreatedAt()));
  }

  @Test
  @Transactional
  void testGetArticleListByUserId() {
    ArticleResponseDto article = articleService.getArticleListByUserId(user1.getId()).getFirst();

    assertThat(article.getArticleId()).isEqualTo(article1.getId());
    assertThat(article.getContent()).isEqualTo(article1.getContent());
    assertThat(article.getAuthorId()).isEqualTo(article1.getAuthor().getId());
    assertThat(article.getAuthorName()).isEqualTo(article1.getAuthor().getUsername());
    assertThat(dateTimeFormatter.format(article.getCreatedAt()))
        .isEqualTo(dateTimeFormatter.format(article1.getCreatedAt()));
  }

  @Test
  @Transactional
  void testGetFeedListByFollowerId() {
    ArticleResponseDto article =
        articleService.getFeedListByFollowerId(follow.getFollower().getId()).getFirst();
    ArticleResponseDto target =
        articleService.getArticleListByUserId(article.getAuthorId()).getFirst();

    assertThat(article.getArticleId()).isEqualTo(target.getArticleId());
    assertThat(article.getContent()).isEqualTo(target.getContent());
    assertThat(article.getAuthorId()).isEqualTo(target.getAuthorId());
    assertThat(article.getAuthorName()).isEqualTo(target.getAuthorName());
    assertThat(dateTimeFormatter.format(article.getCreatedAt()))
        .isEqualTo(dateTimeFormatter.format(target.getCreatedAt()));
  }

  @Test
  @Transactional
  void testDeleteArticle() {
    assertThat(articleRepository.findAll().size()).isEqualTo(2);

    Long articleId = article1.getId(); // 삭제할 게시글 ID 추출
    articleService.deleteArticle(articleId);

    assertThat(articleRepository.findAll().size()).isEqualTo(1);
  }

  @Test
  @Transactional
  void testUpdateArticle() {
    Long articleId = article1.getId(); // 수정할 게시글 ID 추출
    ArticleResponseDto article = articleService.updateArticle(articleId, "updated article 1");

    assertThat(article.getArticleId()).isEqualTo(article1.getId());
    assertThat(article.getContent()).isEqualTo("updated article 1");
    assertThat(article.getAuthorId()).isEqualTo(article1.getAuthor().getId());
    assertThat(article.getAuthorName()).isEqualTo(article1.getAuthor().getUsername());
    assertThat(dateTimeFormatter.format(article.getCreatedAt()))
        .isEqualTo(dateTimeFormatter.format(article1.getCreatedAt()));
  }
}
