package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.mapper.CommentMapper;
import com.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> findByArticleId(Integer articleId) {
        List<Comment> all = commentMapper.selectByArticleId(articleId);
        // 构建树形结构：将平铺的评论转为父子嵌套
        List<Comment> topLevel = all.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());
        for (Comment parent : topLevel) {
            parent.setReplies(all.stream()
                    .filter(c -> parent.getCommentId().equals(c.getParentId()))
                    .collect(Collectors.toList()));
        }
        return topLevel;
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {
        comment.setParentId(null);
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment reply(Comment comment) {
        if (comment.getParentId() == null) {
            throw new IllegalArgumentException("回复必须指定父评论ID");
        }
        // 验证父评论存在
        Comment parent = commentMapper.selectById(comment.getParentId());
        if (parent == null) {
            throw new IllegalArgumentException("要回复的评论不存在");
        }
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
        // 级联删除子回复
        commentMapper.deleteByParentId(commentId);
        commentMapper.deleteById(commentId);
    }
}
