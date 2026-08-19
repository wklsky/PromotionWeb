/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-19 15:00
 * @FilePath: apps/taiji-admin/src/main.ts
 * @Description: CMS 应用入口，挂载 Pinia、路由。Element Plus 采用具名按需导入 +
 *               unplugin-element-plus 按需样式，不再全量注册（见 vite.config.ts）。
 */
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './styles.css';

createApp(App).use(createPinia()).use(router).mount('#app');
