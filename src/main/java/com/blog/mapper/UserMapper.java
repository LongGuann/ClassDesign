package com.blog.mapper;

import com.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectById(@Param("userId") Integer userId);

    User selectByUsername(@Param("username") String username);

    User selectByEmail(@Param("email") String email);

    int insert(User user);

    int update(User user);

    int updatePassword(@Param("userId") Integer userId, @Param("password") String password);
}
