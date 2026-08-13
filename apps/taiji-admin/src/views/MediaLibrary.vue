<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/MediaLibrary.vue
 * @Description: 媒体库（见 docs/11 §5、docs/16 §4.3）。上传 + 列表读取，卡片网格；预览弹窗 + 删除。
 -->
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { ElUpload, ElButton, ElMessage, ElDialog, ElMessageBox, type UploadRequestOptions } from 'element-plus';
import { http } from '~/api/request';
import { listMedia, deleteMedia } from '~/api/media';
import type { MediaVO } from 'taiji-shared';
import { filterMedia } from '~/utils/business';

const list = ref<MediaVO[]>([]);
const loading = ref(false);
const keyword = ref('');

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

// 搜索（按名称，逻辑抽至 utils/business 便于单测，见 docs/16 §4.4）
const filtered = computed(() => filterMedia(list.value, keyword.value));

onMounted(() => load());

// 自定义上传：经 request 实例携带 JWT，避免原生 action 不附加 Authorization 导致写操作 401
type UploadErr = Parameters<NonNullable<UploadRequestOptions['onError']>>[0];
const customUpload = async (options: UploadRequestOptions): Promise<void> => {
  const form = new FormData();
  form.append('file', options.file);
  try {
    const res = await http.post<MediaVO>('/media/upload', form);
    if (res.code === 0 && res.data) {
      list.value.unshift(res.data);
      ElMessage.success('上传成功');
      options.onSuccess(res.data);
    } else {
      const msg = res.message || '上传失败';
      ElMessage.error(msg);
      options.onError(new Error(msg) as unknown as UploadErr);
    }
  } catch (e) {
    const msg = (e as Error).message;
    ElMessage.error(msg);
    options.onError(new Error(msg) as unknown as UploadErr);
  }
};

// 预览弹窗
const previewVisible = ref(false);
const previewData = ref<MediaVO | null>(null);
function openPreview(m: MediaVO): void {
  previewData.value = m;
  previewVisible.value = true;
}

// 删除（docs/16 §4.3）
async function onDelete(m: MediaVO) {
  try {
    await ElMessageBox.confirm(`确认删除「${m.name}」？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
    });
  } catch {
    return; // 用户取消
  }
  const res = await deleteMedia(m.id);
  if (res.code === 0) {
    ElMessage.success('已删除');
    list.value = list.value.filter((x) => x.id !== m.id);
  } else {
    ElMessage.error(res.message || '删除失败');
  }
}
</script>

<template>
  <div class="cms-fade-in">
    <div class="cms-page-head">
      <div>
        <h2 class="cms-page-title">媒体库</h2>
        <p class="cms-page-sub">图片 / 视频素材管理</p>
      </div>
      <ElUpload :http-request="customUpload" :show-file-list="false" list-type="picture-card">
        <ElButton type="primary">+ 上传素材</ElButton>
      </ElUpload>
    </div>

    <div class="cms-toolbar">
      <ElInput
        v-model="keyword"
        placeholder="搜索素材名称"
        clearable
        class="cms-toolbar__search"
      />
    </div>

    <div v-loading="loading" class="media-grid">
      <div
        v-for="m in filtered"
        :key="m.id"
        class="cms-panel media-card"
        @click="openPreview(m)"
      >
        <div class="media-card__media">
          <img v-if="m.url.startsWith('http')" :src="m.url" :alt="m.name" loading="lazy" />
          <div v-else class="media-card__ph">{{ (m.name || '素材').slice(0, 1) }}</div>
        </div>
        <div class="media-card__foot">
          <span class="media-card__name" :title="m.name">{{ m.name }}</span>
          <div class="media-card__actions">
            <button class="media-card__del" title="删除" @click.stop="onDelete(m)">✕</button>
          </div>
        </div>
      </div>

      <div v-if="!loading && !filtered.length" class="media-empty">
        {{ keyword ? '无匹配素材' : '暂无素材，点击右上角上传' }}
      </div>
    </div>

    <!-- 预览弹窗 -->
    <ElDialog v-model="previewVisible" title="素材预览" width="640px">
      <div v-if="previewData" class="media-preview">
        <img v-if="previewData.url.startsWith('http')" :src="previewData.url" :alt="previewData.name" class="media-preview__img" />
        <div v-else class="media-preview__ph">{{ (previewData.name || '素材').slice(0, 1) }}</div>
        <dl class="media-preview__meta">
          <div><dt>名称</dt><dd>{{ previewData.name }}</dd></div>
          <div><dt>类型</dt><dd>{{ previewData.type || 'file' }}</dd></div>
          <div><dt>大小</dt><dd>{{ previewData.size ? (previewData.size / 1024).toFixed(1) + ' KB' : '—' }}</dd></div>
          <div><dt>上传</dt><dd>{{ previewData.createTime?.slice(0, 19) || '—' }}</dd></div>
        </dl>
      </div>
    </ElDialog>
  </div>
</template>

<style scoped>
.cms-toolbar {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}
.cms-toolbar__search {
  max-width: 280px;
}
.media-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
  min-height: 160px;
}
.media-card {
  overflow: hidden;
  cursor: pointer;
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
.media-card__del {
  flex-shrink: 0;
  width: 26px;
  height: 26px;
  border-radius: 8px;
  border: 1px solid var(--cms-border);
  background: transparent;
  color: var(--cms-muted);
  cursor: pointer;
  transition: color 160ms var(--cms-ease), border-color 160ms var(--cms-ease);
}
.media-card__del:hover {
  color: var(--cms-danger);
  border-color: var(--cms-danger);
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
.media-preview__img {
  width: 100%;
  max-height: 60vh;
  object-fit: contain;
  border-radius: var(--cms-radius);
  background: var(--cms-bg);
}
.media-preview__ph {
  display: grid;
  place-items: center;
  height: 200px;
  font-size: 64px;
  font-weight: 700;
  color: var(--cms-primary);
}
.media-preview__meta {
  margin: 16px 0 0;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}
.media-preview__meta div {
  display: flex;
  gap: 8px;
}
.media-preview__meta dt {
  color: var(--cms-muted);
  font-size: 13px;
}
.media-preview__meta dd {
  margin: 0;
  font-size: 13px;
}
</style>
