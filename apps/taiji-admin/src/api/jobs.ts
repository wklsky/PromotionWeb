/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/api/jobs.ts
 * @Description: 后台招聘接口（CMS 写操作，需 JWT，见 docs/13 §7、docs/14）。
 */
import { http } from '~/api/request';
import type { PageResult, JobListVO, JobDTO, JobDetailVO } from 'taiji-shared';

// 招聘列表
export function listJobs(params: { page?: number; size?: number }) {
  return http.get<PageResult<JobListVO>>('/jobs', { params });
}

// 招聘详情：GET /api/jobs/{id}，编辑时回填职责/要求
export function getJob(id: number) {
  return http.get<JobDetailVO>(`/jobs/${id}`);
}

// 新增岗位：POST /api/jobs，需认证
export function createJob(payload: JobDTO) {
  return http.post<number>('/jobs', payload);
}

// 更新岗位：PUT /api/jobs/{id}，需认证
export function updateJob(id: number, payload: JobDTO) {
  return http.put<boolean>(`/jobs/${id}`, payload);
}

// 删除岗位：DELETE /api/jobs/{id}，需认证
export function deleteJob(id: number) {
  return http.delete<boolean>(`/jobs/${id}`);
}
