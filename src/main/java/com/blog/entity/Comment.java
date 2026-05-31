package com.blog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    private Integer commentId;
    private String content;
    private Integer articleId;
    private Integer userId;
    private Integer parentId;
    private LocalDateTime createTime;

    // 关联字段（非数据库字段）
    private String authorName;
    private String authorAvatar;
    private String replyToName;  // 被回复者的昵称

    // 子评论列表（前端渲染用）
    private List<Comment> replies;
}
