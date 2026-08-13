/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:42
 * @FilePath: apps/taiji-web/api/content.ts
 * @Description: 前台内容接口（见 docs/13 §2、docs/14）
 */
import { http } from '~/utils/request';
import type { PageResult, NewsListVO, CompanyInfoVO, JobListVO } from 'taiji-shared';

// 后端 NewsController.list 返回 Result<PageResult<News>>，News.status 为 number 直出，
// 与 shared 的 NewsListVO（已补 status:number）字段对齐（见 docs/13 §2.2）
export function fetchNews(params: { page?: number; size?: number }) {
  return http.get<PageResult<NewsListVO>>('/news', { params });
}

// 后端 ContentController.bySection 返回 Result<List<CompanyInfo>>，按 section 取已启用介绍（见 docs/13 §2.1）
export function fetchContent(section: string) {
  return http.get<CompanyInfoVO[]>(`/content/${section}`);
}

// 后端 JobController.list 返回 Result<PageResult<Job>>，列表字段与 JobListVO 对齐（见 docs/13 §7）
export function fetchJobs(params: { page?: number; size?: number }) {
  return http.get<PageResult<JobListVO>>('/jobs', { params });
}
