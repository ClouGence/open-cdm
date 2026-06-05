<template>
  <div class="license">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/system/order">{{ $t('xu-ke-zheng') }}</BreadcrumbItem>
            <BreadcrumbItem>{{ $t('chuang-jian-xu-ke-zheng') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div>
            <!-- 步骤1内容 -->
            <div :class="`${isDark ? 'p-2 mb-4 license-step' : 'bg-gray-200 p-2 mb-4'}`">
              <h3 class="text-lg font-semibold">{{ $t('di-yi-bu-huo-qu-shen-qing-ma') }}</h3>
            </div>
            <div class="p-2 mb-4">
              <a class="underline hover:underline italic" :href="`${store.state.dmDocUrlPrefix}/license/license_use`" target="_blank">
                {{ $t('ru-he-huo-qu-ni-de-shen-qing-ma') }}
              </a>
            </div>
            <div :class="`${isDark ? 'p-2 mb-4 license-step' : 'bg-gray-200 p-2 mb-4'}`">
              <h3 class="text-lg font-semibold">{{ $t('di-er-bu-shu-ru-shen-qing-ma') }}</h3>
            </div>
            <div class="mb-8">
              <Input
                v-model="license.applyCode"
                type="textarea"
                :rows="4"
                :placeholder="$t('shu-ru-xu-ke-zheng')"
                :class="`border-2 border-gray-200 rounded-lg focus:border-blue-500 ${inputStatus === 'error' ? 'input-error' : ''}`"
                @on-change="handleChangeApplyCode"
              />
              <div v-if="errorMessage" class="error-tip" v-html="errorMessage"></div>
            </div>
            <div class="mb-8" v-if="license.oldResource?.licenseStartTimeMs">
              <div class="mb-4">
                <h4>{{ $t('nin-de-dang-qian-xu-ke-zheng-zi-yuan') }}</h4>
              </div>
              <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                <Table :columns="oldResourcesColumns" :data="oldResourceData" size="small">
                  <template #licenseRemainingTimeMs="{ row }">
                    {{ Math.floor(row.licenseRemainingTimeMs / 1000 / 60 / 60 / 24) }}
                    {{ $t('tian-0') }}
                    {{ Math.floor((row.licenseRemainingTimeMs / 1000 / 60 / 60) % 24) }}{{ $t('xiao-shi-0') }}
                    {{ Math.floor((row.licenseRemainingTimeMs / 1000 / 60) % 60) }}{{ $t('fen') }}
                  </template>
                  <template #authedTimeRange="{ row }">
                    {{ dayjs(row.licenseStartTimeMs).format('YYYY-MM-DD HH:mm:ss') }} -
                    {{ dayjs(row.licenseEndTimeMs).format('YYYY-MM-DD HH:mm:ss') }}
                  </template>
                </Table>
              </div>
            </div>
            <div :class="`${isDark ? 'p-2 mb-4 license-step' : 'bg-gray-200 p-2 mb-4'}`">
              <h3 class="text-lg font-semibold">{{ $t('di-san-bu-xuan-ze-pei-zhi') }}</h3>
            </div>
            <div class="p-2 mb-4">
              <div>
                <div>
                  <div class="flex items-center mb-6">
                    <span class="font-medium w-40">{{ $t('chan-pin-lei-xing-0') }}</span>
                    <RadioGroup v-model="license.productType" type="button" @on-change="handleChangeProductType">
                      <Radio v-for="productType in productTypeList" :key="productType" :label="productType"></Radio>
                    </RadioGroup>
                  </div>
                  <div class="mb-6 flex items-center">
                    <span class="font-medium w-40">{{ $t('ban-ben-xuan-ze') }}</span>
                    <RadioGroup v-model="license.buyType" type="button" @on-change="handleChangeEdition">
                      <Radio v-for="buyType in buyTypeList" :disabled="disabledEdition(buyType)" :key="buyType" :label="buyType">
                        {{ buyTypeI18n[buyType] }}
                      </Radio>
                    </RadioGroup>
                    <Tooltip placement="right-start">
                      <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
                      <template #content>
                        {{ $t('ru-xu-shang-ye-ban-qing') }}
                        <a href="https://www.clougence.com/about">{{ $t('lian-xi-wo-men') }}</a>
                      </template>
                    </Tooltip>
                  </div>
                  <div class="mb-6 flex items-center" v-if="license.productType !== 'CloudDM'">
                    <span class="font-medium w-40">{{ $t('lian-lu-shu') }}</span>
                    <RadioGroup v-if="license.buyType === 'COMMUNITY'" v-model="license.buyTaskCount" type="button">
                      <Radio :label="configObj.FREE_TASK_COUNT_LIMIT">
                        {{ configObj.FREE_TASK_COUNT_LIMIT }}
                      </Radio>
                    </RadioGroup>
                    <RadioGroup v-if="license.buyType === 'POC'" v-model="license.buyTaskCount" type="button">
                      <Radio :label="configObj.POC_TASK_COUNT_LIMIT">
                        {{ configObj.POC_TASK_COUNT_LIMIT === '-1' ? $t('wu-xian-zhi') : configObj.POC_TASK_COUNT_LIMIT }}
                      </Radio>
                    </RadioGroup>
                    <vue-number-input
                      v-model="license.buyTaskCount"
                      v-if="license.buyType === 'FLAGSHIP'"
                      :disabled="license.oldResource.taskMaxThan || (isPreFLAGSHIP && isOffline)"
                      @change="handleGetFinalConfig"
                      :max="parseInt(configObj.BUY_MAX_TASK_COUNT_LIMIT)"
                      :min="parseInt(configObj.BUY_MIN_TASK_COUNT_LIMIT)"
                      inline
                      controls
                      center
                    />
                    <Tooltip placement="right-start">
                      <CustomIcon type="icon-v2-HelpCircle" hoverStyle leftMargin size="16px" />
                      <template #content>
                        <a
                          class="underline hover:underline italic"
                          :href="`${store.state.docUrlPrefix}/reference/service_difference`"
                          target="_blank"
                        >
                          {{ $t('what-is-link') }}
                        </a>
                      </template>
                    </Tooltip>
                  </div>
                  <div class="mb-6 flex items-center">
                    <span class="font-medium w-40">{{ $t('you-xiao-qi') }}</span>
                    <RadioGroup
                      v-if="license.buyType === 'FLAGSHIP'"
                      v-model="license.buyTimeStandardId"
                      type="button"
                      @on-change="handleGetFinalConfig"
                    >
                      <Radio
                        v-for="time in buyTimeStandardList"
                        :label="time.id"
                        :key="time.id"
                        class="month"
                        :disabled="license.oldResource.taskMaxThan"
                      >
                        <span class="mr-2">{{ time.buyTimeValue }}</span>
                        <span>{{ time.buyTimeType }}</span>
                      </Radio>
                    </RadioGroup>
                    <RadioGroup v-if="license.buyType === 'POC'" v-model="license.buyTimeStandardId" type="button" @on-change="handleGetFinalConfig">
                      <Radio
                        v-for="time in pocBuyTimeStandardList"
                        :label="time.id"
                        :key="time.id"
                        class="month"
                        :disabled="license.oldResource.taskMaxThan"
                      >
                        <span class="mr-2">{{ time.buyTimeValue }}</span>
                        <span>{{ time.buyTimeType }}</span>
                      </Radio>
                    </RadioGroup>
                    <RadioGroup
                      v-if="license.buyType === 'COMMUNITY'"
                      v-model="license.buyTimeStandardId"
                      type="button"
                      @on-change="handleGetFinalConfig"
                    >
                      <Radio
                        v-for="time in communityBuyTimeStandardList"
                        :label="time.id"
                        :key="time.id"
                        class="month"
                        :disabled="license.oldResource.taskMaxThan"
                      >
                        <span class="mr-2">{{ time.buyTimeValue }}</span>
                        <span>{{ time.buyTimeType }}</span>
                      </Radio>
                    </RadioGroup>
                  </div>
                  <div class="mt-8">
                    <div class="mb-4" :class="{ 'license-resource': isDark }">
                      <h4>{{ $t('zui-zhong-de-xu-ke-zheng-zi-yuan') }}</h4>
                    </div>
                    <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                      <Table :columns="getFinalResourceColumns" :data="finalResourceData" size="small">
                        <template #finalTaskCount="{ row }">
                          {{ row.finalTaskCount === '-1' ? $t('wu-xian') : row.finalTaskCount }}
                        </template>
                        <template #licenseRemainingTimeMs="{ row }">
                          <span v-if="row.restDays">{{ row.restDays }} {{ $t('tian-0') }}</span>
                          <span v-if="row.restHours">{{ row.restHours }} {{ $t('xiao-shi-0') }}</span>
                        </template>
                        <template #authedTimeRange="{ row }">
                          <span v-if="license.applyCode && !license.oldResource.taskMaxThan">
                            {{ dayjs(new Date()).format('YYYY-MM-DD HH:mm:ss') }} -
                            {{ dayjs(new Date(row.day)).format('YYYY-MM-DD HH:mm:ss') }}
                          </span>
                        </template>
                      </Table>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 支付操作栏 -->
            <Card class="bg-gray-50 mt-24">
              <div class="space-y-3">
                <!-- 价格明细 -->
                <div class="flex justify-start">
                  <span class="text-gray-600 w-32">{{ $t('yuan-shi-jia-ge') }}</span>
                  <span v-if="license.priceData.originalPrice || license.priceData.originalPrice === 0" class="font-medium w-48 text-right">
                    {{ license.priceData.originalPrice }}{{ license.priceData.currencyI18n }}
                  </span>
                </div>

                <!-- 剩余金额 -->
                <div v-if="license.priceData.restPrice" class="flex justify-start text-green-600">
                  <span class="text-gray-600 w-32">{{ $t('sheng-yu-jin-e') }}</span>
                  <span class="w-48 text-right">-{{ license.priceData.restPrice }}{{ license.priceData.currencyI18n }}</span>
                </div>

                <!-- 折扣 -->
                <div v-if="license.priceData.discountSavedMoney" class="flex justify-start text-green-600">
                  <span class="text-gray-600 w-32">{{ $t('zhe-kou') }}</span>
                  <span class="w-48 text-right">-{{ license.priceData.discountSavedMoney }}{{ license.priceData.currencyI18n }}</span>
                </div>

                <!-- 代金券优惠 -->
                <div v-if="license.priceData.voucherSavedMoney" class="flex justify-start text-green-600">
                  <span class="text-gray-600 w-32">{{ $t('you-hui-quan') }}</span>
                  <span class="w-48 text-right">-{{ license.priceData.voucherSavedMoney }}{{ license.priceData.currencyI18n }}</span>
                </div>

                <!-- 分割线 -->
                <Divider class="my-4" />

                <!-- 最终价格 -->
                <div class="flex justify-start items-center" v-if="license.priceData.isPositiveNumber">
                  <div class="w-32">
                    <h3 class="text-lg font-medium text-gray-700">{{ $t('zui-zhong-jia-ge') }}</h3>
                  </div>
                  <div class="text-right w-48" v-if="license.priceData.price || license.priceData.price === 0">
                    <p class="text-2xl font-bold text-blue-600">{{ license.priceData.price }}{{ license.priceData.currencyI18n }}</p>
                  </div>
                </div>
                <div class="flex justify-start items-center" v-if="!license.priceData.isPositiveNumber">
                  <div class="text-left w-full" v-if="license.priceData.price || license.priceData.price === 0">
                    <p class="text-lg font-bold text-red-600">
                      {{ $t('nin-de-sheng-yu-zi-yuan-guo-duo-qing-xuan-ze-geng-gao-gui-ge') }}
                    </p>
                  </div>
                </div>

                <div class="py-2">
                  <RadioGroup v-model="selectedPayType">
                    <Radio v-for="payType in payTypeList" :key="payType.payType" :label="payType.payType">
                      {{ payType.payTypeI18n }}
                      <CustomIcon :type="`icon-v2-${payType.payType}`" left-margin />
                    </Radio>
                  </RadioGroup>
                </div>

                <!-- 支付按钮 -->
                <Button
                  type="primary"
                  icon="md-card"
                  class="w-full mt-4 text-lg"
                  :disabled="hasPay || (!license.priceData.price && license.priceData.price !== 0) || !license.priceData.isPositiveNumber"
                  :loading="payLoading"
                  @click="handlePayment"
                >
                  <span class="text-lg">{{ $t('qu-fu-kuan') }}</span>
                </Button>
              </div>
            </Card>
          </div>
        </div>
      </div>
    </div>
    <pay-confirm-modal :show-pay-confirm="showPaymentConfirm" :handle-click-pay="handlePayment" :handle-confirm-finish="handleConfirmFinish" />
  </div>
</template>
<script>
import VueNumberInput from '@chenfengyuan/vue-number-input/dist/vue-number-input';
import { mapGetters, mapState } from 'vuex';
import PayConfirmModal from '@/views/system/billingandpayment/PayConfirmModal';
import store from '@/store';
import dayjs from 'dayjs';

export default {
  name: 'License',
  components: {
    VueNumberInput,
    PayConfirmModal
  },
  data() {
    return {
      store,
      dayjs,
      license: {
        productType: 'CloudDM',
        activateType: 'OFFLINE',
        applyCode: '',
        buyTaskCount: 0,
        oldResource: {
          taskMaxThan: false
        },
        finalConfig: {},
        buyTimeStandardId: 0,
        buyType: 'FLAGSHIP',
        priceData: {}
      },
      productTypeList: [],
      buyTypeList: ['COMMUNITY', 'POC', 'FLAGSHIP'],
      buyTypeI18n: {
        POC: this.$t('shang-ye-shi-yong-ban'),
        COMMUNITY: this.$t('she-qu-ban'),
        ENTERPRISE: this.$t('shang-ye-ban'),
        FLAGSHIP: this.$t('shang-ye-ban-0')
      },
      BUY_TYPE: {
        BIZ: 'ENTERPRISE',
        FREE: 'COMMUNITY',
        POC: 'POC',
        FLAGSHIP: 'FLAGSHIP'
      },
      configList: [],
      configObj: {},
      buyTimeStandardList: [],
      pocBuyTimeStandardList: [],
      communityBuyTimeStandardList: [],
      config: {
        linkCount: 10,
        validityMonths: 6
      },
      credits: [
        { label: '+3 19,100 Credits', value: 19100 },
        { label: 'Standard Credits', value: 15000 },
        { label: 'Basic Credits', value: 10000 }
      ],
      selectedCredit: null,
      couponCode: '',
      appliedCoupon: null,
      couponDiscount: 0,
      basePrice: 20000,
      ifSelectVoucher: false,
      voucherList: [],
      hasPay: false,
      payLoading: false,
      showPaymentConfirm: false,
      payTypeList: [],
      selectedPayType: '',
      oldResourcesColumns: [
        {
          title: this.$t('lian-lu-shu-0'),
          key: 'authedTaskCount'
        },
        {
          title: this.$t('sheng-yu-shi-jian'),
          slot: 'licenseRemainingTimeMs'
        },
        {
          title: this.$t('shou-quan-shi-jian-fan-wei'),
          slot: 'authedTimeRange'
        }
      ],
      oldResourceData: [],
      finalResourcesColumns: [
        {
          title: this.$t('lian-lu-shu-0'),
          slot: 'finalTaskCount'
        },
        {
          title: this.$t('sheng-yu-shi-jian'),
          slot: 'licenseRemainingTimeMs'
        },
        {
          title: this.$t('shou-quan-shi-jian-fan-wei'),
          slot: 'authedTimeRange'
        }
      ],
      dmFinalResourcesColumns: [
        {
          title: this.$t('sheng-yu-shi-jian'),
          slot: 'licenseRemainingTimeMs'
        },
        {
          title: this.$t('shou-quan-shi-jian-fan-wei'),
          slot: 'authedTimeRange'
        }
      ],
      finalResourceData: [],
      errorMessage: '',
      inputStatus: '',
      restDays: 0,
      restHours: 0
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    }),
    ...mapGetters(['isDark']),
    totalPrice() {
      return `$${this.basePrice.toLocaleString()}`;
    },
    originalPrice() {
      return this.basePrice - this.selectedCredit?.amount || 0;
    },
    isPreFLAGSHIP() {
      return this.license.oldResource.oldBuyType === 'FLAGSHIP';
    },
    isOffline() {
      return this.license.oldResource.contractType === 'OFFLINE';
    },
    // 是否是免费版
    isPreFreeEdition() {
      return this.license.oldResource.oldBuyType === this.BUY_TYPE.FREE;
    },
    isPrePocEdition() {
      return this.license.oldResource.oldBuyType === this.BUY_TYPE.POC;
    },
    isPreBizEdition() {
      return this.license.oldResource.oldBuyType === this.BUY_TYPE.BIZ || this.license.oldResource.oldBuyType === this.BUY_TYPE.FLAGSHIP;
    },
    isFreeEdition() {
      return this.license.buyType === this.BUY_TYPE.FREE;
    },
    isPocEdition() {
      return this.license.buyType === this.BUY_TYPE.POC;
    },
    getFinalResourceColumns() {
      if (this.license.productType === 'CloudDM') {
        return this.dmFinalResourcesColumns;
      } else {
        return this.finalResourcesColumns;
      }
    },
    disabledEdition() {
      return (buyType) => {
        if (buyType === this.BUY_TYPE.FREE) {
          if (this.isPrePocEdition || this.isPreBizEdition) {
            return true;
          }
        } else if (buyType === this.BUY_TYPE.POC) {
          if (this.isPrePocEdition || this.isPreBizEdition) {
            return true;
          }
        }
        return false;
      };
    }
  },
  created() {
    this.productTypeList = ['CloudDM'];
    this.license.productType = 'CloudDM';
    this.buyTypeList = ['COMMUNITY'];
    this.license.buyType = 'COMMUNITY';
    this.getBuyTimeStandard();
    this.getConfigValue();
    this.listPayTypes();
  },
  methods: {
    isPoc(buyType) {
      return buyType === this.BUY_TYPE.POC;
    },
    async listPayTypes() {
      const res = await this.$services.rdpAuthCodeOrderListPayTypes();
      if (res?.success) {
        this.payTypeList = res.data;
        if (this.payTypeList.length > 0) {
          this.selectedPayType = this.payTypeList[0].payType;
        }
      }
    },
    async handlePayment() {
      this.payLoading = true;
      const res = await this.$services.rdpAuthCodeOrderCreate({
        data: {
          productType: this.license.productType,
          activateType: this.license.activateType,
          applyCode: this.license.applyCode,
          buyTimeStandardId: this.license.buyTimeStandardId,
          buyTaskCount: this.license.buyTaskCount,
          buyType: this.license.buyType,
          companyName: this.userInfo.company,
          email: this.userInfo.email,
          phone: this.userInfo.phone,
          userName: this.userInfo.username
        }
      });
      if (res?.success) {
        if (res.data.orderStatus === 'FINISH') {
          this.payLoading = false;
          this.$router.push({
            path: '/system/order'
          });
        } else {
          const res1 = await this.$services.rdpAuthCodeOrderPay({
            data: {
              orderId: res.data.orderId,
              payType: this.selectedPayType
            }
          });
          if (res1.success) {
            this.payLoading = false;
            this.hasPay = true;
            this.showPaymentConfirm = true;
            window.open(res1.data);
          }
        }
      }
      this.payLoading = false;
    },
    async handleChangeApplyCode() {
      this.inputStatus = '';
      this.errorMessage = '';
      if (!this.license.applyCode) {
        this.inputStatus = 'error';
        this.errorMessage = this.$t('shen-qing-ma-bu-neng-wei-kong');
        this.license.oldResource = {
          taskMaxThan: false
        };
        this.oldResourceData = [];
        this.license.oldResource = {
          taskMaxThan: false
        };
        this.license.finalConfig = {};
        this.license.priceData = {};
        return;
      }
      await this.queryOldResource();
    },
    async queryOldResource() {
      const res = await this.$services.rdpAuthCodeOrderQueryOldResource({
        data: {
          applyCode: this.license.applyCode
        },
        modal: false
      });
      if (res?.success) {
        if (res.data) {
          this.license.buyType = res.data.oldBuyType;
          if (res.data.oldBuyType === 'POC') {
            this.license.buyType = 'FLAGSHIP';
          }
          this.oldResourceData = [res.data];
          this.license.oldResource = res.data;
          this.license.oldResource.taskMaxThan =
            !this.isPoc(this.license.oldResource.oldBuyType) &&
            !this.isPreFreeEdition &&
            this.license.oldResource.authedTaskCount > this.configObj.BUY_MAX_TASK_COUNT_LIMIT;
        }
        if (this.license.buyType === 'FLAGSHIP' && this.buyTimeStandardList.length > 0) {
          this.license.buyTimeStandardId = this.buyTimeStandardList[0].id;
          this.license.buyTaskCount = this.configObj.BUY_MIN_TASK_COUNT_LIMIT;
          this.handleGetFinalConfig();
        } else if (this.license.buyType === 'POC' && this.pocBuyTimeStandardList.length > 0) {
          this.license.buyTimeStandardId = this.pocBuyTimeStandardList[0].id;
          this.license.buyTaskCount = this.configObj.BUY_MIN_TASK_COUNT_LIMIT;
          this.handleGetFinalConfig();
        } else if (this.license.buyType === 'COMMUNITY' && this.communityBuyTimeStandardList.length > 0) {
          this.license.buyTimeStandardId = this.communityBuyTimeStandardList[0].id;
          this.license.buyTaskCount = this.configObj.FREE_TASK_COUNT_LIMIT;
          this.handleGetFinalConfig();
        }
        this.errorMessage = '';
        this.inputStatus = '';
      } else {
        this.errorMessage = res.msg;
        this.inputStatus = 'error';
      }
    },
    async getBuyTimeStandard() {
      const res2 = await this.$services.rdpAuthCodeOrderGetBuyTimeStandard({
        data: {
          versionType: 'COMMUNITY',
          productType: this.license.productType
        }
      });
      if (res2?.success) {
        this.communityBuyTimeStandardList = res2.data;
        if (this.license.buyType === 'COMMUNITY') {
          this.license.buyTimeStandardId = this.communityBuyTimeStandardList[0].id;
          this.handleGetFinalConfig();
        }
      }
    },
    async getConfigValue() {
      const res = await this.$services.rdpAuthCodeOrderGetConfigValue();
      if (res?.success) {
        this.configList = res.data;
        const obj = {};
        this.configList.forEach((config) => {
          obj[config.configName] = config.configValue;
        });

        this.configObj = obj;
        if (this.license.buyType === 'COMMUNITY') {
          this.license.buyTaskCount = this.configObj.FREE_TASK_COUNT_LIMIT;
        }
      }
    },
    async handleChangeEdition(data) {
      if (this.license.buyType === 'FLAGSHIP') {
        this.license.buyTimeStandardId = this.buyTimeStandardList[0].id;
      } else if (this.license.buyType === 'POC') {
        this.license.buyTimeStandardId = this.pocBuyTimeStandardList[0].id;
      } else if (this.license.buyType === 'COMMUNITY') {
        this.license.buyTimeStandardId = this.communityBuyTimeStandardList[0].id;
      }
      if (data === 'COMMUNITY') {
        this.license.buyTaskCount = this.configObj.FREE_TASK_COUNT_LIMIT;
      } else if (data === 'POC') {
        this.license.buyTaskCount = this.configObj.POC_TASK_COUNT_LIMIT;
      } else if (data === 'FLAGSHIP') {
        this.license.buyTaskCount = this.configObj.BUY_MIN_TASK_COUNT_LIMIT;
      }
      this.handleGetFinalConfig();
    },
    async handleChangeProductType(data) {
      if (data === 'CloudCanal') {
        this.buyTypeList = ['COMMUNITY', 'POC', 'FLAGSHIP'];
      } else if (data === 'CloudDM') {
        this.buyTypeList = ['COMMUNITY'];
      }
      this.license.buyType = 'COMMUNITY';
      await this.getBuyTimeStandard();
      await this.handleChangeEdition('COMMUNITY');
    },
    async handleGetFinalConfig() {
      console.log('get final config');
      try {
        if (this.license.buyType === 'COMMUNITY') {
          this.calLicenseDate(this.communityBuyTimeStandardList[0]);
          this.calRestDays(this.communityBuyTimeStandardList[0]);
        }
        if (this.license.buyType === 'POC') {
          this.calLicenseDate(this.pocBuyTimeStandardList[0]);
          this.calRestDays(this.pocBuyTimeStandardList[0]);
        } else {
          this.buyTimeStandardList.forEach((standard) => {
            if (standard.id === this.license.buyTimeStandardId) {
              this.calLicenseDate(standard);
              this.calRestDays(standard);
            }
          });
        }
        this.license.finalConfig.finalTaskCount = this.license.buyTaskCount;
        this.license.finalConfig.restDays = this.restDays;
        this.license.finalConfig.restHours = this.restHours;
        this.finalResourceData = [this.license.finalConfig];
        console.log('this.finalResourceData', this.finalResourceData);
        await this.calculatePrice();
      } catch (e) {
        console.log(e);
      }
    },
    async calculatePrice() {
      if (this.license.applyCode) {
        const res = await this.$services.rdpAuthCodeOrderCalculatePrice({
          data: {
            applyCode: this.license.applyCode,
            productType: this.license.productType,
            buyTimeStandardId: this.license.buyTimeStandardId,
            buyTaskCount: this.license.buyTaskCount,
            buyType: this.license.buyType
          },
          modal: false
        });
        if (res?.success) {
          this.license.priceData = res.data;
          this.errorMessage = '';
          this.inputStatus = '';
        } else {
          this.errorMessage = res.msg;
          this.inputStatus = 'error';
        }
      }
    },
    handleConfirmFinish() {
      this.$router.push({
        path: '/system/order'
      });
    },
    calRestDays(standard) {
      this.restDays = 0;
      this.restHours = 0;
      console.log('standard', standard);
      if (standard.buyTimeType === 'HOUR') {
        this.restHours = standard.buyTimeValue;
      } else if (standard.buyTimeType === 'DAY') {
        this.restDays = standard.buyTimeValue;
      } else if (standard.buyTimeType === 'MONTH') {
        const d = new Date();
        const year = d.getFullYear();
        const month = d.getMonth();
        const date = d.getDate();
        this.restDays = Math.ceil((new Date(year, month + standard.buyTimeValue * 1, date).getTime() - d.getTime()) / (1000 * 3600 * 24));
      } else if (standard.buyTimeType === 'YEAR') {
        const d = new Date();
        const year = d.getFullYear();
        const month = d.getMonth();
        const date = d.getDate();
        this.restDays = Math.ceil((new Date(year + standard.buyTimeValue * 1, month, date).getTime() - d.getTime()) / (1000 * 3600 * 24));
      }
    },
    calLicenseDate(standard) {
      if (standard.buyTimeType === 'HOUR') {
        this.license.finalConfig.day = dayjs().add(standard.buyTimeValue, 'h');
      } else if (standard.buyTimeType === 'DAY') {
        this.license.finalConfig.day = this.getDate(0, 0, standard.buyTimeValue);
      } else if (standard.buyTimeType === 'MONTH') {
        this.license.finalConfig.day = this.getDate(0, standard.buyTimeValue, 0);
      } else if (standard.buyTimeType === 'YEAR') {
        this.license.finalConfig.day = this.getDate(standard.buyTimeValue, 0, 0);
      }
    },
    getDate(m, n, y) {
      const d = new Date();
      const year = d.getFullYear();
      const month = d.getMonth();
      const date = d.getDate();
      const hour = d.getHours();
      const minute = d.getMinutes();
      const secodes = d.getSeconds();
      const myDate = new Date(year + m * 1, month + n * 1, date + y * 1, hour, minute, secodes);
      return myDate;
    }
  }
};
</script>
<style scoped lang="less">
.error-tip {
  color: #ed4014; /* IView错误色 */
  font-size: 12px;
  margin-top: 5px;
}

.license-step {
  background: var(--bg-secondary) !important;
}

.license-resource h4 {
  color: var(--color-primary) !important;
}
</style>
