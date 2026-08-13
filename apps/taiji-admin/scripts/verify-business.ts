/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/scripts/verify-business.ts
 * @Description: 业务逻辑零依赖验证（见 docs/16 §4.4）。用 Node22 内置 node:test + 类型剥离直接跑真实
 *               src/utils/business.ts 的断言，无需安装 vitest（当前沙箱 pnpm install 被 safe-delete 拦截）。
 *               运行：node --test --experimental-strip-types scripts/verify-business.ts
 */
import { test } from 'node:test';
import assert from 'node:assert/strict';
import {
  filterNews,
  paginate,
  totalPages,
  statusNewsLabel,
  filterJobs,
  jobStatusLabel,
  filterMedia,
} from '../src/utils/business.ts';
import type { NewsListVO, JobListVO, MediaVO } from 'taiji-shared';

const news: NewsListVO[] = [
  { id: 1, title: '太极馆开馆盛典', category: '企业动态', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
  { id: 2, title: '行业趋势观察', category: '行业资讯', cover: null, summary: null, author: null, status: 0, publishTime: null, viewCount: 0 },
  { id: 3, title: '前端性能优化实践', category: '技术文章', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
  { id: 4, title: '企业发展动态回顾', category: '企业动态', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
];

test('filterNews: 空关键字返回全部', () => {
  assert.equal(filterNews(news, '', '全部').length, 4);
});
test('filterNews: 按分类筛选', () => {
  const r = filterNews(news, '', '企业动态');
  assert.equal(r.length, 2);
  assert.ok(r.every((n) => n.category === '企业动态'));
});
test('filterNews: 标题关键字大小写不敏感', () => {
  assert.equal(filterNews(news, '动态', '全部').length, 1);
  assert.equal(filterNews(news, 'TAIJI', '全部').length, 0);
});
test('filterNews: 分类+关键字 AND', () => {
  assert.deepEqual(filterNews(news, '动态', '企业动态').map((n) => n.id), [4]);
});

test('paginate: 第1页/第2页切片', () => {
  const arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  assert.deepEqual(paginate(arr, 1, 3), [1, 2, 3]);
  assert.deepEqual(paginate(arr, 2, 3), [4, 5, 6]);
  assert.deepEqual(paginate(arr, 4, 3), [10]);
});
test('paginate: 非法页码/大小回退最小值', () => {
  const arr = [1, 2, 3];
  assert.deepEqual(paginate(arr, 0, 3), [1, 2, 3]);
  assert.deepEqual(paginate(arr, 1, 0), [1]);
});

test('totalPages: 空列表至少1页/向上取整/非法回退', () => {
  assert.equal(totalPages(0, 10), 1);
  assert.equal(totalPages(11, 10), 2);
  assert.equal(totalPages(10, 0), 10);
});

test('statusNewsLabel: 1→已发布 0→草稿', () => {
  assert.deepEqual(statusNewsLabel(1), { text: '已发布', type: 'success' });
  assert.deepEqual(statusNewsLabel(0), { text: '草稿', type: 'info' });
});

const jobs: JobListVO[] = [
  { id: 1, position: '前端工程师', department: '研发中心', city: '上海', salary: '20K', type: '全职', status: 1 },
  { id: 2, position: '后端工程师', department: '研发中心', city: '北京', salary: '22K', type: '全职', status: 0 },
  { id: 3, position: '设计实习生', department: '品牌部', city: '上海', salary: '3K', type: '实习', status: 1 },
];
test('filterJobs: 空关键字全返回/匹配岗位/部门/城市', () => {
  assert.equal(filterJobs(jobs, '').length, 3);
  assert.deepEqual(filterJobs(jobs, '前端').map((j) => j.id), [1]);
  assert.deepEqual(filterJobs(jobs, '品牌').map((j) => j.id), [3]);
  assert.deepEqual(filterJobs(jobs, '上海').map((j) => j.id), [1, 3]);
});
test('jobStatusLabel: 1→招聘中 0→已关闭', () => {
  assert.deepEqual(jobStatusLabel(1), { text: '招聘中', type: 'success' });
  assert.deepEqual(jobStatusLabel(0), { text: '已关闭', type: 'info' });
});

const medias: MediaVO[] = [
  { id: 1, url: 'http://x/a.png', name: '封面图', type: 'image', size: 100, createTime: '' },
  { id: 2, url: 'http://x/b.mp4', name: '宣传片', type: 'video', size: 200, createTime: '' },
];
test('filterMedia: 按名称筛选', () => {
  assert.equal(filterMedia(medias, '').length, 2);
  assert.deepEqual(filterMedia(medias, '封面').map((m) => m.id), [1]);
  assert.deepEqual(filterMedia(medias, '片').map((m) => m.id), [2]);
});
