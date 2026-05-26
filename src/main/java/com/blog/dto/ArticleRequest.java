package com.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ArticleRequest {
    @NotBlank(message = "文章标题不能为空")
    private String title;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    private String summary;

    private String coverImage;

    @NotNull(message = "请选择分类")
    private Integer categoryId;

    private Integer status;
}
