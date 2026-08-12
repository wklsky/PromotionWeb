/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: packages/taiji-shared/src/theme.ts
 * @Description: 三大馆主题色板定义。与 docs/05-UI视觉设计规范.md §2.2 一致。
 *               仅切换色彩，布局/风格(新东方科技风)三馆统一。组件须引用 CSS 变量而非硬编码。
 */

/** 主题标识，与路由一一对应 */
export const THEMES = ['default', 'dragon', 'panda', 'kunpeng'] as const;
export type ThemeKey = (typeof THEMES)[number];

/** 各主题色板（CSS 变量值），对应 05 §2.2 主调 */
export interface ThemePalette {
  /** 主色 */
  primary: string;
  /** 强调色 */
  accent: string;
  /** 背景色 */
  base: string;
  /** 文字色 */
  text: string;
}

export const THEME_PALETTES: Record<ThemeKey, ThemePalette> = {
  // 首页/默认：黑金（与全站基础色一致）
  default: {
    primary: '#D4AF37',
    accent: '#D4AF37',
    base: '#080808',
    text: '#F5F5F5',
  },
  // 龙虎馆：黑金主调
  dragon: {
    primary: '#D4AF37',
    accent: '#D4AF37',
    base: '#080808',
    text: '#F5F5F5',
  },
  // 熊猫馆：黑白主调（黑底白字）
  panda: {
    primary: '#F5F5F5',
    accent: '#E5E5E5',
    base: '#080808',
    text: '#F5F5F5',
  },
  // 鲲鹏馆：天蓝白主调（白底深蓝字）
  kunpeng: {
    primary: '#1677FF',
    accent: '#1677FF',
    base: '#F5F5F5',
    text: '#0A1A3F',
  },
};

/** 路由 → 主题映射 */
export const ROUTE_THEME_MAP: Record<string, ThemeKey> = {
  '/': 'default',
  '/about': 'default',
  '/dragon': 'dragon',
  '/panda': 'panda',
  '/kunpeng': 'kunpeng',
  '/news': 'default',
  '/jobs': 'default',
  '/contact': 'default',
};
