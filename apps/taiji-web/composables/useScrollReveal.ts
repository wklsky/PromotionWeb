/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-web/composables/useScrollReveal.ts
 * @Description: 滚动揭示指令 v-reveal（见 docs/16 §4.2）。用 IntersectionObserver 触发、GSAP 执行渐入+上移，
 *               元素进入视口时播放；reduced-motion 或 SSR 环境直接保持可见（降级）。由 plugins/reveal.ts 注册。
 */
import type { Directive } from 'vue';
import gsap from 'gsap';

interface RevealOpts {
  /** 上移距离(px)，默认 24 */
  y?: number;
  /** 延迟(s)，用于错峰 */
  delay?: number;
  /** 时长(s)，默认 0.7 */
  duration?: number;
}

export const vReveal: Directive<HTMLElement, RevealOpts | undefined> = {
  mounted(el, binding) {
    if (typeof window === 'undefined') return;
    const reduced = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    if (reduced) {
      gsap.set(el, { opacity: 1, y: 0 });
      return;
    }
    // 兜底：环境不支持 IntersectionObserver 时直接可见，避免内容永久隐藏
    if (!('IntersectionObserver' in window)) {
      gsap.set(el, { opacity: 1, y: 0 });
      return;
    }
    const { y = 24, delay = 0, duration = 0.7 } = binding.value ?? {};
    gsap.set(el, { opacity: 0, y, willChange: 'transform, opacity' });
    const io = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            gsap.to(el, { opacity: 1, y: 0, duration, delay, ease: 'power2.out', overwrite: true });
            io.unobserve(el);
          }
        });
      },
      { threshold: 0.12, rootMargin: '0px 0px -8% 0px' },
    );
    io.observe(el);
    (el as unknown as { __revealIO?: IntersectionObserver }).__revealIO = io;
  },
  unmounted(el) {
    (el as unknown as { __revealIO?: IntersectionObserver }).__revealIO?.disconnect();
  },
};
