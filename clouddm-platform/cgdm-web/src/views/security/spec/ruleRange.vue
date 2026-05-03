<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import deepClone from 'lodash.clonedeep';
import { hasSchema } from '@/utils';

const EMPTY_RANGE = {
  rangeId: '',
  force: false,
  matchMode: '',
  rangeType: '',
  environment: '',
  instance: '',
  catalog: '',
  schema: '',
  tableorview: '',
  column: '',
  nodes: '',
  chooseAll: ''
};

const RANGE_FORM_KEY_MAP = {
  environment: 'envId',
  instance: 'dsId',
  catalog: 'catalog',
  schema: 'schema',
  tableorview: 'table',
  column: 'column'
};

const RANGE_RES_KEY_MAP = {
  environment: 'env',
  instance: 'ds',
  catalog: 'catalog',
  schema: 'schema',
  table: 'table',
  view: 'view',
  column: 'column'
};

export default {
  name: 'ruleRange',
  data() {
    return {
      scopeSort: ['Environment', 'Instance'],
      scopeList: [],
      scopeItemList: [],
      commonScopeItemList: [],
      matchModeList: [],
      dsType: '',
      selectedScope: {},
      selectedRange: {},
      forceEvent: null,
      forceData: null,
      showForceRuleModal: false,
      forceRuleModalTitle: '',
      forceRuleModalText: '',
      forceRuleRefererList: [],
      forceRuleRefererColumns: [
        {
          title: this.$t('huan-jing-ming-cheng'),
          key: 'envName'
        },
        {
          title: this.$t('huan-jing-miao-shu'),
          key: 'envDesc'
        }
      ],
      loading: false,
      tableLevelType: 'Table',
      dsHasSchema: false,
      specId: null,
      ruleId: null,
      ruleName: '',
      ruleKind: '',
      SCOPE_KEY: {},
      scopeSetting: [],
      rangeForm: deepClone(EMPTY_RANGE),
      rangeFormValidate: {},
      isEditRange: false,
      showAddRangeModal: false,
      specName: '',
      pageSize: 20,
      pageNum: 1,
      total: 0,
      search: '',
      rangeList: [],
      showRangeList: [],
      rangeColumns: [
        {
          title: '范围',
          slot: 'rangeType',
          width: 200
        },
        {
          title: '匹配模式',
          slot: 'matchMode',
          width: 200
        },
        {
          title: '设置值',
          key: 'desc'
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'rangeAction',
          width: 120,
          fixed: 'right'
        }
      ],
      envList: []
    };
  },
  computed: {
    ...mapGetters(['getMatchMode', 'getScopeByMatchMode', 'getMatchModeList', 'getScopeListByMatchMode', 'getScopeListByInstance']),
    ...mapState(['myAuth']),
    breadcrumbList() {
      return [
        {
          label: this.$t('gui-fan-lie-biao'),
          to: '/system/dmspeclist',
          key: 'one'
        },
        {
          label: this.specName,
          to: {
            path: `/system/dmspec/${this.specId}`,
            query: { ruleKind: this.ruleKind }
          },
          key: 'two'
        },
        {
          label: `${this.ruleName}的范围列表`,
          key: 'three'
        }
      ];
    },
    scopeSelectList() {
      return this.scopeList.filter((scope) => !scope.hidden);
    }
  },
  async mounted() {
    this.specId = this.$route.params.specId || null;
    this.ruleId = this.$route.params.ruleId || null;
    this.specName = this.$route.query.specName || '';
    this.ruleKind = this.$route.query.ruleKind || '';
    this.ruleName = this.$route.query.ruleName || '';
    this.refId = this.$route.query.refId || '';
    await this.getRuleSetting();
    await this.getSpecRuleRange();
  },
  methods: {
    ...mapActions(['getRuleSetting']),
    handleMatchModeChange() {
      console.log('match mode change');
      this.scopeList = this.getScopeListByMatchMode(this.ruleKind, this.rangeForm.matchMode);
      this.rangeForm = {
        ...this.rangeForm,
        rangeType: '',
        environment: '',
        instance: '',
        catalog: '',
        schema: '',
        tableorview: '',
        nodes: '',
        chooseAll: ''
      };
      this.rangeForm.rangeType = this.scopeSelectList[0].name;
      this.generateScopeSetting(this.rangeForm.rangeType);
      this.$refs.rangeForm.resetFields();
    },
    handleScopeChange(scopeName) {
      if (!scopeName) {
        return;
      }
      this.rangeForm = {
        ...this.rangeForm,
        environment: '',
        instance: '',
        catalog: '',
        schema: '',
        tableorview: '',
        nodes: '',
        chooseAll: ''
      };
      this.$refs.rangeForm.resetFields();
      this.generateScopeSetting(scopeName);
    },
    async handleCloseForceModal() {
      this.showForceRuleModal = false;
    },
    async handleCloseModal() {
      this.selectedRange = {};
      this.scopeList = [];
      this.scopeItemList = [];
      this.rangeForm = deepClone(EMPTY_RANGE);
      this.showAddRangeModal = false;
      this.showForceRuleModal = false;
      this.forceEvent = null;
      this.forceData = null;
    },
    async handleDeleteRange(range, isForce = false) {
      this.selectedRange = range;

      const deleteRange = async (force) => {
        const res = await this.$services.dmSecurityRulesSpecDeleteRange({
          data: {
            specId: this.specId,
            rangeId: this.selectedRange.rangeId,
            force
          }
        });

        if (!res.success) {
          this.$Message.error(res.message || this.$t('cao-zuo-shi-bai'));
          return;
        }

        const result = res.data || {};

        if (result.success) {
          this.$Message.success(result.message);
          await this.handleCloseModal();
          await this.getSpecRuleRange();
          return;
        }

        if (force) {
          this.showForceRuleModal = false;
          this.$Message.error(result.message || this.$t('cao-zuo-shi-bai'));
          return;
        }

        this.forceEvent = deleteRange;
        this.showForceRuleModal = true;
        this.forceRuleModalTitle = this.$t('shan-chu-fan-wei');
        this.forceRuleModalText = result.message;
        this.forceRuleRefererList = result.referer || [];
      };

      await deleteRange(isForce);
    },
    async handleAddRange(force = false) {
      const handleSaveRange = async (data) => {
        const res = await this.$services.dmSecurityRulesSpecSaveRange({
          data
        });
        this.showAddRangeModal = false;

        if (res.success) {
          this.showRuleDetailModal = false;
          if (res.data) {
            if (res.data.success) {
              this.$Message.success(res.data.message);
              await this.handleCloseModal();
              await this.getSpecRuleRange();
            } else {
              this.forceEvent = this.handleAddRange;
              this.forceData = data;
              this.showForceRuleModal = true;
              this.forceRuleModalTitle = this.isEditRange ? this.$t('bian-ji-fan-wei') : this.$t('xin-jian-fan-wei');
              this.forceRuleModalText = res.data.message;
              this.forceRuleRefererList = res.data.referer;
            }
          }
        }
      };

      if (force) {
        if (this.forceData) {
          this.forceData.force = force;
          await handleSaveRange(this.forceData);
        }
      } else {
        this.$refs.rangeForm.validate(async (valid) => {
          if (valid) {
            const lastKey = this.scopeItemList[this.scopeItemList.length - 1].key;
            if (this.rangeForm.matchMode === 'EXACT') {
              this.rangeForm.nodes = [...this.rangeForm[lastKey]];
            } else {
              this.rangeForm.nodes = [this.rangeForm[lastKey]];
            }

            const data = {
              ...this.rangeForm,
              tableLevelType: this.tableLevelType,
              table: this.rangeForm.tableorview,
              force,
              envId: this.rangeForm.environment,
              dsId: this.rangeForm.instance,
              specId: this.specId,
              ruleId: this.ruleId,
              ruleKind: this.ruleKind,
              [RANGE_FORM_KEY_MAP[lastKey]]: null
            };

            await handleSaveRange(data);
          }
        });
      }
    },
    handleShowAddRangeModal(isEditRange = false, range) {
      try {
        this.isEditRange = isEditRange;
        this.matchModeList = this.getMatchModeList(this.ruleKind);
        this.rangeForm = deepClone(EMPTY_RANGE);
        this.rangeForm.matchMode = this.matchModeList[0].name;
        this.scopeList = this.getScopeListByMatchMode(this.ruleKind, this.rangeForm.matchMode);
        const scopeSort = [];
        const commonScopeItemList = [];
        this.scopeList.forEach((scopeItem) => {
          scopeSort.push(scopeItem.name);
          commonScopeItemList.push({
            ...scopeItem,
            key: scopeItem.name.toLowerCase(),
            loaded: false,
            list: [],
            last: false
          });
        });
        this.commonScopeItemList = commonScopeItemList;
        this.scopeSort = scopeSort;
        if (isEditRange) {
          if (range) {
            this.selectedRange = deepClone(range);
            const { rangeId, matchMode, rangeType, env, ds, catalog, schema, table, nodes, dsType, chooseAll, tableLevelType } = this.selectedRange;
            this.selectedRange.environment = env;
            this.selectedRange.instance = ds;
            this.tableLevelType = tableLevelType;
            if (matchMode === 'EXACT') {
              this.selectedRange[rangeType.toLowerCase()] = nodes;
            } else {
              this.selectedRange[rangeType.toLowerCase()] = nodes[0].value;
            }

            this.rangeForm = {
              rangeId,
              force: false,
              matchMode,
              rangeType,
              environment: env ? env.value : '',
              instance: ds ? ds.value : '',
              catalog: catalog ? catalog.value : '',
              schema: schema ? schema.value : '',
              tableorview: table ? table.value : '',
              column: '',
              nodes: '',
              chooseAll
            };

            if (dsType) {
              this.dsType = dsType;
              this.dsHasSchema = hasSchema(dsType);
            }
            if (matchMode === 'EXACT') {
              this.rangeForm[rangeType.toLowerCase()] = nodes.map((node) => node.value);
            } else {
              this.rangeForm[rangeType.toLowerCase()] = nodes[0].value;
            }

            this.generateInstanceScopeSetting('init');
          }
        } else {
          this.rangeForm.rangeType = this.scopeList[0].name;
          this.generateScopeSetting();
        }

        this.showAddRangeModal = true;
      } catch (e) {
        console.log(e);
      }
    },
    generateInstanceScopeSetting(type) {
      const instanceScopeList = this.getScopeListByInstance(this.ruleKind, this.rangeForm.matchMode, this.dsType);
      const scopeItemList = [];

      for (let i = 0; i < instanceScopeList.length; i++) {
        let item = {
          ...instanceScopeList[i],
          key: instanceScopeList[i].name.toLowerCase(),
          loaded: false,
          list: [],
          last: false
        };
        if (i < 2 && this.scopeItemList[i]) {
          item = this.scopeItemList[i];
        }

        scopeItemList.push(item);

        if (item.name === this.rangeForm.rangeType) {
          break;
        }
      }

      if (!instanceScopeList.length) {
        scopeItemList.push(this.commonScopeItemList[0]);
      }

      for (let i = 0; i < scopeItemList.length - 1; i++) {
        scopeItemList[i].nextKey = scopeItemList[i + 1].key;
      }

      if (type === 'init') {
        scopeItemList.forEach((scopeItem) => {
          scopeItem.last = false;
          const listData = this.selectedRange[scopeItem.key];
          if (listData) {
            scopeItem.list = Array.isArray(listData) ? listData : [listData];
            scopeItem.list = scopeItem.list.map((scopeItemItem) => ({
              ...scopeItemItem,
              objId: scopeItemItem.value,
              objName: scopeItemItem.name,
              objDesc: scopeItemItem.desc
            }));
          }
        });
      }

      scopeItemList.forEach((scopeItem) => {
        if (scopeItem.key === 'tableorview' && scopeItem.children?.length) {
          this.tableLevelType = scopeItem.children[0].name;
        }
      });

      console.log(this.rangeForm.rangeType);

      scopeItemList[scopeItemList.length - 1].last = true;

      this.scopeItemList = scopeItemList;
      this.generateValidate();
    },
    generateScopeSetting() {
      const scopeItemList = [];
      for (let i = 0; i < this.scopeSort.length; i++) {
        const scopeItem = this.commonScopeItemList[i];
        console.log(scopeItem.loaded);
        if (this.rangeForm.rangeType === this.scopeList[i].name) {
          scopeItemList.push({
            ...scopeItem,
            last: true
          });
          break;
        } else {
          scopeItemList.push({
            ...scopeItem,
            last: false
          });
        }
      }

      for (let i = 0; i < scopeItemList.length - 1; i++) {
        scopeItemList[i].nextKey = scopeItemList[i + 1].key;
      }

      scopeItemList.forEach((scopeItem) => {
        if (scopeItem.key === 'tableorview' && scopeItem.children?.length) {
          this.tableLevelType = scopeItem.children[0].name;
        }
      });

      this.scopeItemList = scopeItemList;
      this.generateValidate();

      this.handleGetList();
    },
    generateValidate() {
      const RANGE_FORM_VALIDATE = {
        environment: [
          {
            required: true,
            message: '环境不能为空'
          }
        ],
        instance: [
          {
            required: true,
            message: '实例不能为空'
          }
        ],
        catalog: [
          {
            required: true,
            message: '库不能为空'
          }
        ],
        schema: [
          {
            required: true,
            message: '模式不能为空'
          }
        ],
        table: [
          {
            required: true,
            message: '表不能为空'
          }
        ],
        view: [
          {
            required: true,
            message: '视图不能为空'
          }
        ],
        column: [
          {
            required: true,
            message: '列不能为空'
          }
        ]
      };

      const validate = {};
      this.scopeItemList.forEach((scope) => {
        validate[scope.key] = RANGE_FORM_VALIDATE[scope.key];
      });

      this.rangeFormValidate = validate;
    },
    handleTableOrViewChange(name) {
      if (this.tableLevelType && name.value && this.tableLevelType === name.value) {
        return;
      }
      this.tableLevelType = name.value;
      if (this.tableLevelType) {
        this.handleGetList({ nextKey: 'tableorview' });
      }
    },
    handleSelectOpenChange(scope, force, open) {
      console.log(scope, force, open);
      if ((!scope.loaded || force) && open) {
        scope.loaded = true;
        const { key } = scope;
        const { environment, instance, catalog, schema, tableorview } = this.rangeForm;

        console.log(key);

        const levels = [];
        switch (key) {
          case 'environment':
            this.handleListEnv(key);
            break;
          case 'instance':
            if (environment) {
              this.handleListIns(key, {
                envId: this.rangeForm.environment,
                refId: this.refId,
                rangeType: 'Instance',
                matchType: this.rangeForm.matchMode,
                ruleKind: this.ruleKind
              });
            }
            break;
          case 'catalog':
            if (environment && instance) {
              this.handleListCatalogOrSchema(this.dsHasSchema ? key : 'schema', {
                levels: [environment, instance],
                refreshCache: force
              });
            }
            break;
          case 'schema':
            if (this.dsHasSchema) {
              if (environment && instance && catalog) {
                this.handleListCatalogOrSchema(key, { levels: [environment, instance, catalog], refreshCache: force });
              }
            } else {
              if (environment && instance) {
                this.handleListCatalogOrSchema(key, { levels: [environment, instance], refreshCache: force });
              }
            }
            break;
          case 'tableorview':
            if (this.dsHasSchema) {
              if (environment && instance && catalog && schema) {
                this.handleListTableOrView(key, {
                  levels: [environment, instance, catalog, schema],
                  leafType: this.tableLevelType,
                  refreshCache: force
                });
              }
            } else {
              if (environment && instance && schema) {
                this.handleListTableOrView(key, {
                  levels: [environment, instance, schema],
                  leafType: this.tableLevelType,
                  refreshCache: force
                });
              }
            }
            break;
          case 'column':
            if (this.dsHasSchema) {
              if (environment && instance && catalog && schema && tableorview) {
                this.handleListColumn(key, {
                  levels: [environment, instance, catalog, schema],
                  targetType: this.tableLevelType,
                  targetName: tableorview
                });
              }
            } else {
              if (environment && instance && schema && tableorview) {
                this.handleListColumn(key, {
                  levels: [environment, instance, schema],
                  targetType: this.tableLevelType,
                  targetName: tableorview
                });
              }
            }
            break;
          default:
            break;
        }
      }
    },
    handleGetList(scope = { nextKey: 'environment' }, selectedItem = {}) {
      console.log(scope.key, selectedItem.value);
      const { nextKey, key, last } = scope;

      if (last) {
        return;
      }

      if (key && selectedItem.value) {
        if (this.rangeForm[key] === selectedItem.value) {
          return;
        }
        this.rangeForm[key] = selectedItem.value;
      }

      const { environment, instance, catalog, schema, tableorview } = this.rangeForm;

      if (key === 'instance') {
        let dsHasSchema = false;
        scope.list.forEach((item) => {
          if (item.objId === selectedItem.value) {
            this.dsType = item.objAttr.dsType;
            dsHasSchema = hasSchema(this.dsType);
          }
        });

        this.dsHasSchema = dsHasSchema;
        this.generateInstanceScopeSetting();
      }

      const levels = [];
      console.log(nextKey);
      switch (nextKey) {
        case 'environment':
          this.handleListEnv(nextKey);
          break;
        case 'instance':
          this.rangeForm = {
            ...this.rangeForm,
            instance: '',
            catalog: '',
            schema: '',
            tableorview: '',
            column: '',
            nodes: '',
            chooseAll: ''
          };
          if (this.rangeForm.environment) {
            this.handleListIns(nextKey, {
              envId: this.rangeForm.environment,
              refId: this.refId,
              rangeType: this.rangeForm.rangeType,
              matchType: this.rangeForm.matchMode,
              ruleKind: this.ruleKind
            });
          }
          break;
        case 'catalog':
          this.rangeForm = {
            ...this.rangeForm,
            catalog: '',
            schema: '',
            tableorview: '',
            column: '',
            nodes: '',
            chooseAll: ''
          };
          if (environment && this.rangeForm.instance) {
            this.handleListCatalogOrSchema(this.dsHasSchema ? nextKey : 'schema', {
              levels: [environment, instance]
            });
          }
          break;
        case 'schema':
          this.rangeForm = {
            ...this.rangeForm,
            schema: '',
            tableorview: '',
            column: '',
            nodes: '',
            chooseAll: ''
          };
          if (environment && instance && catalog) {
            this.handleListCatalogOrSchema(nextKey, { levels: [environment, instance, catalog] });
          }
          break;
        case 'tableorview':
          this.rangeForm = {
            ...this.rangeForm,
            tableorview: '',
            column: '',
            nodes: '',
            chooseAll: ''
          };
          if (this.dsHasSchema) {
            if (environment && instance && catalog && schema) {
              this.handleListTableOrView(nextKey, {
                levels: [environment, instance, catalog, schema],
                leafType: this.tableLevelType
              });
            }
          } else {
            if (environment && instance && schema) {
              this.handleListTableOrView(nextKey, {
                levels: [environment, instance, schema],
                leafType: this.tableLevelType
              });
            }
          }
          break;
        case 'column':
          this.rangeForm = {
            ...this.rangeForm,
            column: '',
            nodes: '',
            chooseAll: ''
          };
          if (this.dsHasSchema) {
            if (environment && instance && catalog && schema && tableorview) {
              this.handleListColumn(nextKey, {
                levels: [environment, instance, catalog, schema],
                targetType: this.tableLevelType,
                targetName: tableorview
              });
            }
          } else {
            if (environment && instance && schema && tableorview) {
              this.handleListColumn(nextKey, {
                levels: [environment, instance, schema],
                targetType: this.tableLevelType,
                targetName: tableorview
              });
            }
          }
          break;
        default:
          break;
      }
    },
    async getSpecRuleRange() {
      this.loading = true;
      const res = await this.$services.dmSecurityRulesSpecFetchRange({
        data: {
          ruleId: this.ruleId,
          specId: this.specId,
          ruleKind: this.ruleKind
        }
      });

      this.loading = false;
      if (res.success) {
        this.rangeList = res.data;
        this.total = res.data.length;
        this.setTableShowData();
      }
    },
    async handleListEnv(type) {
      const res = await this.$services.dmSecurityRangeListEnv();
      if (res.success) {
        this.scopeItemList.forEach((scope) => {
          if (scope.key === type) {
            const list = [];
            res.data.forEach((item) => {
              item.disabled = item.objAttr?.disabled;
              list.push(item);
            });
            scope.list = list;
            scope.loaded = true;
          }
        });
      }
    },
    async handleListIns(type, data) {
      const res = await this.$services.dmSecurityRangeListIns({
        data
      });
      if (res.success) {
        this.scopeItemList.forEach((scope) => {
          if (scope.key === type) {
            const list = [];
            res.data.forEach((item) => {
              item.disabled = item.objAttr?.disabled;
              list.push(item);
            });
            scope.list = list;
            scope.loaded = true;
          }
        });
      }
    },
    async handleListCatalogOrSchema(type, data) {
      const res = await this.$services.dmSecurityRangeListLevels({
        data
      });
      if (res.success) {
        this.scopeItemList.forEach((scope) => {
          console.log(scope.key, type);
          if (scope.key === type) {
            scope.list = res.data;
            scope.loaded = true;
          }
        });
      }
    },
    async handleListTableOrView(type, data) {
      console.log(type);
      const res = await this.$services.dmSecurityRangeListLeaf({
        data
      });

      if (res.success) {
        this.scopeItemList.forEach((scope) => {
          if (scope.key === type) {
            scope.list = res.data;
            scope.loaded = true;
          }
        });
      }
    },
    async handleListColumn(type, data) {
      console.log(type, data);
      const res = await this.$services.dmSecurityRangeListColumn({
        data
      });

      if (res.success) {
        this.scopeItemList.forEach((scope) => {
          if (scope.key === type) {
            scope.list = res.data;
            scope.loaded = true;
          }
        });
      }
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    setTableShowData() {
      const { pageNum, pageSize } = this;
      this.showRangeList = this.rangeList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    }
  }
};
</script>

<template>
  <div class="rule-range">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb v-if="breadcrumbList.length">
            <BreadcrumbItem v-for="breadcrumb in breadcrumbList" :to="breadcrumb.to" :key="breadcrumb.key">
              {{ breadcrumb.label }}
            </BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option">
            <div class="left">
              <Input v-model="search" style="width: 280px; margin-right: 10px"></Input>
              <Button @click="getSpecRuleRange" type="primary">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button
                @click="handleShowAddRangeModal(false)"
                type="primary"
                ghost
                style="margin-right: 10px"
                icon="md-add"
                v-if="myAuth.includes('DM_SECRULES_MANAGE')"
              >
                {{ $t('xin-jian-fan-wei') }}
              </Button>
              <Button @click="getSpecRuleRange">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table border :columns="rangeColumns" :data="showRangeList" size="small" :loading="loading">
              <template #rangeType="{ row }">
                {{ getScopeByMatchMode(ruleKind, row.matchMode, row.rangeType) }}
              </template>
              <template #matchMode="{ row }">
                {{ getMatchMode(ruleKind, row.matchMode).i18n }}
              </template>
              <template #rangeAction="{ row }">
                <Button @click="handleShowAddRangeModal(true, row)" type="text" size="small" v-if="myAuth.includes('DM_SECRULES_MANAGE')">
                  {{ $t('bian-ji') }}
                </Button>
                <Poptip confirm transfer :title="$t('que-ding-yao-shan-chu-gai-fan-wei')" @on-ok="handleDeleteRange(row)">
                  <Button type="text" size="small" v-if="myAuth.includes('DM_SECRULES_MANAGE')">
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
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          v-model="pageNum"
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showAddRangeModal" :title="isEditRange ? $t('bian-ji-fan-wei') : $t('xin-jian-fan-wei')" :closable="false">
      <Form :label-width="80" :model="rangeForm" :rules="rangeFormValidate" ref="rangeForm">
        <FormItem :label="$t('pi-pei-mo-shi')">
          <RadioGroup v-model="rangeForm.matchMode" @on-change="handleMatchModeChange">
            <Radio v-for="mode in matchModeList" :label="mode.name" :key="mode.name">
              {{ mode.i18n }}
            </Radio>
          </RadioGroup>
        </FormItem>
        <FormItem :label="$t('fan-wei')">
          <Select v-model="rangeForm.rangeType" @on-change="handleScopeChange" clearable style="width: 384px">
            <Option v-for="mode in scopeSelectList" :value="mode.name" :key="mode.name">
              {{ mode.i18n }}
            </Option>
          </Select>
        </FormItem>
        <FormItem v-for="scope in scopeItemList" :key="scope.name" :label="scope.i18n" :prop="scope.key">
          <div
            style="display: flex; justify-content: center"
            v-if="rangeForm.matchMode === 'EXACT' || (rangeForm.matchMode !== 'EXACT' && !scope.last)"
          >
            <Select
              v-model="tableLevelType"
              style="width: 60px; margin-right: 6px"
              @on-select="handleTableOrViewChange"
              clearable
              :disabled="scope.loading"
              v-if="scope.children"
            >
              <Option v-for="child in scope.children" :value="child.name" :key="child.name">
                {{ child.i18n }}
              </Option>
            </Select>
            <Select
              v-model="rangeForm[scope.key]"
              :multiple="scope.last"
              filterable
              @on-open-change="handleSelectOpenChange(scope, false, $event)"
              @on-select="scope.last ? null : handleGetList(scope, $event)"
              clearable
              :disabled="scope.loading"
              :max-tag-count="5"
            >
              <Option
                :disabled="item.disabled"
                v-for="item in scope.list"
                :value="item.objId === '-1' ? item.objName : item.objId"
                :key="`${scope.key}.${item.objId === '-1' ? item.objName : item.objId}`"
                :label="`${item.objName} ${item.objDesc || ''}`"
              >
                <Tooltip v-if="item.disabled" :content="$t('bu-zhi-chi-gai-fan-wei')" transfer>
                  {{ item.objName }} {{ item.objDesc ? `(${item.objDesc})` : '' }}
                </Tooltip>
                <div v-else>{{ item.objName }} {{ item.objDesc ? `(${item.objDesc})` : '' }}</div>
              </Option>
            </Select>
            <Icon type="ios-loading" v-if="scope.loading" size="24"></Icon>
            <Icon type="ios-refresh" v-else size="24" @click="handleSelectOpenChange(scope, true, true)"></Icon>
          </div>
          <div style="display: flex; align-items: center; width: 384px" v-else>
            <Checkbox v-model="rangeForm.chooseAll">{{ $t('quan-bu') }}</Checkbox>
            <Input v-model="rangeForm[scope.key]" style="flex: 1" :placeholder="$t('qing-shu-ru-nei-rong')" v-if="!rangeForm.chooseAll" />
          </div>
        </FormItem>
      </Form>
      <template #footer>
        <div>
          <Button @click="handleCloseModal">{{ $t('qu-xiao') }}</Button>
          <Button @click="handleAddRange(false)" type="primary">{{ $t('que-ding') }}</Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showForceRuleModal" :title="forceRuleModalTitle">
      <div class="title" v-html="forceRuleModalText" style="margin-bottom: 10px"></div>
      <Table :columns="forceRuleRefererColumns" :data="forceRuleRefererList" size="small"></Table>
      <template #footer>
        <div>
          <Button @click="handleCloseForceModal">{{ $t('qu-xiao') }}</Button>
          <Button type="error" @click="forceEvent && forceEvent(true)">{{ forceRuleModalTitle }}</Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>

<style scoped lang="less">
.rule-range {
  height: 100%;
  display: flex;
  flex-direction: column;
}

:deep(.ivu-select-dropdown) {
  max-height: 300px !important;
}
</style>
