/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/ContactLead.java
 * @Description: 留资/投递实体，对应 contact_lead 表（见 docs/12、docs/13 §2.6）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contact_lead")
public class ContactLead {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 姓名必填，长度上限 50（与 contact_lead.name 字段长度对齐，见 docs/12）
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过 50")
    private String name;
    // 手机号必填，限定中国大陆手机号格式（1 开头 11 位）
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    // 邮箱选填，但若填写需符合邮箱格式
    @Email(message = "邮箱格式不正确")
    private String email;
    // 留资类型必填，仅允许 contact/job（与 taiji-shared CONTACT_TYPES 对齐）
    @NotBlank(message = "留资类型不能为空")
    @Pattern(regexp = "^(contact|job)$", message = "留资类型非法")
    private String type;
    private Long refId;
    private String resumeUrl;
    // 留言选填，长度上限 500
    @Size(max = 500, message = "留言长度不能超过 500")
    private String message;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
