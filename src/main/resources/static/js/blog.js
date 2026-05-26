// ====== 博客管理系统 通用 JavaScript ======

// API 基础路径
const API_BASE = '/api';

// 获取存储的 Token
function getToken() {
    return localStorage.getItem('blog_token');
}

// 存储 Token
function setToken(token) {
    localStorage.setItem('blog_token', token);
}

// 清除 Token
function clearToken() {
    localStorage.removeItem('blog_token');
    localStorage.removeItem('blog_user');
}

// 获取认证请求头
function authHeader() {
    const token = getToken();
    return token ? { 'Authorization': 'Bearer ' + token } : {};
}

// 检查是否已登录
function isLoggedIn() {
    return getToken() !== null;
}

// 通用 AJAX 请求
async function apiRequest(url, method = 'GET', data = null) {
    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            ...authHeader()
        }
    };
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    try {
        const response = await fetch(url, options);
        const result = await response.json();
        if (!response.ok && result.code === 401) {
            // Token 过期，跳转到登录页
            clearToken();
            window.location.href = '/api/users/login-page';
            return null;
        }
        return result;
    } catch (error) {
        console.error('API 请求失败:', error);
        return { code: 500, message: '网络请求失败', data: null };
    }
}

// 显示提示消息
function showToast(message, type = 'success') {
    const container = document.getElementById('toast-container');
    if (!container) return;

    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-bg-${type} border-0 show`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    container.appendChild(toast);

    // 3 秒后自动移除
    setTimeout(() => {
        toast.remove();
    }, 3000);
}

// 显示确认对话框
function showConfirm(message) {
    return new Promise((resolve) => {
        const modal = new bootstrap.Modal(document.getElementById('confirmModal'));
        document.getElementById('confirmModalBody').textContent = message;
        document.getElementById('confirmModalBtn').onclick = function() {
            modal.hide();
            resolve(true);
        };
        modal.show();
    });
}

// 获取当前用户信息
async function fetchCurrentUser() {
    const result = await apiRequest(`${API_BASE}/users/me`);
    if (result && result.code === 200) {
        localStorage.setItem('blog_user', JSON.stringify(result.data));
        return result.data;
    }
    return null;
}

// 更新导航栏登录状态
function updateNavbar() {
    const userStr = localStorage.getItem('blog_user');
    const loginNav = document.getElementById('login-nav');
    const userNav = document.getElementById('user-nav');

    if (!loginNav || !userNav) return;

    if (userStr) {
        const user = JSON.parse(userStr);
        loginNav.style.display = 'none';
        userNav.style.display = 'block';
        document.getElementById('user-nickname').textContent = user.nickname || user.username;
    } else {
        loginNav.style.display = 'block';
        userNav.style.display = 'none';
    }
}

// 页面加载时更新导航栏
document.addEventListener('DOMContentLoaded', function() {
    updateNavbar();
});
