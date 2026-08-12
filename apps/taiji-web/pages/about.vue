/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/pages/about.vue
 * @Description: 关于我们页（见 docs/03 §4）。企业文化 + 数据展示，主题色联动。
 */
<script setup lang="ts">
const stats = [
  { num: '3', label: '主题场馆' },
  { num: '10+', label: '文化体验项目' },
  { num: '100%', label: '东方哲思内核' },
];
const values = [
  { title: '以柔克刚', desc: '于至柔处见至刚，化冲突于无形。' },
  { title: '天人合一', desc: '人法地，地法天，与万物和合共生。' },
  { title: '动静相生', desc: '静中有动，动中有静，循环往复不息。' },
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
        <div v-for="s in stats" :key="s.label" class="card stats__item">
          <p class="stats__num">{{ s.num }}</p>
          <p class="stats__label">{{ s.label }}</p>
        </div>
      </div>

      <div class="values">
        <article v-for="v in values" :key="v.title" class="card value">
          <h3 class="value__title">{{ v.title }}</h3>
          <p class="value__desc">{{ v.desc }}</p>
        </article>
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
@media (max-width: 820px) {
  .stats,
  .values {
    grid-template-columns: 1fr;
  }
}
</style>
