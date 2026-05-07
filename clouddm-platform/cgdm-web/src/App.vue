<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <div id="google_translate_element" class="google-translate-element" aria-hidden="true"></div>
      <router-view v-if="showChild" />
      <div class="loading-page" v-if="!showChild && showLoadingPage">
        <div class="loading-spinner" aria-hidden="true"></div>
      </div>
    </div>
  </a-config-provider>
</template>

<script>
import enUS from 'ant-design-vue/es/locale/en_US';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { cacheDmBootstrapStatus, isDmSystemBootstrapRequired } from './utils/dmGlobalSettings';

export default {
  name: 'App',
  data() {
    return {
      showChild: false,
      showLoadingPage: true,
      locale: zhCN,
      position: {
        ele: null,
        canResize: false
      }
    };
  },
  async created() {
    console.log(this.$i18n.global.locale.value);
    this.locale = this.$i18n.global.locale.value === 'zh-CN' ? zhCN : enUS;

    // 先检测系统是否需要初始化 - 使用 DM 接口
    try {
      const globalSettingRes = await this.$services.dmGlobalSettings();
      cacheDmBootstrapStatus(globalSettingRes);
      if (globalSettingRes.success && isDmSystemBootstrapRequired(globalSettingRes)) {
        await this.$router.replace({ name: 'Initialization' });
        this.showChild = true;
        this.removeLoadingEle();
        return;
      }
    } catch (e) {
      // 如果接口不可用，继续正常流程
    }

    if (window.location.hash === '#/login') {
      this.showChild = true;
      this.removeLoadingEle();
      return;
    }

    // 正常流程
    await this.$store.dispatch('getUserInfo');
    this.showChild = true;
    this.removeLoadingEle();
  },
  mounted() {
    document.addEventListener('mousedown', this.handleMousedown);
    document.addEventListener('mousemove', this.handleMousemove);
    document.addEventListener('mouseup', this.handleMouseup);
  },
  methods: {
    removeLoadingEle() {
      const loadingEl = document.getElementById('app-startup-loading');
      if (loadingEl) {
        loadingEl.style.transition = 'opacity 0.5s ease-out, transform 0.5s ease-out';
        loadingEl.style.opacity = '0';
        loadingEl.style.transform = 'scale(0.95)';

        setTimeout(() => {
          loadingEl.remove();
        }, 100);
      }
    },
    handleMousedown(event) {
      if (['editor-resize', 'tree-resize', 'table-list-resize', 'struct-resize'].includes(event.target.className)) {
        this.position.ele = event.target;
        event.target.style.background = '#ccc';
        this.position.canResize = true;
        this.position.type = event.target.className;
        this.position.clientX = event.clientX;
        this.position.clientY = event.clientY;
        document.body.style.setProperty('user-select', 'none');
      }
    },
    handleMousemove(event) {
      let needMouseUp = false;
      if (this.position.canResize) {
        const leftWidth = event.clientX - this.position.clientX;
        const leftHeight = event.clientY - this.position.clientY;
        this.position.clientX = event.clientX;
        this.position.clientY = event.clientY;

        if (this.position.type === 'editor-resize') {
          const ele = document.querySelector('.monaco-editor');
          if (ele) {
            const rect = ele.getBoundingClientRect();
            const sqlViewerEle = document.querySelector('.query-editor-container');
            const sqlViewerRect = sqlViewerEle.getBoundingClientRect();
            let calcHeight = rect.height;
            if (rect.height >= 20 && sqlViewerRect.height - rect.height >= 74) {
              calcHeight += leftHeight;
            }

            if (calcHeight < 20) {
              calcHeight = 20;
              needMouseUp = true;
            } else if (sqlViewerRect.height - calcHeight < 74) {
              calcHeight = sqlViewerRect.height - 74;
              needMouseUp = true;
            }

            ele.style.setProperty('height', `${calcHeight}px`, 'important');
            this.$bus.emit('setEditorHeight', calcHeight);
          }
        } else if (this.position.type === 'tree-resize') {
          const ele = document.querySelector('.data-source-container');
          if (ele) {
            const rect = ele.getBoundingClientRect();
            let calcWidth = rect.width;

            if (rect.width >= 60) {
              calcWidth += leftWidth;
            }
            if (calcWidth < 60) {
              calcWidth = 60;
              needMouseUp = true;
            }

            ele.style.setProperty('width', `${calcWidth}px`, 'important');
          }
        } else if (this.position.type === 'struct-resize') {
          const ele = document.querySelector('.struct-view .left');
          if (ele) {
            const rect = ele.getBoundingClientRect();
            let calcWidth = rect.width;

            if (rect.width >= 60) {
              calcWidth += leftWidth;
            }
            if (calcWidth < 60) {
              calcWidth = 60;
              needMouseUp = true;
            }

            ele.style.setProperty('width', `${calcWidth}px`, 'important');
          }
        } else if (this.position.type === 'table-list-resize') {
          const ele = document.querySelector('.table-list-container');
          if (ele) {
            const rect = ele.getBoundingClientRect();
            let calcWidth = rect.width;

            if (rect.width >= 60) {
              calcWidth += leftWidth;
            }
            if (calcWidth < 60) {
              calcWidth = 60;
              needMouseUp = true;
            }

            ele.style.setProperty('width', `${calcWidth}px`, 'important');
          }
        }

        if (needMouseUp) {
          this.handleMouseup(event);
        }
      }
    },
    handleMouseup() {
      document.body.style.setProperty('user-select', 'text');
      this.position.canResize = false;
      if (this.position.ele) {
        this.position.ele.style.background = 'rgba(0, 0, 0 ,0)';
        this.position.ele = null;
      }
    }
  },
  beforeUnmount() {
    document.removeEventListener('mousedown', this.handleMousedown);
    document.removeEventListener('mousemove', this.handleMousemove);
    document.removeEventListener('mousemove', this.handleMouseup);
  }
};
</script>

<style lang="less">
#app {
  flex-direction: column;
  display: flex;
  height: 100%;
}

#google_translate_element {
  display: none;
}

.goog-te-banner-frame.skiptranslate {
  display: none !important;
}

body {
  top: 0 !important;
}

.loading-page {
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background: rgba(240, 242, 245, 0.96);
}

.loading-spinner {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  border: 4px solid rgba(22, 119, 255, 0.18);
  border-top-color: #1677ff;
  animation: appLoadingSpin 0.8s linear infinite;
}

@keyframes appLoadingSpin {
  to {
    transform: rotate(360deg);
  }
}
</style>
