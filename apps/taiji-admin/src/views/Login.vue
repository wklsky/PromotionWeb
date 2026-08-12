/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:40 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:40 * @FilePath: apps/taiji-admin/src/views/Login.vue * @Description:
CMS 登录页（见 docs/14 §4 认证流程） */
<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElForm, ElFormItem, ElInput, ElButton, ElMessage } from 'element-plus';
import { http, TOKEN_KEY } from '~/api/request';
import type { LoginResultVO, LoginDTO } from 'taiji-shared';

const router = useRouter();
const form = ref<LoginDTO>({ username: '', password: '' });
const loading = ref(false);

async function onSubmit() {
  loading.value = true;
  try {
    // 后端 POST /api/auth/login 返回 { token, role, username }（见 docs/13 §9）
    const res = await http.post<LoginResultVO>('/auth/login', form.value);
    if (res.code === 0 && res.data?.token) {
      // token 写入 localStorage，request 拦截器自动附加 Bearer（见 api/request.ts）
      localStorage.setItem(TOKEN_KEY, res.data.token);
      ElMessage.success('登录成功');
      router.replace('/news');
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
  <div class="flex items-center justify-center h-screen">
    <ElForm :model="form" class="w-80 theme-card p-6 space-y-4" @submit.prevent="onSubmit">
      <h1 class="text-xl font-bold text-center">太极馆 · CMS 登录</h1>
      <ElFormItem>
        <ElInput v-model="form.username" placeholder="用户名" />
      </ElFormItem>
      <ElFormItem>
        <ElInput v-model="form.password" type="password" placeholder="密码" show-password />
      </ElFormItem>
      <ElButton type="primary" class="w-full" :loading="loading" @click="onSubmit">登录</ElButton>
    </ElForm>
  </div>
</template>
