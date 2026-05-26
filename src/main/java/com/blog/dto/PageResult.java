package com.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> list;          // 数据列表
    private int pageNum;           // 当前页码
    private int pageSize;          // 每页条数
    private long total;            // 总记录数
    private int pages;             // 总页数

    public static <T> PageResult<T> of(List<T> list, int pageNum, int pageSize, long total) {
        int pages = (int) (total / pageSize) + (total % pageSize == 0 ? 0 : 1);
        return new PageResult<>(list, pageNum, pageSize, total, pages);
    }
}
