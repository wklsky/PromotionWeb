/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:40 * @FilePath: apps/taiji-web/pages/[hall].vue * @Description:
三大馆动态路由页：业务介绍（接 content 接口）+ 案例瀑布流（见 docs/03 §4、docs/05 §6.1） */
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import MasonryGrid from '~/components/common/MasonryGrid.vue';
import { fetchContent } from '~/api/content';
import type { CompanyInfoVO } from 'taiji-shared';

// 路由前缀命中 ROUTE_THEME_MAP，进入即切换对应馆主题色（见 docs/09 §6 useTheme）
const route = useRoute();
const hallNameMap: Record<string, string> = {
  dragon: '龙虎馆',
  panda: '熊猫馆',
  kunpeng: '鲲鹏馆',
};
const hallName = hallNameMap[route.params.hall as string] ?? '太极馆';

// 馆内业务介绍：后端 ContentController.bySection 按 section 返回已启用内容（见 docs/13 §2.1）
const intro = ref<CompanyInfoVO[]>([]);
const errorMsg = ref('');

async function loadContent() {
  errorMsg.value = '';
  try {
    const res = await fetchContent(route.params.hall as string);
    if (res.code === 0 && res.data) {
      intro.value = res.data;
    } else {
      errorMsg.value = res.message || '加载内容失败';
    }
  } catch (e) {
    errorMsg.value = (e as Error).message;
  }
}

onMounted(() => loadContent());

// 案例展示：案例独立于 content 表，暂无后端对应表；以馆介绍 cover 拼装瀑布流占位（见 docs/03 §4）
const cases = ref<{ id: number; title: string; cover: string | null }[]>([]);
</script>

<template>
  <section class="py-16 px-6">
    <h1 class="text-3xl font-bold mb-8">{{ hallName }} · 业务与案例</h1>
    <p v-if="errorMsg" class="text-red-500 mb-4">{{ errorMsg }}</p>
    <div class="space-y-12">
      <div>
        <h2 class="text-xl font-semibold mb-2">业务</h2>
        <!-- 四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作（docs/03 §4） -->
        <div v-if="intro.length" class="space-y-4">
          <div v-for="block in intro" :key="block.id" class="theme-card p-4">
            <h3 class="font-semibold">{{ block.title }}</h3>
            <p class="opacity-80 mt-1">{{ block.content }}</p>
          </div>
        </div>
        <p v-else class="opacity-60">（四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作）</p>
      </div>
      <div>
        <h2 class="text-xl font-semibold mb-2">案例展示</h2>
        <MasonryGrid :items="cases">
          <template #default="{ item }">
            <div class="p-4">
              <div v-if="item.cover" class="aspect-video bg-black/20 rounded mb-2 overflow-hidden">
                <img :src="item.cover" :alt="item.title" class="w-full h-full object-cover" />
              </div>
              <div v-else class="aspect-video bg-black/20 rounded mb-2" />
              <p>{{ item.title }}</p>
            </div>
          </template>
        </MasonryGrid>
      </div>
    </div>
  </section>
</template>
