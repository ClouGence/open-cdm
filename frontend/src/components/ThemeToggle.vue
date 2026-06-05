<template>
  <div class="theme-toggle-wrapper">
    <!-- 全屏 loading 遮罩 - 使用 Teleport 渲染到 body，避免受父元素 transform 影响 -->
    <Teleport to="body">
      <Transition name="theme-loading">
        <div v-if="isSwitching" class="theme-switch-loading">
          <div class="theme-loading-content">
            <div class="theme-loading-spinner">
              <div class="spinner-ring"></div>
              <div class="spinner-ring"></div>
              <div class="spinner-ring"></div>
            </div>
            <div class="theme-loading-text">{{ $t('qie-huan-zhong') }}</div>
          </div>
        </div>
      </Transition>
    </Teleport>
    <div class="theme-toggle" :class="currentTheme" @click="toggleTheme">
      <Transition name="fade" mode="out-in">
        <!-- 浅色 -->
        <svg v-if="currentTheme === 'light'" class="theme-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="4" stroke="currentColor" stroke-width="2" />
          <path
            d="M12 5V3M12 21V19M19 12H21M3 12H5M17.657 6.343L19.071 4.929M4.929 19.071L6.343 17.657M17.657 17.657L19.071 19.071M4.929 4.929L6.343 6.343"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
          />
        </svg>
        <!-- 黑夜 -->
        <svg v-else class="theme-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
            stroke-linejoin="round"
          />
        </svg>
      </Transition>
    </div>
  </div>
</template>

<script>
import { computed, ref, nextTick } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'ThemeToggle',
  setup() {
    const store = useStore();
    const currentTheme = computed(() => store.state.theme);
    const isSwitching = ref(false);

    const toggleTheme = async () => {
      // 1. 先显示 loading 遮罩
      isSwitching.value = true;

      // 2. 等待遮罩渲染完成
      await nextTick();
      requestAnimationFrame(() => {
        // 3. 延迟 0.3s 后再切换主题
        setTimeout(() => {
          // 切换主题
          store.dispatch('toggleTheme');

          // 4. 等待 DOM 更新和样式应用
          nextTick().then(() => {
            // 使用双重 requestAnimationFrame 确保样式已完全应用
            requestAnimationFrame(() => {
              requestAnimationFrame(() => {
                // 5. 延迟隐藏 loading，确保过渡动画完成
                setTimeout(() => {
                  isSwitching.value = false;
                }, 400);
              });
            });
          });
        }, 300);
      });
    };

    return {
      currentTheme,
      isSwitching,
      toggleTheme
    };
  }
};
</script>

<style lang="less" scoped>
.theme-toggle-wrapper {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.theme-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: transparent;
  vertical-align: middle;

  &:active {
    transform: scale(0.95);
  }

  .theme-icon {
    width: 20px;
    height: 20px;
    color: var(--text-primary);
    display: block;
  }

  &.light {
    .theme-icon {
      color: #fff; // 亮色模式下也使用白色图标
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition:
    opacity 0.22s cubic-bezier(0.22, 1, 0.36, 1),
    transform 0.22s cubic-bezier(0.22, 1, 0.36, 1);
  will-change: opacity, transform;
  backface-visibility: hidden;
  transform-origin: 50% 50%;
}

.fade-enter-from {
  opacity: 0;
  transform: rotate(-120deg) scale(0.85);
}

.fade-leave-to {
  opacity: 0;
  transform: rotate(120deg) scale(0.85);
}
</style>

<!-- 全局样式：主题切换 loading 动画 -->
<style lang="less">
// 主题切换 loading 动画（全局样式，因为需要覆盖整个页面）
.theme-switch-loading {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  bottom: 0 !important;
  z-index: 99999 !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
  background-color: rgba(0, 0, 0, 0.85) !important;
  backdrop-filter: blur(8px) !important;
  -webkit-backdrop-filter: blur(8px) !important;

  .theme-loading-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 24px;
  }

  .theme-loading-spinner {
    position: relative;
    width: 64px;
    height: 64px;

    .spinner-ring {
      position: absolute;
      width: 100%;
      height: 100%;
      border: 3px solid transparent;
      border-top-color: #0bb9f8;
      border-radius: 50%;
      animation: theme-spin 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;

      &:nth-child(1) {
        animation-delay: -0.45s;
      }

      &:nth-child(2) {
        animation-delay: -0.3s;
        border-top-color: rgba(11, 185, 248, 0.7);
      }

      &:nth-child(3) {
        animation-delay: -0.15s;
        border-top-color: rgba(11, 185, 248, 0.4);
      }
    }
  }

  .theme-loading-text {
    color: #ffffff !important;
    font-size: 14px;
    font-weight: 500;
    letter-spacing: 0.5px;
    animation: theme-pulse 1.5s ease-in-out infinite;
  }
}

// 暗色模式下使用更深的背景
[data-theme='dark'] .theme-switch-loading {
  background-color: rgba(0, 0, 0, 0.95) !important;
}

@keyframes theme-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes theme-pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

// 主题切换 loading 过渡动画
.theme-loading-enter-active,
.theme-loading-leave-active {
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.theme-loading-enter-from,
.theme-loading-leave-to {
  opacity: 0 !important;
}
</style>
