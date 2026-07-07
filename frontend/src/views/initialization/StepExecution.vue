<template>
  <div class="step-execution">
    <div class="summary-section execution-progress-section">
      <div v-if="executionScripts.length" class="execution-progress-wrap">
        <div v-if="executionMessageText" class="execution-inline-message" :class="[executionMessageType, { 'is-live': showExecutionMessageBubbles }]">
          <span v-if="showExecutionMessageBubbles" class="execution-message-bubbles" aria-hidden="true">
            <span class="execution-message-bubble"></span>
            <span class="execution-message-bubble"></span>
            <span class="execution-message-bubble"></span>
          </span>
          <span>{{ executionMessageText }}</span>
        </div>
        <a-progress
          class="execution-progress"
          :percent="executionProgressPercent"
          :status="executionProgressStatus"
          :stroke-color="executionProgressStrokeColor"
          :format="formatExecutionProgress"
        />
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
export default {
  name: 'StepExecution',
  props: {
    executionScripts: { type: Array, default: () => [] },
    operationErrorDetail: { type: String, default: '' },
    executionMessage: { type: Object, default: null }
  },
  data() {
    return {
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
    executionProgressPercent() {
      const total = this.executionScripts.length;
      if (!total) {
        return 0;
      }

      const finished = this.executionScripts.filter((item) => item && ['SUCCESS', 'ERROR'].includes(item.status)).length;
      return Math.max(0, Math.min(100, Math.round((finished / total) * 100)));
    },
    hasExecutionError() {
      return Boolean(this.operationErrorDetail) || this.executionScripts.some((item) => item && item.status === 'ERROR');
    },
    executionProgressStatus() {
      if (this.hasExecutionError) {
        return 'exception';
      }
      if (this.executionProgressPercent >= 100) {
        return 'success';
      }
      if (this.executionScripts.some((item) => item && item.status === 'RUNNING')) {
        return 'active';
      }
      return 'normal';
    },
    executionProgressStrokeColor() {
      return this.hasExecutionError ? '#ff4d4f' : '#52c41a';
    },
    executionMessageText() {
      return this.executionMessage && this.executionMessage.message ? this.executionMessage.message : '';
    },
    executionMessageType() {
      return this.executionMessage && this.executionMessage.type ? this.executionMessage.type : 'info';
    },
    showExecutionMessageBubbles() {
      return Boolean(this.executionMessageText) && this.executionMessageType !== 'error';
    },
    showFallbackErrorDetail() {
      return Boolean(this.operationErrorDetail);
    }
  },
  methods: {
    formatExecutionProgress(percent) {
      return `${Math.round(Number(percent) || 0)}%`;
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
    openFullscreenDetail() {
      this.bodyOverflowBeforeFullscreen = document.body.style.overflow;
      document.body.style.overflow = 'hidden';
      this.fullscreenDetail = {
        visible: true,
        title: this.$t('initialization.processErrorDetail'),
        scriptName: '',
        sql: '-',
        error: this.operationErrorDetail || '-'
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
.execution-progress-section {
  border: none;
}
.execution-progress-wrap {
  padding: 20px 16px;
  background: #fff;
}
.execution-inline-message {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  color: #595959;
  font-size: 13px;
  line-height: 20px;
}
.execution-message-bubbles {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  height: 12px;
  flex: 0 0 auto;
  color: currentColor;
}
.execution-message-bubble {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
  opacity: 0.32;
  animation: executionMessageBubble 1.2s ease-in-out infinite;
}
.execution-message-bubble:nth-child(2) {
  animation-delay: 0.16s;
}
.execution-message-bubble:nth-child(3) {
  animation-delay: 0.32s;
}
@keyframes executionMessageBubble {
  0%,
  80%,
  100% {
    opacity: 0.32;
    transform: translateY(0) scale(0.82);
  }
  40% {
    opacity: 0.9;
    transform: translateY(-3px) scale(1);
  }
}
.execution-inline-message.success {
  color: #389e0d;
}
.execution-inline-message.error {
  color: #cf1322;
}
.execution-inline-message.warning {
  color: #d48806;
}
.execution-inline-message.info {
  color: #389e0d;
}
.execution-progress {
  width: 100%;
}
.detail-section + .detail-section {
  margin-top: 16px;
}
.detail-section {
  min-width: 0;
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
