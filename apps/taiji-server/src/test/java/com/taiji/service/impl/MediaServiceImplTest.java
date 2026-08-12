/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:30
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:30
 * @FilePath: apps/taiji-server/src/test/java/com/taiji/service/impl/MediaServiceImplTest.java
 * @Description: MediaServiceImpl 上传分支测试（testmock 占位模式，见 docs/14 §6）
 */
package com.taiji.service.impl;

import com.taiji.entity.Media;
import com.taiji.mapper.MediaMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 采用 Mockito 隔离 Mapper，无需真实数据库/MinIO 即可验证两种分支：
 * 1) mock 占位模式（minioClient=null）→ 返回 mock:// URL 并落库
 * 2) 文件为空 → 抛出 BusinessException
 *
 * 若需联调真实 MinIO，可在此处注入凭据（testmock 占位账号/令牌约定见 docs/14 §6）：
 *   - 账号：MINIO_ACCESS_KEY（占位值如 dev-access-key）
 *   - 令牌/密钥：MINIO_SECRET_KEY（占位值如 dev-secret-key）
 * 通过 application-test.yml 的 minio.mock=false + 真实端点拉起集成测试。
 */
@ExtendWith(MockitoExtension.class)
class MediaServiceImplTest {

    @org.mockito.Mock
    private MediaMapper mediaMapper;

    @InjectMocks
    private MediaServiceImpl mediaService;

    // testmock 占位操作人（模拟 JWT 解析出的 username）
    private static final String TEST_OPERATOR = "test-operator";

    @BeforeEach
    void setUp() {
        // MyBatis-Plus 的 baseMapper 为父类 protected 字段，Mockito @InjectMocks 不会自动装配，
        // 手动将 mock 的 mapper 注入以隔离数据库依赖（见 docs/14 §6）
        ReflectionTestUtils.setField(mediaService, "baseMapper", mediaMapper);
    }

    @Test
    void upload_shouldReturnMockUrlWhenMinioClientAbsent() {
        // 无 MinIO 客户端（mock=true 默认分支）：应回退占位 URL 并落库
        when(mediaMapper.insert(any(Media.class))).thenAnswer(inv -> {
            Media m = inv.getArgument(0);
            m.setId(1L);
            return 1;
        });

        MultipartFile file = new MockMultipartFile(
                "file", "taiji-hero.png", "image/png", new byte[]{1, 2, 3});

        Media result = mediaService.upload(file, TEST_OPERATOR);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("taiji-hero.png", result.getName());
        assertEquals("image/png", result.getType());
        // 占位模式下 URL 以 mock:// 开头，便于识别未接真实对象存储
        assertEquals(true, result.getUrl().startsWith("mock://taiji-media/"));
    }

    @Test
    void upload_shouldThrowWhenFileEmpty() {
        MultipartFile empty = new MockMultipartFile(
                "file", "empty.png", "image/png", new byte[0]);
        assertThrows(com.taiji.common.BusinessException.class,
                () -> mediaService.upload(empty, TEST_OPERATOR));
    }
}
