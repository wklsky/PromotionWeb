/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/AuthService.java
 * @Description: 认证服务接口，封装登录签发 JWT 与登出拉黑（见 docs/14 §4 安全流程）
 */
package com.taiji.service;

public interface AuthService {
    /**
     * 管理员登录，成功返回 JWT（2h 有效期，见 application.yml jwt.expiration）。
     */
    String login(String username, String password);

    /**
     * 登出：将当前 JWT 加入 Redis 黑名单，实现即时失效。
     */
    void logout(String token);
}
