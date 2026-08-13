<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/pages/[hall].vue
 * @Description: 三大馆动态路由页（见 docs/03 §4、docs/05 §6.1、docs/16 §4.1/§4.3）。
 *               业务介绍交替布局 + 案例瀑布流；取数期骨架屏/空态统一；粘性章节导航 + 图片遮罩叙事。
 -->
<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import SkeletonBlock from '~/components/common/SkeletonBlock.vue';
import EmptyState from '~/components/common/EmptyState.vue';
import { fetchContent } from '~/api/content';
import type { CompanyInfoVO } from 'taiji-shared';

// 路由前缀命中 ROUTE_THEME_MAP，进入即切换对应馆主题色（见 docs/09 §6 useTheme）
const route = useRoute();
const hallNameMap: Record<string, string> = {
  dragon: '龙虎馆',
  panda: '熊猫馆',
  kunpeng: '鲲鹏馆',
};
const hallDescMap: Record<string, string> = {
  dragon: '刚柔并济，金辉映世',
  panda: '黑白分明，憨态藏锋',
  kunpeng: '天海一色，扶摇万里',
};
const hallName = hallNameMap[route.params.hall as string] ?? '太极馆';
const hallDesc = hallDescMap[route.params.hall as string] ?? '';

// SSR 首屏直出：useAsyncData 服务端即取数（见 docs/07 §2 SEO 诉求）。
const { data, pending, error } = await useAsyncData(
  () => `hall-content-${route.params.hall}`,
  async () => {
    const res = await fetchContent(route.params.hall as string);
    if (res.code === 0 && res.data) return res.data as CompanyInfoVO[];
    throw new Error(res.message || '加载内容失败');
  },
);

const intro = ref<CompanyInfoVO[]>(data.value ?? []);
watch(data, (v) => (intro.value = v ?? []));

// 章节导航：取各业务块标题（docs/16 §4.3 粘性章节导航）
const navItems = computed(() => intro.value.map((b, i) => ({ id: `block-${i}`, title: b.title })));
function scrollTo(id: string): void {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' });
}
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="hall-head">
        <span class="tag">主题馆</span>
        <h1 class="section-title">{{ hallName }}</h1>
        <p class="section-sub">{{ hallDesc }}</p>
      </header>

      <!-- 取数期：骨架屏 -->
      <div v-if="pending" class="hall-blocks">
        <div v-for="n in 3" :key="n" class="hall-block theme-card">
          <div class="hall-block__media"><SkeletonBlock height="100%" radius="0" /></div>
          <div class="hall-block__text">
            <SkeletonBlock width="30%" height="22px" />
            <SkeletonBlock :lines="3" />
          </div>
        </div>
      </div>

      <EmptyState v-else-if="error" type="error" :title="error.message" desc="请稍后重试" />
      <EmptyState v-else-if="!intro.length" type="empty" title="内容筹备中" desc="该主题馆内容即将上线" />

      <!-- 业务四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作（docs/03 §4） -->
      <div v-else class="hall-layout">
        <aside class="hall-nav" aria-label="章节导航">
          <button
            v-for="item in navItems"
            :key="item.id"
            class="hall-nav__item"
            @click="scrollTo(item.id)"
          >
            {{ item.title }}
          </button>
        </aside>

        <div class="hall-blocks">
          <article
            v-for="(block, i) in intro"
            :id="`block-${i}`"
            :key="block.id"
            class="hall-block"
            :class="{ 'hall-block--reverse': i % 2 === 1 }"
          >
            <div class="hall-block__media">
              <img v-if="block.cover" :src="block.cover" :alt="block.title" loading="lazy" />
              <div v-else class="hall-block__ph">{{ block.title.slice(0, 1) }}</div>
              <span class="hall-block__mask">{{ block.title }}</span>
            </div>
            <div class="hall-block__text">
              <span class="tag">0{{ i + 1 }}</span>
              <h2 class="hall-block__title">{{ block.title }}</h2>
              <p class="hall-block__content">{{ block.content }}</p>
            </div>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.hall-head {
  margin-bottom: 40px;
}
.hall-layout {
  display: grid;
  grid-template-columns: 180px 1fr;
  gap: 32px;
  align-items: start;
}
.hall-nav {
  position: sticky;
  top: 88px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.hall-nav__item {
  text-align: left;
  padding: 10px 14px;
  border-radius: 999px;
  border: 1px solid transparent;
  background: transparent;
  color: var(--c-muted);
  font-size: 14px;
  cursor: pointer;
  transition: color var(--dur-base) var(--ease), border-color var(--dur-base) var(--ease),
    background var(--dur-base) var(--ease);
}
.hall-nav__item:hover {
  color: var(--theme-text);
  border-color: var(--c-border);
}
.hall-blocks {
  display: flex;
  flex-direction: column;
  gap: 36px;
}
.hall-block {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 32px;
  align-items: center;
  padding: 24px;
  border-radius: var(--radius);
  background: var(--c-bg-card);
  border: 1px solid var(--c-border);
  transition: border-color 300ms var(--ease), box-shadow 300ms var(--ease);
}
.hall-block:hover {
  border-color: var(--theme-primary);
  box-shadow: var(--shadow-card);
}
.hall-block--reverse .hall-block__media {
  order: 2;
}
.hall-block__media {
  position: relative;
  aspect-ratio: 16 / 10;
  border-radius: var(--radius-sm);
  overflow: hidden;
  background: var(--c-bg-soft);
}
.hall-block__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 600ms var(--ease);
}
.hall-block:hover .hall-block__media img {
  transform: scale(1.05);
}
/* 图片遮罩叙事（docs/16 §4.3）：底部渐变 + 标题叠层 */
.hall-block__mask {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 28px 20px 16px;
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
  opacity: 0;
  transform: translateY(8px);
  transition: opacity var(--dur-base) var(--ease), transform var(--dur-base) var(--ease);
}
.hall-block:hover .hall-block__mask {
  opacity: 1;
  transform: translateY(0);
}
.hall-block__ph {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  font-size: 56px;
  font-weight: 700;
  color: var(--theme-primary);
  background: linear-gradient(
    135deg,
    color-mix(in srgb, var(--theme-primary) 16%, transparent),
    transparent
  );
}
.hall-block__title {
  font-size: 24px;
  margin: 12px 0 10px;
}
.hall-block__content {
  color: var(--c-muted);
  margin: 0;
}
@media (max-width: 900px) {
  .hall-layout {
    grid-template-columns: 1fr;
  }
  .hall-nav {
    display: none;
  }
  .hall-block,
  .hall-block--reverse .hall-block__media {
    grid-template-columns: 1fr;
    order: 0;
  }
}
</style>
