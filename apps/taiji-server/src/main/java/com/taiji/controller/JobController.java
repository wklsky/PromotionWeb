/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/JobController.java
 * @Description: 招聘接口（见 docs/13 §2.4/§2.5）
 */
package com.taiji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taiji.common.PageResult;
import com.taiji.common.Result;
import com.taiji.entity.Job;
import com.taiji.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Result<PageResult<Job>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size) {
        IPage<Job> data = jobService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size));
        return Result.success(PageResult.from(data));
    }
}
