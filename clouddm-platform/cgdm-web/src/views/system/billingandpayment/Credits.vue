<template>
  <div class="credits">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('you-hui-quan') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="w-full">
            <Button class="refresh float-right" @click="listVouchers" :loading="loading">
              {{ ' ' }}
              <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
            </Button>
          </div>
          <div>
            <div class="mt-4 flow-root">
              <div class="overflow-x-auto">
                <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                  <Table size="small" :columns="voucherColumns" :data="voucherList" :loading="loading">
                    <template #totalPrice="{ row }">{{ row.totalPrice }} {{ row.currencyI18n }}</template>
                    <template #voucherType="{ row }">
                      {{ VOUCHER_TYPE[row.voucherType] }}
                    </template>
                    <template #restPrice="{ row }">{{ row.restPrice }} {{ row.currencyI18n }}</template>
                    <template #timeRange="{ row }">
                      {{ dayjs(row.startTimeMs).format('YYYY/MM/DD') }} -
                      {{ dayjs(row.endTimeMs).format('YYYY/MM/DD') }}
                    </template>
                  </Table>
                </div>
              </div>
            </div>
            <div v-if="voucherTotal > pageSize">
              <Page class="mt-4 float-right" size="small" :total="voucherTotal" @on-change="handleChangeVoucherPage"></Page>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from 'dayjs';
import { VOUCHER_TYPE } from '@/utils';

export default {
  name: 'Credits',
  data() {
    return {
      VOUCHER_TYPE,
      voucherTotal: 0,
      pageSize: 20,
      pageNum: 1,
      loading: false,
      voucherColumns: [
        {
          title: this.$t('dai-jin-quan-you-xiao-qi'),
          slot: 'timeRange'
        },
        {
          title: this.$t('lei-xing'),
          slot: 'voucherType'
        },
        {
          title: this.$t('dai-jin-quan-zong-e-du'),
          slot: 'totalPrice'
        },
        {
          title: this.$t('dai-jin-quan-sheng-yu'),
          slot: 'restPrice'
        }
      ],
      voucherList: []
    };
  },
  created() {
    this.listVouchers();
  },
  methods: {
    dayjs,
    async listVouchers() {
      this.loading = true;
      const res = await this.$services.rdpVoucherList({
        data: {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      });
      if (res && res.success) {
        this.voucherList = res.data.voList;
        this.voucherTotal = res.data.totalCount;
      }
      this.loading = false;
    },
    handleChangeVoucherPage(page) {
      this.pageNum = page;
      this.listVouchers();
    }
  }
};
</script>
<style>
.credits {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
