/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/security/SecurityConfig.java
 * @Description: Spring Security 配置：放行登录/公开只读/文档，写操作与后台需 JWT（见 docs/14 §4）
 */
package com.taiji.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // 登录与 Swagger/OpenAPI 文档公开（springdoc 路径，见 docs/14）
                .requestMatchers(
                    "/api/auth/login",
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**"
                ).permitAll()
                // 官网公开只读接口放行（无需 JWT，见 docs/13 只读端点）
                // 注意：requestMatchers(String... patterns) 会把所有字符串当 URL 模式，
                // 必须显式使用 HttpMethod.GET 才能按方法+路径放行。
                .requestMatchers(HttpMethod.GET, "/api/news").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/content/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/media/**").permitAll()
                // RBAC 落地：内容写操作需认证且具备 admin/editor 角色（editor 负责内容维护，见 docs/13 §9）。
                // hasAnyRole 会自动拼接 ROLE_ 前缀，与 JwtAuthenticationFilter 注入的权威一致。
                .requestMatchers(HttpMethod.POST, "/api/news", "/api/jobs", "/api/content", "/api/media").hasAnyRole("admin", "editor")
                .requestMatchers(HttpMethod.PUT, "/api/news/**", "/api/jobs/**", "/api/content/**", "/api/media/**").hasAnyRole("admin", "editor")
                .requestMatchers(HttpMethod.DELETE, "/api/news/**", "/api/jobs/**", "/api/content/**", "/api/media/**").hasAnyRole("admin", "editor")
                // 其余（写操作 / CMS 后台 / 未知端点）需认证（RBAC 简版 admin/editor，见 docs/13 §9）
                .anyRequest().hasAnyRole("admin", "editor")
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
