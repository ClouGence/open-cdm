<template>
  <div class="add-desensitization">
    <a-breadcrumb>
      <a-breadcrumb-item>
        <router-link :to="{ name: 'System_Desensitization' }">
          {{ $t('shu-ju-tuo-min') }}
        </router-link>
      </a-breadcrumb-item>
      <a-breadcrumb-item>{{ $t('xin-zeng-tuo-min-ce-lve') }}</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="tip">
      <b>{{ $t('ban-zhe-yan-tuo-min-can-shu-ge-shi') }}:</b>
      {{ $t('36-biao-shi-di-3456-wei-zhe-yan-5-biao-shi-zui-hou-5-wei-zhe-yan-geng-duo-can-shu-ge-shi-can-kao') }}
      <a>{{ $t('bang-zhu-wen-dang') }}</a>
    </div>
    <div class="content">
      <cc-table-tree-select :get-columns="getColumns" type="rule" :check-errors="checkRuleExpr" :nums="modifiedTableNumObj" />
      <div class="columns">
        <a-table
          :columns="columnColumns"
          size="small"
          :data-source="columnList"
          style="flex: 1"
          :row-key="(record) => record.name"
          :pagination="{ pageSize: 20 }"
          :loading="loading"
          bordered
        >
          <template #action="record" v-if="record.ruleType === 'PART_MASK'">
            <a-button type="link" @click="handlePreview(record)">{{ $t('yu-lan') }}</a-button>
          </template>
          <template #algorithm_type="record">
            <div
              :style="`background: ${record.ruleType === rawColumnListObj[record.name].ruleType ? 'transparent' : '#FFF7DB'};
            outline: 6px solid ${record.ruleType === rawColumnListObj[record.name].ruleType ? 'transparent' : '#FFF7DB'}`"
            >
              <a-select
                v-model="record.ruleType"
                :dropdownMatchSelectWidth="false"
                size="small"
                @change="handleRuleTypeChange(record)"
                style="width: 110px"
              >
                <a-select-option v-for="option in desensitizeRuleTypeList" :value="option.ruleType" :key="option.name">
                  {{ option.name }}
                </a-select-option>
              </a-select>
              <a-popover trigger="hover">
                <template #content>
                  <div>{{ $t('bian-ji-qian-suan-fa') }}:{{ record.rawRuleType }}</div>
                </template>
                <cc-iconfont
                  name="warning-ghost"
                  v-if="record.rawRuleType && record.ruleType !== record.rawRuleType"
                  color="rgba(0, 0, 0, 0.88)"
                  style="margin-left: 6px; margin-top: 5px"
                />
              </a-popover>
            </div>
          </template>
          <template #parameter_value="record">
            <div
              :style="`background: ${
                record.ruleExpr === rawColumnListObj[record.name].ruleExpr || record.ruleType === 'FULL_MASK' ? 'transparent' : '#FFF7DB'
              };
            outline: 6px solid ${
              record.ruleExpr === rawColumnListObj[record.name].ruleExpr || record.ruleType === 'FULL_MASK' ? 'transparent' : '#FFF7DB'
            }`"
            >
              <a-popover trigger="click">
                <template #content>
                  <div class="tips">
                    <div class="tip">
                      <b>{{ $t('ban-zhe-yan-tuo-min-can-shu-ge-shi') }}:</b>
                      {{ $t('36-biao-shi-di-3456-wei-zhe-yan-5-biao-shi-zui-hou-5-wei-zhe-yan') }}
                      <!--                    更多参数格式请参考<a>帮助文档</a>。-->
                    </div>
                  </div>
                </template>
                <a-input
                  v-model="record.ruleExpr"
                  :placeholder="placeholderObj[record.algorithmType]"
                  v-if="record.ruleType !== 'null' && record.ruleType !== 'FULL_MASK'"
                  size="small"
                  :style="`width: 164px;${record.error ? 'border-color: red' : ''}`"
                  @blur="handleInputBlur(record)"
                />
              </a-popover>
              <a-popover trigger="hover" v-if="record.ruleType !== 'null' && record.ruleType !== 'FULL_MASK'">
                <template #content>
                  <div>{{ $t('bian-ji-qian-suan-fa') }}:{{ record.rawRuleExpr }}</div>
                </template>
                <cc-iconfont
                  name="warning-ghost"
                  v-if="record.rawRuleExpr && record.ruleExpr !== record.rawRuleExpr"
                  color="rgba(0, 0, 0, 0.88)"
                  style="margin-left: 6px; margin-top: 5px"
                />
              </a-popover>
              <div v-if="record.error" style="margin-top: 3px; color: red">
                {{ record.errorMsg }}
              </div>
            </div>
          </template>
        </a-table>
        <preview-modal :visible="showPreviewModal" :handle-close-modal="hidePreviewModal" :ruleData="selectedColumn" v-if="showPreviewModal" />
      </div>
    </div>
    <div class="footer">
      <a-button type="primary" style="width: 120px; margin-right: 16px" @click="handleConfirm">
        {{ $t('bao-cun') }}
      </a-button>
      <a-button style="width: 120px">
        <router-link :to="{ name: 'System_Desensitization' }">{{ $t('qu-xiao') }}</router-link>
      </a-button>
    </div>
    <rules-confirm-modal
      :visible="showRulesConfirmModal"
      :rules="rules"
      :types="desensitizeRuleTypeObj"
      :handle-close-modal="hideRulesConfirmModal"
      :datasource="confirmDatasource"
    />
  </div>
</template>

<script>
import * as Vue from 'vue';
import cloneDeep from 'lodash.clonedeep';
import { Modal } from 'ant-design-vue';
import PreviewModal from '@/views/system/desensitization/components/PreviewModal';
import { ALGORITHM_TYPES_PLACEHOLDER, PG_GP } from '@/const';
import RulesConfirmModal from '@/views/system/desensitization/components/RulesConfirmModal';

export default {
  name: 'AddDesensitization',
  components: {
    RulesConfirmModal,
    PreviewModal
  },
  data() {
    return {
      loading: false,
      gridOptions: {
        columns: [
          {
            title: 'name',
            slots: {
              header: 'name_title'
            }
          }
        ]
      },
      preRules: [],
      columnColumns: [
        {
          title: this.$t('lie-ming'),
          ellipsis: true,
          dataIndex: 'name'
        },
        {
          title: this.$t('suan-fa-lei-xing'),
          width: 150,
          scopedSlots: { customRender: 'algorithm_type' }
        },
        {
          title: this.$t('can-shu-zhi'),
          width: 220,
          scopedSlots: { customRender: 'parameter_value' }
        },
        {
          title: this.$t('cao-zuo'),
          scopedSlots: { customRender: 'action' }
        }
      ],
      rawColumnList: [],
      rawColumnListObj: {},
      columnList: [{}],
      selectedTable: {},
      selectedColumn: {},
      showPreviewModal: false,
      showRulesConfirmModal: false,
      addDesensitizeRules: {},
      updateDesensitizeRules: {},
      deleteDesensitizeRules: {},
      desensitizeRuleTypeList: [],
      desensitizeRuleTypeObj: {},
      rules: {},
      confirmDatasource: [],
      currentTableRules: [],
      currentTableRulesObj: {},
      modifiedTableNumObj: {}
    };
  },
  methods: {
    checkRuleExpr() {
      let error = false;

      this.columnList.forEach((column) => {
        if (column.ruleType === 'PART_MASK' && !column.ruleExpr) {
          this.handleInputBlur(column);
          column.error = true;
          error = true;
        }
        if (column.error) {
          error = true;
        }
      });

      // this.columnList = [...this.columnList];

      if (error) {
        Modal.error({
          content: this.$t('dang-qian-cun-zai-fei-fa-de-tuo-min-gui-ze-qing-xian-xiu-gai')
        });
      }

      return error;
    },
    async handleInputBlur(record) {
      const { ruleType, ruleExpr } = record;
      const res = await this.$services.dmDesensitizationRuleDesensitizeRuleTest({
        data: { expr: ruleExpr, ruleType },
        modal: false
      });
      if (res.success) {
        record.error = false;
        record.errorMsg = '';
      } else {
        record.error = true;
        record.errorMsg = res.msg;
      }

      for (let i = 0; i < this.columnList.length; i++) {
        if (this.columnList[i].name === record.name) {
          this.columnList.i = record;
        }
      }
    },
    handleRuleTypeChange(row) {
      this.columnList.forEach((column) => {
        if (column.name === row.name) {
          column.ruleExpr = '';
          column.error = false;
          column.errorMsg = '';
        }
      });

      this.columnList = [...this.columnList];
    },
    handleProcessPathElements(key) {
      const keyArr = key.split('/');
      return {
        confirmName: keyArr.slice(1),
        pathElements: keyArr.splice(2)
      };
    },
    hideRulesConfirmModal() {
      this.showRulesConfirmModal = false;
    },
    handleConfirm() {
      if (!this.checkRuleExpr()) {
        this.diff();
        this.confirmDatasource = [
          ...Object.values(this.addDesensitizeRules),
          ...Object.values(this.updateDesensitizeRules),
          ...Object.values(this.deleteDesensitizeRules)
        ];
        this.rules = {
          adds: this.addDesensitizeRules,
          updates: this.updateDesensitizeRules,
          deletes: this.deleteDesensitizeRules
        };

        this.showRulesConfirmModal = true;
      }
    },
    diff() {
      const len = this.rawColumnList.length;
      let modifiedKey = '';

      for (let i = 0; i < len; i++) {
        for (let j = 0; j < len; j++) {
          const pre = this.rawColumnList[i];
          const current = this.columnList[j];
          if (pre.name === current.name) {
            const { key, tableKey } = pre;
            modifiedKey = tableKey;
            const { id } = this.selectedTable;
            // const key = `${schemaKey}/${tableName}/${current.name}`;
            // console.log(key);
            const pathElements = this.handleProcessPathElements(key);
            const ruleId = this.rawColumnListObj[current.name].id;

            if (pre.ruleType === current.ruleType) {
              if (pre.ruleExpr !== current.ruleExpr && ruleId) {
                const updateRule = {
                  key,
                  tableKey,
                  ...pathElements,
                  ruleExpr: current.ruleExpr,
                  ruleId,
                  ruleType: current.ruleType
                };
                this.updateDesensitizeRules[key] = updateRule;
              }
            } else {
              if (pre.ruleType === 'null' && current.ruleType !== 'null') {
                const addRule = {
                  key,
                  tableKey,
                  ...pathElements,
                  dataSourceId: id,
                  ruleExpr: current.ruleExpr || '',
                  ruleType: current.ruleType
                };
                this.addDesensitizeRules[key] = addRule;
              }
              if (pre.ruleType !== 'null' && current.ruleType === 'null') {
                const deleteRule = {
                  key,
                  tableKey,
                  ...pathElements,
                  ruleId,
                  ruleType: current.ruleType,
                  ruleExpr: current.ruleExpr
                };
                this.deleteDesensitizeRules[key] = deleteRule;
              }
              if (pre.ruleType !== 'null' && current.ruleType !== 'null' && ruleId) {
                const updateRule = {
                  key,
                  tableKey,
                  ...pathElements,
                  ruleId,
                  ruleType: current.ruleType,
                  ruleExpr: current.ruleExpr
                };
                this.updateDesensitizeRules[key] = updateRule;
              }
            }
            break;
          }
        }
      }

      let add = 0;
      let update = 0;
      let deleteN = 0;
      Object.values(this.addDesensitizeRules).forEach((a) => {
        if (a.tableKey === modifiedKey) {
          add++;
        }
      });

      Object.values(this.updateDesensitizeRules).forEach((u) => {
        if (u.tableKey === modifiedKey) {
          update++;
        }
      });

      Object.values(this.deleteDesensitizeRules).forEach((d) => {
        if (d.tableKey === modifiedKey) {
          deleteN++;
        }
      });

      this.addDesensitizeRules = { ...this.addDesensitizeRules };
      this.updateDesensitizeRules = { ...this.updateDesensitizeRules };
      this.deleteDesensitizeRules = { ...this.deleteDesensitizeRules };

      if (modifiedKey) {
        this.modifiedTableNumObj.modifiedKey = {
          adds: add,
          updates: update,
          deletes: deleteN
        };
      }
    },
    async getDesensitizeRuleTypeList() {
      const res = await this.$services.dmConstantListDesensitizeRuleTypes();
      if (res.success) {
        const obj = {};
        res.data.forEach((rule) => {
          obj[rule.ruleType] = rule;
        });
        this.desensitizeRuleTypeObj = obj;
        this.desensitizeRuleTypeList = [
          {
            name: this.$t('wu-0'),
            ruleType: 'null'
          },
          ...res.data
        ];
      }
    },
    hidePreviewModal() {
      this.showPreviewModal = false;
    },
    handlePreview(column) {
      this.selectedColumn = column;
      this.showPreviewModal = true;
    },
    async getColumns(selectedTable) {
      this.loading = true;
      // console.log(selectedTable);
      for (let i = 0; i < this.columnList; i++) {
        if (this.columnList[i].error) {
          return;
        }
      }
      this.diff();
      const { dsEnvName, instanceId, id: dataSourceId, schemaName, tableName, parentData = '', dataSourceType, key: selectTableKey } = selectedTable;

      let catalogName = '';

      let arr = [];
      if (PG_GP.includes(dataSourceType)) {
        arr = selectTableKey.split('/');
        catalogName = arr[2];
      }

      // console.log(arr);

      let currentTableRules = [];
      const currentTableRulesObj = {};

      const data = PG_GP.includes(dataSourceType)
        ? {
            dataSourceId,
            ruleCatalog: catalogName,
            ruleSchema: schemaName,
            ruleTable: tableName
          }
        : {
            dataSourceId,
            ruleSchema: schemaName,
            ruleTable: tableName
          };
      const res1 = await this.$services.dmDesensitizationRuleListTableDesensitizeRules({
        data
      });

      currentTableRules = res1.data;
      res1.data.forEach((rule) => {
        const { dsInstanceId, resourcePath } = rule;
        currentTableRulesObj[`${dsEnvName}/${dsInstanceId}${resourcePath}`] = rule;
      });

      this.currentTableRules = currentTableRules;
      this.currentTableRulesObj = currentTableRulesObj;

      const res2 = await this.$services.dmDataSourceSchemaListColumns({
        data: {
          dataSourceId,
          schemaName,
          tableName,
          parentData,
          useVisibility: true
        }
      });

      if (res2.success) {
        const rawColumnListObj = {};
        const tableKey = PG_GP.includes(dataSourceType)
          ? `${dsEnvName}/${instanceId}/${catalogName}/${schemaName}/${tableName}`
          : `${dsEnvName}/${instanceId}/${schemaName}/${tableName}`;
        res2.data.forEach((column) => {
          const key = `${tableKey}/${column.name}`;
          // console.log(key, this.addDesensitizeRules, this.addDesensitizeRules[key]);
          column.ruleType = 'null';
          column.key = key;
          column.tableKey = tableKey;
          if (currentTableRulesObj[key]) {
            const c = currentTableRulesObj[key];
            column.ruleType = c.ruleType;
            column.rawRuleType = c.ruleType;
            column.ruleExpr = c.ruleExpr;
            column.rawRuleExpr = c.ruleExpr;
            column.id = c.id;
          }
        });
        res2.data.forEach((column) => {
          rawColumnListObj[column.name] = column;
        });
        this.selectedTable = selectedTable;
        this.rawColumnList = cloneDeep(res2.data);
        this.rawColumnListObj = cloneDeep(rawColumnListObj);

        res2.data.forEach((column) => {
          const key = `${tableKey}/${column.name}`;
          if (this.addDesensitizeRules[key]) {
            const c = this.addDesensitizeRules[key];
            column.ruleType = c.ruleType;
            column.ruleExpr = c.ruleExpr;
          }
          if (this.updateDesensitizeRules[key]) {
            const c = this.updateDesensitizeRules[key];
            column.ruleType = c.ruleType;
            column.ruleExpr = c.ruleExpr;
          }
          if (this.deleteDesensitizeRules[key]) {
            const c = this.deleteDesensitizeRules[key];
            column.ruleType = c.ruleType;
            column.ruleExpr = c.ruleExpr;
          }
        });
        this.columnList = res2.data;

        Object.values(this.addDesensitizeRules).forEach((add) => {
          if (add.tableKey === tableKey) {
            delete this.addDesensitizeRules[add.key];
          }
        });

        Object.values(this.updateDesensitizeRules).forEach((update) => {
          if (update.tableKey === tableKey) {
            delete this.updateDesensitizeRules[update.key];
          }
        });

        Object.values(this.deleteDesensitizeRules).forEach((d) => {
          if (d.tableKey === tableKey) {
            delete this.deleteDesensitizeRules[d.key];
          }
        });

        this.addDesensitizeRules = { ...this.addDesensitizeRules };
        this.updateDesensitizeRules = { ...this.updateDesensitizeRules };
        this.deleteDesensitizeRules = { ...this.deleteDesensitizeRules };
      }

      this.loading = false;
    }
  },
  created() {
    this.getDesensitizeRuleTypeList();
    this.placeholderObj = ALGORITHM_TYPES_PLACEHOLDER;
  }
};
</script>

<style scoped lang="less">
.add-desensitization {
  display: flex;
  flex-direction: column;
  height: calc(~'100vh - 100px');

  .tip {
    margin-top: 10px;
    height: 28px;
    width: 100%;
    line-height: 28px;
    background: #f3f3f3;
    padding: 0 8px;
  }

  .content {
    width: 100%;
    display: flex;
    margin-top: 10px;

    .columns {
      flex: 1;
      border: 1px solid rgba(199, 199, 199, 1);
      border-left: none;
      display: flex;
      flex-direction: column;
      height: calc(~'100vh - 188px');
      overflow: scroll;
    }
  }

  .footer {
    margin-top: 10px;
    text-align: center;
  }
}

.tips {
  width: 264px;
}
</style>
