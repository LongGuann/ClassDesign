package com.blog.controller;

import com.blog.entity.Comment;
import com.blog.service.CommentService;
import com.blog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 获取文章评论列表（API）
     */
    @GetMapping("/list/{articleId}")
    public Result<List<Comment>> list(@PathVariable Integer articleId) {
        List<Comment> comments = commentService.findByArticleId(articleId);
        return Result.success(comments);
    }

    /**
     * 发表评论（API）
     */
    @PostMapping
    public Result<Comment> create(@RequestBody Comment comment, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        comment.setUserId(userId);
        commentService.create(comment);
        return Result.success("评论成功", comment);
    }

    /**
     * 回复评论（API）
     */
    @PostMapping("/reply")
    public Result<Comment> reply(@RequestBody Comment comment, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        comment.setUserId(userId);
        try {
            commentService.reply(comment);
            return Result.success("回复成功", comment);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 删除评论（API）
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        try {
            commentService.delete(id, userId, role);
            return Result.success("删除成功", null);
        } catch (IllegalArgumentException e) {
            return Result.error(403, e.getMessage());
        }
    }
}
