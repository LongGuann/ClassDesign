package com.blog.mapper;

import com.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {
    Category selectById(@Param("categoryId") Integer categoryId);

    List<Category> selectAll();

    Integer selectMaxSortOrder();

    int insert(Category category);

    int update(Category category);

    int deleteById(@Param("categoryId") Integer categoryId);
}
