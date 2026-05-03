<template>
  <div class="invoiceApplyList">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('fa-piao-shen-qing') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="w-full">
            <Button class="refresh float-right" @click="handleRefresh" :loading="loading">
              <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
            </Button>
            <Button class="mr-4 float-right" type="primary" @click="handleCreateInvoiceApply">
              {{ $t('fa-piao-shen-qing') }}
            </Button>
          </div>
          <div class="mt-6 flow-root">
            <div class="overflow-x-auto">
              <div class="min-w-full align-middle rounded">
                <Table :columns="invoiceApplyColumns" :data="invoiceApplyList" size="small" border :loading="loading">
                  <template #invoiceType="{ row }">
                    {{ INVOICE_TYPE_I18N[row.invoiceBizType] }}
                  </template>
                  <template #amount="{ row }">{{ transformScientific(row.amount) }}{{ row.currencyI18n }}</template>
                  <template #createTime="{ row }">
                    {{ dayjs(row.gmtCreated).format('YYYY-MM-DD HH:mm:ss') }}
                  </template>
                  <template #status="{ row }">
                    {{ INVOICE_APPLY_STATUS_I18N[row.invoiceApplyStatus] }}
                    <a-tooltip :title="row.statusMsg" placement="top" class="ml-[10px]">
                      <Icon
                        type="ios-information-circle"
                        v-if="
                          row.invoiceApplyStatus === INVOICE_APPLY_STATUS.WAIT_TO_MODIFY || row.invoiceApplyStatus === INVOICE_APPLY_STATUS.CANCEL
                        "
                      />
                    </a-tooltip>
                  </template>
                  <template #email="{ row }">
                    {{ row.targetEmail }}
                  </template>
                  <template #action="{ row }">
                    <Button
                      type="text"
                      @click="handleCancelInvoiceApply(row)"
                      v-if="
                        row.invoiceApplyStatus === INVOICE_APPLY_STATUS.START_APPLY || row.invoiceApplyStatus === INVOICE_APPLY_STATUS.WAIT_TO_MODIFY
                      "
                    >
                      {{ $t('qu-xiao') }}
                    </Button>
                    <Button type="text" @click="handleModifyInvoiceApply(row)" v-if="row.invoiceApplyStatus === INVOICE_APPLY_STATUS.WAIT_TO_MODIFY">
                      {{ $t('xiu-gai') }}
                    </Button>
                  </template>
                </Table>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="invoiceApplyCount"
          :page-size="pageSize"
          show-total
          v-model="pageNum"
          :model-value="pageNum"
          @on-change="handlePageChange"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="cancelInvoiceApplyModal" :title="$t('qu-xiao-fa-piao-shen-qing')" footer-hide>
      <Form :model="cancelInvoiceApplyForm" :rules="cancelInvoiceApplyFormRules" :label-width="100" ref="cancelInvoiceApplyFormRef">
        <FormItem :label="$t('qu-xiao-yuan-yin')" prop="cancelMsg">
          <Input v-model="cancelInvoiceApplyForm.cancelMsg" :placeholder="$t('qing-tian-xie-qu-xiao-yuan-yin')" />
        </FormItem>
      </Form>
      <template #footer>
        <Button style="margin-left: 5px" type="default" @click="handleCancelCancelInvoiceApplyModal">{{ $t('guan-bi') }}</Button>
        <Button type="primary" @click="handleCancelInvoiceApplyOk">{{ $t('que-ren') }}</Button>
      </template>
    </CCModal>
    <CCModal v-model="modifyInvoiceApplyModal" :title="$t('xiu-gai-fa-piao-shen-qing')" @on-ok="handleModifyInvoiceApplyOk">
      <Form :model="modifyInvoiceForm" :rules="modifyChinaInvoiceFormRules" :label-width="140" ref="modifyChinaInvoiceFormRef">
        <FormItem :label="$t('kai-piao-zhong-lei')" prop="chineseInvoiceType">
          <RadioGroup v-model="modifyInvoiceForm.chineseInvoiceType" type="button">
            <Radio label="VAT_NORMAL_INVOICE">{{ $t('zeng-zhi-shui-pu-tong-fa-piao') }}</Radio>
            <Radio label="VAT_SPECIAL_INVOICE">{{ $t('zeng-zhi-shui-zhuan-yong-fa-piao') }}</Radio>
          </RadioGroup>
        </FormItem>
        <FormItem :label="$t('shui-shuai')">6%</FormItem>
        <FormItem :label="$t('fa-piao-tai-tou')" prop="chineseInvoiceTitle">
          <Input v-model="modifyInvoiceForm.chineseInvoiceTitle" :placeholder="$t('qing-tian-xie-fa-piao-tai-tou')" />
        </FormItem>
        <FormItem :label="$t('shui-hao')" prop="chineseTaxNo">
          <Input v-model="modifyInvoiceForm.chineseTaxNo" :placeholder="$t('qing-tian-xie-shui-hao')" />
        </FormItem>
        <FormItem :label="$t('kai-hu-hang')" prop="chineseTaxBank">
          <Input v-model="modifyInvoiceForm.chineseTaxBank" :placeholder="$t('qing-tian-xie-kai-hu-hang')" />
        </FormItem>
        <FormItem :label="$t('kai-hu-hang-zhang-hao')" prop="chineseTaxBankNo">
          <Input v-model="modifyInvoiceForm.chineseTaxBankNo" :placeholder="$t('qing-tian-xie-kai-hu-hang-zhang-hao')" />
        </FormItem>
        <FormItem :label="$t('zhu-ce-di-zhi')" prop="chineseTaxRegAddr">
          <Input v-model="modifyInvoiceForm.chineseTaxRegAddr" :placeholder="$t('qing-tian-xie-zhu-ce-di-zhi')" />
        </FormItem>
        <FormItem :label="$t('dian-hua')" prop="chineseTaxPhone">
          <Input v-model="modifyInvoiceForm.chineseTaxPhone" :placeholder="$t('qing-tian-xie-dian-hua')" />
        </FormItem>
        <FormItem :label="$t('fa-piao-jie-shou-you-xiang')" prop="targetEmail">
          <template #label>
            <span>{{ $t('fa-piao-jie-shou-you-xiang') }}</span>
            <a-tooltip :title="$t('kai-piao-cheng-gong-hou-jiang-zi-dong-fa-song-dao-gai-you-xiang')" placement="top" class="ml-[10px]">
              <Icon type="ios-information-circle" />
            </a-tooltip>
          </template>
          <Input v-model="modifyInvoiceForm.targetEmail" :placeholder="$t('qing-tian-xie-fa-piao-jie-shou-you-xiang')" />
        </FormItem>
      </Form>
    </CCModal>
  </div>
</template>
<script>
import { mapGetters } from 'vuex';
import { Page } from 'view-ui-plus';
import dayjs from 'dayjs';
import { INVOICE_APPLY_STATUS, INVOICE_APPLY_STATUS_I18N, INVOICE_TYPE, INVOICE_TYPE_I18N } from '@/utils';
import { transformScientific } from '@/utils/tools';

export default {
  name: 'InvoiceApplyList',
  computed: {
    ...mapGetters(['userInfo'])
  },
  data() {
    return {
      loading: false,
      INVOICE_APPLY_STATUS,
      INVOICE_APPLY_STATUS_I18N,
      INVOICE_TYPE,
      INVOICE_TYPE_I18N,
      invoiceApplyCount: 0,
      invoiceApplyList: [],
      pageNum: 1,
      pageSize: 10,
      invoiceApplyColumns: [
        {
          title: this.$t('fa-piao-lei-xing'),
          slot: 'invoiceType'
        },
        {
          title: this.$t('kai-piao-jin-e'),
          slot: 'amount'
        },
        {
          title: this.$t('fa-qi-shi-jian'),
          slot: 'createTime'
        },
        {
          title: this.$t('sql-zhuang-tai'),
          slot: 'status'
        },
        {
          title: this.$t('fa-song-you-xiang'),
          slot: 'email'
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action'
        }
      ],
      cancelInvoiceApplyModal: false,
      modifyInvoiceApplyModal: false,
      cancelInvoiceApplyForm: {
        invoiceApplyId: '',
        cancelMsg: ''
      },
      cancelInvoiceApplyFormRules: {
        cancelMsg: [{ required: true, message: this.$t('qing-tian-xie-qu-xiao-yuan-yin'), trigger: 'blur' }]
      },
      modifyInvoiceForm: {
        invoiceApplyId: '',
        chineseInvoiceType: 'VAT_NORMAL_INVOICE',
        chineseTaxRate: '6%',
        chineseInvoiceTitle: '',
        chineseTaxNo: '',
        chineseTaxBank: '',
        chineseTaxBankNo: '',
        chineseTaxRegAddr: '',
        chineseTaxPhone: '',
        targetEmail: '',
        overseaTaxTo: '',
        overseaTaxAddr: ''
      },
      modifyChinaInvoiceFormRules: {
        chineseInvoiceType: [{ required: true, message: this.$t('qing-xuan-ze-kai-piao-lei-xing'), trigger: 'change' }],
        chineseInvoiceTitle: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-tai-tou'), trigger: 'change' }],
        chineseTaxNo: [{ required: true, message: this.$t('qing-tian-xie-shui-hao'), trigger: 'change' }],
        chineseTaxBank: [{ required: true, message: this.$t('qing-tian-xie-kai-hu-hang'), trigger: 'change' }],
        chineseTaxBankNo: [{ required: true, message: this.$t('qing-tian-xie-kai-hu-hang-zhang-hao'), trigger: 'change' }],
        targetEmail: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-jie-shou-you-xiang-0'), trigger: 'change' }]
      },
      modifyForeignInvoiceFormRules: {
        overseaTaxTo: [{ required: true, message: this.$t('qing-tian-xie-tai-tou'), trigger: 'change' }],
        overseaTaxAddr: [{ required: true, message: this.$t('qing-tian-xie-di-zhi'), trigger: 'change' }],
        targetEmail: [{ required: true, message: this.$t('qing-tian-xie-fa-piao-jie-shou-you-xiang-0'), trigger: 'change' }]
      }
    };
  },
  mounted() {
    this.getInvoiceApplyList();
  },
  methods: {
    dayjs,
    transformScientific,
    handleCancelInvoiceApply(row) {
      this.cancelInvoiceApplyModal = true;
      this.cancelInvoiceApplyForm.invoiceApplyId = row.id;
      this.cancelInvoiceApplyForm.cancelMsg = row.cancelMsg;
    },
    handleCancelCancelInvoiceApplyModal() {
      this.cancelInvoiceApplyModal = false;
      this.cancelInvoiceApplyForm.invoiceApplyId = '';
      this.cancelInvoiceApplyForm.cancelMsg = '';
    },
    async handleCancelInvoiceApplyOk() {
      this.$refs.cancelInvoiceApplyFormRef.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.rdpFinanceCancelInvoiceApply({
            data: {
              invoiceApplyId: this.cancelInvoiceApplyForm.invoiceApplyId,
              cancelMsg: this.cancelInvoiceApplyForm.cancelMsg
            }
          });

          if (res.success) {
            this.$Message.success(this.$t('qu-xiao-fa-piao-shen-qing-cheng-gong'));
          } else {
            this.$Message.error(this.$t('qu-xiao-fa-piao-shen-qing-shi-bai'));
          }
          this.cancelInvoiceApplyModal = false;
          this.getInvoiceApplyList();
        }
      });
    },
    handleModifyInvoiceApply(row) {
      this.modifyInvoiceApplyModal = true;

      this.modifyInvoiceForm = {
        invoiceApplyId: row.id,
        chineseInvoiceType: '',
        chineseTaxRate: '',
        chineseInvoiceTitle: '',
        chineseTaxNo: '',
        chineseTaxBank: '',
        chineseTaxBankNo: '',
        chineseTaxRegAddr: '',
        chineseTaxPhone: '',
        targetEmail: '',
        overseaTaxTo: '',
        overseaTaxAddr: ''
      };

      this.modifyInvoiceForm.chineseInvoiceType = row.chineseInvoiceType;
      this.modifyInvoiceForm.chineseTaxRate = row.chineseTaxRate;
      this.modifyInvoiceForm.chineseInvoiceTitle = row.chineseInvoiceTitle;
      this.modifyInvoiceForm.chineseTaxNo = row.chineseTaxNo;
      this.modifyInvoiceForm.chineseTaxBank = row.chineseTaxBank;
      this.modifyInvoiceForm.chineseTaxBankNo = row.chineseTaxBankNo;
      this.modifyInvoiceForm.chineseTaxRegAddr = row.chineseTaxRegAddr;
      this.modifyInvoiceForm.chineseTaxPhone = row.chineseTaxPhone;
      this.modifyInvoiceForm.targetEmail = row.targetEmail;
    },
    async handleModifyInvoiceApplyOk() {
      const res = await this.$services.rdpFinanceModifyInvoiceApply({
        data: this.modifyInvoiceForm
      });

      if (res.success) {
        this.$Message.success(this.$t('xiu-gai-fa-piao-shen-qing-cheng-gong'));
      } else {
        this.$Message.error(this.$t('xiu-gai-fa-piao-shen-qing-shi-bai'));
      }
      this.modifyInvoiceApplyModal = false;
      this.getInvoiceApplyList();
    },
    handleCancelInvoiceApplyModal() {
      this.cancelInvoiceApplyModal = false;
      this.cancelInvoiceApplyForm.invoiceApplyId = '';
      this.cancelInvoiceApplyForm.cancelMsg = '';
    },
    handleModifyInvoiceApplyModal() {
      this.modifyInvoiceApplyModal = false;
      this.modifyInvoiceApplyForm.invoiceApplyId = '';
      this.modifyInvoiceApplyForm.invoiceInfo = '';
    },
    handleRefresh() {
      this.getInvoiceApplyList();
    },
    handleCreateInvoiceApply() {
      this.$router.push('/system/invoiceApply');
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.getInvoiceApplyList();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.getInvoiceApplyList();
    },
    async getInvoiceApplyList() {
      this.loading = true;
      const res = await this.$services.rdpFinanceUserInvoiceApplyCount({
        data: {
          uid: this.userInfo.uid
        }
      });

      if (res.success) {
        this.invoiceApplyCount = res.data;
      }

      const res2 = await this.$services.rdpFinancePageListUserInvoiceApply({
        data: {
          uid: this.userInfo.uid,
          pageNum: this.pageNum,
          pageSize: this.pageSize
        }
      });

      if (res2.success) {
        this.invoiceApplyList = res2.data;
      }

      this.loading = false;
    },
    handleSearch(type) {
      console.log(type);
    }
  }
};
</script>

<style lang="less" scoped>
.invoiceApplyList {
  height: 100%;
  display: flex;
  flex-direction: column;
  color: var(--text-primary, rgba(0, 0, 0, 0.88));

  .content {
    color: var(--text-primary, rgba(0, 0, 0, 0.88));
  }

  // Icon 颜色适配
  .ivu-icon {
    color: var(--text-primary, rgba(0, 0, 0, 0.88));
  }
}
</style>

<style lang="less">
// 暗色模式适配
[data-theme='dark'] {
  .invoiceApplyList {
    color: var(--text-primary, #ffffff);

    .content {
      color: var(--text-primary, #ffffff);
    }

    // Icon 颜色适配
    .ivu-icon {
      color: var(--text-primary, #ffffff) !important;
    }

    // 表单标签颜色
    .ivu-form .ivu-form-item-label {
      color: var(--text-primary, #ffffff) !important;
    }

    // 输入框文字颜色
    .ivu-form .ivu-input {
      color: var(--text-primary, #ffffff) !important;
    }

    // Radio 按钮组文字颜色
    .ivu-form .ivu-radio-group-button .ivu-radio-wrapper {
      color: var(--text-primary, #ffffff) !important;
    }

    // 按钮文字颜色（text 类型）
    .ivu-btn-text {
      color: var(--text-primary, #ffffff) !important;

      &:hover {
        color: var(--primary-color, #0087c7) !important;
      }
    }
  }
}
</style>
