<template>
  <div class="change-record-page">
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
              <template #status="{ row }">
                <span class="flow-status-tag" :class="statusIconClass(row)">
                  <Icon class="status-icon" :type="statusIcon(row)" />
                  <span>{{ statusLabel(row) }}</span>
                </span>
              </template>
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
                  <Tooltip :content="row.dsInstance || row.dsDisplay || '-'">
                    <span class="flow-list-ellipsis">{{ compactText(row.dsInstance || row.dsDisplay, 24) }}</span>
                  </Tooltip>
                </div>
              </template>
              <template #stage="{ row }">
                <span class="stage-chip" :class="stageIconClass(row)">
                  <span>{{ stageLabel(row) }}</span>
                </span>
              </template>
              <template #action="{ row }">
                <div class="action flow-actions">
                  <Button type="text" @click="goChangeDetail(row)">{{ $t('xiang-qing') }}</Button>
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
  </div>
</template>

<script>
import { getScmDisplayName, getScmIconResource } from './utils';

export default {
  name: 'CicdChangeRecordList',
  data() {
    return {
      flowId: '',
      searchKeywords: '',
      changeList: [],
      loading: false,
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0
    };
  },
  computed: {
    changeRecordColumns() {
      return [
        {
          title: this.$t('zhuang-tai'),
          slot: 'status',
          width: 140,
          align: 'center'
        },
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
          title: this.$t('jie-duan'),
          slot: 'stage',
          width: 150,
          align: 'center'
        },
        {
          title: this.$t('cao-zuo'),
          slot: 'action',
          width: 100,
          align: 'center'
        }
      ];
    }
  },
  watch: {
    '$route.params.id': {
      handler() {
        this.init();
      }
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    getScmDisplayName,
    getScmIconResource,
    init() {
      this.flowId = this.$route.params.id;
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
    async fetchChangeList() {
      this.loading = true;
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
        this.loading = false;
      }
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
    goChangeDetail(record) {
      if (record.changeId) {
        this.$router.push({
          path: `/cicd/change/${record.changeId}`,
          query: { flowId: this.flowId }
        });
      }
    },
    statusIcon(record) {
      if (record.currentStatus === 'FAILED') {
        return 'ios-close';
      }
      if (record.currentStatus === 'CLOSED') {
        return 'ios-remove';
      }
      if (record.currentStatus === 'FINISH') {
        return 'md-checkmark';
      }
      return 'ios-time-outline';
    },
    statusIconClass(record) {
      if (record.currentStatus === 'FAILED') {
        return 'is-danger';
      }
      if (record.currentStatus === 'CLOSED') {
        return 'is-muted';
      }
      if (record.currentStatus === 'FINISH') {
        return 'is-success';
      }
      return 'is-progress';
    },
    statusLabel(record) {
      const statusMap = {
        OPEN: this.$t('jin-hang-zhong'),
        READY: this.$t('jin-hang-zhong'),
        WAIT: this.$t('deng-dai-zhi-hang'),
        FAILED: this.$t('shi-bai'),
        FINISH: this.$t('wan-cheng'),
        CLOSED: this.$t('yi-guan-bi')
      };
      return statusMap[record.currentStatus] || record.currentStatus || '-';
    },
    stageIconClass(record) {
      if (record.currentStatus === 'FAILED') {
        return 'is-danger';
      }
      if (record.currentStatus === 'CLOSED') {
        return 'is-muted';
      }
      if (record.currentStatus === 'FINISH' || record.currentStep === 'FINISH' || record.currentStep === 'INIT_SNAPSHOT') {
        return 'is-success';
      }
      return 'is-progress';
    },
    stageLabel(record) {
      const stepMap = {
        INIT_SNAPSHOT: this.$t('kuai-zhao-bian-geng'),
        INIT: this.$t('di-jiao'),
        CHECK: this.$t('sql-shen-he'),
        APPROVAL: this.$t('shen-pi-liu'),
        EXECUTE: this.$t('zhi-xing'),
        FINISH: this.$t('wan-cheng')
      };
      return stepMap[record.currentStep] || record.currentStep || '-';
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

.flow-status-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 64px;
  height: 24px;
  padding: 0 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  gap: 4px;

  &.is-success {
    color: #19be6b;
    background: #e7f8ee;
  }

  &.is-progress {
    color: #2d6ccb;
    background: #e8f2ff;
  }

  &.is-danger {
    color: #ed4014;
    background: #fff1f0;
  }

  &.is-muted {
    color: #64748b;
    background: #eef2f7;
  }
}

.status-icon {
  font-size: 14px;
  font-weight: 700;
}

.flow-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;

  :deep(.ivu-btn-text) {
    height: 22px;
    padding: 0 2px;
    line-height: 20px;
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

.stage-chip {
  display: inline-flex;
  align-items: center;
  height: 24px;
  padding: 0 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  white-space: nowrap;

  &.is-success {
    color: #19be6b;
    background: #e7f8ee;
  }

  &.is-progress {
    color: #2d6ccb;
    background: #e8f2ff;
  }

  &.is-danger {
    color: #ed4014;
    background: #fff1f0;
  }

  &.is-muted {
    color: #64748b;
    background: #eef2f7;
  }
}
</style>
