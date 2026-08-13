/**
 * @Author: wj 3363891051@qq.com
 * @Date: 2026-08-13
 * @FilePath: apps/taiji-web/plugins/reveal.ts
 * @Description: 注册全局滚动揭示指令 v-reveal（服务端+客户端均注册，见 docs/16 §4.2）。
 *               页面元素加 v-reveal 即可获得进入视口的渐入动效；SSR 不执行动画逻辑。
 */
import { defineNuxtPlugin } from '#app';
import { vReveal } from '~/composables/useScrollReveal';

export default defineNuxtPlugin((nuxtApp) => {
  nuxtApp.vueApp.directive('reveal', vReveal);
});
