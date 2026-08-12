/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/mapper/AdminUserMapper.java
 * @Description: 管理员 Mapper
 */
package com.taiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taiji.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
