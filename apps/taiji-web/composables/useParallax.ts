/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-12 10:00
 * @LastEditors: wj 3363891051@qq.com
 * @LastEditTime: 2026-08-12 10:00
 * @FilePath: apps/taiji-web/composables/useParallax.ts
 * @Description: 鼠标视差，返回归一化位移 ref，供 3D 相机或 CSS 缓动使用
 */
import { onMounted, onUnmounted, ref } from 'vue';

/**
 * 监听 pointermove，输出 -0.5~0.5 的归一化坐标。
 * 必须在 onUnmounted 解绑，避免内存泄漏（见前端规范）。
 */
export function useParallax(): {
  x: ReturnType<typeof ref<number>>;
  y: ReturnType<typeof ref<number>>;
} {
  const x = ref(0);
  const y = ref(0);

  const onMove = (e: PointerEvent): void => {
    x.value = e.clientX / window.innerWidth - 0.5;
    y.value = e.clientY / window.innerHeight - 0.5;
  };

  onMounted(() => window.addEventListener('pointermove', onMove, { passive: true }));
  onUnmounted(() => window.removeEventListener('pointermove', onMove));

  return { x, y };
}
