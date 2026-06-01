package com.blog.config;

import com.blog.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        // 仅公开API和首页（无需登录）
                        "/api/users/login",
                        "/api/users/register",
                        "/api/users/login-page",
                        "/api/users/register-page",
                        "/api/articles/list",
                        "/api/articles/detail/**",
                        "/api/articles/detail-page/**",
                        "/api/articles/index",
                        "/api/categories/list",
                        "/api/comments/list/**",
                        // 静态资源
                        "/static/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
