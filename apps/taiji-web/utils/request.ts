/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:32
 * @FilePath: apps/taiji-web/utils/request.ts
 * @Description: 前台统一 Axios 实例（见 docs/13 §1 响应契约）
 */
import axios, { type AxiosInstance, type InternalAxiosRequestConfig } from 'axios';
import type { ApiResponse } from 'taiji-shared';

// 后端统一 Result<T> = { code, message, data }，与 shared 的 ApiResponse 字段完全一致，
// 故响应拦截器不解包，直接把完整 ApiResponse 交还调用方，由页面取 .data（见 docs/13 §1）
const instance: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

instance.interceptors.request.use((config: InternalAxiosRequestConfig) => config);

instance.interceptors.response.use(
  (response) => response.data as ApiResponse<unknown>,
  (error) => {
    // 网络/HTTP 错误统一提示，不吞掉异常，交由调用方 catch 处理
    const msg = error?.response?.data?.message || error.message || '网络请求失败';
    console.error('[request]', msg);
    return Promise.reject(new Error(msg));
  },
);

export default instance;
