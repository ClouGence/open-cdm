<template>
  <div v-if="showDriverStatusLine" class="driver-status-line" :class="driverStatusLineClass">
    <div class="driver-status-line-main">
      <span class="driver-status-icon-wrap" :class="{ 'is-clickable': canClickDriverStatusIcon }" @click="handleIconClick">
        <span v-if="showDriverDownloadProgress" class="driver-status-progress-circle" :style="driverProgressCircleStyle">
          <span class="driver-status-progress-circle-text">{{ driverProgressText }}</span>
        </span>
        <Icon v-else-if="driverUiState === 'checking'" type="ios-loading" class="driver-status-loading-icon" />
        <Icon v-else-if="driverUiState === 'ready'" type="md-checkmark-circle" class="driver-status-ready-icon" />
        <Icon v-else-if="driverUiState === 'unknown'" type="ios-help-circle-outline" class="driver-status-unknown-icon" />
        <Icon v-else-if="driverUiState === 'unprepared'" type="ios-warning-outline" class="driver-status-warning-icon" />
        <Icon v-else-if="driverUiState === 'error'" type="ios-alert-circle" class="driver-status-error-icon" />
        <span v-else class="driver-status-phase-dot"></span>
      </span>
      <span class="driver-status-resource" :title="driverResourceText || driverStatusTitleText">
        {{ driverResourceText || driverStatusTitleText }}
      </span>
      <span v-if="driverProgressPercentText" class="driver-status-progress-text driver-status-progress-percent" :title="driverProgressPercentText">
        {{ driverProgressPercentText }}
      </span>
      <div class="driver-status-actions">
        <Button v-if="showDriverActionButton" size="small" @click="handleActionClick">
          {{ driverActionLabel }}
        </Button>
      </div>
    </div>
    <div v-if="driverStatusMessageText" class="driver-status-line-detail driver-status-line-detail-message" :title="driverStatusMessageText">
      {{ driverStatusMessageText }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'DriverStatusLine',
  props: {
    selectedDriverKey: {
      type: String,
      default: ''
    },
    driverFamily: {
      type: String,
      default: ''
    },
    driverVersion: {
      type: String,
      default: ''
    },
    driverStatus: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['check', 'download'],
  computed: {
    driverUiState() {
      switch (this.driverStatus.status) {
        case 'CHECKING':
          return 'checking';
        case 'UNKNOWN':
          return 'unknown';
        case 'AVAILABLE':
          return 'ready';
        case 'UNAVAILABLE':
          return 'unprepared';
        case 'ERROR':
        case 'FAILED':
          return 'error';
        case 'DOWNLOADING':
        case 'PREPARING':
        case 'SYNCING':
          return 'downloading';
        default:
          return 'idle';
      }
    },
    showDriverDownloadProgress() {
      return ['DOWNLOADING', 'PREPARING', 'SYNCING'].includes(this.driverStatus.status);
    },
    showDriverCheckAction() {
      return this.driverUiState === 'unknown';
    },
    showDriverDownloadAction() {
      return this.driverUiState === 'unprepared';
    },
    driverProgressLabel() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return '';
      }

      const safeCompletedFileCount = Math.max(0, Math.min(Number(totalFileCount), Number(completedFileCount) || 0));
      return `${safeCompletedFileCount}/${totalFileCount}`;
    },
    driverProgressValue() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return 0;
      }

      const safeCompletedFileCount = Math.max(0, Math.min(Number(totalFileCount), Number(completedFileCount) || 0));
      return Math.round((safeCompletedFileCount / Number(totalFileCount)) * 100);
    },
    driverProgressCircleStyle() {
      return {
        '--driver-progress-percent': `${this.driverProgressValue}%`
      };
    },
    driverProgressText() {
      return this.showDriverDownloadProgress ? this.driverProgressLabel : '';
    },
    driverProgressPercentText() {
      if (!this.showDriverDownloadProgress) {
        return '';
      }

      return `${Math.max(0, Math.min(100, Number(this.driverStatus.currentFilePercent) || 0))}%`;
    },
    showDriverStatusLine() {
      return !!this.selectedDriverKey && this.driverUiState !== 'idle';
    },
    driverStatusLineClass() {
      return `is-${this.driverUiState}`;
    },
    driverStatusTitleText() {
      return [this.driverFamily, this.driverVersion].filter(Boolean).join(' / ');
    },
    driverStatusTargetText() {
      const resourceText = `${this.driverStatus.resourceCoordinate || ''}`.trim();
      const fileText = `${this.driverStatus.currentFileName || ''}`.trim();
      const driverText = this.driverStatusTitleText;

      if (resourceText && fileText) {
        return `${resourceText}（${fileText}）`;
      }

      return resourceText || fileText || driverText;
    },
    driverResourceText() {
      return this.driverStatusTargetText;
    },
    driverStatusMessageText() {
      const message = `${this.driverStatus.message || ''}`.trim();
      if (!message || message === this.driverStatus.currentFileName || message === this.driverStatus.resourceCoordinate) {
        return '';
      }
      if (message === this.driverStatusTargetText || message === this.driverResourceText) {
        return '';
      }
      return message;
    },
    showDriverActionButton() {
      return this.showDriverCheckAction || this.showDriverDownloadAction || this.driverUiState === 'error';
    },
    driverActionLabel() {
      if (this.showDriverCheckAction) {
        return this.$t('jian-cha');
      }
      if (this.showDriverDownloadAction) {
        return this.$t('xia-zai');
      }
      if (this.driverUiState === 'error') {
        return this.$t('zhong-shi');
      }
      return '';
    },
    canClickDriverStatusIcon() {
      return !['checking', 'downloading'].includes(this.driverUiState);
    }
  },
  methods: {
    handleIconClick() {
      if (this.canClickDriverStatusIcon) {
        this.$emit('check');
      }
    },
    handleActionClick() {
      if (this.showDriverCheckAction) {
        this.$emit('check');
        return;
      }

      if (this.showDriverDownloadAction) {
        this.$emit('download');
        return;
      }

      if (this.driverUiState === 'error') {
        if (this.driverStatus.retryAction === 'DOWNLOAD') {
          this.$emit('download');
        } else {
          this.$emit('check');
        }
      }
    }
  }
};
</script>

<style lang="less" scoped>
.driver-status-loading-icon {
  color: #52c41a;
  font-size: 18px;
}

.driver-status-progress-circle {
  flex: 0 0 auto;
  position: relative;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: conic-gradient(#52c41a var(--driver-progress-percent, 0%), rgba(82, 196, 26, 0.18) 0);
}

.driver-status-progress-circle::before {
  content: '';
  position: absolute;
  inset: 5px;
  border-radius: 50%;
  background: #f6ffed;
}

.driver-status-progress-circle-text {
  position: absolute;
  inset: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
  line-height: 1;
  color: #135200;
}

.driver-status-icon-wrap {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  min-width: 34px;
  height: 34px;
}

.driver-status-icon-wrap.is-clickable {
  cursor: pointer;
}

.driver-status-ready-icon {
  color: #52c41a;
  font-size: 20px;
}

.driver-status-unknown-icon,
.driver-status-warning-icon {
  color: #faad14;
  font-size: 18px;
}

.driver-status-error-icon {
  color: #f5222d;
  font-size: 18px;
}

.driver-status-line {
  margin-top: 10px;
  padding: 10px 12px;
  border: 1px solid #d9f7be;
  background: #f6ffed;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-width: 760px;
}

.driver-status-line.is-checking,
.driver-status-line.is-ready,
.driver-status-line.is-downloading {
  border-color: #d9f7be;
  background: #f6ffed;
}

.driver-status-line.is-unknown,
.driver-status-line.is-unprepared {
  border-color: #ffe58f;
  background: #fffbe6;
}

.driver-status-line.is-error {
  border-color: #ffccc7;
  background: #fff2f0;
}

.driver-status-line-main {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.driver-status-phase-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #52c41a;
  flex: 0 0 auto;
}

.driver-status-resource {
  color: #262626;
  font-size: 13px;
  font-weight: 500;
  flex: 1 1 auto;
  min-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.driver-status-progress-text {
  flex: 0 0 64px;
  text-align: right;
  font-size: 13px;
  font-weight: 500;
  color: #135200;
}

.driver-status-actions {
  flex: 0 0 auto;
  margin-left: auto;
}

.driver-status-line-detail {
  color: #434343;
  font-size: 12px;
  line-height: 18px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.driver-status-line-detail-message {
  color: #595959;
}

.driver-status-line.is-unknown .driver-status-progress-text,
.driver-status-line.is-unprepared .driver-status-progress-text {
  color: #ad6800;
}

.driver-status-line.is-error .driver-status-resource,
.driver-status-line.is-error .driver-status-progress-text,
.driver-status-line.is-error .driver-status-line-detail-message {
  color: #cf1322;
}
</style>
