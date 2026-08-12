/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-admin/src/router/index.ts
 * @Description: CMS 路由表（见 docs/11 功能模块）
 */
import { createRouter, createWebHistory } from 'vue-router';

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/news' },
    {
      path: '/login',
      component: () => import('@/views/Login.vue'),
    },
    {
      path: '/news',
      component: () => import('@/views/NewsManage.vue'),
    },
    {
      path: '/media',
      component: () => import('@/views/MediaLibrary.vue'),
    },
  ],
});
