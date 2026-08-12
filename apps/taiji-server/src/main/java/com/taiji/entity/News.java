/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/News.java
 * @Description: 新闻实体，对应 news 表（见 docs/12、docs/13 §2.2/§2.3）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String category;
    private String content;
    private String cover;
    private String author;
    private String seoTitle;
    private String seoKeywords;
    private String seoDesc;
    private Integer status;
    private Integer viewCount;
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
