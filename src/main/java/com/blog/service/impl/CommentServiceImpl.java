package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findByArticleId(Integer articleId) {
        return commentMapper.selectByArticleId(articleId);
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public void delete(Integer commentId, Integer userId, String role) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("评论不存在");
        }
        // 仅评论作者或管理员可删除
        if (!comment.getUserId().equals(userId) && !"admin".equals(role)) {
            throw new IllegalArgumentException("无权删除此评论");
        }
        commentMapper.deleteById(commentId);
    }
}
