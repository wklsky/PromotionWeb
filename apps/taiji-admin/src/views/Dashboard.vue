<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/Dashboard.vue
 * @Description: CMS 数据概览页（见 docs/16 §4.3）。登录后默认进入：内容计数卡 + 最近动态，
 *               统一暗金令牌与 cms-fade-in 入场。
 -->
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { listNews } from '~/api/news';
import { listMedia } from '~/api/media';
import { listJobs } from '~/api/jobs';
import type { NewsListVO } from 'taiji-shared';

const router = useRouter();
const loading = ref(true);
const stats = ref({ news: 0, media: 0, jobs: 0 });
const recent = ref<NewsListVO[]>([]);

onMounted(async () => {
  try {
    const [newsRes, mediaRes, jobsRes] = await Promise.all([
      listNews({ page: 1, size: 5 }),
      listMedia({ page: 1, size: 8 }),
      listJobs({ page: 1, size: 8 }),
    ]);
    if (newsRes.code === 0 && newsRes.data) {
      stats.value.news = newsRes.data.total ?? newsRes.data.list.length;
      recent.value = newsRes.data.list.slice(0, 5);
    }
    if (mediaRes.code === 0 && mediaRes.data) {
      stats.value.media = mediaRes.data.total ?? mediaRes.data.list.length;
    }
    if (jobsRes.code === 0 && jobsRes.data) {
      stats.value.jobs = jobsRes.data.total ?? jobsRes.data.list.length;
    }
  } finally {
    loading.value = false;
  }
});

const cards = [
  { key: 'news', label: '新闻动态', icon: '📰', to: '/news' },
  { key: 'media', label: '媒体素材', icon: '🖼️', to: '/media' },
  { key: 'jobs', label: '在招岗位', icon: '💼', to: '/jobs' },
] as const;
</script>

<template>
  <div class="cms-fade-in">
    <div class="cms-page-head">
      <div>
        <h2 class="cms-page-title">数据概览</h2>
        <p class="cms-page-sub">内容资产与招聘动态一览</p>
      </div>
    </div>

    <div class="dash-cards">
      <div
        v-for="c in cards"
        :key="c.key"
        class="cms-panel dash-card"
        @click="router.push(c.to)"
      >
        <span class="dash-card__icon">{{ c.icon }}</span>
        <div class="dash-card__body">
          <p class="dash-card__num">{{ loading ? '—' : stats[c.key] }}</p>
          <p class="dash-card__label">{{ c.label }}</p>
        </div>
      </div>
    </div>

    <div class="cms-panel dash-recent">
      <h3 class="dash-recent__title">最近动态</h3>
      <ul v-if="!loading && recent.length" class="dash-recent__list">
        <li v-for="n in recent" :key="n.id" class="dash-recent__item" @click="router.push('/news')">
          <span class="dash-recent__cat">{{ n.category }}</span>
          <span class="dash-recent__name">{{ n.title }}</span>
          <span class="dash-recent__time">{{ n.publishTime?.slice(0, 10) || '—' }}</span>
        </li>
      </ul>
      <p v-else-if="!loading" class="dash-recent__empty">暂无新闻动态</p>
      <p v-else class="dash-recent__empty">加载中…</p>
    </div>
  </div>
</template>

<style scoped>
.dash-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  margin-bottom: 22px;
}
.dash-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 22px;
  cursor: pointer;
  transition: transform 200ms var(--cms-ease), border-color 200ms var(--cms-ease);
}
.dash-card:hover {
  transform: translateY(-4px);
  border-color: var(--cms-primary);
}
.dash-card__icon {
  display: grid;
  place-items: center;
  width: 50px;
  height: 50px;
  border-radius: 12px;
  font-size: 22px;
  background: var(--cms-primary-soft);
}
.dash-card__num {
  margin: 0;
  font-size: 30px;
  font-weight: 700;
  color: var(--cms-primary);
}
.dash-card__label {
  margin: 2px 0 0;
  color: var(--cms-muted);
  font-size: 14px;
}
.dash-recent {
  padding: 20px 22px;
}
.dash-recent__title {
  margin: 0 0 14px;
  font-size: 16px;
}
.dash-recent__list {
  list-style: none;
  margin: 0;
  padding: 0;
}
.dash-recent__item {
  display: grid;
  grid-template-columns: 90px 1fr auto;
  align-items: center;
  gap: 14px;
  padding: 12px 8px;
  border-bottom: 1px solid var(--cms-border);
  cursor: pointer;
  transition: background 160ms var(--cms-ease);
}
.dash-recent__item:last-child {
  border-bottom: none;
}
.dash-recent__item:hover {
  background: rgba(255, 255, 255, 0.03);
}
.dash-recent__cat {
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 999px;
  color: var(--cms-primary);
  background: var(--cms-primary-soft);
  justify-self: start;
}
.dash-recent__name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.dash-recent__time {
  color: var(--cms-muted);
  font-size: 13px;
}
.dash-recent__empty {
  color: var(--cms-muted);
  padding: 12px 0;
  margin: 0;
}
@media (max-width: 760px) {
  .dash-cards {
    grid-template-columns: 1fr;
  }
}
</style>
