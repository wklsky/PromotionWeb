/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/common/PageResult.java
 * @Description: 分页结果结构，与 taiji-shared 的 PageResult<T> 字段一致（见 docs/13 §1）
 */
package com.taiji.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private List<T> list;
    private long total;
    private long page;
    private long size;
    private long pages;

    public static <T> PageResult<T> from(com.baomidou.mybatisplus.core.metadata.IPage<T> page) {
        PageResult<T> r = new PageResult<>();
        r.setList(page.getRecords());
        r.setTotal(page.getTotal());
        r.setPage(page.getCurrent());
        r.setSize(page.getSize());
        r.setPages(page.getPages());
        return r;
    }
}
