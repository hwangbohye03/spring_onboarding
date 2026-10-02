package com.hwang.minilog.util;

import com.hwang.minilog.dto.ArticleResponseDto;
import com.hwang.minilog.dto.FollowResponseDto;
import com.hwang.minilog.dto.UserResponseDto;
import com.hwang.minilog.entity.Article;
import com.hwang.minilog.entity.Follow;
import com.hwang.minilog.entity.User;

public class EntityDtoMapper {
  // Entity -> DTO
  public static UserResponseDto toDto(User user) {
    return UserResponseDto.builder().id(user.getId()).username(user.getUsername()).build();
  }

  public static ArticleResponseDto toDto(Article article) {
    return ArticleResponseDto.builder()
        .articleId(article.getId())
        .content(article.getContent())
        .authorId(article.getAuthor().getId())
        .authorName(article.getAuthor().getUsername())
        .createdAt(article.getCreatedAt())
        .build();
  }

  public static FollowResponseDto toDto(Follow follow) {
    return FollowResponseDto.builder()
        .followeeId(follow.getFollowee().getId())
        .followerId(follow.getFollower().getId())
        .build();
  }

  // DTO -> Entity
  public static Follow toEntity(Long followerId, Long followeeId) {
    return Follow.builder()
        .followee(User.builder().id(followeeId).build())
        .follower(User.builder().id(followerId).build())
        .build();
  }
}
