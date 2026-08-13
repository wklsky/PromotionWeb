/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/api/request.spec.ts
 * @Description: 统一 http 封装单测（见 docs/16 §4.4）。覆盖响应解包、Token 附加、401 清凭证。
 *               通过 vi.mock('axios') 注入假实例，拦截 request/response 拦截器进行断言。
 */
import { describe, it, expect, vi, beforeEach } from 'vitest';

// 在 mock 工厂生效前准备可共享的假实例与拦截器引用
const h = vi.hoisted(() => {
  const requestInterceptors: ((c: any) => any)[] = [];
  const responseFulfilled: ((r: any) => any)[] = [];
  const responseRejected: ((e: any) => any)[] = [];
  const fakeInstance = {
    interceptors: {
      request: { use: (fn: any) => requestInterceptors.push(fn) },
      response: {
        use: (onF: any, onR: any) => {
          responseFulfilled.push(onF);
          responseRejected.push(onR);
        },
      },
    },
    get: vi.fn(),
    post: vi.fn(),
    put: vi.fn(),
    delete: vi.fn(),
  };
  return { requestInterceptors, responseFulfilled, responseRejected, fakeInstance };
});

vi.mock('axios', () => ({ default: { create: () => h.fakeInstance } }));

import { http, TOKEN_KEY } from '~/api/request';

describe('request 拦截器与解包', () => {
  beforeEach(() => {
    localStorage.clear();
    // jsdom 下直接赋值 window.location.href 会触发导航告警，改为可写桩
    Object.defineProperty(window, 'location', {
      value: { href: '' },
      writable: true,
      configurable: true,
    });
    h.fakeInstance.get.mockReset();
    h.fakeInstance.post.mockReset();
    h.fakeInstance.put.mockReset();
    h.fakeInstance.delete.mockReset();
  });

  it('get 返回已解包的 ApiResponse（非双重解包）', async () => {
    h.fakeInstance.get.mockResolvedValue({
      code: 0,
      message: 'ok',
      data: { list: [{ id: 1, title: 't' }], total: 1, page: 1, size: 10, pages: 1 },
    });
    const r = await http.get<{ list: any[]; total: number }>('/news');
    expect(r.code).toBe(0);
    expect(r.data.total).toBe(1);
  });

  it('post 透传 data 并解包', async () => {
    h.fakeInstance.post.mockResolvedValue({ code: 0, message: 'ok', data: 7 });
    const r = await http.post<number>('/news', { title: 'x' });
    expect(r.data).toBe(7);
  });

  it('请求拦截器在存在 token 时附加 Bearer', async () => {
    localStorage.setItem(TOKEN_KEY, 'abc');
    const headers = { set: vi.fn() };
    const cfg = { headers };
    const out = await h.requestInterceptors[0](cfg);
    expect(headers.set).toHaveBeenCalledWith('Authorization', 'Bearer abc');
    expect(out).toBe(cfg);
  });

  it('请求拦截器在无 token 时不附加', async () => {
    const headers = { set: vi.fn() };
    await h.requestInterceptors[0]({ headers });
    expect(headers.set).not.toHaveBeenCalled();
  });

  it('响应拦截器 fulfilled 直接返回 response.data', () => {
    const r = h.responseFulfilled[0]({ data: { code: 0, message: 'ok', data: null } });
    expect(r).toEqual({ code: 0, message: 'ok', data: null });
  });

  it('401 响应：拒绝并清除本地 token、跳转登录', async () => {
    localStorage.setItem(TOKEN_KEY, 'abc');
    const err = { response: { status: 401, data: { message: 'unauthorized' } }, message: 'unauthorized' };
    await expect(h.responseRejected[0](err)).rejects.toThrow('unauthorized');
    expect(localStorage.getItem(TOKEN_KEY)).toBeNull();
    expect(window.location.href).toBe('/login');
  });

  it('非 401 错误：拒绝但保留本地 token', async () => {
    localStorage.setItem(TOKEN_KEY, 'abc');
    const err = { response: { status: 500, data: { message: 'boom' } }, message: 'boom' };
    await expect(h.responseRejected[0](err)).rejects.toThrow('boom');
    expect(localStorage.getItem(TOKEN_KEY)).toBe('abc');
  });
});
