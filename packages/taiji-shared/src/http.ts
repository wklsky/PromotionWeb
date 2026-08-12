/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: packages/taiji-shared/src/http.ts
 * @Description: 统一 HTTP 响应与分页结构类型，对应 docs/13-Swagger接口文档.md §1。
 */

/** 统一响应包装 */
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

/** 分页结果结构（与 13 §1 PageResult 一致） */
export interface PageResult<T> {
  list: T[];
  total: number;
  page: number;
  size: number;
  pages: number;
}

/** 分页请求参数 */
export interface PageQuery {
  page?: number;
  size?: number;
}
