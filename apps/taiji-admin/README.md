# taiji-admin 太极馆 CMS 后台

内容管理后台，面向运营人员。基于 Vue3 + Vite 的 CSR 架构，提供新闻管理、媒体库（瀑布流）、企业信息维护等管理界面。

## 技术栈

| 维度 | 选型 |
| ---- | ---- |
| 框架 | Vue3 + Vite（CSR，`<script setup>`） |
| 语言 | TypeScript（严格模式） |
| UI | Element Plus |
| 状态 | Pinia |
| 路由 | vue-router |
| 请求 | Axios（`src/api/request.ts` 统一封装，返回类型来自 `taiji-shared`） |
| 共享契约 | `taiji-shared`（DTO / 枚举 / 主题契约） |

## 目录结构

```
taiji-admin/
├── src/
│   ├── api/request.ts           Axios 实例 + 拦截器（注入 JWT、统一错误提示）
│   ├── router/index.ts          路由（含登录守卫）
│   ├── views/
│   │   ├── NewsManage.vue       新闻管理（增删改查，见 docs/11 §4）
│   │   └── MediaLibrary.vue     媒体库（复用官网 MasonryGrid，见 docs/11 §5）
│   ├── App.vue                  根组件
│   └── main.ts                  入口
├── vite.config.ts               Vite 配置 + `/api` 代理到后端 :8080
└── index.html
```

## 环境要求

- Node.js ≥ 18（推荐 20+），pnpm ≥ 8
- 依赖安装需在仓库根目录执行 `pnpm install`（workspace 模式）

## 本地开发

```bash
# 在仓库根目录安装依赖并构建共享包
pnpm install
pnpm build:shared

# 启动后台（默认 http://localhost:5173）
pnpm dev:admin
# 或在本目录直接运行
pnpm dev
```

> API 代理：开发态 `vite.config.ts` 的 `server.proxy` 已将 `/api` 代理至 `http://localhost:8080`，无需额外配置跨域。

## 生产构建

```bash
pnpm build            # vue-tsc 类型检查 + vite 打包到 dist/
pnpm preview          # 预览构建产物
```

## 类型检查

```bash
pnpm typecheck
```

## 登录与鉴权

- 后台登录调用 `POST /api/auth/login`，后端返回 `{ token, role, username }`。
- 请求拦截器自动附加 `Authorization: Bearer <token>`；响应拦截器统一处理 401（跳转登录）与错误提示。
- 公开只读接口（如新闻列表）无需登录，写操作需携带有效 JWT（见后端 `SecurityConfig` 放行清单）。

## 约定与注意事项

- Props 必须 `withDefaults` 提供默认值；对象/数组默认用工厂函数返回，避免引用污染。
- Emits 事件名使用 kebab-case；所有对外接口用 JSDoc 注明业务含义。
- 媒体库直接复用 `taiji-shared` 与官网一致的瀑布流契约，保证前后台视觉统一（见 docs/11 §5）。

## 相关文档

- CMS 设计：`docs/11-CMS后台设计.md`
- 组件设计：`docs/09-组件设计文档.md`
- 接口契约：`docs/12-*`、`docs/13-*`（前端 DTO 与 `taiji-shared` 一致）
