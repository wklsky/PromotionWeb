/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-admin/src/App.vue * @Description: CMS
根组件，渲染侧边栏与路由视图 */
<script setup lang="ts">
import { ElMenu, ElMenuItem, ElContainer, ElAside, ElHeader, ElMain } from 'element-plus';
</script>

<template>
  <ElContainer class="h-screen">
    <ElAside width="200px">
      <ElMenu router>
        <ElMenuItem index="/news">新闻管理</ElMenuItem>
        <ElMenuItem index="/media">媒体库</ElMenuItem>
      </ElMenu>
    </ElAside>
    <ElContainer>
      <ElHeader>太极馆 · CMS 后台</ElHeader>
      <ElMain>
        <RouterView />
      </ElMain>
    </ElContainer>
  </ElContainer>
</template>
