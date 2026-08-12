/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-admin/src/views/MediaLibrary.vue
 * @Description: 媒体库（见 docs/11 §5）：上传 + 列表读取，卡片网格展示（复用官网设计语言）。
 */
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
  // 后端 MediaController.upload 为写操作需认证，正常流程 http 自动携带登录 JWT。
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
    <div class="cms-page-head">
      <div>
        <h2 class="cms-page-title">媒体库</h2>
        <p class="cms-page-sub">图片 / 视频素材管理</p>
      </div>
      <ElUpload :http-request="customUpload" :show-file-list="false" list-type="picture-card">
        <ElButton type="primary">+ 上传素材</ElButton>
      </ElUpload>
    </div>

    <div v-loading="loading" class="media-grid">
      <div v-for="m in list" :key="m.id" class="cms-panel media-card">
        <!-- 仅 http(s) 链接走 <img>，mock:// 占位地址不可渲染（见 docs/14 §6） -->
        <div class="media-card__media">
          <img v-if="m.url.startsWith('http')" :src="m.url" :alt="m.name" loading="lazy" />
          <div v-else class="media-card__ph">{{ (m.name || '素材').slice(0, 1) }}</div>
        </div>
        <div class="media-card__foot">
          <span class="media-card__name" :title="m.name">{{ m.name }}</span>
          <span class="media-card__type">{{ m.type || 'file' }}</span>
        </div>
      </div>

      <div v-if="!loading && !list.length" class="media-empty">
        暂无素材，点击右上角上传
      </div>
    </div>
  </div>
</template>

<style scoped>
.media-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  min-height: 160px;
}
.media-card {
  overflow: hidden;
  transition: transform 280ms var(--cms-ease), border-color 280ms var(--cms-ease);
}
.media-card:hover {
  transform: translateY(-4px);
  border-color: var(--cms-primary);
}
.media-card__media {
  aspect-ratio: 4 / 3;
  overflow: hidden;
  background: var(--cms-bg-soft);
}
.media-card__media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.media-card__ph {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  font-size: 40px;
  font-weight: 700;
  color: var(--cms-primary);
  background: linear-gradient(135deg, var(--cms-primary-soft), transparent);
}
.media-card__foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 10px 12px;
}
.media-card__name {
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.media-card__type {
  flex-shrink: 0;
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 999px;
  color: var(--cms-primary);
  background: var(--cms-primary-soft);
}
.media-empty {
  grid-column: 1 / -1;
  display: grid;
  place-items: center;
  padding: 48px;
  border: 1px dashed var(--cms-border);
  border-radius: var(--cms-radius);
  color: var(--cms-muted);
}
</style>
