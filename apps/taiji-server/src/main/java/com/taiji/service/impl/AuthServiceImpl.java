/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/service/impl/AuthServiceImpl.java
 * @Description: 认证服务实现：BCrypt 校验 + JWT 签发 + Redis 黑名单（见 docs/14 §4）
 */
package com.taiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taiji.entity.AdminRole;
import com.taiji.entity.AdminUser;
import com.taiji.mapper.AdminRoleMapper;
import com.taiji.mapper.AdminUserMapper;
import com.taiji.security.JwtUtil;
import com.taiji.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AdminUserMapper userMapper;
    private final AdminRoleMapper roleMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Redis 在本地(local) profile 可能未启用，置为可选，logout 时空安全处理
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthServiceImpl(AdminUserMapper userMapper, AdminRoleMapper roleMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String username, String password) {
        // 业务规则：账号不存在或状态禁用(0)直接拒绝；密码用 BCrypt 密文比对（init.sql 初始账号为 BCrypt 密文）
        AdminUser user = userMapper.selectOne(
                new LambdaQueryWrapper<AdminUser>().eq(AdminUser::getUsername, username));
        if (user == null || user.getStatus() == 0 || !encoder.matches(password, user.getPassword())) {
            // 审计：登录失败不记录具体原因，避免泄露账号是否存在（防用户枚举）
            log.warn("[audit] 登录失败 username={}", username);
            throw new com.taiji.common.BusinessException("用户名或密码错误");
        }
        AdminRole role = roleMapper.selectById(user.getRoleId());
        String roleName = role != null ? role.getName() : "editor";
        // 签发 JWT，并将角色写入声明（RBAC 简版 admin/editor，见 docs/13 §9）
        log.info("[audit] 登录成功 username={} role={}", username, roleName);
        return jwtUtil.generate(username + "|" + roleName);
    }

    @Override
    public void logout(String token) {
        // 登出即把 token 写入 Redis 黑名单，存活至 JWT 自然过期覆盖 2h 窗口
        // 本地无 Redis 时直接跳过（logout 仅影响即时失效能力）
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set("jwt:blacklist:" + token, "1", jwtExpiration, TimeUnit.MILLISECONDS);
        }
    }
}
