<template>
  <div class="change-record-page" :class="{ 'is-embedded': embedded }">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                style="width: 280px; margin-right: 10px"
                clearable
                v-model="searchKeywords"
                :placeholder="$t('qing-shu-ru-bian-geng-ming-cheng-cha-xun')"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
          </div>
          <div class="table-container flow-table-container">
            <Table
              :columns="changeRecordColumns"
              :data="changeList"
              :loading="loading"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
              size="small"
              border
              stripe
            >
              <template #target="{ row }">
                <div class="flow-list-inline flow-list-gitops">
                  <CustomIcon
                    v-if="row.scmType"
                    :resource="getScmIconResource(row.scmType)"
                    :alt="getScmDisplayName(row.scmType)"
                    size="18px"
                    rightMargin
                  />
                  <CustomIcon :type="row.dsType || 'icon-v2-DataBase2'" size="18px" rightMargin />
                  <span class="flow-list-ellipsis">{{ compactText(row.dsInstance || row.dsDisplay, 24) }}</span>
                </div>
              </template>
              <template #action="{ row }">
                <div class="action flow-actions">
                  <Button type="text" @click="showSqlContent(row, 'sql')">{{ $t('sql-bian-geng') }}</Button>
                  <Button v-if="row.flowType !== 'BUILT_IN'" type="text" @click="showSqlContent(row, 'diff')">
                    {{ $t('bian-geng-diff') }}
                  </Button>
                  <Button v-if="row.rootChangeId" type="text" :loading="progressLoadingId === row.changeId" @click="showChangeProgress(row)">
                    {{ $t('cicd-change-progress-action') }}
                  </Button>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="pageTotal"
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
    <ChangeRecordProgressModal v-model="changeProgressVisible" :record="selectedChangeRecord" :flow-tree="selectedChangeFlowTree" />
    <ChangeRecordSqlModal v-model="sqlModalVisible" :record="selectedSqlRecord" :mode="sqlModalMode" />
  </div>
</template>

<script>
import ChangeRecordProgressModal from './components/ChangeRecordProgressModal.vue';
import ChangeRecordSqlModal from './components/ChangeRecordSqlModal.vue';
import { getScmDisplayName, getScmIconResource } from './utils';

const CHANGE_LIST_REFRESH_INTERVAL_MS = 3000;

export default {
  name: 'CicdChangeRecordList',
  components: { ChangeRecordProgressModal, ChangeRecordSqlModal },
  props: {
    embedded: {
      type: Boolean,
      default: false
    },
    targetFlowId: {
      type: [String, Number],
      default: ''
    }
  },
  data() {
    return {
      flowId: '',
      searchKeywords: '',
      changeList: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      refreshTimer: null,
      refreshRequestPending: false,
      progressLoadingId: null,
      changeProgressVisible: false,
      selectedChangeRecord: null,
      selectedChangeFlowTree: null,
      sqlModalVisible: false,
      sqlModalMode: 'sql',
      selectedSqlRecord: null
    };
  },
  computed: {
    changeRecordColumns() {
      return [
        {
          title: this.$t('bian-geng-ming-cheng'),
          key: 'changeName',
          minWidth: 200
        },
        {
          title: this.$t('bian-geng-shi-jian'),
          key: 'changeTime',
          width: 180
        },
        {
          title: this.$t('git-ops'),
          slot: 'target',
          minWidth: 220
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          width: 280,
          align: 'center'
        }
      ];
    }
  },
  watch: {
    '$route.params.id': {
      handler() {
        if (!this.embedded) {
          this.init();
        }
      }
    },
    targetFlowId: {
      handler() {
        if (this.targetFlowId) {
          this.init();
        }
      }
    }
  },
  mounted() {
    document.addEventListener('visibilitychange', this.handleVisibilityChange);
    this.init();
  },
  beforeUnmount() {
    document.removeEventListener('visibilitychange', this.handleVisibilityChange);
    this.stopRefresh();
  },
  methods: {
    getScmDisplayName,
    getScmIconResource,
    init() {
      this.stopRefresh();
      this.flowId = this.targetFlowId || this.$route.params.id;
      this.pageNum = 1;
      this.fetchChangeList();
    },
    compactText(value, maxLength = 16) {
      const text = value || '-';
      if (text.length <= maxLength) {
        return text;
      }
      return `${text.slice(0, maxLength)}...`;
    },
    async fetchChangeList({ silent = false } = {}) {
      this.stopRefresh();
      if (this.refreshRequestPending) {
        return;
      }
      this.refreshRequestPending = true;
      if (!silent) {
        this.loading = true;
      }
      try {
        const res = await this.$services.dmCicdChangeList({
          data: {
            flowId: this.flowId,
            searchKeywords: this.searchKeywords,
            page: {
              pageSize: this.pageSize,
              pageNum: this.pageNum
            }
          }
        });

        if (res.success && res.data) {
          this.changeList = res.data.records || [];
          this.pageNum = res.data.current || this.pageNum;
          this.pageSize = res.data.size || this.pageSize;
          this.pageTotal = res.data.total || 0;
        } else {
          this.changeList = [];
          this.pageTotal = 0;
        }
      } finally {
        if (!silent) {
          this.loading = false;
        }
        this.refreshRequestPending = false;
        this.scheduleRefresh();
      }
    },
    stopRefresh() {
      if (this.refreshTimer) {
        window.clearTimeout(this.refreshTimer);
        this.refreshTimer = null;
      }
    },
    scheduleRefresh() {
      this.stopRefresh();
      if (document.hidden) {
        return;
      }
      this.refreshTimer = window.setTimeout(() => {
        this.refreshChangeList();
      }, CHANGE_LIST_REFRESH_INTERVAL_MS);
    },
    async refreshChangeList() {
      this.refreshTimer = null;
      if (document.hidden || this.refreshRequestPending) {
        this.scheduleRefresh();
        return;
      }
      await this.fetchChangeList({ silent: true });
    },
    handleVisibilityChange() {
      if (document.hidden) {
        this.stopRefresh();
        return;
      }
      this.refreshChangeList();
    },
    async refreshAfterTrigger() {
      this.pageNum = 1;
      await this.fetchChangeList({ silent: true });
    },
    async handleQuery() {
      this.pageNum = 1;
      await this.fetchChangeList();
    },
    async handleQueryClear() {
      this.searchKeywords = '';
      this.pageNum = 1;
      await this.fetchChangeList();
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.fetchChangeList();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.pageNum = 1;
      this.fetchChangeList();
    },
    showSqlContent(record, mode) {
      this.selectedSqlRecord = record;
      this.sqlModalMode = mode;
      this.sqlModalVisible = true;
    },
    async showChangeProgress(record) {
      this.progressLoadingId = record.changeId;
      try {
        const [changeRes, flowRes] = await Promise.all([
          this.$services.dmCicdChangeDetail({ data: { changeId: record.rootChangeId } }),
          this.$services.dmCicdFlowDetail({ data: { flowId: record.flowId } })
        ]);
        if (!changeRes.success || !flowRes.success) {
          return;
        }
        this.selectedChangeRecord = changeRes.data;
        this.selectedChangeFlowTree = flowRes.data.dependencyTree || flowRes.data;
        this.changeProgressVisible = true;
      } finally {
        this.progressLoadingId = null;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.change-record-page {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.change-record-page.is-embedded {
  flex: 1;
  height: 100%;

  :deep(.table-list-layout) {
    flex: 1;
    height: 100%;
    min-height: 0;
  }

  :deep(.table-list) {
    flex: 1;
    height: auto;
    padding: 0;
  }

  :deep(.content) {
    height: auto;
    overflow: visible;
  }

  :deep(.content .option) {
    min-height: 0;
    margin: 0 0 16px;
    padding: 0;
    border: 0;
    background: transparent;
  }

  :deep(.table-container) {
    flex: 0 0 auto;
    overflow-x: auto;
  }

  :deep(.footer) {
    min-height: 0;
    margin-top: auto;
    padding: 16px 0 0;
  }
}

.flow-list-inline {
  display: flex;
  align-items: center;
  min-width: 0;
}

.flow-list-gitops {
  max-width: 100%;
}

.flow-list-ellipsis {
  display: inline-block;
  min-width: 0;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.flow-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;

  :deep(.ivu-btn-text) {
    height: 22px;
    padding: 0 2px;
    border: 0;
    background: transparent;
    box-shadow: none;
    line-height: 20px;
  }

  :deep(.ivu-btn-text:hover),
  :deep(.ivu-btn-text:focus),
  :deep(.ivu-btn-text:active) {
    border: 0;
    background: transparent;
    box-shadow: none;
  }

  :deep(.ivu-btn-text span:hover) {
    border-bottom: 0;
  }
}

.flow-table-container {
  border: 1px solid var(--border-light);
  border-radius: 8px;
  background: var(--bg-card);
  overflow: hidden;
}

.flow-table-container :deep(.ivu-table-wrapper) {
  border: 0;
  border-radius: 0;
}

.flow-table-container :deep(.ivu-table-fixed-right) {
  box-shadow: none;
}

.flow-table-container :deep(.ivu-table-fixed-right::before),
.flow-table-container :deep(.ivu-table-fixed::before) {
  display: none;
}
</style>
