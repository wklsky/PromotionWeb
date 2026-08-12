/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 14:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 14:00
 * @FilePath: apps/taiji-web/pages/contact.vue
 * @Description: 联系我们页（见 docs/13 §8 留资接口）。联系方式卡片 + 留言表单引导，主题色联动。
 */
<script setup lang="ts">
const channels = [
  { label: '合作邮箱', value: 'hello@taiji.com' },
  { label: '媒体咨询', value: 'press@taiji.com' },
  { label: '参观预约', value: '400-000-0000' },
  { label: '总部地址', value: '北京市朝阳区太极文化中心' },
];
</script>

<template>
  <section class="section aura-bg">
    <div class="container">
      <header class="contact-head">
        <span class="tag">联系我们</span>
        <h1 class="section-title">期待与您相遇</h1>
        <p class="section-sub">无论是合作、参观还是加入，欢迎留下您的讯息。</p>
      </header>

      <div class="contact-grid">
        <div class="contact-channels">
          <div v-for="c in channels" :key="c.label" class="card channel">
            <p class="channel__label">{{ c.label }}</p>
            <p class="channel__value">{{ c.value }}</p>
          </div>
        </div>

        <form class="card contact-form" @submit.prevent>
          <label class="field">
            <span class="field__label">称呼</span>
            <input class="field__input" type="text" placeholder="您的姓名" />
          </label>
          <label class="field">
            <span class="field__label">联系方式</span>
            <input class="field__input" type="text" placeholder="手机号或邮箱" />
          </label>
          <label class="field">
            <span class="field__label">留言</span>
            <textarea class="field__input" rows="4" placeholder="想对我们说的话" />
          </label>
          <button class="btn btn--primary" type="submit">提交留言</button>
        </form>
      </div>
    </div>
  </section>
</template>

<style scoped>
.contact-head {
  margin-bottom: 36px;
}
.contact-grid {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 24px;
}
.contact-channels {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  align-content: start;
}
.channel {
  padding: 22px;
}
.channel__label {
  margin: 0 0 8px;
  font-size: 13px;
  color: var(--c-muted);
}
.channel__value {
  margin: 0;
  font-weight: 600;
  color: var(--theme-primary);
}
.contact-form {
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}
.field__label {
  font-size: 14px;
  color: var(--c-muted);
}
.field__input {
  width: 100%;
  padding: 12px 14px;
  border-radius: var(--radius-sm);
  border: 1px solid var(--c-border-strong);
  background: var(--c-bg-soft);
  color: var(--theme-text);
  font-family: inherit;
  transition: border-color 200ms var(--ease);
}
.field__input:focus {
  outline: none;
  border-color: var(--theme-primary);
}
.contact-form .btn {
  align-self: flex-start;
}
@media (max-width: 820px) {
  .contact-grid {
    grid-template-columns: 1fr;
  }
}
</style>
