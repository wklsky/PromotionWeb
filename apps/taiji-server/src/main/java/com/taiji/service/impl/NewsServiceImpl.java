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
import com.taiji.common.BusinessException;
import com.taiji.common.Constants;
import com.taiji.dto.NewsDTO;
import com.taiji.entity.News;
import com.taiji.mapper.NewsMapper;
import com.taiji.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsServiceImpl.class);

    // 业务规则：官网仅展示已发布(status=1)新闻，按发布时间倒序；category 非空时按分类过滤
    @Override
    public IPage<News> pagePublished(long page, long size, String category) {
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(News::getStatus, Constants.STATUS_PUBLISHED)
               .orderByDesc(News::getPublishTime);
        if (category != null && !category.isBlank() && !"全部".equals(category)) {
            wrapper.eq(News::getCategory, category);
        }
        return page(new Page<>(page, size), wrapper);
    }

    @Override
    public Long create(NewsDTO dto) {
        News news = toEntity(dto);
        // 新增默认草稿态（status=0），除非 DTO 显式指定发布态
        news.setStatus(dto.getStatus() != null ? dto.getStatus() : Constants.STATUS_OFFLINE);
        save(news);
        log.info("[audit] 新增新闻 newsId={} title={}", news.getId(), dto.getTitle());
        return news.getId();
    }

    @Override
    public boolean update(Long id, NewsDTO dto) {
        News existing = getById(id);
        if (existing == null) {
            throw new BusinessException("新闻不存在");
        }
        News news = toEntity(dto);
        news.setId(id);
        log.info("[audit] 更新新闻 newsId={}", id);
        return updateById(news);
    }

    /** DTO → 实体映射：仅拷贝业务字段，绝不赋值 id/deleted 等内部字段（防越权注入） */
    private News toEntity(NewsDTO dto) {
        News news = new News();
        news.setTitle(dto.getTitle());
        news.setCategory(dto.getCategory());
        news.setContent(dto.getContent());
        news.setCover(dto.getCover());
        news.setAuthor(dto.getAuthor());
        news.setSeoTitle(dto.getSeoTitle());
        news.setSeoKeywords(dto.getSeoKeywords());
        news.setSeoDesc(dto.getSeoDesc());
        if (dto.getStatus() != null) {
            news.setStatus(dto.getStatus());
        }
        if (dto.getPublishTime() != null && !dto.getPublishTime().isBlank()) {
            try {
                news.setPublishTime(LocalDateTime.parse(dto.getPublishTime()));
            } catch (DateTimeParseException e) {
                // 发布时间格式非法时回退为当前时间，避免整条写入失败（CMS 多为即时发布）
                news.setPublishTime(LocalDateTime.now());
            }
        } else {
            news.setPublishTime(LocalDateTime.now());
        }
        return news;
    }
}
