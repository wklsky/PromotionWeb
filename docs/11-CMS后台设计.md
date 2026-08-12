# 11 太极馆企业宣传网站 · CMS 后台设计

> 文档版本：V1.0
> 关联文档：01-项目立项书、02-产品需求PRD、12-数据库设计DDD、13-Swagger接口文档
> 说明：CMS 为内容运营后台，纯 CSR（Vue3 + Vite + Element Plus），无 SEO 诉求；数据通过 13-Swagger 接口与官网共享同一 MySQL。

---

## 1. 产品定位

CMS 服务于企业运营人员，对官网内容（企业信息、新闻、招聘）进行**可视化录入、编辑、发布与下架**，并管理媒体资源与后台账号权限，使官网具备自运营能力。

核心价值：一处编辑、官网实时可见；运营与研发解耦。

## 2. 技术架构

| 层 | 选型 |
| -- | ---- |
| 框架 | Vue3 + Vite + TypeScript |
| UI | Element Plus |
| 状态 | Pinia |
| 请求 | Axios（统一拦截器，注入 `Authorization`） |
| 路由 | Vue Router（按权限动态注册） |
| 表单校验 | 复用 `taiji-shared` 校验规则 |
| 富文本 | （建议）WangEditor / TipTap |
| 部署 | `vite build` → 静态资源（Docker + Nginx） |

与官网共享 `taiji-shared`（DTO 类型、枚举常量、校验），确保两侧数据契约一致。

## 3. 角色与权限（RBAC 简版）

| 角色 | 权限范围 |
| ---- | -------- |
| admin（管理员） | 全部：内容管理 + 媒体库 + 账号管理 |
| editor（编辑） | 内容管理（新闻/招聘/页面内容），不可管理账号 |

- 登录接口：`POST /api/v1/auth/login` → 返回 `token` + `role`；
- 路由守卫：未登录跳转 `/login`；无权限菜单不渲染；
- 写接口统一需 `Bearer` 鉴权（见 13 文档）。

## 4. 功能模块

```
CMS
├── 登录 /login
├── 仪表盘 /dashboard
├── 内容管理
│   ├── 企业内容 /content        (company_info: 关于/三大馆各 section)
│   ├── 新闻管理 /news           (news)
│   └── 招聘管理 /jobs            (job)
├── 媒体库 /media                (图片/视频上传至 MinIO 对象存储，经 CDN/Nginx 反代)
└── 系统设置
    ├── 管理员管理 /users        (admin only)
    └── 角色管理 /roles          (admin only)
```

### 4.1 企业内容管理（company_info）

| 能力 | 说明 |
| ---- | ---- |
| 区块列表 | 按 `section` 分组（intro/philosophy/history/dragon.*/panda.*/kunpeng.*） |
| 编辑 | 标题、富文本 `content`、封面 `cover`、排序 `sort` |
| SEO 字段 | `seoTitle/seoKeywords/seoDesc`（官网 `useSeoMeta` 直接读取） |
| 状态 | 草稿(0)/发布(1)，发布后官网可见 |

### 4.2 新闻管理（news）

| 能力 | 说明 |
| ---- | ---- |
| 列表 | 分页 + 分类筛选 + 状态筛选 + 关键词搜索 |
| 新建/编辑 | 标题、分类（企业动态/行业资讯/技术文章）、封面、作者、富文本、SEO 字段、状态 |
| 发布时间 | `publishTime`，控制官网展示时序 |
| 软删除 | 回收站式删除（调 `DELETE /news/{id}`） |

### 4.3 招聘管理（job）

| 能力 | 说明 |
| ---- | ---- |
| 列表 | 分页 + 状态过滤（仅开放岗位对官网可见） |
| 表单 | 职位、部门、城市、职责 `description`、要求 `requirement`、薪资、类型(全职/实习)、状态 |
| 投递查看 | 关联 `contact_lead`（`type='job'`）查看简历链接 |

### 4.4 媒体库（media）

- 上传图片/视频至 MinIO 对象存储（经 Nginx/CDN 反代暴露地址），返回 URL；
- 支持按类型/时间检索，供各表单 `cover` 字段选择；
- 建议限制：图片 ≤5MB、视频 ≤100MB，格式白名单。

### 4.5 系统设置

- 管理员管理：增删改账号、分配角色（admin only）；
- 角色管理：维护 `admin`/`editor`（admin only）。

## 5. 关键页面与交互

| 页面 | 核心组件 | 交互要点 |
| ---- | -------- | -------- |
| 登录 | ElForm + ElInput | 校验用户名/密码，失败提示，存 token 至 Pinia+localStorage |
| 内容/新闻/招聘列表 | ElTable + ElPagination + ElTag | 行内编辑/删除，批量上下架 |
| 编辑抽屉/弹窗 | ElForm + 富文本 + ElUpload | 封面选媒体库，SEO 折叠区 |
| 媒体库 | ElUpload + 瀑布流 | 直传 CDN，回填 URL（瀑布流布局复用官网 `MasonryGrid` 设计语言，见 05 §4.1） |
| 仪表盘 | ElCard + 简易统计 | 内容数/待审/今日投递概览 |

## 6. 数据流与契约

- 所有写操作调用 13 文档对应接口（content/news/jobs POST/PUT/DELETE）；
- 列表统一 `PageResult<T>`，前端 `ElPagination` 绑定 `total/page/size`；
- 枚举（分类/类型/状态）来自 `taiji-shared`，下拉选项与官网一致；
- 富文本存储 `LONGTEXT`，官网渲染需做 XSS 过滤（后端存储前或前端渲染前沙箱化）。

## 7. 安全与合规

- 登录密码 BCrypt 存储（见 12 表 `admin_user.password`）；
- 写接口限频 + 操作审计（建议记录操作日志）；
- 媒体上传校验类型/大小，防止恶意文件；
- 退出清理 token。

## 8. 验收标准

- admin 可完整录入并发布一条新闻/职位，官网对应页实时可见；
- editor 无法访问账号管理菜单；
- 媒体库上传的资源可在表单封面中选用；
- 软删除内容不在官网展示，且可恢复。

## 9. 与官网的对接清单

| CMS 动作 | 官网表现 |
| -------- | -------- |
| 发布新闻 | `/news` 列表新增，`/news/[id]` 可访问 |
| 修改企业内容 section | 对应馆/关于页内容更新 |
| 开放/关闭招聘 | 官网列表显隐 |
| 填写 SEO 字段 | 官网 `useSeoMeta` 注入对应页 |
