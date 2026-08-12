/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/mapper/JobMapper.java
 * @Description: 招聘 Mapper
 */
package com.taiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taiji.entity.Job;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobMapper extends BaseMapper<Job> {
}
