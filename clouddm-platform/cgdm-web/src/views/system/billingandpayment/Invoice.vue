<template>
  <div class="invoice">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('fa-piao') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="sm:flex sm:items-center">
            <div class="sm:flex sm:items-center">
              <DatePicker class="mr-4" type="daterange" v-model="timeRange" :placeholder="$t('qing-xuan-ze-shi-jian-fan-wei')" style="width: 300px" />
              <Button :loading="loading" @click="handleSearch" type="primary">
                {{ $t('cha-xun') }}
              </Button>
            </div>
          </div>
          <div class="mt-6 flow-root">
            <div class="overflow-x-auto">
              <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                <Table :columns="invoiceColumns" :data="invoiceList" size="small">
                  <template #timeRange="{ row }">
                    {{ dayjs(row.periodStart * 1000).format('YYYY/MM/DD') }} -
                    {{ dayjs(row.periodEnd * 1000).format('YYYY/MM/DD') }}
                  </template>
                  <template #totalAmount="{ row }">${{ row.totalAmount / 100 }}</template>
                  <template #subscriptionId="{ row }">
                    <a :href="row.hostedInvoiceUrl" target="_blank" class="text-cc-primary">
                      {{ row.invoiceId }}
                    </a>
                  </template>
                  <template #action="{ row }">
                    <a :href="row.invoicePdf" class="text-cc-primary">{{ $t('xia-zai') }}</a>
                  </template>
                </Table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
        <Button :disabled="prevPageList.length < 1" style="font-size: 16px; padding: 0 16px 0 10px" @click="handleSearch('prev')">
          <Icon type="ios-arrow-back" style="font-size: 16px" />
          {{ $t('shang-yi-ye') }}
        </Button>
        <span style="margin: 0 10px">{{ $t('di-page-ye', [page]) }}</span>
        <Button :disabled="!nextPage" style="font-size: 16px; padding: 0 16px 0 10px; margin-left: 5px" @click="handleSearch('next')">
          <Icon type="ios-arrow-forward" style="font-size: 16px" />
          {{ $t('xia-yi-ye') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from 'dayjs';

export default {
  name: 'Invoice',
  data() {
    return {
      dayjs,
      loading: false,
      timeRange: [],
      invoiceColumns: [
        {
          title: this.$t('fa-piao-ri-qi'),
          slot: 'timeRange',
          minWidth: 240
        },
        {
          title: this.$t('zong-jin-e'),
          slot: 'totalAmount',
          minWidth: 150
        },
        {
          title: this.$t('zhi-fu-zhuang-tai'),
          key: 'status',
          minWidth: 120
        },
        {
          title: this.$t('fa-piao-id'),
          slot: 'subscriptionId',
          minWidth: 250
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          width: 150
        }
      ],
      invoiceList: [],
      searchInfo: {
        beginTs: null,
        endTs: null,
        status: '',
        page: null,
        limit: 10
      },
      nextPage: '',
      prevPageList: [],
      page: 0
    };
  },
  mounted() {
    this.handleSearch();
  },
  methods: {
    async handleSearch(type) {
      this.loading = true;
      if (!type) {
        this.nextPage = null;
        this.prevPageList = [];
        this.page = 0;
        type = 'next';
      }
      this.searchInfo.beginTs = this.timeRange ? new Date(this.timeRange[0]).getTime() / 1000 : null;
      this.searchInfo.endTs = this.timeRange ? new Date(this.timeRange[1]).getTime() / 1000 : null;
      if (type === 'next') {
        this.searchInfo.page = this.nextPage;
        this.page++;
      } else if (type === 'prev') {
        this.prevPageList.pop();
        this.searchInfo.page = this.prevPageList[this.prevPageList.length - 1];
        this.page--;
      }
      const res = await this.$services.rdpSaasListInvoices({
        data: this.searchInfo
      });
      if (res && res.success) {
        this.invoiceList = res.data.invoiceList || [];
        if (type === 'next') {
          if (this.nextPage) {
            this.prevPageList.push(this.nextPage);
          }
        }
        this.nextPage = res.data.nextPage;
      }
      this.loading = false;
    }
  }
};
</script>
<style>
.invoice {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
