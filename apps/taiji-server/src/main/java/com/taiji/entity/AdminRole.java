/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/AdminRole.java
 * @Description: 角色实体，对应 admin_role 表（见 docs/12、docs/13 §9）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("admin_role")
public class AdminRole {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String desc;
}
