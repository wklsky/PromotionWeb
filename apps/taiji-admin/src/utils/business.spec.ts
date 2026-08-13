/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/utils/business.spec.ts
 * @Description: 纯业务逻辑单元测试（见 docs/16 §4.4）。覆盖筛选/分页/状态映射。
 */
import { describe, it, expect } from 'vitest';
import {
  filterNews,
  paginate,
  totalPages,
  statusNewsLabel,
  filterJobs,
  jobStatusLabel,
  filterMedia,
} from '~/utils/business';
import type { NewsListVO, JobListVO, MediaVO } from 'taiji-shared';

const news: NewsListVO[] = [
  { id: 1, title: '太极馆开馆盛典', category: '企业动态', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
  { id: 2, title: '行业趋势观察', category: '行业资讯', cover: null, summary: null, author: null, status: 0, publishTime: null, viewCount: 0 },
  { id: 3, title: '前端性能优化实践', category: '技术文章', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
  { id: 4, title: '企业发展动态回顾', category: '企业动态', cover: null, summary: null, author: null, status: 1, publishTime: null, viewCount: 0 },
];

describe('filterNews', () => {
  it('空关键字返回全部', () => {
    expect(filterNews(news, '', '全部')).toHaveLength(4);
  });
  it('按分类筛选', () => {
    const r = filterNews(news, '', '企业动态');
    expect(r).toHaveLength(2);
    expect(r.every((n) => n.category === '企业动态')).toBe(true);
  });
  it('按标题关键字(大小写不敏感)筛选', () => {
    expect(filterNews(news, 'TAIJI', '全部')).toHaveLength(0);
    expect(filterNews(news, '动态', '全部')).toHaveLength(1);
  });
  it('分类与关键字组合且为 AND', () => {
    const r = filterNews(news, '动态', '企业动态');
    expect(r.map((n) => n.id)).toEqual([4]);
  });
});

describe('paginate', () => {
  const arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  it('第 1 页切片', () => {
    expect(paginate(arr, 1, 3)).toEqual([1, 2, 3]);
  });
  it('第 2 页切片', () => {
    expect(paginate(arr, 2, 3)).toEqual([4, 5, 6]);
  });
  it('末页不足一页返回剩余', () => {
    expect(paginate(arr, 4, 3)).toEqual([10]);
  });
  it('非法页码/页大小回退最小值', () => {
    expect(paginate(arr, 0, 3)).toEqual([1, 2, 3]);
    expect(paginate(arr, 1, 0)).toEqual([1]);
  });
});

describe('totalPages', () => {
  it('空列表至少返回 1 页', () => {
    expect(totalPages(0, 10)).toBe(1);
  });
  it('向上取整', () => {
    expect(totalPages(11, 10)).toBe(2);
    expect(totalPages(10, 10)).toBe(1);
  });
  it('页大小非法回退', () => {
    expect(totalPages(10, 0)).toBe(10);
  });
});

describe('statusNewsLabel', () => {
  it('1 → 已发布/success', () => {
    expect(statusNewsLabel(1)).toEqual({ text: '已发布', type: 'success' });
  });
  it('0 → 草稿/info', () => {
    expect(statusNewsLabel(0)).toEqual({ text: '草稿', type: 'info' });
  });
});

const jobs: JobListVO[] = [
  { id: 1, position: '前端工程师', department: '研发中心', city: '上海', salary: '20K', type: '全职', status: 1 },
  { id: 2, position: '后端工程师', department: '研发中心', city: '北京', salary: '22K', type: '全职', status: 0 },
  { id: 3, position: '设计实习生', department: '品牌部', city: '上海', salary: '3K', type: '实习', status: 1 },
];

describe('filterJobs', () => {
  it('空关键字返回全部', () => {
    expect(filterJobs(jobs, '')).toHaveLength(3);
  });
  it('匹配岗位名', () => {
    expect(filterJobs(jobs, '前端').map((j) => j.id)).toEqual([1]);
  });
  it('匹配部门/城市', () => {
    expect(filterJobs(jobs, '品牌').map((j) => j.id)).toEqual([3]);
    expect(filterJobs(jobs, '上海').map((j) => j.id)).toEqual([1, 3]);
  });
});

describe('jobStatusLabel', () => {
  it('1 → 招聘中/success', () => {
    expect(jobStatusLabel(1)).toEqual({ text: '招聘中', type: 'success' });
  });
  it('0 → 已关闭/info', () => {
    expect(jobStatusLabel(0)).toEqual({ text: '已关闭', type: 'info' });
  });
});

const medias: MediaVO[] = [
  { id: 1, url: 'http://x/a.png', name: '封面图', type: 'image', size: 100, createTime: '' },
  { id: 2, url: 'http://x/b.mp4', name: '宣传片', type: 'video', size: 200, createTime: '' },
];

describe('filterMedia', () => {
  it('空关键字返回全部', () => {
    expect(filterMedia(medias, '')).toHaveLength(2);
  });
  it('按名称筛选', () => {
    expect(filterMedia(medias, '封面').map((m) => m.id)).toEqual([1]);
    expect(filterMedia(medias, '片').map((m) => m.id)).toEqual([2]);
  });
});
