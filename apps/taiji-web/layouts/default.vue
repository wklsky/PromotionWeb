/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/layouts/default.vue * @Description:
默认布局，挂载主题切换并渲染公共头部/页脚 */
<script setup lang="ts">
import { onMounted } from 'vue';
import { useTheme } from '~/composables/useTheme';

// 进入布局即启用按路由的主题切换（黑金/黑白/天蓝白）
onMounted(() => useTheme());
</script>

<template>
  <div
    class="min-h-screen"
    :style="{ backgroundColor: 'var(--theme-base)', color: 'var(--theme-text)' }"
  >
    <header class="px-6 py-4">太极馆 · 企业官网</header>
    <main>
      <slot />
    </main>
    <footer class="px-6 py-4 text-center">© 太极馆</footer>
  </div>
</template>
