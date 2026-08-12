# 太极馆企业官网 Monorepo

> 企业官网 + CMS 后台 + 后端服务 + 共享契约的类型安全 monorepo。
> 文档体系见 `docs/`，数据库见 `db/init.sql`，开发设计见 `docs/07`、`docs/14`。

## 工程结构

```
TaiJi/
├── apps/
│   ├── taiji-web/      官网（Nuxt3 SSR + Vue3 + TS + Tailwind + Three.js + GSAP）
│   └── taiji-admin/    CMS 后台（Vue3 + Vite + Element Plus）
├── packages/
│   └── taiji-shared/   前后端共享 DTO/枚举/主题契约（与 docs/12、docs/13 严格一致）
├── db/                 数据库建表 SQL（init.sql）
└── docs/               PRD / 架构 / 组件 / 接口 / 后端设计等文档
```

## 技术栈

| 工程 | 框架 | 语言 | UI | 状态 | 接口 |
| ---- | ---- | ---- | -- | ---- | ---- |
| taiji-web | Nuxt3 (SSR) | TS | Tailwind | Pinia | Axios |
| taiji-admin | Vue3 + Vite (CSR) | TS | Element Plus | Pinia | Axios |
| taiji-server | Spring Boot 3 | Java | — | — | MyBatis-Plus |
| taiji-shared | TS 类型包 | TS | — | — | — |

## 快速开始

```bash
pnpm install
pnpm build:shared      # 先构建共享契约
pnpm dev:web           # 启动官网
pnpm dev:admin         # 启动 CMS
```

## 约定

- 所有 API 返回类型来自 `taiji-shared`，严禁 `any`（未知用 `unknown` 收窄）。
- 组件逻辑 >100 行必须抽离为 `useXxx.ts` composable。
- 主题色通过 `taiji-shared` 的 `THEME_PALETTES` / `ROUTE_THEME_MAP` 与 CSS 变量联动。
