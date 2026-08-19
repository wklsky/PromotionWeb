/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/JobController.java
 * @Description: 招聘接口（见 docs/13 §2.4/§2.5、docs/14 接口→Controller 映射）
 */
package com.taiji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taiji.common.PageResult;
import com.taiji.common.Result;
import com.taiji.dto.JobDTO;
import com.taiji.entity.Job;
import com.taiji.service.JobService;
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
@RequestMapping("/api/jobs")
public class JobController {

    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // 官网公开读取招聘岗位（含开放/关闭），无需认证（见 SecurityConfig 放行规则）
    @GetMapping
    public Result<PageResult<Job>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        IPage<Job> data = jobService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size));
        return Result.success(PageResult.from(data));
    }

    // 招聘详情：供 CMS 编辑回填职责/要求（见 docs/13 §2.5）
    @GetMapping("/{id}")
    public Result<Job> detail(@PathVariable Long id) {
        return Result.success(jobService.getById(id));
    }

    // CMS 写操作：需带 JWT（SecurityConfig 角色校验），入参经 @Valid 校验（见 docs/13 §7）
    @PostMapping
    public Result<Long> create(@Valid @RequestBody JobDTO dto) {
        return Result.success(jobService.create(dto));
    }

    @PutMapping("/{id}")
    public Result<Boolean> update(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {
        return Result.success(jobService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        // 逻辑删除（@TableLogic deleted 字段由 MyBatis-Plus 自动处理，见 docs/12）
        boolean removed = jobService.removeById(id);
        log.info("[audit] 删除招聘 jobId={} result={}", id, removed);
        return Result.success(removed);
    }
}
