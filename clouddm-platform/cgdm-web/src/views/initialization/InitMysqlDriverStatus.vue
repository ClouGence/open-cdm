<template>
  <span class="init-mysql-driver-status" :class="driverStatusClass">
    <span class="init-mysql-driver-icon">
      <span v-if="showDriverDownloadProgress" class="init-mysql-driver-progress-circle" :style="driverProgressCircleStyle">
        <span class="init-mysql-driver-progress-circle-text">{{ driverProgressText }}</span>
      </span>
      <LoadingOutlined v-else-if="driverUiState === 'checking'" class="init-mysql-driver-loading-icon" />
      <CheckCircleOutlined v-else-if="driverUiState === 'ready'" class="init-mysql-driver-ready-icon" />
      <ExclamationCircleOutlined v-else class="init-mysql-driver-warning-icon" />
    </span>
    <span class="init-mysql-driver-type">{{ $t('initialization.jdbcDataSourceTypeValue') }}</span>
    <span v-if="driverInlineMessage" class="init-mysql-driver-message">（{{ driverInlineMessage }}）</span>
    <a-button v-if="showActionButton" size="small" type="primary" :disabled="actionDisabled" @click="handleActionClick">
      {{ actionLabel }}
    </a-button>
  </span>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket';
import { CheckCircleOutlined, ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons-vue';

const createInitialDriverStatus = () => ({
  checking: false,
  available: false,
  totalFileCount: 0,
  completedFileCount: 0,
  status: 'IDLE',
  retryAction: 'CHECK',
  message: '',
  driverFamily: '',
  driverVersion: ''
});

const INIT_MYSQL_RUNTIME_DRIVER_FAMILY = 'cgdm-runtime-mysql';
const INIT_MYSQL_RUNTIME_DRIVER_VERSION = 'default';
const INIT_MYSQL_DRIVER_STATUS_READY = 'READY';
const INIT_MYSQL_DRIVER_STATUS_DOWNLOADING = 'DOWNLOADING';

function buildInitMysqlDriverWsUrl() {
  const explicitBase = (process.env.VUE_APP_BASE_URL || '').trim();
  const fallbackOrigin = window.location.origin;
  const baseUrl = explicitBase || fallbackOrigin;
  const parsed = new URL(baseUrl, fallbackOrigin);
  const wsProtocol = parsed.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${wsProtocol}//${parsed.host}/clouddm/console/api/v1/init/ws/mysql-driver`;
}

function resolveDriverUiState(status) {
  switch (status) {
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
}

export default {
  name: 'InitMysqlDriverStatus',
  components: {
    CheckCircleOutlined,
    ExclamationCircleOutlined,
    LoadingOutlined
  },
  data() {
    return {
      driverStatus: createInitialDriverStatus(),
      driverStatusRequestKey: '',
      driverStatusTimeoutId: null,
      driverStatusSocket: null
    };
  },
  watch: {
    driverStatus: {
      deep: true,
      immediate: true,
      handler(status) {
        this.$emit('status-change', {
          ...status,
          uiState: resolveDriverUiState(status?.status)
        });
      }
    }
  },
  computed: {
    driverUiState() {
      return resolveDriverUiState(this.driverStatus.status);
    },
    showDriverDownloadProgress() {
      return ['DOWNLOADING', 'PREPARING', 'SYNCING'].includes(this.driverStatus.status);
    },
    showActionButton() {
      return this.driverUiState === 'unprepared' || this.driverUiState === 'error';
    },
    actionDisabled() {
      return this.driverUiState === 'checking' || this.driverUiState === 'downloading';
    },
    actionLabel() {
      if (this.driverUiState === 'checking' || this.driverUiState === 'downloading') {
        return this.$t('initialization.mysqlDriverDownload');
      }
      if (this.driverUiState === 'error') {
        return this.driverStatus.retryAction === 'DOWNLOAD'
          ? this.$t('initialization.mysqlDriverRetryDownload')
          : this.$t('initialization.mysqlDriverRetryCheck');
      }
      return this.$t('initialization.mysqlDriverDownload');
    },
    driverStatusClass() {
      return `is-${this.driverUiState}`;
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
        '--init-driver-progress-percent': `${this.driverProgressValue}%`
      };
    },
    driverProgressText() {
      const { totalFileCount, completedFileCount } = this.driverStatus;
      if (!(totalFileCount > 0)) {
        return '0/0';
      }

      const safeCompletedFileCount = Math.max(0, Math.min(Number(totalFileCount), Number(completedFileCount) || 0));
      return `${safeCompletedFileCount}/${totalFileCount}`;
    },
    driverInlineMessage() {
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
        status: 'CHECKING',
        retryAction: 'CHECK',
        message: ''
      };
      this.scheduleDriverStatusTimeout(requestKey);

      try {
        const res = await this.$services.dmInitCheckDriverStatus({ data: {} });
        if (this.driverStatusRequestKey !== requestKey) {
          return;
        }

        this.clearDriverStatusTimeout();
        if (res.success) {
          const driverStatus = `${res.data || ''}`;
          const available = driverStatus === INIT_MYSQL_DRIVER_STATUS_READY;
          const downloading = driverStatus === INIT_MYSQL_DRIVER_STATUS_DOWNLOADING;
          this.driverStatus = {
            ...this.driverStatus,
            checking: false,
            available,
            driverFamily: INIT_MYSQL_RUNTIME_DRIVER_FAMILY,
            driverVersion: INIT_MYSQL_RUNTIME_DRIVER_VERSION,
            status: available ? 'AVAILABLE' : downloading ? 'PREPARING' : 'UNAVAILABLE',
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
        status: 'PREPARING',
        retryAction: 'DOWNLOAD',
        message: this.$t('initialization.mysqlDriverPreparing')
      };

      try {
        const res = await this.$services.dmInitDownloadDriver({ data: {} });
        if (!res.success) {
          this.setErrorStatus(res.msg || '', 'DOWNLOAD');
        }
      } catch (error) {
        this.setErrorStatus(error?.message || '', 'DOWNLOAD');
      }
    },
    handleActionClick() {
      if (this.actionDisabled) {
        return;
      }
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
            driverFamily: event.driverFamily || this.driverStatus.driverFamily,
            driverVersion: event.driverVersion || this.driverStatus.driverVersion,
            status: event.available ? 'AVAILABLE' : 'UNAVAILABLE',
            retryAction: event.available ? 'CHECK' : 'DOWNLOAD',
            message: event.message || ''
          };
          return;
        }

        if (event.status === 'FAILED') {
          this.driverStatus = {
            ...this.driverStatus,
            totalFileCount: Number.isFinite(event.totalFileCount) ? event.totalFileCount : this.driverStatus.totalFileCount,
            completedFileCount: Number.isFinite(event.completedFileCount) ? event.completedFileCount : this.driverStatus.completedFileCount,
            driverFamily: event.driverFamily || this.driverStatus.driverFamily,
            driverVersion: event.driverVersion || this.driverStatus.driverVersion
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
          driverFamily: event.driverFamily || this.driverStatus.driverFamily,
          driverVersion: event.driverVersion || this.driverStatus.driverVersion,
          status: event.status || 'PREPARING',
          retryAction: 'DOWNLOAD',
          message: event.message || ''
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
  display: inline-flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  min-height: 32px;
  line-height: 32px;
  color: rgba(0, 0, 0, 0.85);
  vertical-align: middle;
}

.init-mysql-driver-icon {
  flex: 0 0 28px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  line-height: 1;
}

.init-mysql-driver-loading-icon,
.init-mysql-driver-ready-icon,
.init-mysql-driver-warning-icon {
  font-size: 16px;
}

.init-mysql-driver-type {
  font-size: 14px;
  line-height: 22px;
  white-space: nowrap;
}

.init-mysql-driver-loading-icon,
.init-mysql-driver-ready-icon {
  color: #52c41a;
}

.init-mysql-driver-warning-icon {
  color: #faad14;
}

.init-mysql-driver-progress-circle {
  flex: 0 0 28px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 28px;
  min-width: 28px;
  height: 28px;
  min-height: 28px;
  aspect-ratio: 1 / 1;
  box-sizing: border-box;
  border-radius: 50%;
  background: conic-gradient(#1677ff var(--init-driver-progress-percent, 0%), rgba(22, 119, 255, 0.16) 0);
}

.init-mysql-driver-progress-circle::before {
  content: '';
  position: absolute;
  inset: 4px;
  border-radius: 50%;
  background: #fff;
}

.init-mysql-driver-progress-circle-text {
  position: absolute;
  inset: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 9px;
  font-weight: 600;
  color: #0958d9;
}

.init-mysql-driver-message {
  font-size: 12px;
  line-height: 20px;
  color: rgba(0, 0, 0, 0.65);
}

.init-mysql-driver-status.is-error .init-mysql-driver-message,
.init-mysql-driver-status.is-unprepared .init-mysql-driver-message {
  color: #cf1322;
}
</style>
