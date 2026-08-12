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
import com.taiji.entity.Job;
import com.taiji.mapper.JobMapper;
import com.taiji.service.JobService;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
}
