/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/controller/AuthController.java
 * @Description: 认证接口：登录签发 JWT、登出拉黑（见 docs/13 §2.8、docs/14 §4）
 */
package com.taiji.controller;

import com.taiji.common.Result;
import com.taiji.security.JwtUtil;
import com.taiji.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> body) {
        // 返回结构对齐 taiji-shared.LoginResultVO：{ token, role, username }
        String token = authService.login(body.get("username"), body.get("password"));
        // 角色从 JWT 声明解析（签发格式 "username|role"），不硬编码，保证 RBAC 简版 admin/editor 正确回传
        String[] principal = jwtUtil.parsePrincipal(token);
        String role = principal.length > 1 ? principal[1] : "editor";
        return Result.success(Map.of("token", token, "role", role, "username", principal[0]));
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            authService.logout(token.substring(7));
        }
        return Result.success(null);
    }
}
