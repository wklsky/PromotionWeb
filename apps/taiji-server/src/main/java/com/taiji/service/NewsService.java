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
import com.taiji.entity.News;

public interface NewsService extends IService<News> {
    /**
     * 分页查询已发布新闻（CMS/官网共用，status=1 为已发布）。
     */
    IPage<News> pagePublished(long page, long size);
}
