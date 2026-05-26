package com.blog.service;

import com.blog.dto.PageResult;
import com.blog.entity.Article;

public interface ArticleService {
    Article findById(Integer articleId);

    Article findDetailById(Integer articleId);

    PageResult<Article> findAll(int pageNum, int pageSize);

    PageResult<Article> findByUserId(Integer userId, int pageNum, int pageSize);

    PageResult<Article> findByCategoryId(Integer categoryId, int pageNum, int pageSize);

    PageResult<Article> search(String keyword, int pageNum, int pageSize);

    Article create(Article article);

    Article update(Article article);

    void delete(Integer articleId, Integer userId);

    void incrementViewCount(Integer articleId);
}
