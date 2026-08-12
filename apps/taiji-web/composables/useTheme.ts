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
 */
export function useTheme(): void {
  const route = useRoute();

  const apply = (path: string): void => {
    const theme = (Object.entries(ROUTE_THEME_MAP).find(([prefix]) =>
      path.startsWith(prefix),
    )?.[1] ?? 'dragon') as ThemeKey;
    document.body.dataset.theme = theme;
    const palette = THEME_PALETTES[theme];
    document.documentElement.style.setProperty('--theme-base', palette.base);
    document.documentElement.style.setProperty('--theme-primary', palette.primary);
    document.documentElement.style.setProperty('--theme-accent', palette.accent);
    document.documentElement.style.setProperty('--theme-text', palette.text);
  };

  watch(
    () => route.path,
    (path) => apply(path),
    { immediate: true },
  );
}
