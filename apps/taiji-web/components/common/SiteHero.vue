<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/components/common/SiteHero.vue
 * @Description: 首页 Hero 区（见 docs/03 §4、docs/09 §5、docs/16 §4.2）。
 *               水墨晕染背景 + 太极视觉（桌面端真实 Three.js 太极球 / 移动端 CSS 环）+ 主标语 + 光标光晕微交互。
 -->
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { defineAsyncComponent } from 'vue';

// 三大馆入口，路由联动主题色（见 docs/05 §2.2）
const halls = [
  { id: 'dragon', name: '龙虎馆', desc: '刚柔并济，金辉映世', tag: '武 · 气' },
  { id: 'panda', name: '熊猫馆', desc: '黑白分明，憨态藏锋', tag: '和 · 静' },
  { id: 'kunpeng', name: '鲲鹏馆', desc: '天海一色，扶摇万里', tag: '远 · 达' },
];

// 桌面端异步挂载真实 Three.js 太极球；移动端/降级回退 CSS 环（docs/09 §5.1）
const TaiChiModel = defineAsyncComponent(() => import('~/components/three/TaiChiModel.vue'));
const show3D = ref(false);
const isWide = ref(false);
const heroRef = ref<HTMLElement | null>(null);
const mx = ref('50%');
const my = ref('40%');

onMounted(() => {
  isWide.value = window.innerWidth >= 1024;
  const reduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  show3D.value = isWide.value && !reduced;
});

// 光标跟随光晕（桌面端、非 reduced-motion，见 docs/16 §4.2 微交互）
function onMove(e: PointerEvent): void {
  if (!heroRef.value) return;
  const rect = heroRef.value.getBoundingClientRect();
  mx.value = `${((e.clientX - rect.left) / rect.width) * 100}%`;
  my.value = `${((e.clientY - rect.top) / rect.height) * 100}%`;
}
</script>

<template>
  <section ref="heroRef" class="hero aura-bg" @pointermove="onMove">
    <div
      v-if="isWide && show3D"
      class="hero__glow"
      :style="{ '--mx': mx, '--my': my }"
      aria-hidden="true"
    />

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

      <!-- 太极视觉：桌面端 WebGL 真实太极球，移动端/降级 CSS 环（SSR 安全） -->
      <div class="hero__orbit" :class="{ 'hero__orbit--3d': show3D }" aria-hidden="true">
        <component :is="TaiChiModel" v-if="show3D" class="hero__canvas" />
        <template v-else>
          <div class="taiji-ring">
            <span class="taiji-ring__dot" />
          </div>
          <div class="taiji-ring taiji-ring--outer" />
        </template>
      </div>
    </div>

    <div class="container hero__halls">
      <NuxtLink
        v-for="(hall, i) in halls"
        :key="hall.id"
        :to="`/${hall.id}`"
        class="card hero__hall"
        v-reveal="{ delay: i * 0.08 }"
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
  position: relative;
  overflow: hidden;
}
/* 光标跟随光晕（screen 混合仅增亮，不遮挡内容） */
.hero__glow {
  position: absolute;
  inset: 0;
  z-index: 2;
  pointer-events: none;
  mix-blend-mode: screen;
  opacity: 0.55;
  background: radial-gradient(
    240px 240px at var(--mx) var(--my),
    color-mix(in srgb, var(--theme-primary) 24%, transparent),
    transparent 70%
  );
}
.hero__inner {
  display: grid;
  grid-template-columns: 1.1fr 0.9fr;
  align-items: center;
  gap: 40px;
  min-height: 52vh;
  position: relative;
  z-index: 1;
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
.hero__canvas {
  width: min(420px, 92%);
  height: auto;
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
  position: relative;
  z-index: 1;
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
