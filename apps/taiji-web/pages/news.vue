<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/pages/news.vue
 * @Description: 新闻动态页（见 docs/13 §2.2、docs/16 §4.1/§4.3）。SSR 首屏直出列表，
 *               取数期骨架屏、空/错态统一组件；客户端分类筛选 + 分页；useSeoMeta + JSON-LD。
 -->
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import NewsCard from '~/components/common/NewsCard.vue';
import SkeletonBlock from '~/components/common/SkeletonBlock.vue';
import EmptyState from '~/components/common/EmptyState.vue';
import { fetchNews } from '~/api/content';
import { NEWS_CATEGORIES } from 'taiji-shared';
import type { NewsListVO } from 'taiji-shared';

useSeoMeta({
  title: '新闻动态 · 太极馆',
  description: '太极馆企业动态、行业资讯与技术文章。',
});

// SSR 首屏直出：useAsyncData 服务端即发起请求（见 docs/07 §2 SEO 诉求）。
const { data, pending, error } = await useAsyncData('news-all', async () => {
  const res = await fetchNews({ page: 1, size: 100 });
  if (res.code === 0 && res.data) return res.data.list as NewsListVO[];
  throw new Error(res.message || '加载新闻失败');
});

const all = ref<NewsListVO[]>(data.value ?? []);
watch(data, (v) => (all.value = v ?? []));

// 分类筛选（接 NEWS_CATEGORIES，见 docs/13 §9）
const activeCat = ref<string>('全部');
const cats = ['全部', ...NEWS_CATEGORIES];
const filtered = computed(() =>
  activeCat.value === '全部'
    ? all.value
    : all.value.filter((n) => n.category === activeCat.value),
);

// 客户端分页（后端列表为已发布内容，前端分页保证体验完整，见 docs/16 §4.3）
const pageSize = 9;
const page = ref(1);
const totalPages = computed(() => Math.max(1, Math.ceil(filtered.value.length / pageSize)));
const paged = computed(() => filtered.value.slice((page.value - 1) * pageSize, page.value * pageSize));
watch(filtered, () => (page.value = 1));

// JSON-LD：Article 列表结构化数据（SEO，见 docs/07 §3）
useHead({
  script: [
    {
      type: 'application/ld+json',
      innerHTML: JSON.stringify({
        '@context': 'https://schema.org',
        '@type': 'ItemList',
        itemListElement: all.value.slice(0, 10).map((n, i) => ({
          '@type': 'ListItem',
          position: i + 1,
          name: n.title,
        })),
      }),
    },
  ],
});
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="news-head">
        <h1 class="section-title">新闻动态</h1>
        <p class="section-sub">企业动态 · 行业资讯 · 技术文章</p>
      </header>

      <!-- 分类筛选 -->
      <div v-if="!pending && !error" class="news-filter">
        <button
          v-for="c in cats"
          :key="c"
          class="news-filter__chip"
          :class="{ 'news-filter__chip--active': activeCat === c }"
          @click="activeCat = c"
        >
          {{ c }}
        </button>
      </div>

      <!-- 取数期：骨架屏 -->
      <div v-if="pending" class="masonry masonry--responsive">
        <div v-for="n in 6" :key="n" class="theme-card news-skel">
          <SkeletonBlock height="180px" radius="0" />
          <div class="news-skel__body">
            <SkeletonBlock width="40%" height="20px" />
            <SkeletonBlock :lines="2" />
          </div>
        </div>
      </div>

      <EmptyState v-else-if="error" type="error" :title="error.message" :desc="'请稍后重试'" />
      <EmptyState
        v-else-if="!paged.length"
        type="empty"
        title="暂无新闻"
        :desc="activeCat === '全部' ? '敬请期待更多动态' : `${activeCat} 分类下暂无内容`"
      />

      <template v-else>
        <div class="masonry masonry--responsive">
          <NewsCard v-for="item in paged" :key="item.id" :item="item" />
        </div>

        <!-- 分页 -->
        <nav v-if="totalPages > 1" class="pager" aria-label="分页">
          <button class="pager__btn" :disabled="page <= 1" @click="page--">上一页</button>
          <button
            v-for="p in totalPages"
            :key="p"
            class="pager__num"
            :class="{ 'pager__num--active': p === page }"
            @click="page = p"
          >
            {{ p }}
          </button>
          <button class="pager__btn" :disabled="page >= totalPages" @click="page++">下一页</button>
        </nav>
      </template>
    </div>
  </section>
</template>

<style scoped>
.news-head {
  margin-bottom: 28px;
}
.news-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 28px;
}
.news-filter__chip {
  padding: 7px 18px;
  border-radius: 999px;
  border: 1px solid var(--c-border);
  background: var(--c-bg-card);
  color: var(--c-muted);
  font-size: 14px;
  cursor: pointer;
  transition: color var(--dur-base) var(--ease), border-color var(--dur-base) var(--ease),
    background var(--dur-base) var(--ease);
}
.news-filter__chip:hover {
  color: var(--theme-text);
  border-color: var(--c-border-strong);
}
.news-filter__chip--active {
  color: var(--theme-base);
  background: var(--theme-primary);
  border-color: var(--theme-primary);
  font-weight: 600;
}
.news-skel {
  overflow: hidden;
}
.news-skel__body {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 16px 18px 20px;
}
.pager {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 40px;
  flex-wrap: wrap;
}
.pager__btn,
.pager__num {
  min-width: 38px;
  height: 38px;
  padding: 0 12px;
  border-radius: 999px;
  border: 1px solid var(--c-border);
  background: var(--c-bg-card);
  color: var(--c-muted);
  cursor: pointer;
  font-size: 14px;
  transition: color var(--dur-base) var(--ease), border-color var(--dur-base) var(--ease),
    background var(--dur-base) var(--ease);
}
.pager__btn:hover:not(:disabled),
.pager__num:hover {
  color: var(--theme-text);
  border-color: var(--theme-primary);
}
.pager__num--active {
  color: var(--theme-base);
  background: var(--theme-primary);
  border-color: var(--theme-primary);
  font-weight: 600;
}
.pager__btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
</style>
