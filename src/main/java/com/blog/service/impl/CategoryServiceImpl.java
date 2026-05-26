package com.blog.service.impl;

import com.blog.entity.Category;
import com.blog.mapper.CategoryMapper;
import com.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Category findById(Integer categoryId) {
        return categoryMapper.selectById(categoryId);
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Override
    @Transactional
    public Category create(Category category) {
        categoryMapper.insert(category);
        return category;
    }

    @Override
    @Transactional
    public Category update(Category category) {
        categoryMapper.update(category);
        return categoryMapper.selectById(category.getCategoryId());
    }

    @Override
    @Transactional
    public void delete(Integer categoryId) {
        categoryMapper.deleteById(categoryId);
    }
}
