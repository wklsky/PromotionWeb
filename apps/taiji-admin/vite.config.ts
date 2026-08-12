/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-admin/vite.config.ts
 * @Description: CMS 后台 Vite 构建配置，接入 Element Plus 自动导入与共享契约包
 */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import { fileURLToPath, URL } from 'node:url';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      // 业务代码统一使用 ~/ 别名指向 src（与 tsconfig paths 保持一致），否则 Vite 运行时无法解析 ~/api/*
      '~': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    port: 3200,
    // 开发期将 /api 反代到 Spring Boot(:8080)，避免浏览器直连 CORS/404（见 docs/14 部署）
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
});
