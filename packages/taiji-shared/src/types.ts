/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: packages/taiji-shared/src/types.ts
 * @Description: 业务实体 VO/DTO 类型定义，字段与 docs/12-数据库设计.md 表结构、
 *               docs/13-Swagger接口文档.md §2 逐字段一致。驼峰命名，对应表下划线字段。
 */

import type {
  ContentSection,
  NewsCategory,
  JobType,
  JobStatus,
  ContentStatus,
  ContactType,
  MediaType,
  Role,
} from './enums';

/** 企业内容 VO（读）— company_info 表 */
export interface CompanyInfoVO {
  id: number;
  section: ContentSection;
  title: string;
  content: string | null;
  cover: string | null;
  seoTitle: string | null;
  seoKeywords: string | null;
  seoDesc: string | null;
  sort: number;
  status: ContentStatus;
  updateTime: string;
}

/** 企业内容 DTO（写）— POST/PUT /content */
export interface CompanyInfoDTO {
  section: ContentSection;
  title: string;
  content?: string;
  cover?: string;
  seoTitle?: string;
  seoKeywords?: string;
  seoDesc?: string;
  sort?: number;
  status?: ContentStatus;
}

/** 新闻列表 VO — news 表 */
export interface NewsListVO {
  id: number;
  title: string;
  category: NewsCategory;
  cover: string | null;
  /** 摘要/简介，列表卡片展示用（对应 news.summary，可空） */
  summary: string | null;
  author: string | null;
  /** 发布状态：1=已发布 0=草稿（后端 News.status 直出，与 CONTENT_STATUS 一致） */
  status: number;
  publishTime: string | null;
  viewCount: number;
}

/** 新闻详情 VO（继承列表 + 内容） */
export interface NewsDetailVO extends NewsListVO {
  content: string;
  seoTitle: string | null;
  seoKeywords: string | null;
  seoDesc: string | null;
}

/** 新闻 DTO（写）— POST/PUT /news */
export interface NewsDTO {
  title: string;
  category: NewsCategory;
  content: string;
  cover?: string;
  author?: string;
  seoTitle?: string;
  seoKeywords?: string;
  seoDesc?: string;
  status?: ContentStatus;
  publishTime?: string;
}

/** 招聘列表 VO — job 表 */
export interface JobListVO {
  id: number;
  position: string;
  department: string | null;
  city: string | null;
  salary: string | null;
  type: JobType;
  status: JobStatus;
}

/** 招聘详情 VO */
export interface JobDetailVO extends JobListVO {
  description: string;
  requirement: string;
  publishTime: string | null;
}

/** 招聘 DTO（写）— POST/PUT /jobs */
export interface JobDTO {
  position: string;
  department?: string;
  city?: string;
  description: string;
  requirement: string;
  salary?: string;
  type: JobType;
  status?: JobStatus;
}

/** 留资 VO — contact_lead 表 */
export interface ContactLeadVO {
  id: number;
  name: string;
  phone: string;
  email: string | null;
  type: ContactType;
  refId: number | null;
  resumeUrl: string | null;
  message: string | null;
  createTime: string;
}

/** 联系/投递请求（POST /contact、POST /jobs/{id}/apply） */
export interface ContactLeadDTO {
  name: string;
  phone: string;
  email?: string;
  message?: string;
  resumeUrl?: string;
}

/** 媒体库 VO — media 表 */
export interface MediaVO {
  id: number;
  url: string;
  name: string;
  type: MediaType;
  size: number | null;
  createTime: string;
}

/** 登录返回 VO — admin_user/admin_role */
export interface LoginResultVO {
  token: string;
  role: Role;
  username: string;
}

/** 登录请求 */
export interface LoginDTO {
  username: string;
  password: string;
}
