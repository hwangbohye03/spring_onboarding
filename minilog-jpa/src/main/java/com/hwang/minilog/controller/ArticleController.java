package com.hwang.minilog.controller;

import com.hwang.minilog.dto.ArticleRequestDto;
import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    - 엔드포인트 : '/api/v1/article`
    - 게시글 생성, 조회, 수정, 삭제 기능 제공
*/

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
  private final ArticleService articleService;

  @Autowired
  public ArticleController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostMapping
  @Operation(summary = "게시글 생성")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "사용자 없음")
  })
  public ResponseEntity<ArticleResponseDto> createArticle(@RequestBody ArticleRequestDto article) {
    ArticleResponseDto createdArticle =
        articleService.createArticle(article.getContent(), article.getAuthorId());
    return ResponseEntity.ok(createdArticle);
  }

  @GetMapping("/{articleId}")
  @Operation(summary = "게시글 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "게시글 없음")
  })
  public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable Long articleId) {
    ArticleResponseDto article = articleService.getArticleById(articleId);
    return ResponseEntity.ok(article);
  }

  @PutMapping("/{articleId}")
  @Operation(summary = "게시글 수정")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "게시글 없음")
  })
  public ResponseEntity<ArticleResponseDto> updateArticle(
      @PathVariable Long articleId, @RequestBody ArticleRequestDto article) {
    ArticleResponseDto updatedArticle =
        articleService.updateArticle(articleId, article.getContent());
    return ResponseEntity.ok(updatedArticle);
  }

  @DeleteMapping("/{articleId}")
  @Operation(summary = "게시글 삭제")
  @ApiResponses({
    @ApiResponse(responseCode = "204", description = "성공"),
    @ApiResponse(responseCode = "404", description = "게시글 없음")
  })
  public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
    articleService.deleteArticle(articleId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "유저의 게시글 조회")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "성공"),
    @ApiResponse(responseCode = "404", description = "게시글 없음")
  })
  public ResponseEntity<List<ArticleResponseDto>> getArticleByUserId(@RequestParam Long authorId) {
    List<ArticleResponseDto> articleList = articleService.getArticleListByUserId(authorId);
    return ResponseEntity.ok(articleList);
  }
}
