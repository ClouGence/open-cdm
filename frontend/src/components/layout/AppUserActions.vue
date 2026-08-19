<template>
  <div class="app-user-actions" :class="[`app-user-actions--${placement}`, { 'app-user-actions--compact': compact }]">
    <template v-if="!isDesktop">
      <Tooltip v-if="placement === 'header'" :content="$t('wen-dang')" transfer placement="bottom">
        <button type="button" class="header-action-icon" @click="handleGoHelp('document')">
          <CustomIcon type="icon-v2-ic_document" hoverStyle size="18px" />
        </button>
      </Tooltip>
      <Tooltip v-if="placement === 'header'" :content="$t('lian-xi-wo-men')" transfer placement="bottom">
        <button type="button" class="header-action-icon" @click="handleGoHelp('contact')">
          <CustomIcon type="icon-v2-icon_contact" hoverStyle size="18px" />
        </button>
      </Tooltip>
      <a-dropdown :trigger="['click']">
        <span class="message-icon">
          <cc-iconfont :size="20" name="message" />
          <span v-if="messageList.length > 0" class="message-point"></span>
        </span>
        <template #overlay>
          <p class="title">{{ $t('xiao-xi-zhong-xin') }}</p>
          <div v-for="message in messageList" :key="message.id" class="message-item" @click="handleGoMessageDetail(message.id)">
            <p>{{ CONSOLE_JOB_NAME[message.label] }}{{ $t('shi-bai') }}</p>
            <p class="time">{{ message.gmtModified }}</p>
          </div>
          <div class="message-footer" @click="handleGoMessage">
            {{ $t('cha-kan-geng-duo') }}
          </div>
        </template>
      </a-dropdown>
      <LangSwitcher>
        <template #trigger>
          <CustomIcon hover-style type="icon-v2-yuyanqiehuan" size="20px" />
        </template>
      </LangSwitcher>
      <div v-if="!compact && placement === 'header'" class="domain" translate="no">{{ userInfo.username }}</div>
      <div v-click-out-hide="hideMenu" class="avatar" @click="showMenu = !showMenu">
        <img :alt="$t('useravatar')" src="../../assets/head.png" />
      </div>
      <div v-if="placement === 'sidebar' && !compact" class="sidebar-user-label" translate="no">{{ userInfo.username }}</div>
      <div v-show="showMenu" class="menu" :class="{ 'menu--sidebar': placement === 'sidebar' }">
        <div class="one">
          <div class="avatar">
            <img :alt="$t('useravatar')" src="../../assets/head.png" width="28" height="28" />
          </div>
          <div class="domain">
            <div>{{ userInfo.username }}</div>
            <div v-if="!isInternalUser" class="provider-wrap">{{ $t('lai-yuan') }}: {{ userInfo.bindType }}</div>
            <div class="uid-wrap" @click.stop="handleCopyApplyCode(userInfo.uid)">
              <span>{{ `UID: ${userInfo.uid}` }}</span>
              <CustomIcon type="icon-v2-CopyOutline" size="12px" hoverStyle leftMargin />
            </div>
          </div>
        </div>
        <div class="two">
          <div v-if="userInfo.account">{{ $t('zhang-hao') }}: {{ userInfo.account }}</div>
          <div v-if="!isInternalUser">{{ $t('lai-yuan') }}: {{ userInfo.bindType }}</div>
          <div v-if="userInfo.phone">{{ $t('dian-hua') }}: {{ userInfo.phone }}</div>
          <div>{{ $t('you-xiang') }}: {{ userInfo.email }}</div>
        </div>
        <a v-if="isOidcLogout" class="four block" href="logout" @click="closeWebSocket">
          {{ $t('tui-chu-zhang-hao') }}
        </a>
        <a v-if="!isOidcLogout" class="four block" @click="logout">
          {{ $t('tui-chu-zhang-hao') }}
        </a>
      </div>
    </template>
    <template v-else>
      <div class="desktop-actions">
        <Tooltip :content="$t('wen-dang')" transfer placement="bottom">
          <button type="button" class="header-action-icon" @click="handleGoHelp('document')">
            <CustomIcon type="icon-v2-ic_document" hoverStyle size="18px" />
          </button>
        </Tooltip>
        <Tooltip :content="$t('lian-xi-wo-men')" transfer placement="bottom">
          <button type="button" class="header-action-icon" @click="handleGoHelp('contact')">
            <CustomIcon type="icon-v2-icon_contact" hoverStyle size="18px" />
          </button>
        </Tooltip>
        <a-tooltip trigger="hover">
          <cc-iconfont :size="18" name="help" />
          <template #title>
            <div style="display: flex; flex-direction: column; align-items: center">
              <div style="margin-bottom: 5px">{{ $t('jia-ru-wei-xin-jiao-liu-qun') }}</div>
              <img src="../../assets/wechat-clouddm.png" :width="100" :height="100" />
            </div>
          </template>
        </a-tooltip>
      </div>
    </template>
  </div>
</template>

<script>
import { mapGetters, mapState } from 'vuex';
import LangSwitcher from '@/components/LangSwitcher';
import { CONSOLE_JOB_NAME, LOGIN_TYPE } from '@/const';
import { handleCopy } from '@/utils/clipboard';
import { UPDATE_USERINFO } from '@/store/mutationTypes';
import { hasWebSocketInstance, webSocketClose } from '@/services/socket';

export default {
  name: 'AppUserActions',
  components: { LangSwitcher },
  props: {
    placement: {
      type: String,
      default: 'header'
    },
    compact: {
      type: Boolean,
      default: false
    }
  },
  emits: ['check-version'],
  data() {
    return {
      showMenu: false,
      messageList: [],
      CONSOLE_JOB_NAME
    };
  },
  computed: {
    ...mapGetters(['isDesktop', 'isInternalUser']),
    ...mapState(['userInfo']),
    isOidcLogout() {
      return this.userInfo.bindType === LOGIN_TYPE.OIDC && this.userInfo.loginType === LOGIN_TYPE.OIDC;
    }
  },
  watch: {
    userInfo(val) {
      if (val.showMessage) {
        this.listLastFiveFailedJob();
      }
    }
  },
  methods: {
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
        await this.$router.push({ name: 'Login' });
      }
    },
    handleGoHelp(data) {
      if (data === 'document') {
        let url = `${this.$store.state.dmDocUrlPrefix}/intro/product_intro`;
        if (this.isDesktop) {
          url = `${this.$store.state.dmDocUrlPrefix}/dmp-doc/releaseinfo/desktop_latest`;
        }
        window.open(url);
      } else if (data === 'contact') {
        window.open(this.$store.state.contactUsUrl);
      }
    },
    handleGoMessage() {
      this.$router.push({ path: '/system/info_center' }).catch(() => {});
    },
    handleGoMessageDetail(id) {
      this.$router.push({ path: `/system/console_job/${id}` });
    },
    handleCopyApplyCode(data) {
      handleCopy(data);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    async listLastFiveFailedJob() {
      const res = await this.$services.dmConsoleJobListLastFiveFailedJob();

      if (res.success && res.data.length > 0) {
        this.messageList = res.data;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.app-user-actions {
  display: flex;
  align-items: center;
  position: relative;
  gap: 12px;

  &--header {
    flex-shrink: 0;
  }

  &--sidebar {
    width: 100%;
    padding: 12px 12px 16px;
    gap: 10px;
    border-top: 1px solid var(--sidebar-border);
    cursor: pointer;

    .avatar {
      flex-shrink: 0;
    }

    .sidebar-user-label {
      flex: 1;
      min-width: 0;
      font-size: 13px;
      font-weight: 500;
      color: var(--text-primary);
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .menu {
      left: 12px;
      right: auto;
      bottom: calc(100% + 8px);
      top: auto;
    }
  }

  &--compact {
    gap: 8px;
  }

  .domain {
    font-size: 13px;
    color: var(--text-secondary);
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .avatar {
    cursor: pointer;
    width: 28px;
    height: 28px;
    border-radius: 6px;
    overflow: hidden;
    border: 1px solid var(--border-primary);

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .header-action-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 28px;
    height: 28px;
    padding: 0;
    border: none;
    border-radius: 6px;
    background: transparent;
    color: var(--text-secondary);
    cursor: pointer;
    transition:
      background-color 0.12s ease,
      color 0.12s ease;

    &:hover {
      background: var(--bg-hover);
      color: var(--text-primary);
    }
  }

  .message-icon {
    position: relative;
    cursor: pointer;
    color: var(--text-secondary);

    .message-point {
      position: absolute;
      top: 0;
      right: 0;
      width: 6px;
      height: 6px;
      border-radius: 50%;
      background: var(--error-color);
    }
  }

  .menu {
    position: absolute;
    top: calc(100% + 8px);
    right: 0;
    width: 280px;
    background: var(--bg-card);
    border: 1px solid var(--border-primary);
    border-radius: 8px;
    box-shadow: var(--shadow-lg);
    z-index: 1000;
    overflow: hidden;

    .one {
      display: flex;
      padding: 16px;
      gap: 12px;
      border-bottom: 1px solid var(--border-secondary);

      .domain {
        max-width: none;
      }

      .provider-wrap {
        color: var(--text-tertiary);
        font-size: 12px;
        margin-top: 2px;
      }
    }

    .two {
      padding: 16px 20px;
      line-height: 22px;
      font-size: 13px;
      color: var(--text-secondary);
    }

    .four {
      height: 44px;
      line-height: 44px;
      text-align: center;
      background: var(--bg-tertiary);
      color: var(--error-color);
      cursor: pointer;
      font-size: 13px;
      font-weight: 500;
    }
  }

  .desktop-actions {
    display: flex;
    align-items: center;
    gap: 12px;
  }
}
</style>
