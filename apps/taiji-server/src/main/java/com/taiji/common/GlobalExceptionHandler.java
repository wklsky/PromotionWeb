/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/common/GlobalExceptionHandler.java
 * @Description: 全局异常处理，统一错误响应（code 非 0 表示失败，见 docs/13 §1 ApiResponse）
 */
package com.taiji.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理：统一错误响应，同时强制打印完整堆栈，便于快速定位未处理异常根因。
 * 注意：NullPointerException 的 getMessage() 通常为 null，仅通过 message 无法定位问题。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 显式声明 SLF4J Logger，替代 @Slf4j 注解生成的 log 字段（避免 Lombok 处理器缺失导致编译失败）。
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBiz(BusinessException e) {
        return Result.fail(400, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        // 参数校验失败属于客户端输入错误，统一返回 400 + 首个字段错误文案，避免落到 500 通用异常。
        // 安全约束：仅返回字段级 message，不暴露整体校验结构，避免泄露字段命名约定。
        FieldError firstError = e.getBindingResult().getFieldErrors().get(0);
        String message = firstError != null ? firstError.getDefaultMessage() : "参数校验失败";
        return Result.fail(400, message);
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuth(AuthenticationException e) {
        return Result.fail(401, "未认证或令牌无效");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleDenied(AccessDeniedException e) {
        return Result.fail(403, "无权限访问");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        // 必须打印完整堆栈：NPE 等异常 message 为 null，仅通过 message 无法定位根因。
        // 安全约束：响应体不回传内部异常信息（避免暴露堆栈/SQL/路径等敏感细节），
        // 仅返回通用文案，具体根因依赖服务端日志定位。
        log.error("未处理异常：{} - {}", e.getClass().getName(), e.getMessage(), e);
        return Result.fail(500, "服务器内部错误，请联系管理员");
    }
}
