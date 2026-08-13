<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-admin/src/views/JobsManage.vue
 * @Description: 招聘管理页（见 docs/11 §5、docs/16 §4.3）。列表读取 + 新增/编辑/删除/状态切换
 *               （接 docs/13 §7 写接口，含 PUT /jobs/{id}）。岗位搜索、状态标签、发布/关闭切换。
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
  ElMessageBox,
} from 'element-plus';
import { listJobs, getJob, createJob, updateJob, deleteJob } from '~/api/jobs';
import { JOB_TYPES } from 'taiji-shared';
import type { JobListVO, JobDTO } from 'taiji-shared';
import { filterJobs, jobStatusLabel } from '~/utils/business';

const list = ref<JobListVO[]>([]);
const loading = ref(false);
const errorMsg = ref('');

async function load() {
  loading.value = true;
  errorMsg.value = '';
  try {
    const res = await listJobs({ page: 1, size: 100 });
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

// 搜索（岗位/部门/城市，见 docs/16 §4.3；逻辑抽至 utils/business 便于单测）
const keyword = ref('');
const filtered = computed(() => filterJobs(list.value, keyword.value));

// 新增/编辑弹窗
const dialogVisible = ref(false);
const submitting = ref(false);
const editingId = ref<number | null>(null);
const form = ref<JobDTO>({
  position: '',
  department: '',
  city: '',
  description: '',
  requirement: '',
  salary: '',
  type: '全职',
  status: 1,
});

function resetForm() {
  form.value = {
    position: '',
    department: '',
    city: '',
    description: '',
    requirement: '',
    salary: '',
    type: '全职',
    status: 1,
  };
}

function openCreate() {
  editingId.value = null;
  resetForm();
  dialogVisible.value = true;
}

// 编辑前先拉详情，回填职责/要求（避免只改列表可见字段丢失正文）
async function openEdit(row: JobListVO) {
  editingId.value = row.id;
  resetForm();
  try {
    const res = await getJob(row.id);
    if (res.code === 0 && res.data) {
      const d = res.data;
      form.value = {
        position: d.position,
        department: d.department ?? '',
        city: d.city ?? '',
        description: d.description ?? '',
        requirement: d.requirement ?? '',
        salary: d.salary ?? '',
        type: d.type,
        status: d.status,
      };
    } else {
      // 详情拿不到则用列表数据兜底（缺职责/要求，提示用户补全）
      form.value = {
        position: row.position,
        department: row.department ?? '',
        city: row.city ?? '',
        description: '',
        requirement: '',
        salary: row.salary ?? '',
        type: row.type,
        status: row.status,
      };
      ElMessage.warning(res.message || '详情加载不全，请补全职责与要求');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
    return;
  }
  dialogVisible.value = true;
}

async function submit() {
  if (!form.value.position.trim()) {
    ElMessage.warning('请填写岗位名称');
    return;
  }
  submitting.value = true;
  try {
    const res =
      editingId.value !== null
        ? await updateJob(editingId.value, form.value)
        : await createJob(form.value);
    if (res.code === 0) {
      ElMessage.success(editingId.value !== null ? '更新成功' : '新增成功');
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

async function onDelete(row: JobListVO) {
  try {
    await ElMessageBox.confirm(`确认删除「${row.position}」？`, '删除确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
    });
  } catch {
    return; // 用户取消
  }
  const res = await deleteJob(row.id);
  if (res.code === 0) {
    ElMessage.success('已删除');
    list.value = list.value.filter((x) => x.id !== row.id);
  } else {
    ElMessage.error(res.message || '删除失败');
  }
}

// 发布/关闭切换：先取详情回填再更新，避免 PUT 全量覆盖丢失职责/要求
async function onToggleStatus(row: JobListVO) {
  const next = row.status === 1 ? 0 : 1;
  try {
    const detailRes = await getJob(row.id);
    const d = detailRes.code === 0 && detailRes.data ? detailRes.data : null;
    const payload: JobDTO = {
      position: d?.position ?? row.position,
      department: d?.department ?? row.department ?? '',
      city: d?.city ?? row.city ?? '',
      description: d?.description ?? '',
      requirement: d?.requirement ?? '',
      salary: d?.salary ?? row.salary ?? '',
      type: d?.type ?? row.type,
      status: next,
    };
    const res = await updateJob(row.id, payload);
    if (res.code === 0) {
      ElMessage.success(next === 1 ? '已发布' : '已关闭');
      row.status = next;
    } else {
      ElMessage.error(res.message || '操作失败');
    }
  } catch (e) {
    ElMessage.error((e as Error).message);
  }
}
</script>

<template>
  <div class="cms-fade-in">
    <div class="cms-page-head">
      <div>
        <h2 class="cms-page-title">招聘管理</h2>
        <p class="cms-page-sub">岗位发布 · 人才招募</p>
      </div>
      <ElButton type="primary" @click="openCreate">+ 新增岗位</ElButton>
    </div>

    <p v-if="errorMsg" class="cms-error-tip">{{ errorMsg }}</p>

    <div class="cms-toolbar">
      <ElInput
        v-model="keyword"
        placeholder="搜索岗位 / 部门 / 城市"
        clearable
        class="cms-toolbar__search"
      />
    </div>

    <div class="cms-panel cms-table-wrap">
      <ElTable v-loading="loading" :data="filtered" style="width: 100%" empty-text="暂无岗位">
        <ElTableColumn prop="id" label="ID" width="70" />
        <ElTableColumn prop="position" label="岗位" min-width="160" />
        <ElTableColumn prop="department" label="部门" width="140">
          <template #default="{ row }">
            <span class="muted">{{ (row as JobListVO).department || '—' }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="city" label="城市" width="120">
          <template #default="{ row }">
            <span class="muted">{{ (row as JobListVO).city || '—' }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="salary" label="薪资" width="140">
          <template #default="{ row }">
            <span class="muted">{{ (row as JobListVO).salary || '面议' }}</span>
          </template>
        </ElTableColumn>
        <ElTableColumn prop="type" label="类型" width="100" />
        <ElTableColumn label="状态" width="100">
          <template #default="{ row }">
            <ElTag :type="jobStatusLabel((row as JobListVO).status).type" effect="dark">
              {{ jobStatusLabel((row as JobListVO).status).text }}
            </ElTag>
          </template>
        </ElTableColumn>
        <ElTableColumn label="操作" width="200" align="right">
          <template #default="{ row }">
            <ElButton link type="primary" @click="openEdit(row as JobListVO)">编辑</ElButton>
            <ElButton
              link
              :type="(row as JobListVO).status === 1 ? 'warning' : 'success'"
              @click="onToggleStatus(row as JobListVO)"
            >
              {{ (row as JobListVO).status === 1 ? '关闭' : '发布' }}
            </ElButton>
            <ElButton type="danger" link @click="onDelete(row as JobListVO)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>
    </div>

    <ElDialog
      v-model="dialogVisible"
      :title="editingId !== null ? '编辑岗位' : '新增岗位'"
      width="560px"
    >
      <ElForm :model="form" label-width="80px">
        <ElFormItem label="岗位" required>
          <ElInput v-model="form.position" placeholder="如：前端工程师" />
        </ElFormItem>
        <ElFormItem label="部门">
          <ElInput v-model="form.department" placeholder="如：研发中心" />
        </ElFormItem>
        <ElFormItem label="城市">
          <ElInput v-model="form.city" placeholder="如：上海" />
        </ElFormItem>
        <ElFormItem label="薪资">
          <ElInput v-model="form.salary" placeholder="如：15-25K" />
        </ElFormItem>
        <ElFormItem label="类型">
          <ElSelect v-model="form.type">
            <ElOption v-for="t in JOB_TYPES" :key="t" :label="t" :value="t" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status">
            <ElOption label="招聘中" :value="1" />
            <ElOption label="已关闭" :value="0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="职责">
          <ElInput v-model="form.description" type="textarea" :rows="3" placeholder="岗位描述" />
        </ElFormItem>
        <ElFormItem label="要求">
          <ElInput v-model="form.requirement" type="textarea" :rows="3" placeholder="任职要求" />
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
  max-width: 320px;
}
.cms-table-wrap {
  padding: 8px 12px;
  overflow: hidden;
}
.muted {
  color: var(--cms-muted);
  font-size: 13px;
}
</style>
