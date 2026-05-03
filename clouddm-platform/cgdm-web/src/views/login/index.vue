<template>
  <div class="login" :class="{ 'is-dark': $store.state.theme === 'dark' }" v-if="!isDesktop">
    <header>
      <div class="login-header">
        <dm-logo-header />
      </div>
    </header>
    <div class="content">
      <div :class="`has-background ${backgroundClass}`">
        <div class="tabs">
          <a-tabs v-model:activeKey="loginForm.accountType" size="large" @change="handleTabChange">
            <a-tab-pane
              :key="ACCOUNT_TYPE.PRIMARY_ACCOUNT"
              :tab="$t('zhu-zhang-hao-deng-lu')"
              v-if="!showMfa"
              :disabled="jumpLoginType.includes(selectDomainData.loginType) && (loginCallbackData.token || loginCallbackData.error)"
            ></a-tab-pane>
            <a-tab-pane
              :key="ACCOUNT_TYPE.SUB_ACCOUNT"
              v-if="primaryUserDomainList.length && !showMfa"
              :tab="$t('zi-zhang-hao-deng-lu')"
            ></a-tab-pane>
            <a-tab-pane key="MFA" v-if="showMfa" :tab="$t('duo-yin-zi-ren-zheng-yan-zheng-ma')"></a-tab-pane>
          </a-tabs>
          <div class="tabs-content">
            <div class="input-wrapper mt-4" v-if="!showMfa">
              <div style="display: flex; align-items: center">
                <a-input
                  class="h-12"
                  v-model:value="loginForm.account"
                  @keydown.enter="handleEnter"
                  v-if="loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT || !isPrivate"
                  :placeholder="loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY ? $t('you-xiang-shou-ji-hao') : $t('you-xiang-shou-ji-hao')"
                  size="large"
                ></a-input>
                <a-input
                  class="input-with-addon"
                  v-model:value="loginForm.account"
                  @focus="handlleSubAccountFocus"
                  @blur="handleSubAccountBlur"
                  @keydown.enter="handleEnter"
                  v-if="isPrivate && loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT && !jumpLoginType.includes(selectDomainData.loginType)"
                  :placeholder="
                    loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT
                      ? loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY
                        ? $t('shou-ji')
                        : $t('shou-ji-you-xiang-di-zhi')
                      : selectDomain
                        ? selectDomainData.title
                        : $t('zi-zhang-hao')
                  "
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
                    <div
                      :style="`width:${domainSelectWidth}px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap; height: 100%`"
                      v-if="
                        primaryUserDomainList.length <= 1 &&
                        !(selectDomainData.loginType === LOGIN_TYPE.LOGIN_AD || selectDomainData.loginType === LOGIN_TYPE.LOGIN_LDAP)
                      "
                    >
                      @{{ selectDomain }}
                    </div>
                  </template>
                </a-input>
                <cc-sms-button
                  v-if="loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY"
                  :phone-number="loginForm.account"
                  size="large"
                  style="width: 120px; margin: 0 0 0 10px"
                  verify-code-type="LOGIN"
                  verify-type="SMS_VERIFY_CODE"
                />
              </div>
              <a-input class="mb-6 h-12" disabled v-model:value="loginCallbackData.user" v-if="loginCallbackData.token" size="large"></a-input>
              <a-input
                class="mb-6 h-12"
                v-if="loginCallbackData.token && loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT"
                v-model:value="loginForm.registerInfo.phone"
                :placeholder="$t('shou-ji-hao')"
                @keydown.enter="handleEnter"
                size="large"
              />
              <a-input
                class="h-12"
                v-if="loginCallbackData.token && loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT"
                v-model:value="loginForm.registerInfo.email"
                :placeholder="$t('you-xiang')"
                @keydown.enter="handleEnter"
                size="large"
              />
              <a-input
                class="h-12"
                v-if="loginForm.loginType === LOGIN_TYPE.LOGIN_VERIFY"
                v-model:value="loginForm.verifyCode"
                @keydown.enter="handleEnter"
                :placeholder="$t('yan-zheng-ma')"
                size="large"
              />
              <cc-password-input
                v-if="
                  loginForm.loginType !== LOGIN_TYPE.LOGIN_VERIFY &&
                  !(jumpLoginType.includes(selectDomainData.loginType) && loginForm.accountType !== ACCOUNT_TYPE.PRIMARY_ACCOUNT)
                "
                v-model:value="loginForm.password"
                :handleEnter="handleEnter"
                :placeholder="$t('mi-ma')"
                size="large"
              />
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
            <a-button
              v-if="
                !showMfa &&
                (!jumpLoginType.includes(selectDomainData.loginType) ||
                  loginForm.accountType === ACCOUNT_TYPE.PRIMARY_ACCOUNT ||
                  loginCallbackData.token)
              "
              :disabled="loginLoading"
              :loading="loginLoading"
              type="primary"
              size="large"
              style="margin-top: 20px; width: 100%"
              @click="handleLogin"
            >
              {{ jumpLoginType.includes(selectDomainData.loginType) && loginCallbackData.token ? $t('bu-quan-xin-xi-bing-deng-lu') : $t('deng-lu') }}
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
            <div class="msgContent" v-if="errMsg">
              <a-alert banner type="error">
                <template #message>
                  <div v-html="errMsg"></div>
                </template>
              </a-alert>
            </div>
            <div class="flex items-center justify-center">
              <a-button
                v-if="
                  !showMfa &&
                  jumpLoginType.includes(selectDomainData.loginType) &&
                  loginForm.accountType !== ACCOUNT_TYPE.PRIMARY_ACCOUNT &&
                  !loginCallbackData.token
                "
                :disabled="loginLoading"
                :loading="loginLoading"
                size="large"
                style="margin-top: 20px; width: 108px; border-radius: 4px"
                class="w-36 h-36 border border-zinc-200 rounded-lg block flex flex-col items-center justify-center shadow"
                @click="handleGoJump"
              >
                <svg class="icon-v2 mb-4 text-4xl" aria-hidden="true">
                  <use :xlink:href="`#icon-v2-${selectDomainData.loginType}`"></use>
                </svg>
                {{ selectDomainData.title }} {{ $t('deng-lu') }}
              </a-button>
            </div>
            <p
              v-if="
                primaryUserDomainList.length > 1 &&
                jumpLoginType.includes(selectDomainData.loginType) &&
                loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT
              "
              class="text-red-700 text-sm"
            >
              *{{ $t('dang-qian-cun-zai-duo-ge-zhu-zhang-hao-mo-ren-xuan-ze-di-yi-ge-ru-xu-bang-zhu-qing-lian-xi-guan-fang-gong-zuo-ren-yuan') }}
            </p>
            <!--            <a-button v-if="jumpLoginType.includes(selectDomainData.loginType) && loginForm.accountType !== ACCOUNT_TYPE.PRIMARY_ACCOUNT && !loginCallbackData.token" :disabled="loginLoading" :loading="loginLoading" size="large"-->
            <!--                      style="margin-top: 20px;width: 100%;"-->
            <!--                      @click="handleGoJump"-->
            <!--            >-->
            <!--              <CustomIcon :type="`icon-v2-${selectDomainData.loginType}`" hoverStyle size="14px" />-->
            <!--              {{ selectDomainData.title }} {{$t('deng-lu')}}-->
            <!--            </a-button>-->
            <div class="login-operation">
              <div v-if="globalSettings.features.ENABLE_REGISTER">
                <!--                {{ $t('mei-you-zhang-hao-qu') }}<span style="margin-left: 4px" @click="goRegister">{{ $t('zhu-ce') }}</span>-->
              </div>
              <!--              <div v-if="globalSettings.deployMode !== 'CLOUD_PRIVATE'" @click="goResetPassword">{{ $t('wang-ji-mi-ma') }}</div>-->
              <a
                class="absolute right-[80px]"
                v-if="jumpLoginType.includes(selectDomainData.loginType) && loginCallbackData.token"
                @click="goReLogin"
              >
                {{ $t('chong-xin-deng-lu') }}
              </a>
            </div>
          </div>
          <Modal v-model:visible="showAccountInformationCompletionModal" :title="$t('zhang-hao-xin-xi-bu-quan')" :mask-closable="false" :width="400">
            <Alert>{{ $t('shou-ci-deng-lu-xu-yao-tian-xieewai-xin-xi') }}</Alert>
            <Form :model="completeForm" :rules="rules" ref="completeModalRef" :label-width="60">
              <FormItem :label="$t('you-xiang')" prop="email">
                <Input v-model:value="completeForm.email" />
              </FormItem>
              <FormItem :label="$t('shou-ji-hao')" prop="phone">
                <Input v-model:value="completeForm.phone" />
              </FormItem>
            </Form>
            <template #footer>
              <Button @click="hideCompleteModal">{{ $t('qu-xiao') }}</Button>
              <Button type="primary" @click="handleComplete">{{ $t('bao-cun') }}</Button>
            </template>
          </Modal>
        </div>
      </div>
    </div>
    <footer>
      <dm-footer />
    </footer>
  </div>
</template>

<script>
import cloneDeep from 'lodash.clonedeep';
import DmFooter from '@/components/DmFooter';
import DmLogoHeader from '@/components/DmLogoHeader';
import { ACCOUNT_TYPE, AUTH_TYPE_I18N, LOGIN_TYPE } from '@/const';
import { mapGetters, mapState, mapActions } from 'vuex';
import { UPDATE_DM_GLOBAL_SETTING, UPDATE_GLOBAL_SETTING } from '@/store/mutationTypes';
import { encryptMixin } from '@/mixins/encryptMixin';
import { isNumber } from '@/components/util';
import { filterGlobalSettingByBuild } from '@/utils/product';
import formatError from '@/services/formatError';

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
    }
  },
  data() {
    const validateSubaccount = async (checkType, checkContent, callback) => {
      if (checkContent === this.preCompleteForm[checkType.toLowerCase()]) {
        callback();
      } else {
        const res = await this.$services.checkSupplement({
          data: {
            checkType,
            checkContent,
            primaryUid: this.preCompleteForm.primaryUid
          },
          modal: false
        });
        if (res.success) {
          callback();
        } else {
          callback(new Error(res.msgContent));
        }
      }
    };
    const validateSubaccountPhone = (rule, value, callback) => {
      validateSubaccount('PHONE', value, callback);
    };
    const validateSubaccountEmail = (rule, value, callback) => {
      validateSubaccount('EMAIL', value, callback);
    };
    return {
      domainSelectWidth: 250,
      AUTH_TYPE_I18N,
      showAccountInformationCompletionModal: false,
      ACCOUNT_TYPE,
      LOGIN_TYPE,
      loginedForm: {},
      loginForm: {
        accountType: ACCOUNT_TYPE.PRIMARY_ACCOUNT,
        loginType: LOGIN_TYPE.LOGIN_PASSWORD,
        account: '',
        password: '',
        verifyCode: ''
      },
      mfaCode: '',
      mfaPreActionToken: '',
      completeForm: {
        email: '',
        phone: ''
      },
      jumpLoginType: ['OIDC', 'Feishu', 'DingTalk', 'Wechat'],
      primaryUserDomainList: [],
      selectDomain: '',
      selectDomainData: {},
      isPrivate: false,
      preCompleteForm: {},
      errMsg: '',
      loginLoading: false,
      showMfa: false,
      jumpUrl: '',
      loginCallbackData: {},
      globalSettings: {
        features: {}
      },
      rules: {
        email: [
          { required: true, trigger: 'blur', message: this.$t('you-xiang-bu-neng-wei-kong') },
          { validator: validateSubaccountEmail, trigger: 'blur' }
        ],
        phone: [
          { required: true, trigger: 'blur', message: this.$t('shou-ji-hao-bu-neng-wei-kong') },
          { validator: validateSubaccountPhone, trigger: 'blur' }
        ]
      }
    };
  },
  beforeUnmount() {
    this.loginLoading = false;
  },
  methods: {
    ...mapActions(['getUserInfo']),
    resolveRedirectUrl() {
      if (this.defaultRedirectUrl) {
        return this.defaultRedirectUrl;
      }

      return '/sql';
    },
    async redirectToHome() {
      await this.$router.push(this.resolveRedirectUrl());
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
    async handleComplete() {
      this.$refs.completeModalRef.validate(async (valid) => {
        if (valid) {
          try {
            const res = await this.$services.login({
              data: {
                ...this.loginedForm,
                registerInfo: this.completeForm,
                loginType: this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT ? this.selectDomainData.loginType : this.loginForm.loginType
              },
              modal: false
            });
            this.hideCompleteModal();
            if (res.success) {
              await this.getUserInfo();
              await this.redirectToHome();
            } else {
              this.errMsg = this.resolveErrorMessage(res);
            }
          } catch (error) {
            this.errMsg = this.resolveErrorMessage(error);
          }
        }
      });
    },
    hideCompleteModal() {
      this.showAccountInformationCompletionModal = false;
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
      if (currentLoginType === LOGIN_TYPE.LOGIN_VERIFY || this.jumpLoginType.includes(currentLoginType)) {
        this.loginForm.password = '';
      } else {
        if (!this.loginForm.password) {
          this.errMsg = this.$t('mi-ma-bu-neng-wei-kong');
          return;
        }
      }
      this.loginLoading = true;
      const data = {
        ...this.loginForm,
        password: this.loginForm.password ? this.passwordEncrypt(this.loginForm.password) : '',
        account:
          this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT && this.loginForm.loginType !== LOGIN_TYPE.OIDC
            ? `${this.loginForm.account}@${this.selectDomain}`
            : this.loginForm.account,
        loginType: this.loginForm.accountType === ACCOUNT_TYPE.SUB_ACCOUNT ? this.selectDomainData.loginType : this.loginForm.loginType,
        token: this.loginCallbackData.token
      };
      this.loginedForm = data;
      try {
        const res = await this.$services.login({
          data,
          modal: false
        });
        if (res.success) {
          this.errMsg = '';
          if (res.data.needMore) {
            this.showAccountInformationCompletionModal = true;
            this.completeForm = res.data.moreInfo;
            this.preCompleteForm = cloneDeep(res.data.moreInfo);
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
    goReLogin() {
      this.$router.push({ name: 'Login' });
      window.location.reload();
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

        const icon_url = '/dm.ico';
        const title = 'CloudDM';
        const link = document.querySelector("link[rel*='icon']") || document.createElement('link');
        link.type = 'image/x-icon';
        link.rel = 'shortcut icon';
        link.href = icon_url;
        document.getElementsByTagName('head')[0].appendChild(link);
        document.title = title;

        // CloudDM 独立产品不支持暗色主题：强制设置为亮色并持久化
        this.$store.dispatch('setTheme', 'light');

        this.isPrivate = true;
        this.$services.primaryUserDomains().then((res1) => {
          if (res1.success && res1.data && res1.data.length) {
            this.primaryUserDomainList = res1.data;
            this.selectDomain = res1.data[0].domain;
            this.selectDomainData = res1.data[0];
            if (this.jumpLoginType.includes(this.selectDomainData.loginType)) {
              console.log('route', this.$route);
              if (this.$route.query && this.$route.query.token) {
                this.loginCallbackData = this.$route.query;
                this.loginForm.accountType = ACCOUNT_TYPE.SUB_ACCOUNT;
                this.loginForm.account = this.loginCallbackData.account;
                this.loginForm.registerInfo = {};
                this.loginForm.registerInfo.email = this.loginCallbackData.email;
                this.loginForm.registerInfo.phone = this.loginCallbackData.phone;
                this.loginForm.registerInfo.name = this.loginCallbackData.user;
                this.loginForm.registerInfo.primaryUid = this.selectDomainData.domainUid;
              } else if (this.$route.query && this.$route.query.error) {
                this.loginForm.accountType = ACCOUNT_TYPE.SUB_ACCOUNT;
                this.loginCallbackData = this.$route.query;
                this.errMsg = `${this.$route.query.error}:${this.$route.query.error_description}`;
              }
            }
          }
        });
      }
    }
  },
  created() {
    this.getGlobalSettings();
  }
};
</script>

<style lang="less" scoped>
.login {
  header {
    .width-full();

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
    position: absolute;
    top: 80px;
    //bottom: 56px;
    background-color: var(--bg-page, #c3d8e9);
    overflow: auto;
    height: 545px;
    //padding: 0 105px;

    .has-background {
      //width: 100%;
      height: 100%;
      width: 1200px;
      margin: 0 auto;
      position: relative;
    }

    .rdp-background {
      background: url('../../assets/bg-rdp.png') no-repeat 0 80px;
      background-size: 620px;
    }

    .cc-background {
      background: url('../../assets/logo/loginBg.png') no-repeat 0 60px;
      background-size: 480px;
    }

    .dm-background {
      background: url('../../assets/loginBack.png') no-repeat 0 60px;
      background-size: 580px;
    }

    .tabs {
      width: 520px;
      margin-top: 60px;
      background: var(--card-bg, #ffffff);
      float: right;
      border-radius: 4px;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);

      :deep(.ant-tabs-tab-btn) {
        font-size: 16px;
        color: var(--text-primary, rgba(0, 0, 0, 0.88));
      }

      :deep(.ant-tabs) {
        .ant-tabs-bar {
          border-bottom: 2px solid #ececec;
        }

        .ant-tabs-nav {
          height: 64px;
          padding-left: 82px;

          .ant-tabs-tab {
            line-height: 48px;
            font-size: 16px;
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
        padding: 20px 80px 48px 80px;
        position: relative;
        min-height: 258px;

        .msgContent {
          position: relative;
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
        }

        .ant-btn {
          width: 360px;
          margin-top: 8px;
          margin-bottom: 20px;
          font-size: 16px;

          span {
            font-size: 16px;
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
    //position: absolute;
    //bottom: 0;
    margin-top: 542px;
    //background-color: var(--bg-radio) !important;
  }
}

.input-with-addon {
  :deep(.ant-input) {
    height: 3rem !important;
  }
}
</style>
