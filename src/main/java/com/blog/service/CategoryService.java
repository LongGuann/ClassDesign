package com.blog.service;

import com.blog.entity.Category;
import java.util.List;

public interface CategoryService {
    Category findById(Integer categoryId);

    List<Category> findAll();

    Category create(Category category);

    Category update(Category category);

    void delete(Integer categoryId);
}
