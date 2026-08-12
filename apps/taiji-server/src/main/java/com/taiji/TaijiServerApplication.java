/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-server/src/main/java/com/taiji/TaijiServerApplication.java
 * @Description: Spring Boot 3 后端启动类（见 docs/14）
 */
package com.taiji;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.taiji.mapper")
public class TaijiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaijiServerApplication.class, args);
    }
}
