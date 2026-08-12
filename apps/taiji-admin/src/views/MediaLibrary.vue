/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:33 * @FilePath: apps/taiji-admin/src/views/MediaLibrary.vue *
@Description: 媒体库（见 docs/11 §5），上传 + 瀑布流展示，复用官网 Masonry 设计语言 */
<script setup lang="ts">
import { ref } from 'vue';
import { ElUpload, ElButton, ElMessage, type UploadRequestOptions } from 'element-plus';
import request from '~/api/request';
import type { ApiResponse, MediaVO } from 'taiji-shared';

const list = ref<MediaVO[]>([]);

// 自定义上传：经 request 实例携带 JWT，避免原生 action 不附加 Authorization 导致写操作 401（见 docs/14 §4）
const customUpload = async (options: UploadRequestOptions): Promise<void> => {
  const form = new FormData();
  form.append('file', options.file);
  // 后端 MediaController.upload 要求已认证（回退 anonymous 仅兜底），正常流程携带登录 JWT
  const res = (await request.post('/media/upload', form, {
    headers: { 'Content-Type': 'multipart/form-data' },
  })) as ApiResponse<MediaVO>;
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
    <div class="masonry masonry--responsive mt-4">
      <div v-for="m in list" :key="m.id" class="theme-card p-2">
        <img v-if="m.url.startsWith('http')" :src="m.url" :alt="m.name" class="w-full rounded" />
        <span>{{ m.name }}</span>
      </div>
    </div>
  </div>
</template>
