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

// SSR 首屏直出：服务端分页 + 分类过滤均由后端完成，避免一次性拉取全部数据（见 docs/07 §2、docs/13 §2.2）。
// page 与 activeCat 变化触发 watch 重新取数，key 随筛选项变化保证 SSR 缓存键隔离。
const pageSize = 9;
const page = ref(1);
const activeCat = ref<string>('全部');
const cats = ['全部', ...NEWS_CATEGORIES];

const { data, pending, error } = await useAsyncData(
  () => `news-${activeCat.value}-${page.value}`,
  async () => {
    const res = await fetchNews({ page: page.value, size: pageSize, category: activeCat.value });
    if (res.code === 0 && res.data) return res.data;
    throw new Error(res.message || '加载新闻失败');
  },
);

const list = computed<NewsListVO[]>(() => data.value?.list ?? []);
const totalPages = computed(() => Math.max(1, data.value?.pages ?? 1));

// 切换分类/分页回到首屏，避免残留旧数据造成布局抖动
watch(activeCat, () => (page.value = 1));
watch([page, activeCat], () => refreshNuxtData());

// JSON-LD：Article 列表结构化数据（SEO，见 docs/07 §3）；仅取当前页前 10 条，避免随分类变化失真
useHead({
  script: [
    {
      type: 'application/ld+json',
      innerHTML: JSON.stringify({
        '@context': 'https://schema.org',
        '@type': 'ItemList',
        itemListElement: list.value.slice(0, 10).map((n, i) => ({
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
        v-else-if="!list.length"
        type="empty"
        title="暂无新闻"
        :desc="activeCat === '全部' ? '敬请期待更多动态' : `${activeCat} 分类下暂无内容`"
      />

      <template v-else>
        <div class="masonry masonry--responsive">
          <NewsCard v-for="item in list" :key="item.id" :item="item" />
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
