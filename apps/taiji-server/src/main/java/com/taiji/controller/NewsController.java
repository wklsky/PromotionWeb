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
import com.taiji.entity.News;
import com.taiji.service.NewsService;
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

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // 官网公开读取已发布新闻，无需认证（见 SecurityConfig 放行规则）
    @GetMapping
    public Result<PageResult<News>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        IPage<News> data = newsService.pagePublished(page, size);
        return Result.success(PageResult.from(data));
    }

    // CMS 写操作：需带 JWT（SecurityConfig 已 authenticated），由前端 admin 调用（见 docs/13 §2.3）
    @PostMapping
    public Result<Long> save(@RequestBody News news) {
        newsService.save(news);
        return Result.success(news.getId());
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        return Result.success(newsService.updateById(news));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        // 逻辑删除（@TableLogic deleted 字段由 MyBatis-Plus 自动处理，见 docs/12）
        return Result.success(newsService.removeById(id));
    }
}
