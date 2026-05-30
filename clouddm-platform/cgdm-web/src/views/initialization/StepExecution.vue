<template>
  <div class="step-execution">
    <div class="summary-section">
      <div class="summary-title">{{ $t('initialization.processLogs') }}</div>
      <div v-if="executionScripts.length" class="script-status-list">
        <div
          v-for="scriptItem in executionScripts"
          :key="scriptItem.scriptName"
          :ref="(el) => setScriptEntryRef(el, scriptItem.scriptName)"
          :data-script-name="scriptItem.scriptName"
          class="script-status-entry"
        >
          <div class="script-status-item">
            <div class="script-status-main">
              <component :is="resolveStatusIcon(scriptItem.status)" class="script-status-icon" :class="statusIconClass(scriptItem.status)" />
              <span class="script-name">{{ scriptItem.scriptName }}</span>
            </div>
            <div class="script-status-side">
              <button v-if="scriptItem.status === 'ERROR'" type="button" class="script-status-action" @click="toggleErrorDetail(scriptItem)">
                {{ statusText(scriptItem.status) }}
              </button>
              <span v-else class="script-status-text">{{ statusText(scriptItem.status) }}</span>
            </div>
          </div>

          <div v-if="isErrorDetailExpanded(scriptItem)" class="script-error-panel">
            <div class="detail-title-row detail-panel-header">
              <div class="detail-title">{{ $t('initialization.processErrorDetail') }}</div>
              <button type="button" class="detail-fullscreen-button" @click="openFullscreenDetail(scriptItem)">
                [{{ $t('initialization.fullscreen') }}]
              </button>
            </div>
            <div class="detail-section">
              <div class="detail-title">{{ $t('initialization.failedSql') }}</div>
              <pre class="detail-code detail-code-sql">{{ detailSql(scriptItem) }}</pre>
            </div>
            <div class="detail-section">
              <pre class="detail-code detail-code-stack">{{ detailError(scriptItem) }}</pre>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="summary-empty">{{ $t('initialization.noExecutionScripts') }}</div>
    </div>

    <div v-if="showFallbackErrorDetail" ref="fallbackErrorDetail" class="summary-section summary-section-error">
      <div class="summary-title-row">
        <div class="summary-title error-title">{{ $t('initialization.processErrorDetail') }}</div>
        <button type="button" class="detail-fullscreen-button" @click="openFullscreenDetail()">[{{ $t('initialization.fullscreen') }}]</button>
      </div>
      <div class="detail-section">
        <pre class="detail-code detail-code-stack">{{ operationErrorDetail }}</pre>
      </div>
    </div>

    <teleport to="body">
      <div v-if="fullscreenDetail.visible" class="detail-fullscreen-layer" @click.self="closeFullscreenDetail">
        <div class="detail-fullscreen-panel">
          <div class="detail-fullscreen-header">
            <div class="detail-fullscreen-title-group">
              <div class="detail-fullscreen-title">{{ fullscreenDetail.title }}</div>
              <div v-if="fullscreenDetail.scriptName" class="detail-fullscreen-subtitle">{{ fullscreenDetail.scriptName }}</div>
            </div>
            <button type="button" class="detail-fullscreen-close" @click="closeFullscreenDetail">
              {{ $t('initialization.exitFullscreen') }}
            </button>
          </div>
          <div class="detail-fullscreen-content">
            <div class="detail-section">
              <div class="detail-title">{{ $t('initialization.failedSql') }}</div>
              <pre class="detail-code detail-code-sql detail-code-fullscreen">{{ fullscreenDetail.sql }}</pre>
            </div>
            <div class="detail-section">
              <div class="detail-title">{{ $t('initialization.stackTrace') }}</div>
              <pre class="detail-code detail-code-stack detail-code-fullscreen">{{ fullscreenDetail.error }}</pre>
            </div>
          </div>
        </div>
      </div>
    </teleport>
  </div>
</template>

<script>
import { CheckCircleOutlined, ClockCircleOutlined, CloseCircleOutlined, LoadingOutlined } from '@ant-design/icons-vue';

export default {
  name: 'StepExecution',
  components: {
    CheckCircleOutlined,
    ClockCircleOutlined,
    CloseCircleOutlined,
    LoadingOutlined
  },
  props: {
    executionScripts: { type: Array, default: () => [] },
    operationErrorDetail: { type: String, default: '' }
  },
  data() {
    return {
      expandedScriptName: '',
      scriptEntryRefs: Object.create(null),
      fullscreenDetail: {
        visible: false,
        title: '',
        scriptName: '',
        sql: '-',
        error: '-'
      },
      bodyOverflowBeforeFullscreen: ''
    };
  },
  watch: {
    executionScripts(newScripts, oldScripts) {
      const errorScript = this.findNewErrorScript(newScripts, oldScripts);
      if (errorScript && errorScript.scriptName) {
        this.expandedScriptName = errorScript.scriptName;
        this.$nextTick(() => {
          this.scrollToScript(errorScript.scriptName);
        });
        return;
      }

      const anchorScriptName = this.findAutoScrollAnchorScriptName(newScripts);
      if (!anchorScriptName || anchorScriptName === this.findAutoScrollAnchorScriptName(oldScripts)) {
        return;
      }

      this.$nextTick(() => {
        this.scrollToScript(anchorScriptName);
      });
    },
    operationErrorDetail(value) {
      if (!value) {
        return;
      }

      this.$nextTick(() => {
        this.scrollToFallbackErrorDetail();
      });
    }
  },
  computed: {
    showFallbackErrorDetail() {
      return Boolean(this.operationErrorDetail) && !this.executionScripts.some((item) => item && item.status === 'ERROR' && item.errorDetail);
    }
  },
  methods: {
    resolveStatusIcon(status) {
      if (status === 'SUCCESS') {
        return 'CheckCircleOutlined';
      }
      if (status === 'RUNNING') {
        return 'LoadingOutlined';
      }
      if (status === 'ERROR') {
        return 'CloseCircleOutlined';
      }
      return 'ClockCircleOutlined';
    },
    statusIconClass(status) {
      return `script-status-icon-${String(status || 'PENDING').toLowerCase()}`;
    },
    statusText(status) {
      if (status === 'SUCCESS') {
        return this.$t('initialization.scriptStatusSuccess');
      }
      if (status === 'RUNNING') {
        return this.$t('initialization.scriptStatusRunning');
      }
      if (status === 'ERROR') {
        return this.$t('initialization.scriptStatusError');
      }
      return this.$t('initialization.scriptStatusPending');
    },
    toggleErrorDetail(scriptItem) {
      const scriptName = scriptItem && scriptItem.scriptName ? scriptItem.scriptName : '';
      this.expandedScriptName = this.expandedScriptName === scriptName ? '' : scriptName;
    },
    setScriptEntryRef(element, scriptName) {
      if (!scriptName) {
        return;
      }

      if (element) {
        this.scriptEntryRefs[scriptName] = element;
        return;
      }

      delete this.scriptEntryRefs[scriptName];
    },
    isErrorDetailExpanded(scriptItem) {
      return Boolean(scriptItem && scriptItem.scriptName) && scriptItem.status === 'ERROR' && this.expandedScriptName === scriptItem.scriptName;
    },
    findAutoScrollAnchorScriptName(scripts) {
      if (!Array.isArray(scripts) || !scripts.length) {
        return '';
      }

      const runningIndex = scripts.findIndex((item) => item && item.scriptName && item.status === 'RUNNING');
      if (runningIndex < 0) {
        return '';
      }

      const anchorIndex = Math.max(runningIndex - 2, 0);
      const anchorItem = scripts[anchorIndex];
      return anchorItem && anchorItem.scriptName ? anchorItem.scriptName : '';
    },
    findNewErrorScript(newScripts, oldScripts) {
      if (!Array.isArray(newScripts) || !newScripts.length) {
        return null;
      }

      const oldStatusByName = new Map(
        (Array.isArray(oldScripts) ? oldScripts : []).filter((item) => item && item.scriptName).map((item) => [item.scriptName, item.status])
      );

      return (
        newScripts.find(
          (item) => item && item.scriptName && item.status === 'ERROR' && item.errorDetail && oldStatusByName.get(item.scriptName) !== 'ERROR'
        ) || null
      );
    },
    scrollToScript(scriptName) {
      const targetElement = this.scriptEntryRefs[scriptName];
      if (!targetElement || typeof targetElement.scrollIntoView !== 'function') {
        return;
      }

      targetElement.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
        inline: 'nearest'
      });
    },
    scrollToFallbackErrorDetail() {
      const targetElement = this.$refs.fallbackErrorDetail;
      if (!targetElement || typeof targetElement.scrollIntoView !== 'function') {
        return;
      }

      targetElement.scrollIntoView({
        behavior: 'smooth',
        block: 'start',
        inline: 'nearest'
      });
    },
    detailSql(scriptItem) {
      return scriptItem && scriptItem.failedSql ? scriptItem.failedSql : '-';
    },
    detailError(scriptItem) {
      return scriptItem && scriptItem.errorDetail ? scriptItem.errorDetail : this.operationErrorDetail || '-';
    },
    openFullscreenDetail(scriptItem) {
      this.bodyOverflowBeforeFullscreen = document.body.style.overflow;
      document.body.style.overflow = 'hidden';
      this.fullscreenDetail = {
        visible: true,
        title: this.$t('initialization.processErrorDetail'),
        scriptName: scriptItem && scriptItem.scriptName ? scriptItem.scriptName : '',
        sql: this.detailSql(scriptItem),
        error: this.detailError(scriptItem)
      };
    },
    closeFullscreenDetail() {
      document.body.style.overflow = this.bodyOverflowBeforeFullscreen;
      this.fullscreenDetail = {
        visible: false,
        title: '',
        scriptName: '',
        sql: '-',
        error: '-'
      };
    }
  },
  beforeUnmount() {
    document.body.style.overflow = this.bodyOverflowBeforeFullscreen;
  }
};
</script>

<style scoped>
.step-execution {
  height: 100%;
  min-height: 0;
}
.summary-section {
  background: #fff;
  border: 1px solid #f0f0f0;
  margin-bottom: 0;
  overflow: hidden;
}
.summary-title {
  margin-bottom: 0;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
  font-weight: 600;
  color: #262626;
}
.summary-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
}
.summary-title-row .summary-title {
  padding: 0;
  border-bottom: none;
}
.summary-section-error {
  background: #fff2f0;
  border-color: #ffccc7;
  margin-top: 16px;
}
.summary-empty {
  padding: 16px;
  color: #8c8c8c;
  font-size: 13px;
}
.script-status-list {
  display: flex;
  flex-direction: column;
  gap: 0;
}
.script-status-entry {
  border-bottom: 1px solid #f0f0f0;
  background: #fff;
  overflow: hidden;
}
.script-status-entry:last-child {
  border-bottom: none;
}
.script-status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 12px 16px;
}
.script-status-main {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}
.script-status-side {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}
.script-status-icon {
  font-size: 18px;
}
.script-status-icon-pending {
  color: #8c8c8c;
}
.script-status-icon-running {
  color: #1677ff;
}
.script-status-icon-success {
  color: #52c41a;
}
.script-status-icon-error {
  color: #ff4d4f;
}
.script-name {
  color: #262626;
  font-size: 13px;
  font-weight: 500;
  word-break: break-all;
}
.script-status-text {
  color: #595959;
  font-size: 12px;
}
.script-status-action {
  padding: 0;
  border: none;
  background: transparent;
  color: #cf1322;
  font-size: 12px;
  line-height: 1.4;
  cursor: pointer;
}
.script-status-action:hover {
  color: #ff4d4f;
}
.script-error-panel {
  padding: 16px;
  border-top: 1px solid #f5f5f5;
  background: linear-gradient(180deg, #fffaf8 0%, #ffffff 100%);
}
.detail-section + .detail-section {
  margin-top: 16px;
}
.detail-section {
  min-width: 0;
}
.detail-panel-header {
  margin-bottom: 16px;
}
.detail-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 8px;
}
.detail-title {
  margin-bottom: 0;
  color: #262626;
  font-size: 13px;
  font-weight: 600;
}
.detail-fullscreen-button,
.detail-fullscreen-close {
  padding: 0;
  border: none;
  background: transparent;
  color: #1677ff;
  font-size: 12px;
  line-height: 1.4;
  cursor: pointer;
  white-space: nowrap;
}
.detail-fullscreen-button:hover,
.detail-fullscreen-close:hover {
  color: #4096ff;
}
.detail-code {
  margin: 0;
  padding: 12px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
  background: #f7f8fa;
  color: #262626;
  font-size: 12px;
  line-height: 1.6;
  font-family:
    SFMono-Regular,
    Consolas,
    Liberation Mono,
    Menlo,
    monospace;
}
.detail-code-sql {
  white-space: pre-wrap;
  word-break: break-word;
  overflow-wrap: anywhere;
}
.detail-code-stack {
  white-space: pre;
  overflow-x: auto;
  overflow-y: hidden;
}
.detail-fullscreen-layer {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: stretch;
  justify-content: center;
  padding: 24px;
  background: rgba(15, 23, 42, 0.48);
  box-sizing: border-box;
}
.detail-fullscreen-panel {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  width: min(1400px, 100%);
  max-width: 100%;
  max-height: 100%;
  min-height: 0;
  min-width: 0;
  background: #fffdfb;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.18);
  overflow: hidden;
}
.detail-fullscreen-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  background: linear-gradient(180deg, #fff7f4 0%, #fffdfb 100%);
  min-width: 0;
}
.detail-fullscreen-title-group {
  min-width: 0;
}
.detail-fullscreen-title {
  color: #262626;
  font-size: 16px;
  font-weight: 600;
}
.detail-fullscreen-subtitle {
  margin-top: 4px;
  color: #8c8c8c;
  font-size: 12px;
  word-break: break-all;
}
.detail-fullscreen-content {
  flex: 1 1 auto;
  min-height: 0;
  min-width: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px;
}
.detail-code-fullscreen {
  min-height: 240px;
  max-width: 100%;
}
.error-title {
  color: #cf1322;
}
@media (max-width: 768px) {
  .detail-fullscreen-layer {
    padding: 12px;
  }
  .detail-fullscreen-panel {
    border-radius: 10px;
  }
  .detail-fullscreen-header,
  .detail-fullscreen-content {
    padding: 16px;
  }
}
</style>
