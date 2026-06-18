<template>
  <div class="preference-page">
    <div>
      <div style="position: relative">
        <div>
          <user-config-params-edit
            :refresh-loading="loading"
            v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_R')"
            ref="userConfigParamsEdit"
            :canEdit="canEdit"
          >
            <template v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_R')" #actions>
              <Button :loading="loading" type="default" size="small" @click="handleSearch">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
              </Button>
              <Button v-if="myAuth.includes('RDP_PRI_USER_KV_CONF_W')" type="primary" size="small" @click="handleSubmitUserConfig">
                {{ $t('bao-cun') }}
              </Button>
            </template>
          </user-config-params-edit>
        </div>
      </div>
    </div>
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
import UserConfigParamsEdit from '@/views/system/UserConfigParamsEdit';
// import FollowedJobList from '@/views/system/FollowedJobList';
import { mapGetters, mapState } from 'vuex';
import Mapping from '../util';

export default {
  components: { UserConfigParamsEdit },
  data() {
    return {
      loading: false,
      editEmail: false,
      showAKSK: false,
      canEdit: false,
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
      password: '',
      passwordAgain: '',
      systemForm: {
        EMAIL_HOST_KEY: '',
        EMAIL_PORT_KEY: '465',
        EMAIL_USERNAME_KEY: '',
        EMAIL_PASSWORD_KEY: '',
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
        EMAIL_FROM_KEY: 'spring.mail.properties.from'
      },
      editPasswordRule: {
        password: [{ required: true, message: 'The name cannot be empty', trigger: 'blur' }],
        passwordAgain: [{ required: true, message: 'The name cannot be empty', trigger: 'blur' }]
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
    handleCancelEdit() {
      this.password = '';
      this.passwordAgain = '';
      // this.ifEdit = false;
      this.showEditPassword = false;
      this.editEmail = false;
      this.showAKSK = false;
    },
    handleShowFetchAKSK() {
      this.handleConfirmFetchAKSK();
    },
    handleShowResetAKSK() {
      this.handleConfirmSetAKSK();
    },
    handleConfirmFetchAKSK() {
      this.$services
        .rdpUserQueryUserAkSk({
          data: {}
        })
        .then((res) => {
          if (res.success) {
            this.akskInfo = res.data;
            this.showAKSK = true;
          }
        });
    },
    handleConfirmSetAKSK() {
      this.$services
        .rdpUserResetUserAkSk({
          data: {}
        })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('aksk-chong-zhi-cheng-gong'));
          }
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
.preference-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}
</style>
