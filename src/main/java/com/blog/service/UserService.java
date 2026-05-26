package com.blog.service;

import com.blog.entity.User;

public interface UserService {
    User findById(Integer userId);

    User findByUsername(String username);

    User register(User user);

    User login(String username, String password);

    User updateUser(User user);
}
