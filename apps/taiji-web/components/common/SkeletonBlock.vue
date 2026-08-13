<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-web/components/common/SkeletonBlock.vue
 * @Description: 通用骨架屏块（P0 基础组件层）。取数期占位，shimmer 流光动画，
 *               移动端/reduced-motion 自动降级为静态低对比占位（见 docs/16 §4.1）。
 -->
<script setup lang="ts">
withDefaults(
  defineProps<{
    /** 高度，支持任意 CSS 长度，默认 16px */
    height?: string;
    /** 宽度，默认 100% */
    width?: string;
    /** 圆角，默认跟随卡片圆角 */
    radius?: string;
    /** 多行文本骨架行数（>0 时渲染错位文本行，忽略 height） */
    lines?: number;
  }>(),
  { height: '16px', width: '100%', radius: 'var(--radius-sm)', lines: 0 },
);
</script>

<template>
  <div v-if="lines > 0" class="skeleton-lines" :style="{ width }" aria-hidden="true">
    <span
      v-for="n in lines"
      :key="n"
      class="skeleton skeleton--line"
      :style="{ width: n === lines ? '60%' : '100%', height: '14px' }"
    />
  </div>
  <span
    v-else
    class="skeleton"
    :style="{ height, width, borderRadius: radius }"
    aria-hidden="true"
  />
</template>

<style scoped>
.skeleton {
  display: block;
  background: linear-gradient(
    100deg,
    var(--c-bg-soft) 30%,
    color-mix(in srgb, var(--theme-primary) 14%, var(--c-bg-soft)) 50%,
    var(--c-bg-soft) 70%
  );
  background-size: 200% 100%;
  border-radius: var(--radius-sm);
  animation: skeleton-shimmer 1.4s ease-in-out infinite;
}
.skeleton-lines {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.skeleton--line {
  width: 100%;
}
@keyframes skeleton-shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
@media (prefers-reduced-motion: reduce) {
  .skeleton {
    animation: none;
  }
}
</style>
