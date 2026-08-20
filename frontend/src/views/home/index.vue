<template>
  <div class="home" :class="{ 'home--sql': isSqlRoute }">
    <dm-water-mark :input-text="watermarkStr" ref="watermark" v-if="!isDesktop && globalSetting.enableWaterMark" />

    <template v-if="showChild">
      <Transition name="layout-switch" mode="out-in">
        <div v-if="isSqlRoute" key="sql" class="sql-layout">
          <header class="sql-compact-header">
            <div class="sql-compact-header__brand" @click="handleGoAppHome">
              <AppBrandLogo />
              <span class="sql-compact-header__title">{{ $t('sql-cha-xun') }}</span>
            </div>
            <div class="sql-compact-header__actions">
              <button type="button" class="sql-compact-header__back" @click="handleGoAppHome">
                <span>{{ $t('fan-hui-gong-zuo-tai') }}</span>
              </button>
              <AppUserActions compact @check-version="checkVersion(true)" />
            </div>
          </header>
          <div class="sql-layout-body">
            <router-view />
          </div>
        </div>
        <div v-else key="app" class="app-layout">
          <AppSidebar />
          <main class="app-main">
            <div class="app-main-body">
              <div class="app-main-card">
                <AppContentHeader @check-version="checkVersion(true)" />
                <div class="app-main-card__body">
                  <router-view />
                </div>
              </div>
            </div>
          </main>
        </div>
      </Transition>
    </template>

    <div v-else class="home-entry-loading">
      <a-spin />
      <span>{{ $t('zheng-zai-jia-zai') }}</span>
    </div>

    <div class="user-expr-tip" v-if="userInfo.subAccountPwdValidDays !== null && userInfo.subAccountPwdValidDays < limitDays">
      {{ $t('gen-ju-zhu-zhang-hao-she-zhi-de-mi-ma-shi-xiao-ce-lue', [userInfo.subAccountPwdValidDays + 1]) }}
    </div>
    <CCModal v-model="showDetailModal" :title="selectedCellDetail.column.property" v-if="showDetailModal" key="showDetailModal" :width="800">
      <div class="cell-detail">
        <a-textarea v-model:value="cellDetailContent" :rows="15" readonly style="font-family: monospace; font-size: 12px" />
        <div v-if="cellDetailLoading" class="cell-detail-loading">
          <a-spin :spinning="cellDetailLoading" />
          <span style="margin-left: 8px">{{ $t('zheng-zai-jia-zai') }}</span>
        </div>
      </div>
      <template #footer>
        <a-button v-if="hasMoreData && !cellDetailLoading && !selectedCellDetail.error && !selectedCellDetail.mask" @click="handleLoadMoreCellData">
          {{ $t('jia-zai-geng-duo') }}
        </a-button>
        <a-button type="primary" :loading="cellDetailLoading" @click="handleDetailCopy">{{ $t('fu-zhi') }}</a-button>
        <a-button @click="handleCloseCellDetailModal">{{ $t('guan-bi') }}</a-button>
      </template>
    </CCModal>
    <CCModal v-model="showInactiveModal" :title="$t('cuo-wu')" :closable="false" :mask-closable="false">
      {{ inactiveMsg }}
      <template #footer>
        <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
      </template>
    </CCModal>
    <a-modal v-model="showVersionDetailModal" :title="$t('ban-ben-jian-cha')" :width="800" :mask-closable="false" :closable="false">
      <div>
        <h2>{{ $t('xin-de-clouddm-ban-ben-ke-yong') }}</h2>
        <h2>{{ $t('dang-qian-ban-ben') }}{{ displayVersion }}</h2>
        <h2>{{ $t('zui-xin-ban-ben') }}{{ this.version.lastVersion }}</h2>
        <div style="max-height: 500px; overflow: auto">
          <pre v-for="(d, index) in version.detail" :key="index" v-html="d"></pre>
        </div>
      </div>
      <div class="footer">
        <a-button style="margin-right: 10px" type="primary" @click="handleDownload">
          {{ $t('xia-zai-zui-xin-ban-ben') }}
        </a-button>
        <a-button style="margin-right: 10px" @click="handleCloseVersionDetailModal">
          {{ $t('guan-bi') }}
        </a-button>
        <a-checkbox v-model="version.ignore">{{ $t('bu-zai-ti-shi') }}</a-checkbox>
      </div>
    </a-modal>
  </div>
</template>

<script>
import appLogger from '@/utils/logger';
import { mapGetters, mapState } from 'vuex';
import AppSidebar from '@/components/layout/AppSidebar';
import AppBrandLogo from '@/components/layout/AppBrandLogo';
import AppContentHeader from '@/components/layout/AppContentHeader';
import AppUserActions from '@/components/layout/AppUserActions';
import { setApprovalProcessMixin, setOpPasswordMixin } from '@/mixins/modal';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import XEClipboard from 'xe-clipboard';
import DmWaterMark from '@/components/widgets/DmWaterMark';
import store from '@/store';
import dayjs from 'dayjs';
import fecha from 'fecha';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { resolveWorkbenchFallbackPath, resolveWorkbenchRoute } from '@/utils/workbenchRoute';

export default {
  name: 'Home',
  components: {
    DmWaterMark,
    AppSidebar,
    AppBrandLogo,
    AppContentHeader,
    AppUserActions
  },
  data() {
    return {
      showInactiveModal: false,
      inactiveMsg: '',
      showChild: false,
      showVersionDetailModal: false,
      version: {
        ignore: false,
        newVersion: false,
        prompt: false,
        lastVersion: '',
        detail: []
      },
      showDetailModal: false,
      testHtml: 'test\ntest\n',
      selectedCellDetail: {
        column: {},
        row: {},
        resultId: '',
        rowNumber: -1,
        colNumber: -1,
        cellValue: '',
        moreSize: 0,
        totalSize: 0,
        complete: true,
        error: false,
        mask: false
      },
      cellDetailContent: '',
      cellDetailLoading: false,
      hasMoreData: false,
      limitDays: 6,
      watermarkStr: '',
      store,
      fecha,
      dayjs
    };
  },
  mixins: [setOpPasswordMixin, setApprovalProcessMixin, enterOpPwdMixin],
  computed: {
    ...mapGetters(['isDesktop', 'displayVersion', 'includesDM', 'isInternalUser']),
    ...mapState(['userInfo', 'myAuth', 'globalSetting', 'defaultRedirectUrl', 'dmGlobalSetting', 'remainTrialDay', 'mySystemMenuItems']),
    ...mapGetters(['isSaas']),
    isSqlRoute() {
      return this.$route.path === '/sql' || this.$route.path.startsWith('/sql/');
    }
  },
  async created() {
    await this.$store.dispatch('getRegionList');

    await this.$store.dispatch('getDmGlobalConfig');

    if (this.$route.path === '/') {
      await this.$router.replace({ path: this.defaultRedirectUrl || '/sql' }).catch(() => {});
    }

    this.showChild = true;
    await this.$store.dispatch('getRegionList');
    if (this.globalSetting.enableWaterMark) {
      const waterMark = await this.$services.rdpUserWatermark();
      this.watermarkStr = `${waterMark.data.user_name}_${waterMark.data.user_phone}`;
    }
    this.$bus.on('setOpPasswordModal', (edit = false) => {
      if (this.globalSetting.authOpPassword) {
        this.setOpPasswordModal(edit);
      }
    });
    this.$bus.on('showCellDetailModal', async (data) => {
      appLogger.debug('showCellDetailModal', data);
      if (!this.showDetailModal) {
        this.showDetailModal = true;
        this.selectedCellDetail = {
          row: data.row || {},
          column: data.column || {},
          resultId: data.resultId || '',
          rowNumber: data.rowNumber !== undefined ? data.rowNumber : -1,
          colNumber: data.colNumber !== undefined ? data.colNumber : -1,
          cellValue: data.cellValue || '',
          moreSize: data.moreSize || 0,
          totalSize: data.totalSize || 0,
          complete: data.complete !== undefined ? data.complete : true,
          error: data.error || false,
          mask: data.mask || false
        };
        // Initialize content (show initial values)
        this.cellDetailContent = this.selectedCellDetail.cellValue || '';
        // Disable loading more if error or mask is true
        const canLoadMore = !this.selectedCellDetail.error && !this.selectedCellDetail.mask;
        this.hasMoreData = canLoadMore && (this.selectedCellDetail.moreSize || 0) > 0;

        if (this.hasMoreData && !this.cellDetailLoading) {
          await this.handleLoadMoreCellData();
        }
      }
    });
    this.$bus.on('showEnterOpPwdModal', this.showEnterOpPwdModal);
    this.$bus.on('dingDingSettingModal', this.setApprovalProcessModal);
    this.$bus.on(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL, (msg) => this.handleShowInactiveModal(msg));
    if (this.includesDM) {
      await this.checkVersion();
    }
  },
  unmounted() {
    this.$bus.off('setOpPasswordModal');
    this.$bus.off('showEnterOpPwdModal');
    this.$bus.off('dingDingSettingModal');
    this.$bus.off('showCellDetailModal');
    this.$bus.off(EVENT_BUS_NAME_LIST.SHOW_INACTIVE_MODAL);
  },
  methods: {
    handleShowInactiveModal(msg) {
      appLogger.debug(msg);
      this.showInactiveModal = true;
      this.inactiveMsg = msg;
    },
    handleDownload() {
      window.open('https://www.clougence.com/clouddm-personal', 'blank');
      this.handleCloseVersionDetailModal();
    },
    async handleCloseVersionDetailModal() {
      this.showVersionDetailModal = false;
      if (this.version.ignore) {
        this.version.ignore = false;
        await this.$services.dmVersionIgnore();
        await this.checkVersion();
      }
    },
    async checkVersion(showDetailModal = false) {
      const res = await this.$services.dmVersionCheck();
      if (res.success) {
        const { newVersion, prompt } = res.data;
        this.version = {
          newVersion,
          prompt
        };
        if (prompt || showDetailModal) {
          const res2 = await this.$services.dmVersionDetail();
          if (res2.success && res2.data) {
            this.version.detail = res2.data.detail;
            this.version.lastVersion = res2.data.lastVersion;
            this.showVersionDetailModal = true;
          }
        }
      }
    },
    async handleDetailCopy() {
      if (this.cellDetailLoading) {
        return;
      }

      let copyContent = this.cellDetailContent || '';
      let moreSize = this.hasMoreData ? this.selectedCellDetail.moreSize || 0 : 0;
      const { resultId, rowNumber, colNumber } = this.selectedCellDetail;
      const fetchSize = 128 * 1024;

      this.cellDetailLoading = true;
      try {
        while (moreSize > 0) {
          const previousLength = copyContent.length;
          const res = await this.$services.dmQueryFetchResultData({
            data: {
              resultId,
              rowNumber,
              colNumber,
              offset: previousLength,
              fetchSize
            }
          });
          if (!res.success || !res.data) {
            throw new Error(res.message || this.$t('jia-zai-shu-ju-shi-bai'));
          }

          const dataValue = res.data.value || res.data;
          if (dataValue.error) {
            throw new Error(this.$t('jia-zai-shu-ju-shi-bai'));
          }

          copyContent += dataValue.value || '';
          moreSize = dataValue.moreSize || 0;
          if (copyContent.length <= previousLength) {
            throw new Error(this.$t('jia-zai-shu-ju-shi-bai'));
          }
        }
        if (!XEClipboard.copy(copyContent)) {
          throw new Error(this.$t('fu-zhi-shi-bai'));
        }
        this.$Message.success(this.$t('fu-zhi-cheng-gong'));
      } catch (error) {
        appLogger.error(this.$t('fu-zhi-shi-bai'), error);
        this.$Message.error(this.$t('fu-zhi-shi-bai'));
      } finally {
        this.cellDetailLoading = false;
      }
    },
    handleCloseCellDetailModal() {
      this.showDetailModal = false;
      // Reset Status
      this.cellDetailContent = '';
      this.hasMoreData = false;
      this.cellDetailLoading = false;
      this.selectedCellDetail = {
        column: {},
        row: {},
        resultId: '',
        rowNumber: -1,
        colNumber: -1,
        cellValue: '',
        moreSize: 0,
        totalSize: 0,
        complete: true,
        error: false,
        mask: false
      };
    },
    async handleLoadMoreCellData(isInitial = false) {
      if (this.cellDetailLoading) {
        return;
      }

      // More loads are not allowed if error or mask is true
      if (this.selectedCellDetail.error || this.selectedCellDetail.mask) {
        return;
      }

      const { resultId, rowNumber, colNumber } = this.selectedCellDetail;

      if (!resultId || rowNumber < 0 || colNumber < 0) {
        appLogger.error('缺少必要参数:', { resultId, rowNumber, colNumber });
        return;
      }

      // Calculating item(s)
      const offset = this.cellDetailContent?.length || 0;

      const fetchSize = 128 * 1024; // 128K character

      this.cellDetailLoading = true;

      try {
        const res = await this.$services.dmQueryFetchResultData({
          data: {
            resultId,
            rowNumber,
            colNumber,
            offset,
            fetchSize
          }
        });

        if (res.success && res.data) {
          // The data structure returned from the interface is {value: {complete, mark, error, moreSize, totalSize, value}
          const dataValue = res.data.value || res.data;
          const { value, moreSize, totalSize, complete, error } = dataValue;

          if (error) {
            this.$Message.error(this.$t('jia-zai-shu-ju-shi-bai'));
            return;
          }

          // Add content (because initial values have been shown, only new data will be required here)
          this.cellDetailContent += value || '';

          // Update moreSize and totalSize
          this.selectedCellDetail.moreSize = moreSize || 0;
          this.selectedCellDetail.totalSize = totalSize || 0;
          // Disable loading more if error or mask is true
          const canLoadMore = !this.selectedCellDetail.error && !this.selectedCellDetail.mask;
          this.hasMoreData = canLoadMore && (moreSize || 0) > 0;
        } else {
          this.$Message.error(res.message || this.$t('jia-zai-shu-ju-shi-bai'));
        }
      } catch (error) {
        appLogger.error('加载单元格数据失败:', error);
        this.$Message.error(this.$t('jia-zai-shu-ju-shi-bai'));
      } finally {
        this.cellDetailLoading = false;
      }
    },
    handleGoBackHome() {
      if (this.$route.path !== this.defaultRedirectUrl) {
        this.$router.push({ path: this.defaultRedirectUrl });
      }
    },
    handleGoAppHome() {
      if (this.isSqlRoute) {
        const fallback = resolveWorkbenchFallbackPath(this.mySystemMenuItems);
        const target = resolveWorkbenchRoute(fallback, this.userInfo?.uid, this.mySystemMenuItems);
        this.$router.push(target).catch(() => {});
        return;
      }
      this.handleGoBackHome();
    },
    goAsyncJobList() {
      this.$router.push({ name: 'ASYNC_JOB_LIST' });
    },
    _setApprovalProcessModal() {
      this.$store.dispatch('getUserInfo');
      this.setApprovalProcessModal();
    },
    _setOpPasswordModal() {
      this.$store.dispatch('getUserInfo');
      this.setOpPasswordModal(true);
    },
    goUserConfig() {
      this.$router.push({ name: 'User_Config' });
    },

    handleCloseModal() {
      this.showInactiveModal = false;
    }
  }
};
</script>

<style lang="less" scoped>
.home {
  height: 100%;
  display: flex;
  flex-direction: column;

  .user-expr-tip {
    width: 100%;
    height: 16px;
    line-height: 16px;
    background: rgba(239, 68, 68, 0.15);
    position: fixed;
    top: 0;
    left: 0;
    z-index: 1001;
    text-align: center;
    font-size: 12px;
    color: var(--error-color);
  }

  &--sql .user-expr-tip {
    top: 58px;
  }

  .home-entry-loading {
    flex: 1;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    color: var(--text-secondary);
    background: #f5f7fa;
    font-size: 14px;
  }

  .footer {
    //height: 24px;
    width: 100%;
    border-top: 1px solid #ccc;
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .right {
      display: flex;

      .btn {
        font-size: 12px;
      }
    }

    .async-list {
      position: absolute;
      width: 400px;
      max-height: 400px;
      overflow: auto;
      bottom: 24px;
      border: 1px solid #ccc;
      border-bottom: none;
      z-index: 999;
      background: #fff;
      box-shadow: rgba(100, 100, 111, 0.2) 0px 7px 29px 0px;

      .async-list-header {
        height: 24px;
        border-bottom: 1px solid #ccc;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 10px;
      }

      .list {
        padding: 5px;

        .task {
          padding: 3px;
          border: 1px solid #ccc;
          border-radius: 5px;
          padding: 5px 10px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 5px;
          width: 100%;

          .content {
            flex: 1;
            min-width: 0;
            margin-right: 40px;

            .title {
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              width: 100%;
            }
          }

          .action {
            .async-task-btn + .async-task-btn {
              margin-left: 5px;
            }
          }
        }
      }
    }
  }

  .content-container {
    // padding-top defined in app-shell.less
    height: 100%;
    overflow-y: auto;
  }
}

.ivu-dropdown .ivu-select-dropdown {
  top: 80px !important;
}

.message-list-container {
  width: 360px;
  background: white;
  box-shadow: 1px 1px 6px rgba(164, 164, 164, 0.66);

  .title {
    background: #ececec;
    line-height: 50px;
    font-size: 14px;
    font-family: PingFangSC-Semibold;
    font-weight: 500;
    padding: 0 20px;
  }

  .time {
    color: #888888;
  }

  .message-item {
    padding: 20px;
    border-bottom: 1px solid #dfdfdf;
    line-height: 20px;
    cursor: pointer;

    &:hover {
      background: #f5f5f5;
    }
  }

  .message-footer {
    padding: 0 20px;
    line-height: 38px;
    cursor: pointer;

    &:hover {
      background: #f5f5f5;
    }
  }
}

.cell-detail {
  position: relative;

  .cell-detail-loading {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(255, 255, 255, 0.9);
    padding: 10px 20px;
    border-radius: 4px;
    z-index: 10;
  }

  white-space: pre-line;
  width: 100%;
  margin-top: 10px;
  max-height: 400px;
  overflow: auto;
}

.renew-license-modal {
  padding: 14px 14px 30px 14px;

  :deep(textarea) {
    height: 80px;
    width: 100%;
  }

  .operation {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .section {
    .title {
      font-weight: 400;
      font-size: 16px;
      line-height: 24px;
      color: rgba(0, 0, 0, 0.9);
      margin-bottom: 8px;
    }

    .sub-title {
      color: rgba(0, 0, 0, 0.6);
      margin-bottom: 8px;
      display: flex;
      justify-content: space-between;
      align-items: center;

      div {
        display: flex;
        justify-content: space-between;
        align-items: center;
        cursor: pointer;

        img {
          margin-right: 4px;
        }
      }
    }

    .apply-code {
      width: 100%;
      border: 1px solid #dfdfdf;
      word-wrap: break-word;
      padding: 10px;
      background-color: transparent;
      color: inherit;
    }

    .copy-btn {
      color: #0052d9;
      cursor: pointer;
    }

    .contact-link {
      color: #075ddf;
      cursor: pointer;
    }
  }
}

.apply-license-modal {
  width: 100%;
  padding: 14px;

  .body {
    display: flex;

    :deep(textarea) {
      height: 80px;
    }

    .steps {
      margin-right: 16px;

      .circle {
        width: 25px;
        height: 25px;
        border-radius: 50%;
        border: 1px solid #0052d9;
        font-size: 16px;
        text-align: center;
        line-height: 25px;
        color: #0052d9;
      }

      .line1,
      .line2 {
        width: 1px;
        background: #0052d9;
        margin: 12px;
      }

      .line1 {
        height: 28px;
      }

      .line2 {
        height: 100px;
      }
    }

    .copy-btn {
      color: #0052d9;
      cursor: pointer;
    }

    .contact-link {
      color: #075ddf;
      cursor: pointer;
    }

    .content {
      .tip {
        color: #666666;
        margin-top: 24px;
        width: 825px;
        padding: 15px;
        background: #fafafa;
        line-height: 18px;
        margin-bottom: 30px;

        .link {
          color: #075ddf;
          margin-top: 10px;

          a {
            color: #075ddf;
          }
        }
      }

      .section {
        .title {
          font-weight: 400;
          font-size: 16px;
          line-height: 24px;
          color: rgba(0, 0, 0, 0.9);
          margin-bottom: 8px;
        }

        .sub-title {
          color: rgba(0, 0, 0, 0.6);
          margin-bottom: 8px;
          display: flex;
          justify-content: space-between;
          align-items: center;

          div {
            display: flex;
            justify-content: space-between;
            align-items: center;
            cursor: pointer;

            img {
              margin-right: 4px;
            }
          }
        }

        .apply-code {
          width: 825px;
          border: 1px solid #dfdfdf;
          word-wrap: break-word;
          padding: 10px;
          background-color: transparent;
          color: inherit;
        }

        .copy-btn {
          color: #0052d9;
          cursor: pointer;
        }

        .contact-link {
          color: #075ddf;
          cursor: pointer;
        }
      }
    }
  }
}

.cluster-select {
  width: max-content;
}

.navbar-nav-nb,
.navbar-nav-nb-dropdown {
  a {
    font-size: 14px;
  }

  .iconfont {
    font-size: 20px;
  }
}

.navbar-nav-nb,
.navbar-nav-nb-dropdown {
  a {
    color: #ffffff;
    opacity: 0.85;

    &:hover {
      opacity: 1;
      color: #ffffff;
    }
  }

  float: right;
  position: relative;

  display: block;
  height: 48px;
  line-height: 48px;
  color: #ffffff;
  font-size: 12px;
  text-decoration: none;
  //margin-left: 10px;

  &:hover {
    /*background-color: #f8f8f9;*/
    cursor: pointer;
  }

  .menu-dropdown {
    border: 1px solid rgba(0, 0, 0, 0.1);
    border-radius: 2px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    position: absolute;
    width: 200px;
    background: #fff;
    right: 0;

    .info {
      border-bottom: 1px solid #dee5e7;
      color: #58666e;
      background-color: #edf1f2;
      padding: 15px;
      margin-bottom: 10px;
    }

    .divider {
      height: 1px;
      margin: 9px 0;
      overflow: hidden;
      background-color: #e5e5e5;
    }

    a {
      height: 30px;
      line-height: 30px;
      display: block;
      padding: 0 10px;
      color: #58666e;

      &:hover {
        background-color: #edf1f2;
      }
    }
  }
}

.navbar-nav-nb-dropdown {
  height: 30px;
  line-height: 30px;
  color: rgba(0, 0, 0, 0.88);

  a {
    color: rgba(0, 0, 0, 0.88);

    &:hover {
      opacity: 1;
      color: rgba(0, 0, 0, 0.88);
    }
  }
}

.deemph-text {
  span,
  a,
  strong {
    opacity: 1;
  }
}

.deemph-button {
  opacity: 1;
}

.layout-logo {
  width: 180px;
  height: 48px;
  display: flex;
  align-items: center;
  background-image: url('~@/assets/logo-BG.png');
  float: left;
  position: relative;
  color: #ffffff;
  padding-left: 20px;
  cursor: pointer;

  .iconfont {
    font-size: 22px;
  }
}

.uid-wrap {
  position: relative;
  top: -20px;

  span {
    font-size: 11px;
  }
}

// Layout switch transition: masks the height difference between
// sql-layout (compact header) and app-layout (card-based with padding)
.layout-switch-enter-active,
.layout-switch-leave-active {
  transition:
    opacity 0.18s ease,
    transform 0.18s ease;
}

.layout-switch-enter-from {
  opacity: 0;
  transform: translateY(6px);
}

.layout-switch-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}
</style>
