package com.blog.controller;

import com.blog.entity.Category;
import com.blog.service.CategoryService;
import com.blog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类管理页面
     */
    @GetMapping("/manage-page")
    public String managePage() {
        return "category-manage";
    }

    /**
     * 获取所有分类（API）
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<List<Category>> list() {
        List<Category> categories = categoryService.findAll();
        return Result.success(categories);
    }

    /**
     * 创建分类（API）
     */
    @PostMapping
    @ResponseBody
    public Result<Category> create(@RequestBody Category category, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden("仅管理员可操作");
        }
        categoryService.create(category);
        return Result.success("创建成功", category);
    }

    /**
     * 更新分类（API）
     */
    @PutMapping("/{id}")
    @ResponseBody
    public Result<Category> update(@PathVariable Integer id, @RequestBody Category category,
                                   HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden("仅管理员可操作");
        }
        category.setCategoryId(id);
        categoryService.update(category);
        return Result.success("更新成功", category);
    }

    /**
     * 删除分类（API）
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.forbidden("仅管理员可操作");
        }
        categoryService.delete(id);
        return Result.success("删除成功", null);
    }
}
