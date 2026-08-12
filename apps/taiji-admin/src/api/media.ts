/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:40
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:42
 * @FilePath: apps/taiji-admin/src/api/media.ts
 * @Description: 后台媒体库接口（见 docs/11 §5、docs/14 MinIO）
 */
import { http } from '~/api/request';
import type { PageResult, MediaVO } from 'taiji-shared';

// 媒体列表：GET /api/media 已放行只读（见 SecurityConfig）
export function listMedia(params: { page?: number; size?: number }) {
  return http.get<PageResult<MediaVO>>('/media', { params });
}
