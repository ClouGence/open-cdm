<template>
  <div class="initialization">
    <div v-if="mode === 'loading'" class="init-loading-page">
      <div class="loading-card">
        <h2 class="loading-title">{{ $t('initialization.title') }}</h2>
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
          <a-button @click="handleUpdateDbConfig">{{ $t('initialization.updateDbConfig') }}</a-button>
        </div>
      </div>
    </div>

    <!-- 初始化向导模式 -->
    <div v-else class="init-wizard">
      <div class="wizard-header">
        <h1>{{ $t('initialization.title') }}</h1>
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
            @update:formValues="updateFormValues"
            @validation-change="handleDbValidationChange"
          />
        </div>

        <!-- Step 1: 安全配置 -->
        <div v-show="currentStep === 1" class="step-panel">
          <StepSecurity
            :fieldDefs="securityFields"
            :formValues="formValues"
            @update:formValues="updateFormValues"
            @validation-change="handleSecurityValidationChange"
          />
        </div>

        <!-- Step 2: 连接性配置 -->
        <div v-show="currentStep === connectivityStepIndex" class="step-panel">
          <StepConnectivity :fieldDefs="connectivityFields" :formValues="formValues" @update:formValues="updateFormValues" />
        </div>

        <!-- 确认步骤 -->
        <div v-show="isConfirmStep" class="step-panel">
          <StepConfirm :fieldDefs="fieldDefs" :formValues="formValues" :dbTestResult="dbTestResult" :mode="mode" />
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
            {{ currentFooterMessage.message }}
          </template>
        </div>
        <div class="wizard-footer-actions">
          <a-button v-if="currentStep > 0" @click="prevStep">{{ $t('initialization.prev') }}</a-button>
          <a-button v-if="currentStep === 0" :disabled="testingDb" @click="handleTestDb">
            <span v-if="testingDb" class="button-inline-spinner" aria-hidden="true"></span>
            <span>{{ $t('initialization.testConnection') }}</span>
          </a-button>
          <a-button v-if="!isConfirmStep" type="primary" :disabled="!canNext" @click="nextStep">{{ $t('initialization.next') }}</a-button>
          <a-button v-if="isConfirmStep" type="primary" :loading="applying" @click="handleApply">{{ $t('initialization.applyConfig') }}</a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import StepDb from './StepDb.vue';
import StepSecurity from './StepSecurity.vue';
import StepConnectivity from './StepConnectivity.vue';
import StepConfirm from './StepConfirm.vue';
import { consumeDmBootstrapStatus, getDmSystemStatus, isDmSystemReady } from '../../utils/dmGlobalSettings';

const INIT_DB_CREATE_IF_MISSING = 'clougence.init.db.createIfMissing';
const INIT_DB_REBUILD_IF_NOT_EMPTY = 'clougence.init.db.rebuildIfNotEmpty';
const INIT_DB_CONFIRM_DATABASE_NAME = 'clougence.init.db.confirmDatabaseName';
const INIT_ADMIN_EMAIL = 'clougence.init.admin.email';
const DEFAULT_ADMIN_EMAIL = 'admin@cdmgr.com';

function hasDbFieldChange(patch) {
  return Object.keys(patch).some((key) => key.startsWith('spring.datasource.'));
}

function sleep(timeoutMs) {
  return new Promise((resolve) => setTimeout(resolve, timeoutMs));
}

function buildDmGlobalSettingsUrl() {
  const baseUrl = (process.env.VUE_APP_BASE_URL || '').replace(/\/$/, '');
  return `${baseUrl}/clouddm/console/api/v1/dm_global_settings`;
}

async function pollDmGlobalSettings() {
  const response = await fetch(buildDmGlobalSettingsUrl(), {
    method: 'POST',
    credentials: 'include',
    headers: {
      Accept: 'application/json',
      'Content-Type': 'application/json; charset=UTF-8'
    },
    body: JSON.stringify({})
  });

  if (!response.ok) {
    return null;
  }

  try {
    return await response.json();
  } catch (e) {
    return null;
  }
}

function redirectToLoginPage() {
  window.location.replace(`${window.location.origin}${window.location.pathname}#/login`);
}

export default {
  name: 'Initialization',
  components: { StepDb, StepSecurity, StepConnectivity, StepConfirm },
  data() {
    return {
      mode: 'loading', // 'loading' | 'full' | 'dbOnly' | 'dbError'
      errorMessage: '',
      fieldDefs: [],
      formValues: {},
      rebuildConfirmInput: '',
      testDbRefreshTimer: null,
      dbTestResult: null,
      dbMissingFields: [],
      securityMissingFields: [],
      currentStep: 0,
      testingDb: false,
      applying: false,
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
    stageItems() {
      return [
        { key: 'db', label: this.$t('initialization.stage.db') },
        { key: 'security', label: this.$t('initialization.stage.security') },
        { key: 'connectivity', label: this.$t('initialization.stage.connectivity') },
        { key: 'confirm', label: this.$t('initialization.stage.confirm') }
      ];
    },
    isConfirmStep() {
      return this.currentStep >= this.stageItems.length - 1;
    },
    connectivityStepIndex() {
      return 2;
    },
    currentFooterMessage() {
      if (this.isConfirmStep && this.restartStatusMessage) {
        return {
          type: this.restartStatusType || 'info',
          message: this.restartStatusMessage
        };
      }

      if (this.currentStep === 0) {
        if (this.dbMissingFields.length) {
          return {
            type: 'error',
            message: `${this.$t('initialization.dbFormIncomplete')}：${this.dbMissingFields.join('、')}`
          };
        }

        if (!this.dbTestResult || !this.dbTestResult.message) {
          return null;
        }

        return {
          type: this.dbTestResult.messageType || (this.dbTestResult.success ? 'success' : 'error'),
          message: this.dbTestResult.message
        };
      }

      if (this.currentStep === 1 && this.securityMissingFields.length) {
        return {
          type: 'error',
          message: `${this.$t('initialization.securityFormIncomplete')}：${this.securityMissingFields.join('、')}`
        };
      }

      return null;
    },
    canNext() {
      if (this.currentStep === 0) {
        return !this.dbMissingFields.length && Boolean(this.dbTestResult && this.dbTestResult.canProceed);
      }
      if (this.currentStep === 1) {
        return !this.securityMissingFields.length;
      }
      return true;
    }
  },
  beforeUnmount() {
    this.clearTestDbRefreshTimer();
  },
  async created() {
    await this.bootstrapPage();
  },
  methods: {
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

      const { status, initReason, dbError } = getDmSystemStatus(res);
      if (status === 'Ready') {
        redirectToLoginPage();
        return;
      }

      if (initReason === 'dbConnectionError') {
        this.mode = 'dbError';
        this.errorMessage = dbError || 'Unknown database connection error';
        return;
      }

      const loaded = await this.loadFieldDefs();
      this.mode = loaded ? 'full' : 'dbError';
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
            if (f.propertyKey === INIT_ADMIN_EMAIL) {
              values[f.propertyKey] = f.defaultValue || DEFAULT_ADMIN_EMAIL;
              return;
            }
            values[f.propertyKey] = f.defaultValue || '';
          });
          this.formValues = values;
          this.dbTestResult = null;
          this.dbMissingFields = [];
          this.securityMissingFields = [];
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

    async handleUpdateDbConfig() {
      this.mode = 'loading';
      this.errorMessage = '';
      const loaded = await this.loadFieldDefs();
      this.mode = loaded ? 'dbOnly' : 'dbError';
    },

    nextStep() {
      this.currentStep = Math.min(this.currentStep + 1, this.stageItems.length - 1);
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

    async handleApply() {
      this.applying = true;
      this.restartStatusType = '';
      this.restartStatusMessage = '';
      try {
        const payload = { ...this.formValues };

        const endpoint = this.mode === 'dbOnly' ? this.$services.dmInitUpdateDbConfig : this.$services.dmInitApplyConfig;

        const res = await endpoint({ data: payload });
        if (res.success) {
          this.restartStatusType = 'info';
          this.restartStatusMessage = this.$t('initialization.restarting');
          void this.$services.dmInitRestart({ modal: false }).catch(() => {
            // Connection loss is expected while the service exits.
          });
          await this.waitForRestart();
        }
      } catch (e) {
        console.error('Apply config failed', e);
        this.applying = false;
        this.restartStatusType = '';
        this.restartStatusMessage = '';
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
          redirectToLoginPage();
          return;
        }

        this.restartStatusType = 'info';
        this.restartStatusMessage = this.$t('initialization.restarting');
      }

      this.restartStatusType = 'error';
      this.restartStatusMessage = this.$t('initialization.restartTimeout');
      this.applying = false;
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
  padding: 48px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.wizard-header {
  text-align: center;
  margin-bottom: 32px;
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

.step-panel {
  min-height: 300px;
}

.wizard-footer {
  margin-top: 24px;
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

@media (max-width: 768px) {
  .init-wizard {
    min-height: 100vh;
    padding: 32px 20px;
    border-radius: 0;
  }

  .wizard-stage-progress {
    flex-wrap: wrap;
    row-gap: 16px;
  }

  .wizard-stage-item {
    flex: 1 1 50%;
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
