<template>
  <div class="profile-page user-center">
    <nav class="user-tabs">
      <span class="user-tabs__item" :class="{ 'is-active': activeTab === 'profile' }" @click="handleTabClick('profile')">
        {{ $t('zhang-hu-xin-xi') }}
      </span>
      <span class="user-tabs__item" :class="{ 'is-active': activeTab === 'security' }" @click="handleTabClick('security')">
        {{ $t('an-quan') }}
      </span>
    </nav>
    <div class="profile-page__body">
      <div v-show="activeTab === 'profile'">
        <div>
          <dl class="profile-field-list">
            <div v-if="userInfo.account" class="profile-field-row">
              <dt>{{ $t('zhang-hao') }}</dt>
              <dd>{{ userInfo.account }}</dd>
            </div>
            <div v-if="!isInternalUser" class="profile-field-row">
              <dt>{{ $t('lai-yuan') }}</dt>
              <dd>{{ userInfo.bindType }}</dd>
            </div>
            <div v-if="!isInternalUser" class="profile-field-row">
              <dt>{{ $t('guan-lian-zhang-hao') }}</dt>
              <dd>
                <span :class="{ 'empty-value': !userInfo.bindAccount }">
                  {{ userInfo.bindAccount || $t('initialization.emptyValue') }}
                </span>
              </dd>
            </div>
            <div class="profile-field-row">
              <dt>{{ $t('yong-hu-ming') }}</dt>
              <dd>
                <template v-if="editingField === 'username'">
                  <div class="inline-edit-row">
                    <Input v-model="inlineForm.username" @on-enter="confirmInlineEdit('username')" />
                    <Button type="primary" :loading="loading" @click="confirmInlineEdit('username')">{{ $t('que-ding') }}</Button>
                    <Button @click="cancelInlineEdit">{{ $t('qu-xiao') }}</Button>
                  </div>
                </template>
                <template v-else>
                  {{ userInfo.username }}
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="startInlineEdit('username')" />
                </template>
                <span
                  v-if="userInfo.saasUserStatus === 'SAAS_LOCKED'"
                  class="border border-red-700 border-solid ml-2 text-red-700 bg-red-50 rounded-md px-4 py-2 text-sm font-medium"
                >
                  {{ $t('yi-suo-ding') }}
                </span>
              </dd>
            </div>
            <div class="profile-field-row">
              <dt>{{ $t('shou-ji') }}</dt>
              <dd>
                <template v-if="editingField === 'phone'">
                  <div class="inline-edit-row">
                    <Input v-model="inlineForm.phone" @on-blur="checkInlineDuplicate('phone')" @on-enter="confirmInlineEdit('phone')" />
                    <Icon v-if="duplicateState.phone === 'ok'" type="md-checkmark" class="text-green-500 text-xl" />
                    <Button type="primary" :loading="loading" @click="confirmInlineEdit('phone')">{{ $t('que-ding') }}</Button>
                    <Button @click="cancelInlineEdit">{{ $t('qu-xiao') }}</Button>
                  </div>
                  <div v-if="duplicateError.phone" class="inline-edit-error">{{ duplicateError.phone }}</div>
                </template>
                <template v-else>
                  <span :class="{ 'empty-value': !userInfo.phone }">{{ userInfo.phone || $t('initialization.emptyValue') }}</span>
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="startInlineEdit('phone')" />
                </template>
              </dd>
            </div>
            <div class="profile-field-row">
              <dt>{{ $t('you-xiang') }}</dt>
              <dd>
                <template v-if="editingField === 'email'">
                  <div class="inline-edit-row">
                    <Input v-model="inlineForm.email" @on-blur="checkInlineDuplicate('email')" @on-enter="confirmInlineEdit('email')" />
                    <Icon v-if="duplicateState.email === 'ok'" type="md-checkmark" class="text-green-500 text-xl" />
                    <Button type="primary" :loading="loading" @click="confirmInlineEdit('email')">{{ $t('que-ding') }}</Button>
                    <Button @click="cancelInlineEdit">{{ $t('qu-xiao') }}</Button>
                  </div>
                  <div v-if="duplicateError.email" class="inline-edit-error">{{ duplicateError.email }}</div>
                </template>
                <template v-else>
                  <span :class="{ 'empty-value': !userInfo.email }">{{ userInfo.email || $t('initialization.emptyValue') }}</span>
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="startInlineEdit('email')" />
                </template>
              </dd>
            </div>
            <div class="profile-field-row">
              <dt>{{ $t('deng-lu-mi-ma') }}</dt>
              <dd>
                <template v-if="editingField === 'password'">
                  <div class="inline-edit-row password-edit-row">
                    <Input v-model="inlineForm.originPassword" type="password" password :placeholder="$t('qing-shu-ru-lao-mi-ma')" />
                    <Poptip trigger="focus" placement="right-start">
                      <Input v-model="inlineForm.newPassword" type="password" password :placeholder="$t('xin-mi-ma')" />
                      <template #content>
                        <p>{{ passwordRule.tips }}</p>
                      </template>
                    </Poptip>
                    <Button type="primary" :loading="loading" @click="confirmInlineEdit('password')">{{ $t('que-ding') }}</Button>
                    <Button @click="cancelInlineEdit">{{ $t('qu-xiao') }}</Button>
                  </div>
                  <div v-if="errMsg" class="inline-edit-error">{{ errMsg }}</div>
                </template>
                <template v-else>
                  *******
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="startInlineEdit('password')" />
                </template>
              </dd>
            </div>
            <div v-if="userInfo.marketplaceType && userInfo.marketplaceType !== 'NONE'" class="profile-field-row">
              <dt>{{ $t('yun-shi-chang-lei-xing') }}</dt>
              <dd>
                {{ userInfo.marketplaceType === 'NONE' ? '' : userInfo.marketplaceType }}
              </dd>
            </div>
          </dl>
        </div>
      </div>
      <div v-show="activeTab === 'security'">
        <div>
          <div class="profile-security-block">
            <h3 class="profile-section-title">
              {{ $t('duo-yin-zi-ren-zheng') }}
              <span v-if="!userInfo.useMfa">{{ $t('wei-kai-qi') }}</span>
              <span v-else-if="mfaInvalid">{{ $t('yi-shi-xiao') }}</span>
              <span v-else>{{ $t('yi-kai-qi') }}</span>
            </h3>
            <div>
              <Button v-if="!userInfo.useMfa || mfaInvalid" style="margin-right: 12px" @click="handleOpenMfaSetting">
                {{ mfaInvalid ? $t('chong-xin-qi-yong') : $t('kai-qi-1') }}
              </Button>
              <Button @click="handleResetMfa" v-if="userInfo.useMfa && !mfaInvalid" type="default" style="margin-right: 12px">
                {{ $t('chong-zhi') }}
              </Button>
              <Button @click="handleShowCloseMf" v-if="userInfo.useMfa" type="error" ghost>{{ $t('guan-bi') }}</Button>
            </div>
            <p class="profile-section-desc">
              {{
                $t(
                  'wei-nin-de-zhang-hao-zeng-jia-yi-ceng-bao-zhang-kai-qi-hou-nin-xu-yao-zai-deng-lu-shi-ti-gong-zhang-hao-mi-ma-yi-ji-mfa-yan-zheng-ma'
                )
              }}
            </p>
          </div>
          <div v-if="userInfo.accountType === 'PRIMARY_ACCOUNT'" class="profile-security-block">
            <h3 class="profile-section-title">
              {{ $t('aksk-guan-li') }}
            </h3>
            <div style="margin-bottom: 8px">
              <Button style="font-size: 14px; margin-right: 16px" @click="handleShowFetchAKSK">
                {{ $t('huo-qu-aksk') }}
              </Button>
              <Button type="error" ghost style="font-size: 14px; margin-right: 16px" @click="handleShowResetAKSK">
                {{ $t('chong-zhi-aksk') }}
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <CCModal v-model="showMfaModal" :title="$t('duo-yin-zi-ren-zheng')" width="480px">
      <div v-if="mfaModalLoading" style="text-align: center; padding: 40px 0">
        <i class="ivu-icon ivu-icon-ios-loading ivu-load-loop" style="font-size: 32px"></i>
      </div>
      <div v-else class="mfa-modal-body">
        <div v-if="mfaStep === 1">
          <Form label-position="top">
            <FormItem :label="$t('mfa-zhang-hao')">
              <RadioGroup v-model="mfaAccountType" vertical>
                <Radio v-for="item in mfaAccountOptions" :key="item.value" :label="item.value" :disabled="item.disabled">
                  <span>{{ item.label }}</span>
                  <span class="ml-2 text-gray-500">{{ item.account || $t('wei-she-zhi-0') }}</span>
                </Radio>
              </RadioGroup>
            </FormItem>
          </Form>
          <p class="mt-4 mb-8">
            {{
              $t(
                'wei-nin-de-zhang-hao-zeng-jia-yi-ceng-bao-zhang-kai-qi-hou-nin-xu-yao-zai-deng-lu-shi-ti-gong-zhang-hao-mi-ma-yi-ji-mfa-yan-zheng-ma'
              )
            }}
          </p>
        </div>
        <div v-else>
          <p class="mfa-bind-instruction" :class="{ expired: mfaSettingExpired }">
            {{ mfaBindInstruction }}
          </p>
          <Tabs v-model="mfaCodeTab" :animated="false" class="mfa-code-tabs">
            <TabPane :label="$t('mfa-er-wei-ma')" name="qr">
              <div class="mfa-qr-panel">
                <div class="mfa-qr-wrapper">
                  <img :src="mfaQrCode" class="mfa-qr-code" v-if="mfaQrCode" />
                  <div v-if="mfaSettingExpired" class="mfa-expired-mask">
                    <strong>{{ $t('yi-shi-xiao') }}</strong>
                    <span>{{ $t('qing-zhong-xin-sheng-cheng') }}</span>
                  </div>
                </div>
                <div class="mfa-authenticator-links">
                  <a href="https://support.google.com/accounts/answer/1066447" target="_blank" rel="noopener noreferrer">
                    {{ $t('google-authenticator') }}
                  </a>
                  <a href="https://support.microsoft.com/authenticator/download-microsoft-authenticator" target="_blank" rel="noopener noreferrer">
                    {{ $t('microsoft-authenticator') }}
                  </a>
                </div>
              </div>
            </TabPane>
            <TabPane :label="$t('mfa-code')" name="code">
              <div class="mfa-secret-panel">
                <div class="mfa-secret-code">
                  <div v-for="(row, rowIndex) in mfaSecretCodeRows" :key="rowIndex" class="mfa-secret-code-row">
                    <span v-for="group in row" :key="group">{{ group }}</span>
                  </div>
                </div>
                <div class="mfa-secret-actions">
                  <Button type="primary" ghost @click="handleCopy(mfaSecretCode)">{{ $t('fu-zhi') }}</Button>
                  <Button type="primary" ghost @click="handleDownloadMfaCode">{{ $t('xia-zai') }}</Button>
                </div>
                <div class="mfa-authenticator-links">
                  <a href="https://support.google.com/accounts/answer/1066447" target="_blank" rel="noopener noreferrer">
                    {{ $t('google-authenticator') }}
                  </a>
                  <a href="https://support.microsoft.com/authenticator/download-microsoft-authenticator" target="_blank" rel="noopener noreferrer">
                    {{ $t('microsoft-authenticator') }}
                  </a>
                </div>
              </div>
            </TabPane>
          </Tabs>
          <div class="mfa-verify-divider"></div>
          <Input v-model="mfaInput" :placeholder="$t('qing-shu-ru-yi-ci-xing-de-duo-yin-zi-ren-zheng-ma')" />
        </div>
        <Alert class="mt-4" type="error" v-if="errMsg" show-icon>{{ errMsg }}</Alert>
      </div>
      <template #footer>
        <Button @click="handleCloseMfaModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmMfaModal">{{ mfaStep === 1 ? $t('sheng-cheng-mfa-code') : $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showAKSK" title="AK/SK" width="640px" footer-hide>
      <h3 style="margin-bottom: 20px">
        {{ $t('wei-bao-zheng-nin-de-zhang-hao-an-quan-qing-wu-bi-bao-guan-hao-nin-de-aksk') }}
      </h3>
      <Form label-position="right" :label-width="100">
        <FormItem :label="$t('ak-0')">
          <p>{{ akskInfo.accessKey }}</p>
        </FormItem>
        <FormItem :label="$t('sk-0')">
          <p>{{ akskInfo.secretKey }}</p>
        </FormItem>
      </Form>
      <template #footer>
        <Button type="primary" @click="handleCopy(JSON.stringify(akskInfo))">
          {{ $t('fu-zhi') }}
        </Button>
        <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { isNumber } from '@/components/util';
import { mapGetters, mapMutations, mapState } from 'vuex';
import { encryptMixin } from '@/mixins/encryptMixin';
import { UPDATE_USERINFO } from '@/store/mutationTypes';

const DEFAULT_PASSWORD_MIN_LENGTH = 8;
const MFA_SETTING_EXPIRE_MS = 10 * 60 * 1000;

export default {
  mixins: [encryptMixin],
  data() {
    return {
      loading: false,
      showAKSK: false,
      akskInfo: {},
      passwordRule: {
        strongPolicy: false,
        minLength: DEFAULT_PASSWORD_MIN_LENGTH,
        tips: ''
      },
      errMsg: '',
      editingField: '',
      inlineForm: {
        username: '',
        phone: '',
        email: '',
        originPassword: '',
        newPassword: ''
      },
      duplicateState: {
        phone: '',
        email: ''
      },
      duplicateError: {
        phone: '',
        email: ''
      },
      activeTab: 'profile',
      showMfaModal: false,
      mfaStep: 1,
      mfaCodeTab: 'qr',
      mfaQrCode: '',
      mfaSecretCode: '',
      mfaInput: '',
      mfaAccountType: '',
      mfaMode: 'init',
      mfaExpireAt: 0,
      mfaNow: Date.now(),
      mfaExpireTimer: null,
      mfaModalLoading: false
    };
  },
  created() {
    if (this.userInfo.accountType === 'PRIMARY_ACCOUNT') {
      this.$services.rdpUserGetPrimaryAccountPwdPolicy().then((res) => {
        if (res.success) {
          this.applyPasswordRule(res.data);
        }
      });
    } else {
      this.$services.rdpUserGetSubAccountPwdPolicy().then((res) => {
        if (res.success) {
          this.applyPasswordRule(res.data);
        }
      });
    }
  },
  async mounted() {
    // this.listAllConfigs();
    if (this.$route.query?.tab === 'security') {
      this.activeTab = 'security';
    }
    await this.getConfigValueList();
  },
  beforeUnmount() {
    this.clearMfaExpireTimer();
  },
  computed: {
    ...mapGetters(['isInternalUser']),
    ...mapState(['userInfo']),
    mfaInvalid() {
      return this.userInfo.useMfa && this.userInfo.mfaStatus !== 'ACTIVE';
    },
    mfaAccountOptions() {
      return [
        {
          value: 'ACCOUNT',
          label: this.$t('zhang-hao'),
          account: this.userInfo.account
        },
        {
          value: 'EMAIL',
          label: this.$t('you-xiang'),
          account: this.userInfo.email
        },
        {
          value: 'PHONE',
          label: this.$t('shou-ji-hao'),
          account: this.userInfo.phone
        }
      ].map((item) => ({
        ...item,
        disabled: !item.account
      }));
    },
    mfaSecretCodeRows() {
      const groups = (this.mfaSecretCode || '').replace(/\s/g, '').match(/.{1,4}/g) || [];
      const rows = [];
      for (let i = 0; i < groups.length; i += 4) {
        rows.push(groups.slice(i, i + 4));
      }
      return rows;
    },
    mfaSettingExpired() {
      return Boolean(this.mfaExpireAt && this.mfaNow >= this.mfaExpireAt);
    },
    mfaBindInstruction() {
      if (this.mfaSettingExpired) {
        return this.$t('mfa-code-yi-shi-xiao-qing-zhong-xin-sheng-cheng');
      }
      const target = this.mfaCodeTab === 'code' ? this.$t('mfa-code') : this.$t('mfa-er-wei-ma');
      return this.$t('mfa-bang-ding-ti-shi', [target, this.formatMfaRemainTime(Math.max(this.mfaExpireAt - this.mfaNow, 0))]);
    }
  },
  methods: {
    ...mapMutations([UPDATE_USERINFO]),
    handleTabClick(name) {
      this.activeTab = name;
    },
    async getConfigValueList() {
      return Promise.resolve();
    },
    applyPasswordRule(rule) {
      this.passwordRule.strongPolicy = !!rule.strongPolicy;
      this.passwordRule.minLength = rule.minLength || DEFAULT_PASSWORD_MIN_LENGTH;
      this.passwordRule.tips = rule.tips;
    },
    isPasswordValid(value) {
      if (!value || value.length < (this.passwordRule.minLength || DEFAULT_PASSWORD_MIN_LENGTH)) {
        return false;
      }
      if (!this.passwordRule.strongPolicy) {
        return true;
      }
      return /[A-Z]/.test(value) && /[a-z]/.test(value) && /\d/.test(value);
    },
    async handleOpenMfaSetting() {
      this.mfaMode = 'init';
      this.resetMfaAccountTypeIfUnavailable();
      this.clearMfaExpireTimer();
      this.showMfaModal = true;
      this.mfaStep = 1;
      this.mfaCodeTab = 'qr';
      this.mfaQrCode = '';
      this.mfaSecretCode = '';
      this.mfaInput = '';
      this.errMsg = '';
    },
    resetMfaAccountTypeIfUnavailable() {
      if (this.mfaAccountOptions.some((item) => item.value === this.mfaAccountType && !item.disabled)) {
        return;
      }
      const accountOption = this.mfaAccountOptions.find((item) => item.value === 'ACCOUNT' && !item.disabled);
      const fallbackOption = this.mfaAccountOptions.find((item) => !item.disabled);
      this.mfaAccountType = (accountOption || fallbackOption || {}).value || '';
    },
    async generateMfaQrCode() {
      if (!this.mfaAccountType) {
        this.errMsg = this.$t('qing-xuan-ze-mfa-zhang-hao');
        return;
      }
      this.mfaModalLoading = true;
      try {
        const openRes = await this.$services.rdpMfaCtrlInitMfaSetting();
        if (!openRes.success) {
          return;
        }
        const generateService = this.mfaMode === 'reset' ? this.$services.rdpMfaResetMfaSetting : this.$services.rdpMfaInitMfaSetting;
        const res = await generateService({
          data: {
            mfaAccountType: this.mfaAccountType
          },
          modal: false
        });
        if (res?.success) {
          const mfaData = res.data || res;
          this.mfaQrCode = mfaData?.qrCode || '';
          this.mfaSecretCode = mfaData?.mfaCode || mfaData?.code || '';
          this.mfaCodeTab = 'qr';
          this.mfaStep = 2;
          this.startMfaExpireTimer();
        }
      } finally {
        this.mfaModalLoading = false;
      }
    },
    handleCloseMfaModal() {
      this.showMfaModal = false;
      this.mfaStep = 1;
      this.mfaCodeTab = 'qr';
      this.mfaQrCode = '';
      this.mfaSecretCode = '';
      this.mfaInput = '';
      this.mfaAccountType = '';
      this.mfaMode = 'init';
      this.clearMfaExpireTimer();
    },
    handleResetMfa() {
      this.mfaMode = 'reset';
      this.resetMfaAccountTypeIfUnavailable();
      this.clearMfaExpireTimer();
      this.showMfaModal = true;
      this.mfaStep = 1;
      this.mfaCodeTab = 'qr';
      this.mfaQrCode = '';
      this.mfaSecretCode = '';
      this.mfaInput = '';
      this.errMsg = '';
    },
    async handleConfirmMfaModal() {
      if (this.mfaStep === 1) {
        await this.generateMfaQrCode();
        return;
      }
      if (!this.mfaInput || !isNumber(this.mfaInput)) {
        this.errMsg = this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma');
        return;
      }
      if (this.mfaSettingExpired) {
        this.errMsg = this.$t('mfa-code-yi-shi-xiao-qing-zhong-xin-sheng-cheng');
        return;
      }
      this.mfaModalLoading = true;
      const confirmService = this.mfaMode === 'reset' ? this.$services.rdpMfaConfirmResetMfaSetting : this.$services.rdpMfaConfirmInitMfaSetting;
      const res = await confirmService({
        data: {
          mfaCode: this.mfaInput
        }
      });
      this.errMsg = '';
      if (res?.success) {
        this.$Message.success(this.mfaMode === 'reset' ? this.$t('chong-zhi-duo-yin-zi-ren-zheng-cheng-gong') : this.$t('kai-qi-cheng-gong'));
        this.$store.dispatch('getUserInfo');
        this.handleCloseMfaModal();
      }
      this.mfaModalLoading = false;
    },
    startInlineEdit(field) {
      this.editingField = field;
      this.errMsg = '';
      this.duplicateState.phone = '';
      this.duplicateState.email = '';
      this.duplicateError.phone = '';
      this.duplicateError.email = '';
      this.inlineForm.username = this.userInfo.username || '';
      this.inlineForm.phone = this.userInfo.phone || '';
      this.inlineForm.email = this.userInfo.email || '';
      this.inlineForm.originPassword = '';
      this.inlineForm.newPassword = '';
    },
    cancelInlineEdit() {
      this.editingField = '';
      this.errMsg = '';
      this.duplicateState.phone = '';
      this.duplicateState.email = '';
      this.duplicateError.phone = '';
      this.duplicateError.email = '';
      this.inlineForm.originPassword = '';
      this.inlineForm.newPassword = '';
    },
    async checkInlineDuplicate(field) {
      const value = this.inlineForm[field];
      this.duplicateState[field] = '';
      this.duplicateError[field] = '';
      if (!value || value === this.userInfo[field]) {
        return true;
      }
      const res = await this.$services.rdpUserCheckProfileDuplicate({
        data: {
          checkType: field.toUpperCase(),
          checkContent: value,
          targetUid: this.userInfo.uid
        },
        modal: false
      });
      if (res.success) {
        this.duplicateState[field] = 'ok';
        return true;
      }
      this.duplicateError[field] = res.msgContent || res.msg;
      return false;
    },
    async confirmInlineEdit(field) {
      this.errMsg = '';
      if (field === 'username') {
        if (!this.inlineForm.username) {
          this.$Message.error(this.$t('yong-hu-ming-bu-neng-wei-kong'));
          return;
        }
        this.loading = true;
        try {
          const res = await this.$services.rdpUserUpdateUserName({
            data: {
              userName: this.inlineForm.username
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this[UPDATE_USERINFO]({ username: this.inlineForm.username });
            await this.$store.dispatch('getUserInfo');
            this.cancelInlineEdit();
          }
        } finally {
          this.loading = false;
        }
        return;
      }

      if (field === 'phone' || field === 'email') {
        const duplicateOk = await this.checkInlineDuplicate(field);
        if (!duplicateOk) {
          return;
        }
        const postFunc = field === 'phone' ? this.$services.rdpUserUpdateUserPhone : this.$services.rdpUserUpdateUserEmail;
        const confirmMfaInvalid = await this.confirmMfaInvalidIfNeeded(this.inlineForm[field] === this.userInfo[field] ? [] : [field]);
        if (confirmMfaInvalid === null) {
          return;
        }
        this.loading = true;
        try {
          const res = await postFunc({
            data: {
              [field]: this.inlineForm[field],
              confirmMfaInvalid
            }
          });
          if (res.success) {
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this[UPDATE_USERINFO]({
              [field]: this.inlineForm[field],
              ...(confirmMfaInvalid ? { mfaStatus: 'INVALID' } : {})
            });
            await this.$store.dispatch('getUserInfo');
            this.cancelInlineEdit();
          }
        } finally {
          this.loading = false;
        }
        return;
      }

      if (field === 'password') {
        if (!this.inlineForm.originPassword || !this.inlineForm.newPassword) {
          this.errMsg = this.$t('qing-shu-ru-mi-ma');
          return;
        }
        if (!this.isPasswordValid(this.inlineForm.newPassword)) {
          this.errMsg = this.passwordRule.tips;
          return;
        }
        this.loading = true;
        try {
          const res = await this.$services.rdpUserResetPwdWithOriginPwd({
            data: {
              originPassword: this.passwordEncrypt(this.inlineForm.originPassword),
              newPassword: this.passwordEncrypt(this.inlineForm.newPassword)
            },
            modal: false
          });
          if (res.success) {
            this.cancelInlineEdit();
            this.$router.push({ path: '/login' });
          } else {
            this.errMsg = res.msgContent;
          }
        } finally {
          this.loading = false;
        }
      }
    },
    handleCancelEdit() {
      this.errMsg = '';
      this.showAKSK = false;
      this.mfaStep = 1;
      this.mfaCodeTab = 'qr';
      this.mfaQrCode = '';
      this.mfaSecretCode = '';
      this.mfaAccountType = '';
      this.mfaMode = 'init';
      this.clearMfaExpireTimer();
    },
    handleShowFetchAKSK() {
      this.handleConfirmFetchAKSK();
    },
    async handleConfirmFetchAKSK() {
      const res = await this.$services.rdpUserQueryUserAkSk({
        data: {}
      });
      if (res.success) {
        this.akskInfo = res.data;
        this.showAKSK = true;
      }
    },
    handleShowResetAKSK() {
      this.$Modal.confirm({
        title: this.$t('que-ren-chong-zhi-aksk'),
        content: this.$t('chong-zhi-aksk-hou-jiu-de-aksk-jiang-li-ji-shi-xiao-que-ren-yao-ji-xu-ma'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: () => {
          this.handleConfirmSetAKSK();
        }
      });
    },
    async handleConfirmSetAKSK() {
      const res = await this.$services.rdpUserResetUserAkSk({
        data: {}
      });
      if (res.success) {
        this.$Message.success(this.$t('aksk-chong-zhi-cheng-gong'));
      }
    },
    confirmMfaInvalidIfNeeded(fields) {
      const fieldTypeMap = {
        account: 'ACCOUNT',
        phone: 'PHONE',
        email: 'EMAIL'
      };
      const affectedFields = fields.filter((field) => {
        const type = fieldTypeMap[field];
        const isActiveMfa = this.userInfo.useMfa && this.userInfo.mfaStatus === 'ACTIVE';
        const isSameType = !this.userInfo.mfaAccountType || this.userInfo.mfaAccountType === type;
        return isActiveMfa && isSameType;
      });
      if (!affectedFields.length) {
        return Promise.resolve(false);
      }
      const fieldNames = affectedFields.map((field) => this.mfaFieldLabel(field)).join('、');
      return new Promise((resolve) => {
        this.$Modal.confirm({
          title: this.$t('mfa-shi-xiao-que-ren'),
          content: this.$t('mfa-guan-lian-ziduan-xiugai-tishi', [fieldNames]),
          okText: this.$t('ji-xu'),
          cancelText: this.$t('qu-xiao'),
          onOk: () => resolve(true),
          onCancel: () => resolve(null)
        });
      });
    },
    mfaFieldLabel(field) {
      const labelMap = {
        account: this.$t('zhang-hao'),
        phone: this.$t('shou-ji'),
        email: this.$t('you-xiang')
      };
      return labelMap[field] || field;
    },
    startMfaExpireTimer() {
      this.clearMfaExpireTimer();
      this.mfaNow = Date.now();
      this.mfaExpireAt = this.mfaNow + MFA_SETTING_EXPIRE_MS;
      this.mfaExpireTimer = window.setInterval(() => {
        this.mfaNow = Date.now();
        if (this.mfaSettingExpired) {
          this.clearMfaExpireTimer(false);
        }
      }, 1000);
    },
    clearMfaExpireTimer(reset = true) {
      if (this.mfaExpireTimer) {
        window.clearInterval(this.mfaExpireTimer);
        this.mfaExpireTimer = null;
      }
      if (reset) {
        this.mfaExpireAt = 0;
        this.mfaNow = Date.now();
      }
    },
    formatMfaRemainTime(ms) {
      const seconds = Math.ceil(ms / 1000);
      const minutes = Math.floor(seconds / 60);
      const restSeconds = seconds % 60;
      return `${String(minutes).padStart(2, '0')}:${String(restSeconds).padStart(2, '0')}`;
    },
    handleCopy(value) {
      const aux = document.createElement('input');

      aux.setAttribute('value', value);
      document.body.appendChild(aux);
      aux.select();
      document.execCommand('copy');
      document.body.removeChild(aux);

      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleDownloadMfaCode() {
      const code = (this.mfaSecretCode || '').replace(/\s/g, '');
      if (!code) {
        return;
      }
      const username = this.sanitizeDownloadFileName(this.userInfo.username || this.userInfo.account || this.userInfo.uid || 'user');
      const blob = new Blob([`${code}\n`], { type: 'text/plain;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `clouddm-mfacode-${username}.txt`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
    },
    sanitizeDownloadFileName(name) {
      return (
        String(name || 'user')
          .trim()
          .replace(/[\\/:*?"<>|]+/g, '_')
          .replace(/\s+/g, '_') || 'user'
      );
    },
    handleShowCloseMf() {
      this.$Modal.confirm({
        title: this.$t('guan-bi-duo-yin-zi-ren-zheng'),
        content: this.$t('mfa-guan-bi-wu-yan-zheng-ti-shi'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: async () => {
          const service = this.mfaInvalid ? this.$services.rdpMfaCloseInvalidMfaSettings : this.$services.rdpMfaCloseMfaSettings;
          const res = await service({ data: {} });
          if (res.success) {
            this.$Message.success(this.$t('guan-bi-cheng-gong'));
            await this.$store.dispatch('getUserInfo');
          }
        }
      });
    }
  }
};
</script>
<style lang="less">
.profile-page__body {
  flex: 1;
  min-height: 0;
  overflow: auto;
}

.user-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  padding: 0;
  background: var(--bg-card);

  &__item {
    position: relative;
    padding: 12px 20px 10px;
    color: var(--text-secondary);
    font-size: 13px;
    font-weight: 400;
    line-height: 1.4;
    cursor: pointer;
    border-bottom: none;
    transition: color 0.12s ease;

    &:hover {
      color: var(--text-primary);
      border-bottom: none;
    }

    &.is-active {
      color: var(--text-primary);
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        left: 20px;
        right: 20px;
        bottom: 0;
        height: 2px;
        border-radius: 2px 2px 0 0;
        background: var(--primary-color);
      }
    }
  }
}

.user-center-title-container {
  font-size: 12px;
  padding-left: 8px;
  color: rgba(0, 0, 0, 0.88);
  font-family: PingFangSC-Semibold, serif;
  font-weight: 500;
  border-left: 3px solid #535c70;
  margin-bottom: 6px;
}

.user-center-wrapper {
  margin-top: 16px;
  padding: 20px 40px;
  background-color: #ffffff;
  border: 1px solid #dadada;

  & > p {
    line-height: 36px;
    font-size: 16px;
    padding: 12px 0;
    border-bottom: 1px solid #dadada;

    .user-center-label {
      font-family: PingFangSC-Semibold, serif;
      font-weight: 500;
      display: inline-block;
      width: 104px;
    }

    .user-center-value {
      margin-right: 10px;
      display: inline-block;
    }
  }

  .ivu-table td,
  .ivu-table th {
    height: 40px;
  }
}

.user-center-wrapper-sp {
  background-color: #ececec;
  margin-bottom: 20px;

  .ivu-input {
    height: 40px;
    line-height: 40px;
  }

  .user-center-wrapper-sp-btn {
    background-color: #ffa30e;
    color: #ffffff;
    margin-top: 16px;
    background-image: none;
    border: none;
    width: 100%;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    font-family: PingFangSC-Semibold, serif;
    font-weight: 500;

    &:hover {
      background-color: #ffa30e !important;
      background-image: none;
    }
  }
}

/*.ivu-form-item:last-child{*/
/*    margin-bottom: 0;*/
/*}*/
.system-setting-title {
  font-family: PingFangSC-Semibold, serif;
  font-weight: 500;
  margin-bottom: 20px;
}

.user-center {
  position: relative;
  padding: 0;

  .ivu-table th {
    background-color: var(--bg-secondary);
  }
}

.password-check {
  li {
    font-size: 12px;
    line-height: 25px;
    color: #808695;

    i {
      margin-right: 5px;
      font-size: 14px;
    }

    .ivu-icon-ios-close-circle-outline {
      color: #ed4014;
    }

    .ivu-icon-ios-checkmark-circle-outline {
      color: #19be6b;
    }
  }
}

.resource-basic {
  height: 132px;
  width: 100%;
  /*padding: 20px 60px;*/
  padding-top: 32px;
  /*border-radius: 4px;*/
  background-color: #ffffff;
  position: relative;
  box-shadow: 1px 1px 5px 0 rgba(197, 197, 197, 0.5);

  .resource-content {
    width: 200px;
    margin: 0 auto;
    /*padding-left: 96px;*/

    .license-set-url {
      cursor: pointer;
      font-family: PingFangSC-Medium, serif;
      font-weight: 500;
      margin-bottom: 30px;

      &:hover {
        color: #0bb9f8;
      }

      &:active {
        color: #0087c7;
      }
    }
  }

  .resource-basic-title {
    font-size: 16px;
    margin-bottom: 8px;
  }

  .resource-basic-count {
    font-size: 24px;
    font-family: PingFangSC-Semibold, serif;
    font-weight: 500;
    /*margin-bottom: 16px;*/
  }

  .resource-logo {
    /*position: absolute;*/
    font-size: 32px;
    /*left: 20px;*/
    /*top:37px;*/
    display: inline-block;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    text-align: center;
    line-height: 60px;
    vertical-align: middle;
    margin-right: 16px;

    .iconfont {
      font-size: 32px;
    }

    .iconxingzhuang {
      color: #ffac25;
    }

    .iconquanliangqianyisvg {
      color: #66a2ff;
    }

    .iconqianyi {
      color: #67cd51;
    }

    .iconjiaoyan {
      color: #838aff;
    }
  }
}

.resource-suggest {
  background-color: #fff2de;
  height: 580px;
  overflow: hidden;

  .iconfont {
    color: #f1dab6;
    font-size: 180px;
    right: -50px;
    bottom: -40px;
  }
}

.quota-basic {
  height: 125px;
  background-color: #def3fc;
  border-radius: 4px;
  padding: 20px 26px;

  .quota-basic-title {
    font-size: 16px;
    margin-bottom: 7px;
  }

  .quota-use-count {
    font-size: 36px;
    font-family: PingFangSC-Semibold, serif;
    margin-right: 10px;
  }

  .quota-total-count {
    font-size: 24px;
    font-family: PingFangSC-Semibold, serif;
    margin-top: 20px;
  }
}

.user-account-title {
  font-size: 16px;
  font-family: PingFangSC-Semibold, serif;
  margin-bottom: 10px;
  border: 1px solid #dadada;
  background-color: #eeeeee;
  padding: 0 16px;
  height: 40px;
  line-height: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-quota-count {
  font-family: PingFangSC-Medium, serif;
  font-size: 20px;
  font-weight: 500;
  margin-bottom: 8px;
  text-align: center;
}

.user-quota-title {
  color: #888888;
  text-align: center;
}

.license-set-detail-item {
  height: 40px;
  line-height: 40px;

  span {
    width: 100px;
    display: inline-block;
  }
}

.divide > * {
  border-bottom: 1px solid rgb(243, 244, 246);
}

// Dark pattern fit
[data-theme='dark'] {
  // Page Container Background
  .user-center {
    background-color: var(--bg-primary) !important;

    .ivu-table th {
      background-color: var(--bg-tertiary) !important;
    }
  }

  // Tailwind CSS Colour Overwrite
  .border-gray-100 {
    border-color: var(--border-primary) !important;
  }

  .text-gray-900 {
    color: var(--text-primary) !important;
  }

  .text-gray-700 {
    color: var(--text-secondary) !important;
  }

  // Personal Information List Style
  .divide > * {
    border-bottom-color: var(--border-primary) !important;
  }

  // Custom Style Fit
  .user-center-title-container {
    color: var(--text-primary) !important;
    border-left-color: var(--primary-color) !important;
  }

  .user-center-wrapper {
    background-color: var(--bg-secondary) !important;
    border-color: var(--border-primary) !important;

    & > p {
      border-bottom-color: var(--border-primary) !important;
      color: var(--text-primary) !important;
    }
  }

  .user-center-wrapper-sp {
    background-color: var(--bg-tertiary) !important;
  }

  .resource-basic {
    background-color: var(--bg-secondary) !important;
    box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.5) !important;
  }

  .resource-suggest {
    background-color: var(--bg-tertiary) !important;
  }

  .quota-basic {
    background-color: var(--bg-tertiary) !important;
  }

  .user-account-title {
    border-color: var(--border-primary) !important;
    background-color: var(--bg-tertiary) !important;
    color: var(--text-primary) !important;
  }

  .user-quota-title {
    color: var(--text-secondary) !important;
  }

  .password-check {
    li {
      color: var(--text-secondary) !important;
    }
  }

  // Lock state label style
  .border-red-700 {
    border-color: var(--error-color) !important;
  }

  .text-red-700 {
    color: var(--error-color) !important;
  }

  .bg-red-50 {
    background-color: var(--error-bg) !important;
  }

  // Bread Crumb Navigator
  .ivu-breadcrumb {
    color: var(--text-secondary) !important;

    a {
      color: var(--text-secondary) !important;

      &:hover {
        color: var(--primary-color) !important;
      }
    }
  }

  // Tab Content Area
  .ivu-tabs-content {
    background-color: var(--bg-primary) !important;
  }

  // Icon Component Colour
  .ivu-icon {
    color: var(--text-secondary) !important;

    &:hover {
      color: var(--primary-color) !important;
    }
  }

  // Button Style (to ensure that buttons are displayed correctly in dark colour mode)
  .ivu-btn {
    &-default {
      background-color: var(--bg-secondary) !important;
      border-color: var(--border-primary) !important;
      color: var(--text-primary) !important;

      &:hover {
        background-color: var(--bg-hover) !important;
        border-color: var(--border-primary) !important;
        color: var(--text-primary) !important;
      }
    }
  }

  // Form Input Box
  .ivu-input {
    background-color: var(--bg-tertiary) !important;
    border-color: var(--border-primary) !important;
    color: var(--text-primary) !important;

    &[disabled] {
      background-color: var(--disabled-bg) !important;
      color: var(--text-disabled) !important;
    }
  }

  // Form Label
  .ivu-form-item-label {
    color: var(--text-primary) !important;
  }

  // Warningtip Box
  .ivu-alert {
    background-color: var(--bg-tertiary) !important;
    border-color: var(--border-primary) !important;
    color: var(--text-primary) !important;
  }

  // Modular frame (processed in global styles, ensure overlay here)
  .ivu-modal {
    &-content {
      background-color: var(--bg-secondary) !important;
    }

    &-header {
      background-color: var(--bg-secondary) !important;
      border-bottom-color: var(--border-primary) !important;
      color: var(--text-primary) !important;
    }

    &-body {
      background-color: var(--bg-secondary) !important;
      color: var(--text-primary) !important;
    }

    &-footer {
      background-color: var(--bg-secondary) !important;
      border-top-color: var(--border-primary) !important;
    }
  }

  // Title Style in Secure Tab
  h3 {
    color: var(--text-primary) !important;
  }

  // Paragraph Text
  p {
    color: var(--text-primary) !important;
  }
}
.ivu-poptip-rel {
  width: 100% !important;
}
.empty-value {
  color: #a2a9b6;
}
.inline-edit-row {
  display: flex;
  align-items: center;
  gap: 8px;
  max-width: 680px;

  .ivu-input-wrapper,
  .ivu-poptip {
    flex: 1 1 220px;
    min-width: 160px;
  }

  .ivu-btn {
    flex: 0 0 auto;
  }
}
.password-edit-row {
  max-width: 820px;
}
.inline-edit-error {
  margin-top: 6px;
  color: #ed4014;
  font-size: 12px;
}
.mfa-code-tabs {
  margin-top: 12px;
}
.mfa-modal-body {
  min-height: 394px;
}
.mfa-qr-panel {
  min-height: 208px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.mfa-reset-qr-panel {
  min-height: 208px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 12px;
}
.mfa-qr-wrapper {
  position: relative;
  width: 200px;
  height: 200px;

  &.is-reset {
    width: 180px;
    height: 180px;
  }
}
.mfa-qr-code {
  width: 200px;
  height: 200px;
  display: block;

  &.is-reset {
    width: 180px;
    height: 180px;
  }
}
.mfa-expired-mask {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #ed4014;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid #f5b7b1;
  font-size: 16px;
  text-align: center;

  strong {
    font-size: 22px;
  }
}
.mfa-bind-instruction {
  margin: 16px 0;
  color: #808695;
  font-size: 13px;
  line-height: 20px;

  &.expired {
    color: #ed4014;
    font-weight: 600;
  }
}
.mfa-authenticator-links {
  margin-top: 14px;
  display: flex;
  justify-content: center;
  gap: 24px;
  font-size: 13px;
  line-height: 20px;

  a {
    color: #2d8cf0;
  }
}
.mfa-secret-panel {
  min-height: 208px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 20px;
}
.mfa-secret-code {
  color: #515a6e;
  font-family: Menlo, Monaco, Consolas, 'Courier New', monospace;
  font-size: 28px;
  font-weight: 700;
  line-height: 42px;
  letter-spacing: 0;
  text-align: center;
}
.mfa-secret-code-row {
  display: grid;
  grid-template-columns: repeat(4, 4.4em);
  column-gap: 16px;
  justify-content: center;
}
.mfa-secret-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}
.mfa-verify-divider {
  height: 1px;
  margin: 20px 0 16px;
  background-color: #dcdee2;
}
</style>
