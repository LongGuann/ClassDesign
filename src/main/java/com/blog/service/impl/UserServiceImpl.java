package com.blog.service.impl;

import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Integer userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public User register(User user) {
        // 检查用户名是否已存在
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        // 检查邮箱是否已注册
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("邮箱已被注册");
        }
        // 密码加密
        user.setPassword(encryptPassword(user.getPassword()));
        user.setRole("user");
        user.setStatus(1);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new IllegalArgumentException("账号已被禁用，请联系管理员");
        }
        String encryptedPwd = encryptPassword(password);
        if (!user.getPassword().equals(encryptedPwd)) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        userMapper.update(user);
        return userMapper.selectById(user.getUserId());
    }

    /**
     * 使用 SHA-256 对密码进行加密（简化方案，无需 BCrypt 依赖）
     */
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }
}
