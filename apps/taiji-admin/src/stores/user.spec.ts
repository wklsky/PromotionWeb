/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/stores/user.spec.ts
 * @Description: 用户状态仓库单测（见 docs/16 §4.4）。校验登录态写入/恢复/登出。
 */
import { describe, it, expect, beforeEach } from 'vitest';
import { setActivePinia, createPinia } from 'pinia';
import { useUserStore } from '~/stores/user';
import { TOKEN_KEY } from '~/api/request';
import type { LoginResultVO } from 'taiji-shared';

const session: LoginResultVO = { token: 'tok-123', role: 'admin', username: 'admin' };

describe('useUserStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
    localStorage.clear();
  });

  it('初始无登录态', () => {
    const store = useUserStore();
    expect(store.token).toBe('');
    expect(store.isLoggedIn()).toBe(false);
  });

  it('setSession 写入状态并持久化 token', () => {
    const store = useUserStore();
    store.setSession(session);
    expect(store.token).toBe('tok-123');
    expect(store.user?.username).toBe('admin');
    expect(store.isLoggedIn()).toBe(true);
    expect(localStorage.getItem(TOKEN_KEY)).toBe('tok-123');
  });

  it('logout 清除状态与本地凭证', () => {
    const store = useUserStore();
    store.setSession(session);
    store.logout();
    expect(store.token).toBe('');
    expect(store.user).toBeNull();
    expect(store.isLoggedIn()).toBe(false);
    expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
  });

  it('刷新后从本地恢复登录态', () => {
    const a = useUserStore();
    a.setSession(session);
    // 新 pinia 模拟"刷新"：store 重新实例化，应从 localStorage 恢复
    setActivePinia(createPinia());
    const b = useUserStore();
    expect(b.token).toBe('tok-123');
    expect(b.isLoggedIn()).toBe(true);
  });
});
