package com.blog.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Comment {
    private Integer commentId;
    private String content;
    private Integer articleId;
    private Integer userId;
    private LocalDateTime createTime;

    // 关联字段（非数据库字段）
    private String authorName;
    private String authorAvatar;
}
