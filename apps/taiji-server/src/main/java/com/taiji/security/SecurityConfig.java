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
                .requestMatchers(
                    "GET", "/api/news",
                    "GET", "/api/content/**",
                    "GET", "/api/jobs",
                    "GET", "/api/media/**"
                ).permitAll()
                // 其余（写操作 / CMS 后台）需认证（RBAC 简版 admin/editor，见 docs/13 §9）
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
