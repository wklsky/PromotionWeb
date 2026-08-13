<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/Login.vue
 * @Description: CMS 登录页（见 docs/14 §4、docs/16 §4.4）。分屏布局：左侧品牌氛围面板(太极环/光晕) + 右侧表单。
 -->
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage } from 'element-plus';
import { http, TOKEN_KEY } from '~/api/request';
import { useUserStore } from '~/stores/user';
import type { LoginResultVO, LoginDTO } from 'taiji-shared';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();
const form = ref<LoginDTO>({ username: '', password: '' });
const loading = ref(false);

async function onSubmit() {
  loading.value = true;
  try {
    const res = await http.post<LoginResultVO>('/auth/login', form.value);
    if (res.code === 0 && res.data?.token) {
      userStore.setSession(res.data);
      ElMessage.success('登录成功');
      const redirect = (route.query.redirect as string) || '/dashboard';
      router.replace(redirect);
    } else {
      ElMessage.error(res.message || '登录失败');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <div class="login-wrap">
    <!-- 左侧品牌氛围面板 -->
    <aside class="login-aside" aria-hidden="true">
      <div class="login-aside__aura" />
      <div class="login-aside__ring">
        <span class="login-aside__dot" />
      </div>
      <div class="login-aside__brand">
        <span class="login-aside__mark">太</span>
        <h1 class="login-aside__name">太极馆</h1>
        <p class="login-aside__slogan">以东方哲思，筑现代空间</p>
      </div>
    </aside>

    <!-- 右侧表单 -->
    <main class="login-main">
      <div class="login-card cms-fade-in">
        <div class="login-brand">
          <span class="login-brand__mark">太</span>
          <div>
            <p class="login-brand__name">太极馆</p>
            <p class="login-brand__sub">CMS 内容管理后台</p>
          </div>
        </div>
        <h2 class="login-title">欢迎回来</h2>
        <p class="login-tip">请使用管理员账号登录</p>

        <ElForm :model="form" @submit.prevent="onSubmit">
          <ElFormItem>
            <ElInput v-model="form.username" placeholder="用户名" size="large" />
          </ElFormItem>
          <ElFormItem>
            <ElInput
              v-model="form.password"
              type="password"
              placeholder="密码"
              size="large"
              show-password
              @keyup.enter="onSubmit"
            />
          </ElFormItem>
          <ElButton type="primary" class="login-btn" :loading="loading" @click="onSubmit">
            登录
          </ElButton>
        </ElForm>
      </div>
    </main>
  </div>
</template>

<style scoped>
.login-wrap {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1fr 1fr;
}
.login-aside {
  position: relative;
  overflow: hidden;
  display: grid;
  place-items: center;
  background:
    radial-gradient(60% 50% at 30% 20%, rgba(212, 175, 55, 0.18), transparent 70%),
    radial-gradient(50% 50% at 80% 80%, rgba(212, 175, 55, 0.1), transparent 70%),
    var(--cms-bg-soft);
  border-right: 1px solid var(--cms-border);
}
.login-aside__aura {
  position: absolute;
  inset: 0;
}
.login-aside__ring {
  position: relative;
  width: min(320px, 60%);
  aspect-ratio: 1;
  border-radius: 50%;
  border: 2px solid var(--cms-primary);
  box-shadow: 0 0 40px var(--cms-primary-soft);
  animation: login-spin 22s linear infinite;
}
.login-aside__dot {
  position: absolute;
  top: -7px;
  left: 50%;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: var(--cms-primary);
  transform: translateX(-50%);
  box-shadow: 0 0 16px var(--cms-primary);
}
.login-aside__brand {
  position: absolute;
  bottom: 12%;
  text-align: center;
}
.login-aside__mark {
  display: inline-grid;
  place-items: center;
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: var(--cms-primary);
  color: #0f1115;
  font-weight: 700;
  font-size: 22px;
}
.login-aside__name {
  margin: 14px 0 4px;
  font-size: 28px;
  font-weight: 700;
}
.login-aside__slogan {
  margin: 0;
  color: var(--cms-muted);
  letter-spacing: 0.04em;
}
@keyframes login-spin {
  to {
    transform: rotate(360deg);
  }
}
.login-main {
  display: grid;
  place-items: center;
  background: var(--cms-bg);
}
.login-card {
  width: min(380px, 90vw);
  padding: 36px 32px;
  background: var(--cms-panel);
  border: 1px solid var(--cms-border);
  border-radius: var(--cms-radius);
  box-shadow: var(--cms-shadow);
}
.login-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}
.login-brand__mark {
  display: grid;
  place-items: center;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: var(--cms-primary);
  color: #0f1115;
  font-weight: 700;
  font-size: 20px;
}
.login-brand__name {
  margin: 0;
  font-weight: 700;
  font-size: 17px;
}
.login-brand__sub {
  margin: 2px 0 0;
  font-size: 12px;
  color: var(--cms-muted);
}
.login-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
}
.login-tip {
  margin: 6px 0 24px;
  font-size: 13px;
  color: var(--cms-muted);
}
.login-btn {
  width: 100%;
  margin-top: 4px;
}
@media (max-width: 860px) {
  .login-wrap {
    grid-template-columns: 1fr;
  }
  .login-aside {
    display: none;
  }
}
@media (prefers-reduced-motion: reduce) {
  .login-aside__ring {
    animation: none;
  }
}
</style>
