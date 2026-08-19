/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/NewsController.java
 * @Description: 新闻接口（见 docs/13 §2.2/§2.3、docs/14 接口→Controller 映射）
 */
package com.taiji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taiji.common.PageResult;
import com.taiji.common.Result;
import com.taiji.dto.NewsDTO;
import com.taiji.entity.News;
import com.taiji.service.NewsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 官网公开读取已发布新闻，无需认证（见 SecurityConfig 放行规则）；category 可选按分类过滤
    @GetMapping
    public Result<PageResult<News>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String category) {
        IPage<News> data = newsService.pagePublished(page, size, category);
        return Result.success(PageResult.from(data));
    }

    // 新闻详情：供 CMS 编辑回填正文（见 docs/13 §2.2、docs/16 §4.4 修复 openEdit 覆盖正文）
    @GetMapping("/{id}")
    public Result<News> detail(@PathVariable Long id) {
        return Result.success(newsService.getById(id));
    }

    // CMS 写操作：需带 JWT（SecurityConfig 角色校验），入参经 @Valid 校验（见 docs/13 §2.3）
    @PostMapping
    public Result<Long> create(@Valid @RequestBody NewsDTO dto) {
        return Result.success(newsService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @Valid @RequestBody NewsDTO dto) {
        return Result.success(newsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        // 逻辑删除（@TableLogic deleted 字段由 MyBatis-Plus 自动处理，见 docs/12）
        boolean removed = newsService.removeById(id);
        log.info("[audit] 删除新闻 newsId={} result={}", id, removed);
        return Result.success(removed);
    }
}
