/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-admin/src/stores/user.ts
 * @Description: 后台用户状态（见 docs/13 §9）。持有登录态与用户信息，登出清除本地 token。
 *               token 存储键须与 Login.vue、router 守卫保持一致（localStorage 'token'）。
 */
import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { LoginResultVO } from 'taiji-shared';
// 复用 request 导出的 TOKEN_KEY，避免登录态写入键与请求读取键不一致
// 导致登录后所有鉴权请求缺失 Authorization 头、返回 401（见 api/request.ts）
import { TOKEN_KEY } from '~/api/request';

export const useUserStore = defineStore('user', () => {
  // 登录态：优先从本地恢复（刷新后保持登录）
  const token = ref<string>(localStorage.getItem(TOKEN_KEY) ?? '');
  const user = ref<LoginResultVO | null>(null);

  function setSession(vo: LoginResultVO): void {
    token.value = vo.token;
    user.value = vo;
    localStorage.setItem(TOKEN_KEY, vo.token);
  }

  function logout(): void {
    token.value = '';
    user.value = null;
    // 仅清除登录态键，避免误删其它本地数据
    localStorage.removeItem(TOKEN_KEY);
  }

  /** 是否持有有效登录态（后端鉴权由拦截器附加 Bearer） */
  const isLoggedIn = (): boolean => Boolean(token.value);

  return { token, user, setSession, logout, isLoggedIn };
});
