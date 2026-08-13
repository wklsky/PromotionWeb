<!--
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-13
 * @FilePath: apps/taiji-web/components/three/TaiChiModel.vue
 * @Description: 首页 Hero 真实 Three.js 太极球（见 docs/09 §5、docs/16 §4.2）。
 *               阴阳太极贴图 + 主题色材质/光照 + 外层科技感线框；桌面端且非 reduced-motion 启用，
 *               移动端/降级由使用方(SiteHero)回退到 CSS 环（docs/09 §5.1 强制降级）。
 -->
<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref } from 'vue';
import * as THREE from 'three';
import { useParallax } from '~/composables/useParallax';

const canvasRef = ref<HTMLCanvasElement | null>(null);
const { x: px, y: py } = useParallax();

let renderer: THREE.WebGLRenderer | null = null;
let frame = 0;
let ro: ResizeObserver | null = null;
// 资源引用提升到外层，便于卸载时精确释放（避免 GPU 资源泄漏，见下 onBeforeUnmount）
let sphere: THREE.Mesh | null = null;
let wire: THREE.Mesh | null = null;
let tex: THREE.CanvasTexture | null = null;

/** 读取当前主题主色（运行时由 useTheme 写入 :root） */
function themePrimary(): string {
  if (typeof window === 'undefined') return '#d4af37';
  const v = getComputedStyle(document.documentElement).getPropertyValue('--theme-primary').trim();
  return v || '#d4af37';
}

/** 绘制阴阳太极贴图（白=阳，黑=阴），外环描主题色，保持太极可辨识 */
function makeTaiChiTexture(accent: string): THREE.CanvasTexture {
  const size = 512;
  const cv = document.createElement('canvas');
  cv.width = cv.height = size;
  const ctx = cv.getContext('2d')!;
  const cx = size / 2;
  const cy = size / 2;
  const r = size * 0.44;
  const light = '#f4f4f4';
  const dark = '#101012';

  // 底色
  ctx.fillStyle = dark;
  ctx.fillRect(0, 0, size, size);
  // 整圆白底
  ctx.fillStyle = light;
  ctx.beginPath();
  ctx.arc(cx, cy, r, 0, Math.PI * 2);
  ctx.fill();
  // 左半黑
  ctx.fillStyle = dark;
  ctx.beginPath();
  ctx.arc(cx, cy, r, Math.PI / 2, Math.PI * 1.5);
  ctx.closePath();
  ctx.fill();
  // 下方白色凸（进入黑半）
  ctx.fillStyle = light;
  ctx.beginPath();
  ctx.arc(cx, cy + r / 2, r / 2, 0, Math.PI * 2);
  ctx.fill();
  // 上方黑色凸（进入白半）
  ctx.fillStyle = dark;
  ctx.beginPath();
  ctx.arc(cx, cy - r / 2, r / 2, 0, Math.PI * 2);
  ctx.fill();
  // 阴中阳点（白凸内黑点）
  ctx.fillStyle = dark;
  ctx.beginPath();
  ctx.arc(cx, cy + r / 2, r / 8, 0, Math.PI * 2);
  ctx.fill();
  // 阳中阴点（黑凸内白点）
  ctx.fillStyle = light;
  ctx.beginPath();
  ctx.arc(cx, cy - r / 2, r / 8, 0, Math.PI * 2);
  ctx.fill();
  // 外环主题色描边
  ctx.strokeStyle = accent;
  ctx.lineWidth = 6;
  ctx.beginPath();
  ctx.arc(cx, cy, r + 4, 0, Math.PI * 2);
  ctx.stroke();
  // 顶/底小点主题色描边高亮
  ctx.fillStyle = accent;
  ctx.beginPath();
  ctx.arc(cx, cy + r / 2, r / 16, 0, Math.PI * 2);
  ctx.fill();
  ctx.beginPath();
  ctx.arc(cx, cy - r / 2, r / 16, 0, Math.PI * 2);
  ctx.fill();

  const tex = new THREE.CanvasTexture(cv);
  tex.colorSpace = THREE.SRGBColorSpace;
  tex.anisotropy = 4;
  return tex;
}

onMounted(() => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const canRender =
    window.innerWidth >= 1024 && !window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  if (!canRender) return; // 降级：SiteHero 回退 CSS 环

  const accent = themePrimary();
  const parent = canvas.parentElement!;
  const getSize = () => {
    const w = parent.clientWidth || 360;
    return { w, h: parent.clientHeight || 360 };
  };

  renderer = new THREE.WebGLRenderer({ canvas, antialias: true, alpha: true });
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));
  let { w, h } = getSize();
  renderer.setSize(w, h, false);

  const scene = new THREE.Scene();
  const camera = new THREE.PerspectiveCamera(45, w / h, 0.1, 100);
  camera.position.z = 4.2;

  // 太极球
  tex = makeTaiChiTexture(accent);
  const sphereMat = new THREE.MeshStandardMaterial({
    map: tex,
    metalness: 0.35,
    roughness: 0.45,
    emissive: new THREE.Color(accent),
    emissiveIntensity: 0.1,
  });
  sphere = new THREE.Mesh(new THREE.SphereGeometry(1.4, 64, 64), sphereMat);
  scene.add(sphere);

  // 外层科技感线框（主题色）
  wire = new THREE.Mesh(
    new THREE.IcosahedronGeometry(1.75, 1),
    new THREE.MeshBasicMaterial({ color: new THREE.Color(accent), wireframe: true, transparent: true, opacity: 0.16 }),
  );
  scene.add(wire);

  // 光照（主题色定向 + 环境）
  scene.add(new THREE.AmbientLight(0xffffff, 0.9));
  const key = new THREE.DirectionalLight(new THREE.Color(accent), 1.1);
  key.position.set(2, 2, 3);
  scene.add(key);
  const rim = new THREE.PointLight(new THREE.Color(accent), 0.6, 20);
  rim.position.set(-3, -1, 2);
  scene.add(rim);

  const clock = new THREE.Clock();
  const tick = (): void => {
    const t = clock.getElapsedTime();
    sphere.rotation.y = t * 0.35;
    sphere.rotation.x = Math.sin(t * 0.3) * 0.12;
    wire.rotation.y = -t * 0.12;
    wire.rotation.z = t * 0.05;
    // 鼠标视差（useParallax 归一化 -0.5~0.5）
    camera.position.x += (px.value * 0.6 - camera.position.x) * 0.05;
    camera.position.y += (-py.value * 0.6 - camera.position.y) * 0.05;
    camera.lookAt(0, 0, 0);
    renderer!.render(scene, camera);
    frame = requestAnimationFrame(tick);
  };
  tick();

  ro = new ResizeObserver(() => {
    if (!renderer) return;
    const s = getSize();
    w = s.w;
    h = s.h;
    camera.aspect = w / h;
    camera.updateProjectionMatrix();
    renderer.setSize(w, h, false);
  });
  ro.observe(parent);
});

onBeforeUnmount(() => {
  cancelAnimationFrame(frame);
  ro?.disconnect();
  ro = null;
  // 释放几何体 / 材质 / 贴图，避免离开首页时 GPU 资源泄漏
  const disposeMaterial = (m: THREE.Material | THREE.Material[] | undefined): void => {
    if (!m) return;
    if (Array.isArray(m)) m.forEach((x) => x.dispose());
    else m.dispose();
  };
  if (sphere) {
    sphere.geometry.dispose();
    disposeMaterial(sphere.material);
    sphere = null;
  }
  if (wire) {
    wire.geometry.dispose();
    disposeMaterial(wire.material);
    wire = null;
  }
  tex?.dispose();
  tex = null;
  renderer?.forceContextLoss();
  renderer?.dispose();
  renderer = null;
});
</script>

<template>
  <canvas ref="canvasRef" class="taiji-canvas" aria-label="太极旋转视觉" />
</template>

<style scoped>
.taiji-canvas {
  width: 100%;
  height: 100%;
  display: block;
}
</style>
