<template>
  <div class="order">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('xu-ke-zheng') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="w-full">
            <Button class="refresh float-right" @click="queryOrder" :loading="loading">
              <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
            </Button>
            <Button class="mr-4 float-right" type="primary" @click="handleCreateOrder">
              {{ $t('chuang-jian-xu-ke-zheng') }}
            </Button>
          </div>
          <div class="mt-6 flow-root">
            <div class="overflow-x-auto">
              <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                <Table :columns="orderColumns" :data="orderList" size="small" :loading="loading">
                  <template #id="{ row }">
                    {{ row.orderId }}
                  </template>
                  <template #orderTime="{ row }">
                    {{ dayjs(row.createOrderTimeMs).format('YYYY-MM-DD HH:mm:ss') }}
                  </template>
                  <template #license="{ row }">
                    <Button type="text" v-if="isFinish(row)" @click="handleShowAuthCode(row)">
                      {{ $t('cha-kan') }}
                    </Button>
                  </template>
                  <template #amount="{ row }">
                    <Poptip placement="right" transfer trigger="hover">
                      <span class="cursor-pointer">{{ row.totalPrice }}{{ row.currencyI18n }}</span>
                      <template #content>
                        <div>
                          <p class="mb-2">
                            <span class="w-32 inline-block">{{ $t('yuan-shi-jia-ge') }}</span>
                            <span class="font-semibold">{{ row.originalPrice }}{{ row.currencyI18n }}</span>
                          </p>
                          <p class="mb-2" v-if="row.restPrice">
                            <span class="w-32 inline-block">{{ $t('sheng-yu-jin-e') }}</span>
                            <span class="font-semibold">-{{ row.restPrice }}{{ row.currencyI18n }}</span>
                          </p>
                          <p class="mb-2" v-if="row.discountSavedMoney">
                            <span class="w-32 inline-block">{{ $t('zhe-kou') }}</span>
                            <span class="font-semibold">-{{ row.discountSavedMoney }}{{ row.currencyI18n }}</span>
                          </p>
                          <p class="mb-2" v-if="row.voucherSavedMoney">
                            <span class="w-32 inline-block">{{ $t('you-hui-quan') }}</span>
                            <span class="font-semibold">-{{ row.voucherSavedMoney }}{{ row.currencyI18n }}</span>
                          </p>
                          <p>
                            <span class="w-32 inline-block">{{ $t('zui-zhong-jia-ge') }}</span>
                            <span class="font-semibold">={{ row.totalPrice }}{{ row.currencyI18n }}</span>
                          </p>
                        </div>
                      </template>
                    </Poptip>
                  </template>
                  <template #buyTaskCount="{ row }">
                    {{ row.buyTaskCount == '-1' ? $t('wu-xian-zhi') : row.buyTaskCount }}
                  </template>
                  <template #timeRange="{ row }">
                    {{ row.licenseStartTimeMs ? dayjs(row.licenseStartTimeMs).format('YYYY-MM-DD HH:mm:ss') : '' }}
                    -
                    {{ row.licenseEndTimeMs ? dayjs(row.licenseEndTimeMs).format('YYYY-MM-DD HH:mm:ss') : '' }}
                  </template>
                  <template #startTime="{ row }">
                    {{ dayjs(row.licenseStartTimeMs).format('YYYY-MM-DD HH:mm:ss') }}
                  </template>
                  <template #endTime="{ row }">
                    {{ dayjs(row.licenseEndTimeMs).format('YYYY-MM-DD HH:mm:ss') }}
                  </template>
                  <template #status="{ row }">
                    <span class="status" :style="`color: ${statusColor[row.orderStatus]};background: ${statusBgColor[row.orderStatus]};`">
                      {{ row.orderStatusI18n }}
                    </span>
                  </template>
                  <template #isInvoiced="{ row }">
                    <span class="status" :style="`color: ${invoiceStatusColor[row.isInvoiced]};background: ${invoiceStatusBgColor[row.isInvoiced]};`">
                      {{ row.isInvoiced ? $t('yi-kai-piao') : $t('wei-kai-piao') }}
                    </span>
                  </template>
                  <template #action="{ row, index }">
                    <div>
                      <Button type="text" size="small" v-if="isFinish(row) && !row.isInvoiced && row.totalPrice > 0" to="/system/invoiceApply">
                        {{ $t('huo-qu-fa-piao') }}
                      </Button>
                      <Button
                        :loading="row.fetchInvoiceLoading"
                        type="text"
                        size="small"
                        @click="handleFetchInvoice(row, index)"
                        v-if="isFinish(row) && !row.isInvoiced && row.totalPrice > 0"
                      >
                        {{ $t('huo-qu-shou-ju') }}
                      </Button>
                      <Button type="text" size="small" @click="handleContinuePay(row)" v-if="isWaitPaid(row)">
                        {{ $t('ji-xu-zhi-fu') }}
                      </Button>
                      <Button type="text" size="small" v-if="isWaitPaid(row)" @click="handleShowCloseModal(row)">
                        {{ $t('guan-bi-ding-dan') }}
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
          :total="total"
          show-total
          @on-change="handlePageChange"
          show-sizer
          v-model="pageNum"
          :page-size-opts="[5, 10, 20]"
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showCloseOrderModal" :title="$t('nin-que-ding-yao-qu-xiao-ding-dan-ma')" :mask-closable="false" :closable="false">
      {{ $t('ding-dan-qu-xiao-zhi-hou-jin-zhi-chi-chong-xin-gou-mai') }}
      <template #footer>
        <Button type="primary" @click="handleCloseOrder">{{ $t('que-ding') }}</Button>
        <Button @click="handleCancel">{{ $t('qu-xiao') }}</Button>
      </template>
    </CCModal>
    <verify-code-modal
      :visible="showLicenseSmsModal"
      :title="$t('cha-kan-xu-ke-zheng')"
      verify-code-type="FETCH_AUTH_CODE"
      :handle-close-modal="handleCancel"
      :handle-confirm-callback="handleConfirmShowAuthCode"
      ref="show-license-sms-modal"
      :loading="fetchLicenseLoading"
      :phone-number="userInfo.phone"
      :email="userInfo.email"
      :width="580"
    >
      <template #content>
        <div v-show="showLicenseSmsModal">
          <Alert type="warning" class="w-full mb-4">
            <div>
              {{ $t('xu-ke-zheng-guo-qi-hou') }}
              <strong style="color: red" v-if="selectedOrder.productType !== 'CloudDM'">
                {{ $t('de-suo-you-ren-wu-jiang-bei-qiang-zhi-ting-zhi', [selectedOrder.productType]) }}
              </strong>
              <strong style="color: red" v-if="selectedOrder.productType === 'CloudDM'">
                {{ $t('de-shi-yong-jiang-hui-shou-xian', [selectedOrder.productType]) }}
              </strong>
              {{ $t('qing-ti-qian-xu-xu-ke-zheng-de-shi-jian-yi-mian-ying-xiang-nin-de-shi-yong') }}
            </div>
          </Alert>
        </div>
      </template>
    </verify-code-modal>
    <pay-confirm-modal :show-pay-confirm="showPaymentConfirm" :handle-click-pay="handlePayOrder" :handle-confirm-finish="handleConfirmFinish" />
    <CCModal :title="$t('xu-ke-zheng')" v-model="showAuthCodeModal" width="800px" :mask-closable="false">
      <p class="break-all">
        {{ authCode }}
      </p>
      <template #footer>
        <Button type="primary" @click="handleCopyLicense">{{ $t('fu-zhi') }}</Button>
        <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { mapGetters, mapState } from 'vuex';
import { ORDER_STATUS, STATUS_COLOR, STATUS_BG_COLOR, INVOICE_STATUS_COLOR, INVOICE_STATUS_BG_COLOR } from '@/utils';
import dayjs from 'dayjs';
import PayConfirmModal from '@/views/system/billingandpayment/PayConfirmModal';
import VerifyCodeModal from '@/components/modal/VerifyCodeModal';
import { handleCopy } from '@/utils/clipboard';

export default {
  name: 'Order',
  components: {
    PayConfirmModal,
    VerifyCodeModal
  },
  data() {
    return {
      ORDER_STATUS,
      STATUS_COLOR,
      STATUS_BG_COLOR,
      INVOICE_STATUS_COLOR,
      INVOICE_STATUS_BG_COLOR,
      pageNum: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      orderList: [],
      showPaymentConfirm: false,
      showLicenseSmsModal: false,
      fetchLicenseLoading: false,
      authCode: '',
      showAuthCodeModal: false,
      orderColumns: [
        {
          title: this.$t('ding-dan-id'),
          slot: 'id',
          width: '250'
        },
        {
          title: this.$t('ding-dan-sheng-cheng-shi-jian'),
          slot: 'orderTime',
          width: 190
        },
        {
          title: this.$t('xu-ke-zheng'),
          slot: 'license',
          width: 100
        },
        {
          title: this.$t('ding-dan-jin-e-yuan'),
          slot: 'amount',
          minWidth: 120
        },
        {
          title: this.$t('lian-lu-shu-0'),
          slot: 'buyTaskCount',
          width: 140
        },
        {
          title: this.$t('shi-jian-fan-wei'),
          slot: 'timeRange',
          width: 340
        },
        {
          title: this.$t('zhi-fu-fang-shi'),
          key: 'payTypeI18n',
          width: 120
        },
        {
          title: this.$t('ding-dan-zhuang-tai'),
          slot: 'status',
          width: 100
        },
        {
          title: this.$t('kai-piao-zhuang-tai'),
          slot: 'isInvoiced',
          width: 100
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          fixed: 'right',
          width: 200
        }
      ],
      showCloseOrderModal: false,
      selectedOrder: {}
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo,
      theme: (state) => state.theme
    }),
    ...mapGetters(['verifyType']),
    // 根据主题返回适配的状态颜色
    statusColor() {
      if (this.theme === 'dark') {
        return {
          FINISH: '#52C41A',
          CLOSED: '#86909C',
          WAIT_PAID: '#F7BA1E',
          FAILED: '#FF4D4F'
        };
      }
      return STATUS_COLOR;
    },
    statusBgColor() {
      if (this.theme === 'dark') {
        return {
          FINISH: 'rgba(82, 196, 26, 0.2)',
          CLOSED: 'rgba(134, 144, 156, 0.2)',
          WAIT_PAID: 'rgba(247, 186, 30, 0.2)',
          FAILED: 'rgba(255, 77, 79, 0.2)'
        };
      }
      return STATUS_BG_COLOR;
    },
    invoiceStatusColor() {
      if (this.theme === 'dark') {
        return {
          true: '#52C41A',
          false: '#F7BA1E'
        };
      }
      return INVOICE_STATUS_COLOR;
    },
    invoiceStatusBgColor() {
      if (this.theme === 'dark') {
        return {
          true: 'rgba(82, 196, 26, 0.2)',
          false: 'rgba(247, 186, 30, 0.2)'
        };
      }
      return INVOICE_STATUS_BG_COLOR;
    }
  },
  mounted() {
    this.queryOrder();
  },
  methods: {
    dayjs,
    async queryOrder() {
      this.loading = true;
      const res = await this.$services.rdpAuthCodeOrderQuery({
        data: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          phone: this.userInfo.phone
        }
      });
      if (res?.success) {
        this.orderList = res.data.orderVOS;
        this.total = res.data.totalCount;
      }
      this.loading = false;
    },
    handlePageChange(data) {
      this.pageNum = data;
      this.queryOrder();
    },
    handlePageSizeChange(data) {
      this.pageSize = data;
      this.queryOrder();
    },
    isFinish(row) {
      return row.orderStatus === ORDER_STATUS.FINISH;
    },
    isWaitPaid(row) {
      return row.orderStatus === ORDER_STATUS.WAIT_PAID;
    },
    handleShowCloseModal(row) {
      this.showCloseOrderModal = true;
      this.selectedOrder = row;
    },
    async handleFetchInvoice(row, index) {
      this.orderList[index].fetchInvoiceLoading = true;
      // this.$set(this.orderList, index, this.orderList[index]);
      this.orderList.index = this.orderList[index];
      const res = await this.$services.rdpAuthCodeOrderQueryOrderInvoice({
        data: {
          orderId: row.orderId
        }
      });
      if (res?.success) {
        if (res.data.enable) {
          window.open(res.data.invoiceUrl);
        } else {
          this.$Modal.warning({
            title: this.i18n('yi-chang-ti-shi'),
            content: this.$t('zan-shi-wu-fa-huo-qu-ci-ding-dan-de-fa-piao')
          });
        }
      }
      this.orderList[index].fetchInvoiceLoading = false;
      // this.$set(this.orderList, index, this.orderList[index]);
      this.orderList.index = this.orderList[index];
    },
    handleContinuePay(row) {
      this.selectedOrder = row;
      this.handlePayOrder();
    },
    async handlePayOrder() {
      const res = await this.$services.rdpAuthCodeOrderPay({
        data: {
          orderId: this.selectedOrder.orderId,
          payType: this.selectedOrder.payType || 'ALI_PAY'
        }
      });
      if (res.success) {
        console.log('hello', res);
        this.showPaymentConfirm = true;
        window.open(res.data);
      }
    },
    async handleCloseOrder() {
      try {
        const res = await this.$services.rdpAuthCodeOrderClose({
          data: {
            orderId: this.selectedOrder.orderId
          }
        });
        if (res?.success) {
          this.$Message.success(this.$t('guan-bi-ding-dan-cheng-gong'));
          this.showCloseOrderModal = false;
          this.queryOrder();
        }
      } catch (e) {
        console.log(e);
      }
    },
    handleCancel() {
      this.showCloseOrderModal = false;
      this.showLicenseSmsModal = false;
      this.showAuthCodeModal = false;
    },
    handleConfirmFinish() {
      this.queryOrder();
      this.showPaymentConfirm = false;
    },
    handleShowAuthCode(row) {
      this.selectedOrder = row;
      this.showLicenseSmsModal = true;
    },
    async handleConfirmShowAuthCode(verifyCode) {
      const res = await this.$services.rdpAuthCodeOrderShowAuthCode({
        data: {
          orderId: this.selectedOrder.orderId,
          verifyCode,
          verifyType: this.verifyType
        }
      });
      if (res?.success) {
        this.showAuthCodeModal = true;
        this.authCode = `${res.data.authCode}CloudCanal${res.data.secondAuthCode}`;
      }
    },
    handleCopyLicense() {
      handleCopy(this.authCode);
      this.$Message.success(this.$t('fu-zhi-cheng-gong'));
    },
    handleCreateOrder() {
      this.$router.push('/system/license');
    }
  }
};
</script>
<style scoped>
.order {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
