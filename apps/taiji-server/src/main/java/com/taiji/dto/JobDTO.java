/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-19 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 14:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/dto/JobDTO.java
 * @Description: 招聘写操作入参 DTO（POST/PUT /jobs）。字段与 packages/taiji-shared JobDTO 对齐，
 *               仅含客户端允许设置的业务字段，禁止映射 id/deleted 等内部字段，防止越权注入。
 */
package com.taiji.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JobDTO {
    /** 岗位名称必填，长度上限 100 */
    @NotBlank(message = "岗位名称不能为空")
    @Size(max = 100, message = "岗位名称长度不能超过 100")
    private String position;

    /** 部门选填 */
    private String department;

    /** 城市选填 */
    private String city;

    /** 岗位职责必填 */
    @NotBlank(message = "岗位职责不能为空")
    private String description;

    /** 任职要求必填 */
    @NotBlank(message = "任职要求不能为空")
    private String requirement;

    /** 薪资选填 */
    private String salary;

    /** 工作类型必填，取值见 taiji-shared JOB_TYPE */
    @NotBlank(message = "工作类型不能为空")
    private String type;

    /** 状态选填，默认 0 下线；仅 0/1（见 docs/13 §2.4） */
    private Integer status;
}
