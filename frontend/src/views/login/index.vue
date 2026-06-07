<template>
  <div class="login" :class="{ 'is-dark': $store.state.theme === 'dark' }" v-if="!isDesktop">
    <header>
      <div class="login-header">
        <dm-logo-header />
      </div>
    </header>
    <div class="content">
      <div :class="`has-background ${backgroundClass}`">
        <div class="login-card">
          <a-tabs :activeKey="activeTabKey" size="large">
            <a-tab-pane key="LOGIN" :tab="currentLoginTitle"></a-tab-pane>
            <a-tab-pane key="MFA" v-if="showMfa" :tab="$t('duo-yin-zi-ren-zheng-yan-zheng-ma')"></a-tab-pane>
          </a-tabs>
          <div class="tabs-content">
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
              <a-input
                class="mb-4"
                @pressEnter="handleEnter2"
                @keydown.enter="handleEnter2"
                v-model:value="mfaCode"
                size="large"
                :placeholder="$t('qing-shu-ru-liu-wei-shu-de-mfa-yan-zheng-ma')"
              />
              <p class="opacity-60">
                {{ $t('nin-yi-kai-qi-le-duo-zi-yin-ren-zheng-pei-zhi-mei-ci-deng-lu-xu-yan-zheng-duo-yin-zi-ren-zheng-yan-zheng-ma') }}
              </p>
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
            <a-button
              v-if="showMfa"
              key="mfa"
              :disabled="loginLoading"
              :loading="loginLoading"
              size="large"
              class="login-submit is-mfa"
              type="primary"
              @click="handleMfaValid"
            >
              {{ $t('yan-zheng') }}
            </a-button>
            <div class="msgContent" v-if="errMsg">
              <a-alert banner type="error">
                <template #message>
                  <div v-html="errMsg"></div>
                </template>
              </a-alert>
            </div>
            <div class="login-provider-switcher" v-if="!showMfa && !isCompletionMode && loginDef.length">
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
                <span class="current-arrow" v-if="item.loginType === currentLoginType"></span>
                <CustomIcon
                  v-if="item.icon"
                  :resource="item.icon"
                  :alt="item.iconTitle || item.tabTitle"
                  :disabled="!item.available"
                  size="23px"
                  topMargin="3px"
                />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <footer>
      <dm-footer />
    </footer>
  </div>
</template>

<script>
import DmFooter from '@/components/DmFooter';
import DmLogoHeader from '@/components/DmLogoHeader';
import { ACCOUNT_TYPE, LOGIN_TYPE } from '@/const';
import { mapGetters, mapState, mapActions } from 'vuex';
import { UPDATE_DM_GLOBAL_SETTING, UPDATE_GLOBAL_SETTING, UPDATE_PUBLIC_KEY } from '@/store/mutationTypes';
import { encryptMixin } from '@/mixins/encryptMixin';
import { isNumber } from '@/components/util';
import { filterGlobalSettingByBuild, supportsCloudDMBuild } from '@/utils/product';
import formatError from '@/services/formatError';
import { setPageIcon, WEBSIDE_FAVICON } from '@/utils/pluginResource';

export default {
  name: 'Login',
  components: {
    DmLogoHeader,
    DmFooter
  },
  mixins: [encryptMixin],
  computed: {
    ...mapState(['defaultRedirectUrl']),
    ...mapGetters(['isDesktop']),
    backgroundClass() {
      return 'dm-background';
    },
    activeTabKey() {
      return this.showMfa ? 'MFA' : 'LOGIN';
    },
    currentLoginDef() {
      return this.loginDef.find((item) => item.loginType === this.currentLoginType) || this.loginDef[0] || {};
    },
    currentLoginTitle() {
      return this.currentLoginDef.tabTitle || this.$t('deng-lu');
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
        this.errMsg = formatError(item.errorInfo);
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
          this.errMsg = this.resolveErrorMessage(res);
        }
      } catch (error) {
        this.errMsg = this.resolveErrorMessage(error);
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
        this.errMsg = this.$t('zhang-hao-bu-neng-wei-kong') || this.$t('qing-shu-ru-zhang-hao');
        return;
      }
      if (!this.isCompletionMode && !this.loginForm.password) {
        this.errMsg = this.$t('mi-ma-bu-neng-wei-kong');
        return;
      }
      if (!this.publicKey) {
        this.errMsg = this.$t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan');
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
            this.mfaPreActionToken = res.data.mfaPreActionToken;
          } else {
            await this.getUserInfo();
            await this.redirectToHome();
          }
        } else {
          this.errMsg = this.resolveErrorMessage(res);
        }
      } catch (error) {
        this.errMsg = this.resolveErrorMessage(error);
      } finally {
        this.loginLoading = false;
      }
    },
    async handleMfaValid() {
      if (!this.mfaCode || !isNumber(this.mfaCode)) {
        this.errMsg = this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma');
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
          this.errMsg = this.resolveErrorMessage(res);
        }
      } catch (error) {
        this.errMsg = this.resolveErrorMessage(error);
      } finally {
        this.loginLoading = false;
      }
    },
    handleGoJump(loginDef = this.currentLoginDef) {
      if (!loginDef.available) {
        this.errMsg = this.resolveErrorMessage({ message: loginDef.errorInfo });
        return;
      }
      this.loginLoading = true;
      this.requestJumpUrl(loginDef);
    },
    async goReLogin() {
      this.errMsg = '';
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
    applyCallbackQuery() {
      if (this.$route.query && this.$route.query.token) {
        this.loginCallbackData = {
          ...this.$route.query,
          completion: true
        };
        this.loginForm.account = this.loginCallbackData.account || this.loginCallbackData.sub || '';
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
      } else if (this.$route.query && this.$route.query.error) {
        this.loginCallbackData = this.$route.query;
        this.errMsg = `${this.$route.query.error}:${this.$route.query.error_description}`;
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
      this.applyCallbackQuery();
    }
  },
  created() {
    this.getGlobalSettings();
  }
};
</script>

<style lang="less" scoped>
.login {
  min-height: 100vh;
  display: flex;
  flex-direction: column;

  header {
    .width-full();
    flex: 0 0 80px;

    .login-header {
      position: relative;
      display: block;
      padding: 0 24px;
      height: 80px;
    }
  }

  .content {
    width: 100%;
    .width-full();
    flex: 1 1 auto;
    min-height: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: var(--bg-page, #c3d8e9);
    overflow: auto;
    padding: 32px 24px;

    .has-background {
      min-height: 100%;
      width: 100%;
      max-width: 1200px;
      margin: 0 auto;
      position: relative;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .dm-background {
      background: url('../../assets/loginBack.png') no-repeat 0 center;
      background-size: 580px;
    }

    .login-card {
      width: 520px;
      max-width: calc(100vw - 48px);
      background: var(--card-bg, #ffffff);
      position: relative;
      overflow: hidden;
      border: 1px solid transparent;
      border-radius: 4px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);

      :deep(.ant-tabs-tab-btn) {
        font-size: 18px;
        line-height: 24px;
        color: var(--text-primary, rgba(0, 0, 0, 0.88));
      }

      :deep(.ant-tabs) {
        .ant-tabs-bar {
          border-bottom: 2px solid #ececec;
        }

        .ant-tabs-nav {
          height: 64px;
          padding-left: 80px;
          padding-right: 80px;

          .ant-tabs-tab {
            line-height: 40px;
            font-size: 18px;
            color: var(--text-primary, rgba(0, 0, 0, 0.88));
          }

          .ant-tabs-ink-bar {
            background: #0bb9f8;
          }
        }
      }

      .tabs-content {
        padding: 20px 80px 72px;
        box-sizing: border-box;
        position: relative;
        min-height: 312px;

        .msgContent {
          position: relative;
          margin-top: 4px;
          margin-bottom: 12px;

          :deep(.ant-alert) {
            margin-top: 4px;
            text-align: left;
          }
        }

        .input-wrapper {
          & > div {
            margin-bottom: 20px;
          }

          &.is-completion {
            margin-top: 8px !important;

            & > div {
              margin-bottom: 10px;
            }
          }
        }

        .floating-field {
          --field-border: #d0d7e2;
          --field-active: #4f82d9;
          position: relative;
          display: flex;
          align-items: center;
          min-height: 44px;
          width: 100%;
          margin-bottom: 14px;
          padding: 0 14px;
          border: 1px solid var(--field-border);
          border-radius: 12px;
          background: #ffffff;
          transition:
            border-color 0.18s ease,
            box-shadow 0.18s ease;

          &:focus-within {
            border-color: var(--field-active);
            box-shadow: 0 0 0 1px rgba(79, 130, 217, 0.16);
          }

          .floating-label {
            position: absolute;
            top: -10px;
            left: 16px;
            z-index: 2;
            max-width: calc(100% - 40px);
            padding: 0 8px;
            background: #ffffff;
            color: #9aa4b2;
            font-size: 15px;
            line-height: 18px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            pointer-events: none;
          }

          &:focus-within .floating-label,
          &.has-value .floating-label {
            color: var(--field-active);
          }

          :deep(.ant-input),
          :deep(.ant-input-affix-wrapper) {
            height: 32px;
            padding: 0;
            border: 0;
            box-shadow: none;
            background: transparent;
            color: rgba(15, 23, 42, 0.92);
            font-size: 16px;
          }

          :deep(.ant-input:focus),
          :deep(.ant-input-focused),
          :deep(.ant-input-affix-wrapper:focus),
          :deep(.ant-input-affix-wrapper-focused) {
            border: 0;
            box-shadow: none;
          }

          :deep(.ant-input-affix-wrapper input) {
            font-size: 16px;
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
            border-right: 2px solid #22c55e;
            border-bottom: 2px solid #22c55e;
            transform: rotate(45deg);
          }

          :deep(.ant-input[disabled]) {
            color: rgba(15, 23, 42, 0.7);
            background: transparent;
          }
        }

        .field-error {
          margin: -6px 0 10px;
          color: #ff4d4f;
          font-size: 12px;
          line-height: 18px;
          text-align: left;
        }

        .login-submit {
          width: 100%;
          margin-top: 8px;
          margin-bottom: 20px;
          font-size: 16px;

          span {
            font-size: 16px;
          }

          &.is-mfa {
            margin-top: 46px;
          }
        }

        .completion-actions {
          display: flex;
          gap: 12px;
          margin-top: 20px;
          margin-bottom: 20px;

          .ant-btn {
            margin: 0;
          }

          .completion-submit {
            flex: 1 1 75%;
          }

          .completion-back {
            flex: 0 0 25%;
          }
        }

        .provider-login-button {
          width: 144px;
          height: 144px;
          margin: 10px auto 20px;
          padding: 18px 12px;
          border-radius: 8px;
          border: 1px solid #e5e7eb;
          background: #ffffff;
          color: rgba(0, 0, 0, 0.88);
          cursor: pointer;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          gap: 12px;
          font: inherit;
          line-height: 1.35;
          white-space: normal;
          box-shadow: 0 2px 8px rgba(15, 23, 42, 0.08);
          appearance: none;
          transition:
            border-color 0.2s ease,
            background-color 0.2s ease,
            box-shadow 0.2s ease;

          &:hover,
          &:focus-visible {
            color: #2563eb;
            border-color: #60a5fa;
            background-color: #eff6ff;
            box-shadow: 0 4px 14px rgba(37, 99, 235, 0.14);
            outline: none;
          }

          &:disabled {
            cursor: not-allowed;
            opacity: 0.65;
          }

          span {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            text-align: center;
          }
        }

        .login-provider-switcher {
          position: absolute;
          right: 76px;
          bottom: 32px;
          display: flex;
          align-items: flex-end;
          justify-content: flex-end;
          gap: 10px;
          min-height: 30px;
        }

        .login-provider-icon {
          position: relative;
          width: 26px;
          height: 26px;
          padding: 0;
          border: 0;
          background: transparent;
          cursor: pointer;
          appearance: none;

          &.active {
            cursor: default;
          }

          .current-arrow {
            position: absolute;
            top: -5px;
            left: 50%;
            width: 0;
            height: 0;
            border-left: 4px solid transparent;
            border-right: 4px solid transparent;
            border-bottom: 6px solid #4b5563;
            transform: translateX(-50%);
          }
        }
      }
    }
  }

  > footer {
    flex: 0 0 auto;
  }
}

@media (max-width: 680px) {
  .login {
    .content {
      padding: 20px 12px;

      .login-card {
        max-width: calc(100vw - 24px);

        .tabs-content {
          padding: 20px 28px 76px;

          .login-provider-switcher {
            right: 24px;
            bottom: 28px;
          }
        }

        :deep(.ant-tabs .ant-tabs-nav) {
          padding-left: 28px;
          padding-right: 28px;
        }
      }
    }
  }
}
</style>
