/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/MediaController.java
 * @Description: 媒体库接口（见 docs/11 §5、docs/14 MinIO）
 */
package com.taiji.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taiji.common.PageResult;
import com.taiji.common.Result;
import com.taiji.entity.Media;
import com.taiji.service.MediaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // 直传 MinIO 后回填 URL 并写入 media 表（见 docs/11 §6、docs/14 §6）
    // 操作人取自 JWT 认证主体（SecurityContext 中的 username），用于审计追踪
    @PostMapping("/upload")
    public Result<com.taiji.entity.Media> upload(@RequestParam("file") MultipartFile file, Authentication authentication) {
        String operator = authentication != null && authentication.getName() != null
                ? authentication.getName() : "anonymous";
        return Result.success(mediaService.upload(file, operator));
    }

    @GetMapping
    public Result<PageResult<Media>> list(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "20") long size) {
        IPage<Media> data = mediaService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size));
        return Result.success(PageResult.from(data));
    }

    // 删除媒体：DELETE /api/media/{id}，需认证（见 docs/11 §5、docs/14）
    @DeleteMapping("/{id}")
    public Result<Boolean> remove(@PathVariable Long id) {
        // 逻辑删除（@TableLogic deleted 字段由 MyBatis-Plus 自动处理，见 docs/12）
        return Result.success(mediaService.removeById(id));
    }
}
