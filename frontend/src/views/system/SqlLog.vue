<template>
  <div class="sql-log">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option border-radius-card">
            <div class="left" style="align-items: center">
              <span class="log-time-range-label">{{ $t('cao-zuo-shi-jian') }}</span>
              <a-range-picker
                v-model:value="timeRange"
                show-time
                format="YYYY-MM-DD HH:mm"
                :placeholder="[$t('kai-shi-shi-jian'), $t('jie-shu-shi-jian')]"
                class="log-time-range"
                @change="handleTimeRangeChange"
              />
              <Select v-model="searchType" style="width: 100px; margin-right: 10px" @on-change="handleChangeSearchType">
                <Option value="user" :label="$t('cao-zuo-ren')">
                  <span>{{ $t('cao-zuo-ren') }}</span>
                </Option>
                <Option value="dsId" :label="$t('shu-ju-yuan')">
                  <span>{{ $t('shu-ju-yuan') }}</span>
                </Option>
                <Option value="requester" :label="$t('sql-lai-yuan')">
                  <span>{{ $t('sql-lai-yuan') }}</span>
                </Option>
                <Option value="status" :label="$t('sql-zhuang-tai')">
                  <span>{{ $t('sql-zhuang-tai') }}</span>
                </Option>
              </Select>
              <Select v-if="searchType === 'user'" v-model="searchData.userUid" style="width: 250px" clearable @on-change="handleRefresh">
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="user in operateUserList" :key="user.userUid" :value="user.userUid" :label="user.userName">
                  {{ user.userName }}
                </Option>
              </Select>
              <Select v-if="searchType === 'dsId'" v-model="searchData.dsId" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="ds in dsList" :key="ds.objId" :value="ds.objId" :label="ds.objAttr.dsInstance">
                  <CustomIcon :type="ds.objAttr.dsType" />
                  {{ ds.objAttr.dsInstance }}
                </Option>
              </Select>
              <Select v-if="searchType === 'requester'" v-model="searchData.requester" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option value="CONSOLE" :label="$t('sql-requester-console')">
                  {{ $t('sql-requester-console') }}
                </Option>
                <Option value="TICKET" :label="$t('sql-requester-ticket')">
                  {{ $t('sql-requester-ticket') }}
                </Option>
                <Option value="CHANGE" :label="$t('sql-requester-change')">
                  {{ $t('sql-requester-change') }}
                </Option>
              </Select>
              <Select v-if="searchType === 'status'" v-model="searchData.status" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option value="RUNNING" label="RUNNING">RUNNING</Option>
                <Option value="SUCCESS" label="SUCCESS">SUCCESS</Option>
                <Option value="WAIT_CONFIRM" label="WAIT_CONFIRM">WAIT_CONFIRM</Option>
                <Option value="ROLLBACK" label="ROLLBACK">ROLLBACK</Option>
                <Option value="FAILURE" label="FAILURE">FAILURE</Option>
                <Option value="ERROR" label="ERROR">ERROR</Option>
              </Select>
              <Button type="primary" ghost @click="handleRefresh" :loading="refreshLoading" style="margin-left: 10px">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Tooltip v-if="canReadUserConfig" transfer :content="$t('shen-ji-ri-zhi-she-zhi')" placement="bottom">
                <Button type="default" style="margin-right: 6px" @click="handleOpenRetentionSetting" :loading="retentionLoading">
                  <CustomIcon type="icon-v2-preference" v-if="!retentionLoading" />
                </Button>
              </Tooltip>
            </div>
          </div>
          <div class="table-container audit-log-table">
            <Table size="small" border :columns="logColumn" :data="logData" :loading="refreshLoading" :scroll="tableScroll">
              <template #operator="{ row }">
                <div class="operator-cell">
                  <div>{{ row.userName }}</div>
                  <div class="operator-uid">{{ formatUid(row.uid) }}</div>
                </div>
              </template>
              <template #datasource="{ row }">
                <div class="datasource-cell">
                  <div class="datasource-id">
                    <CustomIcon :type="row.dataSourceType" />
                    <span>{{ row.dsResourceId }}</span>
                  </div>
                  <div class="datasource-desc">{{ formatDsRemark(row.dsRemark) }}</div>
                </div>
              </template>
              <template #execSql="{ row }">
                <div class="sql-content">
                  <Button v-if="row.execSql" type="text" size="small" @click="showSqlDetail(row)">
                    {{ $t('cha-kan') }}
                  </Button>
                  <span v-else class="no-sql">{{ $t('wu-sql-nei-rong') }}</span>
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
    <CCModal v-model="showSqlModal" title="SQL" width="1000px" @on-ok="handleCloseSqlModal" @on-cancel="handleCloseSqlModal">
      <div v-if="showSqlModal">
        <ReadOnlyDiffEditor
          v-if="selectedRow.rewrite"
          :modified-sql="selectedRow.execSql"
          :original-sql="selectedRow.originalSql"
          :ds-type="selectedRow.dataSourceType"
          style="height: 400px"
        />
        <ReadOnlyEditor v-else :text="selectedRow?.execSql" :ds-type="selectedRow.dataSourceType" style="height: 400px" />
      </div>
    </CCModal>
    <CCModal v-model="showRetentionSetting" :title="$t('shen-ji-ri-zhi-she-zhi')" width="520px">
      <Form ref="retentionFormRef" :model="retentionForm" :rules="retentionRules" :label-width="150">
        <FormItem :label="$t('shen-ji-ri-zhi-bao-cun-tian-shu')" prop="sqlAuditRetentionDays">
          <Input v-model="retentionForm.sqlAuditRetentionDays" type="number" :disabled="!canEditUserConfig" />
        </FormItem>
      </Form>
      <template #footer>
        <Button @click="handleCloseRetentionSetting">{{ $t('guan-bi') }}</Button>
        <Button v-if="canEditUserConfig" type="primary" :loading="retentionSaveLoading" @click="handleSaveRetentionSetting">
          {{ $t('bao-cun') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import appLogger from '@/utils/logger';
import fecha from 'fecha';
import { mapState } from 'vuex';
import { h, resolveComponent } from 'vue';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import ReadOnlyDiffEditor from '@/components/editor/ReadOnlyDiffEditor.vue';
import dayjs from '@/utils/dayjsSetup';

const SQL_AUDIT_RETENTION_DAYS_KEY = 'sqlAuditRetentionDays';

export default {
  name: 'SqlLog',
  components: { ReadOnlyDiffEditor, ReadOnlyEditor },
  data() {
    return {
      searchType: 'user',
      refreshLoading: false,
      page: 1,
      total: 0,
      dsList: [],
      operateUserList: [],
      selectedRow: null,
      showSqlModal: false,
      showRetentionSetting: false,
      retentionLoading: false,
      retentionSaveLoading: false,
      retentionConfig: null,
      followCurrentTimeRange: true,
      retentionForm: {
        sqlAuditRetentionDays: ''
      },
      retentionRules: {
        sqlAuditRetentionDays: [
          {
            required: true,
            message: this.$t('qing-shu-ru-1-dao-60-de-zheng-shu'),
            trigger: 'blur'
          },
          {
            validator: (rule, value, callback) => {
              const numValue = Number(value);
              if (!Number.isInteger(numValue) || numValue < 1 || numValue > 60) {
                callback(new Error(this.$t('qing-shu-ru-1-dao-60-de-zheng-shu')));
                return;
              }
              callback();
            },
            trigger: 'blur'
          }
        ]
      },
      timeRange: [dayjs().subtract(1, 'day'), dayjs()],
      searchData: {
        dsId: null,
        userUid: null,
        requester: null,
        status: null,
        pageData: {
          pageNumber: 1,
          pageSize: 10
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
          key: 'operateTime',
          width: 170,
          render: (_, params) => {
            const row = params.row || params;
            if (!row.operateTime) {
              return h('div', {}, '-');
            }
            const date = new Date(row.operateTime);
            if (isNaN(date.getTime())) {
              return h('div', {}, '-');
            }
            return h('div', {}, fecha.format(date, 'YYYY-MM-DD HH:mm:ss'));
          }
        },
        {
          title: this.$t('zhuang-tai'),
          key: 'status',
          width: 110,
          render: (_, params) => {
            const row = params.row || params;
            let color = '#ed4014';
            if (row.status === 'SUCCESS') {
              color = '#19be6b';
            } else if (row.status === 'RUNNING') {
              color = '#faad14';
            }
            const statusNode = h(
              'div',
              {
                style: {
                  color,
                  display: 'flex',
                  'align-items': 'center'
                }
              },
              [
                h('span', row.status),
                (row.status === 'FAILURE' || row?.status === 'ERROR') && row?.message
                  ? h(
                      resolveComponent('Tooltip'),
                      {
                        content: row.message,
                        placement: 'top',
                        transfer: true
                      },
                      [
                        h(resolveComponent('CustomIcon'), {
                          type: 'help',
                          size: 16,
                          style: { color: '#aaa', marginLeft: '4px', cursor: 'pointer' }
                        })
                      ]
                    )
                  : null
              ]
            );
            return statusNode;
          }
        },
        {
          title: this.$t('shu-ju-yuan'),
          slot: 'datasource',
          width: 240
        },
        {
          title: this.$t('sql-lai-yuan'),
          key: 'requester',
          width: 120,
          render: (_, params) => {
            const row = params.row || params;
            let text = row.requester;
            if (text === 'CONSOLE') text = this.$t('sql-requester-console');
            else if (text === 'TICKET') text = this.$t('sql-requester-ticket');
            else if (text === 'CHANGE') text = this.$t('sql-requester-change');
            else if (!text) text = this.$t('quan-bu');
            return h('span', text);
          }
        },
        {
          title: this.$t('sql-zhi-hang-shi-jian'),
          key: 'cost',
          width: 140,
          render: (_, params) => {
            const row = params.row || params;
            return h('div', {}, `${row.cost || 0}ms`);
          }
        },
        {
          title: this.$t('ying-xiang-hang-shu'),
          key: 'affectLine',
          width: 110
        },
        {
          title: this.$t('cao-zuo-di-zhi'),
          key: 'clientIp',
          width: 140
        },
        {
          title: this.$t('ri-zhi-di-zhi'),
          key: 'logIp',
          width: 140
        },
        {
          title: this.$t('sql-nei-rong'),
          slot: 'execSql',
          width: 100,
          fixed: 'right'
        }
      ],
      logData: []
    };
  },
  computed: {
    ...mapState(['globalSetting', 'myAuth']),
    canReadUserConfig() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_R');
    },
    canEditUserConfig() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_W');
    },
    tableScroll() {
      const scrollX = this.logColumn.reduce((sum, column) => {
        return sum + (column.width || column.minWidth || 0);
      }, 0);
      return { x: scrollX };
    },
    pageSize() {
      return this.searchData.pageData.pageSize;
    }
  },
  mounted() {
    this.getDsList();
    this.getOperateUserList();
    this.handleSearch();
  },
  methods: {
    async getDsList() {
      try {
        const res = await this.$services.dmAuditSqlAuditListDs();
        if (res.data && res.code === '1') {
          this.dsList = res.data || [];
        }
      } catch (error) {
        appLogger.error('获取数据源列表失败:', error);
      }
    },

    async getOperateUserList() {
      try {
        const res = await this.$services.dmAuditSqlAuditOperateUser({
          data: {
            search: ''
          }
        });
        if (res.data && res.code === '1') {
          this.operateUserList = res.data || [];
        }
      } catch (error) {
        appLogger.error('获取操作人列表失败:', error);
      }
    },

    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.handleRefresh();
      }
    },

    handleRefresh() {
      if (this.followCurrentTimeRange) {
        const now = dayjs();
        this.timeRange = [now.subtract(1, 'day'), now];
      }
      this.page = 1;
      this.searchData.pageData.pageNumber = 1;
      this.handleSearch();
    },

    handleTimeRangeChange() {
      this.followCurrentTimeRange = false;
    },

    async handleOpenRetentionSetting() {
      this.showRetentionSetting = true;
      await this.fetchRetentionSetting();
    },

    handleCloseRetentionSetting() {
      this.showRetentionSetting = false;
    },

    async fetchRetentionSetting() {
      this.retentionLoading = true;
      try {
        const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
        if (!res.success) {
          this.$Message.error(res.msg || this.$t('cao-zuo-shi-bai'));
          return;
        }

        const config = (res.data || []).find((item) => item.configName === SQL_AUDIT_RETENTION_DAYS_KEY);
        this.retentionConfig = config || null;
        this.retentionForm.sqlAuditRetentionDays = config ? String(config.configValue || config.defaultValue || '') : '';
      } finally {
        this.retentionLoading = false;
      }
    },

    async handleSaveRetentionSetting() {
      const valid = await this.$refs.retentionFormRef.validate();
      if (!valid) return;

      const value = String(Number(this.retentionForm.sqlAuditRetentionDays));
      const hasCreatedConfig = this.retentionConfig && !this.retentionConfig.needCreated;
      const updateConfigs = hasCreatedConfig ? { [SQL_AUDIT_RETENTION_DAYS_KEY]: value } : {};
      const needCreateConfigs = hasCreatedConfig ? {} : { [SQL_AUDIT_RETENTION_DAYS_KEY]: value };

      this.retentionSaveLoading = true;
      try {
        const res = await this.$services.rdpUserConfigUpsertUserConfigs({
          data: { updateConfigs, needCreateConfigs }
        });
        if (!res.success) {
          this.$Message.error(res.msg || this.$t('cao-zuo-shi-bai'));
          return;
        }
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.showRetentionSetting = false;
        await this.fetchRetentionSetting();
      } finally {
        this.retentionSaveLoading = false;
      }
    },

    handleSearch() {
      this.refreshLoading = true;
      this.syncTimeRangeQuery();
      this.searchData.pageData.pageNumber = this.page;

      this.$services
        .dmAuditSqlAuditQueryAll({
          data: this.searchData
        })
        .then((res) => {
          if (res.code === '1') {
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
      this.searchData = {
        dsId: null,
        userUid: null,
        requester: null,
        status: null,
        pageData: {
          pageNumber: 1,
          pageSize: 10
        }
      };
    },

    formatUid(uid) {
      return `UID: ${uid || ''}`;
    },

    formatDsRemark(dsRemark) {
      return `备注: ${dsRemark || ''}`;
    },

    syncTimeRangeQuery() {
      if (Array.isArray(this.timeRange) && this.timeRange[0] && this.timeRange[1]) {
        this.searchData.opStart = dayjs(this.timeRange[0]).subtract(8, 'hour').format('YYYY-MM-DDTHH:mm:ss.SSS');
        this.searchData.opEnd = dayjs(this.timeRange[1]).subtract(8, 'hour').format('YYYY-MM-DDTHH:mm:ss.SSS');
        return;
      }
      this.searchData.opStart = '';
      this.searchData.opEnd = '';
    },

    showSqlDetail(row) {
      this.selectedRow = row;
      this.showSqlModal = true;
    },

    handleCloseSqlModal() {
      this.showSqlModal = false;
      this.selectedRow = null;
    }
  }
};
</script>

<style scoped lang="less">
.sql-log {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
  overflow-y: auto;

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

  .datasource-cell {
    line-height: 20px;

    .datasource-id {
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .datasource-desc {
      color: #9ea7b4;
      font-size: 12px;
    }
  }

  .sql-content {
    .sql-text {
      display: inline-block;
      max-width: 180px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      cursor: pointer;

      &:hover {
        color: #2d8cf0;
      }
    }

    .no-sql {
      color: #999;
      font-style: italic;
    }
  }
}
</style>
