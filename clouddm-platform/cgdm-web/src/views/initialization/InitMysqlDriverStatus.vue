<template>
  <div class="init-mysql-driver-status" :class="driverStatusClass">
    <div class="init-mysql-driver-status-main">
      <span class="init-mysql-driver-title">{{ $t('initialization.mysqlDriverTitle') }}</span>
      <div class="init-mysql-driver-action">
        <a-button v-if="showActionButton" size="small" type="primary" @click="handleActionClick">
          {{ actionLabel }}
        </a-button>
        <span v-else class="init-mysql-driver-icon">
          <span v-if="showDriverDownloadProgress" class="init-mysql-driver-progress-circle" :style="driverProgressCircleStyle">
            <span class="init-mysql-driver-progress-circle-text">{{ driverProgressText }}</span>
          </span>
          <LoadingOutlined v-else-if="driverUiState === 'checking'" class="init-mysql-driver-loading-icon" />
          <CheckCircleOutlined v-else-if="driverUiState === 'ready'" class="init-mysql-driver-ready-icon" />
          <ExclamationCircleOutlined v-else class="init-mysql-driver-warning-icon" />
        </span>
      </div>
      <span v-if="driverProgressPercentText" class="init-mysql-driver-progress-text">{{ driverProgressPercentText }}</span>
    </div>
    <div v-if="statusHintText" class="init-mysql-driver-status-hint">
      {{ statusHintText }}
    </div>
    <div v-if="driverStatusMessageText" class="init-mysql-driver-message">
      {{ driverStatusMessageText }}
    </div>
  </div>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket';
import { CheckCircleOutlined, ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons-vue';

const createInitialDriverStatus = () => ({
  checking: false,
  available: false,
  totalFileCount: 0,
  completedFileCount: 0,
  currentFilePercent: 0,
  status: 'IDLE',
  retryAction: 'CHECK',
  message: '',
  resourceCoordinate: '',
  currentFileName: '',
  driverFamily: '',
  driverVersion: ''
});

function buildInitMysqlDriverWsUrl() {
  const explicitBase = (process.env.VUE_APP_BASE_URL || '').trim();
  const fallbackOrigin = window.location.origin;
  const baseUrl = explicitBase || fallbackOrigin;
  const parsed = new URL(baseUrl, fallbackOrigin);
  const wsProtocol = parsed.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${wsProtocol}//${parsed.host}/clouddm/console/api/v1/init/ws/mysql-driver`;
}

// Internal backend status messages that should not be surfaced to the user
const INTERNAL_PROGRESS_MESSAGES = new Set(['prepare started', 'resource prepared', 'preparing resource', 'driver ready', 'driver unavailable']);

export default {
  name: 'InitMysqlDriverStatus',
  components: {
    CheckCircleOutlined,
    ExclamationCircleOutlined,
    LoadingOutlined
  },
  emits: ['driver-ready-change'],
  data() {
    return {
      driverStatus: createInitialDriverStatus(),
      driverStatusRequestKey: '',
      driverStatusTimeoutId: null,
      driverStatusSocket: null
    };
  },
  computed: {
    driverUiState() {
      switch (this.driverStatus.status) {
        case 'CHECKING':
          return 'checking';
        case 'AVAILABLE':
          return 'ready';
        case 'DOWNLOADING':
        case 'PREPARING':
        case 'SYNCING':
          return 'downloading';
        case 'ERROR':
        case 'FAILED':
          return 'error';
        case 'UNAVAILABLE':
          return 'unprepared';
        default:
          return 'idle';
      }
    },
    showDriverDownloadProgress() {
      return ['DOWNLOADING', 'PREPARING', 'SYNCING'].includes(this.driverStatus.status);
    },
    showActionButton() {
      return this.driverUiState === 'unprepared' || this.driverUiState === 'error';
    },
    actionLabel() {
      if (this.driverUiState === 'error') {
        return this.driverStatus.retryAction === 'DOWNLOAD' ? this.$t('initialization.mysqlDriverRetryDownload') : this.$t('initialization.mysqlDriverRetryCheck');
      }
      return this.$t('initialization.mysqlDriverDownload');
    },
    driverStatusClass() {
      return `is-${this.driverUiState}`;
    },
    driverProgressValue() {
      const { totalFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return Math.max(0, Math.min(100, Number(this.driverStatus.currentFilePercent) || 0));
      }

      return Math.round((this.safeCompletedFileCount / Number(totalFileCount)) * 100);
    },
    safeCompletedFileCount() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      return Math.max(0, Math.min(Number(totalFileCount) || 0, Number(completedFileCount) || 0));
    },
    driverProgressCircleStyle() {
      return {
        '--init-driver-progress-percent': `${this.driverProgressValue}%`
      };
    },
    driverProgressText() {
      const { totalFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return `${this.driverProgressValue}%`;
      }

      return `${this.safeCompletedFileCount}/${totalFileCount}`;
    },
    driverProgressPercentText() {
      if (!this.showDriverDownloadProgress) {
        return '';
      }
      return `${Math.max(0, Math.min(100, Number(this.driverStatus.currentFilePercent) || 0))}%`;
    },
    statusHintText() {
      if (this.driverUiState === 'checking') {
        return this.$t('initialization.mysqlDriverChecking');
      }
      if (this.driverUiState === 'ready') {
        return this.$t('initialization.mysqlDriverReady');
      }
      if (this.driverUiState === 'downloading') {
        return this.$t('initialization.mysqlDriverPreparing');
      }
      if (this.driverUiState === 'unprepared') {
        return this.$t('initialization.mysqlDriverUnavailable');
      }
      if (this.driverUiState === 'error') {
        return this.$t('initialization.mysqlDriverUnavailable');
      }
      return '';
    },
    driverStatusMessageText() {
      const message = `${this.driverStatus.message || ''}`.trim();
      if (!message) {
        return '';
      }
      if (INTERNAL_PROGRESS_MESSAGES.has(message)) {
        return '';
      }
      return message;
    },
    isDriverReady() {
      return this.driverStatus.available === true && this.driverUiState === 'ready';
    }
  },
  watch: {
    isDriverReady(value) {
      this.$emit('driver-ready-change', value);
    }
  },
  created() {
    this.connectDriverStatusSocket();
    this.refreshDriverStatus();
  },
  beforeUnmount() {
    this.clearDriverStatusTimeout();
    this.disconnectDriverStatusSocket();
  },
  methods: {
    clearDriverStatusTimeout() {
      if (this.driverStatusTimeoutId) {
        clearTimeout(this.driverStatusTimeoutId);
        this.driverStatusTimeoutId = null;
      }
    },
    scheduleDriverStatusTimeout(requestKey) {
      this.clearDriverStatusTimeout();
      this.driverStatusTimeoutId = setTimeout(() => {
        if (this.driverStatusRequestKey !== requestKey || this.driverStatus.status !== 'CHECKING') {
          return;
        }

        this.driverStatusRequestKey = '';
        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: false,
          status: 'ERROR',
          retryAction: 'CHECK',
          message: this.$t('initialization.mysqlDriverCheckTimeout')
        };
      }, 15000);
    },
    setErrorStatus(message, retryAction = 'CHECK') {
      this.clearDriverStatusTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        status: 'ERROR',
        retryAction,
        message: message || ''
      };
    },
    async refreshDriverStatus() {
      const requestKey = `mysql-driver::${Date.now()}`;
      this.driverStatusRequestKey = requestKey;
      this.driverStatus = {
        ...this.driverStatus,
        checking: true,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'CHECKING',
        retryAction: 'CHECK',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      };
      this.scheduleDriverStatusTimeout(requestKey);

      try {
        const res = await this.$services.dmInitCheckMysqlDriverStatus({ data: {} });
        if (this.driverStatusRequestKey !== requestKey) {
          return;
        }

        this.clearDriverStatusTimeout();
        if (res.success) {
          const available = !!res.data?.available;
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available,
            driverFamily: res.data?.driverFamily || '',
            driverVersion: res.data?.driverVersion || '',
            status: available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: available ? 'CHECK' : 'DOWNLOAD',
            message: ''
          };
          return;
        }

        this.setErrorStatus(res.msg || '', 'CHECK');
      } catch (error) {
        if (this.driverStatusRequestKey !== requestKey) {
          return;
        }
        this.setErrorStatus(error?.message || '', 'CHECK');
      }
    },
    async handleDownloadDriver() {
      this.clearDriverStatusTimeout();
      this.driverStatus = {
        ...this.driverStatus,
        checking: false,
        available: false,
        totalFileCount: 0,
        completedFileCount: 0,
        currentFilePercent: 0,
        status: 'PREPARING',
        retryAction: 'DOWNLOAD',
        message: '',
        resourceCoordinate: '',
        currentFileName: ''
      };

      try {
        const res = await this.$services.dmInitDownloadMysqlDriver({ data: {} });
        if (!res.success) {
          this.setErrorStatus(res.msg || '', 'DOWNLOAD');
        }
      } catch (error) {
        this.setErrorStatus(error?.message || '', 'DOWNLOAD');
      }
    },
    handleActionClick() {
      if (this.driverUiState === 'error' && this.driverStatus.retryAction !== 'DOWNLOAD') {
        this.refreshDriverStatus();
        return;
      }
      this.handleDownloadDriver();
    },
    connectDriverStatusSocket() {
      if (this.driverStatusSocket) {
        return;
      }

      const socket = new ReconnectingWebSocket(buildInitMysqlDriverWsUrl(), [], {
        debug: false,
        reconnectInterval: 3000
      });

      socket.addEventListener('message', (event) => {
        this.handleDriverStatusSocketMessage(event.data);
      });

      this.driverStatusSocket = socket;
    },
    disconnectDriverStatusSocket() {
      if (!this.driverStatusSocket) {
        return;
      }

      this.driverStatusSocket.close();
      this.driverStatusSocket = null;
    },
    handleDriverStatusSocketMessage(rawMessage) {
      try {
        const payload = JSON.parse(rawMessage);
        if (!payload || payload.type !== 'INIT_MYSQL_DRIVER_PROGRESS') {
          return;
        }

        const event = payload.object || {};
        this.clearDriverStatusTimeout();

        if (event.status === 'COMPLETED') {
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available: !!event.available,
            totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
            completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
            currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
            driverFamily: event.driverFamily || this.driverStatus.driverFamily,
            driverVersion: event.driverVersion || this.driverStatus.driverVersion,
            status: event.available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: event.available ? 'CHECK' : 'DOWNLOAD',
            message: event.message || '',
            resourceCoordinate: event.resourceCoordinate || '',
            currentFileName: event.currentFileName || ''
          };
          return;
        }

        if (event.status === 'FAILED') {
          this.driverStatus = {
            ...this.driverStatus,
            totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
            completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
            currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
            driverFamily: event.driverFamily || this.driverStatus.driverFamily,
            driverVersion: event.driverVersion || this.driverStatus.driverVersion,
            resourceCoordinate: event.resourceCoordinate || '',
            currentFileName: event.currentFileName || ''
          };
          this.setErrorStatus(event.message || '', 'DOWNLOAD');
          return;
        }

        this.driverStatus = {
          ...this.driverStatus,
          checking: false,
          available: !!event.available,
          totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
          completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
          currentFilePercent: Number.isFinite(event.currentFilePercent) ? event.currentFilePercent : this.driverStatus.currentFilePercent,
          driverFamily: event.driverFamily || this.driverStatus.driverFamily,
          driverVersion: event.driverVersion || this.driverStatus.driverVersion,
          status: event.status || 'PREPARING',
          retryAction: 'DOWNLOAD',
          message: event.message || '',
          resourceCoordinate: event.resourceCoordinate || '',
          currentFileName: event.currentFileName || ''
        };
      } catch (error) {
        console.error('Failed to parse mysql driver status message', error);
      }
    }
  }
};
</script>

<style scoped>
.init-mysql-driver-status {
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-width: 420px;
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 8px;
  background: #fff;
}

.init-mysql-driver-status.is-ready,
.init-mysql-driver-status.is-checking,
.init-mysql-driver-status.is-downloading {
  border-color: #b7eb8f;
  background: #f6ffed;
}

.init-mysql-driver-status.is-unprepared,
.init-mysql-driver-status.is-error {
  border-color: #ffe58f;
  background: #fffbe6;
}

.init-mysql-driver-status-main {
  display: flex;
  align-items: center;
  gap: 12px;
}

.init-mysql-driver-title {
  flex: 1 1 auto;
  min-width: 0;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.init-mysql-driver-action {
  flex: 0 0 auto;
  min-width: 48px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.init-mysql-driver-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
}

.init-mysql-driver-loading-icon,
.init-mysql-driver-ready-icon,
.init-mysql-driver-warning-icon {
  font-size: 20px;
}

.init-mysql-driver-loading-icon,
.init-mysql-driver-ready-icon {
  color: #52c41a;
}

.init-mysql-driver-warning-icon {
  color: #faad14;
}

.init-mysql-driver-progress-circle {
  position: relative;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  background: conic-gradient(#52c41a var(--init-driver-progress-percent, 0%), rgba(82, 196, 26, 0.18) 0);
}

.init-mysql-driver-progress-circle::before {
  content: '';
  position: absolute;
  inset: 5px;
  border-radius: 50%;
  background: #f6ffed;
}

.init-mysql-driver-progress-circle-text {
  position: absolute;
  inset: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 600;
  color: #135200;
}

.init-mysql-driver-progress-text,
.init-mysql-driver-status-hint,
.init-mysql-driver-message {
  font-size: 12px;
  line-height: 18px;
}

.init-mysql-driver-progress-text {
  color: #135200;
  font-weight: 500;
}

.init-mysql-driver-status-hint {
  color: rgba(0, 0, 0, 0.65);
}

.init-mysql-driver-message {
  color: #cf1322;
}
</style>
