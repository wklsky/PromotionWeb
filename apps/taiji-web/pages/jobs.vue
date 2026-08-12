/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/pages/jobs.vue
 * @Description: 加入我们页（见 docs/13 §7 招聘接口）。展示招聘岗位 + 投递引导，主题色联动。
 */
<script setup lang="ts">
// 岗位静态展示（后端 JobController 列表可后续接入，见 docs/13 §7）
const jobs = [
  { title: '太极文化讲师', type: '全职', place: '成都 · 熊猫馆', desc: '负责太极文化与武术课程设计与讲授。' },
  { title: '空间体验设计师', type: '全职', place: '北京 · 龙虎馆', desc: '主导主题馆沉浸式体验空间策划。' },
  { title: '前端开发工程师', type: '全职', place: '远程', desc: '参与官网与数字展厅的前端开发。' },
  { title: '品牌实习生', type: '实习', place: '上海 · 鲲鹏馆', desc: '协助品牌内容与活动运营。' },
];
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="jobs-head">
        <span class="tag">加入我们</span>
        <h1 class="section-title">与东方哲思同行</h1>
        <p class="section-sub">我们在寻找热爱文化、笃信长期主义的同行者。</p>
      </header>

      <div class="jobs">
        <article v-for="job in jobs" :key="job.title" class="card job">
          <div class="job__main">
            <h3 class="job__title">{{ job.title }}</h3>
            <p class="job__desc">{{ job.desc }}</p>
          </div>
          <div class="job__meta">
            <span class="tag">{{ job.type }}</span>
            <span class="job__place">{{ job.place }}</span>
            <NuxtLink to="/contact" class="btn btn--ghost job__apply">投递</NuxtLink>
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
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 22px 26px;
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
@media (max-width: 720px) {
  .job {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
