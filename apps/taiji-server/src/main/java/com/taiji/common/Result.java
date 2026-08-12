/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/common/Result.java
 * @Description: 统一 API 响应包装，与 taiji-shared 的 ApiResponse 字段一致（见 docs/13）
 */
package com.taiji.common;

import java.io.Serializable;

/**
 * 统一 API 响应包装，与 taiji-shared 的 ApiResponse 字段一致（见 docs/13）。
 * 不依赖 Lombok，显式声明 getter/setter，避免 IDE/构建环境中 Lombok 注解处理器缺失导致编译失败。
 */
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 0;
        r.message = "success";
        r.data = data;
        return r;
    }

    /**
     * 失败响应工厂：code 非 0 表示失败（见 docs/13 §1 ApiResponse 契约）。
     * 直接字段赋值，不依赖 Lombok 生成的 setter。
     */
    public static <T> Result<T> fail(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        r.data = null;
        return r;
    }
}
