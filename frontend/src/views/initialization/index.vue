<template>
  <div class="initialization" :style="{ '--init-bg-pattern': `url(${backgroundPatternUrl})` }">
    <div class="init-left">
      <header class="init-topbar">
        <div class="init-header">
          <dm-logo-header :title="wizardProductTitle" />
        </div>
      </header>

      <main class="init-shell">
        <section class="init-hero">
          <LoginHero />
        </section>
      </main>

      <footer class="login-bottombar">
        <dm-footer />
      </footer>
    </div>

    <section class="init-workspace">
      <div v-if="mode === 'loading'" class="init-loading-page">
        <div class="loading-card init-page-card">
          <div class="wizard-title-block init-page-title-block">
            <h1 class="wizard-product-title">{{ wizardProductTitle }}</h1>
          </div>
          <p class="loading-text">{{ $t('initialization.loading') }}</p>
        </div>
      </div>

      <!-- Error Page Mode -->
      <div v-else-if="mode === 'dbError'" class="init-error-page">
        <div class="error-card init-page-card">
          <div class="wizard-title-block init-page-title-block">
            <h1 class="wizard-product-title">{{ wizardProductTitle }}</h1>
          </div>
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

      <!-- Initialise Wizard Mode -->
      <div v-else class="init-wizard">
        <div class="wizard-header">
          <div class="wizard-title-block">
            <h1 class="wizard-product-title">{{ wizardProductTitle }}</h1>
          </div>
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
          <!-- Step 0: Database Configuration -->
          <div v-show="!isUpgradeMode && currentStep === 0" class="step-panel">
            <StepDb
              :fieldDefs="dbFields"
              :formValues="formValues"
              :dbTestResult="dbTestResult"
              :readonly="isDbFormReadonly"
              :showTestButton="!isUpgradeMode"
              :testingDb="testingDb"
              @update:formValues="updateFormValues"
              @validation-change="handleDbValidationChange"
              @test-db="handleTestDb"
            />
          </div>

          <!-- Step 1: Secure Configuration -->
          <div v-show="!isUpgradeMode && currentStep === 1" class="step-panel">
            <StepSecurity
              :fieldDefs="securityFields"
              :formValues="formValues"
              @update:formValues="updateFormValues"
              @validation-change="handleSecurityValidationChange"
            />
          </div>

          <!-- Step 2: Connectivity Configuration -->
          <div v-show="hasConnectivityStep && currentStep === connectivityStepIndex" class="step-panel">
            <StepConnectivity
              :fieldDefs="connectivityFields"
              :formValues="formValues"
              :readonly="isConnectivityReadonly"
              @update:formValues="updateFormValues"
            />
          </div>

          <!-- Identification of steps -->
          <div v-show="isConfirmStep" class="step-panel">
            <StepConfirm
              :fieldDefs="visibleFieldDefs"
              :formValues="formValues"
              :mode="mode"
              :workflowMode="workflowMode"
              @update:formValues="updateFormValues"
            />
          </div>

          <div v-show="isExecutionStep" class="step-panel">
            <StepExecution
              :executionScripts="executionScripts"
              :operationErrorDetail="operationErrorDetail"
              :executionMessage="currentExecutionMessage"
            />
          </div>
        </div>

        <div class="wizard-footer">
          <div class="wizard-footer-actions">
            <a-button v-if="showPrevButton" @click="prevStep">{{ $t('initialization.prev') }}</a-button>
            <a-button v-if="showNextButton" class="wizard-next-button" type="primary" @click="nextStep">
              {{ $t('initialization.next') }}
            </a-button>
            <a-button v-if="isConfirmStep" type="primary" :loading="applying" @click="handleConfirmAction">{{ confirmActionLabel }}</a-button>
            <a-button v-if="showExecutionActionButton" type="primary" :loading="applying" @click="handleExecutionStageAction">
              {{ executionActionLabel }}
            </a-button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import ReconnectingWebSocket from 'reconnecting-websocket';
import StepDb from './StepDb.vue';
import StepSecurity from './StepSecurity.vue';
import StepConnectivity from './StepConnectivity.vue';
import StepConfirm from './StepConfirm.vue';
import StepExecution from './StepExecution.vue';
import DmFooter from '@/components/DmFooter';
import DmLogoHeader from '@/components/DmLogoHeader';
import LoginHero from '@/views/login/LoginHero.vue';
import loginBgPattern from '@/assets/login/login-bg-pattern.svg';
import { consumeDmBootstrapStatus, getDmSystemStatus, isDmSystemReady } from '../../utils/dmGlobalSettings';

const INIT_DB_CREATE_IF_MISSING = 'clougence.init.db.createIfMissing';
const INIT_WORKFLOW_MODE_KEY = 'clougence.init.workflowMode';
const ALONE_HIDDEN_FIELD_KEYS = new Set(['server.port', 'clouddm.rsocket.dns', 'clouddm.rsocket.console.port']);
const INSTALL_PHASE_NOTICE_META = {
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
const REDIRECT_HOME_DELAY_MS = 2500;

function buildDmGlobalSettingsUrl() {
  const baseUrl = (process.env.VUE_APP_BASE_URL || '').replace(/\/$/, '');
  return `${baseUrl}/clouddm/console/api/v1/dmGlobalSettings`;
}

function buildInitInstallLogWsUrl() {
  const explicitBase = (process.env.VUE_APP_BASE_URL || '').trim();
  const fallbackOrigin = window.location.origin;
  const baseUrl = explicitBase || fallbackOrigin;
  const parsed = new URL(baseUrl, fallbackOrigin);
  const wsProtocol = parsed.protocol === 'https:' ? 'wss:' : 'ws:';
  return `${wsProtocol}//${parsed.host}/clouddm/console/api/v1/init/ws/install-log`;
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

function redirectToHomePage() {
  window.location.replace(`${window.location.origin}${window.location.pathname}#/`);
}

export default {
  name: 'Initialization',
  components: { DmFooter, DmLogoHeader, LoginHero, StepDb, StepSecurity, StepConnectivity, StepConfirm, StepExecution },
  data() {
    return {
      mode: 'loading', // 'loading' | 'full' | 'upgrade' | 'dbError'
      workflowMode: 'initial',
      errorMessage: '',
      fieldDefs: [],
      formValues: {},
      dbTestResult: null,
      dbMissingFields: [],
      securityMissingFields: [],
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
      restartStatusMessage: '',
      aloneMode: false
    };
  },
  computed: {
    backgroundPatternUrl() {
      return loginBgPattern;
    },
    wizardProductTitle() {
      return this.isUpgradeMode ? this.$t('initialization.productUpgradeTitle') : this.$t('initialization.productInitTitle');
    },
    dbFields() {
      return this.fieldDefs.filter((f) => f.category === 'database');
    },
    securityFields() {
      return this.fieldDefs.filter((f) => f.category === 'security');
    },
    connectivityFields() {
      return this.fieldDefs.filter((f) => f.category === 'connectivity' && !this.isAloneHiddenField(f));
    },
    visibleFieldDefs() {
      return this.fieldDefs.filter((field) => !this.isAloneHiddenField(field));
    },
    isUpgradeMode() {
      return this.workflowMode === 'upgrade';
    },
    hasConnectivityStep() {
      return !this.isUpgradeMode && !this.aloneMode;
    },
    pageTitle() {
      return this.isUpgradeMode ? this.$t('initialization.upgradeTitle') : this.$t('initialization.title');
    },
    stageItems() {
      if (this.isUpgradeMode) {
        return [
          { key: 'confirm', label: this.$t('initialization.stage.confirm') },
          { key: 'execute', label: this.$t('initialization.upgradeAction') }
        ];
      }

      return [
        { key: 'db', label: this.$t('initialization.stage.db') },
        { key: 'security', label: this.$t('initialization.stage.security') },
        ...(this.hasConnectivityStep ? [{ key: 'connectivity', label: this.$t('initialization.stage.connectivity') }] : []),
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
      return this.stageItems.findIndex((stage) => stage.key === 'connectivity');
    },
    confirmStepIndex() {
      return this.stageItems.length - 2;
    },
    executionStepIndex() {
      return this.stageItems.length - 1;
    },
    currentExecutionMessage() {
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

      return null;
    },
    canNext() {
      if (this.currentStep === 0) {
        if (this.isUpgradeMode) {
          return true;
        }

        return !this.dbMissingFields.length && Boolean(this.dbTestResult && this.dbTestResult.canProceed);
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
        return this.$t('initialization.applyConfig');
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
    isDbFormReadonly() {
      return this.isUpgradeMode;
    },
    isConnectivityReadonly() {
      return this.aloneMode;
    },
    canTestDb() {
      return !this.dbMissingFields.length;
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
        'spring.datasource.password': this.formValues['spring.datasource.password'] || ''
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

      this.aloneMode = Boolean(res.data && res.data.aloneMode);
      const { status, initReason, dbError, upgradeScripts = [] } = getDmSystemStatus(res);
      if (status === 'Ready') {
        redirectToHomePage();
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

    handleSecurityValidationChange(missingFields) {
      this.securityMissingFields = missingFields;
    },

    isAloneHiddenField(field) {
      return Boolean(this.aloneMode && field && ALONE_HIDDEN_FIELD_KEYS.has(field.propertyKey));
    },

    updateFormValues(patch) {
      if (hasDbFieldChange(patch)) {
        const shouldPreserveCreateIfMissing = this.isConfirmStep && Object.prototype.hasOwnProperty.call(this.formValues, INIT_DB_CREATE_IF_MISSING);
        this.dbTestResult = null;
        this.executionScripts = [];
        this.formValues = {
          ...this.formValues,
          ...patch,
          [INIT_DB_CREATE_IF_MISSING]: shouldPreserveCreateIfMissing ? this.formValues[INIT_DB_CREATE_IF_MISSING] : 'false'
        };
        return;
      }

      this.formValues = { ...this.formValues, ...patch };
    },

    showDbTestToast(result) {
      if (!result) {
        return;
      }

      const type = `${result.messageType || (result.success ? 'success' : 'error')}`.toLowerCase();
      const fallbackMessage = type === 'success' ? this.$t('ce-shi-lian-jie-cheng-gong') : this.$t('ce-shi-lian-jie-shi-bai');
      const message = `${result.message || fallbackMessage}`.trim();
      if (!message) {
        return;
      }

      if (type === 'success') {
        this.$message.success(message);
        return;
      }

      if (type === 'warning') {
        this.$message.warning(message);
        return;
      }

      this.$message.error(message);
    },

    async handleTestDb() {
      if (this.testingDb) {
        return;
      }

      if (this.dbMissingFields.length) {
        this.dbTestResult = null;
        this.$message.error(this.formatDbMissingFieldsMessage());
        return;
      }

      const params = {
        'spring.datasource.jdbcurl': this.formValues['spring.datasource.jdbcurl'],
        'spring.datasource.username': this.formValues['spring.datasource.username'],
        'spring.datasource.password': this.formValues['spring.datasource.password']
      };
      this.testingDb = true;
      await this.$nextTick();
      try {
        const res = await this.$services.dmInitTestDb({ data: params });
        if (res.success) {
          this.dbTestResult = res.data;
          this.showDbTestToast(res.data);
          this.formValues = {
            ...this.formValues,
            [INIT_DB_CREATE_IF_MISSING]: res.data && res.data.createDatabase ? 'true' : 'false'
          };
        } else {
          this.$message.error(res.msg || this.$t('ce-shi-lian-jie-shi-bai'));
        }
      } catch (e) {
        this.$message.error(this.$t('ce-shi-lian-jie-shi-bai'));
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

    formatDbMissingFieldsMessage() {
      return `${this.$t('initialization.dbFormIncomplete')}：${this.dbMissingFields.join('、')}`;
    },

    formatSecurityMissingFieldsMessage() {
      return `${this.$t('initialization.securityFormIncomplete')}：${this.securityMissingFields.join('、')}`;
    },

    getNextStepBlockedMessage() {
      if (this.currentStep === 0) {
        if (this.isUpgradeMode) {
          return '';
        }

        if (this.dbMissingFields.length) {
          return this.formatDbMissingFieldsMessage();
        }

        if (!this.dbTestResult) {
          return this.$t('initialization.dbTestRequired');
        }

        if (!this.dbTestResult.canProceed) {
          return this.dbTestResult.message || this.$t('initialization.dbTestNotPassed');
        }
      }

      if (!this.isUpgradeMode && this.currentStep === 1 && this.securityMissingFields.length) {
        return this.formatSecurityMissingFieldsMessage();
      }

      return '';
    },

    getConfirmBlockedMessage() {
      if (this.isUpgradeMode) {
        return '';
      }

      if (this.dbMissingFields.length) {
        return this.formatDbMissingFieldsMessage();
      }

      if (this.securityMissingFields.length) {
        return this.formatSecurityMissingFieldsMessage();
      }

      if (!this.dbTestResult) {
        return this.$t('initialization.dbTestRequired');
      }

      if (!this.dbTestResult.canProceed) {
        return this.dbTestResult.message || this.$t('initialization.dbTestNotPassed');
      }

      return '';
    },

    async nextStep() {
      const blockedMessage = this.getNextStepBlockedMessage();
      if (blockedMessage) {
        this.$message.error(blockedMessage);
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
      const blockedMessage = this.getConfirmBlockedMessage();
      if (blockedMessage) {
        this.$message.error(blockedMessage);
        return;
      }

      return this.startExecution();
    },

    async startExecution() {
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

      return this.handleApply();
    },

    buildExecutionPayload() {
      const payload = { ...this.formValues };
      payload[INIT_WORKFLOW_MODE_KEY] = this.workflowMode;
      return payload;
    },

    async handleApply() {
      this.applying = true;
      this.restartTimedOut = false;
      this.applyPendingExecutionStatus();
      this.restartStatusType = this.isUpgradeMode ? 'info' : '';
      this.restartStatusMessage = this.isUpgradeMode ? this.$t('initialization.upgrading') : '';
      this.executionScripts = resetExecutionScriptsForRetry(this.executionScripts);
      this.operationErrorDetail = '';
      this.connectInstallLogSocket();
      try {
        const payload = this.buildExecutionPayload();

        const res = await this.$services.dmInitApplyConfig({ data: payload, modal: false });
        if (res.success) {
          this.disconnectInstallLogSocket();
          this.executionPhaseStatusType = '';
          this.executionPhaseStatusMessage = '';
          this.restartStatusType = this.isUpgradeMode ? 'success' : 'info';
          this.restartStatusMessage = this.$t('initialization.restarting');
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
          this.restartStatusType = 'info';
          this.restartStatusMessage = this.$t('initialization.redirectingHome');
          await sleep(REDIRECT_HOME_DELAY_MS);
          redirectToHomePage();
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
  --init-ink: #171717;
  --init-body: #333840;
  --init-muted: #707070;
  --init-hairline: #dfdfdf;
  --init-canvas: #f8fafc;
  --init-emerald: #3ecf8e;
  --init-emerald-deep: #24b47e;
  --init-panel-width: min(760px, 56vw);

  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background-color: var(--init-canvas);
  background-image: var(--init-bg-pattern);
  background-position: center;
  background-size: cover;
}

.init-left {
  position: relative;
  display: flex;
  flex: 1 0 auto;
  flex-direction: column;
  min-width: 0;
  min-height: 100vh;
}

.init-topbar {
  position: relative;
  z-index: 3;
  flex: 0 0 72px;
}

.init-header {
  position: relative;
  display: block;
  height: 72px;
  padding: 0 32px;
}

.init-shell {
  flex: 1 1 auto;
  min-height: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: auto;
  padding-right: var(--init-panel-width);
  box-sizing: border-box;
}

.login-bottombar {
  position: relative;
  flex: 0 0 auto;
  padding: 0 32px 24px;
  padding-right: calc(var(--init-panel-width) + 32px);
  box-sizing: border-box;
}

.login-bottombar :deep(.footer) {
  height: auto;
  line-height: 1.5;
  text-align: center;
}

.init-hero {
  width: 100%;
  min-width: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 48px 32px;
  box-sizing: border-box;
}

.init-hero :deep(.login-hero-panel) {
  max-width: 560px;
}

.init-hero :deep(.hero-capabilities) {
  display: none;
}

.init-workspace {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  z-index: 2;
  width: var(--init-panel-width);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: auto;
  padding: 80px 48px 40px;
  border-left: 1px solid var(--init-hairline);
  background: #fff;
  box-sizing: border-box;
}

.init-error-page,
.init-loading-page {
  width: 100%;
  max-width: 640px;
}

.init-page-card {
  position: relative;
  padding: 0;
  border: 0;
  border-radius: 0;
  background: transparent;
  text-align: left;
  box-shadow: none;
}

.init-page-title-block {
  gap: 0;
}

.loading-text {
  margin: 20px 0 0;
  font-size: 14px;
  line-height: 22px;
  color: var(--init-muted);
}

.error-detail {
  margin: 24px 0 0;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background: #fff;
  overflow: hidden;
  text-align: left;
}

.error-detail p {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  padding: 10px 14px;
  border-bottom: 1px solid #eef0f3;
  background: #f8fafc;
  color: #344054;
  font-size: 13px;
  font-weight: 600;
  line-height: 20px;
}

.error-detail p::before {
  content: '';
  width: 6px;
  height: 6px;
  flex: 0 0 auto;
  border-radius: 50%;
  background: #cf1322;
  box-shadow: 0 0 0 3px rgba(207, 19, 34, 0.08);
}

.error-message {
  margin: 0;
  max-height: 180px;
  overflow: auto;
  border: 0;
  border-radius: 0;
  background: #fcfcfd;
  padding: 14px 16px;
  color: #344054;
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 12px;
  line-height: 18px;
  white-space: pre-wrap;
  word-break: break-word;
}

.error-message::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.error-message::-webkit-scrollbar-thumb {
  border: 2px solid #fcfcfd;
  border-radius: 999px;
  background: #cfd6e0;
}

.error-message::-webkit-scrollbar-track {
  background: #fcfcfd;
}

.error-actions {
  margin-top: 24px;
  display: flex;
  gap: 12px;
  justify-content: flex-end;
}

.init-wizard {
  position: relative;
  width: 100%;
  max-width: 640px;
  min-height: 560px;
  padding: 0;
  border: 0;
  border-radius: 0;
  background: transparent;
  max-height: calc(100vh - 120px);
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  box-shadow: none;
}

.wizard-header {
  flex: 0 0 auto;
  text-align: center;
  margin-bottom: 22px;
}

.wizard-title-block {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0;
}

.wizard-product-title {
  width: 100%;
  margin: 0;
  color: var(--init-ink);
  font-size: 28px;
  font-weight: 500;
  line-height: 36px;
  text-align: center;
}

.wizard-stage-progress {
  margin-top: 28px;
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
  gap: 6px;
  color: #8f949b;
}

.wizard-stage-marker {
  position: relative;
  z-index: 1;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  border: 1px solid var(--init-hairline);
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
  text-align: center;
}

.wizard-stage-line {
  position: absolute;
  top: 14px;
  left: calc(50% + 22px);
  width: calc(100% - 44px);
  height: 1px;
  background: var(--init-hairline);
}

.wizard-stage-item.completed,
.wizard-stage-item.active {
  color: var(--init-emerald-deep);
}

.wizard-stage-item.completed .wizard-stage-marker,
.wizard-stage-item.active .wizard-stage-marker {
  border-color: var(--init-emerald);
  background: var(--init-emerald);
}

.wizard-stage-item.completed .wizard-stage-marker .wizard-stage-index,
.wizard-stage-item.active .wizard-stage-marker .wizard-stage-index {
  color: #fff;
}

.wizard-stage-item.completed .wizard-stage-line {
  background: var(--init-emerald);
}

.wizard-content {
  flex: 1 1 auto;
  min-height: 0;
  overflow: hidden;
}

.step-panel {
  max-height: calc(100dvh - 292px);
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding-right: 4px;
  box-sizing: border-box;
}

.step-panel::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.step-panel::-webkit-scrollbar-thumb {
  border: 2px solid #fff;
  border-radius: 999px;
  background: #cfd6e0;
}

.step-panel::-webkit-scrollbar-track {
  background: #fff;
}

.wizard-footer {
  flex: 0 0 auto;
  margin-top: 16px;
  padding-top: 14px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: stretch;
  gap: 10px;
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
  margin-left: 0;
  display: flex;
  justify-content: center;
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
  color: #ffffff;
  background: #f5f5f5;
  border-color: #d9d9d9;
  box-shadow: none;
  cursor: not-allowed;
}

.initialization :deep(.ant-btn-primary),
.initialization :deep(.ant-btn-primary:hover),
.initialization :deep(.ant-btn-primary:focus),
.initialization :deep(.ant-btn-primary:active),
.initialization :deep(.ant-btn-primary[disabled]),
.initialization :deep(.ant-btn-primary.ant-btn-disabled),
.initialization :deep(.ant-btn-primary > span),
.initialization :deep(.ant-btn-primary[disabled] > span),
.initialization :deep(.ant-btn-primary.ant-btn-disabled > span) {
  color: #ffffff;
}

@media (max-width: 1180px) {
  .initialization {
    overflow: auto;
    background-image: none;
  }

  .init-left {
    display: contents;
    min-height: auto;
  }

  .init-topbar {
    order: 1;
  }

  .init-shell {
    display: none;
    padding-right: 0;
  }

  .init-hero {
    display: none;
  }

  .init-workspace {
    order: 2;
    position: relative;
    top: auto;
    right: auto;
    bottom: auto;
    z-index: 1;
    width: 100%;
    min-height: calc(100vh - 72px);
    padding: 40px 32px;
    border-left: 0;
    overflow: visible;
  }

  .login-bottombar {
    order: 3;
    padding-right: 32px;
  }

  .init-wizard,
  .init-error-page,
  .init-loading-page {
    max-width: 760px;
  }

  .init-wizard {
    max-height: none;
  }
}

@media (max-width: 768px) {
  .init-header {
    padding: 0 16px;
  }

  .init-workspace {
    align-items: flex-start;
    min-height: calc(100vh - 72px);
    padding: 32px 24px 24px;
  }

  .login-bottombar {
    padding: 0 16px 20px;
  }

  .init-wizard {
    max-height: none;
    min-height: auto;
    padding: 0;
    border-radius: 0;
  }

  .init-page-card {
    padding: 0;
    border-radius: 0;
  }

  .wizard-header {
    margin-bottom: 16px;
    text-align: center;
  }

  .wizard-title-block {
    align-items: center;
    gap: 0;
  }

  .wizard-product-title {
    font-size: 26px;
    line-height: 34px;
  }

  .wizard-stage-progress {
    margin-top: 22px;
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

  .wizard-content {
    overflow: visible;
  }

  .step-panel {
    max-height: none;
    overflow: visible;
    padding-right: 0;
  }

  .wizard-footer {
    flex-direction: column;
    align-items: stretch;
  }

  .wizard-footer-actions {
    margin-left: 0;
    flex-wrap: wrap;
  }
}
</style>
