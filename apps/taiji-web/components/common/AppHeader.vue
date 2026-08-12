/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/components/common/AppHeader.vue
 * @Description: 官网毛玻璃固定导航栏（见 docs/05 §3）。含品牌、主导航与三大馆入口，
 *               当前路由高亮联动 useTheme 主题色。
 */
<script setup lang="ts">
import { computed } from 'vue';

interface NavItem {
  /** 路由路径 */
  to: string;
  /** 导航文案 */
  label: string;
  /** 是否属于三大馆主题入口（高亮联动主题色） */
  hall?: boolean;
}

const navs: NavItem[] = [
  { to: '/', label: '首页' },
  { to: '/dragon', label: '龙虎馆', hall: true },
  { to: '/panda', label: '熊猫馆', hall: true },
  { to: '/kunpeng', label: '鲲鹏馆', hall: true },
  { to: '/news', label: '新闻动态' },
  { to: '/about', label: '关于我们' },
  { to: '/jobs', label: '加入我们' },
  { to: '/contact', label: '联系我们' },
];

const route = useRoute();
// 当前激活项：精确匹配首页，其余前缀匹配（如 /dragon 命中龙虎馆）
const activeTo = computed(() => {
  const path = route.path;
  if (path === '/') return '/';
  return navs.find((n) => n.to !== '/' && path.startsWith(n.to))?.to ?? '';
});

// 移动端菜单开合（默认收起，避免遮挡 Hero）
const menuOpen = ref(false);
const toggleMenu = (): void => {
  menuOpen.value = !menuOpen.value;
};
</script>

<template>
  <header class="site-header">
    <div class="container site-header__inner">
      <NuxtLink to="/" class="brand" aria-label="太极馆首页">
        <span class="brand__mark" aria-hidden="true">太</span>
        <span class="brand__name">太极馆</span>
      </NuxtLink>

      <button class="nav-toggle" :aria-expanded="menuOpen" aria-label="菜单" @click="toggleMenu">
        <span /><span /><span />
      </button>

      <nav class="nav" :class="{ 'nav--open': menuOpen }">
        <NuxtLink
          v-for="item in navs"
          :key="item.to"
          :to="item.to"
          class="nav__link"
          :class="{
            'nav__link--active': activeTo === item.to,
            'nav__link--hall': item.hall,
          }"
          @click="menuOpen = false"
        >
          {{ item.label }}
        </NuxtLink>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.site-header {
  position: sticky;
  top: 0;
  z-index: 50;
  background: color-mix(in srgb, var(--theme-base) 72%, transparent);
  backdrop-filter: blur(14px) saturate(140%);
  -webkit-backdrop-filter: blur(14px) saturate(140%);
  border-bottom: 1px solid var(--c-border);
}
.site-header__inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}
.brand {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-weight: 700;
}
.brand__mark {
  display: grid;
  place-items: center;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: var(--theme-primary);
  color: var(--theme-base);
  font-size: 17px;
  box-shadow: var(--shadow-glow);
}
.brand__name {
  font-size: 18px;
  letter-spacing: 0.08em;
}
.nav {
  display: flex;
  align-items: center;
  gap: 4px;
}
.nav__link {
  position: relative;
  padding: 8px 14px;
  border-radius: 999px;
  font-size: 15px;
  color: var(--c-muted);
  transition: color 200ms var(--ease), background 200ms var(--ease);
}
.nav__link:hover {
  color: var(--theme-text);
  background: var(--c-bg-soft);
}
.nav__link--active {
  color: var(--theme-base);
  background: var(--theme-primary);
  font-weight: 600;
}
.nav__link--hall.nav__link--active {
  box-shadow: var(--shadow-glow);
}
.nav-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  width: 40px;
  height: 40px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
}
.nav-toggle span {
  display: block;
  height: 2px;
  background: var(--theme-text);
  border-radius: 2px;
}
@media (max-width: 860px) {
  .nav-toggle {
    display: flex;
  }
  .nav {
    position: absolute;
    top: 64px;
    left: 0;
    right: 0;
    flex-direction: column;
    align-items: stretch;
    gap: 2px;
    padding: 12px clamp(16px, 4vw, 32px) 20px;
    background: color-mix(in srgb, var(--theme-base) 95%, transparent);
    border-bottom: 1px solid var(--c-border);
    transform: translateY(-12px);
    opacity: 0;
    pointer-events: none;
    transition: opacity 220ms var(--ease), transform 220ms var(--ease);
  }
  .nav--open {
    transform: translateY(0);
    opacity: 1;
    pointer-events: auto;
  }
  .nav__link {
    text-align: center;
  }
}
</style>
