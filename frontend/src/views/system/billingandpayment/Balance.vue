<template>
  <div class="rechargeList" :class="{ 'is-dark': isDark }">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('yu-zhi-fu') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="w-full flex">
            <div class="flex-1">
              <div class="text-xl mr-2 text-gray-600 font-medium recharge-title">{{ $t('yu-e') }}</div>
              <div class="text-2xl font-bold text-primary mr-4 mt-[16px] text-[36px]">
                {{ userBalance.balance }}
                <span class="text-base">{{ userBalance.currencyI18n }}</span>
                <Button type="primary" @click="handlePrePay" class="mr-2 ml-2">
                  {{ $t('qu-chong-zhi') }}
                </Button>
                <Button type="default" @click="handleRefreshList" :loading="refreshListLoading">
                  {{ ' ' }}
                  <CustomIcon type="icon-v2-Refresh" v-if="!refreshListLoading" />
                </Button>
              </div>
            </div>
            <div class="flex-1">
              <div class="text-xl text-gray-600 font-medium recharge-title">{{ $t('yu-e-yu-jing') }}</div>
              <div class="flex items-end mt-[26px]">
                <span class="mr-2">{{ userBalance.needAlert ? $t('yi-kai-qi') : $t('yi-guan-bi') }}</span>
                <div>
                  <i-switch v-model="userBalance.needAlert" @on-change="handleChangeNeedAlert(userBalance.needAlert)" />
                </div>
                <span class="ml-2">{{ $t('yu-jing-qu-zhi') }}:</span>
                <span class="mr-2 font-bold">{{ userBalance.alertBalance }}</span>
                <span class="mr-2">{{ userBalance.currencyI18n }}</span>
                <a @click="handleShowAlertBalanceModal">{{ $t('she-zhi') }}</a>
              </div>
            </div>
          </div>
          <div class="mt-6 flow-root">
            <div class="overflow-x-auto">
              <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                <Table :columns="columns" :data="rechargeList" border size="small">
                  <template #createTime="{ row }">
                    <div>{{ dayjs(row.gmtCreate).format('YYYY-MM-DD HH:mm:ss') }}</div>
                  </template>
                  <template #amount="{ row }">
                    <div>{{ transformScientific(row.amount) }} {{ row.currencyI18n }}</div>
                  </template>
                  <template #payType="{ row }">
                    <div>{{ row.payTypeI18n }}</div>
                  </template>
                  <template #action="{ row }">
                    <div v-if="row.prepayStatus === PREPAY_STATUS.WAIT_PAID">
                      <Button type="text" @click="handleContinuePay(row)">
                        {{ $t('ji-xu-zhi-fu') }}
                      </Button>
                      <Button type="text" @click="handleShowCancelPrepayModal(row)">
                        {{ $t('qu-xiao') }}
                      </Button>
                    </div>
                  </template>
                </Table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="userPrepayCount"
          :page-size="pageSize"
          show-total
          show-elevator
          v-model="pageNum"
          :model-value="pageNum"
          @on-change="handlePageChange"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showCancelPrepayModal" :title="$t('qu-xiao-zhi-fu')">
      <div>
        <Form :model="cancelPrepayForm" :rules="cancelPrepayFormRules" ref="cancelPrepayFormRef" :label-width="100">
          <FormItem :label="$t('qu-xiao-yuan-yin')" prop="cancelMsg">
            <Input v-model="cancelPrepayForm.cancelMsg" :placeholder="$t('qing-shu-ru-qu-xiao-yuan-yin')" />
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button type="default" @click="handleCancelCancelPrepayModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleCancelPrepay">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <!-- 余额告警弹窗，可以设置开启告警和设置告警余额 -->
    <CCModal v-model="showAlertBalanceModal" :title="$t('yuegao-jing-she-zhi')" :footer-hide="true">
      <div>
        <Form :model="alertBalanceForm" :rules="alertBalanceFormRules" :label-width="110" ref="alertBalanceFormRef">
          <FormItem :label="$t('kai-qi-yuegao-jing')">
            <i-switch v-model="alertBalanceForm.needAlert" @on-change="handleChangeNeedAlert(alertBalanceForm.needAlert)" />
          </FormItem>
          <FormItem :label="$t('yuegao-jing-jin-e')" prop="alertBalance">
            <Input v-model="alertBalanceForm.alertBalance" :placeholder="$t('yuegao-jing-jin-e')" type="number">
              <template #append>
                <span>{{ userBalance.currencyI18n }}</span>
              </template>
            </Input>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button type="default" @click="handleCancelAlertBalanceModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleChangeAlertBalance">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <pay-confirm-modal :show-pay-confirm="showPaymentConfirm" :handle-confirm-finish="handleConfirmFinish" />
  </div>
</template>

<script>
import { PREPAY_STATUS } from '@/utils';
import PayConfirmModal from '@/views/system/billingandpayment/PayConfirmModal';
import dayjs from 'dayjs';
import { transformScientific } from '@/utils/tools';
import { mapGetters } from 'vuex';

export default {
  name: 'Balance',
  components: {
    PayConfirmModal
  },
  computed: {
    ...mapGetters(['isDark'])
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
      PREPAY_STATUS,
      showAlertBalanceModal: false,
      alertBalanceForm: {
        needAlert: false,
        alertBalance: 0
      },
      alertBalance: 0,
      alertBalanceFormRules: {
        alertBalance: [
          { required: true, message: this.$t('qing-shu-ru-yuegao-jing-jin-e') },
          { validator: validateAmount, trigger: 'blur' }
        ]
      },
      showCancelPrepayModal: false,
      cancelPrepayForm: {
        prepayId: '',
        cancelMsg: ''
      },
      cancelPrepayFormRules: {
        cancelMsg: [{ required: true, message: this.$t('qing-shu-ru-qu-xiao-yuan-yin') }]
      },
      cancelPrepayLoading: false,
      showPaymentConfirm: false,
      rechargeList: [],
      userPrepayCount: 0,
      pageSize: 10,
      userBalance: {},
      pageNum: 1,
      refreshListLoading: false,
      getUserBalanceLoading: false,
      columns: [
        {
          title: this.$t('chong-zhi-e-du'),
          slot: 'amount'
        },
        {
          title: this.$t('fa-qi-shi-jian'),
          slot: 'createTime'
        },
        {
          title: this.$t('jie-guo'),
          key: 'prepayStatusI18n'
        },
        {
          title: this.$t('zhi-fu-fang-shi'),
          slot: 'payType'
        },
        {
          title: this.$t('cao-zuo'),
          width: 200,
          fixed: 'right',
          slot: 'action'
        }
      ]
    };
  },
  mounted() {
    this.handleRefreshList();
  },
  methods: {
    dayjs,
    transformScientific,
    async handleChangeNeedAlert(value) {
      const res = await this.$services.rdpFinanceChangeBalanceNeedAlert({
        data: {
          id: this.userBalance.id,
          needAlert: value
        }
      });

      if (res.success) {
        this.$Message.success(value ? this.$t('kai-qi-cheng-gong') : this.$t('guan-bi-cheng-gong'));
      }

      this.handleRefreshList();
    },
    handleCancelAlertBalanceModal() {
      this.showAlertBalanceModal = false;
    },
    async handleChangeAlertBalance() {
      this.$refs.alertBalanceFormRef.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.rdpFinanceChangeAlertBalance({
            data: {
              id: this.userBalance.id,
              alertBalance: this.alertBalanceForm.alertBalance
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('xiu-gai-cheng-gong'));
            this.handleRefreshList();
            this.showAlertBalanceModal = false;
          }
        }
      });
    },
    handleShowAlertBalanceModal() {
      this.alertBalanceForm.needAlert = this.userBalance.needAlert;
      this.alertBalanceForm.alertBalance = this.userBalance.alertBalance;
      this.showAlertBalanceModal = true;
    },
    handleShowCancelPrepayModal(row) {
      this.cancelPrepayForm.prepayId = row.prepayId;
      this.cancelPrepayForm.cancelMsg = '';
      this.showCancelPrepayModal = true;
    },
    handleCancelCancelPrepayModal() {
      this.showCancelPrepayModal = false;
    },
    handleCancelPrepayModal() {
      this.showCancelPrepayModal = false;
    },
    handleConfirmFinish() {
      this.showPaymentConfirm = false;
      this.handleRefreshList();
    },
    handlePayment() {
      this.payLoading = true;
    },
    async handleContinuePay(row) {
      const res = await this.$services.rdpFinancePayUrlGenForPrepay({
        data: {
          prePayId: row.prepayId,
          payType: row.payType
        }
      });

      if (res.success) {
        this.showPaymentConfirm = true;
        window.open(res.data);
      }
    },
    async handleCancelPrepay(row) {
      this.$refs.cancelPrepayFormRef.validate(async (valid) => {
        if (valid) {
          this.cancelPrepayLoading = true;
          const res = await this.$services.rdpFinanceCancelPrepay({
            data: {
              prepayId: this.cancelPrepayForm.prepayId,
              cancelMsg: this.cancelPrepayForm.cancelMsg
            }
          });
          this.cancelPrepayLoading = false;
          if (res.success) {
            this.$Message.success(this.$t('qu-xiao-cheng-gong'));
            this.showCancelPrepayModal = false;
            this.handleRefreshList();
          }
        }
      });
    },
    handlePrePay() {
      this.$router.push({
        name: 'Prepay'
      });
    },
    async getUserPrepayCount() {
      const res = await this.$services.rdpFinanceUserPrepayCount();

      if (res.success) {
        this.userPrepayCount = res.data;
      }
    },
    async getPageListUserPrepays() {
      const res = await this.$services.rdpFinancePageListUserPrepays({
        data: {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      });

      if (res.success) {
        this.rechargeList = res.data;
      }
    },
    async handleRefreshList() {
      this.refreshListLoading = true;
      await this.getUserBalance();
      await this.getUserPrepayCount();
      await this.getPageListUserPrepays();
      this.refreshListLoading = false;
    },
    async getUserBalance() {
      this.getUserBalanceLoading = true;
      const res = await this.$services.rdpFinanceQueryUserBalance();

      if (res.success) {
        this.userBalance = res.data;
      }
      this.getUserBalanceLoading = false;
    },
    handlePageChange(page) {
      this.pageNum = page;
      this.getPageListUserPrepays();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getPageListUserPrepays();
    }
  }
};
</script>

<style scoped lang="less">
.rechargeList {
  height: 100%;
  display: flex;
  flex-direction: column;
}

// 暗色模式覆盖
&.is-dark {
  .recharge-title {
    color: var(--color-primary) !important;
  }
}
</style>
