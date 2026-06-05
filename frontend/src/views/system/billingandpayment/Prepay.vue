<template>
  <div class="recharge">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem :to="{ name: 'Balance' }">{{ $t('yu-zhi-fu') }}</BreadcrumbItem>
            <BreadcrumbItem>{{ $t('chong-zhi-0') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content mt-4">
          <Form :model="form" :rules="rules" :label-width="100" ref="formRef">
            <FormItem :label="$t('dang-qian-yu-e')">
              <span class="font-bold text-primary mr-1 text-[16px]">{{ userBalance.balance }}</span>
              <span class="text-base">{{ userBalance.currencyI18n }}</span>
            </FormItem>
            <FormItem :label="$t('zhi-fu-fang-shi')" prop="payType">
              <RadioGroup v-model="form.payType" type="button">
                <Radio v-for="payType in payTypes" :key="payType.payType" :label="payType.payType">
                  <CustomIcon :type="`icon-v2-${payType.payType}`" right-margin="10px" />
                  {{ payType.payTypeI18n }}
                </Radio>
              </RadioGroup>
            </FormItem>
            <FormItem :label="$t('zhi-fu-jin-e')" prop="amount">
              <Input type="number" v-model="form.amount" :placeholder="$t('qing-shu-ru-jin-e')" class="w-[400px]">
                <template #append>{{ userBalance.currencyI18n }}</template>
              </Input>
            </FormItem>
            <FormItem>
              <Checkbox v-model="isAgree">{{ $t('wo-yi-le-jie') }}</Checkbox>
              <div class="mt-2 bg-gray-50 p-4 rounded-md">
                <div class="text-lg font-bold">
                  {{ $t('zhu-yi-shi-xiang') }}
                </div>
                <div class="mt-2">
                  <div>{{ $t('1-yu-fu-kuan-jin-yong-yu-saas-kou-kuan-wu-fa-yong-yu-si-you-bu-shu-license-gou-mai') }}</div>
                  <div>{{ $t('2-yu-fu-kuan-kou-kuan-shun-xu-mo-ren-you-xian-yu-bang-ka-fu-kuan-hai-wai') }}</div>
                  <div>{{ $t('3-saas-wu-lun-shi-yu-fu-kuan-fang-shi-huo-bang-ka-fang-shi-kou-kuan-jun-an-yue-du-zhang-dan-shen-qing-fa-piao') }}</div>
                </div>
              </div>
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                icon="md-card"
                class="mt-4 text-lg"
                :disabled="hasPay || !form.amount || !isAgree"
                :loading="payLoading"
                @click="handlePayment"
              >
                <span class="text-lg">{{ $t('qu-chong-zhi') }}</span>
              </Button>
            </FormItem>
          </Form>
          <!-- <div class="mt-4">
            <div class="flex items-end">
              <span class="font-bold">{{ $t('zhi-fu-jin-e') }}</span>
              <a href="https://www.clougence.com/cc-doc/price/product_price" target="_blank" class="text-blue-500 ml-2 flex">
                <CustomIcon type="icon-v2-help" />
                {{ $t('saas-jia-ge') }}
              </a>
            </div>
          </div> -->
        </div>
      </div>
    </div>
    <pay-confirm-modal :show-pay-confirm="showPaymentConfirm" :handle-confirm-finish="handleConfirmFinish" />
  </div>
</template>

<script>
import PayConfirmModal from '@/views/system/billingandpayment/PayConfirmModal';
import { template } from 'lodash';
export default {
  name: 'Prepay',
  components: {
    PayConfirmModal
  },
  data() {
    const validateAmount = (rule, value, callback) => {
      if (value < 1 || value > 100000) {
        callback(new Error(this.$t('qing-shu-ru-1-100000-jian-de-jin-e')));
      } else {
        callback();
      }
    };
    return {
      form: {
        payType: null,
        amount: null
      },
      rules: {
        payType: [{ required: true, message: this.$t('qing-xuan-ze-zhi-fu-fang-shi'), trigger: 'change' }],
        amount: [
          { required: true, message: this.$t('qing-shu-ru-jin-e'), trigger: 'blur' },
          { validator: validateAmount, trigger: 'blur' }
        ]
      },
      isAgree: false,
      payTypes: [],
      selectedPayType: null,
      payLoading: false,
      hasPay: false,
      showPaymentConfirm: false,
      userBalance: {}
    };
  },
  mounted() {
    this.listPayTypes();
    this.getUserBalance();
  },
  methods: {
    async getUserBalance() {
      this.getUserBalanceLoading = true;
      const res = await this.$services.rdpFinanceQueryUserBalance();

      if (res.success) {
        this.userBalance = res.data;
      }
      this.getUserBalanceLoading = false;
    },
    async listPayTypes() {
      const res = await this.$services.rdpAuthCodeOrderListPayTypes();
      if (res.success) {
        this.payTypes = res.data;
        if (this.payTypes.length > 0) {
          this.form.payType = this.payTypes[0].payType;
        }
      }
    },
    handleConfirmFinish() {
      this.$router.push({
        name: 'Balance'
      });
    },
    async handlePayment() {
      this.$refs.formRef.validate(async (valid) => {
        if (valid) {
          this.payLoading = true;

          const res = await this.$services.rdpFinanceCreatePrepay({
            data: this.form
          });
          if (res?.success) {
            const res1 = await this.$services.rdpFinancePayUrlGenForPrepay({
              data: {
                prePayId: res.data,
                payType: this.form.payType
              }
            });
            if (res1.success) {
              this.payLoading = false;
              this.hasPay = true;
              this.showPaymentConfirm = true;
              window.open(res1.data);
            }
          }
          this.payLoading = false;
        }
      });
    }
  }
};
</script>

<style lang="less" scoped>
.recharge {
  .content {
    color: var(--text-primary, rgba(0, 0, 0, 0.88));
  }

  // 灰色背景区域适配暗色模式
  .bg-gray-50 {
    background-color: #f9fafb;
    color: rgba(0, 0, 0, 0.88);
  }

  // 文字颜色适配
  .text-primary {
    color: var(--primary-color, #0bb9f8);
  }

  .text-base {
    color: var(--text-primary, rgba(0, 0, 0, 0.88));
  }
}
</style>

<style lang="less">
// 暗色模式适配
[data-theme='dark'] {
  .recharge {
    .content {
      color: var(--text-primary, #ffffff);
    }

    // 灰色背景区域适配暗色模式
    .bg-gray-50 {
      background-color: var(--bg-tertiary, #2e3137) !important;
      color: var(--text-primary, #ffffff) !important;
    }

    // 文字颜色适配
    .text-primary {
      color: var(--primary-color, #0087c7) !important;
    }

    .text-base {
      color: var(--text-primary, #ffffff) !important;
    }

    // 表单标签颜色（仅在此页面内）
    .ivu-form .ivu-form-item-label {
      color: var(--text-primary, #ffffff) !important;
    }

    // 输入框文字颜色（仅在此页面内）
    .ivu-form .ivu-input {
      color: var(--text-primary, #ffffff) !important;
    }

    // Radio 按钮组文字颜色（仅在此页面内）
    .ivu-form .ivu-radio-group-button .ivu-radio-wrapper {
      color: var(--text-primary, #ffffff) !important;
    }

    // Checkbox 文字颜色（仅在此页面内）
    .ivu-form .ivu-checkbox-wrapper {
      color: var(--text-primary, #ffffff) !important;
    }
  }
}
</style>
