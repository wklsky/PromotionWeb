-- ============================================================
-- 太极馆企业宣传网站 · 数据库初始化脚本
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4 / utf8mb4_general_ci
-- 关联文档：docs/12-数据库设计.md、docs/13-Swagger接口文档.md
-- 说明：本文件为建表 + 初始化数据，可直接执行；生产建议配合 Flyway/Liquibase 版本管理。
-- ============================================================

CREATE DATABASE IF NOT EXISTS `taiji`
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE `taiji`;

-- ------------------------------------------------------------
-- 2.1 企业内容表 company_info
-- 对应接口：GET/POST/PUT/DELETE /content、GET /content/{section}
-- 字段与 docs/13 §2.1 CompanyInfoVO 一一对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `company_info` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `section`     VARCHAR(32)  NOT NULL COMMENT '区块标识 intro/philosophy/history/honor/culture/dragon.*/panda.*/kunpeng.*',
  `title`       VARCHAR(128) NOT NULL COMMENT '标题',
  `content`     LONGTEXT      NULL     COMMENT '富文本 HTML',
  `cover`       VARCHAR(512) NULL     COMMENT '封面 URL',
  `seo_title`   VARCHAR(128) NULL     COMMENT 'SEO 标题',
  `seo_keywords` VARCHAR(255) NULL    COMMENT 'SEO 关键词',
  `seo_desc`    VARCHAR(512) NULL     COMMENT 'SEO 描述',
  `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '0草稿 1发布',
  `create_by`   VARCHAR(64)  NULL     COMMENT '创建人',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除 0未删 1已删',
  PRIMARY KEY (`id`),
  KEY `idx_section` (`section`),
  KEY `idx_status_sort` (`status`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业内容表';

-- ------------------------------------------------------------
-- 2.2 新闻表 news
-- 对应接口：GET/POST/PUT/DELETE /news、GET /news/{id}
-- 字段与 docs/13 §2.2/§2.3 NewsListVO/NewsDetailVO 对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `news` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title`        VARCHAR(128) NOT NULL COMMENT '标题',
  `category`     VARCHAR(32)  NOT NULL COMMENT '分类 企业动态/行业资讯/技术文章',
  `content`      LONGTEXT     NULL     COMMENT '富文本 HTML',
  `cover`        VARCHAR(512) NULL     COMMENT '封面 URL',
  `author`       VARCHAR(64)  NULL     COMMENT '作者',
  `seo_title`    VARCHAR(128) NULL     COMMENT 'SEO 标题',
  `seo_keywords` VARCHAR(255) NULL     COMMENT 'SEO 关键词',
  `seo_desc`     VARCHAR(512) NULL     COMMENT 'SEO 描述',
  `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '0草稿 1发布',
  `view_count`   INT          NOT NULL DEFAULT 0 COMMENT '浏览量',
  `publish_time` DATETIME     NULL     COMMENT '发布时间（不传则服务端填当前）',
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_status` (`category`, `status`),
  KEY `idx_publish` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='新闻表';

-- ------------------------------------------------------------
-- 2.3 招聘表 job
-- 对应接口：GET/POST/PUT/DELETE /jobs、GET /jobs/{id}
-- 字段与 docs/13 §2.4/§2.5 JobListVO/JobDetailVO 对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `job` (
  `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `position`     VARCHAR(64)  NOT NULL COMMENT '职位名',
  `department`   VARCHAR(64)  NULL     COMMENT '部门',
  `city`         VARCHAR(32)  NULL     COMMENT '城市',
  `description`  LONGTEXT     NULL     COMMENT '职责',
  `requirement`  LONGTEXT     NULL     COMMENT '要求',
  `salary`       VARCHAR(32)  NULL     COMMENT '薪资范围',
  `type`         VARCHAR(16)  NOT NULL DEFAULT '全职' COMMENT '全职/实习',
  `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '0关闭 1开放',
  `publish_time` DATETIME     NULL     COMMENT '发布时间',
  `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`      TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='招聘表';

-- ------------------------------------------------------------
-- 2.4 留资表 contact_lead（联系我们 / 简历投递）
-- 对应接口：POST /contact、POST /jobs/{id}/apply、GET /leads
-- 字段与 docs/13 §2.6 ContactLeadVO 对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `contact_lead` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name`        VARCHAR(64)  NOT NULL COMMENT '姓名',
  `phone`       VARCHAR(32)  NOT NULL COMMENT '手机',
  `email`       VARCHAR(128) NULL     COMMENT '邮箱',
  `type`        VARCHAR(16)  NOT NULL DEFAULT 'contact' COMMENT 'contact/job',
  `ref_id`      BIGINT       NULL     COMMENT '关联 job.id（投递时）',
  `resume_url`  VARCHAR(512) NULL     COMMENT '简历链接',
  `message`     VARCHAR(1024) NULL    COMMENT '留言',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_ref` (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='留资表';

-- ------------------------------------------------------------
-- 2.5 媒体库表 media
-- 对应接口：POST /media/upload、GET /media、DELETE /media/{id}
-- 字段与 docs/13 §2.7 MediaVO 对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `media` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url`         VARCHAR(512) NOT NULL COMMENT 'CDN 地址',
  `name`        VARCHAR(255) NOT NULL COMMENT '原始文件名',
  `type`        VARCHAR(16)  NOT NULL COMMENT 'image/video',
  `size`        BIGINT       NULL     COMMENT '字节',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='媒体库表';

-- ------------------------------------------------------------
-- 2.6 CMS 权限（简版 RBAC）
-- 对应接口：POST /auth/login、POST /auth/logout、用户管理
-- 字段与 docs/13 §2.8 / §9 Role 对应
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `admin_role` (
  `id`   BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(32) NOT NULL COMMENT '角色名 admin/editor',
  `desc` VARCHAR(64) NULL     COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

CREATE TABLE IF NOT EXISTS `admin_user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`    VARCHAR(64)  NOT NULL COMMENT '登录名',
  `password`    VARCHAR(128) NOT NULL COMMENT 'BCrypt 密文',
  `role_id`     BIGINT       NOT NULL COMMENT '角色ID',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
  `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '软删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `admin_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='管理员表';

-- ============================================================
-- 说明：本文件仅负责建库 + 建表（DDL）。
-- 初始化数据（角色/管理员/示例内容等）已拆分至 db/seed.sql（DML），
-- 需先执行本文件建表后，再执行 db/seed.sql 灌入数据。
-- ============================================================
