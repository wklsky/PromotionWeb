/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/ContentServiceImpl.java
 * @Description: 企业内容业务实现
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taiji.entity.CompanyInfo;
import com.taiji.mapper.CompanyInfoMapper;
import com.taiji.service.ContentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements ContentService {

    // 业务规则：按 section 取已启用(status=1)内容，按 sort 升序
    @Override
    public List<CompanyInfo> listBySection(String section) {
        return list(new LambdaQueryWrapper<CompanyInfo>()
                .eq(CompanyInfo::getSection, section)
                .eq(CompanyInfo::getStatus, 1)
                .orderByAsc(CompanyInfo::getSort));
    }
}
