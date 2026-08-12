/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:30
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/MediaServiceImpl.java
 * @Description: 媒体库业务实现（见 docs/11 §5、docs/14 §6）
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taiji.entity.Media;
import com.taiji.mapper.MediaMapper;
import com.taiji.service.MediaService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    // MinIO 客户端：未配置凭据时 Bean 返回 null，由 MinioConfig 控制，这里可选注入
    @Autowired(required = false)
    private MinioClient minioClient;

    @Value("${minio.endpoint:}")
    private String endpoint;

    @Value("${minio.bucket:taiji-media}")
    private String bucket;

    // testmock 占位模式开关：显式置 true 时强制生成模拟 URL，便于无对象存储联调
    @Value("${minio.mock:false}")
    private boolean mockEnabled;

    @Override
    public Media upload(MultipartFile file, String operator) {
        if (file == null || file.isEmpty()) {
            throw new com.taiji.common.BusinessException("上传文件为空");
        }
        String originalName = StringUtils.hasText(file.getOriginalFilename())
                ? file.getOriginalFilename() : "unnamed";
        String objectName = UUID.randomUUID() + "-" + originalName;
        String url;

        // 业务规则：仅当客户端存在且未强制 mock 时才走真实对象存储直传
        if (minioClient != null && !mockEnabled) {
            url = putToMinio(file, objectName);
        } else {
            // testmock 占位：无 MinIO 凭据/本地验证时，返回可识别的模拟地址，仍落库保证流程闭环
            url = buildMockUrl(objectName);
        }

        Media media = new Media();
        media.setUrl(url);
        media.setName(originalName);
        media.setType(file.getContentType());
        media.setSize(file.getSize());
        media.setCreateTime(LocalDateTime.now());
        baseMapper.insert(media);
        return media;
    }

    /** 真实直传 MinIO 并返回可访问 URL（经 Nginx/CDN 反代，见 docs/11 §6） */
    private String putToMinio(MultipartFile file, String objectName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            // 对外暴露 CDN/反代域名而非裸 MinIO 端点（docs/14 §6）
            return endpoint + "/" + bucket + "/" + objectName;
        } catch (Exception e) {
            throw new com.taiji.common.BusinessException("MinIO 上传失败：" + e.getMessage());
        }
    }

    /** testmock 占位 URL：仅用于无真实对象存储时的流程联调，形态可辨识 */
    private String buildMockUrl(String objectName) {
        return "mock://taiji-media/" + objectName;
    }
}
