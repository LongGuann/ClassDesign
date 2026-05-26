-- ============================================================
-- 墨言 - 数据库初始化脚本
-- 数据库版本: MySQL 8.0
-- 数据库名称: blog_db
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- ============================================================
-- 1. 用户表 (user)
-- ============================================================
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `article`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `user_id`     INT          NOT NULL AUTO_INCREMENT  COMMENT '用户ID',
    `username`    VARCHAR(50)  NOT NULL                  COMMENT '用户名（唯一）',
    `password`    VARCHAR(255) NOT NULL                  COMMENT '密码（BCrypt加密）',
    `nickname`    VARCHAR(50)  NOT NULL                  COMMENT '用户昵称',
    `email`       VARCHAR(100) NOT NULL                  COMMENT '邮箱（唯一）',
    `avatar`      VARCHAR(255) DEFAULT NULL              COMMENT '头像URL',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'user'   COMMENT '角色：user/user_admin',
    `status`      TINYINT      NOT NULL DEFAULT 1        COMMENT '状态：1启用/0禁用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ============================================================
-- 2. 分类表 (category)
-- ============================================================
CREATE TABLE `category` (
    `category_id` INT          NOT NULL AUTO_INCREMENT  COMMENT '分类ID',
    `name`        VARCHAR(50)  NOT NULL                  COMMENT '分类名称（唯一）',
    `description` VARCHAR(255) DEFAULT NULL              COMMENT '分类描述',
    `sort_order`  INT          NOT NULL DEFAULT 0        COMMENT '排序序号',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`category_id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类表';

-- ============================================================
-- 3. 文章表 (article)
-- ============================================================
CREATE TABLE `article` (
    `article_id`  INT          NOT NULL AUTO_INCREMENT  COMMENT '文章ID',
    `title`       VARCHAR(200) NOT NULL                  COMMENT '文章标题',
    `content`     TEXT         NOT NULL                  COMMENT '文章正文',
    `summary`     VARCHAR(500) DEFAULT NULL              COMMENT '文章摘要',
    `cover_image` VARCHAR(255) DEFAULT NULL              COMMENT '封面图片URL',
    `user_id`     INT          NOT NULL                  COMMENT '作者ID（外键→user）',
    `category_id` INT          NOT NULL                  COMMENT '分类ID（外键→category）',
    `status`      TINYINT      NOT NULL DEFAULT 1        COMMENT '状态：1发布/0草稿',
    `view_count`  INT          NOT NULL DEFAULT 0        COMMENT '浏览次数',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`article_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_create_time` (`create_time`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- ============================================================
-- 4. 评论表 (comment)
-- ============================================================
CREATE TABLE `comment` (
    `comment_id`  INT          NOT NULL AUTO_INCREMENT  COMMENT '评论ID',
    `content`     VARCHAR(500) NOT NULL                  COMMENT '评论内容',
    `article_id`  INT          NOT NULL                  COMMENT '所属文章ID（外键→article）',
    `user_id`     INT          NOT NULL                  COMMENT '评论者ID（外键→user）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`comment_id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ============================================================
-- 5. 插入测试数据
-- ============================================================

-- 默认管理员（密码为 123456 的 SHA-256 加密值）
INSERT INTO `user` (`username`, `password`, `nickname`, `email`, `role`)
VALUES ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', '系统管理员', 'admin@blog.com', 'admin');

-- 默认分类
INSERT INTO `category` (`name`, `description`, `sort_order`) VALUES
('技术博客', '编程语言、框架、开发工具等技术相关文章', 1),
('生活随笔', '日常生活感悟与记录', 2),
('学习笔记', '课程学习过程中的笔记与总结', 3),
('项目分享', '项目开发经验与心得分享', 4);
