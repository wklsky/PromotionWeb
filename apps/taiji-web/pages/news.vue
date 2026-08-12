/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:40 * @FilePath: apps/taiji-web/pages/news.vue * @Description:
新闻中心，瀑布流展示已发布新闻（见 docs/03 §4、docs/09 §7） */
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import MasonryGrid from '~/components/common/MasonryGrid.vue';
import type { MasonryItem } from '~/components/common/MasonryGrid.vue';
import { fetchNews } from '~/api/content';
import type { NewsListVO } from 'taiji-shared';

const news = ref<MasonryItem[]>([]);
const loading = ref(false);
const errorMsg = ref('');

// 后端 GET /api/news 返回 Result<PageResult<News>>，列表字段与 NewsListVO 对齐（见 docs/13 §2.2）
// 仅展示已发布(status=1)新闻；映射为瀑布流卡片，cover 缺失时由卡片兜底占位
async function loadNews(page = 1, size = 20) {
  loading.value = true;
  errorMsg.value = '';
  try {
    const res = await fetchNews({ page, size });
    if (res.code === 0 && res.data) {
      news.value = res.data.list.map((n: NewsListVO) => ({
        id: n.id,
        title: n.title,
        cover: n.cover,
        category: n.category,
      }));
    } else {
      errorMsg.value = res.message || '加载新闻失败';
    }
  } catch (e) {
    errorMsg.value = (e as Error).message;
  } finally {
    loading.value = false;
  }
}

onMounted(() => loadNews());
</script>

<template>
  <section class="py-16 px-6">
    <h1 class="text-3xl font-bold mb-8">新闻中心</h1>
    <p v-if="errorMsg" class="text-red-500 mb-4">{{ errorMsg }}</p>
    <p v-else-if="loading" class="opacity-60 mb-4">加载中…</p>
    <MasonryGrid v-else :items="news">
      <template #default="{ item }">
        <div class="p-4">
          <!-- cover 缺失时以纯色块兜底，避免瀑布流出现破图（见 docs/09 §7） -->
          <div v-if="item.cover" class="aspect-video bg-black/20 rounded mb-2 overflow-hidden">
            <img
              :src="String(item.cover)"
              :alt="String(item.title)"
              class="w-full h-full object-cover"
            />
          </div>
          <div v-else class="aspect-video bg-black/20 rounded mb-2" />
          <p>{{ item.title }}</p>
        </div>
      </template>
    </MasonryGrid>
  </section>
</template>
