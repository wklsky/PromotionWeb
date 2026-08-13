<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/NewsManage.vue
 * @Description: 新闻管理页（见 docs/11 §5、docs/16 §4.3）。列表读取 + 新增/编辑/删除（接 docs/13 §2.3 写接口）。
 *               封面缩略图、搜索/分类筛选、分页、发布时间列、编辑复用弹窗。
 -->
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
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
  ElTag,
} from 'element-plus';
import { listNews, createNews, updateNews, deleteNews, getNews } from '~/api/news';
import { NEWS_CATEGORIES } from 'taiji-shared';
import type { NewsListVO, NewsDTO } from 'taiji-shared';
import { filterNews, paginate, statusNewsLabel, totalPages as calcTotalPages } from '~/utils/business';

const list = ref<NewsListVO[]>([]);
const loading = ref(false);
const errorMsg = ref('');

async function load() {
  loading.value = true;
  errorMsg.value = '';
  try {
    const res = await listNews({ page: 1, size: 100 });
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

// 搜索 + 分类筛选（客户端，逻辑抽至 utils/business 便于单测，见 docs/16 §4.4）
const keyword = ref('');
const catFilter = ref<string>('全部');
const cats = ['全部', ...NEWS_CATEGORIES];
const filtered = computed(() => filterNews(list.value, keyword.value, catFilter.value));

// 分页（客户端）
const pageSize = ref(10);
const page = ref(1);
const totalPages = computed(() => calcTotalPages(filtered.value.length, pageSize.value));
const paged = computed(() => paginate(filtered.value, page.value, pageSize.value));
const pageSizes = [10, 20, 50];
const sizeModel = ref(10);
function setSize(v: number): void {
  pageSize.value = v;
}

// 新增/编辑弹窗
const dialogVisible = ref(false);
const editing = ref(false);
const form = ref<NewsDTO & { id?: number }>({
  title: '',
  category: '企业动态',
  content: '',
  status: 1,
});
const submitting = ref(false);

function openCreate() {
  editing.value = false;
  form.value = { title: '', category: '企业动态', content: '', status: 1 };
  dialogVisible.value = true;
}

async function openEdit(row: NewsListVO) {
  editing.value = true;
  // 先以列表行兜底填充（content 列表接口不返回），再尝试拉取详情回填正文，
  // 避免直接用空 content 覆盖后端已有正文（docs/16 §4.4 修复点）
  form.value = {
    id: row.id,
    title: row.title,
    category: row.category,
    content: '',
    status: row.status === 1 ? 1 : 0,
  };
  try {
    const res = await getNews(row.id);
    if (res.code === 0 && res.data) {
      form.value = {
        id: row.id,
        title: res.data.title,
        category: res.data.category,
        content: res.data.content ?? '',
        status: res.data.status === 1 ? 1 : 0,
      };
    }
  } catch {
    ElMessage.warning('未能获取正文，保存将保留原内容');
  }
  dialogVisible.value = true;
}

async function submit() {
  submitting.value = true;
  try {
    const res = editing.value
      ? await updateNews(form.value.id!, form.value as NewsDTO)
      : await createNews(form.value as NewsDTO);
    if (res.code === 0) {
      ElMessage.success(editing.value ? '更新成功' : '新增成功');
      dialogVisible.value = false;
      await load();
    } else {
      ElMessage.error(res.message || '操作失败');
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

// status 数字转文案与类型（见 docs/13 §2.2 CONTENT_STATUS；逻辑抽至 utils/business）
const statusMeta = statusNewsLabel;
</script>

<template>
  <div class="cms-fade-in">
    <div class="cms-page-head">
      <div>
        <h2 class="cms-page-title">新闻管理</h2>
        <p class="cms-page-sub">企业动态 · 行业资讯 · 技术文章</p>
      </div>
      <ElButton type="primary" @click="openCreate">+ 新增新闻</ElButton>
    </div>

    <p v-if="errorMsg" class="cms-error-tip">{{ errorMsg }}</p>

    <div class="cms-toolbar">
      <ElInput
        v-model="keyword"
        placeholder="搜索标题"
        clearable
        class="cms-toolbar__search"
        @input="page = 1"
      />
      <ElSelect v-model="catFilter" class="cms-toolbar__cat" @change="page = 1">
        <ElOption v-for="c in cats" :key="c" :label="c" :value="c" />
      </ElSelect>
    </div>

    <div class="cms-panel cms-table-wrap">
      <ElTable v-loading="loading" :data="paged" style="width: 100%" empty-text="暂无新闻">
        <ElTableColumn prop="id" label="ID" width="70" />
        <ElTableColumn label="封面" width="84">
          <template #default="{ row }">
            <img
              v-if="(row as NewsListVO).cover?.startsWith('http')"
              :src="(row as NewsListVO).cover!"
              class="news-thumb"
              :alt="(row as NewsListVO).title"
            />
            <span v-else class="news-thumb news-thumb--ph">{{ (row as NewsListVO).title.slice(0, 1) }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="title" label="标题" min-width="200" />
        <ElTableColumn prop="category" label="分类" width="120" />
        <ElTableColumn label="状态" width="100">
          <template #default="{ row }">
            <ElTag :type="statusMeta((row as NewsListVO).status).type" effect="dark">
              {{ statusMeta((row as NewsListVO).status).text }}
            </ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn label="发布时间" width="130">
          <template #default="{ row }">
            <span class="news-time">{{ (row as NewsListVO).publishTime?.slice(0, 10) || '—' }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" width="150" align="right">
          <template #default="{ row }">
            <ElButton text type="primary" @click="openEdit(row as NewsListVO)">编辑</ElButton>
            <ElButton type="danger" link @click="onDelete(row as NewsListVO)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>

      <div v-if="totalPages > 1" class="cms-pager">
        <button class="cms-pager__btn" :disabled="page <= 1" @click="page--">上一页</button>
        <span class="cms-pager__info">第 {{ page }} / {{ totalPages }} 页</span>
        <button class="cms-pager__btn" :disabled="page >= totalPages" @click="page++">下一页</button>
        <ElSelect v-model="sizeModel" class="cms-pager__size" @change="setSize">
          <ElOption v-for="s in pageSizes" :key="s" :label="`${s} 条`" :value="s" />
        </ElSelect>
        <span class="sr-only">每页 {{ pageSize }} 条</span>
      </div>
    </div>

    <ElDialog v-model="dialogVisible" :title="editing ? '编辑新闻' : '新增新闻'" width="520px">
      <ElForm :model="form" label-width="80px">
        <ElFormItem label="标题">
          <ElInput v-model="form.title" placeholder="请输入标题" />
        </ElFormItem>
        <ElFormItem label="分类">
          <ElSelect v-model="form.category">
            <ElOption v-for="c in NEWS_CATEGORIES" :key="c" :label="c" :value="c" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="内容">
          <ElInput v-model="form.content" type="textarea" :rows="4" placeholder="请输入正文" />
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
.cms-table-wrap {
  padding: 8px 12px;
  overflow: hidden;
}
.news-thumb {
  width: 56px;
  height: 40px;
  border-radius: 6px;
  object-fit: cover;
}
.news-thumb--ph {
  display: grid;
  place-items: center;
  background: var(--cms-primary-soft);
  color: var(--cms-primary);
  font-weight: 700;
}
.news-time {
  color: var(--cms-muted);
  font-size: 13px;
}
.cms-pager {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 4px 4px;
}
.cms-pager__btn {
  padding: 6px 14px;
  border-radius: var(--cms-radius);
  border: 1px solid var(--cms-border);
  background: var(--cms-panel);
  color: var(--cms-text);
  cursor: pointer;
}
.cms-pager__btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}
.cms-pager__info {
  color: var(--cms-muted);
  font-size: 13px;
}
.cms-pager__size {
  margin-left: auto;
  width: 110px;
}
</style>
