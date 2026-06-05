<template>
  <div class="billing">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('zhang-dan') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div>
            <div>
              <div class="sm:flex sm:items-center">
                <DatePicker
                  class="mr-4"
                  type="datetimerange"
                  v-model="timeRange"
                  :placeholder="$t('qing-xuan-ze-shi-jian-fan-wei')"
                  style="width: 320px"
                />
                <Button :loading="loading" @click="handleSearch" type="primary">
                  {{ $t('cha-xun') }}
                </Button>
              </div>
              <div class="mt-6 flow-root">
                <div class="overflow-x-auto">
                  <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                    <Table size="small" :columns="billingColumns" :data="billingList" @on-expand="handleGetBillingDetail" :loading="loading">
                      <template #timeRange="{ row }">
                        {{ dayjs(row.startTimeMs).format('YYYY/MM/DD') }} -
                        {{ dayjs(row.endTimeMs).format('YYYY/MM/DD') }}
                      </template>
                      <template #totalPrice="{ row }">{{ transformScientific(row.totalPrice) }}{{ row.currencyI18n }}</template>
                      <template #actualPrice="{ row }">{{ transformScientific(row.actualPrice) }}{{ row.currencyI18n }}</template>
                      <template #voucherSaved="{ row }">{{ transformScientific(row.voucherSaved) }}{{ row.currencyI18n }}</template>
                      <template #balanceDeduct="{ row }">{{ transformScientific(row.balanceDeduct) }}{{ row.currencyI18n }}</template>
                    </Table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          ref="pages"
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          v-model="this.searchKey.pageNum"
          :page-size="this.searchKey.pageSize"
        />
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from 'dayjs';
import BillingDetail from '@/views/system/billingandpayment/BillingDetail';
import { transformScientific } from '@/utils/tools';

export default {
  name: 'Billing',
  // eslint-disable-next-line vue/no-unused-components
  components: { BillingDetail },
  data() {
    return {
      dayjs,
      loading: false,
      timeRange: [],
      billingColumns: [
        {
          title: this.$t('zhang-dan-zhou-qi'),
          slot: 'timeRange',
          minWidth: 240
        },
        {
          title: this.$t('zong-jin-e'),
          slot: 'totalPrice',
          minWidth: 180
        },
        {
          title: this.$t('shi-fu-jin-e'),
          slot: 'actualPrice',
          minWidth: 180
        },
        {
          title: this.$t('dai-jin-quan-kou-jian'),
          slot: 'voucherSaved',
          minWidth: 180
        },
        {
          title: this.$t('yu-chong-zhi-kou-jian'),
          slot: 'balanceDeduct',
          minWidth: 180
        }
      ],
      billingList: [],
      searchKey: {
        startTimeMs: null,
        endTimeMs: null,
        pageNum: 1,
        pageSize: 20
      },
      total: 0
    };
  },
  mounted() {
    this.handleSearch();
  },
  methods: {
    transformScientific,
    async handleSearch() {
      this.searchKey.pageNum = 1;
      this.$refs.pages.currentPage = 1;
      await this.listBillings();
    },
    async listBillings() {
      this.loading = true;
      this.searchKey.startTimeMs = this.timeRange ? new Date(this.timeRange[0]).getTime() : null;
      this.searchKey.endTimeMs = this.timeRange ? new Date(this.timeRange[1]).getTime() : null;
      const res = await this.$services.rdpBillingList({
        data: this.searchKey
      });
      if (res && res.success) {
        this.billingList = res.data.billingVOS;
        this.total = res.data.totalCount;
      }
      this.loading = false;
    },
    handleGoDetail(row) {
      this.$router.push({
        path: `/system/billing/${row.id}`
      });
    },
    async handleGetBillingDetail(billing, status) {
      if (!billing.detail && status) {
        const res = await this.$services.rdpBillingGetDetailById({
          data: {
            id: billing.id
          }
        });
        if (res && res.success) {
          this.billingList.forEach((theBilling) => {
            if (theBilling.id === billing.id) {
              res.data.jobVOS.forEach((data) => {
                if (data.taskVOS) {
                  data.children = data.taskVOS;
                }
              });
              theBilling.detail = res.data.jobVOS;
              billing.detail = res.data.jobVOS;
            }
          });
          this.billingList = [...this.billingList];
          console.log('this.billingList', this.billingList);
        }
      }
    },
    handlePageChange(pageNum) {
      this.searchKey.pageNum = pageNum;
      this.listBillings();
    },
    handlePageSizeChange(pageSize) {
      this.searchKey.pageSize = pageSize;
      this.handleSearch();
    }
  }
};
</script>
<style>
.billing {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
