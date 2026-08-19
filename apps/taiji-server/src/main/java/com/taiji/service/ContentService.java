/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/ContentService.java
 * @Description: 企业内容业务接口（见 docs/13 §2.1）
 */
package com.taiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taiji.entity.CompanyInfo;

import java.util.List;

public interface ContentService extends IService<CompanyInfo> {
    /**
     * 按区块(section)查询已启用内容，官网各页面对应 section 常量。
     */
    List<CompanyInfo> listBySection(String section);

    /**
     * 更新企业内容（CMS 内容维护写操作，需 admin/editor 角色）。
     */
    boolean updateContent(Long id, String section, String title, String content,
                          String cover, Integer sort, Integer status);
}
