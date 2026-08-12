/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/pages/[hall].vue
 * @Description: 三大馆动态路由页（见 docs/03 §4、docs/05 §6.1）。业务介绍交替布局 + 案例瀑布流。
 */
<script setup lang="ts">
import { ref } from 'vue';
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

const errorMsg = ref('');

// SSR 首屏直出：useAsyncData 在服务端即取数（见 docs/07 §2 SEO 诉求）。
// key 绑定 section，切换馆时自动重新请求；后端 ContentController.bySection 返回已启用内容（见 docs/13 §2.1）。
const { data, error } = await useAsyncData(
  () => `hall-content-${route.params.hall}`,
  async () => {
    const res = await fetchContent(route.params.hall as string);
    if (res.code === 0 && res.data) {
      return res.data as CompanyInfoVO[];
    }
    throw new Error(res.message || '加载内容失败');
  },
);

const intro = ref<CompanyInfoVO[]>(data.value ?? []);
if (error.value) {
  errorMsg.value = error.value.message;
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

      <p v-if="errorMsg" class="state state--error">⚠ {{ errorMsg }}</p>

      <!-- 业务四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作（docs/03 §4） -->
      <div v-else-if="intro.length" class="hall-blocks">
        <article
          v-for="(block, i) in intro"
          :key="block.id"
          class="hall-block"
          :class="{ 'hall-block--reverse': i % 2 === 1 }"
        >
          <div class="hall-block__media">
            <img v-if="block.cover" :src="block.cover" :alt="block.title" loading="lazy" />
            <div v-else class="hall-block__ph">{{ block.title.slice(0, 1) }}</div>
          </div>
          <div class="hall-block__text">
            <span class="tag">0{{ i + 1 }}</span>
            <h2 class="hall-block__title">{{ block.title }}</h2>
            <p class="hall-block__content">{{ block.content }}</p>
          </div>
        </article>
      </div>

      <p v-else class="state">（四段式叙事：业务 → 能力/技术 → 产品/生态 → 案例/合作）</p>
    </div>
  </section>
</template>

<style scoped>
.hall-head {
  margin-bottom: 40px;
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
  aspect-ratio: 16 / 10;
  border-radius: var(--radius-sm);
  overflow: hidden;
  background: var(--c-bg-soft);
}
.hall-block__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
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
@media (max-width: 820px) {
  .hall-block,
  .hall-block--reverse .hall-block__media {
    grid-template-columns: 1fr;
    order: 0;
  }
}
</style>
