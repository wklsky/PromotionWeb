# 13 太极馆企业宣传网站 · Swagger 接口文档

> 文档版本：V1.1（字段级完善）
> 关联文档：12-数据库设计DDD、02-产品需求PRD、11-CMS后台设计
> 说明：本文件为前后端联调的唯一事实源。实际以 Spring Boot 集成 `springdoc-openapi` 生成的 `/swagger-ui.html` 为准，代码中的 DTO/VO 必须与本文逐字段一致。
> 基础路径：`/api/v1`；鉴权：标注 *(需鉴权)* 的写操作需 `Authorization: Bearer <token>`。

---

## 1. 通用约定

- **响应包装**
  ```json
  { "code": 0, "message": "ok", "data": T }
  ```
  `code ≠ 0` 表示错误，`message` 为可读信息。
- **分页响应结构 `PageResult<T>`**
  ```json
  {
    "list": ["T"],
    "total": 100,
    "page": 1,
    "size": 10,
    "pages": 10
  }
  ```
- **分页请求参数**：`page`(默认1, ≥1)、`size`(默认10, 1~50)。
- **时间格式**：`yyyy-MM-dd HH:mm:ss`（后端 Jackson 配置）。
- **ID 类型**：均为 `number`(int64)。
- **空值**：未设置字段返回 `null`，不返回空字符串占位。

---

## 2. 数据模型（DTO / VO 字段定义）

> 以下类型应在 `taiji-shared` 中定义为 TS interface，与后端实体一一对应。

### 2.1 CompanyInfoVO（企业内容，官网读）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | 主键 |
| section | string | 区块标识：`intro`/`philosophy`/`history`/`honor`/`culture`/`dragon.biz`/`dragon.core`/`dragon.product`/`dragon.case`/`panda.*`/`kunpeng.*` |
| title | string | 标题 |
| content | string | 富文本 HTML |
| cover | string\|null | 封面 URL |
| seoTitle | string\|null | SEO 标题 |
| seoKeywords | string\|null | SEO 关键词 |
| seoDesc | string\|null | SEO 描述 |
| sort | number | 排序 |
| status | number | 0草稿/1发布 |
| updateTime | string | 更新时间 |

### 2.2 NewsListVO（新闻列表）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| title | string | — |
| category | string | 企业动态/行业资讯/技术文章 |
| cover | string\|null | 封面 |
| author | string\|null | 作者 |
| publishTime | string\|null | 发布时间 |
| viewCount | number | 浏览量 |

### 2.3 NewsDetailVO（新闻详情，继承列表 + 内容）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| title | string | — |
| category | string | — |
| content | string | 富文本 HTML |
| cover | string\|null | — |
| author | string\|null | — |
| publishTime | string\|null | — |
| viewCount | number | — |
| seoTitle | string\|null | — |
| seoKeywords | string\|null | — |
| seoDesc | string\|null | — |

### 2.4 JobListVO（招聘列表）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| position | string | 职位名 |
| department | string\|null | 部门 |
| city | string\|null | 城市 |
| salary | string\|null | 薪资范围 |
| type | string | 全职/实习 |
| status | number | 0关闭/1开放 |

### 2.5 JobDetailVO（招聘详情）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| position | string | — |
| department | string\|null | — |
| city | string\|null | — |
| description | string | 职责（富文本/纯文本） |
| requirement | string | 要求 |
| salary | string\|null | — |
| type | string | 全职/实习 |
| status | number | — |
| publishTime | string\|null | — |

### 2.6 ContactLeadVO（留资/投递）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| name | string | 姓名 |
| phone | string | 手机 |
| email | string\|null | 邮箱 |
| type | string | `contact`/`job` |
| refId | number\|null | 关联 job.id |
| resumeUrl | string\|null | 简历链接 |
| message | string\|null | 留言 |
| createTime | string | 提交时间 |

### 2.7 MediaVO（媒体库）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| id | number | — |
| url | string | CDN 地址 |
| name | string | 原始文件名 |
| type | string | `image`/`video` |
| size | number | 字节 |
| createTime | string | — |

### 2.8 LoginResultVO（登录返回）
| 字段 | 类型 | 说明 |
| ---- | ---- | ---- |
| token | string | JWT |
| role | string | `admin`/`editor` |
| username | string | — |

---

## 3. 企业内容 company_info

### GET /content
- query：`section`(可选, string)、`status`(默认1)
- 返回：`List<CompanyInfoVO>`

### GET /content/{section}
- 路径：`section` 见 2.1 枚举
- 返回：`CompanyInfoVO`（单区块，用于关于页/各馆）

### POST /content *(需鉴权)*
请求 `CompanyInfoDTO`：
| 字段 | 类型 | 约束 |
| ---- | ---- | ---- |
| section | string | 必填，枚举内 |
| title | string | 必填，≤128 |
| content | string | 可选，富文本 |
| cover | string\|null | URL |
| seoTitle | string\|null | ≤128 |
| seoKeywords | string\|null | ≤255 |
| seoDesc | string\|null | ≤512 |
| sort | number | 默认0 |
| status | number | 0/1，默认1 |

### PUT /content/{id} *(需鉴权)*
- body：`CompanyInfoDTO`（同 POST，全量更新）

### DELETE /content/{id} *(需鉴权)*
- 软删除，返回 `code:0`

---

## 4. 新闻 news

### GET /news
- query：`category`(可选)、`keyword`(可选, 标题/内容模糊)、`page`、`size`
- 返回：`PageResult<NewsListVO>`

### GET /news/{id}
- 副作用：浏览量 +1（Redis 计数或 DB 自增，防并发用乐观锁）
- 返回：`NewsDetailVO`

### POST /news *(需鉴权)*
请求 `NewsDTO`：
| 字段 | 类型 | 约束 |
| ---- | ---- | ---- |
| title | string | 必填 ≤128 |
| category | string | 必填，枚举 |
| content | string | 必填，富文本 |
| cover | string\|null | — |
| author | string\|null | ≤64 |
| seoTitle | string\|null | ≤128 |
| seoKeywords | string\|null | ≤255 |
| seoDesc | string\|null | ≤512 |
| status | number | 0/1，默认1 |
| publishTime | string\|null | 不传则服务端填当前时间 |

### PUT /news/{id} *(需鉴权)*
### DELETE /news/{id} *(需鉴权)*

---

## 5. 招聘 job

### GET /jobs
- query：`status`(默认1, 仅开放)、`page`、`size`
- 返回：`PageResult<JobListVO>`

### GET /jobs/{id}
- 返回：`JobDetailVO`

### POST /jobs *(需鉴权)*
请求 `JobDTO`：
| 字段 | 类型 | 约束 |
| ---- | ---- | ---- |
| position | string | 必填 ≤64 |
| department | string\|null | ≤64 |
| city | string\|null | ≤32 |
| description | string | 必填 |
| requirement | string | 必填 |
| salary | string\|null | ≤32 |
| type | string | 全职/实习 |
| status | number | 0/1，默认1 |

### PUT /jobs/{id} *(需鉴权)*
### DELETE /jobs/{id} *(需鉴权)*

---

## 6. 联系/投递 contact_lead

### POST /contact
请求：
| 字段 | 类型 | 约束 |
| ---- | ---- | ---- |
| name | string | 必填 ≤64 |
| phone | string | 必填，正则 `^1[3-9]\\d{9}$` |
| email | string\|null | 选填，邮箱格式 |
| message | string\|null | ≤1024 |
- 验证：必填 + 手机格式；限频（同 IP 60s 一次，Redis 计数）
- 返回：`ContactLeadVO`（含生成 id）

### POST /jobs/{id}/apply
请求：
| 字段 | 类型 | 约束 |
| ---- | ---- | ---- |
| name | string | 必填 |
| phone | string | 必填，手机格式 |
| email | string\|null | 选填 |
| resumeUrl | string\|null | 选填，URL |
- 写入 `contact_lead`（`type='job'`, `ref_id=id`）；限频同 contact

### GET /leads *(需鉴权, CMS)*
- query：`type`(可选 contact/job)、`page`、`size`
- 返回：`PageResult<ContactLeadVO>`（招聘详情页查看投递）

---

## 7. 媒体库 media *(需鉴权, CMS)*

### POST /media/upload
- 形式：`multipart/form-data`，字段 `file`
- 限制：图片 ≤5MB（image/png,jpeg,webp），视频 ≤100MB（video/mp4,webm）
- 流程：服务端直传 MinIO 对象存储（经 Nginx/CDN 反代暴露地址），返回 `MediaVO`
- 返回：`MediaVO`

### GET /media
- query：`type`(可选 image/video)、`page`、`size`
- 返回：`PageResult<MediaVO>`

### DELETE /media/{id} *(需鉴权)*

---

## 8. 鉴权 auth (CMS)

### POST /auth/login
请求：`{ username: string, password: string }`
返回：`LoginResultVO`（`token`, `role`, `username`）

### POST /auth/logout *(需鉴权)*
- 失效当前 token（服务端黑名单或短时效 JWT）

---

## 9. 枚举常量（taiji-shared 同步）

| 枚举 | 值 |
| ---- | -- |
| NewsCategory | `企业动态` / `行业资讯` / `技术文章` |
| JobType | `全职` / `实习` |
| JobStatus | `0关闭` / `1开放` |
| ContentStatus | `0草稿` / `1发布` |
| ContactType | `contact` / `job` |
| MediaType | `image` / `video` |
| Role | `admin` / `editor` |

---

## 10. 错误码

| code | 含义 | 处理建议 |
| ---- | ---- | -------- |
| 0 | 成功 | — |
| 400 | 参数校验失败 | 返回 field 级 message |
| 401 | 未登录/鉴权失败 | 跳转登录 |
| 403 | 无权限（如 editor 访问用户管理） | 提示无权限 |
| 404 | 资源不存在 | — |
| 429 | 请求过于频繁 | 稍后重试 |
| 500 | 服务错误 | 上报日志 |

---

## 11. 接口与前端调用映射（速查）

| 前端场景 | 接口 |
| -------- | ---- |
| 官网首页统计 | `GET /content?section=honor` 等 |
| 关于页 | `GET /content/{section}` 多次 |
| 三大馆 | `GET /content/{section}`（dragon.*/panda.*/kunpeng.*） |
| 新闻列表 | `GET /news` |
| 新闻详情 | `GET /news/{id}` |
| 招聘列表 | `GET /jobs` |
| 招聘详情 | `GET /jobs/{id}` |
| 联系提交 | `POST /contact` |
| 简历投递 | `POST /jobs/{id}/apply` |
| CMS 登录 | `POST /auth/login` |
| CMS 内容维护 | `/content` CRUD |
| CMS 媒体库 | `/media/upload` + `GET /media` |
| CMS 查看投递 | `GET /leads` |
