/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:40 * @FilePath: apps/taiji-admin/src/views/MediaLibrary.vue *
@Description: 媒体库（见 docs/11 §5），上传 + 列表读取，复用官网 Masonry 设计语言 */
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { ElUpload, ElButton, ElMessage, type UploadRequestOptions } from 'element-plus';
import { http } from '~/api/request';
import { listMedia } from '~/api/media';
import type { MediaVO } from 'taiji-shared';

const list = ref<MediaVO[]>([]);
const loading = ref(false);

// 媒体列表：GET /api/media 已放行只读（见 SecurityConfig）
async function load() {
  loading.value = true;
  try {
    const res = await listMedia({ page: 1, size: 50 });
    if (res.code === 0 && res.data) {
      list.value = res.data.list;
    } else {
      ElMessage.error(res.message || '加载媒体失败');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
  } finally {
    loading.value = false;
  }
}

onMounted(() => load());

// 自定义上传：经 request 实例携带 JWT，避免原生 action 不附加 Authorization 导致写操作 401（见 docs/14 §4）
const customUpload = async (options: UploadRequestOptions): Promise<void> => {
  const form = new FormData();
  form.append('file', options.file);
  // 后端 MediaController.upload 为写操作需认证（回退 anonymous 仅兜底），正常流程 http 自动携带登录 JWT。
  // 提交 FormData 时 axios 自动设置 multipart 边界，无需手动 Content-Type。
  const res = await http.post<MediaVO>('/media/upload', form);
  if (res.code === 0 && res.data) {
    list.value.unshift(res.data);
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(res.message || '上传失败');
  }
};
</script>

<template>
  <div>
    <ElUpload :http-request="customUpload" :show-file-list="false" list-type="picture-card">
      <ElButton type="primary">上传素材</ElButton>
    </ElUpload>
    <div v-loading="loading" class="masonry masonry--responsive mt-4">
      <div v-for="m in list" :key="m.id" class="theme-card p-2">
        <!-- 仅 http(s) 链接走 <img>，mock:// 占位地址不可渲染（见 docs/14 §6） -->
        <img v-if="m.url.startsWith('http')" :src="m.url" :alt="m.name" class="w-full rounded" />
        <span>{{ m.name }}</span>
      </div>
    </div>
  </div>
</template>
