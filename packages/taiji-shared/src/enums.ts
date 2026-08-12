/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: packages/taiji-shared/src/enums.ts
 * @Description: 前后端共享枚举常量，取值与 docs/13-Swagger接口文档.md §9 完全一致，
 *               与 docs/12-数据库设计.md 表字段枚举保持一致。修改需同步文档。
 */

/** 新闻分类 */
export const NEWS_CATEGORIES = ['企业动态', '行业资讯', '技术文章'] as const;
export type NewsCategory = (typeof NEWS_CATEGORIES)[number];

/** 招聘类型 */
export const JOB_TYPES = ['全职', '实习'] as const;
export type JobType = (typeof JOB_TYPES)[number];

/** 通用状态：0禁用/关闭/草稿，1启用/开放/发布 */
export const CONTENT_STATUS = {
  DRAFT: 0,
  PUBLISHED: 1,
} as const;
export type ContentStatus = (typeof CONTENT_STATUS)[keyof typeof CONTENT_STATUS];

/** 招聘状态 */
export const JOB_STATUS = {
  CLOSED: 0,
  OPEN: 1,
} as const;
export type JobStatus = (typeof JOB_STATUS)[keyof typeof JOB_STATUS];

/** 留资类型 */
export const CONTACT_TYPES = ['contact', 'job'] as const;
export type ContactType = (typeof CONTACT_TYPES)[number];

/** 媒体类型 */
export const MEDIA_TYPES = ['image', 'video'] as const;
export type MediaType = (typeof MEDIA_TYPES)[number];

/** CMS 角色 */
export const ROLES = ['admin', 'editor'] as const;
export type Role = (typeof ROLES)[number];

/** 企业内容区块标识（section） */
export const CONTENT_SECTIONS = [
  'intro',
  'philosophy',
  'history',
  'honor',
  'culture',
  'dragon.biz',
  'dragon.core',
  'dragon.product',
  'dragon.case',
  'panda.biz',
  'panda.tech',
  'panda.ecology',
  'panda.case',
  'kunpeng.position',
  'kunpeng.future',
  'kunpeng.innovation',
  'kunpeng.coop',
] as const;
export type ContentSection = (typeof CONTENT_SECTIONS)[number];
