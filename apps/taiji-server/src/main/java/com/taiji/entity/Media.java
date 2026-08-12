/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/Media.java
 * @Description: 媒体库实体，对应 media 表（见 docs/12、docs/13 §2.7）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("media")
public class Media {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String url;
    private String name;
    private String type;
    private Long size;
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}
