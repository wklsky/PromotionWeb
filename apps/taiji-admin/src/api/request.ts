/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:42
 * @FilePath: apps/taiji-admin/src/api/request.ts
 * @Description: 后台统一 Axios 实例与 http 封装（见 docs/13 §1 响应契约、docs/14 §4 鉴权）
 */
import axios, {
  type AxiosInstance,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
} from 'axios';
import type { ApiResponse } from 'taiji-shared';

const TOKEN_KEY = 'taiji_admin_token';

// 后端统一 Result<T> = { code, message, data }，与 shared 的 ApiResponse 字段一致。
// 响应拦截器解包到 response.data（完整 ApiResponse），http 方法泛型 <T> 标注 data 类型（见 docs/13 §1）
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
  (response: AxiosResponse) => response.data,
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

// http 封装：响应拦截器已将 AxiosResponse 解包为完整 ApiResponse，故此处直接取回 Axios 结果（即 ApiResponse 对象），
// 不再二次读取 .data，否则会出现“双重解包”——调用方拿到的是 ApiResponse.data 而非 ApiResponse，
// 进而在 res.code / res.data 上读到 undefined 甚至 null 抛错。泛型 T 标注 ApiResponse.data 的实体类型。
export const http = {
  get: <T>(url: string, config?: Record<string, unknown>) =>
    instance.get<ApiResponse<T>>(url, config).then((r) => r as unknown as ApiResponse<T>),
  post: <T>(url: string, data?: unknown, config?: Record<string, unknown>) =>
    instance.post<ApiResponse<T>>(url, data, config).then((r) => r as unknown as ApiResponse<T>),
  put: <T>(url: string, data?: unknown, config?: Record<string, unknown>) =>
    instance.put<ApiResponse<T>>(url, data, config).then((r) => r as unknown as ApiResponse<T>),
  delete: <T>(url: string, config?: Record<string, unknown>) =>
    instance.delete<ApiResponse<T>>(url, config).then((r) => r as unknown as ApiResponse<T>),
};

export { TOKEN_KEY };
export default instance;
