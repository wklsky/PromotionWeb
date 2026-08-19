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
import com.taiji.common.Constants;
import com.taiji.entity.CompanyInfo;
import com.taiji.mapper.CompanyInfoMapper;
import com.taiji.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl extends ServiceImpl<CompanyInfoMapper, CompanyInfo> implements ContentService {

    private static final Logger log = LoggerFactory.getLogger(ContentServiceImpl.class);

    // 业务规则：按 section 取已启用(status=1)内容，按 sort 升序
    @Override
    public List<CompanyInfo> listBySection(String section) {
        return list(new LambdaQueryWrapper<CompanyInfo>()
                .eq(CompanyInfo::getSection, section)
                .eq(CompanyInfo::getStatus, Constants.STATUS_PUBLISHED)
                .orderByAsc(CompanyInfo::getSort));
    }

    /**
     * 更新企业内容：CMS 内容维护写操作，需 admin/editor 角色（见 SecurityConfig）。
     * 仅更新业务字段，忽略 DTO 不含的内部字段（id/deleted/createTime 等），防止越权注入。
     */
    @Override
    public boolean updateContent(Long id, String section, String title, String content,
                                 String cover, Integer sort, Integer status) {
        CompanyInfo existing = getById(id);
        if (existing == null) {
            throw new com.taiji.common.BusinessException("内容不存在");
        }
        CompanyInfo entity = new CompanyInfo();
        entity.setId(id);
        entity.setSection(section);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setCover(cover);
        entity.setSort(sort);
        // status 仅允许 0/1，缺省沿用原值
        entity.setStatus(status != null ? status : existing.getStatus());
        boolean updated = updateById(entity);
        log.info("[audit] 更新企业内容 contentId={} section={} operator=CMS", id, section);
        return updated;
    }
}
