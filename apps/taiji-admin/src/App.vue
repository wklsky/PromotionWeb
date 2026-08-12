/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-admin/src/App.vue
 * @Description: CMS 后台根布局（见 docs/11 §5）：品牌侧边栏 + 顶栏（面包屑/登出）+ 路由视图。
 *              菜单激活态联动路由，登出清除本地 token 回登录页（与 router 守卫一致）。
 */
<script setup lang="ts">
import { computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMenu, ElMenuItem, ElContainer, ElAside, ElHeader, ElMain, ElButton, ElMessage } from 'element-plus';
import { useUserStore } from '~/stores/user';

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

// 侧边栏菜单：与 router 路由一一对应（见 router/index.ts）
const menus = [
  { index: '/news', label: '新闻管理', icon: '📰' },
  { index: '/media', label: '媒体库', icon: '🖼️' },
];

// 当前激活菜单：精确匹配路径前缀
const activeMenu = computed(() => route.path);

// 顶栏面包屑文案
const pageTitle = computed(
  () => menus.find((m) => route.path.startsWith(m.index))?.label ?? '太极馆 CMS',
);

function onLogout(): void {
  // 清除本地登录态（localStorage 'token'），并交由路由守卫拦截回登录页
  userStore.logout();
  ElMessage.success('已退出登录');
  router.replace('/login');
}
</script>

<template>
  <ElContainer class="h-screen cms-shell">
    <ElAside width="240px" class="cms-aside">
      <div class="cms-brand">
        <span class="cms-brand__mark" aria-hidden="true">太</span>
        <div class="cms-brand__text">
          <p class="cms-brand__name">太极馆</p>
          <p class="cms-brand__role">CMS 后台</p>
        </div>
      </div>

      <ElMenu :default-active="activeMenu" router class="cms-menu" background-color="transparent">
        <ElMenuItem v-for="m in menus" :key="m.index" :index="m.index">
          <span class="cms-menu__icon">{{ m.icon }}</span>
          <span>{{ m.label }}</span>
        </ElMenuItem>
      </ElMenu>

      <div class="cms-aside__foot">
        <p class="cms-aside__tip">内容管理 · 媒体资产</p>
      </div>
    </ElAside>

    <ElContainer>
      <ElHeader class="cms-header">
        <div class="cms-breadcrumb">
          <span class="cms-breadcrumb__root">控制台</span>
          <span class="cms-breadcrumb__sep">/</span>
          <span class="cms-breadcrumb__cur">{{ pageTitle }}</span>
        </div>
        <div class="cms-header__right">
          <span class="cms-user">
            <span class="cms-user__avatar">{{ (userStore.user?.username || '管').slice(0, 1) }}</span>
            <span class="cms-user__name">{{ userStore.user?.username || '管理员' }}</span>
          </span>
          <ElButton text class="cms-logout" @click="onLogout">退出登录</ElButton>
        </div>
      </ElHeader>

      <ElMain class="cms-main">
        <RouterView v-slot="{ Component }">
          <component :is="Component" class="cms-fade-in" />
        </RouterView>
      </ElMain>
    </ElContainer>
  </ElContainer>
</template>

<style scoped>
.cms-shell {
  background: var(--cms-bg);
}
.cms-aside {
  background: var(--cms-bg-soft);
  border-right: 1px solid var(--cms-border);
  display: flex;
  flex-direction: column;
  padding: 18px 14px;
}
.cms-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 10px 18px;
}
.cms-brand__mark {
  display: grid;
  place-items: center;
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background: var(--cms-primary);
  color: #0f1115;
  font-weight: 700;
  font-size: 18px;
  box-shadow: 0 0 18px var(--cms-primary-soft);
}
.cms-brand__name {
  margin: 0;
  font-weight: 700;
  font-size: 17px;
}
.cms-brand__role {
  margin: 2px 0 0;
  font-size: 12px;
  color: var(--cms-muted);
}
.cms-menu {
  flex: 1;
  border-right: none;
}
.cms-menu :deep(.el-menu-item) {
  border-radius: 10px;
  margin-bottom: 6px;
  color: var(--cms-muted);
  transition: background 200ms var(--cms-ease), color 200ms var(--cms-ease);
}
.cms-menu :deep(.el-menu-item.is-active) {
  background: var(--cms-primary-soft);
  color: var(--cms-primary);
  font-weight: 600;
}
.cms-menu :deep(.el-menu-item:hover) {
  background: rgba(255, 255, 255, 0.04);
  color: var(--cms-text);
}
.cms-menu__icon {
  margin-right: 10px;
}
.cms-aside__foot {
  padding: 12px 10px 0;
  border-top: 1px solid var(--cms-border);
}
.cms-aside__tip {
  margin: 0;
  font-size: 12px;
  color: var(--cms-muted);
}
.cms-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--cms-bg-soft);
  border-bottom: 1px solid var(--cms-border);
  height: 60px;
}
.cms-breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}
.cms-breadcrumb__root {
  color: var(--cms-muted);
}
.cms-breadcrumb__sep {
  color: var(--cms-border-strong);
}
.cms-breadcrumb__cur {
  color: var(--cms-text);
  font-weight: 600;
}
.cms-header__right {
  display: flex;
  align-items: center;
  gap: 14px;
}
.cms-user {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}
.cms-user__avatar {
  display: grid;
  place-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: var(--cms-border-strong);
  color: var(--cms-text);
  font-size: 13px;
}
.cms-logout {
  color: var(--cms-muted);
}
.cms-logout:hover {
  color: var(--cms-danger);
}
.cms-main {
  background: var(--cms-bg);
  padding: 24px;
}
</style>
