/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-19 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 14:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/dto/NewsDTO.java
 * @Description: 新闻写操作入参 DTO（POST/PUT /news）。字段与 packages/taiji-shared NewsDTO 对齐，
 *               仅含客户端允许设置的业务字段，禁止映射 id/deleted 等内部字段，防止越权注入。
 */
package com.taiji.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewsDTO {
    /** 标题必填，长度上限 200（与 news.title 字段长度对齐，见 docs/12） */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过 200")
    private String title;

    /** 分类必填，取值见 taiji-shared NEWS_CATEGORY */
    @NotBlank(message = "分类不能为空")
    private String category;

    /** 正文必填 */
    @NotBlank(message = "正文不能为空")
    private String content;

    /** 封面选填 */
    private String cover;

    /** 作者选填 */
    private String author;

    private String seoTitle;
    private String seoKeywords;
    private String seoDesc;

    /** 发布状态选填，默认 0 草稿；仅 0/1（见 docs/13 §2.2） */
    private Integer status;

    /** 发布时间选填，ISO 格式由 Service 层解析 */
    private String publishTime;
}
