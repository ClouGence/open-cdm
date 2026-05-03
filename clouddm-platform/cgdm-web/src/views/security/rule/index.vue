<template>
  <div class="rule-list-container">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb v-if="breadcrumbList.length">
            <BreadcrumbItem v-for="breadcrumb in breadcrumbList" :to="breadcrumb.to" :key="breadcrumb.label">
              {{ breadcrumb.label }}
            </BreadcrumbItem>
          </Breadcrumb>
        </div>
        <Tabs v-model="activeTab" @on-click="handleTabClick">
          <TabPane :label="$t('cha-xun-gui-ze')" name="QUERY"></TabPane>
          <TabPane :label="$t('tuo-min-gui-ze')" name="SENSITIVE"></TabPane>
        </Tabs>
        <div class="content" v-if="isQuery">
          <div class="option">
            <div class="left">
              <Input v-model="QUERY.search" style="width: 280px; margin-right: 10px" clearable></Input>
              <Button @click="getRuleSearch" type="primary">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button
                @click="handleAddRule"
                type="primary"
                ghost
                style="margin-right: 10px"
                icon="md-add"
                v-if="myAuth.includes('DM_SECRULES_MANAGE')"
              >
                {{ $t('xin-jian-gui-ze') }}
              </Button>
              <Button @click="getRuleList">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table border stripe :columns="QUERY.ruleColumns" :data="QUERY.showRuleList" size="small" :loading="QUERY.loading">
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
              <Button @click="getRuleSearch" type="primary">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button
                @click="handleAddRule"
                type="primary"
                ghost
                style="margin-right: 10px"
                icon="md-add"
                v-if="myAuth.includes('DM_SECRULES_MANAGE')"
              >
                {{ $t('xin-jian-gui-ze') }}
              </Button>
              <Button @click="getRuleList">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table border stripe :columns="SENSITIVE.ruleColumns" :data="SENSITIVE.showRuleList" size="small" :loading="SENSITIVE.loading">
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
      breadcrumbList: [{ label: this.$t('gui-ze-lie-biao') }],
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
        showRuleList: [],
        ruleColumns: [
          {
            title: this.$t('gui-ze-ming-cheng'),
            key: 'ruleName',
            width: 200
          },
          {
            title: this.$t('gui-ze-miao-shu'),
            key: 'ruleDesc'
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
        ]
      },
      SENSITIVE: {
        loading: false,
        pageSize: 20,
        pageNum: 1,
        total: 0,
        search: '',
        allRuleList: [],
        ruleList: [],
        showRuleList: [],
        ruleColumns: [
          {
            title: this.$t('gui-ze-ming-cheng'),
            key: 'ruleName',
            width: 200
          },
          {
            title: this.$t('gui-ze-miao-shu'),
            key: 'ruleDesc'
          },
          {
            title: this.$t('cao-zuo'),
            slot: 'ruleAction',
            width: 170,
            fixed: 'right'
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(['getTargetType', 'getSenMode']),
    ...mapState(['myAuth']),
    isQuery() {
      return this.activeTab === 'QUERY';
    }
  },
  methods: {
    ...mapActions(['getRuleSetting']),
    handleTabClick(name) {
      this.$router.push({
        path: '/system/dmrulelist',
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
        path: `/system/dmrule/detail/${row.ruleId}`,
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
        path: '/system/dmrule/create',
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
