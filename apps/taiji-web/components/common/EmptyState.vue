<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-web/components/common/EmptyState.vue
 * @Description: 统一空态/错误态组件（P0 基础组件层）。替代 news/[hall]/jobs 内散落的 .state 写法，
 *               统一样式与语义，支持 loading/empty/error 三态（见 docs/16 §4.1）。
 -->
<script setup lang="ts">
withDefaults(
  defineProps<{
    /** 状态类型：loading 取数期 / empty 无数据 / error 出错 */
    type?: 'loading' | 'empty' | 'error';
    /** 主标题 */
    title?: string;
    /** 次要说明 */
    desc?: string;
    /** 是否显示内置 loading 骨架（type=loading 时默认显示） */
    skeleton?: boolean;
  }>(),
  { type: 'empty', title: '', desc: '', skeleton: false },
);
</script>

<template>
  <div class="empty-state" :class="`empty-state--${type}`" role="status">
    <div v-if="type === 'loading' && skeleton" class="empty-state__skeleton">
      <span class="empty-state__spin" aria-hidden="true" />
      <p v-if="title" class="empty-state__title">{{ title }}</p>
    </div>
    <template v-else>
      <span class="empty-state__icon" aria-hidden="true">
        {{ type === 'error' ? '⚠' : '◌' }}
      </span>
      <p class="empty-state__title">{{ title || (type === 'error' ? '加载失败' : '暂无内容') }}</p>
      <p v-if="desc" class="empty-state__desc">{{ desc }}</p>
    </template>
  </div>
</template>

<style scoped>
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: clamp(40px, 8vw, 72px);
  text-align: center;
  border: 1px dashed var(--c-border);
  border-radius: var(--radius);
  background: color-mix(in srgb, var(--c-bg-soft) 50%, transparent);
  color: var(--c-muted);
}
.empty-state--error {
  color: #ff8585;
  border-color: color-mix(in srgb, #ff8585 40%, transparent);
  background: color-mix(in srgb, #ff8585 8%, transparent);
}
.empty-state__icon {
  font-size: 34px;
  line-height: 1;
}
.empty-state--error .empty-state__icon {
  color: #ff8585;
}
.empty-state__title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--theme-text);
}
.empty-state--error .empty-state__title {
  color: #ff8585;
}
.empty-state__desc {
  margin: 0;
  font-size: 14px;
  max-width: 42ch;
}
.empty-state__skeleton {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}
.empty-state__spin {
  width: 34px;
  height: 34px;
  border-radius: 50%;
  border: 3px solid var(--c-border-strong);
  border-top-color: var(--theme-primary);
  animation: empty-spin 0.8s linear infinite;
}
@keyframes empty-spin {
  to {
    transform: rotate(360deg);
  }
}
@media (prefers-reduced-motion: reduce) {
  .empty-state__spin {
    animation-duration: 2s;
  }
}
</style>
