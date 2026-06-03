<template>
  <div class="initialization">
    <div v-if="mode === 'loading'" class="init-loading-page">
      <div class="loading-card">
        <h2 class="loading-title">{{ pageTitle }}</h2>
        <p class="loading-text">{{ $t('initialization.loading') }}</p>
      </div>
    </div>

    <!-- 错误页模式 -->
    <div v-else-if="mode === 'dbError'" class="init-error-page">
      <div class="error-card">
        <h2 class="error-title">{{ $t('initialization.startFailed') }}</h2>
        <div class="error-detail">
          <p>{{ $t('initialization.errorDetail') }}</p>
          <pre class="error-message">{{ errorMessage }}</pre>
        </div>
        <div class="error-actions">
          <a-button type="primary" @click="handleRetry">{{ $t('initialization.retry') }}</a-button>
          <a-button @click="handleReconfigureDatabase">{{ $t('initialization.reconfigureDatabase') }}</a-button>
        </div>
      </div>
    </div>

    <!-- 初始化向导模式 -->
    <div v-else class="init-wizard">
      <div class="wizard-header">
        <h1>{{ pageTitle }}</h1>
        <div class="wizard-stage-progress">
          <div v-for="(stage, index) in stageItems" :key="stage.key" class="wizard-stage-item" :class="stageState(index)">
            <div class="wizard-stage-marker">
              <span class="wizard-stage-index">{{ index + 1 }}</span>
            </div>
            <span class="wizard-stage-label">{{ stage.label }}</span>
            <div v-if="index < stageItems.length - 1" class="wizard-stage-line" />
          </div>
        </div>
      </div>

      <div class="wizard-content">
        <!-- Step 0: 数据库配置 -->
        <div v-show="currentStep === 0" class="step-panel">
          <StepDb
            :fieldDefs="dbFields"
            :formValues="formValues"
            :dbTestResult="dbTestResult"
            :readonly="isDbFormReadonly"
            :driverStatusActive="currentStep === 0"
            @update:formValues="updateFormValues"
            @driver-status-change="handleMysqlDriverStatusChange"
            @validation-change="handleDbValidationChange"
          />
        </div>

        <!-- Step 1: 安全配置 -->
        <div v-show="!isUpgradeMode && currentStep === 1" class="step-panel">
          <StepSecurity
            :fieldDefs="securityFields"
            :formValues="formValues"
            @update:formValues="updateFormValues"
            @validation-change="handleSecurityValidationChange"
          />
        </div>

        <!-- Step 2: 连接性配置 -->
        <div v-show="!isUpgradeMode && currentStep === connectivityStepIndex" class="step-panel">
          <StepConnectivity :fieldDefs="connectivityFields" :formValues="formValues" @update:formValues="updateFormValues" />
        </div>

        <!-- 确认步骤 -->
        <div v-show="isConfirmStep" class="step-panel">
          <StepConfirm
            :fieldDefs="fieldDefs"
            :formValues="formValues"
            :dbTestResult="dbTestResult"
            :mode="mode"
            :workflowMode="workflowMode"
            :executionScripts="executionScripts"
          />
        </div>

        <div v-show="isExecutionStep" class="step-panel">
          <StepExecution :executionScripts="executionScripts" :operationErrorDetail="operationErrorDetail" />
        </div>
      </div>

      <div class="wizard-footer">
        <div v-if="currentFooterMessage" class="wizard-footer-message" :class="currentFooterMessage.type">
          <template v-if="currentStep === 0 && dbTestResult && dbTestResult.requireConfirmInput">
            <span class="warning-text">{{ currentFooterMessage.message }}</span>
            <span class="warning-confirm-label">{{ dbTestResult.confirmInputLabel }}</span>
            <input
              class="warning-confirm-input"
              :value="rebuildConfirmInput"
              :placeholder="dbTestResult.confirmInputExpectedValue"
              @input="handleRebuildConfirmInput"
            />
          </template>
          <template v-else>
            <span>{{ currentFooterMessage.message }}</span>
          </template>
        </div>
        <div class="wizard-footer-actions">
          <a-button v-if="showPrevButton" @click="prevStep">{{ $t('initialization.prev') }}</a-button>
          <a-button v-if="currentStep === 0 && !isUpgradeMode" :disabled="testingDb || !canTestDb" @click="handleTestDb">
            <span v-if="testingDb" class="button-inline-spinner" aria-hidden="true"></span>
            <span>{{ $t('initialization.testConnection') }}</span>
          </a-button>
          <a-button v-if="showNextButton" class="wizard-next-button" type="primary" :disabled="!canNext" @click="nextStep">
            {{ $t('initialization.next') }}
          </a-button>
          <a-button v-if="isConfirmStep" type="primary" :loading="applying" @click="handleConfirmAction">{{ confirmActionLabel }}</a-button>
          <a-button v-if="showExecutionActionButton" type="primary" :loading="applying" @click="handleExecutionStageAction">
            {{ executionActionLabel }}
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket';
import StepDb from './StepDb.vue';
import StepSecurity from './StepSecurity.vue';
import StepConnectivity from './StepConnectivity.vue';
import StepConfirm from './StepConfirm.vue';
import StepExecution from './StepExecution.vue';
import { consumeDmBootstrapStatus, getDmSystemStatus, isDmSystemReady } from '../../utils/dmGlobalSettings';

const INIT_DB_CREATE_IF_MISSING = 'clougence.init.db.createIfMissing';
const INIT_DB_REBUILD_IF_NOT_EMPTY = 'clougence.init.db.rebuildIfNotEmpty';
const INIT_DB_CONFIRM_DATABASE_NAME = 'clougence.init.db.confirmDatabaseName';
const INIT_WORKFLOW_MODE_KEY = 'clougence.init.workflowMode';
const INSTALL_PHASE_NOTICE_META = {
  DB_REBUILD: {
    titleKey: 'initialization.noticeDbRebuildTitle',
    level: 'info'
  },
  DB_INIT: {
    titleKey: 'initialization.noticeDbInitTitle',
    level: 'info'
  },
  FIX_RUNNING: {
    titleKey: 'initialization.noticeFixTitle',
    level: 'info'
  },
  DB_UPGRADE: {
    titleKey: 'initialization.noticeDbUpgradeTitle',
    level: 'info'
  },
  UPGRADE_TASK: {
    titleKey: 'initialization.noticeUpgradeTaskTitle',
    level: 'info'
  }
};

function hasDbFieldChange(patch) {
  return Object.keys(patch).some((key) => key.startsWith('spring.datasource.'));
}

function normalizeInstallPhaseNotice(payload) {
  return {
    code: payload && typeof payload.code === 'string' ? payload.code : '',
    level: payload && typeof payload.level === 'string' ? payload.level : ''
  };
}

function sleep(timeoutMs) {
  return new Promise((resolve) => setTimeout(resolve, timeoutMs));
}

const RESTART_POLL_REQUEST_TIMEOUT_MS = 1500;

function buildDmGlobalSettingsUrl() {
  const baseUrl = (process.env.VUE_APP_BASE_URL || '').replace(/\/$/, '');
  return `${baseUrl}/clouddm/console/api/v1/dm_global_settings`;
}

function buildInitInstallLogWsUrl() {
  const explicitBase = (process.env.VUE_APP_BASE_URL || '').trim();
  const fallbackOrigin = window.location.origin;
  const baseUrl = explicitBase || fallbackOrigin;
  const parsed = new URL(baseUrl, fallbackOrigin);
  const wsProtocol = parsed.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${wsProtocol}//${parsed.host}/clouddm/console/api/v1/init/ws/install-log`;
}

function resolveMysqlDriverUiState(status) {
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

function createInitialMysqlDriverStatus() {
  return {
    status: 'CHECKING',
    uiState: 'checking',
    available: false,
    message: '',
    detailMessage: ''
  };
}

function normalizeExecutionScriptItem(entry) {
  if (typeof entry === 'string') {
    return {
      scriptName: entry,
      status: 'PENDING',
      failedSql: '',
      errorDetail: ''
    };
  }

  return {
    scriptName: entry?.scriptName || '',
    status: entry?.status || 'PENDING',
    failedSql: entry?.failedSql || '',
    errorDetail: entry?.errorDetail || ''
  };
}

function resetExecutionScriptItems(items) {
  return (items || []).map((item) => {
    const normalized = normalizeExecutionScriptItem(item);
    return {
      ...normalized,
      status: 'PENDING',
      failedSql: '',
      errorDetail: ''
    };
  });
}

function resetExecutionScriptsForRetry(items) {
  return (items || []).map((item) => {
    const normalized = normalizeExecutionScriptItem(item);
    if (normalized.status === 'SUCCESS') {
      return normalized;
    }

    return {
      ...normalized,
      status: 'PENDING',
      failedSql: '',
      errorDetail: ''
    };
  });
}

function mergeExecutionScriptSnapshot(currentItems, snapshotItems) {
  const nextOrder = [];
  const nextMap = new Map();

  (currentItems || []).forEach((item) => {
    const normalized = normalizeExecutionScriptItem(item);
    if (!normalized.scriptName) {
      return;
    }
    nextOrder.push(normalized.scriptName);
    nextMap.set(normalized.scriptName, normalized);
  });

  (snapshotItems || []).forEach((item) => {
    const normalized = normalizeExecutionScriptItem(item);
    if (!normalized.scriptName) {
      return;
    }

    if (!nextMap.has(normalized.scriptName)) {
      nextOrder.push(normalized.scriptName);
    }

    const previous = nextMap.get(normalized.scriptName);
    const shouldKeepSuccess = previous && previous.status === 'SUCCESS' && normalized.status === 'PENDING';
    nextMap.set(normalized.scriptName, {
      ...previous,
      ...normalized,
      ...(shouldKeepSuccess
        ? {
            status: previous.status,
            failedSql: previous.failedSql,
            errorDetail: previous.errorDetail
          }
        : {})
    });
  });

  return nextOrder.map((scriptName) => nextMap.get(scriptName)).filter(Boolean);
}

function upsertExecutionScriptItem(items, nextItem) {
  const normalized = normalizeExecutionScriptItem(nextItem);
  if (!normalized.scriptName) {
    return items;
  }

  const nextItems = [...(items || [])];
  const index = nextItems.findIndex((item) => item.scriptName === normalized.scriptName);
  if (index < 0) {
    nextItems.push(normalized);
    return nextItems;
  }

  nextItems.splice(index, 1, normalized);
  return nextItems;
}

async function pollDmGlobalSettings() {
  const supportsAbortController = typeof AbortController !== 'undefined';
  const controller = supportsAbortController ? new AbortController() : null;
  const timeoutId = controller ? window.setTimeout(() => controller.abort(), RESTART_POLL_REQUEST_TIMEOUT_MS) : null;

  try {
    const response = await fetch(buildDmGlobalSettingsUrl(), {
      method: 'POST',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json; charset=UTF-8'
      },
      body: JSON.stringify({}),
      signal: controller ? controller.signal : undefined
    });

    if (!response.ok) {
      return null;
    }

    try {
      return await response.json();
    } catch (e) {
      return null;
    }
  } catch (e) {
    return null;
  } finally {
    if (timeoutId !== null) {
      window.clearTimeout(timeoutId);
    }
  }
}

function redirectToLoginPage(defaultLogin) {
  const query = defaultLogin ? `?defaultLogin=${encodeURIComponent(defaultLogin)}` : '';
  window.location.replace(`${window.location.origin}${window.location.pathname}#/login${query}`);
}

export default {
  name: 'Initialization',
  components: { StepDb, StepSecurity, StepConnectivity, StepConfirm, StepExecution },
  data() {
    return {
      mode: 'loading', // 'loading' | 'full' | 'upgrade' | 'dbError'
      workflowMode: 'initial',
      errorMessage: '',
      fieldDefs: [],
      formValues: {},
      rebuildConfirmInput: '',
      testDbRefreshTimer: null,
      dbTestResult: null,
      dbMissingFields: [],
      securityMissingFields: [],
      mysqlDriverStatus: createInitialMysqlDriverStatus(),
      upgradeScripts: [],
      executionScripts: [],
      operationErrorDetail: '',
      installLogSocket: null,
      currentStep: 0,
      testingDb: false,
      applying: false,
      restartTimedOut: false,
      executionPhaseStatusType: '',
      executionPhaseStatusMessage: '',
      restartStatusType: '',
      restartStatusMessage: ''
    };
  },
  computed: {
    dbFields() {
      return this.fieldDefs.filter((f) => f.category === 'database');
    },
    securityFields() {
      return this.fieldDefs.filter((f) => f.category === 'security');
    },
    connectivityFields() {
      return this.fieldDefs.filter((f) => f.category === 'connectivity');
    },
    isUpgradeMode() {
      return this.workflowMode === 'upgrade';
    },
    pageTitle() {
      return this.isUpgradeMode ? this.$t('initialization.upgradeTitle') : this.$t('initialization.title');
    },
    stageItems() {
      if (this.isUpgradeMode) {
        return [
          { key: 'db', label: this.$t('initialization.stage.db') },
          { key: 'confirm', label: this.$t('initialization.stage.confirm') },
          { key: 'execute', label: this.$t('initialization.stage.execute') }
        ];
      }

      return [
        { key: 'db', label: this.$t('initialization.stage.db') },
        { key: 'security', label: this.$t('initialization.stage.security') },
        { key: 'connectivity', label: this.$t('initialization.stage.connectivity') },
        { key: 'confirm', label: this.$t('initialization.stage.confirm') },
        { key: 'execute', label: this.$t('initialization.stage.execute') }
      ];
    },
    isConfirmStep() {
      return this.currentStep === this.confirmStepIndex;
    },
    isExecutionStep() {
      return this.currentStep === this.executionStepIndex;
    },
    connectivityStepIndex() {
      return 2;
    },
    confirmStepIndex() {
      return this.stageItems.length - 2;
    },
    executionStepIndex() {
      return this.stageItems.length - 1;
    },
    currentFooterMessage() {
      if (this.isExecutionStep && this.executionPhaseStatusMessage) {
        return {
          type: this.executionPhaseStatusType || 'info',
          message: this.executionPhaseStatusMessage
        };
      }

      if (this.isExecutionStep && this.restartStatusMessage) {
        return {
          type: this.restartStatusType || 'info',
          message: this.restartStatusMessage
        };
      }

      if (this.currentStep === 0) {
        if (this.mysqlDriverFooterMessage) {
          return {
            type: this.mysqlDriverFooterType,
            message: this.mysqlDriverFooterMessage
          };
        }

        if (!this.isUpgradeMode && this.dbMissingFields.length) {
          return {
            type: 'error',
            message: `${this.$t('initialization.dbFormIncomplete')}：${this.dbMissingFields.join('、')}`
          };
        }

        if (this.isUpgradeMode) {
          return null;
        }

        if (!this.dbTestResult || !this.dbTestResult.message) {
          return null;
        }

        return {
          type: this.dbTestResult.messageType || (this.dbTestResult.success ? 'success' : 'error'),
          message: this.dbTestResult.message
        };
      }

      if (!this.isUpgradeMode && this.currentStep === 1 && this.securityMissingFields.length) {
        return {
          type: 'error',
          message: `${this.$t('initialization.securityFormIncomplete')}：${this.securityMissingFields.join('、')}`
        };
      }

      return null;
    },
    canNext() {
      if (this.currentStep === 0) {
        if (this.isUpgradeMode) {
          return this.isMysqlDriverReady;
        }

        return this.isMysqlDriverReady && !this.dbMissingFields.length && Boolean(this.dbTestResult && this.dbTestResult.canProceed);
      }
      if (!this.isUpgradeMode && this.currentStep === 1) {
        return !this.securityMissingFields.length;
      }
      return true;
    },
    showPrevButton() {
      return this.currentStep > 0 && !this.isExecutionStep;
    },
    showNextButton() {
      return !this.isConfirmStep && !this.isExecutionStep;
    },
    confirmActionLabel() {
      if (this.isUpgradeMode) {
        return this.$t('initialization.upgradeAction');
      }

      return this.$t('initialization.applyConfig');
    },
    showExecutionActionButton() {
      return this.isExecutionStep && !this.applying && (this.restartTimedOut || this.restartStatusType === 'error');
    },
    executionActionLabel() {
      if (this.restartTimedOut) {
        return this.$t('shua-xin');
      }

      return this.$t('initialization.retryAction');
    },
    mysqlDriverUiState() {
      return resolveMysqlDriverUiState(this.mysqlDriverStatus.status);
    },
    isMysqlDriverReady() {
      return this.mysqlDriverUiState === 'ready';
    },
    isDbFormReadonly() {
      return this.isUpgradeMode || !this.isMysqlDriverReady;
    },
    mysqlDriverFooterType() {
      switch (this.mysqlDriverUiState) {
        case 'checking':
        case 'downloading':
          return 'info';
        case 'error':
          return 'error';
        case 'unprepared':
          return 'warning';
        default:
          return '';
      }
    },
    mysqlDriverFooterMessage() {
      switch (this.mysqlDriverUiState) {
        case 'ready':
          return '';
        case 'checking':
          return this.$t('initialization.mysqlDriverChecking');
        case 'downloading':
          return this.mysqlDriverStatus.message || this.$t('initialization.mysqlDriverPreparing');
        case 'error':
          return this.mysqlDriverStatus.detailMessage || this.mysqlDriverStatus.message || this.$t('initialization.mysqlDriverDownloadRequired');
        default:
          return this.$t('initialization.mysqlDriverDownloadRequired');
      }
    },
    canTestDb() {
      return this.isMysqlDriverReady && !this.dbMissingFields.length;
    }
  },
  watch: {
    pageTitle: {
      immediate: true,
      handler(value) {
        document.title = value;
      }
    }
  },
  beforeUnmount() {
    this.clearTestDbRefreshTimer();
    this.disconnectInstallLogSocket();
    this.executionPhaseStatusType = '';
    this.executionPhaseStatusMessage = '';
  },
  async created() {
    await this.bootstrapPage();
  },
  methods: {
    connectInstallLogSocket() {
      if (this.installLogSocket) {
        return;
      }

      const socket = new ReconnectingWebSocket(buildInitInstallLogWsUrl(), [], {
        debug: false,
        reconnectInterval: 3000
      });

      socket.addEventListener('message', (event) => {
        this.handleInstallLogSocketMessage(event.data);
      });

      this.installLogSocket = socket;
    },

    disconnectInstallLogSocket() {
      if (!this.installLogSocket) {
        return;
      }

      this.installLogSocket.close();
      this.installLogSocket = null;
    },

    handleInstallLogSocketMessage(rawMessage) {
      try {
        const payload = JSON.parse(rawMessage);
        if (!payload || typeof payload !== 'object') {
          return;
        }

        if (payload.type === 'RESET') {
          this.executionScripts = resetExecutionScriptsForRetry(this.executionScripts);
          this.operationErrorDetail = '';
          if (this.isExecutionStep && this.applying) {
            this.applyPendingExecutionStatus();
          } else {
            this.executionPhaseStatusType = '';
            this.executionPhaseStatusMessage = '';
          }
          return;
        }

        if (payload.type === 'NOTICE') {
          this.applyInstallPhaseStatus(payload.object);
          return;
        }

        if (payload.type === 'SCRIPT_SNAPSHOT') {
          const snapshotItems = Array.isArray(payload.object) ? payload.object.map(normalizeExecutionScriptItem) : [];
          this.executionScripts = mergeExecutionScriptSnapshot(this.executionScripts, snapshotItems);
          return;
        }

        if (payload.type === 'SCRIPT_UPDATE') {
          this.executionScripts = upsertExecutionScriptItem(this.executionScripts, payload.object);
        }
      } catch (e) {
        console.error('Failed to parse install log message', e);
      }
    },

    applyInstallPhaseStatus(rawNotice) {
      const notice = normalizeInstallPhaseNotice(rawNotice);
      const meta = INSTALL_PHASE_NOTICE_META[notice.code];
      if (!meta) {
        return;
      }

      this.executionPhaseStatusType = ['success', 'info', 'warning', 'error'].includes(notice.level) ? notice.level : meta.level;

      // Handle mode-dependent title keys
      if (notice.code === 'DB_INIT') {
        this.executionPhaseStatusMessage = this.$t(this.isUpgradeMode ? 'initialization.noticeDbUpgradeTitle' : 'initialization.noticeDbInitTitle');
      } else if (notice.code === 'FIX_RUNNING') {
        this.executionPhaseStatusMessage = this.$t(this.isUpgradeMode ? 'initialization.noticeUpgradeTaskTitle' : 'initialization.noticeFixTitle');
      } else {
        this.executionPhaseStatusMessage = this.$t(meta.titleKey);
      }
    },

    applyPendingExecutionStatus() {
      this.executionPhaseStatusType = 'info';
      this.executionPhaseStatusMessage = this.$t(this.isUpgradeMode ? 'initialization.upgrading' : 'initialization.installing');
    },

    async loadExecutionScriptsPreview() {
      const payload = {
        'spring.datasource.jdbcurl': this.formValues['spring.datasource.jdbcurl'] || '',
        'spring.datasource.username': this.formValues['spring.datasource.username'] || '',
        'spring.datasource.password': this.formValues['spring.datasource.password'] || '',
        [INIT_DB_REBUILD_IF_NOT_EMPTY]: this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY] || ''
      };

      try {
        const res = await this.$services.dmInitPreviewScripts({ data: payload, modal: false });
        if (res.success && Array.isArray(res.data)) {
          this.executionScripts = res.data.map(normalizeExecutionScriptItem);
          return;
        }
      } catch (e) {
        console.error('Preview execution scripts failed', e);
      }

      this.executionScripts = (this.upgradeScripts || []).map(normalizeExecutionScriptItem);
    },

    async bootstrapPage() {
      this.mode = 'loading';
      try {
        const res = consumeDmBootstrapStatus() || (await this.$services.dmGlobalSettings());
        await this.applySystemStatus(res);
      } catch (e) {
        this.mode = 'dbError';
        this.errorMessage = 'Unable to connect to server';
      }
    },

    async applySystemStatus(res) {
      if (!res || !res.success) {
        this.mode = 'dbError';
        this.errorMessage = 'Unable to connect to server';
        return;
      }

      const { status, initReason, dbError, upgradeScripts = [] } = getDmSystemStatus(res);
      if (status === 'Ready') {
        redirectToLoginPage('manage');
        return;
      }

      if (initReason === 'dbConnectionError') {
        this.mode = 'dbError';
        this.errorMessage = dbError || 'Unknown database connection error';
        return;
      }

      this.workflowMode = status === 'Upgrade' ? 'upgrade' : 'initial';
      this.upgradeScripts = Array.isArray(upgradeScripts) ? upgradeScripts : [];
      const loaded = await this.loadFieldDefs();
      if (loaded && this.isUpgradeMode) {
        await this.loadExecutionScriptsPreview();
      }
      this.mode = loaded ? (this.isUpgradeMode ? 'upgrade' : 'full') : 'dbError';
      if (!loaded && !this.errorMessage) {
        this.errorMessage = 'Failed to load initialization config';
      }
    },

    async loadFieldDefs() {
      try {
        const res = await this.$services.dmInitDefaultConfig();
        if (res.success) {
          this.fieldDefs = res.data;
          const values = {};
          res.data.forEach((f) => {
            values[f.propertyKey] = f.defaultValue || '';
          });
          this.formValues = values;
          this.dbTestResult = null;
          this.dbMissingFields = [];
          this.securityMissingFields = [];
          this.mysqlDriverStatus = createInitialMysqlDriverStatus();
          this.executionScripts = [];
          this.operationErrorDetail = '';
          this.restartTimedOut = false;
          this.executionPhaseStatusType = '';
          this.executionPhaseStatusMessage = '';
          this.restartStatusType = '';
          this.restartStatusMessage = '';
          this.currentStep = 0;
          return true;
        }
        this.errorMessage = res.msg || 'Failed to load initialization config';
      } catch (e) {
        console.error('Failed to load field defs', e);
        this.errorMessage = 'Failed to load initialization config';
      }
      return false;
    },

    handleDbValidationChange(missingFields) {
      this.dbMissingFields = missingFields;
    },

    handleMysqlDriverStatusChange(status) {
      this.mysqlDriverStatus = {
        ...createInitialMysqlDriverStatus(),
        ...(status || {})
      };
      this.mysqlDriverStatus.uiState = resolveMysqlDriverUiState(this.mysqlDriverStatus.status);
      if (this.mysqlDriverStatus.uiState !== 'ready') {
        this.dbTestResult = null;
      }
    },

    handleSecurityValidationChange(missingFields) {
      this.securityMissingFields = missingFields;
    },

    updateFormValues(patch) {
      if (hasDbFieldChange(patch)) {
        this.clearTestDbRefreshTimer();
        this.rebuildConfirmInput = '';
        this.dbTestResult = null;
        this.formValues = {
          ...this.formValues,
          ...patch,
          [INIT_DB_CREATE_IF_MISSING]: 'false',
          [INIT_DB_REBUILD_IF_NOT_EMPTY]: ''
        };
        return;
      }

      if (Object.prototype.hasOwnProperty.call(patch, INIT_DB_REBUILD_IF_NOT_EMPTY) && patch[INIT_DB_REBUILD_IF_NOT_EMPTY] !== 'true') {
        this.rebuildConfirmInput = '';
      }

      this.formValues = { ...this.formValues, ...patch };

      if (Object.prototype.hasOwnProperty.call(patch, INIT_DB_REBUILD_IF_NOT_EMPTY) && this.dbTestResult && this.dbTestResult.showRebuildChoice) {
        this.scheduleTestDbRefresh();
      }
    },

    handleRebuildConfirmInput(event) {
      this.rebuildConfirmInput = event && event.target ? event.target.value : '';

      if (this.dbTestResult && this.dbTestResult.requireConfirmInput) {
        this.scheduleTestDbRefresh(250);
      }
    },

    clearTestDbRefreshTimer() {
      if (this.testDbRefreshTimer) {
        clearTimeout(this.testDbRefreshTimer);
        this.testDbRefreshTimer = null;
      }
    },

    scheduleTestDbRefresh(delay = 0) {
      this.clearTestDbRefreshTimer();
      this.testDbRefreshTimer = setTimeout(() => {
        this.testDbRefreshTimer = null;
        if (!this.canTestDb) {
          return;
        }
        this.handleTestDb();
      }, delay);
    },

    async handleTestDb() {
      if (this.testingDb) {
        return;
      }

      if (this.dbMissingFields.length) {
        this.dbTestResult = null;
        return;
      }

      if (!this.isMysqlDriverReady) {
        this.dbTestResult = null;
        return;
      }

      const params = {
        'spring.datasource.jdbcurl': this.formValues['spring.datasource.jdbcurl'],
        'spring.datasource.username': this.formValues['spring.datasource.username'],
        'spring.datasource.password': this.formValues['spring.datasource.password'],
        [INIT_DB_REBUILD_IF_NOT_EMPTY]: this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY] || '',
        [INIT_DB_CONFIRM_DATABASE_NAME]: this.rebuildConfirmInput.trim()
      };
      this.testingDb = true;
      await this.$nextTick();
      try {
        const res = await this.$services.dmInitTestDb({ data: params });
        if (res.success) {
          const nextRebuildValue =
            res.data && res.data.showRebuildChoice
              ? ['true', 'false'].includes(this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY])
                ? this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY]
                : ''
              : 'false';

          if (nextRebuildValue !== 'true') {
            this.rebuildConfirmInput = '';
          }

          this.dbTestResult = res.data;
          this.formValues = {
            ...this.formValues,
            [INIT_DB_CREATE_IF_MISSING]: res.data && res.data.createDatabase ? 'true' : 'false',
            [INIT_DB_REBUILD_IF_NOT_EMPTY]: nextRebuildValue
          };
        }
      } catch (e) {
        console.error('Test DB failed', e);
      } finally {
        this.testingDb = false;
      }
    },

    async handleRetry() {
      await this.bootstrapPage();
    },

    async handleReconfigureDatabase() {
      this.workflowMode = 'initial';
      this.upgradeScripts = [];
      this.executionScripts = [];
      this.operationErrorDetail = '';
      this.mode = 'loading';
      this.errorMessage = '';
      const loaded = await this.loadFieldDefs();
      this.mode = loaded ? 'full' : 'dbError';
    },

    async nextStep() {
      if (this.currentStep === 0 && !this.isMysqlDriverReady) {
        return;
      }

      const nextStepIndex = Math.min(this.currentStep + 1, this.stageItems.length - 1);
      if (nextStepIndex === this.confirmStepIndex) {
        await this.loadExecutionScriptsPreview();
      }
      this.currentStep = nextStepIndex;
    },

    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      }
    },

    stageState(index) {
      if (index < this.currentStep) {
        return 'completed';
      }
      if (index === this.currentStep) {
        return 'active';
      }
      return 'upcoming';
    },

    handleConfirmAction() {
      return this.startExecution();
    },

    async startExecution() {
      if (!this.isMysqlDriverReady) {
        return;
      }

      this.currentStep = this.executionStepIndex;
      this.applyPendingExecutionStatus();
      await this.$nextTick();

      if (!this.executionScripts.length) {
        await this.loadExecutionScriptsPreview();
      }

      return this.handleApply();
    },

    handleExecutionStageAction() {
      if (this.restartTimedOut) {
        window.location.reload();
        return;
      }

      if (this.restartStatusType === 'error') {
        return this.retryExecutionOnCurrentStep();
      }
    },

    async retryExecutionOnCurrentStep() {
      if (!this.executionScripts.length) {
        await this.loadExecutionScriptsPreview();
      }

      this.restartTimedOut = false;
      this.executionPhaseStatusType = '';
      this.executionPhaseStatusMessage = '';
      this.restartStatusType = '';
      this.restartStatusMessage = '';
      this.operationErrorDetail = '';
      this.applying = false;

      return this.handleApply({ omitRebuild: true });
    },

    buildExecutionPayload({ omitRebuild = false } = {}) {
      const payload = { ...this.formValues };
      payload[INIT_WORKFLOW_MODE_KEY] = this.workflowMode;
      if (omitRebuild) {
        delete payload[INIT_DB_REBUILD_IF_NOT_EMPTY];
      }
      return payload;
    },

    async handleApply(options = {}) {
      this.applying = true;
      this.restartTimedOut = false;
      this.applyPendingExecutionStatus();
      this.restartStatusType = this.isUpgradeMode ? 'info' : '';
      this.restartStatusMessage = this.isUpgradeMode ? this.$t('initialization.upgrading') : '';
      this.executionScripts = resetExecutionScriptsForRetry(this.executionScripts);
      this.operationErrorDetail = '';
      this.connectInstallLogSocket();
      try {
        const payload = this.buildExecutionPayload(options);

        const res = await this.$services.dmInitApplyConfig({ data: payload, modal: false });
        if (res.success) {
          this.disconnectInstallLogSocket();
          this.executionPhaseStatusType = '';
          this.executionPhaseStatusMessage = '';
          this.restartStatusType = this.isUpgradeMode ? 'success' : 'info';
          this.restartStatusMessage = this.isUpgradeMode ? this.$t('initialization.upgradeSuccessRestarting') : this.$t('initialization.restarting');
          await this.waitForRestart();
          return;
        }

        this.executionPhaseStatusType = '';
        this.executionPhaseStatusMessage = '';
        this.restartStatusType = 'error';
        this.restartStatusMessage = this.isUpgradeMode ? this.$t('initialization.upgradeFailed') : this.$t('initialization.installFailed');
        this.operationErrorDetail = res.msg || '';
        this.applying = false;
        this.disconnectInstallLogSocket();
      } catch (e) {
        console.error('Apply config failed', e);
        this.executionPhaseStatusType = '';
        this.executionPhaseStatusMessage = '';
        this.restartStatusType = 'error';
        this.restartStatusMessage = this.isUpgradeMode ? this.$t('initialization.upgradeFailed') : this.$t('initialization.installFailed');
        this.operationErrorDetail = e && e.message ? e.message : this.isUpgradeMode ? 'Upgrade failed' : 'Install failed';
        this.applying = false;
        this.disconnectInstallLogSocket();
      }
    },

    async waitForRestart() {
      const maxRetries = 60;
      for (let i = 0; i < maxRetries; i++) {
        await sleep(2000);
        let res = null;
        try {
          res = await pollDmGlobalSettings();
        } catch (e) {
          // The service is expected to refuse connections while restarting.
        }

        if (isDmSystemReady(res)) {
          redirectToLoginPage('manage');
          return;
        }

        this.restartStatusType = 'info';
        this.restartStatusMessage = this.$t('initialization.restarting');
      }

      this.restartStatusType = 'error';
      this.restartStatusMessage = this.$t('initialization.restartTimeout');
      this.restartTimedOut = true;
      this.applying = false;
      this.disconnectInstallLogSocket();
    }
  }
};
</script>

<style scoped>
.initialization {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  box-sizing: border-box;
  background: #f0f2f5;
}

.init-error-page {
  width: 100%;
  max-width: 560px;
}

.init-loading-page {
  width: 100%;
  max-width: 560px;
}

.loading-card {
  background: #fff;
  border-radius: 8px;
  padding: 48px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.loading-title {
  margin: 0 0 16px;
  font-size: 32px;
  line-height: 40px;
  font-weight: 600;
  color: #1f1f1f;
}

.loading-text {
  margin: 0;
  font-size: 14px;
  line-height: 22px;
  color: rgba(0, 0, 0, 0.65);
}

.error-card {
  background: #fff;
  border-radius: 8px;
  padding: 48px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.error-title {
  margin: 0 0 16px;
  font-size: 32px;
  line-height: 40px;
  font-weight: 600;
  color: #1f1f1f;
}

.error-detail {
  margin: 24px 0;
  text-align: left;
}

.error-detail p {
  margin: 0 0 3px;
}

.error-message {
  margin: 0;
  background: #fff2f0;
  border: 1px solid #ffccc7;
  border-radius: 4px;
  padding: 12px;
  font-size: 13px;
  color: #cf1322;
  white-space: pre-wrap;
  word-break: break-all;
}

.error-actions {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: center;
}

.init-wizard {
  width: 100%;
  max-width: 720px;
  background: #fff;
  border-radius: 8px;
  padding: 32px;
  height: min(920px, calc(100vh - 48px));
  max-height: calc(100vh - 48px);
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.wizard-header {
  flex: 0 0 auto;
  text-align: center;
  margin-bottom: 24px;
}

.wizard-header h1 {
  margin: 0;
  font-size: 32px;
  line-height: 40px;
  font-weight: 600;
  color: #1f1f1f;
}

.wizard-stage-progress {
  margin-top: 24px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.wizard-stage-item {
  position: relative;
  flex: 1 1 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #8c8c8c;
}

.wizard-stage-marker {
  position: relative;
  z-index: 1;
  width: 34px;
  height: 34px;
  border-radius: 50%;
  border: 1px solid #d9d9d9;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.wizard-stage-index {
  line-height: 1;
}

.wizard-stage-label {
  font-size: 13px;
  line-height: 20px;
}

.wizard-stage-line {
  position: absolute;
  top: 16px;
  left: calc(50% + 24px);
  width: calc(100% - 48px);
  height: 1px;
  background: #d9d9d9;
}

.wizard-stage-item.completed,
.wizard-stage-item.active {
  color: #1677ff;
}

.wizard-stage-item.completed .wizard-stage-marker,
.wizard-stage-item.active .wizard-stage-marker {
  border-color: #1677ff;
}

.wizard-stage-item.completed .wizard-stage-marker {
  background: #1677ff;
  color: #fff;
}

.wizard-stage-item.completed .wizard-stage-line {
  background: #1677ff;
}

.wizard-content {
  flex: 1 1 auto;
  min-height: 0;
  overflow: hidden;
}

.step-panel {
  height: 100%;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 4px;
  box-sizing: border-box;
}

.wizard-footer {
  flex: 0 0 auto;
  margin-top: 20px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
}

.button-inline-spinner {
  display: inline-block;
  width: 14px;
  height: 14px;
  margin-right: 8px;
  vertical-align: -2px;
  border-radius: 50%;
  border: 2px solid rgba(0, 0, 0, 0.18);
  border-top-color: #1677ff;
  animation: buttonInlineSpin 0.8s linear infinite;
}

@keyframes buttonInlineSpin {
  to {
    transform: rotate(360deg);
  }
}

.wizard-footer-message {
  flex: 1;
  min-width: 0;
  font-size: 13px;
  text-align: left;
}

.wizard-footer-message.success {
  color: #52c41a;
}

.wizard-footer-message.error {
  color: #ff4d4f;
}

.wizard-footer-message.warning {
  color: #d48806;
}

.wizard-footer-message.info {
  color: #1677ff;
}

.wizard-footer-message-secondary {
  margin-left: 8px;
  color: #1677ff;
}

.warning-text {
  color: #d48806;
}

.warning-confirm-label {
  margin-left: 4px;
  color: #cf1322;
  font-weight: 700;
}

.warning-confirm-input {
  min-width: 120px;
  margin-left: 4px;
  padding: 0 4px 2px;
  border: none;
  border-bottom: 1px dotted #cf1322;
  border-radius: 0;
  outline: none;
  background: transparent;
  color: #1f1f1f;
}

.warning-confirm-input::placeholder {
  color: rgba(0, 0, 0, 0.35);
}

.wizard-footer-actions {
  margin-left: auto;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.wizard-next-button[disabled],
.wizard-next-button[disabled]:hover,
.wizard-next-button[disabled]:focus,
.wizard-next-button[disabled]:active,
.wizard-next-button.ant-btn-disabled,
.wizard-next-button.ant-btn-disabled:hover,
.wizard-next-button.ant-btn-disabled:focus,
.wizard-next-button.ant-btn-disabled:active {
  color: rgba(0, 0, 0, 0.25);
  background: #f5f5f5;
  border-color: #d9d9d9;
  box-shadow: none;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .initialization {
    padding: 0;
  }

  .init-wizard {
    height: 100vh;
    max-height: 100vh;
    padding: 32px 16px;
    border-radius: 0;
  }

  .wizard-stage-progress {
    flex-wrap: nowrap;
    gap: 4px;
  }

  .wizard-stage-item {
    flex: 1 1 0;
    min-width: 0;
    gap: 6px;
  }

  .wizard-stage-marker {
    width: 28px;
    height: 28px;
    font-size: 12px;
  }

  .wizard-stage-label {
    max-width: 100%;
    font-size: 12px;
    line-height: 16px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .wizard-stage-line {
    display: none;
  }

  .wizard-footer {
    flex-direction: column;
    align-items: stretch;
  }

  .wizard-footer-actions {
    margin-left: 0;
  }
}
</style>
