<template>
  <div
    class="login"
    :class="{ 'is-dark': $store.state.theme === 'dark' }"
    :style="{ '--login-bg-pattern': `url(${backgroundPatternUrl})` }"
    v-if="!isDesktop"
  >
    <div class="login-left">
      <header class="login-topbar">
        <div class="login-header">
          <dm-logo-header />
        </div>
      </header>
      <div class="content">
        <div class="hero-column">
          <LoginHero />
        </div>
      </div>
      <footer class="login-bottombar">
        <dm-footer />
      </footer>
    </div>

    <div class="login-panel">
      <div class="panel-header">
        <h2 class="panel-title">{{ showMfa ? $t('duo-yin-zi-ren-zheng-yan-zheng-ma') : currentLoginTitle }}</h2>
        <p class="panel-subtitle" v-if="!showMfa && !isCompletionMode">{{ $t('deng-lu-miao-shu') }}</p>
      </div>
      <div class="panel-body">
        <div class="input-wrapper mt-4" :class="{ 'is-completion': isCompletionMode }" v-if="!showMfa">
          <template v-if="isCompletionMode">
            <div class="floating-field" :class="{ 'has-value': loginForm.registerInfo.name }">
              <span class="floating-label">{{ optionalLabel($t('yong-hu-ming')) }}</span>
              <a-input class="field-input" v-model:value="loginForm.registerInfo.name" @keydown.enter="handleEnter" size="large" />
            </div>
            <div class="floating-field" :class="{ 'has-value': loginForm.registerInfo.phone }">
              <span class="floating-label">{{ optionalLabel($t('shou-ji-hao')) }}</span>
              <a-input
                class="field-input"
                v-model:value="loginForm.registerInfo.phone"
                @update:value="clearCompletionCheck('phone')"
                @blur="checkCompletionDuplicate('phone', 'PHONE')"
                @keydown.enter="handleEnter"
                size="large"
              />
              <span class="field-success" v-if="completionValid.phone"></span>
            </div>
            <div class="field-error" v-if="completionErrors.phone" v-html="completionErrors.phone"></div>
            <div class="floating-field" :class="{ 'has-value': loginForm.registerInfo.email }">
              <span class="floating-label">{{ optionalLabel($t('you-xiang')) }}</span>
              <a-input
                class="field-input"
                v-model:value="loginForm.registerInfo.email"
                @update:value="clearCompletionCheck('email')"
                @blur="checkCompletionDuplicate('email', 'EMAIL')"
                @keydown.enter="handleEnter"
                size="large"
              />
              <span class="field-success" v-if="completionValid.email"></span>
            </div>
            <div class="field-error" v-if="completionErrors.email" v-html="completionErrors.email"></div>
          </template>
          <template v-else-if="isJumpLogin(currentLoginType)">
            <button
              :disabled="loginLoading || !currentLoginDef.available"
              :aria-busy="loginLoading"
              type="button"
              class="provider-login-button"
              :class="{ 'is-loading': loginLoading }"
              @click="handleGoJump(currentLoginDef)"
            >
              <CustomIcon
                v-if="currentLoginDef.icon"
                :resource="currentLoginDef.icon"
                :alt="currentLoginDef.iconTitle || currentLoginDef.tabTitle"
                size="44px"
              />
              <span>{{ currentLoginDef.tabTitle || $t('deng-lu') }}</span>
            </button>
          </template>
          <template v-else>
            <div class="floating-field" :class="{ 'has-value': loginForm.account }">
              <span class="floating-label">{{ accountLabel }}</span>
              <a-input class="field-input" v-model:value="loginForm.account" @keydown.enter="handleEnter" size="large" />
            </div>
            <div class="floating-field" :class="{ 'has-value': loginForm.password }">
              <span class="floating-label">{{ $t('mi-ma') }}</span>
              <a-input-password class="field-input" v-model:value="loginForm.password" @keydown.enter="handleEnter" size="large" />
            </div>
          </template>
        </div>
        <div class="input-wrapper mt-4" v-if="showMfa">
          <div class="floating-field" :class="{ 'has-value': mfaCode }" v-if="!mfaInvalidMode">
            <span class="floating-label">{{ $t('qing-shu-ru-liu-wei-shu-de-mfa-yan-zheng-ma') }}</span>
            <a-input class="field-input" @pressEnter="handleEnter2" @keydown.enter="handleEnter2" v-model:value="mfaCode" size="large" />
          </div>
          <p v-if="!mfaInvalidMode" class="opacity-60">
            {{ $t('nin-yi-kai-qi-le-duo-zi-yin-ren-zheng-pei-zhi-mei-ci-deng-lu-xu-yan-zheng-duo-yin-zi-ren-zheng-yan-zheng-ma') }}
          </p>
          <p v-else class="opacity-60 mfa-invalid-tip">{{ $t('mfa-yi-shi-xiao-deng-lu-ti-shi') }}</p>
        </div>
        <div class="completion-actions" v-if="!showMfa && isCompletionMode">
          <a-button :disabled="loginLoading" :loading="loginLoading" type="primary" size="large" class="completion-submit" @click="handleLogin">
            {{ $t('bu-quan-xin-xi-bing-deng-lu') }}
          </a-button>
          <a-button :disabled="loginLoading" size="large" class="completion-back" @click="goReLogin">
            {{ $t('fan-hui') }}
          </a-button>
        </div>
        <a-button
          v-if="!showMfa && !isCompletionMode && !isJumpLogin(currentLoginType)"
          :disabled="loginLoading || !currentLoginDef.available"
          :loading="loginLoading"
          type="primary"
          size="large"
          class="login-submit"
          @click="handleLogin"
        >
          {{ $t('deng-lu') }}
        </a-button>
        <div class="completion-actions" v-if="showMfa && !mfaInvalidMode">
          <a-button :disabled="loginLoading" :loading="loginLoading" type="primary" size="large" class="completion-submit" @click="handleMfaValid">
            {{ $t('yan-zheng') }}
          </a-button>
          <a-button :disabled="loginLoading" size="large" class="completion-back" @click="goReLogin">
            {{ $t('chong-xin-deng-lu') }}
          </a-button>
        </div>
        <div class="completion-actions" v-if="showMfa && mfaInvalidMode">
          <a-button :disabled="loginLoading" size="large" class="completion-submit mfa-invalid-action" @click="goHandleInvalidMfa">
            {{ $t('qu-chu-li') }}
          </a-button>
          <a-button :disabled="loginLoading" size="large" class="completion-back" @click="redirectToHome">
            {{ $t('shao-hou-chu-li') }}
          </a-button>
        </div>
        <div class="login-provider-switcher" v-if="!showMfa && !isCompletionMode && loginDef.length > 1">
          <button
            v-for="item in loginDef"
            :key="item.loginType"
            type="button"
            class="login-provider-icon"
            :class="{ active: item.loginType === currentLoginType, unavailable: !item.available }"
            :disabled="item.loginType === currentLoginType"
            :title="providerTitle(item)"
            @click="switchLoginType(item)"
          >
            <CustomIcon
              v-if="item.icon"
              :resource="item.icon"
              :alt="item.iconTitle || item.tabTitle"
              :disabled="!item.available"
              size="23px"
              topMargin="3px"
            />
            <span v-else class="login-provider-text">{{ item.tabTitle || item.iconTitle }}</span>
          </button>
        </div>
      </div>
      <footer class="panel-footer">
        <dm-footer />
      </footer>
    </div>
  </div>
</template>

<script>
import DmFooter from '@/components/DmFooter';
import DmLogoHeader from '@/components/DmLogoHeader';
import LoginHero from '@/views/login/LoginHero';
import { ACCOUNT_TYPE, LOGIN_TYPE } from '@/const';
import { mapGetters, mapState, mapActions } from 'vuex';
import { UPDATE_DM_GLOBAL_SETTING, UPDATE_GLOBAL_SETTING, UPDATE_PUBLIC_KEY } from '@/store/mutationTypes';
import { encryptMixin } from '@/mixins/encryptMixin';
import { isNumber } from '@/components/util';
import { filterGlobalSettingByBuild, supportsCloudDMBuild } from '@/utils/product';
import formatError from '@/services/formatError';
import { setPageIcon, WEBSIDE_FAVICON } from '@/utils/pluginResource';
import loginBgPattern from '@/assets/login/login-bg-pattern.svg';
import Toast from '@/utils/toast';

export default {
  name: 'Login',
  components: {
    DmLogoHeader,
    DmFooter,
    LoginHero
  },
  mixins: [encryptMixin],
  computed: {
    ...mapState(['defaultRedirectUrl']),
    ...mapGetters(['isDesktop']),
    backgroundPatternUrl() {
      return loginBgPattern;
    },
    currentLoginDef() {
      return this.loginDef.find((item) => item.loginType === this.currentLoginType) || this.loginDef[0] || {};
    },
    currentLoginTitle() {
      return this.$t('huan-ying-hui-lai');
    },
    accountLabel() {
      if (this.currentLoginType === LOGIN_TYPE.LOGIN_PASSWORD) {
        return `${this.$t('zhang-hao')}/${this.$t('you-xiang')}/${this.$t('shou-ji-hao')}`;
      }
      return this.$t('zhang-hao');
    },
    isCompletionMode() {
      return Boolean(this.loginCallbackData.completion);
    }
  },
  data() {
    return {
      ACCOUNT_TYPE,
      LOGIN_TYPE,
      loginForm: {
        accountType: ACCOUNT_TYPE.SUB_ACCOUNT,
        loginType: LOGIN_TYPE.LOGIN_PASSWORD,
        account: '',
        password: '',
        verifyCode: '',
        registerInfo: {}
      },
      currentLoginType: LOGIN_TYPE.LOGIN_PASSWORD,
      loginDef: [],
      mfaCode: '',
      mfaPreActionToken: '',
      errMsg: '',
      loginLoading: false,
      showMfa: false,
      mfaInvalidMode: false,
      loginCallbackData: {},
      completionErrors: {
        phone: '',
        email: ''
      },
      completionValid: {
        phone: false,
        email: false
      },
      completionCheckSeq: {
        phone: 0,
        email: 0
      },
      globalSettings: {
        features: {}
      }
    };
  },
  beforeUnmount() {
    this.loginLoading = false;
  },
  methods: {
    ...mapActions(['getUserInfo']),
    providerTitle(item) {
      if (item.available) {
        return item.iconTitle || item.tabTitle;
      }
      return item.errorInfo || item.iconTitle || item.tabTitle;
    },
    optionalLabel(label) {
      return `${label} (${this.$t('ke-xuan')})`;
    },
    setCurrentLoginType(loginType, clearForm = true) {
      this.currentLoginType = loginType || LOGIN_TYPE.LOGIN_PASSWORD;
      this.loginForm.loginType = this.currentLoginType;
      if (clearForm) {
        this.loginForm.account = '';
        this.loginForm.password = '';
        this.loginForm.verifyCode = '';
        this.errMsg = '';
      }
    },
    switchLoginType(item) {
      if (!item || item.loginType === this.currentLoginType) {
        return;
      }
      this.setCurrentLoginType(item.loginType);
      if (!item.available && item.errorInfo) {
        Toast.error(formatError(item.errorInfo));
      }
    },
    resolveRedirectUrl() {
      return this.defaultRedirectUrl || '/sql';
    },
    async redirectToHome() {
      await this.$router.push(this.resolveRedirectUrl());
    },
    isJumpLogin(loginType = this.currentLoginType) {
      const def = this.loginDef.find((item) => item.loginType === loginType);
      return Boolean(def && def.jump);
    },
    async requestJumpUrl(loginDef) {
      try {
        const res = await this.$services.requestJumpUrl({
          data: {
            type: loginDef.loginType
          },
          modal: false
        });
        if (res && res.success && res.data) {
          window.location.href = res.data;
        } else {
          Toast.error(this.resolveErrorMessage(res));
        }
      } catch (error) {
        Toast.error(this.resolveErrorMessage(error));
      } finally {
        this.loginLoading = false;
      }
    },
    resolveErrorMessage(error) {
      if (!error) {
        return this.$t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan');
      }
      const rawMsg = error.msgContent || error.msg || error.message;
      return formatError(rawMsg) || this.$t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan');
    },
    showCompleteForm(loginData, submittedForm) {
      const moreInfo = loginData.moreInfo || {};
      this.loginCallbackData = {
        completion: true,
        token: loginData.token || submittedForm.token,
        account: moreInfo.account,
        user: moreInfo.name,
        phone: moreInfo.phone,
        email: moreInfo.email,
        primaryUid: moreInfo.primaryUid
      };
      this.loginForm.registerInfo = {
        account: moreInfo.account,
        email: moreInfo.email,
        phone: moreInfo.phone,
        name: moreInfo.name,
        primaryUid: moreInfo.primaryUid
      };
      this.completionErrors = {
        phone: '',
        email: ''
      };
      this.completionValid = {
        phone: false,
        email: false
      };
    },
    clearCompletionCheck(field) {
      this.completionErrors[field] = '';
      this.completionValid[field] = false;
    },
    async checkCompletionDuplicate(field, checkType) {
      const registerInfo = this.loginForm.registerInfo || {};
      const checkContent = (registerInfo[field] || '').trim();
      this.completionErrors[field] = '';
      this.completionValid[field] = false;
      if (!checkContent) {
        return true;
      }
      const primaryUid = registerInfo.primaryUid || this.loginCallbackData.primaryUid;
      if (!primaryUid) {
        return true;
      }
      const checkSeq = this.completionCheckSeq[field] + 1;
      this.completionCheckSeq[field] = checkSeq;
      try {
        const res = await this.$services.checkSupplement({
          data: {
            primaryUid,
            checkType,
            checkContent
          },
          modal: false
        });
        if (checkSeq !== this.completionCheckSeq[field]) {
          return !this.completionErrors[field];
        }
        if (!res.success) {
          this.completionErrors[field] = this.resolveErrorMessage(res);
          return false;
        }
        this.completionValid[field] = true;
        return true;
      } catch (error) {
        if (checkSeq === this.completionCheckSeq[field]) {
          this.completionErrors[field] = this.resolveErrorMessage(error);
        }
        return false;
      }
    },
    async validateCompletionInfo() {
      const phoneValid = await this.checkCompletionDuplicate('phone', 'PHONE');
      const emailValid = await this.checkCompletionDuplicate('email', 'EMAIL');
      return phoneValid && emailValid;
    },
    async handleLogin() {
      this.errMsg = '';
      if (!this.isCompletionMode && !this.loginForm.account) {
        Toast.error(this.$t('zhang-hao-bu-neng-wei-kong') || this.$t('qing-shu-ru-zhang-hao'));
        return;
      }
      if (!this.isCompletionMode && !this.loginForm.password) {
        Toast.error(this.$t('mi-ma-bu-neng-wei-kong'));
        return;
      }
      if (!this.publicKey) {
        Toast.error(this.$t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan'));
        return;
      }
      if (this.isCompletionMode) {
        const completionValid = await this.validateCompletionInfo();
        if (!completionValid) {
          return;
        }
      }

      this.loginLoading = true;
      const isCompletionLogin = this.isCompletionMode;
      const data = {
        ...this.loginForm,
        accountType: ACCOUNT_TYPE.SUB_ACCOUNT,
        loginType: this.currentLoginType,
        password: this.loginForm.password ? this.passwordEncrypt(this.loginForm.password) : '',
        registerInfo: isCompletionLogin ? this.loginForm.registerInfo : null,
        token: isCompletionLogin ? this.loginCallbackData.token : null
      };
      try {
        const res = await this.$services.login({
          data,
          modal: false
        });
        if (res.success) {
          this.errMsg = '';
          if (res.data.needMore) {
            this.showCompleteForm(res.data, data);
          } else if (res.data.needMfa) {
            this.showMfa = true;
            this.mfaInvalidMode = false;
            this.mfaPreActionToken = res.data.mfaPreActionToken;
          } else if (res.data.mfaInvalid) {
            await this.getUserInfo();
            this.handleMfaInvalidLogin();
          } else {
            await this.getUserInfo();
            await this.redirectToHome();
          }
        } else {
          Toast.error(this.resolveErrorMessage(res));
        }
      } catch (error) {
        Toast.error(this.resolveErrorMessage(error));
      } finally {
        this.loginLoading = false;
      }
    },
    async handleMfaValid() {
      if (this.mfaInvalidMode) {
        return;
      }
      if (!this.mfaCode || !isNumber(this.mfaCode)) {
        Toast.error(this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma'));
        return;
      }
      this.loginLoading = true;
      try {
        const res = await this.$services.loginMfaValid({
          data: {
            mfaCode: this.mfaCode,
            mfaPreActionToken: this.mfaPreActionToken
          },
          modal: false
        });
        if (res.success) {
          await this.$store.dispatch('getUserInfo');
          await this.redirectToHome();
        } else {
          Toast.error(this.resolveErrorMessage(res));
        }
      } catch (error) {
        Toast.error(this.resolveErrorMessage(error));
      } finally {
        this.loginLoading = false;
      }
    },
    handleMfaInvalidLogin() {
      this.showMfa = true;
      this.mfaInvalidMode = true;
      this.mfaCode = '';
      this.mfaPreActionToken = '';
      this.errMsg = '';
    },
    goHandleInvalidMfa() {
      this.$router.push({ path: '/settings/profile', query: { tab: 'security' } });
    },
    handleGoJump(loginDef = this.currentLoginDef) {
      if (!loginDef.available) {
        Toast.error(this.resolveErrorMessage({ message: loginDef.errorInfo }));
        return;
      }
      this.loginLoading = true;
      this.requestJumpUrl(loginDef);
    },
    async goReLogin() {
      this.errMsg = '';
      this.showMfa = false;
      this.mfaInvalidMode = false;
      this.mfaCode = '';
      this.mfaPreActionToken = '';
      this.loginCallbackData = {};
      this.loginForm.account = '';
      this.loginForm.password = '';
      this.loginForm.verifyCode = '';
      this.loginForm.registerInfo = {};
      this.completionErrors = {
        phone: '',
        email: ''
      };
      this.completionValid = {
        phone: false,
        email: false
      };
      this.setCurrentLoginType(LOGIN_TYPE.LOGIN_PASSWORD, false);
      await this.$router.replace({ name: 'Login', query: {} });
    },
    handleEnter(arg) {
      if (arg.keyCode === 13) {
        this.handleLogin();
      }
    },
    handleEnter2(arg) {
      if (arg.keyCode === 13) {
        this.handleMfaValid();
      }
    },
    async applyCallbackQuery() {
      const query = this.$route.query || {};
      if (query.mfa === '1') {
        const challengeToken = Array.isArray(query.mfaPreActionToken) ? query.mfaPreActionToken[0] : query.mfaPreActionToken;
        await this.$router.replace({ name: 'Login', query: {} });
        this.loginCallbackData = {};
        this.mfaInvalidMode = false;
        this.mfaCode = '';
        if (!challengeToken) {
          this.showMfa = false;
          Toast.error(this.$t('mfa-deng-lu-zhuang-tai-wu-xiao-qing-zhong-xin-deng-lu'));
          return;
        }
        this.mfaPreActionToken = challengeToken;
        this.showMfa = true;
      } else if (query.token) {
        this.loginCallbackData = {
          ...query,
          completion: true
        };
        this.loginForm.account = this.loginCallbackData.account || this.loginCallbackData.sub || this.loginCallbackData.registerAccount || '';
        this.loginForm.registerInfo = {
          account: this.loginCallbackData.registerAccount || this.loginCallbackData.account,
          email: this.loginCallbackData.email,
          phone: this.loginCallbackData.phone,
          name: this.loginCallbackData.user,
          primaryUid: this.loginCallbackData.primaryUid
        };
        if (this.loginCallbackData.loginType) {
          this.setCurrentLoginType(this.loginCallbackData.loginType, false);
        }
      } else if (query.error) {
        this.loginCallbackData = query;
        Toast.error(`${query.error}:${query.error_description}`);
      }
    },
    async getGlobalSettings() {
      const res = await this.$services.getGlobalSettings({ data: {} });
      if (!res.success) {
        return;
      }

      const filteredGlobalSetting = filterGlobalSettingByBuild(res.data);
      this.globalSettings = filteredGlobalSetting;
      this.$store.commit(UPDATE_GLOBAL_SETTING, filteredGlobalSetting);
      if (supportsCloudDMBuild) {
        const dmRes = await this.$services.dmGlobalSettings();
        if (dmRes.success) {
          this.$store.commit(UPDATE_DM_GLOBAL_SETTING, dmRes.data);
          if (dmRes.data.publicKey) {
            this.$store.commit(UPDATE_PUBLIC_KEY, dmRes.data.publicKey);
          }
          this.loginDef = Array.isArray(dmRes.data.loginDef) ? dmRes.data.loginDef : [];
          this.setCurrentLoginType(dmRes.data.loginDefault || this.loginDef[0]?.loginType || LOGIN_TYPE.LOGIN_PASSWORD, false);
          if (dmRes.data.personal) {
            this.$i18n.global.locale.value = 'zh-CN';
            this.loginForm.account = dmRes.data.personal.account;
            this.loginForm.password = dmRes.data.personal.password;
            await this.handleLogin();
          }
        }
      }

      setPageIcon(WEBSIDE_FAVICON);
      document.title = 'CloudDM';
      this.$store.dispatch('setTheme', 'light');
      await this.applyCallbackQuery();
    }
  },
  created() {
    this.getGlobalSettings();
  }
};
</script>

<style lang="less" scoped>
.login {
  --login-ink: #171717;
  --login-muted: #707070;
  --login-hairline: #dfdfdf;
  --login-emerald: #3ecf8e;
  --login-emerald-deep: #24b47e;
  --login-surface: #ffffff;
  --login-canvas: #f8fafc;

  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
  background-color: var(--login-canvas);
  background-image: var(--login-bg-pattern);
  background-size: cover;
  background-position: center;

  .login-left {
    display: flex;
    flex-direction: column;
    flex: 1 0 auto;
    min-width: 0;
    min-height: 0;
    position: relative;
  }

  .login-topbar {
    flex: 0 0 72px;
    position: relative;
    z-index: 3;

    .login-header {
      position: relative;
      display: block;
      padding: 0 32px;
      height: 72px;
    }
  }

  .content {
    flex: 1 1 auto;
    min-height: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: auto;
    padding-right: 480px;

    .hero-column {
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 48px 32px;
      min-height: 0;
      width: 100%;
    }
  }

  .login-bottombar {
    flex: 0 0 auto;
    position: relative;
    padding: 0 32px 24px;
    padding-right: calc(480px + 32px);

    :deep(.footer) {
      height: auto;
      line-height: 1.5;
      text-align: center;
    }
  }

  .login-panel {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    width: 480px;
    z-index: 2;
    background: var(--login-surface);
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 80px 56px;
    animation: panel-enter 0.55s cubic-bezier(0.22, 1, 0.36, 1) 0.08s both;

    .panel-header {
      margin-bottom: 40px;
    }

    .panel-title {
      margin: 0;
      color: var(--login-ink);
      font-size: 28px;
      font-weight: 500;
      line-height: 1.2;
      letter-spacing: -0.42px;
    }

    .panel-subtitle {
      color: var(--login-muted);
      font-size: 14px;
      font-weight: 400;
      line-height: 1.5;
      margin: 8px 0 0;
    }

    .panel-footer {
      display: none;

      :deep(.footer) {
        height: auto;
        line-height: 1.5;
      }
    }

    .panel-body {
      width: 100%;
      max-width: 368px;
      box-sizing: border-box;

      .input-wrapper {
        & > div {
          margin-bottom: 16px;
        }

        .mfa-invalid-tip {
          white-space: pre-line;
        }

        &.is-completion {
          margin-top: 4px !important;

          & > div {
            margin-bottom: 10px;
          }
        }
      }

      .floating-field {
        --field-border: var(--login-hairline);
        --field-active: var(--login-emerald-deep);
        position: relative;
        display: flex;
        align-items: center;
        min-height: 56px;
        width: 100%;
        margin-bottom: 16px;
        padding: 16px;
        border: 1px solid var(--field-border);
        border-radius: 8px;
        background: var(--login-surface);
        transition:
          border-color 0.2s ease,
          box-shadow 0.2s ease;

        &:hover:not(:focus-within) {
          border-color: #c7c7c7;
        }

        &:focus-within {
          border-color: var(--field-active);
          box-shadow: 0 0 0 3px rgba(62, 207, 142, 0.15);
        }

        .floating-label {
          position: absolute;
          left: 16px;
          top: 50%;
          z-index: 2;
          max-width: calc(100% - 48px);
          padding: 0;
          background: transparent;
          color: #9a9a9a;
          font-size: 16px;
          line-height: 1;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          pointer-events: none;
          transform: translateY(-50%);
          transition:
            top 0.2s ease,
            font-size 0.2s ease,
            color 0.2s ease,
            transform 0.2s ease;
        }

        &:focus-within .floating-label,
        &.has-value .floating-label {
          top: 0;
          transform: translateY(-50%);
          font-size: 12px;
          color: var(--field-active);
          background: var(--login-surface);
          padding: 0 4px;
          left: 12px;
        }

        :deep(.ant-input),
        :deep(.ant-input-affix-wrapper) {
          width: 100%;
          height: 24px !important;
          padding: 0 !important;
          margin: 0;
          border: 0 !important;
          box-shadow: none !important;
          outline: none !important;
          background: transparent !important;
          color: var(--login-ink);
          font-size: 16px;
          line-height: 24px;
        }

        :deep(.ant-input-affix-wrapper) {
          display: flex;
          align-items: center;
        }

        :deep(.ant-input-affix-wrapper .ant-input) {
          height: auto !important;
          flex: 1 1 auto;
        }

        :deep(.ant-input-affix-wrapper .ant-input-suffix) {
          display: flex;
          align-items: center;
          background: transparent;
        }

        :deep(.ant-input-password-icon) {
          color: #9a9a9a;
          transition: color 0.2s ease;
        }

        &:focus-within :deep(.ant-input-password-icon) {
          color: var(--login-emerald-deep);
        }

        .field-success {
          position: relative;
          flex: 0 0 18px;
          width: 18px;
          height: 18px;
          margin-left: 8px;
        }

        .field-success::after {
          content: '';
          position: absolute;
          top: 2px;
          left: 6px;
          width: 6px;
          height: 11px;
          border-right: 2px solid var(--login-emerald);
          border-bottom: 2px solid var(--login-emerald);
          transform: rotate(45deg);
        }

        :deep(.ant-input[disabled]) {
          color: rgba(23, 23, 23, 0.55);
          background: transparent;
        }
      }

      .field-error {
        margin: -8px 0 12px;
        color: #ff2201;
        font-size: 12px;
        line-height: 18px;
        text-align: left;
      }

      .login-submit {
        width: 100%;
        height: 48px;
        margin-top: 8px;
        margin-bottom: 8px;
        border-radius: 10px;
        font-size: 16px;
        font-weight: 500;
        letter-spacing: 0.01em;
        background: #3ecf8e;
        border-color: #3ecf8e;
        color: #171717;
        box-shadow: 0 2px 8px rgba(62, 207, 142, 0.25);
        transition:
          transform 0.15s ease,
          box-shadow 0.2s ease,
          background 0.2s ease;

        span {
          font-size: 16px;
        }

        &:hover,
        &:focus {
          background: #24b47e;
          border-color: #24b47e;
          color: #171717;
          box-shadow: 0 4px 16px rgba(62, 207, 142, 0.35);
        }

        &:not(:disabled):active {
          background: #1ea06a;
          border-color: #1ea06a;
          transform: scale(0.985);
        }

        &.is-mfa {
          margin-top: 32px;
        }
      }

      .completion-actions {
        display: flex;
        gap: 12px;
        margin-top: 20px;
        margin-bottom: 8px;

        .ant-btn {
          margin: 0;
          height: 48px;
          border-radius: 10px;
        }

        .completion-submit {
          flex: 1 1 75%;
        }

        .completion-back {
          flex: 0 0 25%;
        }

        .mfa-invalid-action {
          color: #ff2201;
          border-color: #ff2201;
          background: var(--login-surface);

          &:hover,
          &:focus {
            color: #e2005a;
            border-color: #e2005a;
            background: var(--login-surface);
          }
        }
      }

      .login-provider-switcher {
        display: flex;
        flex-wrap: wrap;
        gap: 12px;
        justify-content: center;
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px dashed var(--login-hairline);
      }

      .login-provider-icon {
        position: relative;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        width: 44px;
        height: 44px;
        padding: 0;
        border-radius: 10px;
        border: 1px solid var(--login-hairline);
        background: var(--login-surface);
        color: var(--login-ink);
        cursor: pointer;
        appearance: none;
        transition:
          border-color 0.2s ease,
          background-color 0.2s ease,
          box-shadow 0.2s ease;

        &:hover:not(:disabled) {
          border-color: var(--login-emerald);
          background-color: rgba(62, 207, 142, 0.06);
        }

        &.active {
          border-color: var(--login-emerald-deep);
          background-color: rgba(62, 207, 142, 0.1);
          cursor: default;
        }

        &.unavailable {
          opacity: 0.45;
          cursor: not-allowed;
        }

        .current-arrow {
          position: absolute;
          top: -5px;
          left: 50%;
          width: 6px;
          height: 6px;
          background: var(--login-emerald-deep);
          border-radius: 50%;
          transform: translateX(-50%);
        }

        .login-provider-text {
          font-size: 12px;
          font-weight: 500;
          line-height: 1;
        }
      }

      .provider-login-button {
        width: 160px;
        height: 160px;
        margin: 16px auto 24px;
        padding: 20px 16px;
        border-radius: 12px;
        border: 1px solid var(--login-hairline);
        background: var(--login-surface);
        color: var(--login-ink);
        cursor: pointer;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: 14px;
        font: inherit;
        line-height: 1.35;
        white-space: normal;
        box-shadow: 0 2px 8px rgba(23, 23, 23, 0.05);
        appearance: none;
        transition:
          border-color 0.2s ease,
          background-color 0.2s ease,
          box-shadow 0.2s ease,
          transform 0.15s ease;

        &:hover,
        &:focus-visible {
          color: var(--login-emerald-deep);
          border-color: var(--login-emerald);
          background-color: rgba(62, 207, 142, 0.06);
          box-shadow: 0 8px 24px rgba(62, 207, 142, 0.12);
          outline: none;
          transform: translateY(-2px);
        }

        &:disabled {
          cursor: not-allowed;
          opacity: 0.55;
        }

        &.is-loading {
          pointer-events: none;
        }

        span {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          font-size: 14px;
          font-weight: 500;
          text-align: center;
        }
      }
    }
  }

  &.is-dark {
    --login-ink: #f5f5f5;
    --login-muted: #9a9a9a;
    --login-hairline: #333333;
    --login-surface: #1c1c1c;
    --login-canvas: #121212;
  }
}

@keyframes panel-enter {
  from {
    opacity: 0;
    transform: translateX(16px);
  }

  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@media (max-width: 1024px) {
  .login {
    background-image: none;
    overflow-y: auto;

    .login-left {
      flex: 0 0 auto;
    }

    .content {
      display: none;
    }

    .login-bottombar {
      display: none;
    }

    .login-panel {
      position: relative;
      top: auto;
      right: auto;
      bottom: auto;
      width: 100%;
      flex: 1;
      padding: 48px 32px;

      .panel-footer {
        display: block;
        margin-top: 48px;
        text-align: center;
      }
    }
  }
}

@media (max-width: 680px) {
  .login {
    .login-topbar .login-header {
      padding: 0 16px;
    }

    .login-panel {
      padding: 40px 24px;

      .panel-title {
        font-size: 22px;
      }
    }
  }
}
</style>
