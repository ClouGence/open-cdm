<template>
  <div class="operation-log">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option border-radius-card">
            <div class="left" style="align-items: center">
              <Select
                v-if="!$route.meta.managementTab"
                v-model="auditLogType"
                style="width: 120px; margin-right: 10px"
                @on-change="handleChangeAuditLogType"
              >
                <Option value="operation" :label="$t('cao-zuo-shen-ji')">
                  <span>{{ $t('cao-zuo-shen-ji') }}</span>
                </Option>
                <Option value="sql" :label="$t('sql-shen-ji')">
                  <span>{{ $t('sql-shen-ji') }}</span>
                </Option>
              </Select>
              <span class="log-time-range-label">{{ $t('cao-zuo-shi-jian') }}</span>
              <a-range-picker
                v-model:value="timeRange"
                show-time
                format="YYYY-MM-DD HH:mm"
                :placeholder="[$t('kai-shi-shi-jian'), $t('jie-shu-shi-jian')]"
                class="log-time-range"
              />
              <Select v-model="searchType" style="width: 100px; margin-right: 10px" @on-change="handleChangeSearchType">
                <Option value="user" :label="$t('cao-zuo-ren')">
                  <span>{{ $t('cao-zuo-ren') }}</span>
                </Option>
                <Option value="resourceType" :label="$t('zi-yuan-lei-xing')">
                  <span>{{ $t('zi-yuan-lei-xing') }}</span>
                </Option>
                <Option value="auditType" :label="$t('cao-zuo-dong-zuo')">
                  <span>{{ $t('cao-zuo-dong-zuo') }}</span>
                </Option>
                <Option value="uid" label="uid">
                  <span>uid</span>
                </Option>
              </Select>
              <Input v-if="searchType === 'user'" v-model="searchData.userNameLike" @on-keydown="handleEnterSearch" style="width: 250px" clearable />
              <Input v-if="searchType === 'uid'" v-model="searchData.uid" @on-keydown="handleEnterSearch" style="width: 250px" clearable />
              <Select v-if="searchType === 'resourceType'" v-model="searchData.resourceType" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in resourceTypeList" :value="item.resourceType" :key="item.resourceType">
                  {{ item.alias }}
                </Option>
              </Select>
              <Select v-if="searchType === 'auditType'" v-model="searchData.auditType" filterable style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in auditTypeList" :value="item.auditType" :key="item.auditType">
                  {{ item.alias }}
                </Option>
              </Select>
              <Button type="primary" ghost @click="handleRefresh" :loading="refreshLoading" style="margin-left: 10px">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Tooltip transfer :content="$t('dao-chu')" placement="bottom">
                <Button type="default" style="margin-right: 6px" @click="handleExport">
                  <CustomIcon type="icon-v2-daochu" />
                </Button>
              </Tooltip>
            </div>
          </div>
          <div class="table-container audit-log-table">
            <Table size="small" border :columns="logColumn" :data="logData" :loading="refreshLoading" :scroll="tableScroll">
              <template #resourceValue="{ row }">
                <p v-if="row.resourceType !== 'PURE_URL'">
                  {{ row.resourceVO && row.resourceVO.resourceFlag }}
                </p>
                <p v-if="row.resourceType === 'PURE_URL'">
                  {{ row.operationUri || row.resourceValue }}
                </p>
              </template>
              <template #operator="{ row }">
                <div class="operator-cell">
                  <div>{{ row.userName }}</div>
                  <div class="operator-uid">{{ formatUid(row.uid) }}</div>
                </div>
              </template>
              <template #detail="{ row }">
                <div>
                  <a v-if="row.isExistsLog" @click="handleGetAuditDetail(row)">
                    {{ $t('cha-kan') }}
                  </a>
                  <a disabled v-if="!row.isExistsLog">{{ $t('cha-kan') }}</a>
                </div>
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
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
          :model-value="page"
        />
      </div>
    </div>
    <CCModal v-model="showAuditDetail" :title="$t('cha-kan-ri-zhi')" width="1200px">
      <div>
        <div class="log-content">
          <p class="log-content-desc">
            <span>{{ $t('miao-shu-0') }}</span>
            <span class="point-content">{{ auditLogDetail.desc }}</span>
          </p>
          <p class="log-content-desc">
            <span>{{ $t('lu-jing-0') }}</span>
            <span class="point-content">{{ auditLogDetail.path }}</span>
          </p>
          <div class="warn-text" v-if="isParseError">
            <CustomIcon type="icon-v2-WarnColorful" rightMargin />
            <div>{{ $t('dang-qian-can-shu-guo-da-jian-yi-fu-zhi-hou-zi-hang-cha-kan') }}</div>
          </div>
          <div class="detail">
            <div style="padding-bottom: 20px" v-if="auditLogDetail.content">
              <pre>{{ getLogDetail(auditLogDetail.content) }}</pre>
            </div>
            <div v-if="!auditLogDetail.content && selectedRow.uuidKey">
              <p>{{ $t('ri-zhi-yi-gui-dang-qing-zhi-fu-wu-qi-cha-kan') }}</p>
              <pre>{{ JSON.parse(selectedRow.uuidKey) }}</pre>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div>
          <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
          <Button v-if="isParseError" @click="copyText(auditLogDetail.content, $t('fu-zhi-can-shu-cheng-gong'))" type="primary">
            {{ $t('fu-zhi-can-shu') }}
          </Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showExport" :title="$t('dao-chu-cao-zuo-shen-ji')" width="850px">
      <div>
        <div>
          <Form :label-width="100">
            <FormItem :label="$t('shai-xuan-tiao-jian')">
              <div class="export-filter-summary">{{ exportFilterSummary }}</div>
            </FormItem>
            <FormItem :label="$t('dao-chu-tiao-shu')">
              <div class="export-row-count">
                <RadioGroup v-model="exportForm.rowMode" type="button" class="export-radio-group" :disabled="exportLoading">
                  <Radio :label="'all'" :disabled="exportLoading">{{ $t('quan-bu') }}</Radio>
                  <Radio :label="'part'" :disabled="exportLoading">{{ $t('bu-fen') }}</Radio>
                </RadioGroup>
                <Input
                  v-if="exportForm.rowMode === 'part'"
                  v-model="exportForm.maxRows"
                  type="number"
                  :disabled="exportLoading"
                  class="export-count-input"
                  clearable
                  :placeholder="$t('qing-shu-ru-dao-chu-tiao-shu')"
                />
              </div>
            </FormItem>
            <FormItem :label="$t('dao-chu-ge-shi')">
              <RadioGroup v-model="exportForm.formatName" type="button" class="export-radio-group" :disabled="exportLoading">
                <Radio v-for="item in exportTypes" :label="item.name" :key="item.name" :disabled="exportLoading">
                  {{ item.description || item.name }}
                </Radio>
              </RadioGroup>
            </FormItem>
            <FormItem v-if="exportLoading">
              <Tooltip transfer :content="exportProgressTooltip" placement="top">
                <div style="width: 266px">
                  <Progress v-if="exportProgress.stage === 'PREPARING'" :percent="100" status="active" hide-info />
                  <Progress v-else type="circle" :percent="exportProgress.percent" :width="46" />
                </div>
              </Tooltip>
            </FormItem>
          </Form>
        </div>
      </div>
      <template #footer>
        <div>
          <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
          <Button :loading="exportLoading" :disabled="exportLoading" type="primary" @click="handleConfirmExport">
            {{ exportButtonText }}
          </Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import appLogger from '@/utils/logger';
import Mapping from '@/views/util';
import { mapState } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
import dayjs from '@/utils/dayjsSetup';
import { formatTime, toUtcISOString } from '@/utils';

export default {
  name: 'OperationLog',
  mixins: [copyMixin],
  data() {
    return {
      resourceType: Mapping.resourceType,
      auditLogType: 'operation',
      searchType: 'user',
      refreshLoading: false,
      showAuditDetail: false,
      showExport: false,
      exportLoading: false,
      page: 1,
      total: 0,
      timeRange: [dayjs().subtract(1, 'day'), dayjs()],
      searchData: {
        uid: '',
        userName: '',
        opStart: '',
        opEnd: '',
        // securityLevel:'',
        pageData: {
          pageNumber: 1,
          pageSize: 20
        }
      },
      logColumn: [
        {
          title: this.$t('cao-zuo-zhe'),
          slot: 'operator',
          width: 160
        },
        {
          title: this.$t('cao-zuo-shi-jian'),
          key: 'operateDate',
          width: 170,
          render: (h, params) => h('div', {}, formatTime(params.row.operateDate, 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('zi-yuan-lei-xing'),
          key: 'resourceTypeDesc',
          width: 120
        },
        {
          title: this.$t('cao-zuo-dong-zuo'),
          key: 'auditTypeDesc',
          width: 140
        },
        {
          title: this.$t('cao-zuo-zi-yuan'),
          slot: 'resourceValue',
          width: 220
        },
        {
          title: this.$t('cao-zuo-di-zhi'),
          key: 'sourceIp',
          width: 140
        },
        {
          title: this.$t('ri-zhi-di-zhi'),
          key: 'logPathWorkerIp',
          width: 140
        },
        {
          title: this.$t('an-quan-deng-ji'),
          key: 'securityLevel',
          width: 110,
          render: (h, params) =>
            h(
              'div',
              {
                style: {
                  color: params.row.securityLevel === 'NORMAL' ? '#19be6b' : params.row.securityLevel === 'HIGH' ? '#ed4014' : ''
                }
              },
              params.row.securityLevel === 'NORMAL' ? this.$t('pu-tong') : params.row.securityLevel === 'HIGH' ? this.$t('gao-feng-xian') : ''
            ),
          filterMultiple: false,
          filters: [
            {
              label: this.$t('pu-tong'),
              value: 'NORMAL'
            },
            {
              label: this.$t('gao-feng-xian'),
              value: 'HIGH'
            }
          ],
          filterRemote(value) {
            this.searchData.securityLevel = value[0];
            this.handleRefresh();
          }
        },
        {
          title: this.$t('ri-zhi-wei-yi-xin-xi'),
          key: 'uuidKey',
          width: 320
        }
      ],
      logData: [],
      auditTypeList: [],
      resourceTypeList: [],
      auditLogDetail: {},
      selectedRow: {},
      exportForm: {
        rowMode: 'all',
        maxRows: '',
        formatName: ''
      },
      exportProgress: {
        exportId: '',
        stage: '',
        preparedRows: 0,
        percent: 0
      },
      exportPageSize: 1000,
      isParseError: false
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    tableScroll() {
      const scrollX = this.logColumn.reduce((sum, column) => {
        return sum + (column.width || column.minWidth || 0);
      }, 0);
      return { x: scrollX };
    },
    exportTypes() {
      return this.dmGlobalSetting?.fmtConvertDef || [];
    },
    exportFilterSummary() {
      return `${this.$t('cao-zuo-shi-jian')}: ${this.exportTimeRangeText} / ${this.exportSearchTypeText}: ${this.exportSearchValueText}`;
    },
    exportTimeRangeText() {
      if (!this.timeRange || this.timeRange.length === 0 || !this.timeRange[0] || !this.timeRange[1]) {
        return this.$t('quan-bu');
      }
      return `${dayjs(this.timeRange[0]).format('YYYY-MM-DD HH:mm')} - ${dayjs(this.timeRange[1]).format('YYYY-MM-DD HH:mm')}`;
    },
    exportSearchTypeText() {
      const searchTypeMap = {
        user: this.$t('cao-zuo-ren'),
        resourceType: this.$t('zi-yuan-lei-xing'),
        auditType: this.$t('cao-zuo-dong-zuo'),
        uid: 'uid'
      };
      return searchTypeMap[this.searchType] || this.searchType;
    },
    exportSearchValueText() {
      if (this.searchType === 'user') {
        return this.searchData.userNameLike || this.$t('quan-bu');
      }
      if (this.searchType === 'uid') {
        return this.searchData.uid || this.$t('quan-bu');
      }
      if (this.searchType === 'resourceType') {
        return this.getResourceTypeI18n(this.searchData.resourceType) || this.$t('quan-bu');
      }
      if (this.searchType === 'auditType') {
        return this.getAuditTypeI18n(this.searchData.auditType) || this.$t('quan-bu');
      }
      return this.$t('quan-bu');
    },
    exportProgressTooltip() {
      if (this.exportProgress.stage === 'PREPARING') {
        return this.$t('yi-zhun-bei-x-tiao-shu-ju', [this.exportProgress.preparedRows || 0]);
      }
      return `${this.exportProgress.percent || 0}%`;
    },
    exportButtonText() {
      if (this.exportProgress.stage === 'PREPARING') {
        return this.$t('zheng-zai-zhun-bei-shu-ju');
      }
      if (this.exportProgress.stage === 'CONVERTING') {
        return this.$t('zheng-zai-zhuan-huan-wen-jian');
      }
      return this.$t('dao-chu');
    },
    pageSize() {
      return this.searchData.pageData.pageSize;
    }
  },
  created() {
    this.rdpQueryOperationListCondition();
  },
  mounted() {
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, this.handleOpAuditExportEvent);
    this.handleSearch();
  },
  beforeUnmount() {
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_EXPORT_EVENT, this.handleOpAuditExportEvent);
  },
  methods: {
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.handleRefresh();
      }
    },

    handleChangeAuditLogType(value) {
      if (value === 'sql') {
        this.$router.push('/manager/logs/sql');
        return;
      }
      this.auditLogType = 'operation';
    },

    getLogDetail(detail) {
      try {
        const res = JSON.parse(detail);
        this.isParseError = false;
        return res;
      } catch (e) {
        this.isParseError = true;
        return detail;
      }
    },
    getResourceTypeI18n(type) {
      let alias = '';
      this.resourceTypeList.forEach((resource) => {
        if (type === resource.resourceType) {
          alias = resource.alias;
        }
      });
      return alias;
    },
    getAuditTypeI18n(type) {
      let alias = '';
      this.auditTypeList.forEach((audit) => {
        if (type === audit.auditType) {
          alias = audit.alias;
        }
      });
      return alias;
    },
    handleExport() {
      this.ensureDefaultExportFormat();
      this.exportProgress = {
        exportId: '',
        stage: '',
        preparedRows: 0,
        percent: 0
      };
      this.showExport = true;
    },
    ensureDefaultExportFormat() {
      if (!this.exportForm.formatName && this.exportTypes.length > 0) {
        this.exportForm.formatName = this.exportTypes[0].name;
      }
    },
    handleOpAuditExportEvent(exportData) {
      if (!exportData || exportData.exportId !== this.exportProgress.exportId) {
        return;
      }

      this.exportProgress = {
        ...this.exportProgress,
        stage: exportData.stage,
        preparedRows: exportData.preparedRows,
        percent: exportData.percent || 0
      };

      if (exportData.stage === 'FAILED' && exportData.errorMessage) {
        this.$Message.error(exportData.errorMessage);
      }
    },
    async handleConfirmExport() {
      this.ensureDefaultExportFormat();
      if (!this.exportForm.formatName) {
        this.$Message.warning(this.$t('qing-xuan-ze-dao-chu-ge-shi'));
        return;
      }

      const maxRows = this.exportForm.rowMode === 'all' ? null : Number(this.exportForm.maxRows);
      if (maxRows !== null && (!Number.isInteger(maxRows) || maxRows <= 0)) {
        this.$Message.warning(this.$t('qing-shu-ru-dao-chu-tiao-shu'));
        return;
      }

      const exportId = `${Date.now()}-${Math.random().toString(16).slice(2)}`;
      this.exportLoading = true;
      this.exportProgress = {
        exportId,
        stage: 'PREPARING',
        preparedRows: 0,
        percent: 0
      };
      this.syncTimeRangeQuery();
      const data = { ...this.searchData };
      data.exportId = exportId;
      data.formatName = this.exportForm.formatName;
      data.maxRows = maxRows;
      data.pageData = null;

      try {
        const res = await this.$services.rdpAuditExport({
          data,
          responseType: 'blob',
          modal: false
        });
        if (res && res.headers) {
          const contentDisposition = res.headers['content-disposition'];

          let fileName = '';
          if (contentDisposition) {
            const fileNameMatch = contentDisposition.match(/filename\*=UTF-8''(.+)|filename\*=(.+)|filename=(.+)/);
            if (fileNameMatch && fileNameMatch[1]) {
              fileName = decodeURIComponent(fileNameMatch[1]);
            } else if (fileNameMatch && fileNameMatch[2]) {
              fileName = decodeURIComponent(`${fileNameMatch[2].replace(/\+/g, ' ')}`);
            } else if (fileNameMatch && fileNameMatch[3]) {
              fileName = fileNameMatch[3];
            }
          }

          const blob = new Blob([res.data], { type: res.headers['content-type'] || 'application/octet-stream' });
          const link = document.createElement('a');
          link.href = window.URL.createObjectURL(blob);
          link.download = fileName;
          document.body.appendChild(link); // Need to add links to the document
          link.click();
          document.body.removeChild(link);
          window.URL.revokeObjectURL(link.href);
        }
        this.showExport = false;
      } finally {
        this.exportLoading = false;
      }
    },
    handleRefresh() {
      this.page = 1;
      this.searchData.pageData.pageNumber = 1;
      this.handleSearch();
    },
    syncTimeRangeQuery() {
      if (Array.isArray(this.timeRange) && this.timeRange[0] && this.timeRange[1]) {
        this.searchData.opStart = toUtcISOString(this.timeRange[0]);
        this.searchData.opEnd = toUtcISOString(this.timeRange[1]);
        return;
      }
      this.searchData.opStart = '';
      this.searchData.opEnd = '';
    },
    rdpQueryOperationListCondition() {
      this.$services.rdpAuditQueryListCondition().then((res) => {
        if (res.success) {
          this.auditTypeList = res.data.auditTypeVOS;
          this.resourceTypeList = res.data.resourceTypeVOS;
        }
      });
    },
    async handleSearch() {
      this.refreshLoading = true;
      this.syncTimeRangeQuery();
      this.searchData.pageData.pageNumber = this.page;
      this.$services
        .rdpAuditQueryAll({ data: this.searchData })
        .then((res) => {
          if (res.success) {
            this.logData = res.data.records;
            this.total = res.data.total;
          }
          this.refreshLoading = false;
        })
        .catch(() => {
          this.refreshLoading = false;
        });
    },
    handlePageChange(nextPage) {
      this.page = nextPage;
      this.searchData.pageData.pageNumber = nextPage;
      this.handleSearch();
    },
    handlePageSizeChange(pageSize) {
      this.searchData.pageData.pageSize = pageSize;
      this.handleRefresh();
    },
    handleChangeSearchType() {
      this.page = 1;
      // Reset all search values when switching query type
      this.searchData = {
        uid: '',
        userName: '',
        opStart: '',
        opEnd: '',
        // securityLevel:'',
        pageData: {
          pageNumber: 1,
          pageSize: 20
        }
      };
    },
    formatUid(uid) {
      return `UID: ${uid || ''}`;
    },
    handleGetAuditDetail(row) {
      this.$services
        .rdpLogViewGrepOperationLog({
          data: { operationId: row.id }
        })
        .then((res) => {
          if (res.success) {
            appLogger.debug('res', res);
            this.auditLogDetail = res.data;
            this.selectedRow = row;
            this.showAuditDetail = true;
          }
        });
    },
    handleCancel() {
      this.showAuditDetail = false;
      this.showExport = false;
    },
    formatAuditContent(data) {
      return JSON.parse(`[${data.split('] ')[1]}`);
    }
  }
};
</script>

<style scoped lang="less">
.operation-log {
  height: 100%;
  display: flex;
  flex-direction: column;

  .operator-cell {
    line-height: 20px;

    .operator-uid {
      color: #9ea7b4;
      font-size: 12px;
    }
  }

  .log-time-range-label {
    margin-right: 10px;
  }

  .log-time-range {
    width: 320px;
    margin-right: 10px;
  }

  :deep(.log-time-range.ant-picker) {
    height: 32px;
    border-radius: 6px;
  }
}

.warn-text {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.export-row-count {
  display: flex;
  align-items: center;
  gap: 12px;
}

.export-filter-summary {
  line-height: 32px;
  color: #515a6e;
  word-break: break-word;
}

.export-radio-group {
  :deep(.ivu-radio-wrapper) {
    min-width: 82px;
    text-align: center;
  }
}

.export-count-input {
  width: 180px;
}
</style>
