/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-admin/src/views/NewsManage.vue *
@Description: 新闻管理页（见 docs/11 §5），Element Plus 表格 + 共享 NewsVO 类型 */
<script setup lang="ts">
import { ref } from 'vue';
import { ElTable, ElTableColumn, ElButton } from 'element-plus';
import type { NewsVO } from 'taiji-shared';

// 示例数据；真实场景由后端接口拉取（见 docs/13、docs/14）
const list = ref<NewsVO[]>([
  {
    id: 1,
    title: '示例新闻',
    category: 'company',
    status: 'published',
    author: 'admin',
    createTime: '',
    updateTime: '',
  },
]);
</script>

<template>
  <div>
    <ElButton type="primary" class="mb-4">新增新闻</ElButton>
    <ElTable :data="list" border>
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="title" label="标题" />
      <ElTableColumn prop="category" label="分类" />
      <ElTableColumn prop="status" label="状态" />
    </ElTable>
  </div>
</template>
