package com.blog.service.impl;

import com.blog.dto.PageResult;
import com.blog.entity.Article;
import com.blog.mapper.ArticleMapper;
import com.blog.service.ArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Article findById(Integer articleId) {
        return articleMapper.selectById(articleId);
    }

    @Override
    public Article findDetailById(Integer articleId) {
        return articleMapper.selectDetailById(articleId);
    }

    @Override
    public PageResult<Article> findAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll();
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageNum, pageSize, pageInfo.getTotal());
    }

    @Override
    public PageResult<Article> findByUserId(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectByUserId(userId);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageNum, pageSize, pageInfo.getTotal());
    }

    @Override
    public PageResult<Article> search(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.search(keyword);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        return PageResult.of(pageInfo.getList(), pageNum, pageSize, pageInfo.getTotal());
    }

    @Override
    @Transactional
    public Article create(Article article) {
        if (article.getStatus() == null) {
            article.setStatus(1); // 默认为发布状态
        }
        article.setViewCount(0);
        articleMapper.insert(article);
        return article;
    }

    @Override
    @Transactional
    public Article update(Article article) {
        articleMapper.update(article);
        return articleMapper.selectById(article.getArticleId());
    }

    @Override
    @Transactional
    public void delete(Integer articleId, Integer userId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new IllegalArgumentException("文章不存在");
        }
        articleMapper.deleteById(articleId);
    }

    @Override
    @Transactional
    public void incrementViewCount(Integer articleId) {
        articleMapper.incrementViewCount(articleId);
    }
}
