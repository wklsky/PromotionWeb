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
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("contact_lead")
public class ContactLead {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String type;
    private Long refId;
    private String resumeUrl;
    private String message;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
