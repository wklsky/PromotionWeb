/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/pages/index.vue * @Description:
首页：Hero(3D 太极球) + 三大馆入口卡片（见 docs/03 §4、docs/09 §5） */
<script setup lang="ts">
import { defineAsyncComponent } from 'vue';

// 3D 组件异步加载，避免阻塞首屏 LCP（见 docs/07 §7）
const TaiChiModel = defineAsyncComponent(() => import('~/components/three/TaiChiModel.vue'));

const halls = [
  { id: 'dragon', name: '龙虎馆', desc: '刚柔并济，金辉映世' },
  { id: 'panda', name: '熊猫馆', desc: '黑白分明，憨态藏锋' },
  { id: 'kunpeng', name: '鲲鹏馆', desc: '天海一色，扶摇万里' },
];
</script>

<template>
  <section class="flex flex-col items-center gap-8 py-16">
    <TaiChiModel class="w-[300px] h-[300px]" />
    <h1 class="text-3xl font-bold">太极馆 · 三大主题馆</h1>
    <div class="grid gap-6 md:grid-cols-3">
      <NuxtLink
        v-for="hall in halls"
        :key="hall.id"
        :to="`/${hall.id}`"
        class="theme-card p-6 text-center"
      >
        <h2 class="text-xl font-semibold">{{ hall.name }}</h2>
        <p class="mt-2 opacity-80">{{ hall.desc }}</p>
      </NuxtLink>
    </div>
  </section>
</template>
