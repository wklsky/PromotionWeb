/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-admin/src/views/Login.vue
 * @Description: CMS 登录页（见 docs/14 §4 认证流程）。深色金主题卡片 + 氛围光晕。
 */
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
    // 后端 POST /api/auth/login 返回 { token, role, username }（见 docs/13 §9）
    const res = await http.post<LoginResultVO>('/auth/login', form.value);
    if (res.code === 0 && res.data?.token) {
      // token 写入 store 与 localStorage，request 拦截器自动附加 Bearer（见 api/request.ts）
      userStore.setSession(res.data);
      ElMessage.success('登录成功');
      // 优先回跳到被守卫拦截前的目标页；无 redirect 时进入默认新闻管理页
      const redirect = (route.query.redirect as string) || '/news';
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
    <div class="login-card cms-fade-in">
      <div class="login-brand">
        <span class="login-brand__mark">太</span>
        <div>
          <p class="login-brand__name">太极馆</p>
          <p class="login-brand__sub">CMS 内容管理后台</p>
        </div>
      </div>
      <h1 class="login-title">欢迎回来</h1>
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
  </div>
</template>

<style scoped>
.login-wrap {
  height: 100vh;
  display: grid;
  place-items: center;
  background:
    radial-gradient(50% 40% at 30% 20%, rgba(212, 175, 55, 0.16), transparent 70%),
    radial-gradient(40% 40% at 80% 80%, rgba(212, 175, 55, 0.1), transparent 70%),
    var(--cms-bg);
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
</style>
