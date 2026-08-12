/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/components/common/SiteHero.vue
 * @Description: 首页 Hero 区（见 docs/03 §4、docs/09 §5）。水墨晕染背景 + 旋转太极环 + 主标语，
 *               纯 CSS/SVG 实现保证 SSR 首屏直出，3D 增强由使用方自行异步挂载。
 */
<script setup lang="ts">
// 三大馆入口，路由联动主题色（见 docs/05 §2.2）
const halls = [
  { id: 'dragon', name: '龙虎馆', desc: '刚柔并济，金辉映世', tag: '武 · 气' },
  { id: 'panda', name: '熊猫馆', desc: '黑白分明，憨态藏锋', tag: '和 · 静' },
  { id: 'kunpeng', name: '鲲鹏馆', desc: '天海一色，扶摇万里', tag: '远 · 达' },
];
</script>

<template>
  <section class="hero aura-bg">
    <div class="container hero__inner">
      <div class="hero__copy animate-in">
        <span class="tag">新东方科技 · 太极文化</span>
        <h1 class="hero__title">以柔克刚<br />和合共生</h1>
        <p class="hero__lead">
          太极馆以东方哲思为魂，融合现代科技之美，构筑龙虎、熊猫、鲲鹏三大主题空间，
          于动静之间见天地人和。
        </p>
        <div class="hero__actions">
          <NuxtLink to="/dragon" class="btn btn--primary">探索三大馆</NuxtLink>
          <NuxtLink to="/about" class="btn btn--ghost">了解我们</NuxtLink>
        </div>
      </div>

      <!-- 旋转太极环：主题色描边，纯 CSS 动画，SSR 安全 -->
      <div class="hero__orbit" aria-hidden="true">
        <div class="taiji-ring">
          <span class="taiji-ring__dot" />
        </div>
        <div class="taiji-ring taiji-ring--outer" />
      </div>
    </div>

    <div class="container hero__halls">
      <NuxtLink
        v-for="hall in halls"
        :key="hall.id"
        :to="`/${hall.id}`"
        class="card hero__hall"
      >
        <span class="tag">{{ hall.tag }}</span>
        <h3 class="hero__hall-name">{{ hall.name }}</h3>
        <p class="hero__hall-desc">{{ hall.desc }}</p>
        <span class="hero__hall-more">进入 →</span>
      </NuxtLink>
    </div>
  </section>
</template>

<style scoped>
.hero {
  padding-top: clamp(48px, 8vw, 88px);
  padding-bottom: clamp(24px, 4vw, 48px);
}
.hero__inner {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  align-items: center;
  gap: 40px;
  min-height: 52vh;
}
.hero__title {
  font-size: clamp(40px, 7vw, 72px);
  margin: 18px 0 20px;
  background: linear-gradient(
    120deg,
    var(--theme-text),
    color-mix(in srgb, var(--theme-primary) 90%, var(--theme-text))
  );
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}
.hero__lead {
  max-width: 46ch;
  color: var(--c-muted);
  font-size: 17px;
}
.hero__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 28px;
}
.hero__orbit {
  position: relative;
  display: grid;
  place-items: center;
  aspect-ratio: 1;
}
.taiji-ring {
  position: relative;
  width: min(360px, 78%);
  aspect-ratio: 1;
  border-radius: 50%;
  border: 2px solid var(--theme-primary);
  box-shadow: var(--shadow-glow);
  animation: spin 18s linear infinite;
}
.taiji-ring--outer {
  position: absolute;
  width: min(440px, 96%);
  border-style: dashed;
  border-color: color-mix(in srgb, var(--theme-primary) 40%, transparent);
  opacity: 0.5;
  animation-duration: 36s;
  animation-direction: reverse;
}
.taiji-ring__dot {
  position: absolute;
  top: -8px;
  left: 50%;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--theme-primary);
  transform: translateX(-50%);
  box-shadow: 0 0 18px var(--theme-primary);
}
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
.hero__halls {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-top: clamp(32px, 6vw, 64px);
}
.hero__hall {
  padding: 26px 24px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.hero__hall-name {
  font-size: 22px;
  margin: 4px 0 0;
}
.hero__hall-desc {
  color: var(--c-muted);
  font-size: 14px;
  margin: 0;
}
.hero__hall-more {
  margin-top: auto;
  font-weight: 600;
  color: var(--theme-primary);
}
@media (max-width: 900px) {
  .hero__inner {
    grid-template-columns: 1fr;
    text-align: center;
  }
  .hero__orbit {
    order: -1;
    max-width: 280px;
    margin-inline: auto;
  }
  .hero__actions {
    justify-content: center;
  }
  .hero__lead {
    margin-inline: auto;
  }
  .hero__halls {
    grid-template-columns: 1fr;
  }
}
</style>
