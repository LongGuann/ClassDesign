package com.blog.interceptor;

import com.blog.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 从请求头获取 Token
        String token = request.getHeader("Authorization");

        // 如果请求头没有，则从 Cookie 获取（用于页面导航请求）
        if (token == null || token.isEmpty()) {
            token = getTokenFromCookie(request);
        }

        if (token == null || token.isEmpty()) {
            handleUnauthorized(request, response, "未登录，请先登录");
            return false;
        }

        // 去除 "Bearer " 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证 Token
        if (!JwtUtil.validateToken(token)) {
            handleUnauthorized(request, response, "Token无效或已过期，请重新登录");
            return false;
        }

        // 将用户信息存入请求属性
        Integer userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        String role = JwtUtil.getRole(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        request.setAttribute("role", role);

        return true;
    }

    /**
     * 从 Cookie 中获取 Token
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("blog_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response,
                                    String message) throws Exception {
        // 判断是否是页面请求（浏览器直接访问）
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("text/html")) {
            // 页面请求 → 跳转到登录页
            response.sendRedirect("/api/users/login-page");
        } else {
            // API 请求 → 返回 JSON
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"code\":401,\"message\":\"" + message + "\",\"data\":null}"
            );
        }
    }
}
