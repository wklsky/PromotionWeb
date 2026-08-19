/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 15:00
 * @FilePath: apps/taiji-admin/vite.config.ts
 * @Description: CMS 后台 Vite 构建配置，接入 Element Plus 按需样式 + 第三方分包与共享契约包
 */
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import ElementPlus from 'unplugin-element-plus/vite';
import { fileURLToPath, URL } from 'node:url';

export default defineConfig({
  plugins: [
    vue(),
    // 业务文件已用具名导入（import { ElButton } from 'element-plus'），此插件按需注入对应组件样式，
    // 取代 main.ts 全量引入 element-plus/dist/index.css，主包 CSS 体积从 ~353KB 大幅下降。
    ElementPlus({}),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      // 业务代码统一使用 ~/ 别名指向 src（与 tsconfig paths 保持一致），否则 Vite 运行时无法解析 ~/api/*
      '~': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  build: {
    // 第三方依赖拆分为独立 chunk，避免主包臃肿、利于浏览器长缓存命中（见 docs/14 部署优化）
    rollupOptions: {
      output: {
        manualChunks: {
          vue: ['vue', 'vue-router', 'pinia'],
          'element-plus': ['element-plus'],
          axios: ['axios'],
        },
      },
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
