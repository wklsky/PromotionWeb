/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-web/composables/useTheme.ts
 * @Description: 按路由设置 body[data-theme]，注入主题 CSS 变量并同步 3D 材质颜色
 */
import { ROUTE_THEME_MAP, THEME_PALETTES, type ThemeKey } from 'taiji-shared';

/**
 * 在 layouts/default.vue 的 onMounted 中调用，监听路由变化切换主题。
 * 进入 /dragon → 黑金，/panda → 黑白，/kunpeng → 天蓝白（见 docs/05 §2.2）。
 *
 * 主题色板以 THEME_PALETTES 为单一来源，由本函数写入 :root 的 CSS 变量；
 * main.css 仅保留 :root 默认（黑金），不再用 body[data-theme] 重复定义，避免优先级冲突。
 * 同时按底色深浅切换中性色（浅底馆需翻转卡片/边框/文字对比度，否则白底上卡片不可见）。
 */
export function useTheme(): void {
  const route = useRoute();

  // 中性色两套：深色底（黑金/黑白）与浅色底（天蓝白），按 palette.base 判断
  const setNeutrals = (light: boolean): void => {
    const root = document.documentElement.style;
    if (light) {
      root.setProperty('--c-bg-soft', 'rgba(10, 26, 63, 0.04)');
      root.setProperty('--c-bg-card', 'rgba(255, 255, 255, 0.7)');
      root.setProperty('--c-border', 'rgba(10, 26, 63, 0.12)');
      root.setProperty('--c-border-strong', 'rgba(10, 26, 63, 0.2)');
      root.setProperty('--c-muted', 'rgba(10, 26, 63, 0.6)');
      root.setProperty('--shadow-card', '0 8px 30px rgba(10, 26, 63, 0.12)');
    } else {
      root.setProperty('--c-bg-soft', 'rgba(255, 255, 255, 0.04)');
      root.setProperty('--c-bg-card', 'rgba(255, 255, 255, 0.06)');
      root.setProperty('--c-border', 'rgba(255, 255, 255, 0.1)');
      root.setProperty('--c-border-strong', 'rgba(255, 255, 255, 0.18)');
      root.setProperty('--c-muted', 'rgba(245, 245, 245, 0.6)');
      root.setProperty('--shadow-card', '0 8px 30px rgba(0, 0, 0, 0.35)');
    }
  };

  // 业务规则：ROUTE_THEME_MAP 含 '/' 前缀会匹配所有路径，若按插入顺序 find 则 '/'
  // 永远先命中，导致 /dragon、/panda、/kunpeng 三馆主题被错误降级为 default。
  // 因此按 key 长度降序匹配，优先命中更具体的路由前缀，最后才回退到 '/'。
  const sortedEntries = Object.entries(ROUTE_THEME_MAP).sort(
    (a, b) => b[0].length - a[0].length,
  );

  const apply = (path: string): void => {
    const theme = (sortedEntries.find(([prefix]) =>
      path.startsWith(prefix),
    )?.[1] ?? 'dragon') as ThemeKey;
    document.body.dataset.theme = theme;
    const palette = THEME_PALETTES[theme];
    const root = document.documentElement.style;
    root.setProperty('--theme-base', palette.base);
    root.setProperty('--theme-primary', palette.primary);
    root.setProperty('--theme-accent', palette.accent);
    root.setProperty('--theme-text', palette.text);
    // 浅底判定：base 接近白色（kunpeng 为 #F5F5F5）时需翻转中性色对比度
    setNeutrals(palette.base.toLowerCase() === '#f5f5f5');
  };

  watch(
    () => route.path,
    (path) => apply(path),
    { immediate: true },
  );
}
