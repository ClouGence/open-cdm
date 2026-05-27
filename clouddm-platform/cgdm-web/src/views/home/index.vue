<template>
  <div class="home">
    <dm-water-mark :input-text="watermarkStr" ref="watermark" v-if="!isDesktop && globalSetting.enableWaterMark" />
    <div class="header">
      <div class="brand-container" @click="handleGoBackHome">
        <cc-iconfont :size="18" color="#FEFEFE" name="dm"></cc-iconfont>
      </div>
      <navbar style="flex: 1" />
      <div class="user-info" v-if="!isDesktop">
        <Dropdown @on-click="handleGoHelp" transfer>
          <CustomIcon type="icon-v2-help" hoverStyle size="18px" />
          <template #list>
            <DropdownItem name="document">
              <CustomIcon type="icon-v2-ic_document" hoverStyle size="14px" />
              {{ $t('wen-dang') }}
            </DropdownItem>
            <DropdownItem name="contact">
              <CustomIcon type="icon-v2-icon_contact" hoverStyle size="14px" />
              {{ $t('lian-xi-wo-men') }}
            </DropdownItem>
          </template>
        </Dropdown>
        <!-- 主题切换按钮 -->
        <a-dropdown v-if="userInfo.menuItems && userInfo.menuItems.includes('/ccsystem/console_job')" :trigger="['click']">
          <span class="message-icon">
            <cc-iconfont :size="20" name="message" />
            <span v-if="messageList.length > 0" class="message-point"></span>
          </span>
          <template #overlay>
            <p class="title">{{ $t('xiao-xi-zhong-xin') }}</p>
            <div v-for="message in messageList" :key="message.id" class="message-item" @click="handleGoMessageDetail(message.id)">
              <p>{{ CONSOLE_JOB_NAME[message.label] }}{{ $t('shi-bai') }}</p>
              <p class="time">{{ message.gmtModified || formatTime('YYYY-MM-DD HH:mm:ss') }}</p>
            </div>
            <div class="message-footer" @click="handleGoMessage">
              {{ $t('cha-kan-geng-duo') }}
            </div>
          </template>
        </a-dropdown>
        <!--        语言切换-->
        <LangSwitcher>
          <template #trigger>
            <CustomIcon hover-style type="icon-v2-yuyanqiehuan" size="20px" />
          </template>
        </LangSwitcher>
        <div class="domain" translate="no">{{ userInfo.username }}</div>
        <div v-click-out-hide="hideMenu" class="avatar" @click="showMenu = !showMenu">
          <img :alt="$t('useravatar')" src="../../assets/head.png" />
        </div>
        <div v-show="showMenu" class="menu">
          <div class="one">
            <div class="avatar">
              <img :alt="$t('useravatar')" src="../../assets/head.png" width="28" height="28" />
            </div>
            <div class="domain">
              <div>
                {{ userInfo.username }}
                <a-tag color="orange" class="parent-account-label" v-if="userInfo.accountType === 'PRIMARY_ACCOUNT' && !isSaas">
                  {{ $t('zhu') }}
                </a-tag>
              </div>
              <div class="uid-wrap" @click.stop="handleCopyApplyCode(userInfo.uid)">
                <span>{{ `UID: ${userInfo.uid}` }}</span>
                <CustomIcon type="icon-v2-CopyOutline" size="12px" hoverStyle leftMargin />
              </div>
            </div>
            <!--            <span v-if="userInfo.accountType!=='PRIMARY_ACCOUNT'">-->
            <!--              <a-popover v-model="showAccount" placement="left" trigger="hover">-->
            <!--              <p slot="content">-->
            <!--                {{ userInfo.loginAccount }}-->
            <!--              </p>-->
            <!--              <cc-iconfont style="margin-left: 10px;margin-right: 6px" name="account"-->
            <!--                           color="#555555"-->
            <!--                           :size="20"></cc-iconfont>-->
            <!--            </a-popover>-->
            <!--            <a @click="handleCopy(userInfo.loginAccount)">{{ $t('fu-zhi-zhang-hao') }}</a>-->
            <!--            </span>-->
          </div>
          <div class="two">
            <div v-if="userInfo.phone">{{ $t('dian-hua') }}: {{ userInfo.phone }}</div>
            <div>{{ $t('you-xiang') }}: {{ userInfo.email }}</div>
            <!--            <div v-if="userInfo.accountType!=='PRIMARY_ACCOUNT'">{{$t('suo-shu-zhu-zhang-hao')}}: {{ userInfo.pusername }}({{userInfo.pemail}})</div>-->
          </div>
          <!--          <div class="three" v-if="isAsia">-->
          <!--            <div @click="goUserCenter">-->
          <!--              <a-icon style="font-size:12px;color:#555555" type="user" />-->
          <!--              {{ $t('zhang-hu-zhong-xin') }}-->
          <!--            </div>-->
          <!--            <div @click="goMyAuth" v-if="userInfo.accountType !== 'PRIMARY_ACCOUNT'">-->
          <!--              <a-icon style="font-size:12px;color:#555555" type="crown" />-->
          <!--              {{ $t('wo-de-quan-xian') }}-->
          <!--            </div>-->
          <!--          </div>-->
          <a v-if="userInfo.bindType === 'OIDC'" class="four block" href="logout" @click="closeWebSocket">
            {{ $t('tui-chu-zhang-hao') }}
          </a>
          <a v-if="userInfo.bindType !== 'OIDC'" class="four block" @click="logout">
            {{ $t('tui-chu-zhang-hao') }}
          </a>
        </div>
      </div>
      <div v-else style="display: flex">
        <div class="version" style="display: flex; align-items: center; position: relative">
          <a-button class="deemph-button" type="link" size="large" @click="checkVersion(true)" style="font-weight: bold">
            {{ buildVersion }}
            <span style="color: red" v-if="version.newVersion">{{ $t('new') }}</span>
          </a-button>
        </div>
        <a-tooltip trigger="hover">
          <cc-iconfont :size="20" name="help" style="margin-right: 30px" />
          <template #title>
            <div style="display: flex; flex-direction: column; align-items: center">
              <div style="margin-bottom: 5px">{{ $t('jia-ru-wei-xin-jiao-liu-qun') }}</div>
              <img src="../../assets/wechat-clouddm.png" :width="100" :height="100" />
            </div>
          </template>
        </a-tooltip>
      </div>
    </div>
    <div class="user-expr-tip" v-if="userInfo.subAccountPwdValidDays !== null && userInfo.subAccountPwdValidDays < limitDays">
      {{ $t('gen-ju-zhu-zhang-hao-she-zhi-de-mi-ma-shi-xiao-ce-lue', [userInfo.subAccountPwdValidDays + 1]) }}
    </div>
    <div class="content-container" v-if="showChild">
      <router-view />
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
        <a-button type="primary" @click="handleDetailCopy">{{ $t('fu-zhi') }}</a-button>
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
        <h2>{{ $t('dang-qian-ban-ben') }}{{ this.buildVersion }}</h2>
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
import { mapGetters, mapState } from 'vuex';
import Navbar from '@/components/Navbar';
import LangSwitcher from '@/components/LangSwitcher';
import { setApprovalProcessMixin, setOpPasswordMixin } from '@/mixins/modal';
import enterOpPwdMixin from '@/mixins/modal/enterOpPwdMixin';
import { CONSOLE_JOB_NAME } from '@/const';
import XEClipboard from 'xe-clipboard';
import DmWaterMark from '@/components/widgets/DmWaterMark';
import store from '@/store';
import dayjs from 'dayjs';
import { handleCopy } from '@/utils/clipboard';
import fecha from 'fecha';
import { UPDATE_USERINFO } from '@/store/mutationTypes';
import { hasWebSocketInstance, webSocketClose } from '@/services/socket';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import { setAppLanguage } from '@/i18n';

export default {
  name: 'Home',
  components: {
    DmWaterMark,
    Navbar,
    LangSwitcher
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
      showMenu: false,
      showAccount: false,
      hasMessage: false,
      messageList: [],
      limitDays: 6,
      CONSOLE_JOB_NAME,
      watermarkStr: '',
      store,
      fecha,
      dayjs
    };
  },
  mixins: [setOpPasswordMixin, setApprovalProcessMixin, enterOpPwdMixin],
  computed: {
    ...mapGetters(['isDesktop', 'buildVersion', 'includesDM']),
    ...mapState(['userInfo', 'myAuth', 'globalSetting', 'defaultRedirectUrl', 'dmGlobalSetting', 'remainTrialDay']),
    ...mapGetters(['isSaas']),
    getDmProductClusterList() {
      return [];
    }
  },
  async created() {
    await this.$store.dispatch('getRegionList');

    await this.$store.dispatch('getDmGlobalConfig');

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
      console.log('showCellDetailModal', data);
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
        // 初始化内容（显示初始值）
        this.cellDetailContent = this.selectedCellDetail.cellValue || '';
        // 如果 error 或 mask 为 true，则禁用加载更多
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
      console.log(msg);
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
    /* eslint-disable */
    setupBaiduTongji() {
      var _hmt = _hmt || [];
      _hmt.push([
        '_requirePlugin',
        'UrlChangeTracker',
        {
          shouldTrackUrlChange: function (newPath, oldPath) {
            return newPath && oldPath;
          }
        }
      ]);
      window._hmt = _hmt; // 修改为window 全局变量
      (function () {
        var hm = document.createElement('script');
        hm.src = 'https://hm.baidu.com/hm.js?b86e2e73fd7517d78d5ccdf0cd12384c'; //此处请替换你的站点id
        var s = document.getElementsByTagName('script')[0];
        s.parentNode.insertBefore(hm, s);
      })();
    },
    /* eslint-enable */
    handleDetailCopy() {
      if (XEClipboard.copy(this.cellDetailContent)) {
        this.$Message.success(this.$t('fu-zhi-cheng-gong'));
      }
    },
    handleCloseCellDetailModal() {
      this.showDetailModal = false;
      // 重置状态
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

      // 如果 error 或 mask 为 true，则不允许加载更多
      if (this.selectedCellDetail.error || this.selectedCellDetail.mask) {
        return;
      }

      const { resultId, rowNumber, colNumber } = this.selectedCellDetail;

      if (!resultId || rowNumber < 0 || colNumber < 0) {
        console.error('缺少必要参数:', { resultId, rowNumber, colNumber });
        return;
      }

      // 计算 offset（当前内容的长度）
      const offset = this.cellDetailContent?.length || 0;

      const fetchSize = 128 * 1024; // 128K 字符

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
          // 接口返回的数据结构是 { value: { complete, mask, error, moreSize, totalSize, value } }
          const dataValue = res.data.value || res.data;
          const { value, moreSize, totalSize, complete, error } = dataValue;

          if (error) {
            this.$Message.error(this.$t('jia-zai-shu-ju-shi-bai'));
            return;
          }

          // 追加内容（因为初始值已经显示，这里只需要追加新数据）
          this.cellDetailContent += value || '';

          // 更新 moreSize 和 totalSize
          this.selectedCellDetail.moreSize = moreSize || 0;
          this.selectedCellDetail.totalSize = totalSize || 0;
          // 如果 error 或 mask 为 true，则禁用加载更多
          const canLoadMore = !this.selectedCellDetail.error && !this.selectedCellDetail.mask;
          this.hasMoreData = canLoadMore && (moreSize || 0) > 0;
        } else {
          this.$Message.error(res.message || this.$t('jia-zai-shu-ju-shi-bai'));
        }
      } catch (error) {
        console.error('加载单元格数据失败:', error);
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
    goAsyncJobList() {
      this.$router.push({ name: 'ASYNC_JOB_LIST' });
    },
    async listLastFiveFailedJob() {
      const res = await this.$services.dmConsoleJobListLastFiveFailedJob();

      if (res.success) {
        if (res.data.length > 0) {
          this.messageList = res.data;
        }
      }
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
    hideMenu() {
      this.showMenu = false;
    },
    closeWebSocket() {
      if (hasWebSocketInstance()) {
        webSocketClose();
      }
    },
    async logout() {
      const res = await this.$services.logout();

      if (res.success) {
        this.closeWebSocket();
        await this.$store.commit(UPDATE_USERINFO);
        // this.$refs.watermark.removeMaskDiv();
        await this.$router.push({ name: 'Login' });
      }
    },
    handleGoHelp(data) {
      if (data === 'document') {
        let url = `${store.state.dmDocUrlPrefix}/intro/product_intro`;
        if (this.isDesktop) {
          url = `${store.state.dmDocUrlPrefix}/dmp-doc/releaseinfo/desktop_latest`;
        }
        window.open(url);
      } else if (data === 'contact') {
        window.open(store.state.contactUsUrl);
      }
    },
    handleGoMessage() {
      this.$router.push({ path: '/system/info_center' }).catch(() => {});
    },
    handleGoMessageDetail(id) {
      this.$router.push({ path: `/system/console_job/${id}` });
    },
    goUserCenter() {
      this.$router.push({ path: '/userCenter' });
    },
    goMyAuth() {
      this.$router.push({ path: '/system/permission' });
    },

    handleCloseModal() {
      this.showInactiveModal = false;
    },
    handleCopyApplyCode(data) {
      handleCopy(data);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    }
  },
  watch: {
    userInfo(val) {
      if (val.showMessage) {
        this.listLastFiveFailedJob();
      }
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
    background: rgba(255, 24, 21, 0.3);
    position: absolute;
    margin-top: 48px;
    text-align: center;
  }

  //overflow: hidden;
  .header {
    background: var(--header-bg);
    display: flex;
    justify-content: space-between;
    color: var(--header-text);
    height: 49px;
    line-height: 48px;
    background-position: left;
    background-size: 200px 49px;
    background-repeat: no-repeat;
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    z-index: 999;
    box-shadow: var(--header-shadow);

    .ant-menu-dark {
      background: none;
    }

    .brand-container {
      padding: 0 15px;
    }

    .ant-menu-horizontal {
      line-height: 48px;
    }

    .user-info {
      display: flex;
      align-items: center;
      position: relative;

      & > i {
        opacity: 0.7;
        cursor: pointer;
      }

      & > *:not(:last-child) {
        margin-right: 20px;
        font-size: 14px;
      }

      .icon-help {
        font-size: 20px;
      }

      .avatar {
        cursor: pointer;
        width: 28px;
        height: 28px;

        img {
          width: 100%;
          height: 100%;
        }
      }

      .message-icon {
        position: relative;
        cursor: pointer;

        i {
          opacity: 0.7;
        }

        .message-point {
          position: absolute;
          background: #ff6e0d;
          width: 6px;
          height: 6px;
          display: block;
          border-radius: 50%;
          right: -2px;
          top: 6px;
        }
      }

      .menu {
        z-index: 999;
        position: absolute;
        width: 300px;
        top: 40px;
        right: 0;
        color: rgba(0, 0, 0, 0.88);
        box-shadow: 1px 1px 6px 0px rgba(164, 164, 164, 0.66);
        background: white;

        .avatar {
          width: 42px;
          height: 42px;
        }

        & > div:not(:last-child) {
          border-bottom: 1px solid rgba(223, 223, 223, 1);
        }

        .one {
          display: flex;
          align-items: center;
          padding: 20px;

          .domain {
            margin-left: 10px;
            font-size: 14px;
          }

          .parent-account-label {
            margin-left: 6px;
          }
        }

        .two {
          padding: 20px;
          line-height: 22px;

          div {
            margin-left: 0;
          }
        }

        .three {
          padding: 15px 24px;

          div {
            cursor: pointer;
            height: 22px;
            line-height: 22px;

            i {
              margin-right: 15px;
            }
          }
        }

        .four {
          height: 40px;
          line-height: 40px;
          text-align: center;
          background: #ececec;
          color: #ff6e0d;
          cursor: pointer;
        }
      }
    }
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
    padding-top: 48px;
    height: 100%;
    overflow-y: auto;
    height: 100%;
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
</style>
