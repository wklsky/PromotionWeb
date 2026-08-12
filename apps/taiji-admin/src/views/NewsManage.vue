/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:40 * @FilePath: apps/taiji-admin/src/views/NewsManage.vue *
@Description: 新闻管理页（见 docs/11 §5），列表读取 + 新增/删除（接 docs/13 §2.3 写接口） */
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElMessage,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElSelect,
  ElOption,
} from 'element-plus';
import { listNews, createNews, deleteNews } from '~/api/news';
import type { NewsListVO, NewsDTO, NewsCategory, ContentStatus } from 'taiji-shared';

const list = ref<NewsListVO[]>([]);
const loading = ref(false);
const errorMsg = ref('');

// 后端 NewsController.list 返回 Result<PageResult<News>>，列表字段与 NewsListVO 对齐（见 docs/13 §2.2）
async function load() {
  loading.value = true;
  errorMsg.value = '';
  try {
    const res = await listNews({ page: 1, size: 50 });
    if (res.code === 0 && res.data) {
      list.value = res.data.list;
    } else {
      errorMsg.value = res.message || '加载失败';
    }
  } catch (e) {
    errorMsg.value = (e as Error).message;
  } finally {
    loading.value = false;
  }
}

onMounted(() => load());

// 新增/编辑弹窗：POST/PUT /api/news 需认证（见 SecurityConfig）
const dialogVisible = ref(false);
const form = ref<NewsDTO>({ title: '', category: '企业动态', content: '', status: 1 });
const submitting = ref(false);

function openCreate() {
  // NewsCategory 取值与 shared 枚举一致（中文：企业动态/行业资讯/技术文章，见 docs/13 §9）
  form.value = { title: '', category: '企业动态', content: '', status: 1 };
  dialogVisible.value = true;
}

async function submit() {
  submitting.value = true;
  try {
    const res = await createNews(form.value);
    if (res.code === 0) {
      ElMessage.success('新增成功');
      dialogVisible.value = false;
      await load();
    } else {
      ElMessage.error(res.message || '新增失败');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
  } finally {
    submitting.value = false;
  }
}

async function onDelete(row: NewsListVO) {
  try {
    const res = await deleteNews(row.id);
    if (res.code === 0) {
      ElMessage.success('删除成功');
      await load();
    } else {
      ElMessage.error(res.message || '删除失败');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
  }
}

// status 数字直出转文案（见 docs/13 §2.2 CONTENT_STATUS）
const statusText = (s: number) => (s === 1 ? '已发布' : '草稿');
</script>

<template>
  <div>
    <ElButton type="primary" class="mb-4" @click="openCreate">新增新闻</ElButton>
    <p v-if="errorMsg" class="text-red-500 mb-2">{{ errorMsg }}</p>
    <ElTable v-loading="loading" :data="list" border>
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="category" label="分类" />
      <ElTableColumn label="状态">
        <template #default="{ row }">{{ statusText((row as NewsListVO).status) }}</template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="120">
        <template #default="{ row }">
          <ElButton type="danger" link @click="onDelete(row as NewsListVO)">删除</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>

    <ElDialog v-model="dialogVisible" title="新增新闻">
      <ElForm :model="form" label-width="80px">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" />
        </ElFormItem>
        <ElFormItem label="分类">
          <ElSelect v-model="form.category">
            <ElOption label="企业动态" value="企业动态" />
            <ElOption label="行业资讯" value="行业资讯" />
            <ElOption label="技术文章" value="技术文章" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="内容">
          <ElInput v-model="form.content" type="textarea" :rows="4" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status">
            <ElOption label="草稿" :value="0" />
            <ElOption label="已发布" :value="1" />
          </ElSelect>
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" :loading="submitting" @click="submit">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>
