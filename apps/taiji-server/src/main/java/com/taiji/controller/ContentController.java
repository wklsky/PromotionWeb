/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/ContentController.java
 * @Description: 企业内容接口（见 docs/13 §2.1）
 */
package com.taiji.controller;

import com.taiji.common.Result;
import com.taiji.dto.ContentUpdateDTO;
import com.taiji.entity.CompanyInfo;
import com.taiji.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping("/{section}")
    public Result<List<CompanyInfo>> bySection(@PathVariable String section) {
        return Result.success(contentService.listBySection(section));
    }

    // CMS 内容维护写操作：需带 JWT 且具备 admin/editor 角色（见 SecurityConfig），入参经 @Valid 校验
    @PutMapping("/{section}/{id}")
    public Result<Boolean> update(
            @PathVariable String section,
            @PathVariable Long id,
            @Valid @RequestBody ContentUpdateDTO dto) {
        return Result.success(contentService.updateContent(
                id, dto.getSection(), dto.getTitle(), dto.getContent(),
                dto.getCover(), dto.getSort(), dto.getStatus()));
    }
}
