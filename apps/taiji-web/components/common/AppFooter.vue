/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/components/common/AppFooter.vue
 * @Description: 官网页脚（见 docs/05 §3），含品牌、导航与版权信息，主题色联动。
 */
<script setup lang="ts">
const year = new Date().getFullYear();
const links = [
  { to: '/news', label: '新闻动态' },
  { to: '/dragon', label: '龙虎馆' },
  { to: '/panda', label: '熊猫馆' },
  { to: '/kunpeng', label: '鲲鹏馆' },
  { to: '/about', label: '关于我们' },
  { to: '/contact', label: '联系我们' },
];
</script>

<template>
  <footer class="site-footer">
    <div class="container site-footer__inner">
      <div class="site-footer__brand">
        <span class="brand__mark" aria-hidden="true">太</span>
        <div>
          <p class="site-footer__title">太极馆</p>
          <p class="site-footer__slogan">刚柔并济 · 和合共生</p>
        </div>
      </div>
      <nav class="site-footer__nav" aria-label="页脚导航">
        <NuxtLink v-for="l in links" :key="l.to" :to="l.to" class="site-footer__link">
          {{ l.label }}
        </NuxtLink>
      </nav>
    </div>
    <div class="container site-footer__bottom">
      <span>© {{ year }} 太极馆 · 企业官网</span>
      <span>以柔克刚 · 以静制动</span>
    </div>
  </footer>
</template>

<style scoped>
.site-footer {
  margin-top: 64px;
  border-top: 1px solid var(--c-border);
  background: color-mix(in srgb, var(--theme-base) 80%, var(--theme-primary) 4%);
}
.site-footer__inner {
  display: flex;
  flex-wrap: wrap;
  gap: 32px;
  justify-content: space-between;
  align-items: center;
  padding-block: 40px;
}
.site-footer__brand {
  display: flex;
  align-items: center;
  gap: 12px;
}
.brand__mark {
  display: grid;
  place-items: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--theme-primary);
  color: var(--theme-base);
  font-weight: 700;
}
.site-footer__title {
  margin: 0;
  font-weight: 700;
  font-size: 18px;
}
.site-footer__slogan {
  margin: 2px 0 0;
  font-size: 13px;
  color: var(--c-muted);
}
.site-footer__nav {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 20px;
}
.site-footer__link {
  color: var(--c-muted);
  font-size: 14px;
  transition: color 200ms var(--ease);
}
.site-footer__link:hover {
  color: var(--theme-primary);
}
.site-footer__bottom {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px;
  padding-block: 18px;
  border-top: 1px solid var(--c-border);
  font-size: 13px;
  color: var(--c-muted);
}
</style>
