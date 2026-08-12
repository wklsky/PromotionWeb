/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/NewsServiceImpl.java
 * @Description: 新闻业务实现（MyBatis-Plus ServiceImpl，见 docs/14）
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taiji.entity.News;
import com.taiji.mapper.NewsMapper;
import com.taiji.service.NewsService;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    // 业务规则：官网仅展示已发布(status=1)新闻，按发布时间倒序
    @Override
    public IPage<News> pagePublished(long page, long size) {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(News::getStatus, 1)
               .orderByDesc(News::getPublishTime);
        return page(new Page<>(page, size), wrapper);
    }
}
