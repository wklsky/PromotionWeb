/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-19 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 14:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/common/Constants.java
 * @Description: 全局业务常量。集中收口状态枚举/类型枚举等魔法值，消除散落在各 Service 的字面量。
 */
package com.taiji.common;

/** 发布状态：与 docs/12-13 约定一致，news/job/content 共用 */
public final class Constants {
    private Constants() {}

    /** 内容下线/草稿 */
    public static final int STATUS_OFFLINE = 0;
    /** 内容已发布/上线 */
    public static final int STATUS_PUBLISHED = 1;
}
