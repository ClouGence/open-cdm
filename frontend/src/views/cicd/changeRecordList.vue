<template>
  <div class="change-record-page">
    <div class="table-list-layout change-record-layout">
      <div class="table-list change-record-shell">
        <div class="record-filter-card">
          <Input
            v-model="searchKeywords"
            class="record-filter-input"
            clearable
            :placeholder="$t('qing-shu-ru-bian-geng-ming-cheng-cha-xun')"
            @on-enter="handleQuery"
            @on-clear="handleQueryClear"
          />
          <Button class="record-query-button" type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
        </div>

        <div class="record-table-card">
          <Spin v-if="loading" fix />
          <div class="record-table">
            <div class="record-table-row record-table-head">
              <div>{{ $t('zhuang-tai') }}</div>
              <div>{{ $t('bian-geng-ming-cheng') }}</div>
              <div>{{ $t('bian-geng-shi-jian') }}</div>
              <div>{{ $t('git-ops') }}</div>
              <div>{{ $t('jie-duan') }}</div>
              <div>{{ $t('cao-zuo') }}</div>
            </div>

            <div class="record-table-body">
              <div v-for="record in changeList" :key="record.changeId" class="record-table-row record-data-row">
                <div class="status-cell">
                  <span class="record-status-pill" :class="statusIconClass(record)">
                    <Icon class="status-icon" :type="statusIcon(record)" />
                    <span>{{ statusLabel(record) }}</span>
                  </span>
                </div>
                <div class="change-name-cell">
                  <Tooltip :content="record.changeName || '-'">
                    <span>{{ record.changeName || '-' }}</span>
                  </Tooltip>
                </div>
                <div class="change-time-cell">{{ record.changeTime || '-' }}</div>
                <div class="release-flow-cell">
                  <span class="target-tag">{{ $t('mu-biao') }}</span>
                  <CustomIcon :type="record.dsType || 'icon-v2-DataBase2'" size="18px" />
                  <Tooltip :content="record.dsInstance || record.dsDisplay || '-'">
                    <span class="target-name">{{ compactText(record.dsInstance || record.dsDisplay, 18) }}</span>
                  </Tooltip>
                </div>
                <div class="stage-cell">
                  <span class="stage-chip" :class="stageIconClass(record)">
                    <span>{{ stageLabel(record) }}</span>
                  </span>
                </div>
                <div class="action-cell">
                  <Button type="text" class="record-detail-button" @click="goChangeDetail(record)">{{ $t('xiang-qing') }}</Button>
                </div>
              </div>

              <div v-if="!loading && !changeList.length" class="record-empty">
                <Icon type="ios-folder-open-outline" />
                <span>{{ $t('zan-wu-shu-ju') }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="record-footer">
        <span class="record-total">{{ $t('gong') }} {{ pageTotal }} {{ $t('tiao') }}</span>
        <Page
          v-model="pageNum"
          :total="pageTotal"
          :page-size="pageSize"
          :page-size-opts="pageSizeOptions"
          show-sizer
          size="small"
          @on-change="handlePageChange"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
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
      pageTotal: 0,
      pageSizeOptions: [10, 20, 50, 100]
    };
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
        return 'danger';
      }
      if (record.currentStatus === 'CLOSED') {
        return 'muted';
      }
      if (record.currentStatus === 'FINISH') {
        return 'success';
      }
      return 'progress';
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
        return 'danger';
      }
      if (record.currentStatus === 'CLOSED') {
        return 'muted';
      }
      if (record.currentStatus === 'FINISH' || record.currentStep === 'FINISH' || record.currentStep === 'INIT_SNAPSHOT') {
        return 'success';
      }
      return 'progress';
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
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: #f6f9fc;
}

.change-record-layout {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  padding: 12px 20px 16px;
  background: #f6f9fc;
}

.change-record-shell {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
}

.record-filter-card {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  gap: 14px;
  min-height: 72px;
  margin-bottom: 14px;
  padding: 16px 18px;
  border: 1px solid #dbe6f1;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 12px 30px rgba(31, 45, 61, 0.04);
}

.record-filter-input {
  width: 440px;
  max-width: 45vw;

  &.ant-input-affix-wrapper {
    height: 40px;
    padding: 0 12px;
    border-color: #d9e3ee;
    border-radius: 7px;
    box-shadow: inset 0 1px 2px rgba(16, 24, 40, 0.04);
  }

  :deep(.ivu-input-wrapper),
  :deep(.ivu-input) {
    width: 100%;
  }

  :deep(.ant-input),
  :deep(.ivu-input) {
    height: 38px;
    color: #111827;
    font-size: 14px;
    line-height: 38px;

    &::placeholder {
      color: #a0a7b3;
    }
  }
}

.record-query-button {
  width: 88px;
  height: 40px;
  border-color: #14b86f;
  border-radius: 7px;
  color: #0fac69;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0;

  :deep(span) {
    font-weight: 500;
    letter-spacing: 0;
  }
}

.record-table-card {
  position: relative;
  display: flex;
  flex: 1 1 auto;
  min-height: 0;
  overflow: hidden;
  border: 1px solid #dbe6f1;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 12px 30px rgba(31, 45, 61, 0.04);
}

.record-table {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  overflow: auto;
}

.record-table-row {
  display: grid;
  grid-template-columns: 150px minmax(260px, 1.2fr) 210px minmax(340px, 1.25fr) 150px 112px;
  min-width: 1160px;
}

.record-table-head {
  flex: 0 0 auto;
  min-height: 52px;
  border-bottom: 1px solid #e1ebf3;
  background: #f8fbfe;

  > div {
    display: flex;
    align-items: center;
    padding: 0 20px;
    color: #66758a;
    font-size: 14px;
    font-weight: 800;
  }
}

.record-table-body {
  flex: 1 1 auto;
  min-height: 0;
  overflow: visible;
  background: #fff;
}

.record-data-row {
  min-height: 76px;
  border-bottom: 1px solid #e1ebf3;
  transition: background 0.18s ease;

  &:hover {
    background: #fbfefd;
  }

  > div {
    display: flex;
    align-items: center;
    min-width: 0;
    padding: 0 20px;
  }
}

.status-cell {
  justify-content: flex-start;
}

.record-status-pill,
.stage-chip {
  display: inline-flex;
  align-items: center;
  min-width: 0;
  max-width: 100%;
  height: 30px;
  padding: 0 12px;
  border-radius: 999px;
  font-size: 13px;
  font-weight: 700;
  line-height: 1;
  white-space: nowrap;

  &.success {
    color: #0fac69;
    background: #e0f8e9;
  }

  &.progress {
    color: #2175d9;
    background: #e8f2ff;
  }

  &.danger {
    color: #d92d20;
    background: #fff0ee;
  }

  &.muted {
    color: #667085;
    background: #eef2f7;
  }
}

.status-icon {
  margin-right: 7px;
  font-size: 17px;
  font-weight: 800;
}

.change-name-cell,
.change-time-cell {
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
}

.change-name-cell span,
.target-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.release-flow-cell {
  gap: 8px;
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
}

.target-tag {
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  height: 32px;
  padding: 0 12px;
  border-radius: 5px;
  background: #14b86f;
  color: #fff;
  font-weight: 700;
}

.stage-cell {
  justify-content: flex-start;
}

.action-cell {
  justify-content: flex-start;

  :deep(.ivu-btn-text) {
    padding: 0;
    color: #0fac69;
    font-size: 14px;
    font-weight: 600;
  }
}

.record-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 100%;
  min-height: 240px;
  color: #98a2b3;
  font-size: 14px;

  .ivu-icon {
    font-size: 20px;
  }
}

.record-footer {
  display: flex;
  flex: 0 0 auto;
  align-items: center;
  justify-content: space-between;
  min-height: 58px;
  padding: 12px 8px 0;
  background: transparent;
}

.record-total {
  white-space: nowrap;
  color: #6b7280;
  font-size: 15px;
}

@media (max-width: 1180px) {
  .record-filter-input {
    width: 360px;
  }
}
</style>
