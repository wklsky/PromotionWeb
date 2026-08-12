/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/pages/[hall].vue * @Description:
三大馆动态路由页：统一四段式叙事 + 案例瀑布流（见 docs/03 §4、docs/05 §6.1） */
<script setup lang="ts">
import MasonryGrid from '~/components/common/MasonryGrid.vue';

// 路由前缀命中 ROUTE_THEME_MAP，进入即切换对应馆主题色（见 docs/09 §6 useTheme）
const route = useRoute();
const hallNameMap: Record<string, string> = {
  dragon: '龙虎馆',
  panda: '熊猫馆',
  kunpeng: '鲲鹏馆',
};
const hallName = hallNameMap[route.params.hall as string] ?? '太极馆';

// 示例案例卡片，真实数据应由 api/content 拉取（见 docs/13）
const cases = [
  { id: 1, title: '案例一', cover: '' },
  { id: 2, title: '案例二', cover: '' },
  { id: 3, title: '案例三', cover: '' },
  { id: 4, title: '案例四', cover: '' },
];
</script>

<template>
  <section class="py-16 px-6">
    <h1 class="text-3xl font-bold mb-8">{{ hallName }} · 业务与案例</h1>
    <div class="space-y-12">
      <div>
        <h2 class="text-xl font-semibold mb-2">业务</h2>
        <p class="opacity-80">（四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作）</p>
      </div>
      <div>
        <h2 class="text-xl font-semibold mb-2">案例展示</h2>
        <MasonryGrid :items="cases" @select="(item) => console.log('select', item.id)">
          <template #default="{ item }">
            <div class="p-4">
              <div class="aspect-video bg-black/20 rounded mb-2" />
              <p>{{ (item as { title: string }).title }}</p>
            </div>
          </template>
        </MasonryGrid>
      </div>
    </div>
  </section>
</template>
