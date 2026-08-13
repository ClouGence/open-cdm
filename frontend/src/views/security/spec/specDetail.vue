<template>
  <div class="spec-detail">
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
              <Button @click="handleRuleSearch('search')" type="primary" ghost>
                {{ $t('cha-xun') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table border :columns="QUERY.ruleColumns" :data="QUERY.showRuleList" :scroll="queryTableScroll" size="small" :loading="QUERY.loading">
              <template #targetType="{ row }">
                {{ getTargetType(row.targetType).i18n }}
              </template>
              <template #ruleAction="{ row }">
                <div class="rule-action-list">
                  <Button @click="handleGoRange(row)" type="text" size="small" :disabled="!row.enable || row.deprecated">
                    {{ $t('fan-wei') }}
                  </Button>
                  <Button @click="handleViewRuleDetail(row)" type="text" size="small" :disabled="row.deprecated">
                    {{ $t('xiang-qing') }}
                  </Button>
                  <Button
                    @click="handleEditRuleDetail(row)"
                    type="text"
                    size="small"
                    v-if="myAuth.includes('DM_SECRULES_MANAGE')"
                    :disabled="row.deprecated"
                  >
                    {{ $t('she-zhi') }}
                  </Button>
                </div>
              </template>
              <template #dsRange="{ row }">
                <DataSourceRangeTags :ds-range="row.dsRange" />
              </template>
              <template #warnLevel="{ row }">
                <Tag :color="row.warnLevel === 'SUGGEST' ? 'warning' : 'error'">
                  {{ RULE_WARN_LEVEL[row.warnLevel] }}
                </Tag>
              </template>
              <template #enable="{ row }">
                <i-switch
                  v-model="row.enable"
                  true-color="#52C41A"
                  :disabled="row.deprecated"
                  @on-change="handleEnableSpecRule(row, 'enable', false)"
                />
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
              <Button @click="handleRuleSearch" type="primary" ghost>{{ $t('cha-xun') }}</Button>
            </div>
          </div>
          <div class="table-container">
            <Table
              border
              :columns="SENSITIVE.ruleColumns"
              :data="SENSITIVE.showRuleList"
              :scroll="sensitiveTableScroll"
              size="small"
              :loading="SENSITIVE.loading"
            >
              <template #senMode="{ row }">
                {{ getSenMode(row.senMode).i18n }}
              </template>
              <template #ruleAction="{ row }">
                <div class="rule-action-list">
                  <Button @click="handleGoRange(row)" type="text" size="small" :disabled="!row.enable || row.deprecated">
                    {{ $t('fan-wei') }}
                  </Button>
                  <Button @click="handleViewRuleDetail(row)" type="text" size="small" :disabled="row.deprecated">
                    {{ $t('xiang-qing') }}
                  </Button>
                  <Button
                    @click="handleEditRuleDetail(row)"
                    type="text"
                    size="small"
                    v-if="myAuth.includes('DM_SECRULES_MANAGE')"
                    :disabled="row.deprecated"
                  >
                    {{ $t('she-zhi') }}
                  </Button>
                </div>
              </template>
              <template #enable="{ row }">
                <i-switch
                  v-model="row.enable"
                  true-color="#52C41A"
                  :disabled="row.deprecated"
                  @on-change="handleEnableSpecRule(row, 'enable', false)"
                />
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
    <CCModal v-model="showEditSpecRuleModal" :title="$t('she-zhi')" @ok="handleEditRule('edit', false)" @cancel="handleCloseModal" width="800px">
      <Form :label-width="80">
        <FormItem :label="$t('deng-ji')" v-if="selectedRule.ruleKind === 'QUERY'">
          <Select v-model="selectedRule.warnLevel" style="width: 200px">
            <Option key="SUGGEST" value="SUGGEST">{{ $t('ti-shi') }}</Option>
            <Option key="FAILURE" value="FAILURE">{{ $t('zu-sai') }}</Option>
          </Select>
        </FormItem>
        <FormItem :label="$t('tuo-min-fang-shi')" v-if="selectedRule.ruleKind === 'SENSITIVE'">
          <Select v-model="selectedRule.senMode" style="width: 200px">
            <Option v-for="sen in ruleSetting.senConf.senMode" :key="sen.name" :value="sen.name">
              {{ sen.i18n }}
            </Option>
          </Select>
        </FormItem>
        <FormItem :label="$t('can-shu')">
          <Table :columns="specRuleParamColumns" :data="specRuleParamList" size="small" border height="300">
            <template #value="{ row }">
              <div>
                <Input v-model="ruleParam[row.name]" />
              </div>
            </template>
          </Table>
        </FormItem>
      </Form>
    </CCModal>
    <CCModal v-model="showForceRuleModal" :title="forceRuleModalTitle">
      <div class="title" v-html="forceRuleModalText" style="margin-bottom: 10px"></div>
      <Table :columns="forceRuleRefererColumns" :data="forceRuleRefererList" size="small">
        <template #envName="{ row }">
          <Tooltip :content="row.envName" placement="top" transfer>
            <span class="force-referer-cell">
              {{ row.envName }}
            </span>
          </Tooltip>
        </template>
        <template #envDesc="{ row }">
          <Tooltip :content="row.envDesc" placement="top" transfer>
            <span class="force-referer-cell">
              {{ row.envDesc }}
            </span>
          </Tooltip>
        </template>
      </Table>
      <template #footer>
        <div>
          <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
          <Button type="error" @click="handleEditRule(forceEvent.type, true)" v-if="forceRuleModalTitle">
            {{ forceRuleModalTitle }}
          </Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import { RULE_WARN_LEVEL } from '@/utils';
import DataSourceRangeTags from '@/views/security/components/DataSourceRangeTags';
import { mapActions, mapGetters, mapState } from 'vuex';

export default {
  name: 'SpecDetail',
  components: { DataSourceRangeTags },
  mounted() {
    this.specId = this.$route.params.specId;
    this.activeTab = this.normalizeRuleKind(this.$route.query && this.$route.query.ruleKind);
    this.getRuleSetting();
    this.getSpecDetail();
  },
  computed: {
    ...mapState(['ruleSetting', 'myAuth']),
    ...mapGetters(['getTargetType', 'getSenMode']),
    isQuery() {
      return this.activeTab === 'QUERY';
    },
    ruleKindOptions() {
      return [
        { value: 'QUERY', label: this.$t('cha-xun-gui-ze') },
        { value: 'SENSITIVE', label: this.$t('tuo-min-gui-ze') }
      ];
    },
    queryTableScroll() {
      return { x: 1250 };
    },
    sensitiveTableScroll() {
      return { x: 910 };
    }
  },
  data() {
    return {
      activeTab: 'QUERY',
      ruleKinds: ['QUERY', 'SENSITIVE'],
      specId: '',
      showData: [],
      ruleListLoading: false,
      ruleParam: {},
      forceEvent: {
        type: '',
        data: {}
      },
      showForceRuleModal: false,
      forceRuleModalTitle: '',
      forceRuleModalText: '',
      forceRuleRefererList: [],
      forceRuleRefererColumns: [
        {
          title: this.$t('huan-jing-ming-cheng'),
          key: 'envName',
          slot: 'envName'
        },
        {
          title: this.$t('huan-jing-miao-shu'),
          key: 'envDesc',
          slot: 'envDesc'
        }
      ],
      showEditSpecRuleModal: false,
      specRuleParamColumns: [
        {
          title: this.$t('ming-cheng'),
          key: 'name'
        },
        {
          title: this.$t('miao-shu'),
          key: 'hint'
        },
        {
          title: this.$t('zhi'),
          slot: 'value'
        }
      ],
      specRuleParamList: [],
      RULE_WARN_LEVEL,
      specDetail: {
        specName: '',
        specDesc: ''
      },
      selectedRule: {},
      supportTargetList: [],
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
            title: this.$t('wei-gui-ti-shi'),
            key: 'ruleDesc',
            width: 360
          },
          {
            title: this.$t('shi-yong-shu-ju-yuan'),
            slot: 'dsRange',
            width: 250
          },
          {
            title: this.$t('dui-xiang-lei-xing'),
            key: 'targetTypeI18n',
            width: 100
          },
          {
            title: this.$t('deng-ji'),
            slot: 'warnLevel',
            width: 90
          },
          {
            title: this.$t('qi-yong'),
            slot: 'enable',
            width: 80,
            fixed: 'right'
          },
          {
            title: this.$t('cao-zuo'),
            slot: 'ruleAction',
            width: 170,
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
            key: 'ruleDesc',
            width: 360
          },
          {
            title: '脱敏方式',
            width: 100,
            slot: 'senMode'
          },
          {
            title: this.$t('qi-yong'),
            slot: 'enable',
            width: 80,
            fixed: 'right'
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
  methods: {
    ...mapActions(['getRuleSetting']),
    normalizeRuleKind(ruleKind) {
      return this.ruleKinds.includes(ruleKind) ? ruleKind : 'QUERY';
    },
    handlePageChange(pageNum) {
      this[this.activeTab].pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this[this.activeTab].pageSize = pageSize;
      this.handlePageChange(1);
    },
    handleRuleKindChange(name) {
      const ruleKind = this.normalizeRuleKind(name);
      if (ruleKind !== name) {
        this.activeTab = ruleKind;
        this.$Message.warning(this.$t('gui-ze-lei-xing-bu-neng-wei-kong'));
      }
      this.$router.replace({ query: { ruleKind } });
      if (!this[ruleKind].total) {
        this.getSpecDetail();
      }
    },
    handleEnableSpecRule(rule, type, force) {
      this.selectedRule = rule;
      this.handleEditRule(type, force);
    },
    async handleEditRule(type, force = false) {
      const { ruleId, enable, warnLevel, parameterValue, ruleKind, senMode } = this.selectedRule;
      const data = force
        ? this.forceEvent.data
        : {
            specId: this?.specId,
            rule: {
              ruleKind,
              senMode,
              enable,
              ruleId,
              warnLevel,
              ruleParam: type === 'enable' ? parameterValue : this.ruleParam
            }
          };
      data.force = force;
      const res = await this.$services.dmSecurityRulesSpecSaveRule({
        data
      });

      if (res?.data?.success) {
        await this.handleCloseModal();
        await this.getSpecDetail();
        if (res.data && res.data?.message) {
          this.$Message.success(res.data.message);
        } else if (res.message) {
          this.$Message.success(res.message);
        }
      } else {
        this.selectedRule.enable = !enable;
        this.forceEvent = {
          type,
          data
        };
        this.showForceRuleModal = true;
        this.forceRuleModalTitle =
          type === 'enable' ? (enable ? this.$t('qiang-zhi-qi-yong') : this.$t('qiang-zhi-guan-bi')) : this.$t('qiang-zhi-xiu-gai');
        this.forceRuleModalText = (res.data && res.data.message) || res.message || '';
        this.forceRuleRefererList = (res.data && res.data.referer) || res.referer || [];
      }
    },
    async handleCloseModal() {
      this.showForceRuleModal = false;
      this.showEditSpecNameModal = false;
      this.showEditSpecDescModal = false;
      this.showEditSpecRuleModal = false;
      this.newSpecName = '';
      this.newSpecDesc = '';
    },
    handleRuleSearch(type = 'search') {
      this[this.activeTab].ruleList = this[this.activeTab].allRuleList.filter(
        (rule) => rule.ruleName.includes(this[this.activeTab].search) || rule.ruleDesc.includes(this[this.activeTab].search)
      );
      this[this.activeTab].total = this[this.activeTab].ruleList.length;

      if (type === 'search') {
        this.handlePageSizeChange(this[this.activeTab].pageSize);
      } else {
        this.setTableShowData();
      }
    },
    async getSpecDetail() {
      this[this.activeTab].loading = true;
      const res = await this.$services.dmSecurityRulesSpecDetail({
        data: {
          specId: this.specId,
          ruleKind: this.activeTab
        }
      });

      this[this.activeTab].loading = false;
      if (res.success) {
        this.specDetail = res.data;
        // this[this.activeTab].search = '';
        this[this.activeTab].allRuleList = res.data.ruleList;
        this[this.activeTab].ruleList = res.data.ruleList;
        this[this.activeTab].total = res.data.ruleList.length;
        this.handleRuleSearch('refresh');
      }
    },
    setTableShowData() {
      const { pageNum, pageSize } = this[this.activeTab];
      this[this.activeTab].showRuleList = this[this.activeTab].ruleList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    generateParamList() {
      const { parameterDef, parameterValue } = this.selectedRule;
      const specRuleParamList = [];
      parameterDef.forEach((def) => {
        this.ruleParam[def.name] = parameterValue ? parameterValue[def.name] : null;
        specRuleParamList.push({ ...def, value: parameterValue ? parameterValue[def.name] : null });
      });
      this.specRuleParamList = specRuleParamList;
    },
    handleGoRange(rule) {
      this.$router.push({
        path: `/system/dmspec/${this.specId}/rule/${rule.ruleId}/range`,
        query: {
          ruleKind: this.activeTab,
          specName: this.specDetail.specName,
          ruleName: rule.ruleName,
          refId: rule.refId
        }
      });
    },
    handleViewRuleDetail(rule) {
      this.$router.push({
        path: `/system/dmspec/${this.specId}/rule/${rule.ruleId}/detail`,
        query: {
          ruleKind: this.activeTab,
          specName: this.specDetail.specName,
          ruleName: rule.ruleName,
          refId: rule.refId
        }
      });
    },
    async getSpecRuleDetail(rule) {
      const res = await this.$services.dmSecurityRulesSpecRuleDetail({
        data: {
          specId: this.specDetail.specId,
          ruleId: rule.ruleId,
          ruleKind: this.activeTab
        }
      });

      if (res.success) {
        this.selectedRule = res.data;
      }
    },
    async handleEditRuleDetail(rule) {
      await this.getSpecRuleDetail(rule);
      this.generateParamList();
      this.showEditSpecRuleModal = true;
    }
  }
};
</script>
<style lang="less" scoped>
:deep(.ivu-form-item) {
  margin-bottom: 10px;
}

.spec-detail {
  height: 100%;
  display: flex;
  flex-direction: column;

  // Fix the problem of inconsistent height of the fixed list header
  :deep(.ivu-table-small) {
    // Centre Main Header Text
    .ivu-table-header thead tr th,
    .ivu-table-fixed-header thead tr th {
      text-align: center !important;
    }

    .ivu-table-fixed-right {
      .ivu-table-fixed-header {
        thead tr th {
          height: 34px !important;
          line-height: 34px !important;
          padding: 0 !important;
          text-align: center !important;
        }
      }
    }

    // Ensure that the level of the fixed column header is consistent
    .ivu-table-fixed-header {
      height: 34px !important;
    }
  }
}
:deep(.rule-action-list) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  width: 100%;
  white-space: nowrap;
}
:deep(.rule-action-list .ivu-btn) {
  flex: 0 0 auto;
  padding-right: 4px;
  padding-left: 4px;
  white-space: nowrap;
}
:deep(.ivu-table-fixed-header) {
  .ivu-table-cell {
    height: 100%;
  }
}
.force-referer-cell {
  display: inline-block;
  width: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
