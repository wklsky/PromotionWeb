# taiji-web 太极馆官网（前台）

企业官网前端，面向终端访客。基于 Nuxt3 的 SSR 架构，承载首页（太极 Hero 3D）、三馆叙事流、新闻瀑布流等核心展示页面。

## 技术栈

| 维度 | 选型 |
| ---- | ---- |
| 框架 | Nuxt3（SSR / Vue3 `<script setup>`） |
| 语言 | TypeScript（严格模式） |
| 样式 | Tailwind CSS |
| 状态 | Pinia |
| 3D | Three.js（仅首页 Hero，`≥1024px` 且非 `prefers-reduced-motion` 时启用，否则降级） |
| 动效 | GSAP（视差 / 入场） |
| 请求 | Axios（统一封装于 `utils/request.ts`，返回类型来自 `taiji-shared`） |
| 共享契约 | `taiji-shared`（DTO / 枚举 / 主题 `THEME_PALETTES` / `ROUTE_THEME_MAP`） |

## 目录结构

```
taiji-web/
├── assets/styles/main.css       全局样式与主题 CSS 变量
├── components/
│   ├── common/MasonryGrid.vue   瀑布流通用组件（见 docs/03、docs/09 §7）
│   └── three/TaiChiModel.vue    太极 3D 模型（降级策略见 docs/09 §5）
├── composables/                 useTheme / useParallax 等组合式函数
├── api/content.ts               内容接口（类型对齐 shared 的 NewsListVO 等）
├── layouts/default.vue          默认布局
├── pages/                       index / [hall] / news 等路由页面
├── nuxt.config.ts               SSR 配置 + `/api` 反向代理到后端 :8080
└── app.vue                      根组件
```

## 环境要求

- Node.js ≥ 18（推荐 20+），pnpm ≥ 8
- 依赖安装需在仓库根目录执行 `pnpm install`（workspace 模式）

## 本地开发

```bash
# 在仓库根目录安装全部 workspace 依赖并构建共享包
pnpm install
pnpm build:shared

# 启动前台（默认 http://localhost:3000）
pnpm dev:web
# 或在本目录直接运行
pnpm dev
```

> API 代理：开发态 `nuxt.config.ts` 的 `nitro.routeRules` 已将 `/api` 代理至 `http://localhost:8080`，无需额外配置跨域。

## 生产构建

```bash
pnpm build          # 产出 .output（Nuxt 服务器产物）
pnpm generate       # 静态化（适用于纯静态部署场景）
pnpm preview        # 本地预览构建结果
```

## 类型检查

```bash
pnpm typecheck
```

## 约定与注意事项

- 主题色由 `taiji-shared` 的 `THEME_PALETTES` / `ROUTE_THEME_MAP` 驱动，页面仅消费 CSS 变量，禁止硬编码色值。
- 3D 仅用于首页 Hero，三馆内页使用 CSS / Canvas2D 粒子，避免重型渲染拖累叙事流性能（见 docs/09 §5）。
- 组件业务 `script` 超过 100 行须抽离为 `composables/useXxx.ts`。
- 接口返回类型强制来自 `taiji-shared`，严禁 `any`（未知用 `unknown` 收窄）。

## 相关文档

- 信息架构：`docs/03-网站信息架构IA.md`
- UI 规范：`docs/05-UI视觉设计规范.md`
- 组件设计：`docs/09-组件设计文档.md`
- 三馆与首页设计：`docs/07-*`
