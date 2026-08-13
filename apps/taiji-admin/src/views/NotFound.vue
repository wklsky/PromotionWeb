<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/NotFound.vue
 * @Description: 404 兜底页（见 docs/16 §4.4）。独立顶级路由渲染，提供返回控制台入口。
 -->
<script setup lang="ts">
import { useRouter } from 'vue-router';
import { ElButton } from 'element-plus';

const router = useRouter();
</script>

<template>
  <div class="nf-wrap">
    <div class="nf-aura" aria-hidden="true" />
    <div class="nf-inner cms-fade-in">
      <p class="nf-code">404</p>
      <h1 class="nf-title">页面走丢了</h1>
      <p class="nf-sub">你访问的地址不存在或已被移动</p>
      <ElButton type="primary" class="nf-btn" @click="router.replace('/dashboard')">
        返回控制台
      </ElButton>
    </div>
  </div>
</template>

<style scoped>
.nf-wrap {
  position: relative;
  min-height: 100vh;
  display: grid;
  place-items: center;
  background: var(--cms-bg);
  overflow: hidden;
}
.nf-aura {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(50% 40% at 50% 35%, var(--cms-primary-soft), transparent 70%);
}
.nf-inner {
  position: relative;
  text-align: center;
}
.nf-code {
  margin: 0;
  font-size: 96px;
  font-weight: 800;
  line-height: 1;
  color: var(--cms-primary);
  text-shadow: 0 0 40px var(--cms-primary-soft);
}
.nf-title {
  margin: 12px 0 6px;
  font-size: 24px;
  font-weight: 700;
}
.nf-sub {
  margin: 0 0 24px;
  color: var(--cms-muted);
}
.nf-btn {
  width: 180px;
}
</style>
