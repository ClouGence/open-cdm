<template>
  <div class="user-center content-wrapper" style="padding: 0">
    <div>
      <div style="position: relative">
        <div>
          <div v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_R')" class="user-account-title border-radius-card" style="margin-bottom: 8px">
            <div>{{ $t('yong-hu-she-zhi') }}</div>
            <div>
              <Button
                :loading="loading"
                type="default"
                style="margin-right: 6px"
                @click="handleSearch"
                v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_R')"
                size="small"
              >
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
              </Button>
              <Button v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_W')" type="primary" @click="handleSubmitUserConfig" size="small">
                {{ $t('bao-cun') }}
              </Button>
            </div>
          </div>
          <user-config-params-edit
            :refresh-loading="loading"
            v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_R')"
            ref="userConfigParamsEdit"
            :canEdit="canEdit"
          />
        </div>
      </div>
      <p style="margin-top: 20px">
        <Button
          :loading="verifyEmailLoading"
          v-if="myAuth.includes('RDP_PRI_USER_NORMAL_CONF_R')"
          style="font-size: 14px; margin-right: 16px"
          @click="handleVerifyEmail"
        >
          {{ $t('yan-zheng-you-xiang-fu-wu-qi') }}
        </Button>
        <Button :loading="verifyIMLoading" v-if="myAuth.includes('RDP_PRI_USER_NORMAL_CONF_R')" style="font-size: 14px" @click="handleVerifyIm">
          {{ $t('yan-zheng-im-gao-jing') }}
        </Button>
      </p>
    </div>
    <verify-code-modal
      :visible="showEditUserInfo"
      :title="$t('xiu-gai-updateuserinfotypephone-shou-ji-hao-you-xiang', [updateUserInfoType === `phone` ? $t('shou-ji-hao') : $t('you-xiang')])"
      :verify-code-type="
        updateUserInfoType === 'phone'
          ? isVerifyPhone
            ? 'VERIFY_OLD_ACCOUNT'
            : 'UPDATE_USER_PHONE'
          : isVerifyEmail
            ? 'VERIFY_OLD_ACCOUNT'
            : 'UPDATE_USER_EMAIL'
      "
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmUpdateUserInfo"
      :has-next-step="isVerifyPhone || isVerifyEmail"
      :new-phone="newPhone"
      :phone-number="updateUserInfo.phone"
      :email="updateUserInfo.email"
      :new-email="newEmail"
      ref="clear-position-modal"
      :width="580"
    >
      <template #content>
        <Form label-position="right" :label-width="60">
          <FormItem :label="$t('shou-ji-hao-0')" v-if="updateUserInfoType === 'phone' && !isVerifyPhone">
            <Input v-model="updateUserInfo.phone" />
          </FormItem>
          <FormItem
            :label="updateUserInfoType === `phone` ? $t('yuan-shou-ji-hao') : $t('shou-ji-hao-0')"
            prop="phone"
            v-if="isVerifyPhone || updateUserInfoType !== 'phone'"
          >
            <Input v-model="userInfo.phone" disabled />
          </FormItem>
          <FormItem :label="$t('you-xiang-0')" v-if="updateUserInfoType === 'email' && !isVerifyEmail">
            <Input v-model="updateUserInfo.email" />
          </FormItem>
        </Form>
      </template>
    </verify-code-modal>
    <verify-code-modal
      :visible="showFetchAKSK"
      :title="$t('huo-qu-aksk')"
      verify-code-type="FETCH_USER_AK_SK"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmFetchAKSK"
      ref="clear-position-modal"
      :loading="confirmFetchLoading"
      :width="580"
    >
      <template #content>
        <h3 style="margin-bottom: 20px">
          {{ $t('wei-bao-zheng-an-quan-xing-qing-shu-ru-yan-zheng-ma-lai-huo-qu-aksk') }}
        </h3>
      </template>
    </verify-code-modal>
    <verify-code-modal
      :visible="showResetAKSK"
      :title="$t('chong-zhi-aksk')"
      verify-code-type="RESET_USER_AK_SK"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmSetAKSK"
      ref="clear-position-modal"
      :loading="confirmFetchLoading"
      :width="580"
    >
      <template #content>
        <h3 style="margin-bottom: 20px">
          {{ $t('wei-bao-zheng-an-quan-xing-qing-shu-ru-yan-zheng-ma-lai-chong-zhi-aksk') }}
        </h3>
      </template>
    </verify-code-modal>
    <CCModal v-model="showAKSK" title="AK/SK" width="600px">
      <div>
        <h3 style="margin-bottom: 20px">
          {{ $t('wei-bao-zheng-nin-de-zhang-hao-an-quan-qing-wu-bi-bao-guan-hao-nin-de-aksk') }}
        </h3>
        <Form label-position="right" :label-width="80">
          <FormItem label="accessKey：">
            <p>{{ akskInfo.accessKey }}</p>
          </FormItem>
          <FormItem label="secretKey：">
            <p>{{ akskInfo.secretKey }}</p>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleCopy(JSON.stringify(akskInfo))">
          {{ $t('fu-zhi') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import fecha from 'fecha';
// import { getGlobalSystemConfig } from '@/services/cc/api/constant';
import { formatHour } from '@/components/util';
import VerifyCodeModal from '@/components/modal/VerifyCodeModal';
import UserConfigParamsEdit from '@/views/system/UserConfigParamsEdit';
// import FollowedJobList from '@/views/system/FollowedJobList';
import { mapGetters, mapState } from 'vuex';
import Mapping from '../util';

export default {
  components: { UserConfigParamsEdit, VerifyCodeModal },
  data() {
    return {
      updateUserInfoType: '',
      loading: false,
      editEmail: false,
      isVerifyPhone: false,
      isVerifyEmail: false,
      newPhone: false,
      newEmail: false,
      showEditUserInfo: false,
      showFetchAKSK: false,
      showResetAKSK: false,
      showAKSK: false,
      canEdit: false,
      confirmFetchLoading: false,
      verifyEmailLoading: false,
      verifyIMLoading: false,
      akskInfo: {},
      formatHour,
      updateUserInfo: {
        phone: '',
        email: ''
      },
      resourceData: {
        fullCheckCount: 0,
        fullTransferCount: 0,
        incrementDuration: 0,
        structTransferCount: 0
      },
      quotaData: {
        workerCount: 0,
        workerCountUsed: 0,
        dataJobCount: 0,
        dataSourceCount: 0,
        dataJobCountUsed: 0,
        dataSourceCountUsed: 0
      },
      applyCode: '',
      ifEdit: true,
      showTest: false,
      connection: false,
      showEditPassword: false,
      showSmtp: false,
      pwLength: false,
      pwContain: false,
      pwFormat: false,
      pwConfirm: false,
      verifyCode: '',
      password: '',
      passwordAgain: '',
      sendcodeDisabled: true,
      sendCodeAgainTime: 60,
      systemForm: {
        EMAIL_HOST_KEY: '',
        EMAIL_PORT_KEY: '465',
        EMAIL_USERNAME_KEY: '',
        EMAIL_PASSWORD_KEY: '',
        DINGDING_URL_TOKEN_KEY: '',
        EMAIL_FROM_KEY: ''
      },
      alarmSetting: {},
      setList: [],
      licenseUrl: {},
      aliyunAk: '',
      aliyunSk: '',
      emailList: [],
      emailSuffix: ['qq.com', 'sina.com', '163.com', 'sohu.com', '126.com'],
      smtpList: {
        'qq.com': 'smtp.qq.com',
        'sina.com': 'smtp.sina.com.cn',
        '163.com': 'smtp.163.com',
        'sohu.com': 'smtp.sohu.com',
        '126.com': 'smtp.126.com'
      },
      smtpPort: {
        'qq.com': '465',
        'sina.com': '25',
        '163.com': '465',
        'sohu.com': '110',
        '126.com': '25'
      },
      configKeyMap: {
        EMAIL_HOST_KEY: 'spring.mail.host',
        EMAIL_PORT_KEY: 'spring.mail.port',
        EMAIL_USERNAME_KEY: 'spring.mail.username',
        EMAIL_PASSWORD_KEY: 'spring.mail.password',
        EMAIL_FROM_KEY: 'spring.mail.properties.from',
        DINGDING_URL_TOKEN_KEY: 'console.config.alert.dingtalk.alerturl'
      },
      editPasswordRule: {
        password: [{ required: true, message: 'The name cannot be empty', trigger: 'blur' }],
        passwordAgain: [{ required: true, message: 'The name cannot be empty', trigger: 'blur' }],
        verifyCode: [{ required: true, message: 'The verifyCode cannot be empty', trigger: 'blur' }]
      },
      setMetaColumn: [
        {
          title: this.$t('tao-can-ming-cheng'),
          key: 'licenseSetMeta'
        },
        {
          title: this.$t('nei-rong'),
          slot: 'licenseContent'
        },
        {
          title: this.$t('mu-lu-jia-ge'),
          width: 120,
          render: (h, params) =>
            h('div', {}, this.$t('thisgetlicensepriceparamsrowlicensemetas-yuan', [this.getLicensePrice(params.row.licenseMetas)]))
        }
      ],
      guotaColumn: [
        {
          title: this.$t('xian-zhi-xiang-mu'),
          key: 'description',
          minWidth: 160
        },
        {
          title: this.$t('yi-yong-shu-liang'),
          key: 'used',
          minWidth: 80
        },
        {
          title: this.$t('zong-shu'),
          key: 'quota',
          minWidth: 80
        }
      ],
      userConfigList: [],
      userConfigs: {}
    };
  },
  created() {
    this.canEdit = this.myAuth.includes('RDP_PRI_USER_KV_CONF_W');
  },
  mounted() {
    // this.listAllConfigs();
    if (this.myAuth.includes('RDP_PRI_USER_KV_CONF_R')) {
      this.getUserConfig();
    }
  },
  computed: {
    ...mapGetters(['verifyType']),
    ...mapState(['userInfo', 'myAuth']),
    getCreateTime() {
      if (this.userInfo.gmtCreate) {
        return fecha.format(new Date(this.userInfo.gmtCreate), 'YYYY-MM-DD HH:mm:ss');
      }
      return '';
    },
    getUpdateTime() {
      if (this.userInfo.gmtModified) {
        return fecha.format(new Date(this.userInfo.gmtModified), 'YYYY-MM-DD HH:mm:ss');
      }
      return '';
    }
  },
  methods: {
    async handleSubmitUserConfig() {
      await this.$refs.userConfigParamsEdit.showUserConfigModal();
    },
    async getUserConfig() {
      this.loading = true;
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      this.loading = false;
      if (res.success) {
        this.userConfigList = res.data;
        this.userConfigList.forEach((item) => {
          this.userConfigs[item.configName] = item.configValue;
        });
      }
    },
    handleSearch() {
      this.getUserConfig();
    },
    listAllConfigs() {
      this.loading = true;
      this.$services.ccSystemConfigList().then((res) => {
        this.loading = false;
        if (res.success) {
          this.alarmSetting.emailAddress = res.data.emailAddress;
          this.alarmSetting.phoneNumber = res.data.phoneNumber;
          res.data.systemConfigVOList.map((item) => {
            if (item.configName === 'spring.mail.host') {
              this.systemForm.EMAIL_HOST_KEY = item.configValue;
            }
            if (item.configName === 'spring.mail.port') {
              this.systemForm.EMAIL_PORT_KEY = item.configValue;
            }
            if (item.configName === 'spring.mail.username') {
              this.systemForm.EMAIL_USERNAME_KEY = item.configValue;
            }
            if (item.configName === 'spring.mail.password') {
              // this.systemForm.EMAIL_PASSWORD_KEY = item.configValue;
            }
            if (item.configName === 'console.config.alert.dingtalk.alerturl') {
              this.systemForm.DINGDING_URL_TOKEN_KEY = item.configValue;
            }
            if (item.configName === 'spring.mail.properties.from') {
              this.systemForm.EMAIL_FROM_KEY = item.configValue;
            }
            if (this.systemForm.EMAIL_FROM_KEY) {
              this.systemForm.EMAIL_USERNAME_KEY = this.systemForm.EMAIL_FROM_KEY;
            } else if (this.systemForm.EMAIL_USERNAME_KEY) {
              this.systemForm.EMAIL_FROM_KEY = this.systemForm.EMAIL_USERNAME_KEY;
            }
            return null;
          });
          this.handleShowStmp();
        }
      });
    },
    handleShowPassword() {
      // this.showEditPassword = true;
      this.$router.push({ path: '/reset' });
      // window.location.reload();
    },
    handleUpdateUserInfo(type) {
      this.updateUserInfoType = type;
      this.showEditUserInfo = true;
      this.updateUserInfo.phone = this.userInfo.phone;
      this.updateUserInfo.email = this.userInfo.email;
      if (type === 'phone') {
        this.isVerifyPhone = true;
      }
    },
    handleCancelEdit() {
      this.verifyCode = '';
      this.password = '';
      this.passwordAgain = '';
      // this.ifEdit = false;
      this.showEditPassword = false;
      this.editEmail = false;
      this.showEditUserInfo = false;
      this.showFetchAKSK = false;
      this.showResetAKSK = false;
      this.showAKSK = false;
      this.isVerifyPhone = false;
      this.isVerifyEmail = false;
    },
    handleShowFetchAKSK() {
      this.showFetchAKSK = true;
    },
    handleShowResetAKSK() {
      this.showResetAKSK = true;
    },
    handleConfirmFetchAKSK(verifyCode) {
      this.confirmFetchLoading = true;
      this.$services
        .rdpUserQueryUserAkSk({
          data: {
            verifyCode,
            verifyType: this.verifyType
          }
        })
        .then((res) => {
          this.showFetchAKSK = false;
          if (res.success) {
            this.akskInfo = res.data;
            this.showAKSK = true;
          }
          this.confirmFetchLoading = false;
        });
    },
    handleConfirmSetAKSK(verifyCode) {
      this.confirmFetchLoading = true;
      this.$services
        .rdpUserResetUserAkSk({
          data: {
            verifyCode,
            verifyType: this.verifyType
          }
        })
        .then((res) => {
          this.showFetchAKSK = false;
          if (res.success) {
            this.$Message.success(this.$t('aksk-chong-zhi-cheng-gong'));
            this.showResetAKSK = false;
          }
          this.confirmFetchLoading = false;
        });
    },
    handleConfirmUpdateUserInfo(verifyCode) {
      if (this.isVerifyPhone || this.isVerifyEmail) {
        const data = {
          verifyCode,
          verifyType: this.verifyType
        };
        this.$services.rdpUserCheckVerifyCode({ data }).then((res) => {
          if (res.success) {
            this.isVerifyPhone = false;
            this.isVerifyEmail = false;
            this.userInfo.verifyCode = '';
            // this.$refs.verifyCount.counting = false;
            this.newPhone = true;
            this.newEmail = true;
          }
        });
      } else {
        const postFunc = this.updateUserInfoType === 'phone' ? this.$services.rdpUserUpdateUserPhone : this.$services.rdpUserUpdateUserEmail;
        postFunc({
          data: {
            phone: this.updateUserInfo.phone,
            email: this.updateUserInfo.email,
            verifyCode,
            verifyType: this.verifyType
          }
        }).then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this.showEditUserInfo = false;
            setTimeout(() => {
              this.getUserInfo();
            }, 500);
          }
        });
      }
    },
    handleVerify() {
      this.sendcodeDisabled = false;
      this.sendCodeAgainTime = 60;
      const that = this;

      this.sendCodeAgain = setInterval(() => {
        if (that.sendCodeAgainTime > 0) {
          that.sendCodeAgainTime--;
        } else {
          clearInterval(that.sendCodeAgain);
          that.sendcodeDisabled = true;
        }
      }, 1000);

      this.$services
        .rdpVerifySendCodeInLoginState({
          data: {
            verifyType: 'SMS_VERIFY_CODE',
            verifyCodeType: 'RESET_PASSWORD'
          }
        })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('fa-song-cheng-gong'));
          } else {
            this.sendcodeDisabled = true;
            this.sendCodeAgainTime = 60;
            clearInterval(this.sendCodeAgain);
            this.$Modal.error({
              title: 'ERROR',
              content: `${res.msg}`
            });
          }
        })
        .catch((res) => {
          this.sendcodeDisabled = true;
          this.sendCodeAgainTime = 60;
          clearInterval(this.sendCodeAgain);
          this.$Modal.error({
            title: 'ERROR',
            content: `${res.data.msg}`
          });
        });
    },
    handleCheckPasswordAgain() {
      this.pwConfirm = Boolean(this.password && this.password === this.passwordAgain);
    },
    updateDingDingConfigs() {
      const list = [];

      for (const key in this.systemForm) {
        if (key !== 'EMAIL_PASSWORD_KEY') {
          list.push({
            configName: this.configKeyMap[key],
            configValue: this.systemForm[key]
          });
        } else if (this.systemForm[key]) {
          list.push({
            configName: this.configKeyMap[key],
            configValue: this.systemForm[key]
          });
        }
      }
      list.push({
        configName: this.configKeyMap.EMAIL_FROM_KEY,
        configValue: this.systemForm.EMAIL_USERNAME_KEY
      });
      this.$services.ccSystemConfigUpdate({ data: list }).then((res) => {
        if (res.success) {
          this.listAllConfigs();
          this.$Message.success(this.$t('xiu-gai-cheng-gong'));
          this.editEmail = false;
        }
        // this.ifEdit = false;
      });
    },
    handleFillEmail(value) {
      this.emailList = [];
      if (value.indexOf('@') < 0) {
        this.emailSuffix.map((item) => {
          this.emailList.push(`${value}@${item}`);
          return null;
        });
      }
      this.handleShowStmp();
    },
    handleShowStmp() {
      if (this.systemForm.EMAIL_USERNAME_KEY) {
        const list = this.systemForm.EMAIL_USERNAME_KEY.split('@');

        if (list.length > 1) {
          if (this.emailSuffix.indexOf(list[1]) < 0) {
            this.showSmtp = true;
          } else {
            this.showSmtp = false;
            this.systemForm.EMAIL_HOST_KEY = this.smtpList[list[1]];
            this.systemForm.EMAIL_PORT_KEY = this.smtpPort[list[1]];
          }
        } else {
          this.showSmtp = false;
        }
      } else {
        this.showSmtp = false;
      }
    },
    handleVerifyEmail() {
      this.verifyEmailLoading = true;
      this.$services.rdpUserVerifyMail().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('yan-zheng-you-xiang-fu-wu-qi-cheng-gong'));
        }
        this.verifyEmailLoading = false;
      });
    },
    handleVerifyIm() {
      this.verifyIMLoading = true;
      this.$services.rdpUserVerifyIm().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('yan-zheng-im-gao-jing-cheng-gong'));
        }
        this.verifyIMLoading = false;
      });
    },
    handleShowEdit() {
      this.ifEdit = true;
    },
    handleApplyStToken() {
      this.$services.ccAliyunStsInvalidStsToken().then((res) => {
        if (res.success) {
          this.$services
            .ccAliyunStsApplyStsToken({
              data: {
                userAk: this.aliyunAk,
                userSk: this.aliyunSk
              }
            })
            .then((res1) => {
              if (res1.success) {
                this.$Message.success(this.$t('cao-zuo-cheng-gong'));
                this.aliyunAk = '';
                this.aliyunSk = '';
              }
            });
        }
      });
    },
    handleCleanStToken() {
      this.$services.ccAliyunStsInvalidStsToken().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        }
      });
    },
    handleCheckPassword() {
      this.pwLength = this.password.length >= 8 && this.password.length <= 32;
      this.pwContain = this.password.indexOf(this.userInfo.phone) === -1;

      const pattern = /(?=.*[0-9])(?=.*[a-zA-Z])/;

      this.pwFormat = pattern.test(this.password);
    },
    handleGoLicenseSet(url) {
      window.open(url);
    },
    getLicenseType(key) {
      const value = key.substring(14, key.length - 2);
      const list = value.split(', ');
      const map = {};

      list.map((item) => {
        const kv = item.split('=');

        map[kv[0]] = kv[1];
        return null;
      });
      return Mapping.licenseTypeDefault[map.licenseType];
    },
    getLicenseCount(key) {
      const value = key.substring(14, key.length - 2);
      const list = value.split(', ');
      const map = {};

      list.map((item) => {
        const kv = item.split('=');

        map[kv[0]] = kv[1];
        return null;
      });
      if (map.licenseType === 'INCREMENT_SYNC_DURATION') {
        return this.$t('paramsrowamount-24-tian', [map.amount / 24]);
      }
      return this.$t('mapamount-ci', [map.amount]);
    },
    getLicensePrice(data) {
      let totalPrice = 0;

      Object.keys(data).map((key) => {
        const value = key.substring(14, key.length - 2);
        const list = value.split(', ');
        const map = {};

        list.map((item) => {
          const kv = item.split('=');

          map[kv[0]] = kv[1];
          return null;
        });
        totalPrice = map.price * data[key];
        return null;
      });
      return totalPrice;
    },
    handleEditEmail() {
      this.editEmail = true;
    },
    handleCopy(value) {
      const aux = document.createElement('input');

      aux.setAttribute('value', value);
      document.body.appendChild(aux);
      aux.select();
      document.execCommand('copy');
      document.body.removeChild(aux);

      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    }
  }
};
</script>
<style lang="less">
.user-center-title {
  font-size: 16px;
  line-height: 16px;
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

  .ivu-tabs-nav .ivu-tabs-tab-active {
    color: #0bb9f8;
    font-family: PingFangSC-Semibold, serif;
  }

  .ivu-table th {
    background-color: #f5f5f5;
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
</style>
