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

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> body) {
        // 返回结构对齐 taiji-shared.LoginResultVO：{ token, role, username }
        // TODO: AuthServiceImpl.login 当前抛 UnsupportedOperationException，需接入 AdminUserMapper + BCrypt
        String token = authService.login(body.get("username"), body.get("password"));
        return Result.success(Map.of("token", token, "role", "admin", "username", body.get("username")));
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
