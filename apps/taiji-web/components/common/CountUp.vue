<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-web/components/common/CountUp.vue
 * @Description: 数字增长计数组件（见 docs/16 §4.3）。进入视口时从 0 缓动到目标值，
 *               reduced-motion 直接显示终值（降级）。配合 about.vue 数据展示。
 -->
<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import gsap from 'gsap';

const props = withDefaults(
  defineProps<{
    /** 目标数值 */
    value: number;
    /** 后缀（如 + / %） */
    suffix?: string;
    /** 时长(s) */
    duration?: number;
  }>(),
  { suffix: '', duration: 1.4 },
);

const display = ref(0);
const el = ref<HTMLElement | null>(null);
let io: IntersectionObserver | null = null;
let tween: gsap.core.Tween | null = null;

onMounted(() => {
  const reduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (reduced || !el.value) {
    display.value = props.value;
    return;
  }
  const obj = { v: 0 };
  io = new IntersectionObserver(
    (entries) => {
      if (entries[0]?.isIntersecting) {
        tween = gsap.to(obj, {
          v: props.value,
          duration: props.duration,
          ease: 'power2.out',
          onUpdate: () => (display.value = Math.round(obj.v)),
        });
        io?.disconnect();
      }
    },
    { threshold: 0.4 },
  );
  io.observe(el.value);
});

onBeforeUnmount(() => {
  io?.disconnect();
  io = null;
  tween?.kill();
  tween = null;
});
</script>

<template>
  <span ref="el">{{ display }}{{ suffix }}</span>
</template>
