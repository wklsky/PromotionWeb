/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/entity/CompanyInfo.java
 * @Description: 企业内容实体，对应 company_info 表（见 docs/12、docs/13 §2.1）
 */
package com.taiji.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("company_info")
public class CompanyInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String section;
    private String title;
    private String content;
    private String cover;
    private String seoTitle;
    private String seoKeywords;
    private String seoDesc;
    private Integer sort;
    private Integer status;
    private String createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;
}
