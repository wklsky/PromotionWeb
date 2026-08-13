/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/utils/business.ts
 * @Description: CMS 后台可单测的纯业务逻辑（见 docs/16 §4.4「测试代码业务逻辑」）。
 *               列表筛选 / 分页 / 状态映射等从各视图中抽取为无副作用纯函数，便于 Vitest 单测，
 *               同时被 NewsManage / JobsManage / MediaLibrary 复用，避免逻辑分散漂移。
 */
import type { NewsListVO, JobListVO, MediaVO } from 'taiji-shared';

/** 新闻筛选：按分类（'全部' 不过滤）+ 标题关键字（大小写不敏感） */
export function filterNews(list: NewsListVO[], keyword: string, category: string): NewsListVO[] {
  const kw = keyword.trim().toLowerCase();
  return list.filter((n) => {
    const okCat = category === '全部' || n.category === category;
    const okKw = !kw || n.title.toLowerCase().includes(kw);
    return okCat && okKw;
  });
}

/** 通用分页切片：页码/页大小非法时回退为最小值，避免 NaN 切片 */
export function paginate<T>(list: T[], page: number, size: number): T[] {
  const p = Math.max(1, Math.floor(page) || 1);
  const s = Math.max(1, Math.floor(size) || 1);
  return list.slice((p - 1) * s, p * s);
}

/** 总页数：至少 1 页（空列表也返回 1，供分页器渲染） */
export function totalPages(length: number, size: number): number {
  return Math.max(1, Math.ceil(length / Math.max(1, size)));
}

/** 新闻状态 → 文案与标签类型（1=已发布 success / 0=草稿 info） */
export function statusNewsLabel(status: number): { text: string; type: 'success' | 'info' } {
  return status === 1 ? { text: '已发布', type: 'success' } : { text: '草稿', type: 'info' };
}

/** 招聘筛选：岗位 / 部门 / 城市 任一包含关键字即命中（大小写不敏感） */
export function filterJobs(list: JobListVO[], keyword: string): JobListVO[] {
  const kw = keyword.trim().toLowerCase();
  if (!kw) return list;
  return list.filter(
    (j) =>
      j.position.toLowerCase().includes(kw) ||
      (j.department ?? '').toLowerCase().includes(kw) ||
      (j.city ?? '').toLowerCase().includes(kw),
  );
}

/** 招聘状态 → 文案与标签类型（1=招聘中 success / 0=已关闭 info） */
export function jobStatusLabel(status: number): { text: string; type: 'success' | 'info' } {
  return status === 1 ? { text: '招聘中', type: 'success' } : { text: '已关闭', type: 'info' };
}

/** 媒体筛选：按名称关键字（大小写不敏感） */
export function filterMedia(list: MediaVO[], keyword: string): MediaVO[] {
  const kw = keyword.trim().toLowerCase();
  if (!kw) return list;
  return list.filter((m) => m.name.toLowerCase().includes(kw));
}
