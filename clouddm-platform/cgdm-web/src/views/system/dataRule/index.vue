<template>
  <div class="data-rule">
    <div class="query-header">
      <div class="query">
        <a-select
          v-model="queryForm.dataSourceType"
          :dropdownMatchSelectWidth="false"
          @change="handleDsTypeChange"
          style="margin-right: 10px; width: 120px"
        >
          <a-select-option v-for="t in dsTypes" :value="t.type" :key="t.type">
            {{ t && t.name }}
          </a-select-option>
        </a-select>
        <div class="section">
          <div class="label">{{ $t('shi-li-ming-0') }}</div>
          <a-select
            :filter-option="filterOption"
            show-search
            style="width: 160px"
            v-model="queryForm.dataSourceId"
            filterable
            :placeholder="$t('qing-xuan-ze-shu-ju-yuan-shi-li')"
            :dropdownMatchSelectWidth="false"
          >
            <a-select-option value="">
              {{ $t('quan-bu') }}
            </a-select-option>
            <a-select-option v-for="ds in allDsList" :value="ds.id" :key="ds.id">
              <cc-data-source-icon color="#4DBAEE" :size="18" :type="ds.dataSourceType" :instanceType="ds.deployEnvType"></cc-data-source-icon>
              {{ ds.instanceDesc ? ds.instanceDesc : ds.instanceId }}
            </a-select-option>
          </a-select>
        </div>
        <div class="section">
          <div class="label">{{ $t('ku-ming') }}</div>
          <a-input v-if="hasSchema(queryForm.dataSourceType)" v-model="queryForm.ruleCatalog" style="width: 120px; margin-right: 4px" />
          <a-input v-if="!hasSchema(queryForm.dataSourceType)" v-model="queryForm.ruleSchema" style="width: 120px; margin-right: 4px" />
        </div>
        <div v-if="hasSchema(queryForm.dataSourceType)" class="section">
          <div class="label">SCHEMA</div>
          <a-input v-model="queryForm.ruleSchema" style="width: 120px" />
        </div>
        <div class="section">
          <div class="label">{{ $t('biao-ming') }}</div>
          <a-input v-model="queryForm.ruleTable" style="width: 120px" />
        </div>
        <div class="section">
          <div class="label">{{ $t('lie-ming') }}</div>
          <a-input v-model="queryForm.ruleColumn" style="width: 120px" />
          <a-button class="search-btn" type="primary" @click="handleRefresh">
            {{ $t('cha-xun') }}
          </a-button>
        </div>
      </div>
      <div class="op">
        <a-button icon="plus" style="margin-right: 10px" @click="goAddDataRule">
          {{ $t('bian-ji-gui-ze') }}
        </a-button>
        <a-button @click="handleRefresh">
          <CustomIcon type="icon-v2-Refresh" />
        </a-button>
      </div>
    </div>
    <div class="table-container">
      <a-table :columns="columns" :data-source="showConfigData" size="small" :row-key="(record) => record.id" :pagination="false">
        <template #dsInstanceId="record">
          <cc-data-source-icon :instanceType="getEnvType(record)" :size="18" :type="getDsType(record)" color="#4DBAEE" />
          {{ record.dsInstanceId }}/ {{ record.dsDesc }}
        </template>
        <template #database="record">
          {{ record.resourcePathList[0] }}
        </template>
        <template #schema="record">
          {{ hasSchema(getDsType(record)) ? record.resourcePathList[1] : '' }}
        </template>
        <template #table="record">
          {{ hasSchema(getDsType(record)) ? record.resourcePathList[2] : record.resourcePathList[1] }}
        </template>
        <template #columnName="record">
          {{ hasSchema(getDsType(record)) ? record.resourcePathList[3] : record.resourcePathList[2] }}
        </template>
        <template #pkgId="record">{{ record.pkgDesc }}/{{ record.pkgInstanceName }}</template>
        <template #status="record">
          <cc-status :content="record.disable ? $t('ting-yong') : $t('qi-yong')" :type="record.disable ? 'error' : 'success'" />
        </template>
        <template #action="record">
          <a-button type="link" @click="handleUpdateStatus(record)">
            {{ record.disable ? $t('qi-yong') : $t('ting-yong') }}
          </a-button>
          <a-button type="link" @click="handleEditDataRule(record)">{{ $t('bian-ji') }}</a-button>
          <a-popconfirm
            :cancel-text="$t('qu-xiao')"
            :ok-text="$t('que-ren')"
            :title="$t('que-ding-shan-chu-gai-gui-ze-ma')"
            @confirm="handleDeleteDataRule(record)"
          >
            <a-button type="link">{{ $t('shan-chu') }}</a-button>
          </a-popconfirm>
        </template>
      </a-table>
      <a-pagination
        v-model="currentPage"
        :page-size="pageSize"
        :page-size-options="pageLevel"
        :total="configData.length"
        size="small"
        @change="handlePageChange"
        style="margin-top: 10px"
      />
    </div>
    <a-modal
      v-model="showEditDataRule"
      :mask-closable="false"
      :width="400"
      :cancelText="$t('qu-xiao')"
      :okText="$t('bao-cun')"
      :title="$t('shu-ju-chu-li-gui-ze-bian-ji')"
      wrapClassName="have-footer"
      @ok="handleUpdateDataRule(currentRecord)"
    >
      <div>
        <p class="data-rule-path">
          {{ currentRecord.resourcePathList ? currentRecord.resourcePathList.join('/') : '' }}
        </p>
        <div>
          <span style="margin-right: 8px">{{ $t('shu-ju-chu-li-dai-ma-bao-ming-cheng-id') }}</span>
          <a-select style="width: 200px" @change="handleChangePkg" v-model="currentRecord.packageId">
            <a-select-option v-for="pkg in dataCodeList" :value="pkg.id" :key="pkg.id">
              {{ pkg.description }}/{{ pkg.pkgInstanceName }}
            </a-select-option>
          </a-select>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import deepClone from 'lodash.clonedeep';
import { hasSchema } from '@/utils';
import * as Vue from 'vue';

export default {
  name: 'DataRule',
  data() {
    return {
      hasSchema,
      queryForm: {
        dataSourceId: '',
        ruleCatalog: '',
        ruleSchema: '',
        ruleTable: '',
        ruleColumn: '',
        startId: 0,
        pageSize: 1000,
        dataSourceType: 'MySQL'
      },
      query: {},
      currentRecord: {},
      showEditDataRule: false,
      dataCodeList: [],
      allDsList: [],
      columns: [
        {
          title: this.$t('shi-li-ming-0'),
          scopedSlots: { customRender: 'dsInstanceId' }
        },
        {
          title: this.$t('ku-ming'),
          scopedSlots: { customRender: 'database' }
        },
        {
          title: 'schema',
          scopedSlots: { customRender: 'schema' }
        },
        {
          title: this.$t('biao-ming'),
          scopedSlots: { customRender: 'table' }
        },
        {
          title: this.$t('lie-ming'),
          scopedSlots: { customRender: 'columnName' }
        },
        {
          title: this.$t('shu-ju-chu-li-dai-ma-bao'),
          scopedSlots: { customRender: 'pkgId' }
        },
        {
          title: this.$t('zhuang-tai'),
          scopedSlots: { customRender: 'status' }
        },
        {
          title: this.$t('cao-zuo'),
          scopedSlots: { customRender: 'action' }
        }
      ],
      configData: [],
      showConfigData: [{}],
      pageLevel: [],
      currentPage: 1,
      showPageNumber: 5,
      pageSize: 20,
      startId: 0,
      dsTypes: []
    };
  },
  mounted() {
    this.getAllDsType();
    this.handleRefresh();
    this.listPkg();
    this.getAllDs();
  },
  computed: {
    getDsType() {
      return (record) => {
        let type = '';
        this.allDsList.map((ds) => {
          if (ds.instanceId === record.dsInstanceId) {
            type = ds.dataSourceType;
          }
          return null;
        });
        return type;
      };
    },
    getEnvType() {
      return (record) => {
        let type = '';
        this.allDsList.map((ds) => {
          if (ds.instanceId === record.dsInstanceId) {
            type = ds.deployEnvType;
          }
          return null;
        });
        return type;
      };
    }
  },
  methods: {
    filterOption(input, option) {
      return option.componentOptions.children[1] && option.componentOptions.children[1].text.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    async getAllDsType() {
      const res = await this.$services.dmConstantListDsTypesWithoutEnv();
      if (res.success) {
        this.dsTypes = res.data;
      }
    },
    async handleDsTypeChange(value) {
      this.queryForm.dataSourceType = value;
      await this.handleRefresh();
      await this.listAllDs();
    },
    getAllDs() {
      this.listAllDs();
    },
    async listAllDs() {
      const res = await this.$services.rdpDataSourceListByCondition({
        data: {
          lifeCycleState: 'CREATED',
          dataSourceType: this.queryForm.dataSourceType,
          useVisibility: true
        }
      });
      if (res.success) {
        this.allDsList = res.data;
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
    goAddDataRule() {
      this.$router.push({
        name: 'System_Data_Rules_Add'
      });
    },
    async handleRefresh() {
      this.configData = [];
      const data = this.queryForm;
      this.currentPage = 1;
      data.pageSize = this.pageSize * this.showPageNumber;
      data.startId = 0;
      const res = await this.$services.dmDataHandlePackageListConfig({ data });
      if (res.success) {
        this.configData = [...this.configData, ...res.data];
        this.sliceData();
      }
    },
    async listConfig() {
      const data = this.queryForm;
      data.pageSize = this.pageSize * this.showPageNumber;
      data.startId = this.configData.length ? this.configData[this.configData.length - 1].id : 0;
      const res = await this.$services.dmDataHandlePackageListConfig({ data });
      if (res.success) {
        this.configData = [...this.configData, ...res.data];
        this.sliceData();
      }
    },
    sliceData() {
      this.showConfigData = this.configData.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
    handleDeleteDataRule(record) {
      this.deleteRule(record);
    },
    handleUpdateStatus(record) {
      this.availableSwitch(record);
    },
    async availableSwitch(record) {
      const data = {
        configId: record.id,
        disable: !record.disable
      };
      const res = await this.$services.dmDataHandlePackageSwitch({ data });
      if (res.success) {
        this.handleRefresh();
      }
    },
    async updateRule(record) {
      const data = {
        pkgConfigId: record.id,
        pkgId: this.currentRecord.packageId,
        dataSourceId: record.datasourceId,
        pkgInstanceName: this.currentRecord.pkgInstanceName,
        pkgDesc: this.currentRecord.pkgDesc
      };
      const res = await this.$services.dmDataHandlePackageUpdateRule({ data });
      if (res.success) {
        this.showEditDataRule = false;
        this.handleRefresh();
      }
    },
    handleUpdateDataRule(record) {
      this.updateRule(record);
    },
    async deleteRule(record) {
      const data = {
        pkgConfigId: record.id,
        dataSourceId: record.datasourceId
      };
      const res = await this.$services.dmDataHandlePackageDeleteRule({ data });
      if (res.success) {
        this.handleRefresh();
      }
    },
    handleEditDataRule(record) {
      this.currentRecord = deepClone(record);
      this.showEditDataRule = true;
    },
    handleChangePkg(data) {
      this.dataCodeList.map((item) => {
        if (item.id === data) {
          this.currentRecord.pkgInstanceName = item.pkgInstanceName;
          this.currentRecord.pkgDesc = item.description;
        }
        return null;
      });
    },
    handlePageChange(page, pageSize) {
      this.currentPage = page;
      this.pageSize = pageSize;
      this.sliceData();
      if (this.configData.length / this.pageSize - page < 3) {
        this.listConfig();
      }
    }
  }
};
</script>

<style lang="less">
.data-rule-path {
  font-family: PingFangSC-Semibold, serif;
  font-weight: 500;
  margin-bottom: 12px;
}
</style>
