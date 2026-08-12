/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:42
 * @FilePath: apps/taiji-web/utils/request.ts
 * @Description: 前台统一 Axios 实例与 http 封装（见 docs/13 §1 响应契约）
 */
import axios, {
  type AxiosInstance,
  type InternalAxiosRequestConfig,
  type AxiosResponse,
} from 'axios';
import type { ApiResponse } from 'taiji-shared';

// 后端统一 Result<T> = { code, message, data }，与 shared 的 ApiResponse 字段完全一致。
// 响应拦截器解包到 response.data（即完整 ApiResponse），由 http 方法泛型 <T> 标注 data 类型，
// 调用方直接 await http.get<X>('/news') 得到 ApiResponse<X>（见 docs/13 §1）
const instance: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

instance.interceptors.request.use((config: InternalAxiosRequestConfig) => config);

instance.interceptors.response.use(
  (response: AxiosResponse) => response.data,
  (error) => {
    // 网络/HTTP 错误统一提示，不吞掉异常，交由调用方 catch 处理
    const msg = error?.response?.data?.message || error.message || '网络请求失败';
    console.error('[request]', msg);
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

export default instance;
