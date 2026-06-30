<template>
  <div class="driver-selection-field">
    <div class="driver-selection-row">
      <Select v-model="innerDriverFamily" style="width: 180px" transfer @on-change="handleDriverFamilyChange">
        <Option v-for="family in currentDriverFamilies" :key="family.name" :value="family.name">
          {{ family.name }}
        </Option>
      </Select>
      <Select v-model="innerDriverVersion" style="width: 126px" transfer @on-change="handleDriverVersionChange">
        <Option v-for="version in currentDriverVersions" :key="version" :value="version">
          {{ version }}
        </Option>
      </Select>
      <Button v-if="showDriverStatusButton" class="driver-status-button" :disabled="driverStatusButtonDisabled" @click="handleDriverAction">
        {{ driverActionLabel }}
      </Button>
      <span v-if="showDriverReadyState" class="driver-status-icon-wrap">
        <Icon type="md-checkmark-circle" class="driver-status-ready-icon" />
      </span>
    </div>
    <div v-if="showDriverStatusDetail" class="driver-status-detail" :class="driverStatusLineClass">
      <span class="driver-status-icon-wrap" :class="{ 'is-clickable': canClickDriverStatusIcon }" @click="handleDriverStatusIconClick">
        <span v-if="showDriverDownloadProgress" class="driver-status-progress-circle" :style="driverProgressCircleStyle">
          <span class="driver-status-progress-circle-text">{{ driverProgressCircleText }}</span>
        </span>
        <Icon v-else-if="driverUiState === 'checking'" type="ios-loading" class="driver-status-loading-icon" />
        <Icon v-else-if="driverUiState === 'ready'" type="md-checkmark-circle" class="driver-status-ready-icon" />
        <Icon v-else-if="driverUiState === 'unknown'" type="ios-help-circle-outline" class="driver-status-unknown-icon" />
        <Icon v-else-if="driverUiState === 'unprepared'" type="ios-warning-outline" class="driver-status-warning-icon" />
        <Icon v-else-if="driverUiState === 'error'" type="ios-alert-circle" class="driver-status-error-icon" />
        <span v-else class="driver-status-phase-dot"></span>
      </span>
      <span v-if="showDriverStatusMessage" class="driver-status-inline-message" :title="driverStatusInlineMessageText">
        {{ driverStatusInlineMessageText }}
      </span>
    </div>
  </div>
</template>

<script>
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';

const createInitialDriverStatus = () => ({
  checking: false,
  available: false,
  totalFileCount: 0,
  completedFileCount: 0,
  currentFilePercent: 0,
  status: 'IDLE',
  retryAction: 'CHECK',
  message: '',
  detailMessage: '',
  currentFileName: ''
});

export default {
  name: 'DriverSelectionField',
  props: {
    dataSourceType: {
      type: String,
      default: ''
    },
    driverFamilyMap: {
      type: Object,
      default: () => ({})
    },
    queryClusterId: {
      type: [Number, String],
      default: null
    },
    requireCluster: {
      type: Boolean,
      default: false
    },
    currentQueryCluster: {
      type: Object,
      default: () => ({})
    },
    currentStep: {
      type: Number,
      default: 0
    },
    driverFamily: {
      type: String,
      default: ''
    },
    driverVersion: {
      type: String,
      default: ''
    },
    driverValue: {
      type: String,
      default: ''
    },
    driverReady: {
      type: Boolean,
      default: false
    }
  },
  emits: ['update:driverFamily', 'update:driverVersion', 'update:driverValue', 'update:driverReady'],
  data() {
    return {
      innerDriverFamily: this.driverFamily || '',
      innerDriverVersion: this.driverVersion || '',
      driverStatus: createInitialDriverStatus(),
      driverStatusRequestKey: '',
      driverStatusTimeoutId: null
    };
  },
  computed: {
    currentDriverFamilies() {
      return this.driverFamilyMap[this.dataSourceType] || [];
    },
    currentDriverVersions() {
      const family = this.currentDriverFamilies.find((item) => item.name === this.innerDriverFamily);
      return Array.isArray(family?.versions) ? family.versions : [];
    },
    selectedDriverKey() {
      return this.innerDriverFamily && this.innerDriverVersion ? `${this.innerDriverFamily}::${this.innerDriverVersion}` : '';
    },
    selectedDriverStatusKey() {
      if (!this.selectedDriverKey) {
        return '';
      }

      const clusterId = this.normalizeDriverClusterId(this.queryClusterId);
      if (this.requireCluster && !clusterId) {
        return '';
      }
      return `${this.selectedDriverKey}::${clusterId || 'ALL'}`;
    },
    isDriverPrepared() {
      return this.driverStatus.status === 'AVAILABLE' && !!this.driverStatus.available;
    },
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
    showDriverReadyState() {
      return this.driverUiState === 'ready';
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
    showDriverStatusButton() {
      return (
        this.driverUiState !== 'ready' &&
        (this.showDriverCheckAction || this.showDriverDownloadAction || this.showDriverDownloadProgress || this.driverUiState === 'error')
      );
    },
    driverStatusButtonDisabled() {
      return this.driverUiState === 'checking' || this.showDriverDownloadProgress;
    },
    showDriverStatusMessage() {
      return this.driverUiState !== 'ready' && !!this.driverStatusInlineMessageText;
    },
    showDriverStatusDetail() {
      return this.showDriverStatusLine && (this.showDriverStatusMessage || this.showDriverDownloadProgress);
    },
    driverProgressLabel() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return '0/0';
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
    driverProgressCircleText() {
      return this.showDriverDownloadProgress ? this.driverProgressLabel : '';
    },
    driverProgressCircleStyle() {
      return {
        '--driver-progress-percent': `${this.driverProgressValue}%`
      };
    },
    showDriverStatusLine() {
      return !!this.selectedDriverKey && this.driverUiState !== 'idle';
    },
    driverStatusLineClass() {
      return `is-${this.driverUiState}`;
    },
    driverStatusTitleText() {
      return [this.innerDriverFamily, this.innerDriverVersion].filter(Boolean).join(' / ');
    },
    driverStatusTargetText() {
      const fileText = `${this.driverStatus.currentFileName || ''}`.trim();
      const driverText = this.driverStatusTitleText;

      return fileText || driverText;
    },
    driverResourceText() {
      if (this.showDriverDownloadProgress) {
        return `${this.driverStatus.currentFileName || ''}`.trim() || this.driverStatusTargetText;
      }

      return this.driverStatusTitleText;
    },
    driverStatusInlineMessageText() {
      const message = `${this.driverStatus.message || ''}`.trim();
      if (this.driverUiState === 'checking') {
        return message || this.$t('initialization.mysqlDriverChecking');
      }

      if (this.driverUiState === 'downloading') {
        return message || this.$t('initialization.mysqlDriverPreparing');
      }

      if (this.driverUiState === 'unprepared') {
        return this.$t('initialization.mysqlDriverUnavailable');
      }

      if (this.driverUiState === 'error') {
        return message || this.$t('initialization.mysqlDriverUnavailable');
      }

      return '';
    },
    driverActionLabel() {
      if (this.showDriverDownloadProgress) {
        return this.$t('initialization.mysqlDriverDownloadingButton');
      }
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
  watch: {
    driverFamily(value) {
      if (value !== this.innerDriverFamily) {
        this.innerDriverFamily = value || '';
      }
    },
    driverVersion(value) {
      if (value !== this.innerDriverVersion) {
        this.innerDriverVersion = value || '';
      }
    },
    currentDriverFamilies: {
      handler() {
        this.applyDriverFamilySelection();
      },
      immediate: true
    },
    dataSourceType() {
      this.applyDriverFamilySelection(true);
    },
    selectedDriverStatusKey: {
      handler() {
        if (!this.selectedDriverStatusKey) {
          this.resetDriverStatus();
          return;
        }

        if (this.currentStep === 1) {
          this.refreshDriverStatus();
        }
      },
      immediate: true
    },
    currentStep(step) {
      if (step === 1) {
        this.refreshDriverStatus();
      }
    },
    isDriverPrepared: {
      handler(value) {
        this.$emit('update:driverReady', value);
      },
      immediate: true
    }
  },
  created() {
    this.$bus.on(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
  },
  beforeUnmount() {
    this.clearDriverStatusCheckTimeout();
    this.$bus.off(EVENT_BUS_NAME_LIST.WS_RES_DRIVER_DOWNLOAD_EVENT, this.handleDriverDownloadEvent);
  },
  methods: {
    normalizeDriverClusterId(clusterId) {
      const normalized = Number(clusterId);
      return Number.isFinite(normalized) && normalized > 0 ? normalized : null;
    },
    currentClusterHasRunningWorkers() {
      return Number(this.currentQueryCluster?.runningCount) > 0;
    },
    ensureDriverClusterHasRunningWorkers() {
      if (this.currentClusterHasRunningWorkers()) {
        return true;
      }

      this.$Message.warning(this.$t('gai-ji-qun-wu-cun-huo-ji-qi'));
      return false;
    },
    getDriverClusterId() {
      const clusterId = this.normalizeDriverClusterId(this.queryClusterId);
      return clusterId || undefined;
    },
    driverClusterReady() {
      return !this.requireCluster || !!this.normalizeDriverClusterId(this.queryClusterId);
    },
    syncDriverOutputs() {
      const driverValue =
        this.innerDriverFamily && this.innerDriverVersion ? JSON.stringify([this.innerDriverFamily, `/${this.innerDriverVersion}`]) : '';
      this.$emit('update:driverFamily', this.innerDriverFamily || '');
      this.$emit('update:driverVersion', this.innerDriverVersion || '');
      this.$emit('update:driverValue', driverValue);
    },
    applyDriverFamilySelection(forceReset = false) {
      const families = this.currentDriverFamilies;

      if (!families.length) {
        this.innerDriverFamily = '';
        this.innerDriverVersion = '';
        this.syncDriverOutputs();
        this.resetDriverStatus();
        return;
      }

      let currentFamily = families.find((item) => item.name === this.innerDriverFamily);
      if (!currentFamily || forceReset) {
        currentFamily = families[0];
        this.innerDriverFamily = currentFamily?.name || '';
      }

      const versions = Array.isArray(currentFamily?.versions) ? currentFamily.versions : [];
      if (!versions.length) {
        this.innerDriverVersion = '';
        this.syncDriverOutputs();
        this.resetDriverStatus();
        return;
      }

      if (forceReset || !versions.includes(this.innerDriverVersion)) {
        this.innerDriverVersion = versions[0];
      }

      this.syncDriverOutputs();
    },
    handleDriverFamilyChange(familyName) {
      const family = this.currentDriverFamilies.find((item) => item.name === familyName);
      const versions = Array.isArray(family?.versions) ? family.versions : [];
      this.innerDriverFamily = familyName || '';
      this.innerDriverVersion = versions.length ? versions[0] : '';
      this.syncDriverOutputs();
    },
    handleDriverVersionChange(version) {
      this.innerDriverVersion = version || '';
      this.syncDriverOutputs();
    },
    resetDriverStatus() {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusRequestKey = '';
      this.driverStatus = createInitialDriverStatus();
    },
    clearDriverStatusCheckTimeout() {
      if (this.driverStatusTimeoutId) {
        clearTimeout(this.driverStatusTimeoutId);
        this.driverStatusTimeoutId = null;
      }
    },
    scheduleDriverStatusCheckTimeout(requestKey) {
      this.clearDriverStatusCheckTimeout();
      this.driverStatusTimeoutId = setTimeout(() => {
        if (this.driverStatusRequestKey !== requestKey || this.driverStatus.status !== 'CHECKING') {
          return;
        }

        this.driverStatusRequestKey = '';
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: false,
          status: 'UNKNOWN',
          retryAction: 'CHECK',
          message: '',
          detailMessage: ''
        };
      }, 15000);
    },
    setDriverErrorStatus(message, retryAction = 'CHECK', detailMessage = '') {
      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        status: 'ERROR',
        retryAction,
        message: message || '',
        detailMessage: detailMessage || ''
      };
    },
    async refreshDriverStatus() {
      const driverKey = this.selectedDriverStatusKey;
      if (!driverKey || !this.driverClusterReady()) {
        this.resetDriverStatus();
        return;
      }

      const requestKey = `${driverKey}::${Date.now()}`;
      this.driverStatusRequestKey = requestKey;
      this.driverStatus = {
        ...this.driverStatus,
        checking: true,
        available: false,
        status: 'CHECKING',
        retryAction: 'CHECK',
        message: '',
        detailMessage: '',
        currentFileName: '',
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0
      };
      this.scheduleDriverStatusCheckTimeout(requestKey);

      try {
        const res = await this.$services.rdpDataSourceCheckDriverStatus({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily: this.innerDriverFamily,
            driverVersion: this.innerDriverVersion
          }
        });

        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.clearDriverStatusCheckTimeout();

        if (res.success) {
          const available = !!res.data?.available;
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available,
            status: available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: available ? 'CHECK' : 'DOWNLOAD',
            message: '',
            detailMessage: ''
          };
          return;
        }

        this.setDriverErrorStatus(res.msg || '', 'CHECK');
      } catch (error) {
        if (this.driverStatusRequestKey !== requestKey || this.selectedDriverStatusKey !== driverKey) {
          return;
        }

        this.setDriverErrorStatus(error?.message || '', 'CHECK');
      }
    },
    handleCheckDriverStatus() {
      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.refreshDriverStatus();
    },
    handleDriverStatusIconClick() {
      if (this.canClickDriverStatusIcon) {
        this.handleCheckDriverStatus();
      }
    },
    handleDriverAction() {
      if (this.showDriverCheckAction) {
        this.handleCheckDriverStatus();
        return;
      }

      if (this.showDriverDownloadAction) {
        this.handleDownloadDriver();
        return;
      }

      if (this.driverUiState === 'error') {
        if (this.driverStatus.retryAction === 'DOWNLOAD') {
          this.handleDownloadDriver();
        } else {
          this.handleCheckDriverStatus();
        }
      }
    },
    async handleDownloadDriver() {
      if (!this.innerDriverFamily || !this.innerDriverVersion) {
        return;
      }

      if (!this.ensureDriverClusterHasRunningWorkers()) {
        return;
      }

      this.clearDriverStatusCheckTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: '',
        detailMessage: '',
        currentFileName: ''
      };

      try {
        const res = await this.$services.rdpDataSourceDownloadDriver({
          data: {
            clusterId: this.getDriverClusterId(),
            driverFamily: this.innerDriverFamily,
            driverVersion: this.innerDriverVersion
          }
        });

        if (!res.success) {
          this.setDriverErrorStatus(res.msg || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
          this.$Message.error(res.msg || this.$t('xia-zai-shi-bai'));
        }
      } catch (error) {
        this.setDriverErrorStatus(error?.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD');
        this.$Message.error(error?.message || this.$t('xia-zai-shi-bai'));
      }
    },
    handleDriverDownloadEvent(payload) {
      const event = payload?.object || payload;
      if (!event) {
        return;
      }

      const isCurrentDriver =
        event.driverFamily === this.innerDriverFamily &&
        event.driverVersion === this.innerDriverVersion &&
        this.normalizeDriverClusterId(event.clusterId) === this.normalizeDriverClusterId(this.queryClusterId);
      if (!isCurrentDriver) {
        return;
      }

      this.clearDriverStatusCheckTimeout();

      if (event.status === 'COMPLETED') {
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: !!event.available,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          status: 'DOWNLOADING',
          retryAction: 'DOWNLOAD',
          message: event.message || '',
          detailMessage: event.detailMessage || '',
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.refreshDriverStatus();
        return;
      }

      if (event.status === 'FAILED') {
        this.setDriverErrorStatus(event.message || this.$t('xia-zai-shi-bai'), 'DOWNLOAD', event.detailMessage || event.message || '');
        this.driverStatus = {
          ...this.driverStatus,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          currentFileName: event.currentFileName || this.driverStatus.currentFileName
        };
        this.$Message.error(event.message || this.$t('xia-zai-shi-bai'));
        return;
      }

      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: !!event.available,
        totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
        completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
        currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
        status: event.status || 'DOWNLOADING',
        retryAction: 'DOWNLOAD',
        message: event.message || '',
        detailMessage: event.detailMessage || '',
        currentFileName: event.currentFileName || this.driverStatus.currentFileName
      };
    }
  }
};
</script>

<style lang="less" scoped>
.driver-selection-field {
  display: inline-flex;
  min-width: 0;
  position: relative;
}

.driver-selection-row {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
  min-width: 0;
}

.driver-status-loading-icon {
  color: #52c41a;
  font-size: 16px;
}

.driver-status-progress-circle {
  flex: 0 0 28px;
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  min-width: 28px;
  height: 28px;
  min-height: 28px;
  aspect-ratio: 1 / 1;
  box-sizing: border-box;
  border-radius: 50%;
  background: conic-gradient(#1677ff var(--driver-progress-percent, 0%), rgba(22, 119, 255, 0.16) 0);
}

.driver-status-progress-circle::before {
  content: '';
  position: absolute;
  inset: 4px;
  border-radius: 50%;
  background: #fff;
}

.driver-status-progress-circle-text {
  position: absolute;
  inset: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 9px;
  font-weight: 600;
  line-height: 1;
  color: #0958d9;
}

.driver-status-icon-wrap {
  flex: 0 0 28px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  min-width: 28px;
  height: 28px;
  line-height: 1;
}

.driver-status-icon-wrap.is-clickable {
  cursor: pointer;
}

.driver-status-ready-icon {
  color: #52c41a;
  font-size: 16px;
}

.driver-status-unknown-icon,
.driver-status-warning-icon {
  color: #faad14;
  font-size: 16px;
}

.driver-status-error-icon {
  color: #f5222d;
  font-size: 16px;
}

.driver-status-detail {
  position: absolute;
  left: 0;
  top: 34px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
  max-width: 480px;
  min-height: 22px;
  line-height: 22px;
  color: rgba(0, 0, 0, 0.85);
  vertical-align: middle;
}

.driver-status-phase-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #52c41a;
  flex: 0 0 auto;
}

.driver-status-inline-message {
  color: rgba(0, 0, 0, 0.65);
  font-size: 12px;
  line-height: 20px;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.driver-status-button {
  flex: 0 0 auto;
  min-width: 72px;
}

.driver-status-detail.is-unknown .driver-status-inline-message,
.driver-status-detail.is-unprepared .driver-status-inline-message {
  color: #ad6800;
}

.driver-status-detail.is-error .driver-status-inline-message {
  color: #cf1322;
}
</style>
