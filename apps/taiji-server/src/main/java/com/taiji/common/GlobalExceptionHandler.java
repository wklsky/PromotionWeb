/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/common/GlobalExceptionHandler.java
 * @Description: 全局异常处理，统一错误响应（code 非 0 表示失败，见 docs/13 §1 ApiResponse）
 */
package com.taiji.common;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBiz(BusinessException e) {
        return fail(400, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuth(AuthenticationException e) {
        return fail(401, "未认证或令牌无效");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleDenied(AccessDeniedException e) {
        return fail(403, "无权限访问");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleOther(Exception e) {
        return fail(500, "服务器内部错误：" + e.getMessage());
    }

    private <T> Result<T> fail(int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(null);
        return r;
    }
}
