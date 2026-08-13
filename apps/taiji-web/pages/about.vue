<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/pages/about.vue
 * @Description: 关于我们页（见 docs/03 §4、docs/16 §4.3）。企业文化 + 数据增长计数 + 发展历程时间轴，主题色联动。
 -->
<script setup lang="ts">
import CountUp from '~/components/common/CountUp.vue';

const stats = [
  { num: 3, suffix: '', label: '主题场馆' },
  { num: 10, suffix: '+', label: '文化体验项目' },
  { num: 100, suffix: '%', label: '东方哲思内核' },
];
const values = [
  { title: '以柔克刚', desc: '于至柔处见至刚，化冲突于无形。' },
  { title: '天人合一', desc: '人法地，地法天，与万物和合共生。' },
  { title: '动静相生', desc: '静中有动，动中有静，循环往复不息。' },
];
// 发展历程时间轴（docs/16 §4.3）
const milestones = [
  { year: '2021', title: '太极馆创立', desc: '以东方哲思为内核，落子首座主题空间。' },
  { year: '2022', title: '龙虎馆开馆', desc: '刚柔并济的武学体验空间正式对外开放。' },
  { year: '2023', title: '熊猫馆落成', desc: '黑白分明、憨态藏锋的生态文化场景上线。' },
  { year: '2024', title: '鲲鹏馆启航', desc: '天海一色、扶摇万里的科技美学空间面世。' },
  { year: '2025', title: '数字太极', desc: '官网与数字展厅升级，沉浸式品牌体验落地。' },
];
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="about-head">
        <span class="tag">关于太极馆</span>
        <h1 class="section-title">以东方哲思，筑现代空间</h1>
        <p class="section-sub">
          太极馆承袭太极文化之精髓，融武术、生态、科技三大主题于一体，打造可游、可学、可感的东方生活美学空间。
        </p>
      </header>

      <div class="stats">
        <div v-for="s in stats" :key="s.label" class="card stats__item" v-reveal>
          <p class="stats__num"><CountUp :value="s.num" :suffix="s.suffix" /></p>
          <p class="stats__label">{{ s.label }}</p>
        </div>
      </div>

      <div class="values">
        <article v-for="v in values" :key="v.title" class="card value" v-reveal>
          <h3 class="value__title">{{ v.title }}</h3>
          <p class="value__desc">{{ v.desc }}</p>
        </article>
      </div>

      <!-- 发展历程时间轴 -->
      <div class="timeline-wrap">
        <h2 class="section-title timeline-title">发展历程</h2>
        <ol class="timeline">
          <li v-for="(m, i) in milestones" :key="m.year" class="timeline__item" v-reveal="{ delay: i * 0.05 }">
            <span class="timeline__dot" aria-hidden="true" />
            <div class="timeline__card">
              <span class="timeline__year">{{ m.year }}</span>
              <h3 class="timeline__name">{{ m.title }}</h3>
              <p class="timeline__desc">{{ m.desc }}</p>
            </div>
          </li>
        </ol>
      </div>
    </div>
  </section>
</template>

<style scoped>
.about-head {
  margin-bottom: 40px;
  max-width: 60ch;
}
.stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  margin-bottom: 48px;
}
.stats__item {
  padding: 28px;
  text-align: center;
}
.stats__num {
  margin: 0;
  font-size: 40px;
  font-weight: 700;
  color: var(--theme-primary);
}
.stats__label {
  margin: 6px 0 0;
  color: var(--c-muted);
}
.values {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  margin-bottom: 56px;
}
.value {
  padding: 26px 24px;
}
.value__title {
  font-size: 22px;
  margin: 0 0 10px;
  color: var(--theme-primary);
}
.value__desc {
  margin: 0;
  color: var(--c-muted);
}
/* 时间轴 */
.timeline-title {
  margin-bottom: 28px;
}
.timeline {
  list-style: none;
  margin: 0;
  padding: 0;
  position: relative;
}
.timeline::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 6px;
  bottom: 6px;
  width: 2px;
  background: linear-gradient(var(--theme-primary), transparent);
  opacity: 0.5;
}
.timeline__item {
  position: relative;
  padding: 0 0 28px 40px;
}
.timeline__dot {
  position: absolute;
  left: 0;
  top: 4px;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: var(--theme-primary);
  box-shadow: 0 0 0 4px color-mix(in srgb, var(--theme-primary) 20%, transparent);
}
.timeline__card {
  padding: 16px 20px;
  border-radius: var(--radius-sm);
  background: var(--c-bg-card);
  border: 1px solid var(--c-border);
  transition: border-color var(--dur-base) var(--ease), transform var(--dur-base) var(--ease);
}
.timeline__item:hover .timeline__card {
  border-color: var(--theme-primary);
  transform: translateX(4px);
}
.timeline__year {
  font-size: 13px;
  font-weight: 700;
  color: var(--theme-primary);
  letter-spacing: 0.05em;
}
.timeline__name {
  margin: 4px 0 6px;
  font-size: 18px;
}
.timeline__desc {
  margin: 0;
  color: var(--c-muted);
  font-size: 14px;
}
@media (max-width: 820px) {
  .stats,
  .values {
    grid-template-columns: 1fr;
  }
}
</style>
