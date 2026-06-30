<template>
  <a-config-provider :locale="locale" :theme="antdTheme">
    <div id="app">
      <div id="google_translate_element" class="google-translate-element" aria-hidden="true"></div>
      <router-view v-if="showChild" />
      <div class="system-starting-page" v-if="showStartupPage">
        <div class="system-starting-spinner" aria-hidden="true"></div>
        <div class="system-starting-title">{{ $t('qi-dong-zhong') }}</div>
      </div>
      <div class="system-starting-mask" v-else-if="showStartupMask">
        <div class="system-starting-spinner" aria-hidden="true"></div>
        <div class="system-starting-title">{{ $t('qi-dong-zhong') }}</div>
      </div>
      <div class="loading-page" v-else-if="!showChild && showLoadingPage">
        <svg class="loading-brand-logo" viewBox="0 0 5897 1024" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
          <path
            d="M5718.945185 18.962963l13.425778 0.455111v-0.417185H5897.481481V1024h-165.110518V371.332741l-315.828148 514.085926h-100.010667l-315.828148-514.085926V1024H4835.555556V19.000889h165.148444v0.417185l13.425778-0.455111 352.426666 604.027259L5718.945185 18.962963zM491.045926 18.962963c132.342519 0 204.363852 32.085333 223.307852 41.965037l6.238815 3.223704-33.640297 113.493333-9.102222-4.399407c-49.929481-24.082963-114.801778-37.394963-182.689185-37.394963-217.088 0-351.914667 142.336-351.914667 371.427555 0 226.607407 129.517037 361.908148 346.434371 361.908148 70.106074 0 143.265185-14.298074 190.881185-37.394963l9.443555-4.570074 29.05126 111.104-6.219852 3.109926C669.544296 963.015111 581.707852 986.074074 465.009778 986.074074 182.518519 986.074074 0 799.744 0 511.412148 0 216.860444 197.328593 18.962963 491.045926 18.962963zM3413.333333 18.962963v944.943407h-124.965926V881.777778C3215.872 950.97363 3132.302222 986.074074 3039.762963 986.074074c-100.920889 0-188.112593-36.370963-259.128889-107.975111-70.883556-71.49037-106.856296-158.53037-106.856296-258.654815 0-100.124444 36.389926-186.993778 108.183703-258.161778 71.831704-71.186963 160.692148-107.254519 264.116149-107.254518 94.151111 0 175.521185 34.190222 242.289777 101.679407V18.962963H3413.333333zM967.111111 37.925926v948.148148h-132.740741V37.925926h132.740741z m1092.133926 246.518518v394.012445c0 101.527704 88.500148 184.206222 197.347556 184.206222 108.809481 0 197.347556-82.678519 197.347555-184.206222V284.444444H2578.962963v679.822223h-125.022815v-43.102815A330.391704 330.391704 0 0 1 2256.592593 986.074074c-177.758815 0-322.37037-137.993481-322.370371-307.617185V284.444444h125.022815zM4336.924444 0C4528.545185 0 4683.851852 151.343407 4683.851852 338.071704V647.964444C4683.851852 834.74963 4528.545185 986.074074 4336.924444 986.074074H3830.518519V0zM1452.088889 265.481481c102.4 0 190.615704 35.726222 262.219852 106.268445 62.558815 61.591704 98.133333 134.902519 106.059852 218.320593H1664.758519c-7.585185-41.908148-27.97037-79.511704-61.212445-111.99526-42.81837-41.870222-94.189037-63.070815-152.651852-63.070815-58.424889 0-109.624889 21.200593-152.082963 63.032889-42.571852 42.040889-64.170667 92.672-64.170666 150.376297 0 57.799111 20.821333 107.52 61.895111 147.721481 40.903111 40.12563 93.051259 60.453926 154.927407 60.453926 59.695407 0 111.274667-20.536889 153.27763-61.022815 33.697185-32.426667 53.892741-70.731852 60.681481-114.119111h57.723259v57.078519h66.294519v-57.078519H1820.444444c-7.604148 83.911111-42.704593 157.392593-104.846222 218.94637C1644.86637 950.518519 1556.233481 986.074074 1452.107852 986.074074c-104.144593 0-192.986074-35.555556-264.135111-105.680593C1116.899556 810.477037 1080.888889 724.992 1080.888889 626.403556c0-98.550519 36.219259-184.263111 107.747555-254.672593C1260.202667 301.226667 1348.835556 265.481481 1452.107852 265.481481z m1596.548741 108.942223c-70.200889 0-129.517037 24.689778-176.412445 73.443555-47.085037 48.96237-70.959407 107.254519-70.959407 173.416297 0 66.085926 24.500148 124.60563 72.779852 173.909333 48.165926 49.246815 106.666667 74.164148 173.947259 74.164148 63.29837 0 120.64237-23.419259 170.533926-69.594074 49.682963-46.042074 74.903704-105.28237 74.903704-176.033185 0-70.997333-23.438222-130.977185-69.613038-178.270815-46.08-47.122963-105.054815-71.035259-175.179851-71.035259z m1288.286814-219.477334h-347.363555v676.143408h347.363555c103.61363 0 187.922963-82.147556 187.922963-183.125334V338.071704c0-100.977778-84.309333-183.125333-187.922963-183.125334zM1355.719111 587.851852c12.762074 0 24.026074 4.778667 33.374815 14.184296 9.386667 9.462519 14.165333 20.783407 14.165333 33.716148 0 12.913778-4.683852 24.064-13.899852 33.204148A46.08 46.08 0 0 1 1355.851852 682.666667c-13.539556 0-24.936296-4.570074-33.905778-13.596445A45.605926 45.605926 0 0 1 1308.444444 635.828148c0-12.951704 4.702815-24.348444 14.032593-33.810963 9.291852-9.40563 20.442074-14.184296 33.242074-14.184296zM4238.222222 360.296296c47.141926 0 85.333333 37.736296 85.333334 84.309334 0 33.242074-19.683556 61.724444-47.99526 75.434666V644.740741h-74.676148v-124.700445C4172.572444 506.311111 4152.888889 477.847704 4152.888889 444.60563c0-46.554074 38.191407-84.309333 85.333333-84.309334z"
            fill="#181d26"
          />
        </svg>
      </div>
      <SonnerToast />
    </div>
  </a-config-provider>
</template>

<script>
import SonnerToast from '@/components/SonnerToast.vue';
import enUS from 'ant-design-vue/es/locale/en_US';
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import { setDayjsLocale } from '@/utils/dayjsSetup';
import { cacheDmBootstrapStatus, isDmSystemBootstrapRequired, isDmSystemStarting } from './utils/dmGlobalSettings';
import { UPDATE_DM_GLOBAL_SETTING, UPDATE_PUBLIC_KEY } from '@/store/mutationTypes';
import { supportsCloudDMBuild } from '@/utils/product';

const SYSTEM_READY_POLL_INTERVAL_MS = 2000;

export default {
  name: 'App',
  components: {
    SonnerToast
  },
  data() {
    return {
      showChild: false,
      showLoadingPage: true,
      systemStarting: false,
      startupDisplayMode: 'page',
      startupPollTimer: null,
      locale: zhCN,
      antdTheme: {
        token: {
          colorPrimary: '#3ecf8e',
          colorLink: '#24b47e',
          colorLinkHover: '#1ea06a',
          colorSuccess: '#22c55e',
          colorWarning: '#f59e0b',
          colorError: '#ef4444',
          borderRadius: 8,
          borderRadiusSM: 6,
          borderRadiusLG: 12,
          fontSize: 14,
          colorBgContainer: '#ffffff',
          colorBorder: '#dfdfdf',
          colorText: '#171717',
          colorTextLightSolid: '#ffffff',
          colorTextSecondary: '#707070',
          controlHeight: 32,
          controlHeightSM: 28
        },
        components: {
          Table: {
            headerBg: '#fafafa',
            headerColor: '#707070',
            rowHoverBg: '#f0f0f0',
            borderColor: '#efefef',
            cellPaddingBlockSM: 8,
            cellPaddingInlineSM: 12
          },
          Button: {
            primaryShadow: 'none',
            defaultShadow: 'none',
            primaryColor: '#ffffff'
          }
        }
      },
      position: {
        ele: null,
        canResize: false
      }
    };
  },
  computed: {
    showStartupPage() {
      return this.systemStarting && this.startupDisplayMode === 'page';
    },
    showStartupMask() {
      return this.systemStarting && this.startupDisplayMode === 'mask';
    }
  },
  async created() {
    console.log(this.$i18n.global.locale.value);
    this.syncLocale();
    await this.bootstrapApp();
  },
  mounted() {
    document.addEventListener('mousedown', this.handleMousedown);
    document.addEventListener('mousemove', this.handleMousemove);
    document.addEventListener('mouseup', this.handleMouseup);
  },
  methods: {
    syncLocale() {
      const currentLocale = this.$i18n.global.locale.value;
      this.locale = currentLocale === 'zh-CN' ? zhCN : enUS;
      setDayjsLocale(currentLocale);
    },
    isHomeEntryRoute() {
      const hash = window.location.hash || '#/';
      return hash === '#/' || hash === '' || hash === '#/login' || hash.startsWith('#/login?');
    },
    sleep(ms) {
      return new Promise((resolve) => {
        this.startupPollTimer = setTimeout(resolve, ms);
      });
    },
    async waitForSystemReady() {
      this.systemStarting = true;
      this.showLoadingPage = false;
      this.startupDisplayMode = this.isHomeEntryRoute() ? 'page' : 'mask';

      while (this.systemStarting) {
        await this.sleep(SYSTEM_READY_POLL_INTERVAL_MS);
        const res = await this.$services.dmGlobalSettings();
        cacheDmBootstrapStatus(res);
        if (!res.success || isDmSystemBootstrapRequired(res)) {
          return res;
        }
        if (!isDmSystemStarting(res)) {
          return res;
        }
      }
    },
    async resolveDmGlobalSettings() {
      let globalSettingRes = await this.$services.dmGlobalSettings();
      cacheDmBootstrapStatus(globalSettingRes);
      if (globalSettingRes.success && isDmSystemStarting(globalSettingRes)) {
        globalSettingRes = await this.waitForSystemReady();
      }
      this.systemStarting = false;
      this.showLoadingPage = true;
      this.commitDmGlobalSettings(globalSettingRes);
      return globalSettingRes;
    },
    // refresh / direct URL 进入内部页时, login 流程被跳过, publicKey 与 dmGlobalSetting 不会再被填充
    // 这里在每次 bootstrap 拿到响应后同步写回 store, 让 encryptMixin / 数据源页等依赖项不会处于空状态
    commitDmGlobalSettings(globalSettingRes) {
      if (!supportsCloudDMBuild) {
        return;
      }
      if (!globalSettingRes || !globalSettingRes.success || !globalSettingRes.data) {
        return;
      }
      this.$store.commit(UPDATE_DM_GLOBAL_SETTING, globalSettingRes.data);
      if (globalSettingRes.data.publicKey) {
        this.$store.commit(UPDATE_PUBLIC_KEY, globalSettingRes.data.publicKey);
      }
    },
    async bootstrapApp() {
      const isInitializationRoute = () => window.location.hash.startsWith('#/initialization');

      // Check whether the system needs initialization - use the DM interface
      try {
        const globalSettingRes = await this.resolveDmGlobalSettings();
        if (!globalSettingRes.success || isDmSystemBootstrapRequired(globalSettingRes)) {
          await this.$router.replace({ name: 'Initialization' });
          this.showChild = true;
          this.removeLoadingEle();
          return;
        }

        if (isInitializationRoute()) {
          await this.$router.replace({ name: 'Login' });
          this.showChild = true;
          this.removeLoadingEle();
          return;
        }
      } catch (e) {
        this.systemStarting = false;
        this.showLoadingPage = true;
        if (isInitializationRoute()) {
          this.showChild = true;
          this.removeLoadingEle();
          return;
        }
        await this.$router.replace({ name: 'Initialization' });
        this.showChild = true;
        this.removeLoadingEle();
        return;
      }

      if (window.location.hash === '#/login' || window.location.hash.startsWith('#/login?')) {
        this.showChild = true;
        this.removeLoadingEle();
        return;
      }

      // Normal process
      await this.$store.dispatch('getUserInfo');
      this.showChild = true;
      this.removeLoadingEle();
    },
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
    if (this.startupPollTimer) {
      clearTimeout(this.startupPollTimer);
    }
    document.removeEventListener('mousedown', this.handleMousedown);
    document.removeEventListener('mousemove', this.handleMousemove);
    document.removeEventListener('mouseup', this.handleMouseup);
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

.loading-brand-logo {
  width: 200px;
  height: auto;
  animation: logoPulse 2s ease-in-out infinite;
}

@keyframes logoPulse {
  0%,
  100% {
    opacity: 0.4;
  }
  50% {
    opacity: 1;
  }
}

.system-starting-page,
.system-starting-mask {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
}

.system-starting-page {
  background: #f7f9fc;
}

.system-starting-mask {
  background: rgba(247, 249, 252, 0.86);
  backdrop-filter: blur(2px);
}

.system-starting-spinner {
  width: 56px;
  height: 56px;
  margin-bottom: 24px;
  border-radius: 50%;
  border: 5px solid rgba(45, 140, 240, 0.16);
  border-top-color: #2d8cf0;
  animation: appLoadingSpin 0.8s linear infinite;
}

.system-starting-title {
  color: #17233d;
  font-size: 32px;
  font-weight: 600;
  line-height: 1.4;
}

@keyframes appLoadingSpin {
  to {
    transform: rotate(360deg);
  }
}
</style>
