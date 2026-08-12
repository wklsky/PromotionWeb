/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:33
 * @FilePath: apps/taiji-admin/src/api/request.ts
 * @Description: 后台统一 Axios 实例（见 docs/13 §1 响应契约）
 */
import axios, { type AxiosInstance, type InternalAxiosRequestConfig } from 'axios';
import type { ApiResponse } from 'taiji-shared';

const TOKEN_KEY = 'taiji_admin_token';

// 后端统一 Result<T> = { code, message, data }，与 shared 的 ApiResponse 字段一致。
// 响应拦截器不解包，交还完整 ApiResponse，由调用方取 .data（与 taiji-web 保持一致契约）
const instance: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

instance.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  // 登录后写入 localStorage 的 token 自动附加 Bearer，后台写操作需鉴权（见 docs/14 §4）
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) {
    config.headers.set('Authorization', `Bearer ${token}`);
  }
  return config;
});

instance.interceptors.response.use(
  (response) => response.data as ApiResponse<unknown>,
  (error) => {
    const msg = error?.response?.data?.message || error.message || '请求失败';
    if (error?.response?.status === 401) {
      // 401 表示 token 失效或缺失，清除本地凭证并跳回登录
      localStorage.removeItem(TOKEN_KEY);
      window.location.href = '/login';
    }
    console.error('[admin-request]', msg);
    return Promise.reject(new Error(msg));
  },
);

export { TOKEN_KEY };
export default instance;
