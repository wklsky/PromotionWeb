/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/JobServiceImpl.java
 * @Description: 招聘业务实现
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taiji.common.BusinessException;
import com.taiji.common.Constants;
import com.taiji.dto.JobDTO;
import com.taiji.entity.Job;
import com.taiji.mapper.JobMapper;
import com.taiji.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    @Override
    public Long create(JobDTO dto) {
        Job job = toEntity(dto);
        // 新增默认下线态（status=0），除非 DTO 显式指定上线态
        job.setStatus(dto.getStatus() != null ? dto.getStatus() : Constants.STATUS_OFFLINE);
        save(job);
        log.info("[audit] 新增招聘 jobId={} position={}", job.getId(), dto.getPosition());
        return job.getId();
    }

    @Override
    public boolean update(Long id, JobDTO dto) {
        Job existing = getById(id);
        if (existing == null) {
            throw new BusinessException("招聘不存在");
        }
        Job job = toEntity(dto);
        job.setId(id);
        log.info("[audit] 更新招聘 jobId={}", id);
        return updateById(job);
    }

    /** DTO → 实体映射：仅拷贝业务字段，绝不赋值 id/deleted 等内部字段（防越权注入） */
    private Job toEntity(JobDTO dto) {
        Job job = new Job();
        job.setPosition(dto.getPosition());
        job.setDepartment(dto.getDepartment());
        job.setCity(dto.getCity());
        job.setDescription(dto.getDescription());
        job.setRequirement(dto.getRequirement());
        job.setSalary(dto.getSalary());
        job.setType(dto.getType());
        if (dto.getStatus() != null) {
            job.setStatus(dto.getStatus());
        }
        return job;
    }
}
