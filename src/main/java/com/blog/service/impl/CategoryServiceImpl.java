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
        // 检查名称是否已存在
        List<Category> all = categoryMapper.selectAll();
        boolean exists = all.stream()
                .anyMatch(c -> c.getName().equals(category.getName()));
        if (exists) {
            throw new IllegalArgumentException("分类名称已存在");
        }
        // 设置默认排序
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
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
