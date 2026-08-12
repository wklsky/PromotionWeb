/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/pages/news.vue
 * @Description: 新闻动态页（见 docs/13 §2.2）。SSR 首屏直出列表，卡片化展示 + 空态/错误态。
 */
<script setup lang="ts">
import { ref } from 'vue';
import NewsCard from '~/components/common/NewsCard.vue';
import { fetchNews } from '~/api/content';
import type { NewsListVO } from 'taiji-shared';

const errorMsg = ref('');

// SSR 首屏直出：useAsyncData 在服务端即发起请求（见 docs/07 §2 SEO 诉求）。
// 仅展示已发布(status=1)新闻，后端按 page/size 分页返回（见 docs/13 §2.2）。
const { data, error } = await useAsyncData('news-list', async () => {
  const res = await fetchNews({ page: 1, size: 20 });
  if (res.code === 0 && res.data) {
    return res.data.list as NewsListVO[];
  }
  throw new Error(res.message || '加载新闻失败');
});

const news = ref<NewsListVO[]>(data.value ?? []);
if (error.value) {
  errorMsg.value = error.value.message;
}
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="news-head">
        <h1 class="section-title">新闻动态</h1>
        <p class="section-sub">企业动态 · 行业资讯 · 技术文章</p>
      </header>

      <p v-if="errorMsg" class="state state--error">⚠ {{ errorMsg }}</p>

      <div v-else-if="news.length" class="masonry masonry--responsive">
        <NewsCard v-for="item in news" :key="item.id" :item="item" />
      </div>

      <div v-else class="state state--empty">
        <p>暂无新闻，敬请期待</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.news-head {
  margin-bottom: 36px;
}
.state {
  padding: 48px;
  text-align: center;
  border: 1px dashed var(--c-border);
  border-radius: var(--radius);
  color: var(--c-muted);
}
.state--error {
  color: #ff8585;
  border-color: color-mix(in srgb, #ff8585 40%, transparent);
}
</style>
