/** * @Author: wj 3363891051@qq.com * @Date: 2026-08-12 10:00 * @LastEditors: wj 3363891051@qq.com *
@LastEditTime: 2026-08-12 10:00 * @FilePath: apps/taiji-web/components/three/TaiChiModel.vue *
@Description: 首页 Hero 3D 太极球占位组件（见 docs/09 §5）。仅挂载/卸载/尺寸自适应，< 100 行 */
<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';

// 仅在桌面端且用户未要求减少动效时挂载 WebGL（见 docs/09 §5.1 强制降级）
const canvasRef = ref<HTMLCanvasElement | null>(null);
let raf = 0;

onMounted(() => {
  const canRender =
    window.innerWidth >= 1024 && !window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (!canRender || !canvasRef.value) return;
  const ctx = canvasRef.value.getContext('2d');
  // TODO: 接入 useThreeScene 渲染真实 Three.js 太极球与阴阳 Shader（见 docs/09 §5.3）
  const draw = (t: number): void => {
    if (!ctx || !canvasRef.value) return;
    ctx.clearRect(0, 0, canvasRef.value.width, canvasRef.value.height);
    ctx.save();
    ctx.translate(canvasRef.value.width / 2, canvasRef.value.height / 2);
    ctx.rotate(t / 1000);
    ctx.strokeStyle = getComputedStyle(document.body).getPropertyValue('--theme-primary');
    ctx.lineWidth = 2;
    ctx.beginPath();
    ctx.arc(0, 0, 80, 0, Math.PI * 2);
    ctx.stroke();
    ctx.restore();
    raf = requestAnimationFrame(draw);
  };
  raf = requestAnimationFrame(draw);
});

onUnmounted(() => {
  cancelAnimationFrame(raf);
});
</script>

<template>
  <canvas ref="canvasRef" width="600" height="600" aria-label="太极旋转视觉" />
</template>
