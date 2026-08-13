/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-admin/src/router/index.ts
 * @Description: CMS 路由表与鉴权守卫（见 docs/11 功能模块、docs/14 §4）
 */
import { createRouter, createWebHistory } from 'vue-router';
// 与 api/request.ts 共用同一 token 键，确保登录态判定与请求鉴权一致
import { TOKEN_KEY } from '~/api/request';

// 鉴权白名单：无需携带登录态即可访问的路由
const WHITE_LIST = ['/login'];

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      // 根路由同时承担后台布局（App.vue：侧边栏 + 顶栏 + 路由视图）与默认重定向。
      // 注意：path 为 '/' 的路由全局只能有一个，重定向须用 redirect 字段表达，
      // 不能再单独定义一条 { path: '/', redirect }，否则 vue-router 路由表冲突导致页面空白。
      path: '/',
      component: () => import('@/App.vue'),
      redirect: '/dashboard',
      children: [
        { path: 'dashboard', component: () => import('@/views/Dashboard.vue'), meta: { title: '数据概览' } },
        { path: 'news', component: () => import('@/views/NewsManage.vue'), meta: { title: '新闻管理' } },
        { path: 'jobs', component: () => import('@/views/JobsManage.vue'), meta: { title: '招聘管理' } },
        { path: 'media', component: () => import('@/views/MediaLibrary.vue'), meta: { title: '媒体库' } },
      ],
    },
    {
      // 登录页为独立顶级路由，避免被后台布局（侧边栏/顶栏）包裹
      path: '/login',
      component: () => import('@/views/Login.vue'),
    },
    {
      // 404 兜底：置于路由表末尾，匹配所有未命中路径（见 docs/16 §4.4）
      path: '/:pathMatch(.*)*',
      component: () => import('@/views/NotFound.vue'),
      meta: { title: '页面不存在' },
    },
  ],
});

// 全局前置守卫：未登录直接访问 /news、/media 等受保护页时，拦截回登录页，
// 避免无 token 状态下页面骨架先渲染、接口再 401 的割裂体验。
// token 存储键须与 api/request.ts、stores/user.ts 保持一致。
router.beforeEach((to) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token || WHITE_LIST.includes(to.path)) {
    return true;
  }
  return { path: '/login', query: { redirect: to.fullPath } };
});

export default router;
