/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/mapper/NewsMapper.java
 * @Description: 新闻 Mapper（MyBatis-Plus，见 docs/14）
 */
package com.taiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taiji.entity.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
