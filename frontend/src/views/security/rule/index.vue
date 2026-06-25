<template>
  <div class="rule-list-container">
    <div class="table-list-layout">
      <nav class="rule-tabs">
        <span class="rule-tabs__item" :class="{ 'is-active': activeTab === 'QUERY' }" @click="handleTabClick('QUERY')">
          {{ $t('cha-xun-gui-ze') }}
        </span>
        <span class="rule-tabs__item" :class="{ 'is-active': activeTab === 'SENSITIVE' }" @click="handleTabClick('SENSITIVE')">
          {{ $t('tuo-min-gui-ze') }}
        </span>
      </nav>
      <div class="table-list">
        <div class="content" v-if="isQuery">
          <div class="option">
            <div class="left">
              <Input v-model="QUERY.search" style="width: 280px; margin-right: 10px" clearable></Input>
              <Button @click="getRuleSearch" type="primary" ghost>{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleAddRule" type="primary" style="margin-right: 10px" icon="md-add" v-if="myAuth.includes('DM_SECRULES_MANAGE')">
                {{ $t('xin-jian-gui-ze') }}
              </Button>
              <Button @click="getRuleList">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
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
                <Button @click="handleViewRule(row, 'edit')" type="text" size="small" v-if="!row.inner && myAuth.includes('DM_SECRULES_MANAGE')">
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
                  <Button type="text" size="small" v-if="!row.inner && myAuth.includes('DM_SECRULES_MANAGE')">
                    {{ $t('shan-chu') }}
                  </Button>
                </Poptip>
              </template>
              <template #dsRange="{ row }">
                <Tooltip transfer placement="top">
                  <div class="ds-range-row">
                    <CustomIcon v-for="ds in row.dsRange.slice(0, 8)" :key="ds" :type="ds" rightMargin />
                    <span v-if="row.dsRange && row.dsRange.length > 8" class="more-count">{{ row.dsRange.length - 8 }}</span>
                  </div>
                  <template #content>
                    <CustomIcon v-for="ds in row.dsRange" :key="`full-` + ds" :type="ds" rightMargin />
                  </template>
                </Tooltip>
              </template>
            </Table>
          </div>
        </div>
        <div class="content" v-else>
          <div class="option">
            <div class="left">
              <Input v-model="SENSITIVE.search" style="width: 280px; margin-right: 10px" clearable></Input>
              <Button @click="getRuleSearch" type="primary" ghost>{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button @click="handleAddRule" type="primary" style="margin-right: 10px" icon="md-add" v-if="myAuth.includes('DM_SECRULES_MANAGE')">
                {{ $t('xin-jian-gui-ze') }}
              </Button>
              <Button @click="getRuleList">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table
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
                <Button @click="handleViewRule(row, 'edit')" type="text" size="small" v-if="!row.inner && myAuth.includes('DM_SECRULES_MANAGE')">
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
                  <Button type="text" size="small" v-if="!row.inner && myAuth.includes('DM_SECRULES_MANAGE')">
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
          :page-size="QUERY.pageSize"
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
  </div>
</template>
<script>
import { mapActions, mapGetters, mapState } from 'vuex';

export default {
  name: 'RuleList',
  mounted() {
    if (this.$route.query.ruleKind) {
      this.activeTab = this.$route.query.ruleKind;
    }
    this.getRuleList();
    this.getRuleSetting();
  },
  data() {
    return {
      activeTab: 'QUERY',
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
    isQuery() {
      return this.activeTab === 'QUERY';
    },
    queryRuleColumns() {
      return [
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
          title: this.$t('shu-ju-yuan'),
          slot: 'dsRange',
          width: 250
        },
        {
          title: this.$t('dui-xiang-lei-xing'),
          key: 'targetTypeI18n',
          width: 100
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'ruleAction',
          width: 160,
          fixed: 'right'
        }
      ];
    },
    queryTableScroll() {
      return { x: 1070 };
    },
    sensitiveRuleColumns() {
      return [
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
    },
    sensitiveTableScroll() {
      return { x: 730 };
    }
  },
  methods: {
    ...mapActions(['getRuleSetting']),
    handleTabClick(name) {
      this.activeTab = name;
      this.$router.push({
        path: '/data-access/rules',
        query: {
          ruleKind: name
        }
      });
      if (!this[name].total) {
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
.rule-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
  padding: 0;
  background: var(--bg-card);

  &__item {
    position: relative;
    padding: 12px 20px 10px;
    color: var(--text-secondary);
    font-size: 13px;
    font-weight: 400;
    line-height: 1.4;
    text-decoration: none;
    cursor: pointer;
    border-bottom: none;
    transition: color 0.12s ease;

    &:hover {
      color: var(--text-primary);
      border-bottom: none;
    }

    &.is-active {
      color: var(--text-primary);
      font-weight: 500;

      &::after {
        content: '';
        position: absolute;
        left: 20px;
        right: 20px;
        bottom: 0;
        height: 2px;
        border-radius: 2px 2px 0 0;
        background: var(--primary-color);
      }
    }
  }
}

:deep(.ivu-form-item) {
  margin-bottom: 10px;
}
.rule-list-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/deep/.ds-range-row {
  white-space: nowrap;
  display: inline-flex;
  align-items: center;
}
/deep/.more-count {
  white-space: nowrap;
  display: inline-block;
  margin-left: 6px;
  padding: 0 6px;
  font-size: 12px;
  line-height: 18px;
  color: #595959;
  background: #f0f0f0;
  border-radius: 9px;
}
/deep/.more-count::before {
  content: '+';
  margin-right: 2px;
}
</style>
