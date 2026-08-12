/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-web/nuxt.config.ts
 * @Description: Nuxt3 官网工程配置：SSR、Tailwind、Pinia、共享契约包接入
 */
export default defineNuxtConfig({
  // 启用 SSR 以满足企业官网 SEO 与首屏直出诉求（见 docs/07 §2）
  ssr: true,
  modules: ['@nuxtjs/tailwindcss', '@pinia/nuxt'],
  css: ['~/assets/styles/main.css'],
  // 共享契约包通过 workspace 协议本地接入，构建期由 Nuxt 自动打包
  build: {
    transpile: ['taiji-shared'],
  },
  // 三大馆主题色在运行时由 useTheme 写入 body[data-theme]，此处仅声明基础变量文件
  app: {
    head: {
      title: '太极馆 · 企业官网',
      htmlAttrs: { lang: 'zh-CN' },
    },
  },
  // 开发期将 /api 反代到 Spring Boot(:8080)，避免浏览器直连 CORS/404（见 docs/14 部署）
  nitro: {
    routeRules: {
      '/api/**': { proxy: 'http://localhost:8080/**' },
    },
  },
});
