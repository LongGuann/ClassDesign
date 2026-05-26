package com.blog.mapper;

import com.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    Article selectById(@Param("articleId") Integer articleId);

    Article selectDetailById(@Param("articleId") Integer articleId);

    List<Article> selectByUserId(@Param("userId") Integer userId);

    List<Article> selectByCategoryId(@Param("categoryId") Integer categoryId);

    List<Article> search(@Param("keyword") String keyword);

    List<Article> selectAll();

    int insert(Article article);

    int update(Article article);

    int deleteById(@Param("articleId") Integer articleId);

    int incrementViewCount(@Param("articleId") Integer articleId);
}
