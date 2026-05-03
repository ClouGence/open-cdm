<template>
  <div class="payment">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('fu-kuan') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="pt-4 pb-20 sm:pb-24">
            <div class="text-center" v-if="paymentList.length === 0">
              <svg
                t="1734057363337"
                class="mx-auto w-28 h-28 text-gray-400"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="4264"
                width="200"
                height="200"
              >
                <path
                  d="M928 128H96C43.2 128 0 171.2 0 224v576c0 52.8 43.2 96 96 96h832c52.8 0 96-43.2 96-96V224c0-52.8-43.2-96-96-96zM96 192h832c17.344 0 32 14.656 32 32v96H64V224c0-17.344 14.656-32 32-32z m832 640H96c-17.344 0-32-14.656-32-32v-288h896v288c0 17.344-14.656 32-32 32zM128 640h64v128H128z m128 0h64v128H256z m128 0h64v128h-64z"
                  p-id="4265"
                ></path>
              </svg>
              <h3 class="mt-10 text-2xl font-semibold text-gray-900">
                {{ $t('tian-jia-ni-de-ka-pian') }}
              </h3>
              <p class="mt-4 text-xl text-gray-700">{{ $t('xin-zeng-ka-pian-yi-shi-yong') }}</p>
              <!--        <p class="mt-1 text-sm text-gray-500">Get started by creating a new project.</p>-->
              <!--              <button @click="handleGoStripe" type="button" class="mt-4 inline-flex items-center rounded-md bg-black px-3 py-2 text-center text-lg font-semibold text-white shadow-sm hover:bg-slate-900">-->
              <!--                <svg t="1734058834566" class="w-5 h-5 mr-2" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6083" width="200" height="200"><path d="M863.328262 481.340895l-317.344013 0.099772L545.984249 162.816826c0-17.664722-14.336138-32.00086-32.00086-32.00086s-31.99914 14.336138-31.99914 32.00086l0 318.400215-322.368714-0.17718c-0.032684 0-0.063647 0-0.096331 0-17.632039 0-31.935493 14.239806-32.00086 31.904529-0.096331 17.664722 14.208843 32.031824 31.871845 32.095471l322.59234 0.17718 0 319.167424c0 17.695686 14.336138 32.00086 31.99914 32.00086s32.00086-14.303454 32.00086-32.00086L545.982529 545.440667l317.087703-0.099772c0.063647 0 0.096331 0 0.127295 0 17.632039 0 31.935493-14.239806 32.00086-31.904529S880.960301 481.404542 863.328262 481.340895z" fill="#ffffff" p-id="6084"></path></svg>-->
              <!--                {{$t('xin-zeng-ka-pian')}}-->
              <!--              </button>-->
              <Button :loading="paymentLoading" @click="handleGoStripe" icon="md-add" type="primary" class="mt-4">
                {{ $t('xin-zeng-ka-pian') }}
              </Button>
            </div>
            <div v-if="paymentList.length > 0">
              <div class="sm:flex sm:items-center">
                <div class="sm:flex-auto">
                  <p class="mt-2 text-xl text-gray-700 font-medium">
                    {{ $t('yi-tian-jia-de-zhi-fu-ka') }}
                  </p>
                </div>
              </div>
              <div class="mt-4 flow-root">
                <div class="overflow-x-auto">
                  <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                    <Table :columns="paymentColumns" :data="paymentList" size="small" :loading="paymentLoading">
                      <template #cardNumber="{ row }">****{{ row.last4 }}</template>
                      <template #status="{ row }">
                        {{ row.subscriptionId ? $t('yi-ding-yue') : $t('wei-ding-yue') }}
                      </template>
                      <template #setting="{ row }">
                        <div>
                          <Poptip
                            v-if="row.subscriptionId"
                            transfer
                            confirm
                            width="300"
                            :title="
                              $t('que-ren-yao-qu-xiao-ding-yue-ma-qing-que-bao-zhi-shao-ding-yue-yi-zhang-ka-fou-ze-kn-neng-ying-xinag-kou-kuan')
                            "
                            @on-ok="handleSubscribe(row, false)"
                          >
                            <a class="text-cc-primary">{{ $t('qu-xiao-ding-yue') }}</a>
                          </Poptip>
                          <Poptip
                            v-if="!row.subscriptionId"
                            transfer
                            confirm
                            width="300"
                            :title="$t('qing-que-ren-shi-fou-ding-yue-ding-yue-cheng-gong-hou-jiang-yi-zhe-zhang-ka-jin-xing-kou-fei')"
                            @on-ok="handleSubscribe(row, true)"
                          >
                            <a class="text-cc-primary">{{ $t('ding-yue') }}</a>
                          </Poptip>
                        </div>
                      </template>
                    </Table>
                  </div>
                </div>
              </div>
              <Button :loading="paymentLoading" @click="handleGoStripe" icon="md-add" type="primary" class="mt-4">
                {{ $t('xin-zeng-ka-pian') }}
              </Button>
            </div>
            <div class="mt-20" v-if="upComingData.length > 0">
              <div class="sm:flex sm:items-center">
                <div class="sm:flex-auto">
                  <p class="mt-2 text-xl text-gray-700 font-medium">
                    {{ $t('ji-jiang-dao-lai-de-zhang-dan') }}
                  </p>
                </div>
              </div>
              <div class="mt-4 flow-root">
                <div class="overflow-x-auto">
                  <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                    <Table size="small" :columns="upComingColumns" :data="upComingData" :loading="upComingLoading">
                      <template #created="{ row }">
                        {{ dayjs(row.created * 1000).format('YYYY/MM/DD') }}
                      </template>
                      <template #nextPaymentAttempt="{ row }">
                        {{ dayjs(row.nextPaymentAttempt * 1000).format('YYYY/MM/DD') }}
                      </template>
                      <template #totalPrice="{ row }">${{ row.totalAmount / 100 }}</template>
                      <template #timeRange="{ row }">
                        {{ dayjs(row.periodStart * 1000).format('YYYY/MM/DD') }} -
                        {{ dayjs(row.periodEnd * 1000).format('YYYY/MM/DD') }}
                      </template>
                    </Table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import dayjs from 'dayjs';
import { mapState } from 'vuex';

export default {
  name: 'Payment',
  data() {
    return {
      dayjs,
      stripeUrl: '',
      payTypeList: [],
      paymentColumns: [
        {
          title: this.$t('ka-pian-lei-xing'),
          key: 'brand'
        },
        {
          title: this.$t('zhang-hao'),
          key: 'name'
        },
        {
          title: this.$t('ka-hao'),
          slot: 'cardNumber'
        },
        {
          title: this.$t('zhuang-tai'),
          slot: 'status'
        },
        {
          title: this.$t('she-zhi-0'),
          slot: 'setting',
          width: 180
        }
      ],
      paymentList: [],
      upComingColumns: [
        {
          title: this.$t('xia-yi-zhang-dan-ri'),
          slot: 'nextPaymentAttempt'
        },
        {
          title: this.$t('zong-jin-e'),
          slot: 'totalPrice'
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          slot: 'created'
        },
        {
          title: this.$t('zhang-dan-zhou-qi'),
          slot: 'timeRange'
        }
      ],
      upComingData: [],
      paymentLoading: false,
      upComingLoading: false,
      pageSize: 20,
      pageNum: 1
    };
  },
  computed: {
    ...mapState(['userInfo'])
  },
  async created() {
    await this.listPayments();
    if (this.userInfo.saasUserStatus === 'SAAS_NORMAL' || this.userInfo.saasUserStatus === 'SAAS_LOCKED') {
      await this.listUpComing();
    }
  },
  beforeUnmount() {
    this.paymentLoading = false;
  },
  methods: {
    async listPayTypes() {
      const res = await this.$services.rdpAuthCodeOrderListPayTypes();
      if (res?.success) {
        this.payTypeList = res.data;
        console.log('payTypeList', this.payTypeList);
      }
    },
    async listPayments() {
      this.paymentLoading = true;
      const res = await this.$services.rdpSaasListPaymentMethods();
      if (res && res.success) {
        this.paymentList = res.data.paymentMethods;
      }
      this.paymentLoading = false;
    },
    async listUpComing() {
      this.upComingLoading = true;
      const res = await this.$services.rdpSaasRetrieveUpcomingInvoice({
        data: {}
      });
      if (res && res.success) {
        this.upComingData = [];
        this.upComingData.push(res.data);
      }
      this.upComingLoading = false;
    },
    async createSession(sessionType) {
      const res = await this.$services.rdpSaasCreateSession({
        data: {
          sessionType
        }
      });
      if (res && res.success) {
        this.stripeUrl = res.data;
      }
    },
    async handleSubscribe(payment, ifSubscribe) {
      this.paymentLoading = true;
      if (ifSubscribe) {
        const res = await this.$services.rdpSaasSubscription({
          data: {
            paymentMethodId: payment.id
          }
        });
      } else {
        const res = await this.$services.rdpSaasCancelSubscription({
          data: {
            subscriptionId: payment.subscriptionId
          }
        });
      }
      await this.listPayments();
      await this.$store.dispatch('getUserInfo');
    },
    async handleGoStripe() {
      try {
        this.paymentLoading = true;
        if (!this.stripeUrl) {
          if (this.paymentList.length > 0) {
            await this.createSession('PORTAL');
          } else {
            await this.createSession('CHECKOUT');
          }
        }
        window.location.href = this.stripeUrl;
      } catch (e) {
        console.log('err', e);
      }
    }
  }
};
</script>
