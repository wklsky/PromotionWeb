# taiji-shared

太极馆企业宣传网站前后端共享契约包。

## 定位

本包是 `docs/12-数据库设计.md` 与 `docs/13-Swagger接口文档.md` 的**代码化镜像**，为三处提供单一事实源：

| 来源 | 内容 |
| ---- | ---- |
| `src/enums.ts` | 枚举常量（与 13 §9 一致） |
| `src/types.ts` | 业务 VO/DTO（与 12 表字段、13 §2 一致） |
| `src/http.ts` | 响应/分页包装（与 13 §1 一致） |

## 使用

```bash
npm install taiji-shared
```

```ts
import type { NewsDTO, NewsCategory, ApiResponse, PageResult } from 'taiji-shared';
```

- 官网 `taiji-web`、CMS `taiji-admin` 直接依赖本包做类型约束；
- 后端（Spring Boot）可据此反向生成实体/DTO，或作为接口评审基准。

## 变更纪律

修改枚举取值、VO 字段、响应结构时，**必须同步更新 `docs/12` 与 `docs/13`**，并在 PR 中说明，防止契约漂移。
