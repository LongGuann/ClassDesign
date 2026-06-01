# 📝 墨言

> **课程名称：** 网络接口编程  
> **技术栈：** Spring Boot + Spring MVC + MyBatis + MySQL + Bootstrap  
> **开发语言：** Java 17  
> **分支：** `InterNetAPI`

---

## 📋 项目简介

基于 Spring Boot 3 构建的轻量级墨言，实现了用户认证、文章发布与管理、评论互动、分类管理等核心功能。采用 RESTful API 设计与前后端分离架构，支持 JWT Token 认证、分页查询、模糊搜索等特性。

---

## 🚀 快速开始

### 环境要求

| 工具 | 版本要求 |
|------|---------|
| JDK | 17+ |
| Maven | 3.9+ |
| MySQL | 8.0+ |

### 安装步骤

```bash
# 1. 克隆项目
git clone git@github.com:LongGuann/ClassDesign.git
cd ClassDesign
git checkout InterNetAPI

# 2. 创建数据库
mysql -u root -p < sql/init.sql

# 3. 修改数据库密码（如需要）
# 编辑 src/main/resources/application.yml，将 password 改为你的 MySQL 密码

# 4. 启动项目
mvn spring-boot:run
```

访问 http://localhost:8080/api/articles/index

### 演示账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | `admin` | `123456` |
| 普通用户 | 注册新账号 | — |

---

## 🏗️ 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | JDK 17 | 开发语言 |
| Spring Boot | 3.2.0 | 应用框架 |
| Spring MVC | 6.1.1 | RESTful API |
| MyBatis | 3.0.3 | 数据持久化 |
| MySQL | 8.0 | 数据库 |
| Maven | 3.9+ | 构建工具 |
| JJWT | 0.12.3 | JWT Token 认证 |
| PageHelper | 2.1.0 | 分页插件 |

### 前端

| 技术 | 用途 |
|------|------|
| HTML5 | 页面结构 |
| CSS3 + Bootstrap 5.3 | 响应式UI |
| JavaScript (ES6) | 交互逻辑 |
| Thymeleaf | 服务端模板引擎 |

---

## 📂 项目结构

```
blog-system/
├── pom.xml                              # Maven 依赖配置
├── sql/init.sql                         # 数据库建表脚本（含测试数据）
├── 设计文档.html / 设计文档.pdf          # 系统设计文档
├── 测试表单.md                           # 功能测试用例表
│
└── src/main/
    ├── java/com/blog/
    │   ├── BlogApplication.java         # Spring Boot 启动类
    │   ├── config/
    │   │   └── WebMvcConfig.java        # MVC 配置 + 拦截器注册
    │   ├── controller/
    │   │   ├── UserController.java      # 用户认证接口
    │   │   ├── ArticleController.java   # 文章 CRUD 接口
    │   │   ├── CommentController.java   # 评论管理接口
    │   │   └── CategoryController.java  # 分类管理接口
    │   ├── service/
    │   │   ├── UserService.java         # 用户业务接口
    │   │   ├── ArticleService.java      # 文章业务接口
    │   │   ├── CommentService.java      # 评论业务接口
    │   │   ├── CategoryService.java     # 分类业务接口
    │   │   └── impl/                    # 接口实现类
    │   ├── mapper/
    │   │   ├── UserMapper.java          # 用户数据访问
    │   │   ├── ArticleMapper.java       # 文章数据访问
    │   │   ├── CommentMapper.java       # 评论数据访问
    │   │   └── CategoryMapper.java      # 分类数据访问
    │   ├── entity/
    │   │   ├── User.java                # 用户实体
    │   │   ├── Article.java             # 文章实体
    │   │   ├── Comment.java             # 评论实体
    │   │   └── Category.java            # 分类实体
    │   ├── dto/                         # 数据传输对象
    │   ├── interceptor/
    │   │   └── LoginInterceptor.java    # JWT 登录拦截器
    │   ├── handler/
    │   │   └── GlobalExceptionHandler.java # 全局异常处理
    │   └── util/
    │       ├── JwtUtil.java             # JWT 工具类
    │       └── Result.java              # 统一响应封装
    │
    └── resources/
        ├── application.yml              # 应用配置
        ├── mapper/                      # MyBatis XML 映射文件
        ├── static/
        │   ├── css/blog.css             # 全局样式
        │   └── js/blog.js               # 通用脚本
        └── templates/                   # Thymeleaf 页面
            ├── index.html               # 首页（文章列表）
            ├── login.html               # 登录页
            ├── register.html            # 注册页
            ├── article-detail.html      # 文章详情
            ├── article-create.html      # 发布文章
            ├── article-edit.html        # 编辑文章
            ├── article-manage.html      # 我的文章
            ├── category-manage.html     # 分类管理
            ├── profile.html             # 个人信息
            └── error/404.html           # 404 页面
```

---

## 📡 RESTful API 接口

### 用户模块

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|:----:|
| POST | `/api/users/login` | 用户登录 | 公开 |
| POST | `/api/users/register` | 用户注册 | 公开 |
| GET | `/api/users/me` | 获取当前用户信息 | 登录 |
| PUT | `/api/users/profile` | 更新个人信息 | 登录 |

### 文章模块

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|:----:|
| GET | `/api/articles/list` | 文章列表（分页/分类/搜索） | 公开 |
| GET | `/api/articles/detail/{id}` | 文章详情 | 公开 |
| POST | `/api/articles` | 发布文章 | 登录 |
| PUT | `/api/articles/{id}` | 更新文章 | 登录 |
| DELETE | `/api/articles/{id}` | 删除文章 | 登录 |

### 评论模块

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|:----:|
| GET | `/api/comments/list/{articleId}` | 评论列表 | 公开 |
| POST | `/api/comments` | 发表评论 | 登录 |
| DELETE | `/api/comments/{id}` | 删除评论 | 登录 |

### 分类模块

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|:----:|
| GET | `/api/categories/list` | 分类列表 | 公开 |
| POST | `/api/categories` | 创建分类 | 管理员 |
| PUT | `/api/categories/{id}` | 更新分类 | 管理员 |
| DELETE | `/api/categories/{id}` | 删除分类 | 管理员 |

---

## 🗄️ 数据库设计

4 张关联表：`user` · `article` · `category` · `comment`

```
User (1) ────→ (N) Article
User (1) ────→ (N) Comment
Category (1) ────→ (N) Article
Article (1) ────→ (N) Comment
```

详见 [设计文档.pdf](https://github.com/LongGuann/ClassDesign/blob/InterNetAPI/%E8%AE%BE%E8%AE%A1%E6%96%87%E6%A1%A3.pdf) 第三章。

---

## ✅ 功能清单

| 功能类别 | 功能 | 状态 |
|---------|------|:----:|
| 🔐 用户 | 注册、登录、退出、个人信息管理 | ✅ |
| 📄 文章 | 发布、编辑、删除、分页列表、详情 | ✅ |
| 🔍 搜索 | 模糊搜索（标题/内容） | ✅ |
| 🏷️ 分类 | 分类筛选、管理（管理员） | ✅ |
| 💬 评论 | 发表、查看、删除 | ✅ |
| 🔒 安全 | JWT 认证、拦截器、全局异常处理 | ✅ |
| 📄 文档 | 系统设计文档（PDF） | ✅ |

---

## 🧪 测试

测试表单见 [测试表单.md](https://github.com/LongGuann/ClassDesign/blob/InterNetAPI/%E6%B5%8B%E8%AF%95%E8%A1%A8%E5%8D%95.md)，共 35 项测试用例覆盖所有核心功能。

---

## 📜 许可

本项目为 **百色学院《网络接口编程》课程设计作品**，仅用于教学目的。
