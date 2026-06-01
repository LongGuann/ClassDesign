package com.blog.controller;

import com.blog.dto.ArticleRequest;
import com.blog.dto.PageResult;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.service.ArticleService;
import com.blog.service.CategoryService;
import com.blog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 首页（文章列表页面）
     */
    @GetMapping("/index")
    public String indexPage(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(required = false) Integer categoryId,
                            @RequestParam(required = false) String keyword) {
        PageResult<Article> pageResult;
        if (keyword != null && !keyword.isEmpty()) {
            pageResult = articleService.search(keyword, page, 10);
            model.addAttribute("keyword", keyword);
        } else if (categoryId != null) {
            pageResult = articleService.findByCategoryId(categoryId, page, 10);
            model.addAttribute("categoryId", categoryId);
        } else {
            pageResult = articleService.findAll(page, 10);
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("categories", categories);
        return "index";
    }

    /**
     * 文章详情页面
     */
    @GetMapping("/detail-page/{id}")
    public String detailPage(@PathVariable Integer id, Model model) {
        Article article = articleService.findDetailById(id);
        if (article == null) {
            return "error/404";
        }
        articleService.incrementViewCount(id);
        model.addAttribute("article", article);
        return "article-detail";
    }

    /**
     * 发布文章页面
     */
    @GetMapping("/create-page")
    public String createPage(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "article-create";
    }

    /**
     * 编辑文章页面
     */
    @GetMapping("/edit-page/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        Article article = articleService.findDetailById(id);
        if (article == null) {
            return "error/404";
        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("article", article);
        model.addAttribute("categories", categories);
        return "article-edit";
    }

    /**
     * 我的文章管理页面
     */
    @GetMapping("/manage-page")
    public String managePage(HttpServletRequest request, Model model,
                             @RequestParam(defaultValue = "1") int page) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return "redirect:/api/users/login-page";
        }
        PageResult<Article> pageResult = articleService.findByUserId(userId, page, 10);
        model.addAttribute("pageResult", pageResult);
        return "article-manage";
    }

    // ==================== RESTful API ====================

    /**
     * 获取文章列表（API）
     */
    @GetMapping("/list")
    @ResponseBody
    public Result<PageResult<Article>> list(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(required = false) Integer categoryId,
                                            @RequestParam(required = false) String keyword) {
        PageResult<Article> pageResult;
        if (keyword != null && !keyword.isEmpty()) {
            pageResult = articleService.search(keyword, page, 10);
        } else if (categoryId != null) {
            pageResult = articleService.findByCategoryId(categoryId, page, 10);
        } else {
            pageResult = articleService.findAll(page, 10);
        }
        return Result.success(pageResult);
    }

    /**
     * 获取文章详情（API）
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    public Result<Article> detail(@PathVariable Integer id) {
        Article article = articleService.findDetailById(id);
        if (article == null) {
            return Result.error(404, "文章不存在");
        }
        articleService.incrementViewCount(id);
        return Result.success(article);
    }

    /**
     * 发布文章（API）
     */
    @PostMapping
    @ResponseBody
    public Result<Article> create(@Valid @RequestBody ArticleRequest articleRequest,
                                  HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        Article article = new Article();
        article.setTitle(articleRequest.getTitle());
        article.setContent(articleRequest.getContent());
        article.setSummary(articleRequest.getSummary());
        article.setCoverImage(articleRequest.getCoverImage());
        article.setCategoryId(articleRequest.getCategoryId());
        article.setUserId(userId);
        article.setStatus(articleRequest.getStatus() != null ? articleRequest.getStatus() : 1);
        articleService.create(article);
        return Result.success("发布成功", article);
    }

    /**
     * 更新文章（API）
     */
    @PutMapping("/{id}")
    @ResponseBody
    public Result<Article> update(@PathVariable Integer id,
                                  @Valid @RequestBody ArticleRequest articleRequest,
                                  HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        Article existing = articleService.findById(id);
        if (existing == null) {
            return Result.error(404, "文章不存在");
        }
        Article article = new Article();
        article.setArticleId(id);
        article.setTitle(articleRequest.getTitle());
        article.setContent(articleRequest.getContent());
        article.setSummary(articleRequest.getSummary());
        article.setCoverImage(articleRequest.getCoverImage());
        article.setCategoryId(articleRequest.getCategoryId());
        article.setStatus(articleRequest.getStatus());
        articleService.update(article);
        return Result.success("更新成功", articleService.findDetailById(id));
    }

    /**
     * 删除文章（API）
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<Void> delete(@PathVariable Integer id, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        try {
            articleService.delete(id, userId);
            return Result.success("删除成功", null);
        } catch (IllegalArgumentException e) {
            return Result.error(403, e.getMessage());
        }
    }
}
