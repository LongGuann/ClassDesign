package com.blog.controller;

import com.blog.dto.LoginRequest;
import com.blog.dto.RegisterRequest;
import com.blog.entity.User;
import com.blog.service.UserService;
import com.blog.util.JwtUtil;
import com.blog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录页面
     */
    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    /**
     * 注册页面
     */
    @GetMapping("/register-page")
    public String registerPage() {
        return "register";
    }

    /**
     * 个人信息页面
     */
    @GetMapping("/profile-page")
    public String profilePage(HttpServletRequest request, Model model) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId != null) {
            model.addAttribute("user", userService.findById(userId));
        }
        return "profile";
    }

    /**
     * 用户登录（API）
     */
    @PostMapping("/login")
    @ResponseBody
    public Result<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        // 生成 Token
        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername(), user.getRole());
        return Result.success("登录成功", token);
    }

    /**
     * 用户注册（API）
     */
    @PostMapping("/register")
    @ResponseBody
    public Result<User> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setNickname(registerRequest.getNickname());
        user.setEmail(registerRequest.getEmail());
        userService.register(user);
        user.setPassword(null); // 不返回密码
        return Result.success("注册成功", user);
    }

    /**
     * 获取当前用户信息（API）
     */
    @GetMapping("/me")
    @ResponseBody
    public Result<User> getCurrentUser(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("未登录");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 更新用户信息（API）
     */
    @PutMapping("/profile")
    @ResponseBody
    public Result<User> updateProfile(HttpServletRequest request, @RequestBody User updateUser) {
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("未登录");
        }
        updateUser.setUserId(userId);
        User user = userService.updateUser(updateUser);
        user.setPassword(null);
        return Result.success("更新成功", user);
    }

    /**
     * 退出登录（客户端清除 Token 即可，此接口仅作标记）
     */
    @PostMapping("/logout")
    @ResponseBody
    public Result<Void> logout() {
        return Result.success("退出成功", null);
    }
}
