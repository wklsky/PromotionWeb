/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:40
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:42
 * @FilePath: apps/taiji-admin/src/api/news.ts
 * @Description: 后台新闻接口（CMS 写操作，需 JWT，见 docs/13 §2.3、docs/14）
 */
import { http } from '~/api/request';
import type { PageResult, NewsListVO, NewsDTO } from 'taiji-shared';

// 读取新闻列表：后端 GET /api/news 已放行（只读），但 CMS 场景统一带 token（见 SecurityConfig）
export function listNews(params: { page?: number; size?: number }) {
  return http.get<PageResult<NewsListVO>>('/news', { params });
}

// 新增新闻：POST /api/news，需认证（见 docs/13 §2.3）
export function createNews(payload: NewsDTO) {
  return http.post<number>('/news', payload);
}

// 更新新闻：PUT /api/news/{id}，需认证
export function updateNews(id: number, payload: NewsDTO) {
  return http.put<boolean>(`/news/${id}`, payload);
}

// 删除新闻（逻辑删除）：DELETE /api/news/{id}，需认证
export function deleteNews(id: number) {
  return http.delete<boolean>(`/news/${id}`);
}
