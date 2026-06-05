<template>
  <div class="billingDetail">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/system/billing">{{ $t('zhang-dan') }}</BreadcrumbItem>
            <BreadcrumbItem>
              {{ dayjs(billingDetail.startTimeMs).format('YYYY/MM/DD') }} -
              {{ dayjs(billingDetail.endTimeMs).format('YYYY/MM/DD') }}
            </BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="sm:flex sm:items-center">
            <Select class="mr-4" style="width: 180px" v-model="ccProductName">
              <Option v-for="product in productNameList" :value="product" :key="product">
                {{ product }}
              </Option>
            </Select>
            <Button :loading="loading" @click="handleGetBillingDetail" type="primary">
              {{ $t('cha-xun') }}
            </Button>
          </div>
          <div class="mt-6 flow-root">
            <div class="border border-solid border-gray-200 rounded">
              <Table row-key="jobName" size="small" :columns="columns" :data="jobVOS">
                <template #totalPrice="{ row }">${{ transformScientific(row.totalPrice) }}</template>
              </Table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from 'dayjs';
import { transformScientific } from '@/utils/tools';

export default {
  name: 'BillingDetail',
  data() {
    return {
      dayjs,
      billingId: null,
      columns: [
        {
          title: this.$t('ren-wu-ming-cheng'),
          key: 'jobName',
          tree: true
        },
        {
          title: this.$t('shi-yong-liang'),
          key: 'dataCount'
        },
        {
          title: this.$t('zong-jia'),
          slot: 'totalPrice'
        }
      ],
      jobVOS: [],
      productNameList: [],
      ccProductName: '',
      billingDetail: {},
      loading: false
    };
  },
  created() {
    this.billingId = this.$route.params.id;
  },
  async mounted() {
    await this.saasCcProductNames();
    await this.handleGetBillingDetail();
  },
  methods: {
    transformScientific,
    async handleGetBillingDetail() {
      this.loading = true;
      const res = await this.$services.rdpBillingGetDetailById({
        data: {
          id: this.billingId,
          ccProductName: this.ccProductName
        }
      });
      if (res && res.success) {
        this.billingDetail = res.data;
        this.jobVOS = [];
        this.billingDetail.jobVOS.forEach((job) => {
          const children = [];
          job.taskVOS.forEach((task) => {
            children.push({
              id: task.taskId,
              jobName: task.taskName,
              dataCount: task.totalCount,
              totalPrice: task.totalPrice
            });
          });
          this.jobVOS.push({
            id: job.id,
            jobName: job.jobName,
            dataCount: '',
            totalPrice: job.totalPrice,
            children
          });
        });
      }
      this.loading = false;
    },
    async saasCcProductNames() {
      const res = await this.$services.rdpSaasCcProductNames();
      if (res && res.success) {
        this.productNameList = res.data;
        this.ccProductName = res.data[0];
      }
    },
    handleChangeProduct(data) {
      this.ccProductName = data;
      this.handleGetBillingDetail();
    }
  }
};
</script>
