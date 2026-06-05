<template>
  <div class="add-data-rule">
    <a-breadcrumb>
      <a-breadcrumb-item>
        <router-link :to="{ name: 'System_Data_Rules' }">
          {{ $t('shu-ju-chu-li-gui-ze-guan-li') }}
        </router-link>
      </a-breadcrumb-item>
      <a-breadcrumb-item>{{ $t('xin-zeng-gui-ze') }}</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="content">
      <cc-table-tree-select :get-columns="getColumns" :nums="modifiedTableNumObj" :check-errors="checkRuleExpr" />
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
          <template #code="record">
            <div
              :style="`background: ${record.pkgId === rawColumnListObj[record.name].pkgId ? 'transparent' : '#FFF7DB'};
            outline: 6px solid ${record.pkgId === rawColumnListObj[record.name].pkgId ? 'transparent' : '#FFF7DB'}`"
            >
              <a-select v-model="record.pkgId" style="width: 280px" size="small">
                <a-select-option v-for="pkg in dataCodeList" :value="pkg.id" :key="pkg.id">
                  {{ pkg.description }}/{{ pkg.pkgInstanceName }}
                </a-select-option>
              </a-select>
            </div>
          </template>
        </a-table>
      </div>
    </div>
    <div class="footer">
      <a-button type="primary" style="width: 120px; margin-right: 16px" @click="handleConfirm">
        {{ $t('bao-cun') }}
      </a-button>
      <a-button style="width: 120px" @click="handleReturnList">{{ $t('qu-xiao') }}</a-button>
    </div>
    <a-modal :visible="showRulesConfirmModal" :title="$t('shu-ju-chu-li-gui-ze-que-ren')" @cancel="handleCloseModal" :width="800">
      <a-table :columns="confirmColumns" :data-source="rules" size="small">
        <template #name="record">
          {{ record.key }}
        </template>
        <template #pkgName="record">
          {{ getPkgName(record.pkgId) }}
        </template>
      </a-table>
      <div class="footer">
        <a-button :loading="confirmLoading" type="primary" @click="handleConfirmBind">
          {{ $t('que-ding') }}
        </a-button>
        <a-button @click="handleCloseModal">{{ $t('qu-xiao') }}</a-button>
      </div>
    </a-modal>
  </div>
</template>

<script>
import cloneDeep from 'lodash.clonedeep';
import * as Vue from 'vue';
import { PG_GP } from '@/const';

export default {
  name: 'AddDataRule',
  data() {
    return {
      addDataRules: {},
      updateDataRules: {},
      deleteDataRules: {},
      showRulesConfirmModal: false,
      selectedColumn: {},
      confirmLoading: false,
      loading: false,
      columnColumns: [
        {
          title: this.$t('lie-ming'),
          dataIndex: 'name',
          width: 360
        },
        {
          title: this.$t('shu-ju-chu-li-dai-ma-bao-ming-cheng-id'),
          scopedSlots: { customRender: 'code' }
        }
      ],
      columnList: [{}],
      rawColumnList: [],
      rawColumnListObj: {},
      currentTableRules: [],
      currentTableRulesObj: {},
      dataCodeList: [],
      confirmColumns: [
        {
          title: this.$t('shi-li-ming-ku-ming-biao-ming-lie-ming'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: this.$t('shu-ju-chu-li-dai-ma-bao-ming-cheng'),
          scopedSlots: { customRender: 'pkgName' },
          width: 300
        }
      ],
      rules: [{}],
      modifiedTableNumObj: {}
    };
  },
  mounted() {
    this.listPkg();
  },
  computed: {
    getPkgName() {
      return (id) => {
        let name = '';
        this.dataCodeList.map((item) => {
          if (item.id === id) {
            name = `${item.description}/${item.pkgInstanceName}`;
          }
          return null;
        });
        return name;
      };
    }
  },
  methods: {
    async listConfig() {
      const data = this.queryForm;
      const res = await this.$services.dmDataHandleConfigListConfig({ data });
      if (res.success) {
        this.configData = res.data;
      }
    },
    async listPkg() {
      const data = {
        descLike: '',
        startId: 0,
        pageSize: 100
      };
      const res = await this.$services.dmDataHandlePackageListPkg({ data });
      if (res.success) {
        this.dataCodeList = res.data;
      }
    },
    async getColumns(selectedTable) {
      this.loading = true;
      this.diff();
      const { id: dataSourceId, schemaName, tableName, dsEnvName, instanceId, parentData = '', dataSourceType, key: selectTableKey } = selectedTable;
      let catalogName = '';

      let arr = [];
      if (PG_GP.includes(dataSourceType)) {
        arr = selectTableKey.split('/');
        catalogName = arr[2];
      }
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
      const res1 = await this.$services.dmDataHandlePackageListTableDataHandleConfigs({ data });

      if (res1.success) {
        currentTableRules = res1.data;
        res1.data.forEach((column) => {
          const { dsInstanceId, resourcePathList } = column;
          currentTableRulesObj[`${dsEnvName}/${dsInstanceId}/${resourcePathList.join('/')}`] = column;
        });
        this.currentTableRules = currentTableRules;
        this.currentTableRulesObj = currentTableRulesObj;
        const res = await this.$services.dmDataSourceSchemaListColumns({
          data: {
            dataSourceId,
            schemaName,
            tableName,
            parentData,
            useVisibility: true
          }
        });
        if (res.success) {
          const rawColumnListObj = {};
          currentTableRules = res.data;
          const tableKey = PG_GP.includes(dataSourceType)
            ? `${dsEnvName}/${instanceId}/${catalogName}/${schemaName}/${tableName}`
            : `${dsEnvName}/${instanceId}/${schemaName}/${tableName}`;
          res.data.map((column) => {
            const key = PG_GP.includes(dataSourceType)
              ? `${dsEnvName}/${instanceId}/${catalogName}/${schemaName}/${tableName}/${column.name}`
              : `${dsEnvName}/${instanceId}/${schemaName}/${tableName}/${column.name}`;
            column.key = key;
            column.tableKey = tableKey;
            if (currentTableRulesObj[key]) {
              const c = currentTableRulesObj[key];
              column.id = c.id;
              column.pkgId = c.packageId;
              column.configId = c.id;
              column.pkgInstanceName = c.pkgInstanceName;
              column.pkgDesc = c.pkgDesc;
            }
            return null;
          });
          res.data.forEach((column) => {
            rawColumnListObj[column.name] = column;
          });
          this.selectedTable = selectedTable;
          this.rawColumnList = cloneDeep(res.data);
          this.rawColumnListObj = cloneDeep(rawColumnListObj);

          res.data.forEach((column) => {
            const key = `${tableKey}/${column.name}`;
            if (this.addDataRules[key]) {
              const c = this.addDataRules[key];
              column.pkgId = c.pkgId;
            }
            if (this.updateDataRules[key]) {
              const c = this.updateDataRules[key];
              column.pkgId = c.pkgId;
            }
            if (this.deleteDataRules[key]) {
              const c = this.deleteDataRules[key];
              column.pkgId = c.pkgId;
            }
          });
          Object.values(this.addDataRules).forEach((add) => {
            if (add.tableKey === tableKey) {
              delete this.addDataRules[add.key];
            }
          });

          Object.values(this.updateDataRules).forEach((update) => {
            if (update.tableKey === tableKey) {
              delete this.updateDataRules[update.key];
            }
          });

          Object.values(this.deleteDataRules).forEach((d) => {
            if (d.tableKey === tableKey) {
              delete this.deleteDataRules[d.key];
            }
          });

          this.columnList = res.data;

          this.addDataRules = { ...this.addDataRules };
          this.updateDataRules = { ...this.updateDataRules };
          this.deleteDataRules = { ...this.deleteDataRules };
        }
        this.loading = false;
      }
    },
    handleProcessPathElements(key) {
      const keyArr = key.split('/');
      return {
        confirmName: keyArr.slice(1),
        pathElements: keyArr.splice(2)
      };
    },
    handleConfirm() {
      this.diff();
      this.rules = [...Object.values(this.addDataRules), ...Object.values(this.updateDataRules), ...Object.values(this.deleteDataRules)];
      this.showRulesConfirmModal = true;
    },
    handleReturnList() {
      this.$router.push({ name: 'System_Data_Rules' });
    },
    handleCloseModal() {
      this.showRulesConfirmModal = false;
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
            const pathElements = this.handleProcessPathElements(key);
            if (pre.pkgId && current.pkgId && pre.pkgId !== current.pkgId) {
              const updateRule = {
                dataSourceId: id,
                key,
                tableKey,
                pkgId: current.pkgId,
                pkgConfigId: pre.configId,
                pkgInstanceName: current.pkgInstanceName,
                pkgDesc: current.pkgDesc
              };
              this.updateDataRules.key = updateRule;
            } else {
              if (!pre.pkgId && current.pkgId) {
                const addRule = {
                  key,
                  tableKey,
                  dataSourceId: id,
                  ...pathElements,
                  pkgId: current.pkgId,
                  pkgInstanceName: current.pkgInstanceName,
                  pkgDesc: current.pkgDesc
                };
                this.addDataRules.key = addRule;
              }
              if (pre.pkgId && !current.pkgId) {
                const deleteRule = {
                  ...pathElements,
                  key,
                  tableKey,
                  pkgConfigId: pre.configId,
                  pkgId: current.pkgId,
                  dataSourceId: id,
                  pkgInstanceName: current.pkgInstanceName,
                  pkgDesc: current.pkgDesc
                };
                this.deleteDataRules.key = deleteRule;
              }
            }
            break;
          }
        }
        let add = 0;
        let update = 0;
        let deleteN = 0;
        Object.values(this.addDataRules)
          // eslint-disable-next-line no-loop-func
          .forEach((a) => {
            if (a.tableKey === modifiedKey) {
              add++;
            }
          });

        Object.values(this.updateDataRules)
          // eslint-disable-next-line no-loop-func
          .forEach((u) => {
            if (u.tableKey === modifiedKey) {
              update++;
            }
          });

        Object.values(this.deleteDataRules)
          // eslint-disable-next-line no-loop-func
          .forEach((d) => {
            if (d.tableKey === modifiedKey) {
              deleteN++;
            }
          });

        this.addDataRules = { ...this.addDataRules };
        this.updateDataRules = { ...this.updateDataRules };
        this.deleteDataRules = { ...this.deleteDataRules };

        if (modifiedKey) {
          this.modifiedTableNumObj.modifiedKey = {
            adds: add,
            updates: update,
            deletes: deleteN
          };
        }
      }
    },
    async configPackageBind() {
      this.confirmLoading = true;
      const data = {
        addRules: Object.values(this.addDataRules),
        updateRules: Object.values(this.updateDataRules),
        deleteRules: Object.values(this.deleteDataRules)
      };
      const res = await this.$services.dmDataHandlePackageConfigBind({ data });
      console.log(res);
      this.showRulesConfirmModal = false;
      this.confirmLoading = false;
      this.addDataRules = {};
      this.updateDataRules = {};
      this.deleteDataRules = {};
      if (res.success) {
        this.$router.push({ name: 'System_Data_Rules' });
      }
    },
    handleConfirmBind() {
      this.configPackageBind();
    },
    checkRuleExpr() {}
  }
};
</script>

<style scoped lang="less">
.add-data-rule {
  display: flex;
  flex-direction: column;
  height: calc(~'100vh - 100px');

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
    }
  }

  .footer {
    margin-top: 10px;
    text-align: center;
  }
}
</style>
