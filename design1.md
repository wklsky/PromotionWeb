# 《太极馆企业宣传网站设计方案 V1.0》（总览索引）

> 本文档为项目总览。详细内容已拆分为以下必需文件，以 `docs/` 下编号文档为权威来源，本文仅作导航与摘要，避免两处维护。

## 文档导航（必需文件）

| 文件 | 内容 |
| ---- | ---- |
| [01-项目立项书](docs/01-项目立项书.md) | 目标/范围/干系人/里程碑/风险 |
| [02-产品需求PRD](docs/02-产品需求PRD.md) | 功能清单/用户角色/验收口径 |
| [03-网站信息架构IA](docs/03-网站信息架构IA.md) | 信息架构/路由表/响应式断点 |
| [05-UI视觉设计规范](docs/05-UI视觉设计规范.md) | 色彩/字体/间距/动效 token |
| [07-前端技术架构设计](docs/07-前端技术架构设计.md) | 技术选型/目录/双前端契约 |
| [09-组件设计文档](docs/09-组件设计文档.md) | 组件分层/接口/组合式函数 |
| [11-CMS后台设计](docs/11-CMS后台设计.md) | 运营后台/权限/媒体库/对接 |
| [12-数据库设计](docs/12-数据库设计.md) | 表结构/索引/前后端契约 |
| [db/init.sql](db/init.sql) | 建表+初始化数据（MySQL 8.0+） |
| [13-Swagger接口文档](docs/13-Swagger接口文档.md) | 接口契约/枚举/错误码 |
| [14-后端开发设计书](docs/14-后端开发设计书.md) | Spring Boot3/MP/Security/JWT/Redis/MinIO |

## 设计要点摘要（详见上述文件）

- **定位**：融合东方美学、现代科技、企业实力的沉浸式品牌官网，含 CMS 自运营。
- **技术**：官网 Nuxt3(SSR)+Vue3+TS+Three.js+GSAP+Tailwind；CMS Vue3+Element Plus；后端 Spring Boot3+MyBatis Plus+MySQL+Redis；共享 `taiji-shared`。
- **架构**：首页 Hero(3D太极球)→品牌介绍→三大馆入口；三大馆（龙虎/熊猫/鲲鹏）四段式叙事，主题色按路由切换。
- **计划**：10 周 5 里程碑（M1设计冻结→M2基建→M3官网→M4 CMS→M5上线）。
- **指标**：LCP<2.5s / FID<100ms / CLS<0.1；全站 SEO（meta/JSON-LD/sitemap）。

---

> 以下为原始设计正文残留内容（已拆分，建议以 docs/ 为准）：

---

# 1. 项目概述

## 1.1 项目名称

**太极馆企业品牌宣传官方网站**

英文名称：**TAIJI HALL Official Website**

## 1.2 项目定位

太极馆是一家以东方文化、科技创新、产业生态为核心的综合型企业品牌。

官方网站定位：

> 打造一个融合东方美学、现代科技、企业实力展示的沉浸式品牌官网。

网站同时承担：品牌展示中心、产品业务入口、企业文化传播平台、商务合作窗口、用户体验展示平台。

---

# 2. 网站整体定位分析

## 2.1 用户群体

| 用户     | 访问目的               |
| -------- | ---------------------- |
| 企业客户 | 了解业务能力、寻找合作机会 |
| 投资机构 | 了解企业实力           |
| 合作伙伴 | 获取合作信息           |
| 普通用户 | 了解品牌文化           |
| 招聘用户 | 了解企业环境           |

## 2.2 网站设计关键词

整体视觉：东方、科技、力量、生态、未来、沉浸。

设计风格：新东方科技风、黑金高级感、3D沉浸体验、动态视觉效果、极简商业设计。

参考方向：苹果官网产品展示、华为企业官网、特斯拉品牌官网、小米生态官网。

---

# 3. 网站整体架构设计

## 3.1 网站信息架构

```
太极馆官网
├── 首页 Home
├── 关于太极馆（企业介绍 / 企业文化 / 发展历程 / 企业荣誉）
├── 龙虎馆（业务介绍 / 核心能力 / 产品服务 / 应用案例）
├── 熊猫馆（业务介绍 / 技术体系 / 产品生态 / 合作案例）
├── 鲲鹏馆（战略定位 / 未来生态 / 创新能力 / 产业合作）
├── 新闻中心
├── 招聘中心
└── 联系我们
```

---

# 4. 首页设计方案

## 4.1 首页目标

承担第一印象建立、品牌价值传递、三大业务入口导流。

## 4.2 首页分屏布局

- **第一屏 Hero 区**：全屏视频 / WebGL 3D动画。内容："太极馆 / 融合东方智慧 探索未来科技 / ENTER THE FUTURE"。交互：鼠标移动视差、粒子动画、水墨流动、3D太极球旋转。技术：Three.js / GSAP / WebGL / Shader。
- **第二屏 品牌介绍**："关于太极馆 / 以东方哲学 构建未来产业生态"。滚动触发：文字渐入、图片浮现、数字增长。
- **第三屏 三大业务馆**：太极馆 → 龙虎馆 / 熊猫馆 / 鲲鹏馆，三卡片设计，点击进入对应馆。

## 4.3 三大业务馆差异化设计

| 馆     | 定位           | 视觉            | 关键词                 | 标志性视觉元素           |
| ------ | -------------- | --------------- | ---------------------- | ------------------------ |
| 龙虎馆 | 力量型业务中心 | 红黑色系        | 能量/速度/工业/技术    | 火焰粒子、金属质感、动态光效 |
| 熊猫馆 | 生态与服务中心 | 黑白 + 青绿色   | 生态/智慧/用户/服务    | 偏互联网科技风           |
| 鲲鹏馆 | 未来战略创新中心 | 蓝紫科技风    | AI/云/大数据/未来      | 星空背景、飞行轨迹、3D模型 |

各馆页面纵向叙事结构（业务→能力/技术→产品/生态→案例/合作），详见原方案第4章。

---

# 5. 页面详细设计

## 5.1 关于我们

结构：企业介绍 → 品牌理念 → 发展历程 → 组织架构 → 荣誉资质 → 企业文化。
组件：时间轴、数据统计、企业照片墙。

## 5.2 新闻中心

功能：新闻列表、新闻详情、分类管理、搜索。
分类：企业动态、行业资讯、技术文章。

## 5.3 招聘中心

功能：招聘岗位、职位详情、在线投递。

---

# 6. 前端技术架构设计

## 6.1 技术选型（官网）

| 模块 | 技术           |
| ---- | -------------- |
| 框架 | Nuxt3（SSR）   |
| 语言 | TypeScript     |
| UI   | Tailwind CSS   |
| 动画 | GSAP           |
| 3D   | Three.js       |
| 状态 | Pinia          |
| 接口 | Axios          |
| 构建 | Vite           |
| 部署 | Docker + Nginx |

### 为什么选择 Nuxt3

企业官网重点在 SEO、首屏速度、SSR、搜索引擎收录，Nuxt3 的文件路由与 `@nuxtjs/seo`（或 `useHead`/`useSeoMeta`）天然契合。

## 6.2 双前端工程策略（关键修订）

原方案第9章 CMS 使用 Vue3 + Element Plus，与官网（Nuxt3）技术栈不同。**修订为两个独立工程，但通过"组件/类型契约"复用：**

- `taiji-web`（官网）：Nuxt3 + Vue3 + TS + Tailwind + Three.js。负责面向 C 端的展示与 3D 体验，要求 SSR/SEO。
- `taiji-admin`（CMS）：Vue3 + Vite + TS + Element Plus + Pinia。负责内容运营，纯 CSR，无 SEO 诉求。
- **共享层**：抽离 `taiji-shared`（npm 私有包或 monorepo workspace），统一 API 请求类型（DTO）、枚举常量（新闻分类、招聘状态）、表单校验规则，避免两端数据契约漂移。

---

# 7. 前端工程架构

```
taiji-web/
├── assets/            # 全局样式、字体、精灵图
├── components/
│   ├── common/        # Header.vue / Footer.vue / NavDrawer.vue
│   ├── three/         # TaiChiModel.vue（3D太极球，按需异步加载）
│   ├── sections/      # HeroSection / BrandSection / HallCards
│   └── business/      # 龙虎/熊猫/鲲鹏馆业务区块组件
├── composables/       # useScrollReveal / useParallax / useCountUp
├── layouts/           # default.vue
├── pages/             # index / about / dragon / panda / kunpeng / news / jobs / contact
├── stores/            # 全局轻量状态（如导航、语言）
├── utils/             # request 封装、SEO 工具
├── api/               # 接口模块
└── server/            # Nuxt server routes（如需 BFF 聚合）

taiji-admin/
├── src/
│   ├── views/         # 登录/新闻/招聘/页面内容/媒体库
│   ├── components/    # 业务表单与表格
│   ├── api/  ├── store/  ├── router/  └── utils/
```

---

# 8. UI 设计规范

## 8.1 色彩体系

- 主黑 `#080808`
- 主金 `#D4AF37`
- 科技蓝 `#1677FF`

各馆在基础黑金上叠加主题色（龙虎红、熊猫青绿、鲲鹏蓝紫），通过 CSS 变量按路由切换主题。

## 8.2 字体

中文：思源黑体 / HarmonyOS Sans；英文：Inter / Roboto。

## 8.3 动效规范

- 滚动：`ScrollTrigger`（GSAP）
- 页面切换：`Page Transition`
- 3D：Three.js Camera Animation

**响应式动效降级（新增）**：3D 与重粒子动效仅在 `min-width: 1024px` 且 `prefers-reduced-motion: no-preference` 时启用；移动端以静态视觉稿 + CSS 轻动画替代，保证 LCP 与电量。

---

# 9. 后台管理系统设计

## 9.1 架构

Vue3 + Element Plus + Spring Boot 3 + MyBatis Plus + MySQL + Redis。

## 9.2 模块

新闻管理、页面内容管理、媒体库（图片/视频）、招聘管理、管理员与权限。

## 9.3 数据表设计（修订补全）

### company_info（企业内容）
```
id          BIGINT PK
section     VARCHAR   -- 内容区块标识（intro/philosophy/history...）
title       VARCHAR
content     LONGTEXT  -- 富文本
cover       VARCHAR   -- 封面URL
seo_title   VARCHAR   -- SEO标题
seo_keywords VARCHAR
seo_desc    VARCHAR
sort        INT       -- 排序
status      TINYINT   -- 0草稿 1发布
create_by   VARCHAR
update_time DATETIME
deleted     TINYINT   -- 软删除
```

### news（新闻）
```
id, title, category(企业动态/行业资讯/技术文章), content(LONGTEXT),
cover, author, seo_*, status, view_count, publish_time, update_time, deleted
```

### job（招聘）
```
id, position, department, city, description(LONGTEXT),
requirement, salary, type(全职/实习), status(开放/关闭),
publish_time, update_time, deleted
```

> 补充理由：原表缺 SEO 字段、发布时间、软删除、排序与浏览量，无法满足官网 SEO 与运营需求。

---

# 10. 性能优化方案

## 10.1 首屏优化措施与责任模块

- 图片 WebP + `<picture>` 响应式源；首屏图走 CDN。
- 3D/重动画组件 `defineAsyncComponent` 懒加载，Non-critical JS 延迟。
- Nuxt SSR 直出首屏 HTML，提升 LCP。
- 路由级代码分割（Nuxt 默认）。
- 字体 `font-display: swap` + 子集化。

## 10.2 目标

LCP < 2.5s、FID < 100ms、CLS < 0.1（移动端 4G 弱网为准）。

---

# 11. SEO 优化方案

- 每页 `useSeoMeta` 动态注入 title/keywords/description（来自 CMS 的 seo_* 字段）。
- 结构化数据（JSON-LD）：组织信息、新闻 Article、招聘 JobPosting。
- 生成 `nuxt.config` 中的 `sitemap.xml` 与 `robots.txt`。
- 语义化标签 + alt 文本 + 合理 h1~h3 层级。

---

# 12. 部署架构

```
用户 → CDN → Nginx → Nuxt SSR 服务（Docker）
                       → API 服务（Spring Boot, Docker）
                       → MySQL / Redis
CMS 独立部署：Nginx → taiji-admin 静态资源 + 同 API 服务
```

CI/CD：GitLab CI 构建镜像 → 推送镜像仓库 → 服务器拉取部署；腾讯云/阿里云。

---

# 13. 开发实施计划（修订版 · 总工期 10 周）

> 将原"4阶段"细化为带任务、依赖、里程碑、验收的研发计划。

## 阶段一：设计与规范对齐（第1~2周）

目标：冻结视觉与数据契约，减少返工。

| 任务 | 产出 | 验收标准 |
| ---- | ---- | -------- |
| 高保真 UI 设计稿（首页+三大馆+关于/新闻/招聘） | Figma | 各馆主题色、组件态齐全 |
| 设计规范文档（色彩/字体/动效/间距 token） | design tokens | 可落地的 Tailwind 配置 |
| CMS-官网数据契约（DTO/枚举/接口清单） | API 契约文档 | 两端评审通过 |
| 信息架构与路由表确认 | 路由清单 | 搜索引擎友好 URL |

**里程碑 M1**：设计稿 + 契约评审通过，可启动开发。

## 阶段二：基础设施与脚手架（第3周，依赖 M1）

| 任务 | 说明 |
| ---- | ---- |
| 初始化 taiji-web（Nuxt3+TS+Tailwind+Pinia） | 含 SEO、布局、设计 token |
| 初始化 taiji-admin（Vue3+Vite+Element Plus） | 路由/权限/请求封装 |
| 初始化 taiji-shared（共享类型与常量） | monorepo 或私有包 |
| 后端骨架（Spring Boot + MyBatis Plus + 表结构） | 基础 CRUD 与鉴权 |
| 搭建 Docker / GitLab CI 基础流水线 | 可自动构建 |

**里程碑 M2**：两端可启动、能联调一个"新闻列表"接口。

## 阶段三：官网核心页面与 3D（第4~6周，依赖 M2）

| 周次 | 任务 | 验收 |
| ---- | ---- | ---- |
| W4 | 公共布局（Header/Footer/路由过渡）+ 首页 Hero(3D太极球) + 品牌介绍屏 | 首屏可滚动、3D在桌面端运行 |
| W5 | 三大馆页面（龙虎/熊猫/鲲鹏）业务区块 + 滚动动效 | 三馆主题切换正确、动效降级生效 |
| W6 | 关于/新闻列表+详情/招聘列表+详情/联系我们 + SEO 注入 | 各页 useSeoMeta 正确、sitemap 生成 |

**里程碑 M3**：官网全流程可走通（除 CMS 内容外为静态/接口数据）。

## 阶段四：CMS 后台（第7~8周，依赖 M2，与阶段三部分并行）

| 任务 | 说明 |
| ---- | ---- |
| 登录与权限（RBAC 基础） | 管理员/编辑角色 |
| 新闻/招聘/页面内容 CRUD | 含富文本、状态、软删除 |
| 媒体库（上传至 CDN/对象存储） | 图片视频管理 |
| 与官网接口联调，验证 seo_* 字段回流 | 官网实时读取 CMS 数据 |

**里程碑 M4**：运营可完整录入并发布一条新闻/职位，官网可见。

## 阶段五：性能、SEO、测试与上线（第9~10周，依赖 M3+M4）

| 任务 | 说明 |
| ---- | ---- |
| Lighthouse 性能调优至目标 | LCP/FID/CLS 达标 |
| 全站 SEO 校验（meta/JSON-LD/sitemap/robots） | 提交搜索引擎 |
| 功能/兼容/弱网测试（桌面+移动端） | 跨浏览器与机型 |
| 压测与监控接入（基础日志/告警） | API 与 SSR 稳定性 |
| 生产部署 + DNS/CDN 配置 | 正式对外 |

**里程碑 M5**：生产环境上线，SEO 提交完成。

---

# 14. 交付成果

```
太极馆官网 V1.0
├── PC 官网（Nuxt3 SSR）
├── 移动端 H5（同一套响应式，非独立工程）
├── CMS 管理后台（Vue3）
├── SEO 体系（meta/JSON-LD/sitemap）
├── 3D 视觉系统（太极球/各馆特效，桌面端）
├── 企业内容系统（CMS 驱动）
└── 部署与运维文档
```

> 修订说明：移动端不再单列"H5 独立工程"，而以官网响应式实现，降低维护成本；3D 在移动端按第8.3节降级。

---

# 15. 推荐最终技术方案

- 前端：`Nuxt3 + Vue3 + TypeScript + Three.js + GSAP + TailwindCSS`（官网）；`Vue3 + Vite + Element Plus`（CMS）
- 后端：`Spring Boot 3 + MyBatis Plus + MySQL + Redis`
- 共享：`taiji-shared`（类型/常量/校验）
- 运维：`Docker + Nginx + GitLab CI/CD + 腾讯云/阿里云`

---

# 16. 测试与质量保障策略（新增）

- **前端**：组件单测（Vitest，覆盖 composables/工具）、E2E（Playwright，覆盖关键转化路径：首页→三大馆→联系/投递）。
- **后端**：接口单测 + 集成测试（TestContainers 跑 MySQL）。
- **视觉回归**：关键页面截图对比，防止动效/样式回退。
- **可访问性**：基础 a11y 检查（alt、对比度、键盘可达）。
- **准入**：合并前 CI 必须绿；Lighthouse 性能门禁（LCP 低于阈值则阻断）。

---

# 17. 环境与分支规范（新增）

- 环境：本地 → 开发(dev) → 预发(staging) → 生产(prod)。
- 分支：`main`（生产）、`release/*`（预发）、`feature/*`、`fix/*`。
- 配置：环境变量按环境分离（`.env.dev/.env.prod`），密钥走 CI Secret，禁止入库。

---

# 18. 风险与应对（新增）

| 风险 | 影响 | 应对 |
| ---- | ---- | ---- |
| 3D/动效导致移动端性能劣化 | LCP 超标、跳出率高 | 第8.3节降级策略 + 性能门禁 |
| 双前端数据契约漂移 | 官网展示异常 | taiji-shared 统一 DTO + 接口契约评审 |
| 设计稿延期阻塞开发 | 排期顺延 | 阶段一先冻结"契约与路由"，视觉分批交付 |
| SEO 收录不及预期 | 获客弱 | 上线即提交 sitemap + 结构化数据 + 外链 |
| 后端接口不稳定 | 联调阻塞 | 前端以 Mock（taiji-shared 假数据）并行开发 |

---

# 项目定位总结

"太极馆"不应只是普通企业官网，而应打造：

> **一个具有东方文化符号、未来科技视觉、三大业务生态展示能力的品牌数字体验中心。**

按本方案实施，可达到华为、小米、苹果级企业官网展示效果，并具备后续扩展为数字展厅、3D虚拟空间、AI智能导览平台的能力。
