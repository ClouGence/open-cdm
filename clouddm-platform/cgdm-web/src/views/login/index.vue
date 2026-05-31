<template>
  <div class="login" :class="{ 'is-dark': $store.state.theme === 'dark' }" v-if="!isDesktop">
    <header>
      <div class="login-header">
        <dm-logo-header />
      </div>
    </header>
    <div class="content">
      <div :class="`has-background ${backgroundClass}`">
        <div class="tabs" :class="{ 'is-manager-login': isManagerLogin }">
          <a-tabs v-model:activeKey="loginForm.accountType" size="large" @change="handleTabChange">
            <a-tab-pane :key="ACCOUNT_TYPE.SUB_ACCOUNT" v-if="primaryUserDomainList.length && !showMfa" :tab="subAccountTabTitle"></a-tab-pane>
            <a-tab-pane :key="ACCOUNT_TYPE.PRIMARY_ACCOUNT" :tab="$t('guan-li-deng-lu')" v-if="!showMfa"></a-tab-pane>
            <a-tab-pane key="MFA" v-if="showMfa" :tab="$t('duo-yin-zi-ren-zheng-yan-zheng-ma')"></a-tab-pane>
          </a-tabs>
          <div v-if="isManagerLogin && !showMfa" class="manager-corner-badge">{{ $t('manager-login-badge') }}</div>
          <div class="tabs-content">
            <div class="input-wrapper mt-4" :class="{ 'is-completion': isCompletionMode }" v-if="!showMfa">
              <div class="login-fields" v-if="!isCompletionMode">
                <div
                  class="floating-field"
                  :class="{ 'has-value': loginForm.account }"
                  v-if="loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT || !isPrivate"
                >
                  <span class="floating-label">
                    {{ loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY ? $t('you-xiang-shou-ji-hao') : $t('you-xiang-shou-ji-hao') }}
                  </span>
                  <a-input class="field-input" v-model:value="loginForm.account" @keydown.enter="handleEnter" size="large"></a-input>
                </div>
                <div
                  class="floating-field"
                  :class="{ 'has-value': loginForm.account }"
                  v-if="isPrivate && loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT && !isJumpLogin()"
                >
                  <span class="floating-label">{{ subAccountLabel }}</span>
                  <a-input
                    class="field-input input-with-addon"
                    v-model:value="loginForm.account"
                    @focus="handlleSubAccountFocus"
                    @blur="handleSubAccountBlur"
                    @keydown.enter="handleEnter"
                    size="large"
                  >
                    <template #addonAfter>
                      <a-select
                        v-model:value="selectDomain"
                        size="large"
                        :style="`width:${domainSelectWidth}px;border-left:none; height: 100%`"
                        @change="handleDomainChange"
                        v-if="primaryUserDomainList.length > 1"
                      >
                        <a-select-option v-for="domain in primaryUserDomainList" :key="domain.domain" :value="domain.domain">
                          @{{ domain.domain }}
                        </a-select-option>
                      </a-select>
                    </template>
                  </a-input>
                </div>
              </div>
              <div class="floating-field has-value" v-if="isCompletionMode">
                <span class="floating-label">{{ $t('yong-hu-ming') }}</span>
                <a-input class="field-input" disabled v-model:value="loginCallbackData.user" size="large"></a-input>
              </div>
              <div class="floating-field" :class="{ 'has-value': loginForm.registerInfo.phone }" v-if="isCompletionMode">
                <span class="floating-label">{{ $t('shou-ji-hao') }}</span>
                <a-input class="field-input" v-model:value="loginForm.registerInfo.phone" @keydown.enter="handleEnter" size="large" />
              </div>
              <div class="floating-field" :class="{ 'has-value': loginForm.registerInfo.email }" v-if="isCompletionMode">
                <span class="floating-label">{{ $t('you-xiang') }}</span>
                <a-input class="field-input" v-model:value="loginForm.registerInfo.email" @keydown.enter="handleEnter" size="large" />
              </div>
              <div
                class="floating-field field-with-action"
                :class="{ 'has-value': loginForm.verifyCode }"
                v-if="loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY"
              >
                <span class="floating-label">{{ $t('yan-zheng-ma') }}</span>
                <a-input class="field-input" v-model:value="loginForm.verifyCode" @keydown.enter="handleEnter" size="large" />
                <cc-sms-button
                  :phone-number="loginForm.account"
                  size="large"
                  class="field-action"
                  verify-code-type="LOGIN"
                  verify-type="SMS_VERIFY_CODE"
                />
              </div>
              <div
                class="floating-field"
                :class="{ 'has-value': loginForm.password }"
                v-if="
                  !isCompletionMode &&
                  loginForm.loginType !== LOGIN_TYPE.LOGIN_VERIFY &&
                  !(isJumpLogin() && loginForm.accountType !== ACCOUNT_TYPE.PRIMARY_ACCOUNT)
                "
              >
                <span class="floating-label">{{ $t('mi-ma') }}</span>
                <a-input-password class="field-input" v-model:value="loginForm.password" @keydown.enter="handleEnter" size="large" />
              </div>
            </div>
            <div class="input-wrapper mt-4" v-if="showMfa">
              <a-input
                class="mb-4"
                @pressEnter="handleEnter2"
                @keydown.enter="handleEnter"
                v-model:value="mfaCode"
                size="large"
                :placeholder="$t('qing-shu-ru-liu-wei-shu-de-mfa-yan-zheng-ma')"
              ></a-input>
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
              v-if="!showMfa && !isCompletionMode && (!isJumpLogin() || loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT)"
              :disabled="loginLoading"
              :loading="loginLoading"
              type="primary"
              size="large"
              style="margin-top: 10px; width: 100%"
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
              style="margin-top: 46px; width: 100%"
              type="primary"
              @click="handleMfaValid"
            >
              {{ $t('yan-zheng') }}
            </a-button>
            <div class="flex items-center justify-center">
              <button
                v-if="!showMfa && isJumpLogin() && loginForm.accountType !== ACCOUNT_TYPE.PRIMARY_ACCOUNT && !loginCallbackData.token"
                :disabled="loginLoading"
                :aria-busy="loginLoading"
                type="button"
                class="provider-login-button"
                :class="{ 'is-loading': loginLoading }"
                @click="handleGoJump"
              >
                <CustomIcon :type="resolveLoginProviderIcon(selectDomainData.loginType)" size="40px" bottomMargin="16px" />
                {{ selectDomainData.title }} {{ $t('deng-lu') }}
              </button>
            </div>
            <div class="msgContent" v-if="errMsg">
              <a-alert banner type="error">
                <template #message>
                  <div v-html="errMsg"></div>
                </template>
              </a-alert>
            </div>
            <p
              v-if="primaryUserDomainList.length > 1 && isJumpLogin() && loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT"
              class="text-red-700 text-sm"
            >
              *{{ $t('dang-qian-cun-zai-duo-ge-zhu-zhang-hao-mo-ren-xuan-ze-di-yi-ge-ru-xu-bang-zhu-qing-lian-xi-guan-fang-gong-zuo-ren-yuan') }}
            </p>
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
import { ACCOUNT_TYPE, AUTH_TYPE_I18N, LOGIN_TYPE } from '@/const';
import { mapGetters, mapState, mapActions } from 'vuex';
import { UPDATE_DM_GLOBAL_SETTING, UPDATE_GLOBAL_SETTING, UPDATE_PUBLIC_KEY } from '@/store/mutationTypes';
import { encryptMixin } from '@/mixins/encryptMixin';
import { isNumber } from '@/components/util';
import { filterGlobalSettingByBuild } from '@/utils/product';
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
    isManagerLogin() {
      return this.loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT;
    },
    isCompletionMode() {
      return Boolean(this.loginCallbackData.completion && this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT);
    },
    subAccountLabel() {
      if (this.loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT) {
        return this.loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY ? this.$t('shou-ji') : this.$t('shou-ji-you-xiang-di-zhi');
      }

      return this.$t('zhang-hao');
    },
    subAccountTabTitle() {
      return this.selectDomainData.tabTitle || this.selectDomainData.title || this.$t('zhang-hao-deng-lu');
    }
  },
  data() {
    return {
      domainSelectWidth: 250,
      AUTH_TYPE_I18N,
      ACCOUNT_TYPE,
      LOGIN_TYPE,
      loginForm: {
        accountType: ACCOUNT_TYPE.SUB_ACCOUNT,
        loginType: LOGIN_TYPE.LOGIN_PASSWORD,
        account: '',
        password: '',
        verifyCode: ''
      },
      mfaCode: '',
      mfaPreActionToken: '',
      jumpLoginType: ['OIDC', 'Feishu', 'DingTalk', 'Wechat'],
      primaryUserDomainList: [],
      selectDomain: '',
      selectDomainData: {},
      isPrivate: false,
      errMsg: '',
      loginLoading: false,
      showMfa: false,
      jumpUrl: '',
      loginCallbackData: {},
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
    applyDefaultLoginFromRoute() {
      this.loginForm.accountType =
        this.$route.query && this.$route.query.defaultLogin === 'manage' ? ACCOUNT_TYPE.PRIMARY_ACCOUNT : ACCOUNT_TYPE.SUB_ACCOUNT;
    },
    async ensurePublicKeyLoaded() {
      if (this.publicKey) {
        return true;
      }

      try {
        const publicKeyRes = await this.$services.getPublicKey({ modal: false });
        if (publicKeyRes.success && publicKeyRes.data) {
          this.$store.commit(UPDATE_PUBLIC_KEY, publicKeyRes.data);
          return true;
        }
      } catch (error) {
        console.error('Failed to load public key', error);
      }

      this.errMsg = this.$t('xi-tong-yi-chang-qing-lian-xi-guan-li-yuan');
      return false;
    },
    resolveRedirectUrl() {
      if (this.defaultRedirectUrl) {
        return this.defaultRedirectUrl;
      }

      return '/sql';
    },
    async redirectToHome() {
      await this.$router.push(this.resolveRedirectUrl());
    },
    isJumpLogin(loginType = this.selectDomainData.loginType) {
      const selectedDomainMatches = this.selectDomainData && this.selectDomainData.loginType === loginType;
      return Boolean(selectedDomainMatches && this.selectDomainData.jump) || this.jumpLoginType.includes(loginType);
    },
    buildLoginAccount(currentLoginType) {
      if (this.loginForm.accountType !== ACCOUNT_TYPE.SUB_ACCOUNT) {
        return this.loginForm.account;
      }

      if (!this.selectDomain || this.loginForm.account.includes('@')) {
        return this.loginForm.account;
      }

      return `${this.loginForm.account}@${this.selectDomain}`;
    },
    async requestJumpUrl(primaryUser) {
      try {
        const res = await this.$services.requestJumpUrl({
          data: {
            primaryUid: primaryUser.domainUid,
            type: primaryUser.loginType
          },
          modal: false
        });
        if (res && res.success) {
          this.jumpUrl = res.data;
          window.location.href = this.jumpUrl;
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
        user: moreInfo.name,
        phone: moreInfo.phone,
        email: moreInfo.email
      };
      this.loginForm.registerInfo = {
        email: moreInfo.email,
        phone: moreInfo.phone,
        name: moreInfo.name,
        primaryUid: moreInfo.primaryUid
      };
    },
    filterOption(input, option) {
      return option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    handleTabChange() {
      this.errMsg = '';
      this.loginForm.loginType = LOGIN_TYPE.LOGIN_PASSWORD;
    },
    handleChangeLoginType() {
      const loginType = this.loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY ? LOGIN_TYPE.LOGIN_PASSWORD : LOGIN_TYPE.LOGIN_VERIFY;
      this.loginForm.loginType = loginType;
    },
    async handleLogin() {
      console.warn('登陆', this.loginForm);

      this.errMsg = '';
      const currentLoginType = this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT ? this.selectDomainData.loginType : this.loginForm.loginType;
      if (currentLoginType === LOGIN_TYPE.LOGIN_VERIFY || this.isJumpLogin(currentLoginType)) {
        this.loginForm.password = '';
      } else {
        if (!this.loginForm.password) {
          this.errMsg = this.$t('mi-ma-bu-neng-wei-kong');
          return;
        }

        const publicKeyReady = await this.ensurePublicKeyLoaded();
        if (!publicKeyReady) {
          return;
        }
      }
      this.loginLoading = true;
      const isCompletionLogin = this.isCompletionMode;
      const data = {
        ...this.loginForm,
        password: this.loginForm.password ? this.passwordEncrypt(this.loginForm.password) : '',
        account: this.buildLoginAccount(currentLoginType),
        loginType: this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT ? this.selectDomainData.loginType : this.loginForm.loginType,
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
    handleGoJump() {
      this.loginLoading = true;
      this.requestJumpUrl(this.selectDomainData);
    },
    resolveLoginProviderIcon(loginType) {
      const normalizedLoginType = `${loginType || ''}`.toLowerCase();
      const iconMap = {
        dingtalk: 'icon-v2-DingTalk',
        dingding: 'icon-v2-DingTalk',
        feishu: 'icon-v2-Feishu',
        wechat: 'icon-v2-Wechat',
        weixin: 'icon-v2-Wechat',
        oidc: 'icon-v2-OIDC'
      };
      return iconMap[normalizedLoginType] || `icon-v2-${loginType}`;
    },
    async goReLogin() {
      this.errMsg = '';
      this.loginCallbackData = {};
      this.loginForm.accountType = ACCOUNT_TYPE.SUB_ACCOUNT;
      this.loginForm.loginType = LOGIN_TYPE.LOGIN_PASSWORD;
      this.loginForm.account = '';
      this.loginForm.password = '';
      this.loginForm.verifyCode = '';
      this.loginForm.registerInfo = {};
      await this.$router.replace({ name: 'Login', query: {} });
    },
    handleSubAccountBlur() {
      this.domainSelectWidth = 250;
    },
    handlleSubAccountFocus() {
      this.domainSelectWidth = 150;
    },
    handleDomainChange() {
      this.primaryUserDomainList.forEach((domain) => {
        if (domain.domain === this.selectDomain) {
          this.selectDomainData = domain;
        }
      });
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
    async getGlobalSettings() {
      const res = await this.$services.getGlobalSettings({ data: {} });

      if (res.success) {
        const filteredGlobalSetting = filterGlobalSettingByBuild(res.data);
        this.globalSettings = filteredGlobalSetting;
        this.$store.commit(UPDATE_GLOBAL_SETTING, filteredGlobalSetting);
        if (filteredGlobalSetting.features.PRODUCT_CLOUD_DM) {
          const dmRes = await this.$services.dmGlobalSettings();
          if (dmRes.success) {
            this.$store.commit(UPDATE_DM_GLOBAL_SETTING, dmRes.data);
            if (dmRes.data.personal) {
              this.$i18n.global.locale.value = 'zh-CN';
              this.loginForm.account = dmRes.data.personal.account;
              this.loginForm.password = dmRes.data.personal.password;
              await this.handleLogin();
            }
          }
        }

        const icon_url = WEBSIDE_FAVICON;
        const title = 'CloudDM';
        setPageIcon(icon_url);
        document.title = title;

        // CloudDM 独立产品不支持暗色主题：强制设置为亮色并持久化
        this.$store.dispatch('setTheme', 'light');

        this.isPrivate = true;
        this.$services.primaryUserDomains().then((res1) => {
          if (res1.success && res1.data && res1.data.length) {
            this.primaryUserDomainList = res1.data;
            this.selectDomain = res1.data[0].domain;
            this.selectDomainData = res1.data[0];
            console.log('route', this.$route);
            if (this.$route.query && this.$route.query.token) {
              this.loginCallbackData = {
                ...this.$route.query,
                completion: true
              };
              const callbackPrimary = this.primaryUserDomainList.find((domain) => domain.domainUid === this.loginCallbackData.primary);
              if (callbackPrimary) {
                this.selectDomain = callbackPrimary.domain;
                this.selectDomainData = callbackPrimary;
              }
              if (this.isJumpLogin(this.selectDomainData.loginType)) {
                this.loginForm.accountType = ACCOUNT_TYPE.SUB_ACCOUNT;
                this.loginForm.account = this.loginCallbackData.account || this.loginCallbackData.sub;
                this.loginForm.registerInfo = {};
                this.loginForm.registerInfo.email = this.loginCallbackData.email;
                this.loginForm.registerInfo.phone = this.loginCallbackData.phone;
                this.loginForm.registerInfo.name = this.loginCallbackData.user;
                this.loginForm.registerInfo.primaryUid = this.selectDomainData.domainUid;
              }
            } else if (this.$route.query && this.$route.query.error && this.isJumpLogin(this.selectDomainData.loginType)) {
              this.loginForm.accountType = ACCOUNT_TYPE.SUB_ACCOUNT;
              this.loginCallbackData = this.$route.query;
              this.errMsg = `${this.$route.query.error}:${this.$route.query.error_description}`;
            }
          }
        });
      }
    }
  },
  created() {
    this.applyDefaultLoginFromRoute();
    this.ensurePublicKeyLoaded();
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

      .theme-toggle-wrapper {
        position: absolute;
        right: 24px;
        top: 50%;
        transform: translateY(-50%);

        :deep(.light) {
          .theme-icon {
            color: #333333; // 亮色模式下也使用白色图标
          }
        }
      }
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

    .rdp-background {
      background: url('../../assets/bg-rdp.png') no-repeat 0 center;
      background-size: 620px;
    }

    .cc-background {
      background: url('../../assets/logo/loginBg.png') no-repeat 0 center;
      background-size: 480px;
    }

    .dm-background {
      background: url('../../assets/loginBack.png') no-repeat 0 center;
      background-size: 580px;
    }

    .tabs {
      width: 520px;
      max-width: calc(100vw - 48px);
      background: var(--card-bg, #ffffff);
      position: relative;
      overflow: hidden;
      border: 1px solid transparent;
      border-radius: 4px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
      transition:
        border-color 0.18s ease,
        box-shadow 0.18s ease;

      &.is-manager-login {
        border-color: #ff7875;
        outline: 2px solid rgba(245, 34, 45, 0.18);
        outline-offset: 2px;
        box-shadow:
          inset 0 0 0 1px rgba(245, 34, 45, 0.18),
          0 0 0 4px rgba(245, 34, 45, 0.08),
          0 2px 12px rgba(120, 38, 32, 0.08);
      }

      .manager-corner-badge {
        position: absolute;
        right: -42px;
        bottom: 18px;
        z-index: 3;
        width: 150px;
        height: 28px;
        transform: rotate(-38deg);
        transform-origin: center;
        background: #fff1f0;
        border-top: 1px solid #f2b8b5;
        border-bottom: 1px solid #f2b8b5;
        color: #b42318;
        font-size: 12px;
        font-weight: 600;
        line-height: 26px;
        letter-spacing: 0;
        text-align: center;
        pointer-events: none;
        text-transform: uppercase;
      }

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

          .ant-tabs-tab-active {
            font-weight: 400;
          }
        }
      }

      .tabs-content {
        padding: 20px 80px 48px;
        box-sizing: border-box;
        position: relative;
        min-height: 258px;

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

        .login-fields {
          margin-bottom: 0 !important;
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
            transition: color 0.18s ease;
          }

          &:focus-within .floating-label,
          &.has-value .floating-label {
            color: var(--field-active);
          }

          :deep(.ant-input),
          :deep(.ant-input-affix-wrapper),
          :deep(.ant-input-group-wrapper),
          :deep(.ant-input-wrapper),
          :deep(.ant-input-group),
          :deep(.ant-input-group-addon) {
            border: 0;
            box-shadow: none;
            background: transparent;
          }

          :deep(.ant-input),
          :deep(.ant-input-affix-wrapper) {
            height: 32px;
            padding: 0;
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

          :deep(.ant-input[disabled]) {
            color: rgba(15, 23, 42, 0.7);
            background: transparent;
          }

          :deep(.ant-input-group-addon) {
            padding: 0;
          }

          :deep(.ant-select-selector) {
            height: 32px !important;
            border: 0 !important;
            box-shadow: none !important;
            background: transparent !important;
          }

          :deep(.ant-select-selection-item) {
            line-height: 32px !important;
            color: #4f82d9;
            font-size: 15px;
          }
        }

        .field-with-action {
          padding-right: 0;

          .field-input {
            flex: 1 1 auto;
            min-width: 0;
          }

          .field-action {
            flex: 0 0 auto;
            width: auto !important;
            min-width: 96px;
            height: 40px;
            margin: 0 !important;
            padding: 0 20px;
            border: 0;
            background: transparent;
            color: #4f82d9;
            box-shadow: none;
            font-size: 15px;
          }
        }

        .ant-btn {
          width: 100%;
          margin-top: 8px;
          margin-bottom: 20px;
          font-size: 16px;

          span {
            font-size: 16px;
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
          margin-top: 10px;
          margin-bottom: 20px;
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
          gap: 0;
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

          &:active {
            color: #2563eb;
            border-color: #60a5fa;
            background-color: #eff6ff;
            box-shadow: 0 4px 14px rgba(37, 99, 235, 0.14);
            transform: none;
          }

          &:disabled {
            cursor: not-allowed;
            opacity: 0.65;
          }

          &.is-loading {
            pointer-events: none;
          }

          span {
            display: inline-flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            text-align: center;
          }
        }
      }

      .login-operation {
        display: flex;
        justify-content: space-between;
        font-size: 14px;
        //margin-top: 20px;

        div:last-child,
        div > span {
          cursor: pointer;
        }

        div,
        span {
          font-size: 14px;
        }

        span {
          color: @primary-color;
        }
      }
    }
  }

  > footer {
    flex: 0 0 auto;
  }

  // 暗色模式覆盖
  &.is-dark {
    .content {
      background-color: var(--bg-radio) !important;

      .tabs {
        background: var(--card-bg, #141414);
        box-shadow: 0 6px 20px rgba(0, 0, 0, 0.35);

        :deep(.ant-tabs-nav .ant-tabs-tab) {
          color: var(--text-primary, rgba(255, 255, 255, 0.85));
        }

        :deep(.ant-tabs-ink-bar) {
          background: #0bb9f8;
        }
      }
    }

    // a-input 暗色占位符与文字颜色
    :deep(.ant-input) {
      color: var(--text-primary);
    }

    :deep(.ant-input::placeholder) {
      color: var(--text-primary);
    }
  }

  .tooltip {
    position: relative;
    width: 105px;
    height: 30px;
    border: 1px solid #ffc441;
    color: #ff6e0d;
    background-color: #fff7db;
    margin-right: 12px;
    line-height: 30px;
    text-align: center;
  }

  .tooltip:before {
    content: '';
    display: block;
    position: absolute;
    right: -10px;
    top: 5px;
    border-top: 10px solid transparent;
    border-bottom: 10px solid transparent;
    border-left: 10px solid #ffc441;
  }

  .tooltip:after {
    content: '';
    display: block;
    position: absolute;
    right: -9px;
    top: 5px;
    border-top: 10px solid transparent;
    border-bottom: 10px solid transparent;
    border-left: 10px solid #fff7db;
  }

  .footer {
    .width-full();
    flex: 0 0 auto;
    //background-color: var(--bg-radio) !important;
  }
}

@media (max-width: 900px) {
  .login {
    .content {
      .rdp-background,
      .cc-background,
      .dm-background {
        background-image: none;
      }
    }
  }
}

@media (max-width: 560px) {
  .login {
    .content {
      padding: 24px 16px;

      .tabs {
        max-width: calc(100vw - 32px);

        :deep(.ant-tabs) {
          .ant-tabs-nav {
            padding-left: 24px;
            padding-right: 24px;
          }
        }

        .tabs-content {
          padding-right: 24px;
          padding-left: 24px;
        }
      }
    }
  }
}

.input-with-addon {
  :deep(.ant-input) {
    height: 3rem !important;
  }
}
</style>
