/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/pages/news.vue * @Description:
新闻中心，瀑布流展示新闻卡片（见 docs/03 §4、docs/09 §7） */
<script setup lang="ts">
import { ref } from 'vue';
import MasonryGrid from '~/components/common/MasonryGrid.vue';
import type { MasonryItem } from '~/components/common/MasonryGrid.vue';

// 示例数据；真实场景由 api/content.fetchNews 拉取并映射为 MasonryItem[]
const news = ref<MasonryItem[]>(
  Array.from({ length: 6 }, (_, i) => ({ id: i + 1, title: `新闻 ${i + 1}` })),
);
</script>

<template>
  <section class="py-16 px-6">
    <h1 class="text-3xl font-bold mb-8">新闻中心</h1>
    <MasonryGrid :items="news">
      <template #default="{ item }">
        <div class="p-4">
          <div class="aspect-video bg-black/20 rounded mb-2" />
          <p>{{ (item as { title: string }).title }}</p>
        </div>
      </template>
    </MasonryGrid>
  </section>
</template>
