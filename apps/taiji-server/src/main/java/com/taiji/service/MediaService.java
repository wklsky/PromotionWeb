/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/MediaService.java
 * @Description: 媒体库业务接口（见 docs/13 §2.7）
 */
package com.taiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taiji.entity.Media;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService extends IService<Media> {

    /**
     * 上传媒体文件。
     * 业务规则：优先直传 MinIO（需配置 minio.access-key/secret-key），
     * 未配置时回退 testmock 占位模式（生成模拟 URL，便于无对象存储环境联调）。
     *
     * @param file     上传的文件
     * @param operator 操作人（来自 JWT 解析的 username），用于审计追踪
     * @return 持久化后的媒体记录（含可访问 url）
     */
    Media upload(MultipartFile file, String operator);
}
