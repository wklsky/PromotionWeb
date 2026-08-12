/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/security/JwtAuthenticationFilter.java
 * @Description: JWT 认证过滤器：校验 Bearer 头、黑名单、注入 SecurityContext（见 docs/14 §4）
 */
package com.taiji.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 本地(local) profile 禁用 Redis 自动配置，黑名单能力不可用，因此置为可选。
    // 与 AuthServiceImpl 保持一致：无 Redis 时跳过黑名单校验，仅依赖 JWT 自身有效期。
    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            // 黑名单校验：登出后的 token 在 Redis 中命中即拒绝（见 docs/14 §4）。
            // 容错：本地无 Redis(redisTemplate==null) 或 Redis 抖动(hasKey 返回 null) 时，
            // 不能因黑名单查不到就拒绝所有合法请求，降级为仅校验 JWT 有效期（fail-open），保证可用性。
            boolean blacklisted = redisTemplate != null
                    && Boolean.TRUE.equals(redisTemplate.hasKey("jwt:blacklist:" + token));
            if (!blacklisted && !jwtUtil.isExpired(token)) {
                String username = jwtUtil.parseUsername(token);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, null);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}
