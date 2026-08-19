/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/NewsService.java
 * @Description: 新闻业务接口（见 docs/14 服务层）
 */
package com.taiji.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taiji.dto.NewsDTO;
import com.taiji.entity.News;

public interface NewsService extends IService<News> {
    /**
     * 分页查询已发布新闻（CMS/官网共用，status=1 为已发布）。
     * @param category 可选分类过滤，null 表示全部
     */
    IPage<News> pagePublished(long page, long size, String category);

    /**
     * 新增新闻：入参为写操作 DTO（字段经 @Valid 校验，禁止越权注入 id/deleted）。
     * @return 新记录主键
     */
    Long create(NewsDTO dto);

    /**
     * 更新新闻：按 id 覆盖业务字段，忽略 DTO 中不存在的内部字段。
     * @return 是否更新成功
     */
    boolean update(Long id, NewsDTO dto);
}
