# taiji-server 太极馆后端服务

内容管理与企业官网的后端 API。基于 Spring Boot 3，提供认证、新闻、招聘、媒体、企业信息、联系线索等 RESTful 接口，统一 `Result<T>` 响应契约（与 `taiji-shared` 的 `ApiResponse` 对齐）。

## 技术栈

| 维度 | 选型 |
| ---- | ---- |
| 框架 | Spring Boot 3.2.5 |
| 语言 | Java 17 |
| 持久层 | MyBatis-Plus 3.5.7（分页拦截器 + 逻辑删除 `deleted`） |
| 安全 | Spring Security 6 + JWT（jjwt 0.12.5）+ Redis 黑名单 |
| 存储 | MySQL 8（业务表）+ MinIO（媒体对象存储，可选） |
| 文档 | springdoc-openapi 2.5.0（`/swagger-ui.html`） |
| 构建 | Maven（Java 17） |

## 目录结构

```
taiji-server/
├── src/main/java/com/taiji/
│   ├── config/          MybatisPlusConfig（分页）、MinioConfig（可选客户端）、SecurityConfig
│   ├── controller/      News / Auth / Content / Job / Contact / Media
│   ├── entity/          CompanyInfo / News / Job / ContactLead / Media / AdminUser / AdminRole
│   ├── mapper/          MyBatis-Plus Mapper 接口
│   ├── service/         业务接口 + impl（Auth 真实 BCrypt + JWT + Redis 黑名单）
│   ├── security/        JwtUtil / JwtAuthenticationFilter / SecurityConfig
│   ├── common/          Result / PageResult / BusinessException / GlobalExceptionHandler
│   └── TaijiServerApplication.java
├── src/main/resources/
│   ├── application.yml          默认配置（MySQL / Redis / JWT / MinIO）
│   └── application-local.yml    本地验证（H2 内存库 + 排除 Redis + MinIO mock）
├── src/test/                    MediaServiceImplTest 等单元测试
└── pom.xml
```

## 环境要求

- JDK 17
- Maven 3.9+
- （可选）MySQL 8、Redis、MinIO —— 缺失时可用 `local` profile 验证

## 快速开始（无外部依赖验证）

`application-local.yml` 已内置 H2 内存库、排除 Redis、MinIO 强制 `mock` 模式，可在无 MySQL/Redis/MinIO 环境直接启动：

```bash
cd apps/taiji-server
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

启动后访问：

- API 基址：`http://localhost:8080/api`
- 接口文档：`http://localhost:8080/swagger-ui.html`
- H2 控制台：`http://localhost:8080/h2-console`（local 开启）

## 本地开发（接真实依赖）

1. 准备 MySQL 库（建表见仓库根 `db/init.sql`，初始化数据见 `db/seed.sql`），Redis，MinIO。
2. 通过环境变量注入凭据（避免明文写入配置）：

```bash
export DB_USER=root
export DB_PASSWORD=yourpassword
export JWT_SECRET=<长度≥32字节的强随机串>
export MINIO_ACCESS_KEY=<真实AccessKey>
export MINIO_SECRET_KEY=<真实SecretKey>
mvn spring-boot:run
```

> 不配置 `MINIO_ACCESS_KEY` / `MINIO_SECRET_KEY` 时，`MediaServiceImpl` 自动回退 testmock 占位（返回 `mock://taiji-media/...` URL 并落库），保证上传流程闭环。

## 构建与测试

```bash
mvn -q compile        # 编译主代码
mvn -q test           # 运行单元测试（含 MediaServiceImpl 上传分支）
mvn package           # 打可执行 jar
```

## 认证与鉴权

- 登录：`POST /api/auth/login` → 返回 `{ token, role, username }`。
- 客户端在后续请求头携带 `Authorization: Bearer <token>`。
- `JwtAuthenticationFilter` 解析 JWT 并写入 `SecurityContext`；退出登录将 token 加入 Redis 黑名单。
- 公开只读接口（如 `GET /api/news`、`GET /api/content/*`）免登录；写操作与管理接口需有效 JWT（放行清单见 `SecurityConfig`）。

## 媒体上传（MinIO / testmock 占位）

`MediaController.upload` 接收 `MultipartFile`，由 `MediaServiceImpl.upload` 处理：

- 配置了 MinIO 凭据且未强制 `mock` → 直传 MinIO，URL 经 endpoint + bucket 拼接返回。
- 否则回退 `mock://taiji-media/<uuid>-<原名>` 占位地址，仍落 `media` 表。

testmock 占位账号/令牌约定（集成测试用）：

- 账号：`MINIO_ACCESS_KEY`（占位值如 `dev-access-key`）
- 令牌/密钥：`MINIO_SECRET_KEY`（占位值如 `dev-secret-key`）

`MediaServiceImplTest` 已用 Mockito 隔离 Mapper，验证 mock 占位分支与空文件异常分支（见 `src/test`）。

## 约定

- 所有响应统一 `Result<T>`；分页用 `PageResult`（结构与 `taiji-shared` 对齐）。
- 业务异常抛 `BusinessException`，由 `GlobalExceptionHandler` 统一转换为 `Result`。
- JWT 使用 jjwt 0.12.x 新 API（`parseSignedClaims`），密钥以 `SecretKey` 注入，满足 HS256 长度要求。

## 相关文档

- 后端设计：`docs/14-*`
- 接口契约：`docs/12-*`、`docs/13-*`（与 `taiji-shared` 严格一致）
- CMS 设计：`docs/11-CMS后台设计.md`
