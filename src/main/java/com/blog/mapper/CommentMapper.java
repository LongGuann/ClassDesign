package com.blog.mapper;

import com.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    Comment selectById(@Param("commentId") Integer commentId);

    List<Comment> selectByArticleId(@Param("articleId") Integer articleId);

    int insert(Comment comment);

    int deleteById(@Param("commentId") Integer commentId);

    int deleteByArticleId(@Param("articleId") Integer articleId);
}
