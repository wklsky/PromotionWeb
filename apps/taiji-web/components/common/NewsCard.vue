/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/components/common/NewsCard.vue
 * @Description: 新闻卡片（见 docs/05 §6.1）。封面懒加载 + 分类标签 + 悬停抬升，
 *               封面缺失时回退主题色渐变占位。
 */
<script setup lang="ts">
import type { NewsListVO } from 'taiji-shared';

// 单条新闻数据，字段与 shared 的 NewsListVO 对齐（见 docs/13 §2.2）
withDefaults(
  defineProps<{
    item: NewsListVO;
  }>(),
  {},
);

const emit = defineEmits<{
  // 点击卡片，回传新闻 id 便于详情跳转/弹层
  (e: 'select', id: number): void;
}>();
</script>

<template>
  <article class="news-card theme-card" @click="emit('select', item.id)">
    <div class="news-card__media">
      <img
        v-if="item.cover"
        :src="item.cover"
        :alt="item.title"
        loading="lazy"
        class="news-card__img"
      />
      <div v-else class="news-card__placeholder">{{ item.title.slice(0, 1) }}</div>
    </div>
    <div class="news-card__body">
      <span class="tag">{{ item.category }}</span>
      <h3 class="news-card__title">{{ item.title }}</h3>
      <p v-if="item.summary" class="news-card__summary">{{ item.summary }}</p>
    </div>
  </article>
</template>

<style scoped>
.news-card {
  overflow: hidden;
  cursor: pointer;
  background: var(--c-bg-card);
}
.news-card__media {
  aspect-ratio: 16 / 10;
  overflow: hidden;
}
.news-card__img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 500ms var(--ease);
}
.news-card:hover .news-card__img {
  transform: scale(1.06);
}
.news-card__placeholder {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  font-size: 48px;
  font-weight: 700;
  color: var(--theme-primary);
  background: linear-gradient(
    135deg,
    color-mix(in srgb, var(--theme-primary) 18%, transparent),
    transparent
  );
}
.news-card__body {
  padding: 16px 18px 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.news-card__title {
  font-size: 18px;
  margin: 0;
}
.news-card__summary {
  margin: 0;
  font-size: 14px;
  color: var(--c-muted);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
