<template>
  <div class="rule-list-container">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content" v-if="isQuery">
          <div class="option">
            <div class="left">
              <Select v-model="activeTab" style="width: 160px; margin-right: 10px" @on-change="handleRuleKindChange">
                <Option v-for="item in ruleKindOptions" :value="item.value" :key="item.value">
                  {{ item.label }}
                </Option>
              </Select>
              <Input
                v-model="QUERY.search"
                style="width: 280px; margin-right: 10px"
                clearable
                :placeholder="$t('qing-shu-ru-gui-ze-ming-cheng-miao-shu-cha-xun')"
              ></Input>
              <Button @click="getRuleSearch" type="primary" ghost>{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleOpenApplyTemplateModal()" type="primary" ghost style="margin-right: 10px" v-if="hasRuleManage">
                {{ $t('ying-yong') }}
              </Button>
              <Button @click="handleAddRule" type="primary" style="margin-right: 10px" icon="md-add" v-if="hasRuleManage">
                {{ $t('xin-jian-gui-ze-mo-ban') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
              :key="`query-${hasRuleManage}`"
              border
              stripe
              :columns="queryRuleColumns"
              :data="QUERY.showRuleList"
              :scroll="queryTableScroll"
              size="small"
              :loading="QUERY.loading"
            >
              <template #targetType="{ row }">
                {{ getTargetType(row.targetType).i18n }}
              </template>
              <template #ruleAction="{ row }">
                <Button @click="handleViewRule(row)" type="text" size="small">
                  {{ $t('xiang-qing') }}
                </Button>
                <Button @click="handleViewRule(row, 'edit')" type="text" size="small" v-if="!row.inner && hasRuleManage">
                  {{ $t('bian-ji') }}
                </Button>
                <Poptip
                  confirm
                  transfer
                  :title="$t('que-ding-yao-shan-chu-gai-gui-ze-ma')"
                  :ok-text="$t('que-ding')"
                  :cancel-text="$t('qu-xiao')"
                  @on-ok="handleDeleteRule(row)"
                >
                  <Button type="text" size="small" v-if="!row.inner && hasRuleManage">
                    {{ $t('shan-chu') }}
                  </Button>
                </Poptip>
              </template>
              <template #dsRange="{ row }">
                <DataSourceRangeTags :ds-range="row.dsRange" />
              </template>
            </Table>
          </div>
        </div>
        <div class="content" v-else>
          <div class="option">
            <div class="left">
              <Select v-model="activeTab" style="width: 160px; margin-right: 10px" @on-change="handleRuleKindChange">
                <Option v-for="item in ruleKindOptions" :value="item.value" :key="item.value">
                  {{ item.label }}
                </Option>
              </Select>
              <Input
                v-model="SENSITIVE.search"
                style="width: 280px; margin-right: 10px"
                clearable
                :placeholder="$t('qing-shu-ru-gui-ze-ming-cheng-miao-shu-cha-xun')"
              ></Input>
              <Button @click="getRuleSearch" type="primary" ghost>{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleOpenApplyTemplateModal()" type="primary" ghost style="margin-right: 10px" v-if="hasRuleManage">
                {{ $t('ying-yong') }}
              </Button>
              <Button @click="handleAddRule" type="primary" style="margin-right: 10px" icon="md-add" v-if="hasRuleManage">
                {{ $t('xin-jian-gui-ze-mo-ban') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
              :key="`sensitive-${hasRuleManage}`"
              border
              stripe
              :columns="sensitiveRuleColumns"
              :data="SENSITIVE.showRuleList"
              :scroll="sensitiveTableScroll"
              size="small"
              :loading="SENSITIVE.loading"
            >
              <template #ruleAction="{ row }">
                <Button @click="handleViewRule(row)" type="text" size="small">
                  {{ $t('xiang-qing') }}
                </Button>
                <Button @click="handleViewRule(row, 'edit')" type="text" size="small" v-if="!row.inner && hasRuleManage">
                  {{ $t('bian-ji') }}
                </Button>
                <Poptip
                  confirm
                  transfer
                  :title="$t('que-ding-yao-shan-chu-gai-gui-ze-ma')"
                  :ok-text="$t('que-ding')"
                  :cancel-text="$t('qu-xiao')"
                  @on-ok="handleDeleteRule(row)"
                >
                  <Button type="text" size="small" v-if="!row.inner && hasRuleManage">
                    {{ $t('shan-chu') }}
                  </Button>
                </Poptip>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="QUERY.total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          v-if="isQuery"
          show-sizer
          v-model="QUERY.pageNum"
          :page-size="QUERY.pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
        <Page
          :total="SENSITIVE.total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          v-else
          show-sizer
          v-model="SENSITIVE.pageNum"
          :page-size="SENSITIVE.pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <Modal
      v-model="showForceRuleModal"
      :title="forceRuleModalTitle"
      @on-cancel="handleCloseModal"
      @on-ok="forceEvent(selectedRule, true)"
      :ok-text="forceRuleModalTitle"
    >
      <div class="title" v-html="forceRuleModalText" style="margin-bottom: 10px"></div>
      <Table :columns="forceRuleRefererColumns" :data="forceRuleRefererList" size="small" />
    </Modal>
    <CCModal v-model="showApplyTemplateModal" :title="$t('ying-yong-dao-an-quan-gui-ze')" :width="620" @on-cancel="handleCloseApplyTemplateModal">
      <div class="apply-template-modal-content">
        <Form class="apply-template-form" :label-width="96">
          <FormItem class="apply-template-form-item" :label="$t('gui-ze-mo-ban')">
            <div class="apply-template-summary">
              <Tag color="blue">{{ applyRuleKindLabel }}</Tag>
              <span>{{ applyTemplateRuleCountText }}</span>
            </div>
          </FormItem>
          <FormItem class="apply-template-form-item apply-template-spec-item" :label="$t('an-quan-gui-fan')">
            <Select v-model="applySpecIds" multiple filterable transfer :loading="applySpecLoading" style="width: 100%">
              <Option v-for="spec in applySpecList" :value="spec.specId" :key="spec.specId">
                {{ spec.name }}
                <span v-if="spec.description">（{{ spec.description }}）</span>
              </Option>
            </Select>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <div class="apply-template-footer">
          <Button @click="handleCloseApplyTemplateModal">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" :loading="applySaving" @click="handleApplyTemplateRules(false)">
            {{ $t('ying-yong') }}
          </Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showApplyForceModal" :title="$t('qiang-zhi-ying-yong')" @on-cancel="handleCloseApplyForceModal">
      <Alert type="warning">
        <div v-html="applyForceText"></div>
      </Alert>
      <Table :columns="applyForceRefererColumns" :data="applyForceRefererList" size="small">
        <template #envDesc="{ row }">
          <Tooltip :content="row.envDesc" placement="top" transfer>
            <span class="apply-force-env-desc-cell">{{ row.envDesc }}</span>
          </Tooltip>
        </template>
      </Table>
      <template #footer>
        <Button @click="handleCloseApplyForceModal">{{ $t('qu-xiao') }}</Button>
        <Button type="error" :loading="applySaving" @click="handleApplyTemplateRules(true)">
          {{ $t('qiang-zhi-ying-yong') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import DataSourceRangeTags from '@/views/security/components/DataSourceRangeTags';
export default {
  name: 'RuleList',
  components: { DataSourceRangeTags },
  mounted() {
    this.activeTab = this.normalizeRuleKind(this.$route.query.ruleKind);
    this.getRuleList();
    this.getRuleSetting();
  },
  data() {
    return {
      activeTab: 'QUERY',
      ruleKinds: ['QUERY', 'SENSITIVE'],
      forceEvent: null,
      supportTypeList: ['int', 'integer', 'float', 'decimal', 'bool', 'string', 'date', 'time', 'datetime'],
      isEdit: false,
      showForceRuleModal: false,
      forceRuleModalTitle: '',
      forceRuleModalText: '',
      forceRuleRefererList: [],
      forceRuleRefererColumns: [
        {
          title: this.$t('gui-fan-ming-cheng'),
          key: 'specName'
        },
        {
          title: this.$t('gui-fan-miao-shu'),
          key: 'specDesc'
        }
      ],
      selectedRule: {},
      applyTemplateRules: [],
      applySpecList: [],
      applySpecIds: [],
      applySpecLoading: false,
      applySaving: false,
      showApplyTemplateModal: false,
      showApplyForceModal: false,
      applyForceData: null,
      applyForceText: '',
      applyForceRefererList: [],
      applyForceRefererColumns: [
        {
          title: this.$t('huan-jing-ming-cheng'),
          key: 'envName'
        },
        {
          title: this.$t('huan-jing-miao-shu'),
          key: 'envDesc',
          slot: 'envDesc'
        }
      ],
      // query rule
      QUERY: {
        loading: false,
        pageSize: 20,
        pageNum: 1,
        total: 0,
        search: '',
        allRuleList: [],
        ruleList: [],
        showRuleList: []
      },
      SENSITIVE: {
        loading: false,
        pageSize: 20,
        pageNum: 1,
        total: 0,
        search: '',
        allRuleList: [],
        ruleList: [],
        showRuleList: []
      }
    };
  },
  computed: {
    ...mapGetters(['getTargetType', 'getSenMode']),
    ...mapState(['myAuth']),
    hasRuleManage() {
      return this.myAuth.includes('DM_SECRULES_MANAGE');
    },
    ruleKindOptions() {
      return [
        { value: 'QUERY', label: this.$t('cha-xun-gui-ze') },
        { value: 'SENSITIVE', label: this.$t('tuo-min-gui-ze') }
      ];
    },
    isQuery() {
      return this.activeTab === 'QUERY';
    },
    applyRuleKindLabel() {
      const option = this.ruleKindOptions.find((item) => item.value === this.activeTab);
      return option ? option.label : '';
    },
    applyTemplateRuleCountText() {
      return this.$t('gong-count-tiao-gui-ze', { count: this.applyTemplateRules.length });
    },
    queryRuleColumns() {
      const columns = [
        {
          title: this.$t('gui-ze-ming-cheng'),
          key: 'ruleName',
          width: 200
        },
        {
          title: this.$t('wei-gui-ti-shi'),
          key: 'ruleDesc',
          width: 360
        },
        {
          title: this.$t('shu-ju-yuan'),
          slot: 'dsRange',
          width: 260
        },
        {
          title: this.$t('dui-xiang-lei-xing'),
          key: 'targetTypeI18n',
          width: 100
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'ruleAction',
          width: 170,
          fixed: 'right'
        }
      ];
      return columns;
    },
    queryTableScroll() {
      return { x: 1182 };
    },
    sensitiveRuleColumns() {
      const columns = [
        {
          title: this.$t('gui-ze-ming-cheng'),
          key: 'ruleName',
          width: 200
        },
        {
          title: this.$t('gui-ze-miao-shu'),
          key: 'ruleDesc',
          width: 360
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'ruleAction',
          width: 170,
          fixed: 'right'
        }
      ];
      return columns;
    },
    sensitiveTableScroll() {
      return { x: 822 };
    }
  },
  methods: {
    ...mapActions(['getRuleSetting']),
    normalizeRuleKind(ruleKind) {
      return this.ruleKinds.includes(ruleKind) ? ruleKind : 'QUERY';
    },
    handleRuleKindChange(name) {
      const ruleKind = this.normalizeRuleKind(name);
      if (ruleKind !== name) {
        this.activeTab = ruleKind;
        this.$Message.warning(this.$t('gui-ze-lei-xing-bu-neng-wei-kong'));
      }
      this.$router.push({
        path: '/data-access/rules',
        query: {
          tab: 'template',
          ruleKind
        }
      });
      if (!this[ruleKind].total) {
        this.getRuleList();
      }
    },
    handlePageChange(pageNum) {
      this[this.activeTab].pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this[this.activeTab].pageSize = pageSize;
      this.handlePageChange(1);
    },
    handleViewRule(row, type = 'view') {
      this.$router.push({
        path: `/data-access/rules/detail/${row.ruleId}`,
        query: { type, ruleKind: row.ruleKind }
      });
    },
    async handleDeleteRule(rule, force = false) {
      this.selectedRule = rule;
      const data = {
        ruleKind: rule.ruleKind,
        ruleId: rule.ruleId,
        force
      };
      const res = await this.$services.dmSecurityRulesRuleDelete({
        data
      });

      if (res.success) {
        if (res.data) {
          if (res.data.success) {
            this.showForceRuleModal = false;
            this.$Message.success(res.data.message);
            await this.getRuleList();
          } else {
            this.showForceRuleModal = true;
            this.forceRuleModalTitle = this.$t('qiang-zhi-shan-chu');
            this.forceRuleModalText = res.data.message;
            this.forceEvent = this.handleDeleteRule;
            this.forceRuleRefererList = res.data.referer;
          }
        }
      }
    },
    setTableShowData() {
      const { pageNum, pageSize } = this[this.activeTab];
      this[this.activeTab].showRuleList = this[this.activeTab].ruleList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    handleCloseModal() {
      this.showViewRuleModal = false;
      this.showEditRuleModal = false;
    },
    handleAddRule() {
      this.$router.push({
        path: '/data-access/rules/create',
        query: {
          ruleKind: this.activeTab
        }
      });
    },
    async handleOpenApplyTemplateModal() {
      const rules = this[this.activeTab].allRuleList || [];
      if (!rules.length) {
        this.$Message.warning(this.$t('dang-qian-gui-ze-mo-ban-mei-you-ke-ying-yong-de-gui-ze'));
        return;
      }
      this.applyTemplateRules = rules.slice();
      this.applySpecIds = [];
      this.showApplyTemplateModal = true;
      await this.getApplySpecList();
    },
    async getApplySpecList() {
      this.applySpecLoading = true;
      const res = await this.$services.dmSecurityRulesSpecList({
        data: {
          search: ''
        }
      });
      this.applySpecLoading = false;
      if (res.success) {
        this.applySpecList = res.data || [];
      }
    },
    buildRuleParam(rule) {
      const params = {};
      (rule.ruleParameter || []).forEach((param) => {
        params[param.name] = param.defaultValue;
      });
      return params;
    },
    buildApplyRulesPayload() {
      return this.applyTemplateRules.map((rule) => ({
        ruleId: rule.ruleId,
        ruleKind: rule.ruleKind,
        enable: true,
        warnLevel: rule.ruleKind === 'QUERY' ? 'SUGGEST' : undefined,
        senMode: rule.ruleKind === 'SENSITIVE' ? rule.senMode || 'ROW' : undefined,
        ruleParam: this.buildRuleParam(rule)
      }));
    },
    async handleApplyTemplateRules(force = false) {
      if (!this.applySpecIds.length) {
        this.$Message.warning(this.$t('qing-xuan-ze-an-quan-gui-ze'));
        return;
      }
      const data =
        force && this.applyForceData
          ? { ...this.applyForceData, force: true }
          : {
              specIds: this.applySpecIds,
              rules: this.buildApplyRulesPayload(),
              force
            };
      this.applySaving = true;
      const res = await this.$services.dmSecurityRulesSpecSaveRules({
        data
      });
      this.applySaving = false;

      if (res.success && res.data) {
        if (res.data.success) {
          this.$Message.success(res.data.message || this.$t('ying-yong-cheng-gong'));
          this.handleCloseApplyTemplateModal();
          this.handleCloseApplyForceModal();
        } else {
          this.applyForceData = data;
          this.applyForceText = res.data.message || '';
          this.applyForceRefererList = res.data.referer || [];
          this.showApplyForceModal = true;
        }
      }
    },
    handleCloseApplyTemplateModal() {
      this.showApplyTemplateModal = false;
      this.applyTemplateRules = [];
      this.applySpecIds = [];
      this.applySaving = false;
      this.applyForceData = null;
    },
    handleCloseApplyForceModal() {
      this.showApplyForceModal = false;
      this.applyForceText = '';
      this.applyForceRefererList = [];
    },
    getRuleSearch() {
      const ruleList = this[this.activeTab].allRuleList.filter(
        (rule) => rule.ruleName.includes(this[this.activeTab].search) || rule.ruleDesc.includes(this[this.activeTab].search)
      );
      this[this.activeTab].total = ruleList.length;
      this[this.activeTab].ruleList = ruleList;
      this.handlePageChange(1);
    },
    async getRuleList() {
      this[this.activeTab].loading = true;
      const res = await this.$services.dmSecurityRulesRuleList({
        data: {
          search: this[this.activeTab].search,
          ruleKind: this.activeTab
        }
      });

      this[this.activeTab].loading = false;
      if (res.success) {
        this[this.activeTab].search = '';
        this[this.activeTab].allRuleList = res.data;
        this[this.activeTab].ruleList = res.data;
        this[this.activeTab].total = res.data.length;
        this.setTableShowData();
      }
    },
    async handleShowEditRuleModal(rule) {
      this.isEdit = !!rule.ruleId;
      this.selectedRule = {};
      this.ruleParamList = [];
      if (rule.ruleId) {
        const res = await this.$services.dmSecurityRulesRuleDetail({
          data: {
            ruleId: rule.ruleId
          }
        });

        if (res.success) {
          this.ruleParamList = rule.ruleParameter;
          this.selectedRule = { ...rule, ...res.data };
        }
      }

      this.showEditRuleModal = true;

      const res2 = await this.$services.dmSecurityRulesRuleSupportDs();
      if (res2.success) {
        this.supportDsList = res2.data;
      }
    }
  }
};
</script>
<style lang="less" scoped>
:deep(.ivu-form-item) {
  margin-bottom: 10px;
}
.rule-list-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.rule-list-container .table-list-layout .content .option {
  flex-wrap: wrap;
  align-items: flex-start;
  overflow: visible;
  row-gap: 10px;
}

.rule-list-container .table-list-layout .content .option .left {
  flex: 1 1 520px;
  max-width: 100%;
}

.rule-list-container .table-list-layout .content .option .right {
  flex: 0 0 auto;
  margin-left: auto;
}

@media (max-width: 1280px) {
  .rule-list-container .table-list-layout .content .option .right {
    width: 100%;
    justify-content: flex-end;
    margin-left: 0;
  }
}

.apply-template-summary {
  min-height: 32px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  line-height: 24px;
}

.apply-template-modal-content {
  max-height: calc(100vh - 280px);
  overflow-y: auto;
  padding-right: 2px;
}

.apply-template-form {
  padding-top: 2px;
}

:deep(.apply-template-form .apply-template-form-item) {
  margin-bottom: 18px;
}

:deep(.apply-template-form .apply-template-spec-item .ivu-form-item-label::before) {
  content: '*';
  display: inline-block;
  margin-right: 4px;
  color: #ed4014;
  font-family: SimSun, sans-serif;
  line-height: 1;
}

:deep(.apply-template-form .ivu-form-item-error-tip) {
  padding-top: 6px;
  line-height: 18px;
}

.apply-template-footer {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
  width: 100%;
}

.apply-template-footer .ivu-btn,
.apply-template-footer :deep(.ivu-btn) {
  min-width: 100px;
}

.apply-force-env-desc-cell {
  display: inline-block;
  max-width: 220px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
