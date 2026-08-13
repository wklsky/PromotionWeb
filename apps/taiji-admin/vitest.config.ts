/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/vitest.config.ts
 * @Description: CMS 单元测试配置（见 docs/16 §4.4「测试代码业务逻辑」）。
 *               jsdom 环境 + @vitejs/plugin-vue 编译 .vue + ~/ @ 别名与 vite.config 对齐。
 */
import { defineConfig } from 'vitest/config';
import vue from '@vitejs/plugin-vue';
import { fileURLToPath, URL } from 'node:url';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '~': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  test: {
    environment: 'jsdom',
    globals: true,
    include: ['src/**/*.spec.ts'],
  },
});
