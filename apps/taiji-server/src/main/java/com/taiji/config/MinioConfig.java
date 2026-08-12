/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:30
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:30
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/config/MinioConfig.java
 * @Description: MinIO 客户端配置（见 docs/14 §6）
 */
package com.taiji.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * 仅当显式配置了 access-key / secret-key 时才创建 MinioClient Bean。
 * 否则不注入（MediaServiceImpl 走 testmock 占位分支），避免本地无对象存储时启动失败。
 */
@Configuration
public class MinioConfig {

    @Value("${minio.endpoint:}")
    private String endpoint;

    @Value("${minio.access-key:}")
    private String accessKey;

    @Value("${minio.secret-key:}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        if (!StringUtils.hasText(accessKey) || !StringUtils.hasText(secretKey)) {
            // 未配置凭据：不创建客户端，交由 MediaServiceImpl 回退占位实现
            return null;
        }
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
