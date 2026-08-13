<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/pages/jobs.vue
 * @Description: 加入我们页（见 docs/13 §7、docs/16 §4.3）。接 JobController 真实列表数据（失败回退静态），
 *               标签 chips + 点击展开详情 + 投递引导，主题色联动。
 -->
<script setup lang="ts">
import { ref } from 'vue';
import SkeletonBlock from '~/components/common/SkeletonBlock.vue';
import EmptyState from '~/components/common/EmptyState.vue';
import { fetchJobs } from '~/api/content';
import type { JobListVO } from 'taiji-shared';

// 静态兜底（后端 JobController 列表可后续接入，见 docs/13 §7）
const fallback = [
  { position: '太极文化讲师', type: '全职', city: '成都 · 熊猫馆', department: '文化', salary: '', desc: '负责太极文化与武术课程设计与讲授。' },
  { position: '空间体验设计师', type: '全职', city: '北京 · 龙虎馆', department: '设计', salary: '', desc: '主导主题馆沉浸式体验空间策划。' },
  { position: '前端开发工程师', type: '全职', city: '远程', department: '研发', salary: '', desc: '参与官网与数字展厅的前端开发。' },
  { position: '品牌实习生', type: '实习', city: '上海 · 鲲鹏馆', department: '品牌', salary: '', desc: '协助品牌内容与活动运营。' },
];

interface JobView {
  id: number;
  title: string;
  type: string;
  place: string;
  department: string;
  salary: string;
  desc: string;
}

const { data, pending, error } = await useAsyncData('jobs-list', async () => {
  const res = await fetchJobs({ page: 1, size: 50 });
  if (res.code === 0 && res.data) return res.data.list as JobListVO[];
  throw new Error(res.message || '加载岗位失败');
});

function toView(list: JobListVO[]): JobView[] {
  return list.map((j) => ({
    id: j.id,
    title: j.position,
    type: j.type,
    place: [j.city, j.department].filter(Boolean).join(' · '),
    department: j.department ?? '',
    salary: j.salary ?? '',
    desc: '',
  }));
}

const jobs = ref<JobView[]>(
  data.value ? toView(data.value) : fallback.map((f, i) => ({ id: i, ...f })),
);

const openId = ref<number | null>(null);
function toggle(id: number): void {
  openId.value = openId.value === id ? null : id;
}
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="jobs-head">
        <span class="tag">加入我们</span>
        <h1 class="section-title">与东方哲思同行</h1>
        <p class="section-sub">我们在寻找热爱文化、笃信长期主义的同行者。</p>
      </header>

      <div v-if="pending" class="jobs">
        <div v-for="n in 4" :key="n" class="card job job--skel">
          <SkeletonBlock height="22px" width="50%" />
          <SkeletonBlock :lines="2" />
        </div>
      </div>

      <EmptyState v-else-if="error" type="error" :title="error.message" desc="请稍后重试" />
      <EmptyState v-else-if="!jobs.length" type="empty" title="暂无在招岗位" desc="敬请期待更多机会" />

      <div v-else class="jobs">
        <article
          v-for="job in jobs"
          :key="job.id"
          class="card job"
          :class="{ 'job--open': openId === job.id }"
          v-reveal
        >
          <button class="job__row" @click="toggle(job.id)">
            <div class="job__main">
              <h3 class="job__title">{{ job.title }}</h3>
              <p v-if="job.desc" class="job__desc">{{ job.desc }}</p>
            </div>
            <div class="job__meta">
              <span class="tag">{{ job.type }}</span>
              <span class="job__place">{{ job.place }}</span>
              <span class="job__chevron" :class="{ 'job__chevron--up': openId === job.id }">⌄</span>
            </div>
          </button>

          <div v-if="openId === job.id" class="job__detail">
            <div class="job__chips">
              <span v-if="job.salary" class="tag job__chip">薪资 {{ job.salary }}</span>
              <span v-if="job.department" class="tag job__chip">部门 {{ job.department }}</span>
              <span class="tag job__chip">{{ job.type }}</span>
              <span class="tag job__chip">{{ job.place }}</span>
            </div>
            <NuxtLink to="/contact" class="btn btn--ghost job__apply">投递该岗位</NuxtLink>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<style scoped>
.jobs-head {
  margin-bottom: 36px;
}
.jobs {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.job {
  padding: 0;
  overflow: hidden;
}
.job--skel {
  padding: 22px 26px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.job__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  width: 100%;
  padding: 22px 26px;
  background: transparent;
  border: none;
  color: inherit;
  cursor: pointer;
  text-align: left;
  font: inherit;
}
.job__title {
  font-size: 20px;
  margin: 0 0 6px;
}
.job__desc {
  margin: 0;
  color: var(--c-muted);
}
.job__meta {
  display: flex;
  align-items: center;
  gap: 12px;
}
.job__place {
  color: var(--c-muted);
  font-size: 14px;
}
.job__chevron {
  font-size: 22px;
  color: var(--theme-primary);
  transition: transform var(--dur-base) var(--ease);
}
.job__chevron--up {
  transform: rotate(180deg);
}
.job__detail {
  padding: 0 26px 22px;
  display: flex;
  flex-direction: column;
  gap: 14px;
  animation: fade-down 280ms var(--ease) both;
}
.job__chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}
.job__chip {
  font-size: 12px;
}
@keyframes fade-down {
  from {
    opacity: 0;
    transform: translateY(-8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@media (max-width: 720px) {
  .job__row {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
