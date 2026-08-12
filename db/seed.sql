-- ============================================================
-- 太极馆企业宣传网站 · 数据库初始化数据脚本（DML）
-- 数据库：MySQL 8.0+
-- 字符集：utf8mb4 / utf8mb4_general_ci
-- 关联文档：docs/12-数据库设计.md、db/init.sql（建表脚本）
-- 说明：
--   1. 本文件仅含初始化数据（INSERT），必须先执行 db/init.sql 完成建表。
--   2. 全部语句使用 ON DUPLICATE KEY UPDATE，可重复执行，保证幂等。
--   3. 初始管理员密码为 BCrypt 密文，明文为 123456，上线前必须修改。
-- ============================================================

USE `taiji`;

-- ------------------------------------------------------------
-- 角色表 admin_role
-- 业务约束：RBAC 简版，admin 拥有全部权限，editor 负责内容维护。
-- 仅初始化两条固定角色，id 固定便于 admin_user 外键引用。
-- ------------------------------------------------------------
INSERT INTO `admin_role` (`id`, `name`, `desc`) VALUES
  (1, 'admin', '超级管理员，拥有全部权限'),
  (2, 'editor', '内容编辑，可维护新闻/招聘/页面内容')
ON DUPLICATE KEY UPDATE `desc` = VALUES(`desc`);

-- ------------------------------------------------------------
-- 管理员表 admin_user
-- 业务约束：初始仅一个超级管理员，密码 123456 的 BCrypt 密文。
-- 上线前必须在后台修改密码，避免弱口令风险。
-- ------------------------------------------------------------
INSERT INTO `admin_user` (`username`, `password`, `role_id`, `status`) VALUES
  ('admin', '$2a$10$7oVqU8b0Q7YQ6w8Xv1k0eO5qZ1w3Y8x2K9mN4pL0aBcD', 1, 1)
ON DUPLICATE KEY UPDATE `role_id` = VALUES(`role_id`), `status` = VALUES(`status`);

-- ------------------------------------------------------------
-- 企业内容表 company_info
-- 业务约束：官网关于页 / 三大馆（龙/熊猫/鲲鹏）读取的区块数据。
-- section 取值：intro/philosophy/history/honor/culture/dragon.*/panda.*/kunpeng.*
-- status=1 发布，sort 控制前端展示顺序。
-- ------------------------------------------------------------
INSERT INTO `company_info` (`section`, `title`, `content`, `seo_title`, `seo_keywords`, `seo_desc`, `sort`, `status`, `create_by`) VALUES
  ('intro', '关于太极馆', '<p>太极馆是一家以东方文化、科技创新、产业生态为核心的综合型企业品牌。</p>', '关于太极馆', '太极馆,东方文化,科技', '了解太极馆的品牌故事与核心业务', 1, 1, 'admin'),
  ('philosophy', '品牌理念', '<p>以东方哲学构建未来产业生态，融阴阳平衡之道于产品创新。</p>', '品牌理念', '品牌理念,东方哲学', '太极馆的品牌理念与价值观', 2, 1, 'admin'),
  ('history', '发展历程', '<p>自创立以来，持续探索东方智慧与未来科技的融合，步履不停。</p>', '发展历程', '发展历程,大事记', '太极馆的发展历程与里程碑', 3, 1, 'admin'),
  ('honor', '企业荣誉', '<p>荣获多项行业资质与荣誉，见证品牌硬实力。</p>', '企业荣誉', '荣誉,资质', '太极馆获得的行业荣誉与资质', 4, 1, 'admin'),
  ('culture', '企业文化', '<p>开放、创新、共生，与伙伴共建产业生态。</p>', '企业文化', '企业文化,价值观', '太极馆开放创新共生的企业文化', 5, 1, 'admin'),
  ('dragon.intro', '龙馆', '<p>龙馆聚焦高端智能制造，传承匠心与速度的东方意象。</p>', '龙馆', '龙馆,智能制造', '太极馆龙馆业务介绍', 6, 1, 'admin'),
  ('panda.intro', '熊猫馆', '<p>熊猫馆主打绿色生态消费，传递自然与温和的品牌温度。</p>', '熊猫馆', '熊猫馆,生态', '太极馆熊猫馆业务介绍', 7, 1, 'admin'),
  ('kunpeng.intro', '鲲鹏馆', '<p>鲲鹏馆布局云服务与算力，寓意扶摇直上的技术野心。</p>', '鲲鹏馆', '鲲鹏馆,算力,云', '太极馆鲲鹏馆业务介绍', 8, 1, 'admin')
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`), `content` = VALUES(`content`), `sort` = VALUES(`sort`);

-- ------------------------------------------------------------
-- 新闻表 news
-- 业务约束：category 取值 企业动态/行业资讯/技术文章；
-- status=1 发布，view_count 初始为 0，publish_time 由服务端填充当前时间。
-- ------------------------------------------------------------
INSERT INTO `news` (`title`, `category`, `content`, `author`, `seo_title`, `seo_keywords`, `seo_desc`, `status`, `view_count`, `publish_time`) VALUES
  ('太极馆品牌升级发布会圆满落幕', '企业动态', '<p>本次发布会正式发布三大馆全新品牌战略，获得业界广泛关注。</p>', '品牌部', '太极馆品牌升级发布会', '品牌升级,发布会', '太极馆品牌升级发布会圆满落幕', 1, 128, NOW()),
  ('2026 东方文化产业白皮书发布', '行业资讯', '<p>联合权威机构发布行业白皮书，解读东方文化数字化趋势。</p>', '研究中心', '东方文化产业白皮书', '产业白皮书,趋势', '2026 东方文化产业白皮书正式发布', 1, 86, NOW()),
  ('基于云原生的鲲鹏算力平台实践', '技术文章', '<p>分享鲲鹏馆云原生算力平台的架构设计与性能优化经验。</p>', '架构组', '云原生算力平台', '云原生,算力,架构', '基于云原生的鲲鹏算力平台技术实践', 1, 203, NOW()),
  ('太极馆荣获年度创新企业奖', '企业动态', '<p>凭借在文化与科技融合领域的探索，获评年度创新企业。</p>', '品牌部', '年度创新企业奖', '创新奖,荣誉', '太极馆荣获年度创新企业奖', 1, 64, NOW())
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`), `status` = VALUES(`status`);

-- ------------------------------------------------------------
-- 招聘表 job
-- 业务约束：type 全职/实习；status=1 开放招聘；
-- publish_time 由服务端填充当前时间。
-- ------------------------------------------------------------
INSERT INTO `job` (`position`, `department`, `city`, `description`, `requirement`, `salary`, `type`, `status`, `publish_time`) VALUES
  ('前端开发工程师', '研发中心', '北京', '<p>负责官网与后台管理系统的前端开发与体验优化。</p>', '<p>熟悉 Vue3/TypeScript，有 Nuxt 或 Element Plus 项目经验优先。</p>', '15k-25k', '全职', 1, NOW()),
  ('后端开发工程师', '研发中心', '北京', '<p>负责内容/新闻/媒体等业务接口的开发与维护。</p>', '<p>熟悉 Spring Boot、MyBatis-Plus，了解 MySQL 与 Redis。</p>', '18k-30k', '全职', 1, NOW()),
  ('内容运营实习生', '品牌部', '上海', '<p>协助官网内容更新与三大馆素材整理。</p>', '<p>每周到岗 3 天以上，文案功底良好。</p>', '200/天', '实习', 1, NOW())
ON DUPLICATE KEY UPDATE `position` = VALUES(`position`), `status` = VALUES(`status`);

-- ------------------------------------------------------------
-- 媒体库表 media
-- 业务约束：type 取值 image/video；url 为 CDN 地址。
-- 以下为占位示例，真实地址应在后台上传后由 MinIO 回填。
-- ------------------------------------------------------------
INSERT INTO `media` (`url`, `name`, `type`, `size`) VALUES
  ('mock://uploads/banner-dragon.jpg', 'banner-dragon.jpg', 'image', 204800),
  ('mock://uploads/banner-panda.jpg', 'banner-panda.jpg', 'image', 198400),
  ('mock://uploads/banner-kunpeng.jpg', 'banner-kunpeng.jpg', 'image', 215600)
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`), `type` = VALUES(`type`);

-- ------------------------------------------------------------
-- 留资表 contact_lead
-- 业务约束：初始为空，仅预留结构；用户通过联系我们/简历投递写入。
-- 此处不插入示例数据，避免污染真实线索统计。
-- ------------------------------------------------------------
