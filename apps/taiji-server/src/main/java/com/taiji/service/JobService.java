/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/JobService.java
 * @Description: 招聘业务接口（见 docs/13 §2.4/§2.5）
 */
package com.taiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taiji.dto.JobDTO;
import com.taiji.entity.Job;

public interface JobService extends IService<Job> {
    /**
     * 新增招聘：入参为写操作 DTO（字段经 @Valid 校验，禁止越权注入 id/deleted）。
     * @return 新记录主键
     */
    Long create(JobDTO dto);

    /**
     * 更新招聘：按 id 覆盖业务字段，忽略 DTO 中不存在的内部字段。
     * @return 是否更新成功
     */
    boolean update(Long id, JobDTO dto);
}
