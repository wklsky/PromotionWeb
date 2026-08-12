/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/AdminUser.java
 * @Description: 管理员实体，对应 admin_user 表（见 docs/12、docs/13 §2.8）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_user")
public class AdminUser {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Long roleId;
    private Integer status;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
