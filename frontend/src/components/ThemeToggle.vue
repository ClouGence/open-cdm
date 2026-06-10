<template>
  <div class="theme-toggle-wrapper">
    <!-- Fullscreen load mask - Use Teleport to render body to avoid parent elements -->
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
        <!-- Light -->
        <svg v-if="currentTheme === 'light'" class="theme-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <circle cx="12" cy="12" r="4" stroke="currentColor" stroke-width="2" />
          <path
            d="M12 5V3M12 21V19M19 12H21M3 12H5M17.657 6.343L19.071 4.929M4.929 19.071L6.343 17.657M17.657 17.657L19.071 19.071M4.929 4.929L6.343 6.343"
            stroke="currentColor"
            stroke-width="2"
            stroke-linecap="round"
          />
        </svg>
        <!-- Night -->
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
      // 1. Show loating mask first
      isSwitching.value = true;

      // 2. Waiting for mask rendering to be completed
      await nextTick();
      requestAnimationFrame(() => {
        // 3. Delay 0.3s before switching themes
        setTimeout(() => {
          // Switch Theme
          store.dispatch('toggleTheme');

          // 4. Waiting for DOM updates and style applications
          nextTick().then(() => {
            // Make sure that the style is fully applied using the dual referenceAnimationrama
            requestAnimationFrame(() => {
              requestAnimationFrame(() => {
                // 5. Delayed hiding, ensuring transition animation is completed
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
      color: #fff; // Use white icons in bright color mode
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

<!-- Global Styles: Theme Switching -->
<style lang="less">
// Theme Switching Animation (global style because the whole page needs to be covered)
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

// Use deeper background in dark color mode
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

// Theme Switching Transition Animation
.theme-loading-enter-active,
.theme-loading-leave-active {
  transition: opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1) !important;
}

.theme-loading-enter-from,
.theme-loading-leave-to {
  opacity: 0 !important;
}
</style>
