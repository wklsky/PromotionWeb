/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/layouts/default.vue
 * @Description: 官网默认布局（见 docs/05 §3）：固定毛玻璃导航 + 路由视图 + 页脚。
 *               进入即按路由切换三大馆主题色（useTheme）。
 */
<script setup lang="ts">
import AppHeader from '~/components/common/AppHeader.vue';
import AppFooter from '~/components/common/AppFooter.vue';

// 客户端挂载后启动路由→主题监听（首屏默认 :root 黑金，避免 SSR 闪白）
onMounted(() => useTheme());
</script>

<template>
  <div class="site-shell">
    <AppHeader />
    <main class="site-main">
      <NuxtPage />
    </main>
    <AppFooter />
  </div>
</template>

<style scoped>
.site-shell {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
.site-main {
  flex: 1;
}
</style>
