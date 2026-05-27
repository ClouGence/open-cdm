<template>
  <div class="user-center" style="padding: 16px">
    <Breadcrumb style="margin-bottom: 14px">
      <BreadcrumbItem>{{ $t('ge-ren-zi-liao') }}</BreadcrumbItem>
    </Breadcrumb>
    <Tabs value="profile" :animated="false">
      <TabPane :label="$t('zhang-hu-xin-xi')" name="profile">
        <!-- 个人信息tab内容：原账户信息栏目 -->
        <div>
          <div class="mt-6 border-t border-gray-100">
            <dl class="divide divide-gray-100">
              <div class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('yong-hu-ming') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.username }}
                  <span
                    v-if="userInfo.saasUserStatus === 'SAAS_LOCKED'"
                    class="border border-red-700 border-solid ml-2 text-red-700 bg-red-50 rounded-md px-4 py-2 text-sm font-medium"
                  >
                    {{ $t('yi-suo-ding') }}
                  </span>
                </dd>
              </div>
              <div class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('you-xiang') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.email }}
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="handleUpdateUserInfo('email')" />
                </dd>
              </div>
              <div v-if="userInfo.phone" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('shou-ji-hao') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.phone }}
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="handleUpdateUserInfo('phone')" />
                </dd>
              </div>
              <div v-if="isInternalUser" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('deng-lu-mi-ma') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  *******
                  <Icon class="cursor-pointer text-2xl" type="ios-create-outline" @click="handleShowPassword" />
                </dd>
              </div>
              <div v-if="userInfo.accountType === 'SUB_ACCOUNT'" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('zhang-hao') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.loginAccount }}
                </dd>
              </div>
              <div v-if="userInfo.accountType === 'SUB_ACCOUNT'" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('suo-shu-zhu-zhang-hao') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">{{ userInfo.pusername }}({{ userInfo.pemail }})</dd>
              </div>
              <div v-if="userInfo.organization" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('gong-si-ming-cheng') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.organization }}
                </dd>
              </div>
              <div v-if="userInfo.marketplaceType && userInfo.marketplaceType !== 'NONE'" class="px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0">
                <dt class="text-sm/6 font-medium text-gray-900">{{ $t('yun-shi-chang-lei-xing') }}</dt>
                <dd class="mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0">
                  {{ userInfo.marketplaceType === 'NONE' ? '' : userInfo.marketplaceType }}
                </dd>
              </div>
            </dl>
          </div>
        </div>
      </TabPane>
      <TabPane :label="$t('an-quan')" name="security">
        <div class="mt-8">
          <div>
            <div class="mb-4" style="font-size: 14px; font-weight: bold">
              {{ $t('duo-yin-zi-ren-zheng') }}
              <span v-if="!userInfo.useMfa">{{ $t('wei-kai-qi') }}</span>
              <span v-if="userInfo.useMfa">{{ $t('yi-kai-qi') }}</span>
            </div>
            <div>
              <div>
                <Button v-if="!userInfo.useMfa" style="margin-right: 12px" @click="handleOpenMfaSetting">
                  {{ $t('kai-qi-1') }}
                </Button>
                <Button @click="handleResetMfa" v-if="userInfo.useMfa" type="default" style="margin-right: 12px">
                  {{ $t('chong-zhi') }}
                </Button>
                <Button @click="handleShowCloseMf" v-if="userInfo.useMfa" type="error" ghost>{{ $t('guan-bi') }}</Button>
              </div>
              <p class="mt-4 max-w-xl">
                {{
                  $t(
                    'wei-nin-de-zhang-hao-zeng-jia-yi-ceng-bao-zhang-kai-qi-hou-nin-xu-yao-zai-deng-lu-shi-ti-gong-zhang-hao-mi-ma-yi-ji-mfa-yan-zheng-ma'
                  )
                }}
              </p>
            </div>
          </div>
          <!-- aksk管理栏目 -->
          <div v-if="userInfo.accountType === 'PRIMARY_ACCOUNT'">
            <div class="mt-12 mb-4" style="font-size: 14px; font-weight: bold">
              {{ $t('aksk-guan-li') }}
            </div>
            <div style="margin-bottom: 24px">
              <Button style="font-size: 14px; margin-right: 16px" @click="handleShowFetchAKSK">
                {{ $t('huo-qu-aksk') }}
              </Button>
              <Button style="font-size: 14px; margin-right: 16px" @click="handleShowResetAKSK">
                {{ $t('chong-zhi-aksk') }}
              </Button>
            </div>
          </div>
          <!-- 新增：阿里云访问授权栏目 -->
          <div v-if="myCatLog.includes('CAT_RDP_PRI_THIRD_PARTY_CONF')">
            <div class="mt-12 mb-4" style="font-size: 14px; font-weight: bold">
              {{ $t('a-li-yun-fang-wen-quan-xian') }}
            </div>
            <Button class="mr-4" @click="showAliyunModal = true">{{ $t('shou-quan-fang-wen') }}</Button>
            <Button @click="handleShowAliyunCleanConfirm">{{ $t('jie-chu-fang-wen-quan-xian') }}</Button>
          </div>
        </div>
      </TabPane>
    </Tabs>
    <CCModal v-model="showMfaModal" :title="$t('kai-qi-duo-yin-zi-ren-zheng')" width="400px">
      <div v-if="mfaModalLoading" style="text-align: center; padding: 40px 0">
        <i class="ivu-icon ivu-icon-ios-loading ivu-load-loop" style="font-size: 32px"></i>
      </div>
      <div v-else>
        <p class="mt-4 mb-8">
          {{
            $t(
              'shi-yong-nin-de-an-quan-yan-zheng-app-lai-sao-miao-yi-xia-er-wei-ma-bing-jiang-ta-ti-gong-de-yi-ci-xing-de-yan-zheng-ma-tian-ru-xia-mian-de-shu-ru-kuang'
            )
          }}
        </p>
        <div
          class="border-b border-solid border-[#dcdee2] pb-8"
          style="display: flex; justify-content: center; align-items: center; margin-bottom: 16px; min-height: 180px"
        >
          <img
            :src="mfaQrCode"
            class="border border-solid border-[#dcdee2] rounded"
            style="width: 180px; height: 180px; display: block"
            v-if="mfaQrCode"
          />
        </div>
        <div class="mt-8">
          <Input v-model="mfaInput" :placeholder="$t('qing-shu-ru-yi-ci-xing-de-duo-yin-zi-ren-zheng-ma')" />
          <Alert class="mt-4" type="error" v-if="errMsg" show-icon>{{ errMsg }}</Alert>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCloseMfaModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleConfirmMfaModal">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="showAliyunModal" :title="$t('a-li-yun-fang-wen-quan-xian')" width="500px">
      <div v-if="showAliyunModal">
        <Alert type="warning" show-icon>
          <p>
            {{ $t('a-li-yun-aksk-xu-yao-fu-yu-bi-yao-de-quan-xian') }}
            <a :href="`${store.state.docUrlPrefix}/reference/rds_mysql_ram_least_privilege`" target="_blank">
              {{ $t('ru-he-fu-quan') }}
            </a>
          </p>
          <!--          <p style="margin-top: 8px">{{ $t('a-li-yun-aksk-jin-bao-cun-2-xiao-shi-guo-qi-hou-zi-dong-shan-chu') }}</p>-->
        </Alert>
        <Form :model="aliyunForm" :rules="aliyunFormValidate" ref="aliyunForm" :label-width="160">
          <FormItem label="AccessKey ID" prop="aliyunAk">
            <Input v-model="aliyunForm.aliyunAk" />
          </FormItem>
          <FormItem label="AccessKey Secret" prop="aliyunSk">
            <Input v-model="aliyunForm.aliyunSk" />
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleAliyunApplyStToken">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <verify-code-modal
      v-model:visible="showEditUserInfo"
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
      :phone-number="updateUserInfo?.phone"
      :email="updateUserInfo?.email"
      :new-email="newEmail"
      ref="clear-position-modal"
      :width="500"
      withMargin
    >
      <template #content>
        <Form label-position="right" :label-width="80">
          <FormItem :label="$t('shou-ji-hao-0')" v-if="updateUserInfoType === 'phone' && !isVerifyPhone">
            <Input v-model="updateUserInfo.phone" />
          </FormItem>
          <FormItem
            :label="updateUserInfoType === `phone` ? $t('yuan-shou-ji-hao') : $t('shou-ji-hao-0')"
            prop="phone"
            v-if="(isVerifyPhone || updateUserInfoType !== 'phone') && userInfo.phone"
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
      v-model:visible="showFetchAKSK"
      :title="$t('huo-qu-aksk')"
      verify-code-type="FETCH_USER_AK_SK"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmFetchAKSK"
      :loading="confirmFetchLoading"
      ref="clear-position-modal"
      :width="580"
    >
      <template #content>
        <h3 style="margin-bottom: 20px">
          {{ $t('wei-bao-zheng-an-quan-xing-qing-shu-ru-yan-zheng-ma-lai-huo-qu-aksk') }}
        </h3>
      </template>
    </verify-code-modal>
    <verify-code-modal
      v-model:visible="showResetAKSK"
      :title="$t('chong-zhi-aksk')"
      verify-code-type="RESET_USER_AK_SK"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmSetAKSK"
      :loading="confirmFetchLoading"
      ref="clear-position-modal"
      :width="580"
    >
      <template #content>
        <h3 style="margin-bottom: 20px">
          {{ $t('wei-bao-zheng-an-quan-xing-qing-shu-ru-yan-zheng-ma-lai-chong-zhi-aksk') }}
        </h3>
      </template>
    </verify-code-modal>
    <verify-mfa-modal
      v-model:visible="showCloseMfaModal"
      :title="$t('guan-bi-duo-yin-zi-ren-zheng')"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmCloseMfa"
      :loading="mfaModalLoading"
      :err-msgContent="errMsg"
      ref="close-mfa-modal"
    >
      <p class="mb-4">
        {{
          $t(
            'guan-bi-duo-yin-zi-ren-zheng-hou-nin-de-zhang-hao-jiang-shao-yi-ceng-bao-hu-ru-que-ren-yao-guan-bi-qing-shu-ru-duo-yin-zi-yan-zheng-ma-hou-dian-ji-que-ding'
          )
        }}
      </p>
    </verify-mfa-modal>
    <verify-mfa-modal
      v-model:visible="showResetMfaModal"
      :has-next-step="!hasConfirmReset"
      :title="$t('chong-zhi-duo-yin-zi-ren-zheng')"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmResetMfa"
      :loading="mfaModalLoading"
      :err-msgContent="errMsg"
    >
      <div>
        <p class="mb-4">
          {{ $t('qing-shu-ru-duo-yin-zi-yan-zheng-ma-yi-chong-zhi-duo-yin-zi-ren-zheng-pei-zhi') }}
        </p>
        <div v-if="hasConfirmReset">
          <div
            class="border-b border-solid border-[#dcdee2] pb-8"
            style="display: flex; justify-content: center; align-items: center; margin-bottom: 16px; min-height: 180px"
          >
            <img
              :src="mfaQrCode"
              class="border border-solid border-[#dcdee2] rounded"
              style="width: 180px; height: 180px; display: block"
              v-if="mfaQrCode"
            />
          </div>
        </div>
      </div>
    </verify-mfa-modal>
    <password-confirm-modal
      v-model:visible="showEditPassword"
      :title="$t('chong-zhi-mi-ma')"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmEditPassword"
      ref="clear-position-modal"
      :width="500"
      :err-msgContent="errMsg"
    >
      <template #content>
        <FormItem :label="$t('xin-mi-ma')">
          <Poptip trigger="focus" placement="right-start" style="width: 100%">
            <Input style="width: 100%" v-model="newPassword" type="password" password :placeholder="$t('qing-shu-ru-xin-mi-ma')"></Input>
            <template #content>
              <p>{{ passwordRule.tips }}</p>
            </template>
          </Poptip>
        </FormItem>
      </template>
    </password-confirm-modal>
    <password-confirm-modal
      :visible="showEditEmail"
      :title="$t('xiu-gai-you-xiang')"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmEditEmail"
      ref="clear-position-modal"
      :width="500"
      :err-msgContent="errMsg"
      :label="$t('mi-ma')"
      :placeholder="$t('xu-yan-zheng-cao-zuo-ren-shen-fen')"
    >
      <template #content>
        <FormItem :label="$t('xin-you-xiang')">
          <Input style="width: 100%" v-model="newEmailToReset" :placeholder="$t('qing-shu-ru-xin-you-xiang')"></Input>
        </FormItem>
      </template>
    </password-confirm-modal>
    <password-confirm-modal
      :visible="showEditPhone"
      :title="$t('xiu-gai-shou-ji-hao')"
      :handle-close-modal="handleCancelEdit"
      :handle-confirm-callback="handleConfirmEditPhone"
      ref="clear-position-modal"
      :width="500"
      :err-msgContent="errMsg"
      :label="$t('mi-ma')"
      :placeholder="$t('xu-yan-zheng-cao-zuo-ren-shen-fen')"
    >
      <template #content>
        <FormItem :label="$t('xin-shou-ji-hao')">
          <Input style="width: 100%" v-model="newPhoneToReset" :placeholder="$t('qing-shu-ru-xin-shou-ji-hao')"></Input>
        </FormItem>
      </template>
    </password-confirm-modal>
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
import fecha from 'fecha';
import { formatHour, isNumber } from '@/components/util';
import VerifyCodeModal from '@/components/modal/VerifyCodeModal';
import PasswordConfirmModal from '@/components/modal/PasswordConfirmModal';
import verifyMfaModal from '@/components/modal/VerifyMfaModal';
import { mapGetters, mapMutations, mapState } from 'vuex';
import { encryptMixin } from '@/mixins/encryptMixin';
import { UPDATE_USERINFO } from '@/store/mutationTypes';
import store from '@/store';
import Mapping from '../util';

export default {
  components: { VerifyCodeModal, PasswordConfirmModal, verifyMfaModal },
  mixins: [encryptMixin],
  data() {
    return {
      store,
      updateUserInfoType: '',
      loading: false,
      editEmail: false,
      isVerifyPhone: false,
      isVerifyEmail: false,
      newPhone: false,
      newEmail: false,
      newEmailToReset: '',
      newPhoneToReset: '',
      showEditUserInfo: false,
      showFetchAKSK: false,
      showAKSK: false,
      confirmFetchLoading: false,
      akskInfo: {},
      newPassword: '',
      passwordRule: {
        expr: '',
        tips: ''
      },
      errMsg: '',
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
      showEditEmail: false,
      showEditPhone: false,
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
      userConfigs: {},
      showResetAKSK: false, // 新增
      aliyunForm: {
        aliyunAk: '',
        aliyunSk: ''
      },
      aliyunFormValidate: {
        aliyunAk: [
          {
            required: true,
            message: this.$t('a-li-yun-ak-bu-neng-wei-kong')
          }
        ],
        aliyunSk: [
          {
            required: true,
            message: this.$t('a-li-yun-sk-bu-neng-wei-kong')
          }
        ]
      },
      showMfaModal: false,
      showCloseMfaModal: false,
      showResetMfaModal: false,
      mfaQrCode: '',
      mfaInput: '',
      mfaModalLoading: false,
      hasConfirmReset: false,
      showAliyunModal: false,
      showAliyunCleanConfirm: false
    };
  },
  created() {
    if (this.userInfo.accountType === 'PRIMARY_ACCOUNT') {
      this.$services.rdpUserGetPrimaryAccountPwdValidateExpr().then((res) => {
        if (res.success) {
          console.log('res', res);
          this.passwordRule.tips = res.data.tips;
          this.passwordRule.expr = res.data.expr;
        }
      });
    } else {
      this.$services.rdpUserGetSubAccountPwdValidateExpr().then((res) => {
        if (res.success) {
          console.log('res', res);
          this.passwordRule.tips = res.data.tips;
          this.passwordRule.expr = res.data.expr;
        }
      });
    }
  },
  async mounted() {
    // this.listAllConfigs();
    await this.getConfigValueList();
  },
  computed: {
    ...mapGetters(['verifyType', 'isInternalUser']),
    ...mapState(['userInfo', 'globalSetting', 'myAuth', 'myCatLog'])
  },
  methods: {
    ...mapMutations([UPDATE_USERINFO]),
    async getConfigValueList() {
      const res = await this.$services.rdpUserConfigGetUserSpecifiedConfs({
        data: {
          configNames: ['subAccountPwdVerifyTips', 'subAccountPwdVerifyExpr']
        }
      });
      if (res.success && res.data) {
        let subAccountPwdVerifyTips = '';
        let subAccountPwdVerifyExpr = '';

        if (res.data.subAccountPwdVerifyTips) {
          subAccountPwdVerifyTips = res.data.subAccountPwdVerifyTips.configValue;
        } else if (res.data.subAccountPwdVerifyExpr) {
          subAccountPwdVerifyExpr = res.data.subAccountPwdVerifyExpr.configValue;
        }
        if (subAccountPwdVerifyExpr && subAccountPwdVerifyTips) {
          this.passwordRule.tips = subAccountPwdVerifyTips;
          this.passwordRule.expr = subAccountPwdVerifyExpr;
        }
      }
    },
    async handleOpenMfaSetting() {
      this.mfaModalLoading = true;
      try {
        const openRes = await this.$services.rdpMfaCtrlInitMfaSetting();
        if (!openRes.success) {
          return;
        }
        const res = await this.$services.rdpMfaInitMfaSetting({ responseType: 'blob', modal: false });
        console.log('res mfa', res, res.data);
        if (res) {
          const blob = new Blob([res], { type: 'image/png' });
          this.mfaQrCode = URL.createObjectURL(blob);
          this.showMfaModal = true;
        }
      } finally {
        this.mfaModalLoading = false;
      }
    },
    handleCloseMfaModal() {
      this.showMfaModal = false;
      this.mfaQrCode = '';
      this.mfaInput = '';
    },
    handleResetMfa() {
      this.showResetMfaModal = true;
    },
    async handleConfirmMfaModal() {
      if (!this.mfaInput || !isNumber(this.mfaInput)) {
        this.errMsg = this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma');
        return;
      }
      // 这里预留后续确认逻辑
      this.mfaModalLoading = true;
      const res = await this.$services.rdpMfaConfirmInitMfaSetting({
        data: {
          mfaCode: this.mfaInput
        }
      });
      this.errMsg = '';
      if (res?.success) {
        this.$Message.success(this.$t('kai-qi-cheng-gong'));
        this.$store.dispatch('getUserInfo');
        this.handleCloseMfaModal();
      }
      this.mfaModalLoading = false;
    },
    handleShowPassword() {
      this.showEditPassword = true;
      // window.location.reload();
    },
    handleUpdateUserInfo(type) {
      if (this.globalSetting.onPremiseDeployMode) {
        if (type === 'email') {
          this.showEditEmail = true;
        } else if (type === 'phone') {
          this.showEditPhone = true;
        }
      } else {
        this.updateUserInfoType = type;
        this.showEditUserInfo = true;
        this.updateUserInfo.phone = this.userInfo.phone;
        this.updateUserInfo.email = this.userInfo.email;
        if (type === 'phone') {
          this.isVerifyPhone = true;
        }
      }
    },
    handleCancelEdit() {
      this.verifyCode = '';
      this.password = '';
      this.passwordAgain = '';
      this.newPassword = '';
      // this.ifEdit = false;
      this.newEmailToReset = '';
      this.newPhoneToReset = '';
      this.errMsg = '';
      this.showEditPassword = false;
      this.editEmail = false;
      this.showEditUserInfo = false;
      this.showFetchAKSK = false;
      this.showAKSK = false;
      this.isVerifyPhone = false;
      this.isVerifyEmail = false;
      this.showEditEmail = false;
      this.showEditPhone = false;
      this.showResetAKSK = false;
      this.showCloseMfaModal = false;
      this.showResetMfaModal = false;
      this.hasConfirmReset = false;
      this.mfaQrCode = '';
      this.showAliyunModal = false;
    },
    handleShowFetchAKSK() {
      this.showFetchAKSK = true;
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
          if (res.success) {
            this.showFetchAKSK = false;
            this.akskInfo = res.data;
            this.showAKSK = true;
          }
          this.confirmFetchLoading = false;
        });
    },
    handleShowResetAKSK() {
      this.showResetAKSK = true;
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
    async handleConfirmCloseMfa(mfaCode) {
      if (!mfaCode || !isNumber(mfaCode)) {
        this.errMsg = this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma');
        return;
      }
      this.mfaModalLoading = true;
      const res = await this.$services.rdpMfaCloseMfaSettings({
        data: {
          mfaCode
        }
      });
      this.errMsg = '';
      if (res.success) {
        this.$Message.success(this.$t('guan-bi-cheng-gong'));
        this.$store.dispatch('getUserInfo');
        this.showCloseMfaModal = false;
      }
      this.mfaModalLoading = false;
    },
    async handleConfirmResetMfa(mfaCode) {
      if (!mfaCode || !isNumber(mfaCode)) {
        this.errMsg = this.$t('qing-shu-ru-zheng-que-de-yan-zheng-ma');
        return;
      }
      this.mfaModalLoading = true;
      if (this.hasConfirmReset) {
        const res = await this.$services.rdpMfaConfirmResetMfaSetting({
          data: {
            mfaCode
          }
        });
        this.errMsg = '';
        if (res.success) {
          this.$Message.success(this.$t('chong-zhi-duo-yin-zi-ren-zheng-cheng-gong'));
          this.$store.dispatch('getUserInfo');
          this.showResetMfaModal = false;
          this.hasConfirmReset = false;
        }
        this.mfaModalLoading = false;
      } else {
        const res = await this.$services.rdpMfaResetMfaSetting({
          data: {
            mfaCode
          },
          responseType: 'blob'
        });
        this.errMsg = '';
        if (res.fail) {
          this.errMsg = res.msgContent;
        } else {
          const blob = new Blob([res], { type: 'image/png' });
          this.mfaQrCode = URL.createObjectURL(blob);
          this.hasConfirmReset = true;
        }
        this.mfaModalLoading = false;
      }
    },
    handleConfirmEditPassword(originPassword) {
      const regExp = new RegExp(this.passwordRule.expr);
      if (regExp.test(this.newPassword)) {
        this.$services
          .rdpUserResetPwdWithOriginPwd({
            data: {
              originPassword: this.passwordEncrypt(originPassword),
              newPassword: this.passwordEncrypt(this.newPassword)
            },
            modal: false
          })
          .then((res) => {
            if (res.success) {
              this.showEditPassword = false;
              this.newPassword = '';
              this.$router.push({ path: '/login' });
            } else {
              this.errMsg = res.msgContent;
            }
          });
      } else {
        this.errMsg = this.passwordRule.tips;
      }
    },
    handleConfirmEditEmail(originPassword) {
      this.$services
        .rdpUserUpdateUserEmailWithPwd({
          data: {
            password: this.passwordEncrypt(originPassword),
            email: this.newEmailToReset
          },
          modal: false
        })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('you-xiang-xiu-gai-cheng-gong'));
            this.showEditEmail = false;
            this[UPDATE_USERINFO]({ email: this.newEmailToReset });
            this.newEmailToReset = '';
          } else {
            this.errMsg = res.msgContent;
          }
        });
    },
    handleConfirmEditPhone(originPassword) {
      this.$services
        .rdpUserUpdateUserPhoneWithPwd({
          data: {
            password: this.passwordEncrypt(originPassword),
            phone: this.newPhoneToReset
          },
          modal: false
        })
        .then((res) => {
          if (res.success) {
            this.$Message.success(this.$t('shou-ji-xiu-gai-cheng-gong'));
            this.showEditPhone = false;
            this[UPDATE_USERINFO]({ phone: this.newPhoneToReset });
            this.newPhoneToReset = '';
          } else {
            this.errMsg = res.msgContent;
          }
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
            this.$store.dispatch('getUserInfo');
          }
        });
      }
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
      this.$services.rdpUserVerifyMail().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('yan-zheng-you-xiang-fu-wu-qi-cheng-gong'));
        }
      });
    },
    handleVerifyIm() {
      this.$services.rdpUserVerifyIm().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('yan-zheng-im-gao-jing-cheng-gong'));
        }
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
              data: this.aliyunForm
            })
            .then((res1) => {
              if (res1.success) {
                this.$Message.success(this.$t('cao-zuo-cheng-gong'));
                this.aliyunForm.aliyunAk = '';
                this.aliyunForm.aliyunSk = '';
                this.handleCancelEdit();
              }
            });
        }
      });
    },
    handleCleanStToken() {
      this.$services.ccAliyunStsInvalidStsToken().then((res) => {
        if (res.success) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.aliyunForm.aliyunAk = '';
          this.aliyunForm.aliyunSk = '';
        }
      });
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
    handleCopy(value) {
      const aux = document.createElement('input');

      aux.setAttribute('value', value);
      document.body.appendChild(aux);
      aux.select();
      document.execCommand('copy');
      document.body.removeChild(aux);

      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleShowCloseMf() {
      this.showCloseMfaModal = true;
    },
    handleShowAliyunCleanConfirm() {
      this.$Modal.confirm({
        title: this.$t('qing-chu-a-li-yun-fang-wen-quan-xian'),
        content: this.$t('ni-que-ding-yao-qing-chu-a-li-yun-fang-wen-quan-xian-ma'),
        onOk: () => {
          this.handleAliyunCleanStToken();
        }
      });
    }
  }
};
</script>
<style lang="less">
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
  padding: 16px;

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

.divide > * {
  border-bottom: 1px solid rgb(243, 244, 246);
}

// 暗色模式适配
[data-theme='dark'] {
  // 页面容器背景
  .user-center {
    background-color: var(--bg-primary) !important;

    .ivu-table th {
      background-color: var(--bg-tertiary) !important;
    }
  }

  // Tailwind CSS 颜色类覆盖
  .border-gray-100 {
    border-color: var(--border-primary) !important;
  }

  .text-gray-900 {
    color: var(--text-primary) !important;
  }

  .text-gray-700 {
    color: var(--text-secondary) !important;
  }

  // 个人信息列表样式
  .divide > * {
    border-bottom-color: var(--border-primary) !important;
  }

  // 自定义样式适配
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

  // 锁定状态标签样式
  .border-red-700 {
    border-color: var(--error-color) !important;
  }

  .text-red-700 {
    color: var(--error-color) !important;
  }

  .bg-red-50 {
    background-color: var(--error-bg) !important;
  }

  // 面包屑导航
  .ivu-breadcrumb {
    color: var(--text-secondary) !important;

    a {
      color: var(--text-secondary) !important;

      &:hover {
        color: var(--primary-color) !important;
      }
    }
  }

  // 标签页内容区域
  .ivu-tabs-content {
    background-color: var(--bg-primary) !important;
  }

  // Icon 组件颜色
  .ivu-icon {
    color: var(--text-secondary) !important;

    &:hover {
      color: var(--primary-color) !important;
    }
  }

  // 按钮样式（确保按钮在暗色模式下正确显示）
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

  // 表单输入框
  .ivu-input {
    background-color: var(--bg-tertiary) !important;
    border-color: var(--border-primary) !important;
    color: var(--text-primary) !important;

    &[disabled] {
      background-color: var(--disabled-bg) !important;
      color: var(--text-disabled) !important;
    }
  }

  // 表单标签
  .ivu-form-item-label {
    color: var(--text-primary) !important;
  }

  // 警告提示框
  .ivu-alert {
    background-color: var(--bg-tertiary) !important;
    border-color: var(--border-primary) !important;
    color: var(--text-primary) !important;
  }

  // 模态框（已在全局样式中处理，这里确保覆盖）
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

  // 安全标签页中的标题样式
  h3 {
    color: var(--text-primary) !important;
  }

  // 段落文字
  p {
    color: var(--text-primary) !important;
  }
}
.ivu-poptip-rel {
  width: 100% !important;
}
</style>
