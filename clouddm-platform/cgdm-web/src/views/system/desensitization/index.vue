<template>
  <div class="desensitization">
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
            v-model="queryForm.dataSourceId"
            style="width: 160px"
            @change="handleInstanceChange"
            :dropdownMatchSelectWidth="false"
          >
            <a-select-option value="" key="allDs">{{ $t('quan-bu') }}</a-select-option>
            <a-select-option v-for="datasource in dsList" :value="datasource.id" :key="datasource.id" :ds-type="datasource.dataSourceType">
              <cc-data-source-icon
                color="#4DBAEE"
                :size="18"
                :type="datasource.dataSourceType"
                :instanceType="datasource.deployEnvType"
              ></cc-data-source-icon>
              {{ datasource.instanceDesc }}
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
          <a-button class="search-btn" type="primary" @click="handleQuery">
            {{ $t('cha-xun') }}
          </a-button>
        </div>
      </div>
      <div class="op">
        <a-button icon="plus" style="margin-right: 10px" @click="goAddDesensitizationPage">
          {{ $t('bian-ji-tuo-min-ce-lve') }}
        </a-button>
        <a-button @click="handleRefresh">
          <CustomIcon type="icon-v2-Refresh" />
        </a-button>
      </div>
    </div>
    <div class="table-container">
      <a-table :columns="columns" size="small" :data-source="showRuleList" :row-key="(record) => record.id" :pagination="false">
        <!--        <template #buildOptionText="props">-->
        <!--          <span>{{ props.value }}条/页</span>-->
        <!--        </template>-->
        <template #ruleType="record">
          {{ desensitizeRuleTypeObj[record.ruleType] && desensitizeRuleTypeObj[record.ruleType].name }}
        </template>
        <template #instanceName="record">
          <cc-data-source-icon :instanceType="getEnvType(record)" :size="18" :type="getDsType(record)" color="#4DBAEE" />
          {{ record.dsInstanceId }}/ {{ record.dsDesc }}
        </template>
        <template #status="record">
          <cc-status :type="record.disable ? 'error' : 'success'" :content="record.disable ? '停用' : '正常'" />
        </template>
        <template #action="record">
          <a-button type="link" @click="handleActions('disable', record)" v-if="record.disable">
            {{ $t('qi-yong') }}
          </a-button>
          <a-popconfirm
            cancel-text="取消"
            ok-text="确定"
            :title="$t('que-ding-ting-yong-tuo-min-gui-ze-ma')"
            @confirm="handleActions('disable', record)"
            v-else
          >
            <a-button type="link">{{ $t('ting-yong') }}</a-button>
          </a-popconfirm>
          <a-button type="link" @click="handleActions('showModal', record)">
            {{ $t('bian-ji') }}
          </a-button>
          <a-popconfirm
            :cancel-text="$t('qu-xiao')"
            :ok-text="$t('que-ding')"
            :title="$t('que-ding-shan-chu-tuo-min-gui-ze-ma')"
            @confirm="handleActions('delete', record)"
          >
            <a-button type="link">{{ $t('shan-chu') }}</a-button>
          </a-popconfirm>
        </template>
      </a-table>
      <a-pagination
        v-model="currentPage"
        :page-size="pageSize"
        :total="ruleList.length"
        size="small"
        @change="handlePageChange"
        style="margin-top: 10px"
      />
    </div>
    <edit-rule-modal
      :visible="showEditRuleModal"
      :rulesObj="desensitizeRuleTypeObj"
      :pre-rule="selectedRule"
      :handle-close-modal="hideEditRuleModal"
      v-if="showEditRuleModal"
    />
  </div>
</template>

<script>
import * as Vue from 'vue';
import EditRuleModal from '@/views/system/desensitization/components/EditRuleModal';
import { hasSchema } from '@/utils';

export default {
  name: 'Desensitization',
  components: { EditRuleModal },
  data() {
    return {
      hasSchema,
      pageLevel: [],
      currentPage: 1,
      showPageNumber: 5,
      pathElements: [
        {
          i18nName: this.$t('ku'),
          paramName: 'ruleSchema',
          element: 'SCHEMA'
        },
        {
          i18nName: this.$t('biao'),
          paramName: 'ruleTable',
          element: 'TABLE'
        },
        {
          i18nName: this.$t('lie'),
          paramName: 'ruleColumn',
          element: 'COLUMN'
        }
      ],
      dsTypes: [],
      dsList: [],
      queryForm: {
        dataSourceType: 'MySQL',
        dataSourceId: '',
        names: {
          ruleSchema: '',
          ruleTable: '',
          ruleColumn: ''
        }
      },
      query: {},
      columns: [
        {
          title: this.$t('shi-li-ming-0'),
          scopedSlots: { customRender: 'instanceName' }
        },
        {
          title: this.$t('ku-ming'),
          dataIndex: 'dbName'
        },
        {
          title: 'schema/sid',
          dataIndex: 'schemaName'
        },
        {
          title: this.$t('biao-ming'),
          dataIndex: 'tableName'
        },
        {
          title: this.$t('lie-ming'),
          dataIndex: 'columnName'
        },
        {
          title: this.$t('tuo-min-suan-fa'),
          scopedSlots: { customRender: 'ruleType' }
        },
        {
          title: this.$t('can-shu-zhi'),
          dataIndex: 'ruleExpr'
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
      showEditRuleModal: false,
      desensitizeRuleTypeList: [],
      desensitizeRuleTypeObj: {},
      ruleList: [],
      showRuleList: [{}],
      pageSize: 20,
      startId: 0,
      selectedRule: {}
    };
  },
  computed: {
    getDsType() {
      return (record) => {
        let type = '';
        this.dsList.map((ds) => {
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
        this.dsList.map((ds) => {
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
    handleShowSizeChange(current, size) {
      this.currentPage = current;
      this.pageSize = size;
      this.sliceData();
    },
    sliceData() {
      this.showRuleList = this.ruleList.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize);
    },
    handlePageChange(page, pageSize) {
      this.currentPage = page;
      this.pageSize = pageSize;
      this.sliceData();
      if (this.ruleList.length / this.pageSize - page < 3) {
        this.getDesensitizeList();
      }
    },
    async getPageElementsLevelList() {
      const res = await this.$services.dmConstantListPageElementsLevel();
      if (res.success) {
        this.pageLevel = res.data;
      }
    },
    handleQuery() {
      this.handleRefresh();
    },
    hideEditRuleModal() {
      this.showEditRuleModal = false;
    },
    async getAllDsType() {
      const res = await this.$services.dmConstantListDsTypesWithoutEnv();
      if (res.success) {
        this.dsTypes = res.data;
      }
    },
    async getAllDsList() {
      const res = await this.$services.rdpDataSourceListByCondition({
        data: { dataSourceType: this.queryForm.dataSourceType, useVisibility: true }
      });

      if (res.success) {
        this.dsList = res.data;
      }
    },
    handleInstanceChange(value, option) {
      this.handleDsTypeChange(option.data.attrs['ds-type']);
    },
    async handleDsTypeChange(value) {
      this.queryForm.dataSourceType = value;
      if (value) {
        const res = await this.$services.dmConstantListDsDesensitizePathElements({
          data: { dataSourceType: value }
        });

        if (res.success) {
          const pathElements = res.data;
          const names = {};
          pathElements.forEach((path) => {
            names[path.paramName] = '';
          });

          this.pathElements = pathElements;
          this.queryForm.names = names;
        }
      }
      await this.handleRefresh();
      await this.getAllDsList();
    },
    async handleActions(type, record) {
      this.selectedRule = record;
      const { id: ruleId, disable } = record;
      let res = {};
      switch (type) {
        case 'disable':
          res = await this.$services.dmDesensitizationRuleUpdateDesensitizeRuleAbility({
            data: {
              ruleId,
              disable: !disable
            },
            msg: `${disable ? this.$t('qi-yong') : this.$t('ting-yong')}成功`
          });
          break;
        case 'showModal':
          this.showEditRuleModal = true;
          break;
        case 'edit':
          break;
        case 'delete':
          res = await this.$services.dmDesensitizationRuleDeleteDesensitizeRule({
            data: {
              ruleId
            },
            msg: this.$t('shan-chu-cheng-gong')
          });
          break;
        default:
          break;
      }

      if (res.success && ['disable', 'delete'].includes(type)) {
        this.startId = 0;
        this.handleRefresh();
      }
    },
    goAddDesensitizationPage() {
      this.$router.push({ name: 'System_Desensitization_Add' });
    },
    async getDesensitizeRuleTypeList() {
      const res = await this.$services.dmConstantListDesensitizeRuleTypes();
      const obj = {};
      if (res.success) {
        res.data.forEach((type) => {
          obj[type.ruleType] = type;
        });
        this.desensitizeRuleTypeList = res.data;
        this.desensitizeRuleTypeObj = obj;
      }
    },
    handleRefresh() {
      this.ruleList = [];
      this.currentPage = 1;
      this.queryForm.pageSize = this.pageSize * this.showPageNumber;
      this.queryForm.startId = 0;
      this.getDesensitizeList();
    },
    async getDesensitizeList() {
      const { dataSourceId, dataSourceType, names } = this.queryForm;
      const res = await this.$services.dmDesensitizationRuleListDesensitizeRules({
        data: {
          dataSourceId,
          dataSourceType,
          ...names,
          pageSize: this.pageSize * this.showPageNumber,
          startId: this.ruleList.length ? this.ruleList[this.ruleList.length - 1].id : 0
        }
      });
      if (res.success) {
        res.data.forEach((rule) => {
          const { resourcePath } = rule;
          const arr = resourcePath.split('/');
          const len = arr.length;
          rule.dbName = arr[1];
          rule.tableName = arr[len - 2];
          rule.columnName = arr[len - 1];
          if (arr.length === 5) {
            rule.schemaName = arr[2];
          }
        });

        this.ruleList = [...this.ruleList, ...res.data];
        this.sliceData();
      }
    }
  },
  created() {
    this.getDesensitizeRuleTypeList();
    this.getDesensitizeList();
    this.getAllDsList();
    this.getAllDsType();
    this.getPageElementsLevelList();
  }
};
</script>

<style scoped lang="less">
.desensitization {
}
</style>
