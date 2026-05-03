<template>
  <div class="invoiceApply">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/system/invoiceApplyList">
              {{ $t('fa-piao-shen-qing-lie-biao') }}
            </BreadcrumbItem>
            <BreadcrumbItem>{{ $t('shen-qing-fa-piao') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content mt-4">
          <Form :model="form" :rules="rules" :label-width="120" v-if="currentStep === 0" ref="formRef">
            <FormItem :label="$t('fa-piao-lei-xing')" prop="invoiceType">
              <RadioGroup v-model="form.invoiceBizType" type="button" @on-change="handleInvoiceBizTypeChange">
                <Radio :label="INVOICE_TYPE.LICENSE">{{ INVOICE_TYPE_I18N.LICENSE }}</Radio>
                <Radio :label="INVOICE_TYPE.MONTHLY_BILL">{{ INVOICE_TYPE_I18N.MONTHLY_BILL }}</Radio>
              </RadioGroup>
            </FormItem>
            <FormItem :label="$t('ke-kai-piao-lie-biao')">
              <Table
                :columns="columns"
                :data="list"
                border
                size="small"
                @on-selection-change="handleSelectMonthlyBill"
                :loading="listLoading || calculateInvoiceAmountLoading"
              >
                <template #radio="{ row }">
                  <Radio v-model="row.selected" @on-change="handleSelectOrder(row)">{{ '' }}</Radio>
                </template>
                <template #orderCreateTime="{ row }">
                  {{ dayjs(row.orderCreateTime).format('YYYY-MM-DD HH:mm:ss') }}
                </template>
                <template #totalPrice="{ row }">{{ transformScientific(row.totalPrice || row.amount) }} {{ row.currencyI18n }}</template>
              </Table>
              <div class="flex justify-end mt-[10px]">
                <Page
                  size="small"
                  show-total
                  :total="this[form.invoiceBizType].count"
                  :model-value="this[form.invoiceBizType].pageNum"
                  :page-size="this[form.invoiceBizType].pageSize"
                  @on-change="handlePageChange"
                />
              </div>
            </FormItem>
          </Form>
          <div v-if="currentStep === 1">
            <Form :model="chinaInvoiceForm" :rules="chinaInvoiceFormRules" :label-width="140" ref="chinaInvoiceFormRef">
              <FormItem :label="$t('ke-kai-piao-jin-e')">
                <div class="flex items-baseline">
                  <span>{{ unInVoiceAmount.amount }}</span>
                  <span>{{ unInVoiceAmount.currencyI18n }}</span>
                </div>
              </FormItem>
              <FormItem :label="$t('kai-piao-zhong-lei')" prop="chineseInvoiceType">
                <RadioGroup v-model="chinaInvoiceForm.chineseInvoiceType" type="button">
                  <Radio label="VAT_NORMAL_INVOICE">{{ $t('zeng-zhi-shui-pu-tong-fa-piao') }}</Radio>
                  <Radio label="VAT_SPECIAL_INVOICE">{{ $t('zeng-zhi-shui-zhuan-yong-fa-piao') }}</Radio>
                </RadioGroup>
              </FormItem>
              <FormItem :label="$t('shui-shuai')">6%</FormItem>
              <FormItem :label="$t('fa-piao-tai-tou')" prop="chineseInvoiceTitle">
                <Input v-model="chinaInvoiceForm.chineseInvoiceTitle" :placeholder="$t('qing-tian-xie-fa-piao-tai-tou')" />
              </FormItem>
              <FormItem :label="$t('shui-hao')" prop="chineseTaxNo">
                <Input v-model="chinaInvoiceForm.chineseTaxNo" :placeholder="$t('qing-tian-xie-shui-hao')" />
              </FormItem>
              <FormItem :label="$t('kai-hu-hang')" prop="chineseTaxBank">
                <Input v-model="chinaInvoiceForm.chineseTaxBank" :placeholder="$t('qing-tian-xie-kai-hu-hang')" />
              </FormItem>
              <FormItem :label="$t('kai-hu-hang-zhang-hao')" prop="chineseTaxBankNo">
                <Input v-model="chinaInvoiceForm.chineseTaxBankNo" :placeholder="$t('qing-tian-xie-kai-hu-hang-zhang-hao')" />
              </FormItem>
              <FormItem :label="$t('zhu-ce-di-zhi')" prop="chineseTaxRegAddr">
                <Input v-model="chinaInvoiceForm.chineseTaxRegAddr" :placeholder="$t('qing-tian-xie-zhu-ce-di-zhi')" />
              </FormItem>
              <FormItem :label="$t('dian-hua')" prop="chineseTaxPhone">
                <Input v-model="chinaInvoiceForm.chineseTaxPhone" :placeholder="$t('qing-tian-xie-dian-hua')" />
              </FormItem>
              <FormItem :label="$t('fa-piao-jie-shou-you-xiang')" prop="targetEmail">
                <template #label>
                  <span>{{ $t('fa-piao-jie-shou-you-xiang') }}</span>
                  <a-tooltip :title="$t('kai-piao-cheng-gong-hou-jiang-zi-dong-fa-song-dao-gai-you-xiang')" placement="top" class="ml-[10px]">
                    <Icon type="ios-information-circle" />
                  </a-tooltip>
                </template>
                <Input v-model="chinaInvoiceForm.targetEmail" :placeholder="$t('qing-tian-xie-fa-piao-jie-shou-you-xiang')" />
              </FormItem>
            </Form>
          </div>
        </div>
      </div>
      <div class="footer pr-[16px]">
        <div class="flex mr-[10px] items-baseline" v-if="form.invoiceBizInfo.length">
          <span>{{ $t('ke-kai-piao-jin-e') }}：</span>
          <span class="font-bold text-[24px]">{{ unInVoiceAmount.amount }}</span>
          <span>{{ unInVoiceAmount.currencyI18n }}</span>
        </div>
        <Button @click="handlePreStep" :disabled="!form.invoiceBizInfo.length" v-if="currentStep === 1" class="mr-[10px] float-left">
          {{ $t('shang-yi-bu') }}
        </Button>
        <Button
          type="primary"
          @click="handleNextStep"
          :disabled="!form.invoiceBizInfo.length || calculateInvoiceAmountLoading"
          :loading="calculateInvoiceAmountLoading"
          v-if="currentStep === 0 && form.invoiceBizInfo.length && unInVoiceAmount.amount > 0"
        >
          {{ $t('xia-yi-bu') }}
        </Button>
        <Tooltip placement="right" transfer>
          <template #content>
            {{ $t('qing-xuan-ze-ke-kai-piao-xiang-mu-bing-qie-jineda-yu-0') }}
          </template>
          <Button
            type="primary"
            @click="handleNextStep"
            :loading="calculateInvoiceAmountLoading"
            disabled
            v-if="(currentStep === 0 && !form.invoiceBizInfo.length) || (form.invoiceBizInfo.length && unInVoiceAmount.amount <= 0)"
          >
            {{ $t('xia-yi-bu') }}
          </Button>
        </Tooltip>
        <Button type="primary" @click="handleApply" v-if="currentStep === 1">{{ $t('shen-qing-fa-piao') }}</Button>
      </div>
    </div>
  </div>
</template>
<script>
import { INVOICE_TYPE, INVOICE_TYPE_I18N } from '@/utils';
import { mapGetters, mapState } from 'vuex';
import dayjs from 'dayjs';
import { transformScientific } from '@/utils/tools';

export default {
  name: 'InvoiceApply',
  computed: {
    ...mapState(['globalSetting']),
    ...mapGetters(['userInfo']),
    columns() {
      return this[this.form.invoiceBizType].columns;
    },
    list() {
      return this[this.form.invoiceBizType].list;
    }
  },
  data() {
    return {
      calculateInvoiceAmountLoading: false,
      currentStep: 0,
      selectedOrderId: '',
      INVOICE_TYPE,
      INVOICE_TYPE_I18N,
      chinaInvoiceForm: {
        chineseInvoiceType: 'VAT_NORMAL_INVOICE',
        chineseTaxRate: '6%',
        chineseInvoiceTitle: '',
        chineseTaxNo: '',
        chineseTaxBank: '',
        chineseTaxBankNo: '',
        chineseTaxRegAddr: '',
        chineseTaxPhone: '',
        targetEmail: ''
      },
      chinaInvoiceFormRules: {
        chineseInvoiceType: [{ required: true, message: this.$t('qing-xuan-ze-kai-piao-lei-xing'), trigger: 'change' }],
        chineseInvoiceTitle: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-tai-tou'), trigger: 'change' }],
        chineseTaxNo: [{ required: true, message: this.$t('qing-tian-xie-shui-hao'), trigger: 'change' }],
        chineseTaxBank: [{ required: true, message: this.$t('qing-tian-xie-kai-hu-hang'), trigger: 'change' }],
        chineseTaxBankNo: [{ required: true, message: this.$t('qing-tian-xie-kai-hu-hang-zhang-hao'), trigger: 'change' }],
        targetEmail: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-jie-shou-you-xiang-0'), trigger: 'change' }]
      },
      foreignInvoiceForm: {
        overseaTaxTo: '',
        overseaTaxAddr: '',
        targetEmail: ''
      },
      foreignInvoiceFormRules: {
        overseaTaxTo: [{ required: true, message: this.$t('qing-tian-xie-tai-tou'), trigger: 'change' }],
        overseaTaxAddr: [{ required: true, message: this.$t('qing-tian-xie-di-zhi'), trigger: 'change' }],
        targetEmail: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-jie-shou-you-xiang-0'), trigger: 'change' }]
      },
      form: {
        invoiceBizType: INVOICE_TYPE.LICENSE,
        invoiceBizInfo: []
      },
      rules: {
        invoiceBizInfo: [{ required: true, message: this.$t('qing-xuan-ze-kai-piao-xiang-mu'), trigger: 'change' }]
      },
      LICENSE: {
        columns: [
          {
            width: 50,
            slot: 'radio'
          },
          {
            title: this.$t('ding-dan-id-0'),
            minWidth: 160,
            key: 'orderId'
          },
          {
            title: this.$t('ding-dan-chuang-jian-shi-jian'),
            width: 170,
            slot: 'orderCreateTime'
          },
          {
            title: this.$t('gou-mai-chan-pin'),
            width: 160,
            key: 'productType'
          },
          {
            title: this.$t('gou-mai-ban-ben'),
            width: 160,
            key: 'buyTypeI18n'
          },
          {
            title: this.$t('ke-kai-piao-jin-e'),
            minWidth: 100,
            slot: 'totalPrice'
          }
        ],
        list: [],
        count: 0,
        pageNum: 1,
        pageSize: 10
      },
      MONTHLY_BILL: {
        columns: [
          {
            type: 'selection',
            width: 60
          },
          {
            title: this.$t('nian-yue'),
            key: 'yearMonth'
          },
          {
            title: this.$t('ke-kai-piao-e'),
            slot: 'totalPrice'
          }
        ],
        list: [],
        count: 0,
        pageNum: 1,
        pageSize: 10
      },
      unInVoiceAmount: {
        amount: 0,
        currency: '',
        currencyI18n: ''
      }
    };
  },
  created() {
    this.getUnInvoicedList();
  },
  methods: {
    dayjs,
    transformScientific,
    handlePageChange(pageNum) {
      this[this.form.invoiceBizType].pageNum = pageNum;
      this.getUnInvoicedList();
    },
    handleSelectOrder(row) {
      this[this.form.invoiceBizType].list.forEach((item) => {
        item.selected = item.orderId === row.orderId;
      });
      this.form.invoiceBizInfo = [row.orderId];
      this.calculateInvoiceAmount();
    },
    handleSelectMonthlyBill(selection) {
      this.form.invoiceBizInfo = selection.map((item) => item.id);
      if (this.form.invoiceBizInfo.length) {
        this.calculateInvoiceAmount();
      } else {
        this.unInVoiceAmount = {
          amount: 0,
          currency: '',
          currencyI18n: ''
        };
      }
    },
    async calculateInvoiceAmount() {
      this.calculateInvoiceAmountLoading = true;
      const res = await this.$services.rdpFinanceCalculateInvoiceAmount({
        data: {
          invoiceBizType: this.form.invoiceBizType,
          invoiceBizInfo: this.form.invoiceBizInfo.join(',')
        }
      });

      if (res.success) {
        this.unInVoiceAmount = res.data;
      }
      this.calculateInvoiceAmountLoading = false;
    },
    handleNextStep() {
      if (this.currentStep === 0) {
        this.$refs.formRef.validate((valid) => {
          if (valid) {
            this.currentStep++;
          }
        });
      }
    },
    handlePreStep() {
      this.currentStep--;
    },
    async handleApply() {
      this.$refs.chinaInvoiceFormRef.validate((valid) => {
        if (valid) {
          this.applyInvoice(this.chinaInvoiceForm);
        }
      });
    },
    async applyInvoice(form) {
      const res = await this.$services.rdpFinanceApplyInvoice({
        data: {
          uid: this.userInfo.uid,
          invoiceBizType: this.form.invoiceBizType,
          invoiceBizInfo: this.form.invoiceBizInfo.join(','),
          globalDeploySite: 'china',
          ...form
        }
      });

      if (res.success) {
        this.$Message.success(this.$t('shen-qing-fa-piao-cheng-gong'));
        this.$router.push({
          name: 'InvoiceApplyList'
        });
      } else {
        this.$Message.error(this.$t('shen-qing-fa-piao-shi-bai'));
      }
    },
    handleInvoiceBizTypeChange(value) {
      this.form.invoiceBizInfo = [];
      this.form.targetEmail = '';
      this.form.invoiceTitle = '';
      this.LICENSE = {
        ...this.LICENSE,
        pageNum: 1
      };
      this.MONTHLY_BILL = {
        ...this.MONTHLY_BILL,
        pageNum: 1
      };
      this.unInVoiceAmount = {
        amount: 0,
        currency: '',
        currencyI18n: ''
      };
      this.chinaInvoiceForm = {
        chineseInvoiceType: 'VAT_NORMAL_INVOICE',
        chineseTaxRate: '6%',
        chineseInvoiceTitle: '',
        chineseTaxNo: '',
        chineseTaxBank: '',
        chineseTaxBankNo: '',
        chineseTaxRegAddr: '',
        chineseTaxPhone: '',
        targetEmail: ''
      };
      this.foreignInvoiceForm = {
        overseaTaxTo: '',
        overseaTaxAddr: '',
        targetEmail: ''
      };
      this.listLoading = false;
      this.getUnInvoicedList();
    },
    async getUserUnInvoicedOrderList() {
      this.listLoading = true;
      const res = await this.$services.rdpFinanceUserUnInvoicedOrderCount({
        data: {
          uid: this.userInfo.uid
        }
      });

      if (res.success) {
        this[this.form.invoiceBizType].count = res.data;
      }

      const res2 = await this.$services.rdpFinancePageListUserUnInvoicedOrder({
        data: {
          uid: this.userInfo.uid,
          pageNum: this[this.form.invoiceBizType].pageNum,
          pageSize: 10
        }
      });

      if (res2.success) {
        this[this.form.invoiceBizType].list = res2.data.map((item) => ({
          ...item,
          selected: false
        }));
      }
      this.listLoading = false;
    },
    async getUserUnInvoicedMonthlyBillList() {
      this.listLoading = true;
      const res = await this.$services.rdpFinanceUserUnInvoicedMonthlyBillCount({
        data: {
          uid: this.userInfo.uid
        }
      });

      if (res.success) {
        this[this.form.invoiceBizType].count = res.data;
      }

      const res2 = await this.$services.rdpFinancePageListUserUnInvoicedMonthlyBill({
        data: {
          uid: this.userInfo.uid,
          pageNum: this[this.form.invoiceBizType].pageNum,
          pageSize: 10
        }
      });

      if (res2.success) {
        this[this.form.invoiceBizType].list = res2.data;
      }
      this.listLoading = false;
    },
    getUnInvoicedList() {
      if (this.form.invoiceBizType === INVOICE_TYPE.LICENSE) {
        this.getUserUnInvoicedOrderList();
      } else {
        this.getUserUnInvoicedMonthlyBillList();
      }
    }
  }
};
</script>

<style lang="less" scoped>
.invoiceApply {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
