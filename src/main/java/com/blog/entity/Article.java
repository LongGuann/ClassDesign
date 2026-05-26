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
public class Article {
    private Integer articleId;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer userId;
    private Integer categoryId;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 关联字段（非数据库字段，由 JOIN 查询填充）
    private String authorName;
    private String categoryName;
}
