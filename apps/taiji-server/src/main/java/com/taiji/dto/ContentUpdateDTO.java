/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-19 14:30
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 14:30
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/dto/ContentUpdateDTO.java
 * @Description: 企业内容更新入参 DTO（PUT /api/content/{section}/{id}）。字段与 packages/taiji-shared
 *               CompanyInfoDTO 对齐，仅含客户端允许设置的业务字段，禁止映射 id/deleted 等内部字段。
 */
package com.taiji.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentUpdateDTO {
    /** 区块标识必填，如 about/contact（见 taiji-shared SECTIONS） */
    @NotBlank(message = "区块标识不能为空")
    private String section;

    /** 标题必填，长度上限 200 */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过 200")
    private String title;

    /** 正文必填 */
    @NotBlank(message = "正文不能为空")
    private String content;

    /** 封面选填 */
    private String cover;

    /** 排序选填，默认 0 */
    private Integer sort;

    /** 状态选填，仅 0/1（见 docs/13 §2.1） */
    private Integer status;
}
