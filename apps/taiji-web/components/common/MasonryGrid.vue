/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/components/common/MasonryGrid.vue *
@Description: 通用瀑布流容器（见 docs/05 §6.1、docs/09 §7），CSS column 实现，SSR 友好 */
<script setup lang="ts">
// 瀑布流数据源，泛型卡片数组（业务对象需含唯一 id）
interface MasonryItem {
  id: number | string;
  [key: string]: unknown;
}

// 各断点列数：base 默认 1、md 默认 2、lg 默认 3（见 docs/05 §6.1）
withDefaults(
  defineProps<{
    items: MasonryItem[];
    columns?: { base?: number; md?: number; lg?: number };
    gap?: number;
  }>(),
  {
    items: () => [],
    columns: () => ({ base: 1, md: 2, lg: 3 }),
    gap: 16,
  },
);

// 点击卡片，回传该卡片数据对象
const emit = defineEmits<{
  (e: 'select', item: MasonryItem): void;
}>();
</script>

<template>
  <div class="masonry masonry--responsive" :style="{ columnGap: `${gap}px` }" role="list">
    <div
      v-for="item in items"
      :key="item.id"
      class="theme-card"
      role="listitem"
      @click="emit('select', item)"
    >
      <slot :item="item" />
    </div>
  </div>
</template>
