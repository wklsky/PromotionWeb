/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:32
 * @FilePath: apps/taiji-web/api/content.ts
 * @Description: 前台内容接口（见 docs/13 §2、docs/14）
 */
import request from '~/utils/request';
import type { ApiResponse, PageResult, NewsListVO } from 'taiji-shared';

// 后端 NewsController.list 返回 Result<PageResult<News>>，News.status 为 number 直出，
// 与 shared 的 NewsListVO（已补 status:number）字段对齐（见 docs/13 §2.2）
export function fetchNews(params: { page?: number; size?: number }) {
  return request.get<ApiResponse<PageResult<NewsListVO>>>('/news', { params });
}
