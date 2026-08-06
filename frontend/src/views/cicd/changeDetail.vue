<template>
  <div class="change-detail-page">
    <div class="change-detail-shell">
      <div class="change-detail-hero">
        <div class="left-wrap">
          <div class="title-wrap">
            <Tooltip :content="changeInfo?.changeName">
              <span class="title-text-ellipsis">{{ changeInfo?.changeName || '-' }}</span>
            </Tooltip>
            <span class="change-status-pill" :class="changeStatusClass">{{ changeStatusLabel }}</span>
            <span class="change-step-pill" v-if="showChangeStepPill">{{ changeStepLabel }}</span>
            <Tooltip :content="changeInfo.remark" style="width: 450px" v-if="changeInfo.remark">
              <span class="collapse-text-ellipsis" :class="changeInfo.currentStatus === 'FAILED' ? 'red-text' : 'gray-text'" v-if="changeInfo.remark">
                {{ '(' + changeInfo.remark + ')' }}
              </span>
            </Tooltip>
          </div>
          <div class="release-grid change-detail-pipeline">
            <div class="release-panel">
              <div class="panel-subheading">
                <CustomIcon
                  :resource="getScmIconResource(changeInfo?.scmType)"
                  :type="changeInfo?.scmType"
                  :alt="getScmDisplayName(changeInfo?.scmType)"
                  size="24px"
                  rightMargin="8px"
                />
                <span>{{ $t('cang-ku') }}</span>
              </div>
              <div class="endpoint-summary-lines">
                <div class="endpoint-summary-line">
                  <span>{{ $t('cang-ku') }}：</span>
                  <strong>{{ changeInfo?.repoName || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('fen-zhi') }}：</span>
                  <strong>{{ changeInfo?.repoBranch || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('lu-jing') }}：</span>
                  <strong>{{ changeInfo?.repoScriptPath || '-' }}</strong>
                </div>
              </div>
            </div>
            <div class="link-divider" aria-hidden="true">
              <span>
                <svg class="flow-link-arrows" viewBox="0 0 28 28">
                  <path d="M7 14h14"></path>
                  <path d="m16.8 9.8 4.2 4.2-4.2 4.2"></path>
                </svg>
              </span>
            </div>
            <div class="release-panel">
              <div class="panel-subheading">
                <CustomIcon :type="changeInfo?.dsType" size="24px" rightMargin="8px" />
                <span>{{ $t('shu-ju-ku') }}</span>
              </div>
              <div class="endpoint-summary-lines">
                <div class="endpoint-summary-line">
                  <span>{{ $t('shi-li-0') }}：</span>
                  <strong>{{ changeInfo?.dsInstance || '-' }}</strong>
                </div>
                <div class="endpoint-summary-line">
                  <span>{{ $t('miao-shu') }}：</span>
                  <strong>{{ changeInfo?.dsDesc || '-' }}</strong>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="right-wrap">
          <div class="btns">
            <Button
              class="detail-action-btn"
              @click="retryChange"
              v-if="changeInfo.currentStep !== 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)"
            >
              {{ $t('zhong-shi-bian-geng') }}
            </Button>
            <Button
              class="detail-action-btn"
              @click="retryChange"
              v-if="changeInfo.currentStep === 'APPROVAL' && !(cantRetry || isBtnOnlyRead || isReadyStatus)"
            >
              {{ $t('zhong-xin-fa-qi-gong-dan') }}
            </Button>
            <Button class="detail-action-btn" @click="closeChange" v-if="!isBtnOnlyRead">
              {{ $t('guan-bi-bian-geng') }}
            </Button>
            <Button class="detail-action-btn refresh-action-btn" @click="handleRefresh" :loading="loading">
              <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
            </Button>
          </div>
          <Steps
            :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
            :status="STATUS_MAP[changeInfo?.currentStatus]"
            size="small"
            class="step-wrap"
            v-if="changeInfo.currentStatus !== 'CLOSED' && changeInfo.currentStep !== 'INIT_SNAPSHOT'"
          >
            <Step :title="$t('di-jiao')"></Step>
            <Step :title="$t('zhi-xing')"></Step>
            <Step :title="$t('wan-cheng')"></Step>
          </Steps>
          <Steps
            :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
            :status="STATUS_MAP[changeInfo?.currentStatus]"
            size="small"
            class="step-wrap"
            v-if="changeInfo.currentStatus === 'CLOSED' && changeInfo.currentStep !== 'INIT_SNAPSHOT'"
          >
            <Step :title="$t('di-jiao')"></Step>
            <Step :title="$t('bian-geng-guan-bi')"></Step>
          </Steps>
          <Steps
            :current="CHANGE_STATUS_MAP[changeInfo?.currentStep]"
            :status="STATUS_MAP[changeInfo?.currentStatus]"
            size="small"
            class="step-wrap"
            v-if="changeInfo.currentStep === 'INIT_SNAPSHOT'"
          >
            <Step :title="$t('di-jiao')"></Step>
            <Step :title="$t('kuai-zhao-bian-geng')"></Step>
          </Steps>
        </div>
      </div>
      <div class="change-detail-body">
        <div class="content-wrap">
          <AppPageTabs v-model="currentTab" class="tab-wrap" :tabs="detailTabs" @change="tabClick">
            <template #label="{ tab }">
              <Dropdown v-if="tab.name === 'sql-change'" transfer @on-click="handleDropdownClick">
                <span class="change-content-tab-label">
                  {{ $t('sql-bian-geng-nei-rong') }}：{{ subTabLabel }}
                  <CustomIcon type="icon-v2-ArrowDown" size="13px" leftMargin="3px" />
                </span>
                <template #list>
                  <DropdownMenu>
                    <DropdownItem name="result">{{ $t('bian-geng-jie-guo') }}</DropdownItem>
                    <DropdownItem name="diff">{{ $t('bian-geng-diff') }}</DropdownItem>
                  </DropdownMenu>
                </template>
              </Dropdown>
              <span v-else>{{ tab.label }}</span>
            </template>
          </AppPageTabs>
          <div class="tab-item-wrap">
            <div v-if="currentTab === 'sql-change'" class="tab-item">
              <div v-if="isNotChangeReady" style="height: 100%">
                <div v-if="subTab === 'result'" class="change-sql-preview" @wheel="handleSqlPreviewWheel">
                  <read-only-editor
                    ref="changeSqlPreviewEditor"
                    :text="rowSql"
                    key="raw"
                    v-if="rowSql.length"
                    :ds-type="changeInfo?.dsType"
                    virtual-scroll-mode
                    :line-number-start="sqlPreviewStartLine"
                    @viewport-line-count-change="handleSqlPreviewViewportChange"
                  />
                  <input
                    v-if="sqlPreviewInitialized"
                    v-model.number="sqlPreviewStartLine"
                    class="change-virtual-scrollbar"
                    type="range"
                    min="1"
                    :max="sqlPreviewMaxStartLine"
                    step="1"
                    :aria-label="$t('ticket-sql-virtual-scrollbar')"
                    aria-orientation="vertical"
                    @input="scheduleSqlPreview"
                  />
                  <CCEmptyContent v-if="!rowSql.length" :content="changeInfo?.remark ? changeInfo?.remark : $t('wu-bian-geng-nei-rong')" />
                </div>
                <div v-if="subTab === 'diff'" style="height: 100%">
                  <Collapse v-model="activeNames" accordion v-if="changeBody.length" @on-change="handleDiffPanelChange">
                    <Panel v-for="(item, index) in changeBody" :key="index" :name="index.toString()">
                      {{ item.contentName }}
                      <template #content>
                        <div style="height: 400px">
                          <ChangeBodyDiff
                            :original="item.oldBody || ''"
                            :modified="item.newBody || ''"
                            language="sql"
                            :ds-type="changeInfo?.dsType"
                          />
                        </div>
                      </template>
                    </Panel>
                  </Collapse>
                  <CCEmptyContent v-else :content="changeInfo?.remark ? changeInfo?.remark : $t('wu-bian-geng-nei-rong')" />
                </div>
              </div>
              <CCEmptyContent v-else loading :content="$t('bian-geng-nei-rong-fen-xi-zhong')" />
            </div>
            <div v-if="currentTab === 'execute'" class="tab-item">
              <CCEmptyContent v-if="approveText" :link="`/ticket/${currentTicket?.ticketId}`" :icon="approveIcon" :content="approveText" />
              <CCEmptyContent v-else loading :content="$t('gong-dan-xin-xi-huo-qu-zhong')" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import CCEmptyContent from '@/components/widgets/CCEmptyContent';
import AppPageTabs from '@/components/layout/AppPageTabs';
import ChangeBodyDiff from './changeBodyDiff';
import { getScmDisplayName, getScmIconResource } from './utils';

import { CHANGE_STATUS_MAP, FLOW_STEP, FLOW_STEP_NUM, STATUS_MAP } from './constant';

export default {
  name: 'changeDetail',
  computed: {
    detailTabs() {
      const currentStep = this.changeInfo?.currentStep;
      const currentStepIndex = CHANGE_STATUS_MAP[currentStep] ?? -1;
      const isSnapshot = currentStep === 'INIT_SNAPSHOT';
      const hasApproval = ['APPROVAL', 'FINISH'].includes(currentStep);
      return [
        {
          name: 'sql-change',
          label: this.$t('sql-bian-geng-nei-rong'),
          disabled: currentStepIndex < FLOW_STEP.S0 || isSnapshot
        },
        {
          name: 'execute',
          label: this.$t('zhi-xing'),
          disabled: !hasApproval || isSnapshot
        }
      ];
    },
    approveStatusMap() {
      return {
        WAIT: {
          icon: 'icon-v2-Progress',
          text: this.$t('cicd-linked-ticket-running')
        },
        FAILED: {
          icon: 'icon-v2-error',
          text: this.$t('cicd-linked-ticket-failed')
        },
        FINISH: {
          icon: 'icon-v2-Success2',
          text: this.$t('cicd-linked-ticket-completed')
        },
        CLOSED: {
          icon: 'icon-v2-Warning2',
          text: this.$t('cicd-linked-ticket-closed')
        }
      };
    },
    currentApproveStatus() {
      const enumStatus = this.currentTicket?.ticketStatus;
      switch (enumStatus) {
        case 'PRE_INIT_WAIT':
        case 'PRE_INIT_RUN':
        case 'WAIT_APPROVAL':
        case 'WAIT_CONFIRM':
        case 'WAIT_EXEC':
        case 'RUNNING':
        case 'EXEC_PAUSE':
          return 'WAIT';
        case 'REJECTED':
        case 'FAILED':
        case 'EXEC_FAIL':
          return 'FAILED';
        case 'FINISHED':
          return 'FINISH';
        case 'CLOSED':
        case 'CANCELED':
          return 'CLOSED';
        default:
          return null;
      }
    },
    approveIcon() {
      const status = this.currentTicket?.ticketStatus || '';
      return this.approveStatusMap[this.currentApproveStatus]?.icon || '';
    },
    approveText() {
      const status = this.currentTicket?.ticketStatus || '';
      return this.approveStatusMap[this.currentApproveStatus]?.text || '';
    },
    changeStatusLabel() {
      const statusMap = {
        OPEN: this.$t('jin-hang-zhong'),
        READY: this.$t('jin-hang-zhong'),
        WAIT: this.$t('zhi-xing-zhong'),
        FAILED: this.$t('shi-bai'),
        FINISH: this.$t('wan-cheng'),
        CLOSED: this.$t('yi-guan-bi')
      };
      return statusMap[this.changeInfo?.currentStatus] || this.changeInfo?.currentStatus || '-';
    },
    changeStatusClass() {
      const status = this.changeInfo?.currentStatus;
      if (status === 'FAILED') {
        return 'danger';
      }
      if (status === 'CLOSED') {
        return 'muted';
      }
      if (status === 'FINISH') {
        return 'success';
      }
      return 'progress';
    },
    changeStepLabel() {
      const stepMap = {
        INIT_SNAPSHOT: this.$t('kuai-zhao-bian-geng'),
        INIT: this.$t('di-jiao'),
        APPROVAL: this.$t('zhi-xing'),
        FINISH: this.$t('wan-cheng')
      };
      return stepMap[this.changeInfo?.currentStep] || this.changeInfo?.currentStep || '-';
    },
    showChangeStepPill() {
      return this.changeStepLabel !== '-' && this.changeStepLabel !== this.changeStatusLabel;
    },
    subTabLabel() {
      return this.subTab === 'diff' ? this.$t('bian-geng-diff') : this.$t('bian-geng-jie-guo');
    },
    sqlPreviewMaxStartLine() {
      return Math.max(1, this.sqlPreviewTotalLines - this.sqlPreviewLineCount + 1);
    },
    isBtnOnlyRead() {
      return this.changeInfo.locked;
    },
    isNotChangeReady() {
      return FLOW_STEP_NUM[this.changeInfo.currentStep] > FLOW_STEP_NUM.INIT || this.changeInfo.currentStatus !== 'READY';
    },
    isReadyStatus() {
      return this.changeInfo.currentStatus === 'READY';
    },
    cantRetry() {
      return this.changeInfo.currentStatus !== 'FAILED';
    }
  },
  watch: {
    currentTab(newVal) {
      this.$router.replace({ query: { ...this.$route.query, tab: newVal } });
    }
  },
  components: {
    AppPageTabs,
    ReadOnlyEditor,
    ChangeBodyDiff,
    CCEmptyContent
  },
  data() {
    return {
      activeNames: [],
      loading: false,
      changeInfo: {},
      rowSql: '',
      sqlPreviewStartLine: 1,
      sqlPreviewTotalLines: 1,
      sqlPreviewLineCount: 30,
      sqlPreviewInitialized: false,
      sqlPreviewTimer: null,
      changeBody: [],
      currentTab: '',
      subTab: 'result',
      changeId: '',
      currentTicket: {},
      FLOW_STEP_NUM,
      FLOW_STEP,
      CHANGE_STATUS_MAP,
      STATUS_MAP
    };
  },
  created() {
    if (this.$route.query.tab) {
      this.currentTab = this.$route.query.tab;
    }
  },
  async mounted() {
    await this.init();
  },
  methods: {
    getScmDisplayName,
    getScmIconResource,
    async init() {
      this.changeId = this.$route.params.id;
      await this.getDetail();
      this.moveToCurrentTab();
      if (this.currentTab === 'sql-change' && this.isNotChangeReady) {
        this.loadSqlPreview();
      }
      if (this.currentTab === 'execute') {
        this.getApproval();
      }
    },
    async getDetail() {
      this.loading = true;
      const res = await this.$services.dmCicdChangeDetail({
        data: {
          changeId: this.changeId
        }
      });

      if (res.success) {
        this.changeInfo = res.data;
        this.syncFlowIdQuery();
      }
      this.loading = false;
    },
    syncFlowIdQuery() {
      const flowId = this.changeInfo?.flowId;
      if (!flowId || String(this.$route.query.flowId || '') === String(flowId)) {
        return;
      }
      this.$router.replace({
        path: this.$route.path,
        query: {
          ...this.$route.query,
          flowId: String(flowId)
        }
      });
    },
    async loadSqlPreview() {
      const editor = this.$refs.finishSqlPreviewEditor || this.$refs.changeSqlPreviewEditor;
      this.sqlPreviewLineCount = Math.min(200, editor?.getVisibleLineCount() || this.sqlPreviewLineCount);
      const res = await this.$services.dmCicdChangeSqlPreview({
        data: {
          changeId: this.changeId,
          startLine: this.sqlPreviewStartLine,
          lineCount: this.sqlPreviewLineCount
        }
      });
      if (res.success) {
        this.sqlPreviewStartLine = res.data?.startLine || 1;
        this.sqlPreviewTotalLines = res.data?.totalLines || 1;
        this.rowSql = res.data?.content || '';
        if (res.data?.itemList) {
          this.changeBody = res.data.itemList;
        }
        this.sqlPreviewInitialized = true;
      }
    },
    async handleDiffPanelChange(name) {
      const selected = Array.isArray(name) ? name[0] : name;
      if (selected === undefined || selected === null || selected === '') {
        return;
      }
      const index = Number(selected);
      const item = this.changeBody[index];
      if (!item || item.loaded) {
        return;
      }
      const res = await this.$services.dmCicdChangeSqlPreview({
        data: {
          changeId: this.changeId,
          contentName: item.contentName,
          startLine: 1,
          lineCount: 1
        }
      });
      if (res.success && res.data?.itemList?.length) {
        this.changeBody.splice(index, 1, { ...res.data.itemList[0], loaded: true });
      }
    },
    scheduleSqlPreview() {
      clearTimeout(this.sqlPreviewTimer);
      this.sqlPreviewTimer = setTimeout(() => this.loadSqlPreview(), 120);
    },
    handleSqlPreviewViewportChange(lineCount) {
      if (!lineCount || lineCount === this.sqlPreviewLineCount) {
        return;
      }
      this.sqlPreviewLineCount = lineCount;
      this.scheduleSqlPreview();
    },
    handleSqlPreviewWheel(event) {
      if (!this.sqlPreviewInitialized) {
        return;
      }
      event.preventDefault();
      const step = Math.max(1, Math.floor(this.sqlPreviewLineCount / 3));
      const delta = event.deltaY > 0 ? step : -step;
      this.sqlPreviewStartLine = Math.max(1, Math.min(this.sqlPreviewMaxStartLine, this.sqlPreviewStartLine + delta));
      this.scheduleSqlPreview();
    },
    async getApproval() {
      const res = await this.$services.dmCicdChangeApproval({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.currentTicket = res.data;
      }
    },
    async closeChange() {
      const res = await this.$services.dmCicdChangeClose({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('guan-bi-cheng-gong'));
        this.init();
      }
    },

    async tabClick(name) {
      await this.getDetail();
      if (name === 'sql-change' && !this.isReadyStatus) {
        this.loadSqlPreview();
      }
      if (name === 'execute') {
        this.getApproval();
      }
    },
    goTicket() {
      if (this.currentTicket) {
        this.$router.push({ path: `/ticket/${this.currentTicket?.ticketId}` });
      }
    },
    async handleRefresh() {
      await this.init();
      const curStep = this.changeInfo?.currentStep;
      this.moveToCurrentTab(curStep);
    },
    async retryChange() {
      const res = await this.$services.dmCicdChangeRetry({
        data: {
          changeId: this.changeId
        }
      });
      if (res.success) {
        this.$Message.success(this.$t('zhong-shi-bian-geng-cheng-gong'));
        this.init();
      }
    },
    moveToCurrentTab(step = this.changeInfo.currentStep) {
      const requestedTab = this.$route.query.tab;
      const requestedTabConfig = this.detailTabs.find((tab) => tab.name === requestedTab);
      if (requestedTabConfig && !requestedTabConfig.disabled) {
        this.currentTab = requestedTab;
        return;
      }

      switch (step) {
        case 'INIT':
          this.currentTab = 'sql-change';
          break;
        case 'APPROVAL':
        case 'FINISH':
          this.currentTab = 'execute';
          break;
        default:
          this.currentTab = 'sql-change';
      }
    },
    handleDropdownClick(name) {
      this.subTab = name;
      if (name === 'result' || !this.changeBody.length) {
        this.loadSqlPreview();
      }
    }
  }
};
</script>

<style lang="less" scoped>
.change-detail-page {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  background: #fff;
  color: #1f2937;

  .uid {
    display: flex;
    cursor: pointer;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }

  .copy-account {
    display: flex;
    align-items: center;
    cursor: pointer;

    .square {
      width: 15px;
      height: 12px;
    }

    i {
      display: none;
    }

    &:hover {
      i {
        display: block;
      }

      .square {
        display: none;
      }
    }
  }

  .action {
    a {
      margin-right: 16px;
    }
  }

  .actions {
    font-size: 12px;
  }
}

.change-detail-shell {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  padding: 20px 24px;
  overflow: hidden;
  gap: 32px;
}

.change-detail-body {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

.page-section {
  min-width: 0;
}

.page-section__title {
  position: relative;
  margin-bottom: 0;
  padding-left: 12px;
  color: #181d26;
  font-size: 16px;
  font-weight: 500;
  line-height: 1.4;
}

.page-section__title::before {
  position: absolute;
  top: 50%;
  left: 0;
  width: 3px;
  height: 16px;
  border-radius: 2px;
  background: #18b566;
  transform: translateY(-50%);
  content: '';
}

.panel-subheading {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  color: #181d26;
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
}

.release-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 64px minmax(0, 1fr);
  align-items: stretch;
  gap: 24px;
}

.release-panel {
  min-width: 0;
}

.link-divider {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.link-divider::before {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  border-left: 1px dashed #d5e0eb;
  content: '';
  transform: translateX(-50%);
}

.link-divider span {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 74px;
  width: 74px;
  min-width: 74px;
  max-width: 74px;
  height: 74px;
  min-height: 74px;
  max-height: 74px;
  box-sizing: border-box;
  aspect-ratio: 1 / 1;
  border: 1px dashed #d8ecdf;
  border-radius: 999px;
  background: #fff;
  color: #0fa958;
}

.link-divider span::before {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 52px;
  min-width: 52px;
  height: 52px;
  min-height: 52px;
  aspect-ratio: 1 / 1;
  border-radius: 999px;
  background: #dff7eb;
  content: '';
  transform: translate(-50%, -50%);
}

.flow-link-arrows {
  position: relative;
  z-index: 1;
  width: 30px;
  height: 30px;
  fill: none;
  stroke: currentColor;
  stroke-linecap: round;
  stroke-linejoin: round;
  stroke-width: 2.3;
}

.change-detail-hero {
  display: grid;
  flex: 0 0 auto;
  grid-template-columns: minmax(620px, 1.35fr) minmax(420px, 0.9fr);
  gap: 24px;
  align-items: start;
  margin-bottom: 0 !important;
  padding: 0;
}

.left-wrap {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.title-wrap {
  display: flex;
  align-items: center;
  min-width: 0;
  gap: 10px;
  margin-bottom: 18px;
}

.title-text-ellipsis {
  display: inline-block;
  max-width: 520px;
  overflow: hidden;
  color: #111827;
  font-size: 20px;
  font-weight: 800;
  line-height: 28px;
  text-overflow: ellipsis;
  vertical-align: middle;
  white-space: nowrap;
}

.change-status-pill,
.change-step-pill {
  display: inline-flex;
  flex: 0 0 auto;
  align-items: center;
  height: 26px;
  padding: 0 12px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 800;
  line-height: 1;
}

.change-status-pill {
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

.change-step-pill {
  color: #5b6a80;
  background: #eef4fa;
}

.change-detail-pipeline {
  margin-top: 8px;
}

.endpoint-summary-lines {
  display: grid;
  gap: 8px;
  min-width: 0;
}

.endpoint-summary-line {
  display: grid;
  grid-template-columns: 88px minmax(0, 1fr);
  align-items: center;
  min-width: 0;
  color: #111827;
  font-size: 14px;
  line-height: 22px;

  span {
    color: #66758a;
    font-weight: 500;
    white-space: nowrap;
  }

  strong {
    min-width: 0;
    overflow: hidden;
    color: #111827;
    font-weight: 500;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.right-wrap {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  min-width: 0;
  min-height: 150px;

  .btns {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-end;
    gap: 8px;
    min-width: 0;
  }

  :deep(.ivu-steps.ivu-steps-small .ivu-steps-title) {
    width: auto;
    color: #5b6a80;
    font-size: 12px;
  }
}

.detail-action-btn {
  height: 32px;
  padding: 0 12px;
  border-color: #d9e3ee;
  border-radius: 5px;
  color: #111827;
  font-size: 12px;
  font-weight: 500;
  background: #fff;
  box-shadow: 0 3px 8px rgba(31, 45, 61, 0.04);
}

.primary-action {
  border-color: #14b86f;
  background: #14b86f;
}

.refresh-action-btn {
  width: 34px;
  padding: 0;
}

.step-wrap {
  width: 100%;
  max-width: 560px;
  margin-top: 20px;
  padding: 0;
  border-left: 0;
}

.progress-bar {
  width: 200px;
}

.content-wrap {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;

  .read-only-editor {
    border: none;
  }

  .tab-wrap {
    flex: 0 0 auto;
    width: 100%;
  }

  .change-content-tab-label {
    display: inline-flex;
    align-items: center;
  }

  .tab-item-wrap {
    flex: 1 1 auto;
    min-height: 0;
    padding: 18px 0 0;
    overflow: auto;
  }

  .tab-item {
    height: 100%;
    min-height: 0;
  }
}

.collapse-text-ellipsis {
  display: inline-block;
  max-width: 90%;
  overflow: hidden;
  text-overflow: ellipsis;
  vertical-align: middle;
  white-space: nowrap;
}

.change-sql-preview {
  position: relative;
  height: 100%;
  padding-right: 18px;
}

.change-virtual-scrollbar {
  position: absolute;
  z-index: 3;
  top: 8px;
  right: 3px;
  width: 14px;
  height: calc(100% - 16px);
  margin: 0;
  writing-mode: vertical-lr;
  direction: ltr;
  appearance: none;
  background: transparent;
  cursor: pointer;

  &::-webkit-slider-runnable-track {
    width: 10px;
    height: 100%;
    background: transparent;
  }

  &::-webkit-slider-thumb {
    width: 10px;
    height: 20px;
    border: 0;
    border-radius: 0;
    appearance: none;
    background: rgba(100, 100, 100, 0.45);
  }

  &::-moz-range-track {
    width: 10px;
    height: 100%;
    background: transparent;
  }

  &::-moz-range-thumb {
    width: 10px;
    height: 20px;
    border: 0;
    border-radius: 0;
    background: rgba(100, 100, 100, 0.45);
  }
}

.red-text {
  color: #ea3323;
  margin-left: 5px;
}

.gray-text {
  color: #66758a;
  margin-left: 5px;
}

@media (max-width: 1320px) {
  .change-detail-hero {
    grid-template-columns: 1fr;
  }

  .right-wrap {
    align-items: flex-start;
    min-height: auto;

    .btns {
      justify-content: flex-start;
    }
  }

  .step-wrap {
    width: 100%;
  }
}

@media (max-width: 980px) {
  .change-detail-shell {
    padding: 16px;
  }

  .change-detail-hero {
    gap: 20px;
  }

  .title-wrap {
    flex-wrap: wrap;
  }

  .title-text-ellipsis {
    max-width: 100%;
  }

  .change-detail-pipeline,
  .release-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .link-divider {
    min-height: 64px;
  }

  .content-wrap .tab-item-wrap {
    padding-top: 16px;
  }
}
</style>
