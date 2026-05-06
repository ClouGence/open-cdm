<template>
  <div class="initialization">
    <!-- 错误页模式 -->
    <div v-if="mode === 'dbError'" class="init-error-page">
      <div class="error-card">
        <div class="error-icon" />
        <h2>{{ $t('initialization.dbError') }}</h2>
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
      </div>

      <a-steps :current="currentStep" class="wizard-steps">
        <a-step :title="$t('initialization.step.db')" />
        <a-step v-if="mode === 'full'" :title="$t('initialization.step.security')" />
        <a-step v-if="mode === 'full'" :title="$t('initialization.step.connectivity')" />
        <a-step :title="$t('initialization.step.confirm')" />
      </a-steps>

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
        <div v-show="currentStep === 1 && mode === 'full'" class="step-panel">
          <StepSecurity :fieldDefs="securityFields" :formValues="formValues" @update:formValues="updateFormValues" />
        </div>

        <!-- Step 2: 连接性配置 -->
        <div v-show="currentStep === (mode === 'full' ? 2 : 1)" class="step-panel">
          <StepConnectivity :fieldDefs="connectivityFields" :formValues="formValues" @update:formValues="updateFormValues" />
        </div>

        <!-- 确认步骤 -->
        <div v-show="isConfirmStep" class="step-panel">
          <StepConfirm
            :fieldDefs="fieldDefs"
            :formValues="formValues"
            :dbTestResult="dbTestResult"
            :mode="mode"
            :advancedMode="advancedMode"
            :rawContent="rawContent"
            :applying="applying"
            @update:advancedMode="advancedMode = $event"
            @update:rawContent="rawContent = $event"
            @apply="handleApply"
          />
        </div>
      </div>

      <div class="wizard-footer">
        <div v-if="currentStep === 0 && dbFooterMessage" class="wizard-footer-message" :class="dbFooterMessage.type">
          {{ dbFooterMessage.message }}
        </div>
        <div class="wizard-footer-actions">
          <a-button v-if="currentStep > 0" @click="prevStep">{{ $t('initialization.prev') }}</a-button>
          <a-button v-if="currentStep === 0" @click="handleTestDb">{{ $t('initialization.testConnection') }}</a-button>
          <a-button v-if="!isConfirmStep" type="primary" :disabled="!canNext" @click="nextStep">{{ $t('initialization.next') }}</a-button>
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
import { getDmSystemStatus, isDmSystemReady } from '../../utils/dmGlobalSettings';

const INIT_DB_CREATE_IF_MISSING = 'clougence.init.db.createIfMissing';
const INIT_DB_REBUILD_IF_NOT_EMPTY = 'clougence.init.db.rebuildIfNotEmpty';

function hasDbFieldChange(patch) {
  return Object.keys(patch).some((key) => key.startsWith('spring.datasource.'));
}

function isDatabaseEmpty(result) {
  return Boolean(result && (result.empty || result.isEmpty));
}

export default {
  name: 'Initialization',
  components: { StepDb, StepSecurity, StepConnectivity, StepConfirm },
  data() {
    return {
      mode: 'full', // 'full' | 'dbOnly' | 'dbError'
      errorMessage: '',
      fieldDefs: [],
      formValues: {},
      dbTestResult: null,
      dbMissingFields: [],
      currentStep: 0,
      advancedMode: false,
      rawContent: '',
      applying: false
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
    isConfirmStep() {
      const maxStep = this.mode === 'full' ? 3 : 1;
      return this.currentStep >= maxStep;
    },
    dbNeedsRebuildChoice() {
      return Boolean(
        this.dbTestResult &&
          this.dbTestResult.success &&
          this.dbTestResult.databaseExists &&
          !isDatabaseEmpty(this.dbTestResult) &&
          this.dbTestResult.charsetValid
      );
    },
    dbRebuildChoiceMissing() {
      if (!this.dbNeedsRebuildChoice) {
        return false;
      }
      return !['true', 'false'].includes(this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY]);
    },
    dbFooterMessage() {
      if (this.currentStep !== 0) {
        return null;
      }

      if (this.dbMissingFields.length) {
        return {
          type: 'error',
          message: `${this.$t('initialization.dbFormIncomplete')}：${this.dbMissingFields.join('、')}`
        };
      }

      if (this.dbRebuildChoiceMissing) {
        return {
          type: 'error',
          message: this.$t('initialization.dbRebuildChoiceRequired')
        };
      }

      if (!this.dbTestResult) {
        return null;
      }

      const extraMessages = [];
      if (this.dbTestResult.success && isDatabaseEmpty(this.dbTestResult)) {
        extraMessages.push(this.$t('initialization.dbEmpty'));
      }
      if (this.dbTestResult.success && this.dbTestResult.isInstalled) {
        extraMessages.push(this.$t('initialization.dbInstalled'));
      }

      return {
        type: this.dbTestResult.success ? 'success' : 'error',
        message: [this.dbTestResult.message, ...extraMessages].filter(Boolean).join(' - ')
      };
    },
    canNext() {
      if (this.currentStep === 0) {
        return !this.dbMissingFields.length && !this.dbRebuildChoiceMissing && this.dbTestResult && this.dbTestResult.success;
      }
      return true;
    }
  },
  async created() {
    // 直接调用 API 获取状态，不依赖 store（因为 init 模式下 store 可能未加载）
    try {
      const res = await this.$services.dmGlobalSettings();
      if (res.success) {
        const { status, initReason, dbError } = getDmSystemStatus(res);
        // 系统已就绪 → 跳转到登录页
        if (status === 'Ready') {
          this.$router.push({ name: 'Login' });
          return;
        }
        if (initReason === 'dbConnectionError') {
          this.mode = 'dbError';
          this.errorMessage = dbError || 'Unknown database connection error';
        } else {
          this.mode = 'full';
          await this.loadFieldDefs();
        }
      }
    } catch (e) {
      this.mode = 'dbError';
      this.errorMessage = 'Unable to connect to server';
    }
  },
  methods: {
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
        }
      } catch (e) {
        console.error('Failed to load field defs', e);
      }
    },

    handleDbValidationChange(missingFields) {
      this.dbMissingFields = missingFields;
    },

    updateFormValues(patch) {
      if (hasDbFieldChange(patch)) {
        this.dbTestResult = null;
        this.formValues = {
          ...this.formValues,
          ...patch,
          [INIT_DB_CREATE_IF_MISSING]: 'false',
          [INIT_DB_REBUILD_IF_NOT_EMPTY]: ''
        };
        return;
      }
      this.formValues = { ...this.formValues, ...patch };
    },

    async handleTestDb() {
      if (this.dbMissingFields.length) {
        this.dbTestResult = null;
        return;
      }

      const params = {
        'spring.datasource.jdbcurl': this.formValues['spring.datasource.jdbcurl'],
        'spring.datasource.username': this.formValues['spring.datasource.username'],
        'spring.datasource.password': this.formValues['spring.datasource.password']
      };
      try {
        const res = await this.$services.dmInitTestDb({ data: params });
        if (res.success) {
          this.dbTestResult = res.data;
          this.formValues = {
            ...this.formValues,
            [INIT_DB_CREATE_IF_MISSING]: res.data && res.data.createDatabase ? 'true' : 'false',
            [INIT_DB_REBUILD_IF_NOT_EMPTY]:
              res.data && res.data.databaseExists && !isDatabaseEmpty(res.data)
                ? ['true', 'false'].includes(this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY])
                  ? this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY]
                  : ''
                : 'false'
          };
        }
      } catch (e) {
        console.error('Test DB failed', e);
      }
    },

    async handleRetry() {
      try {
        const res = await this.$services.dmGlobalSettings();
        const systemStatus = getDmSystemStatus(res);
        if (res.success && systemStatus.status === 'Ready') {
          this.$router.push({ name: 'Login' });
        } else if (res.success && systemStatus.initReason !== 'dbConnectionError') {
          this.mode = 'full';
          this.errorMessage = '';
          await this.loadFieldDefs();
        } else {
          this.errorMessage = systemStatus.dbError || this.errorMessage;
        }
      } catch (e) {
        console.error('Retry failed', e);
      }
    },

    handleUpdateDbConfig() {
      this.mode = 'dbOnly';
      this.errorMessage = '';
      this.loadFieldDefs();
    },

    nextStep() {
      this.currentStep++;
    },

    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      }
    },

    async handleApply() {
      this.applying = true;
      try {
        const dbActionPayload = {
          [INIT_DB_CREATE_IF_MISSING]: this.formValues[INIT_DB_CREATE_IF_MISSING] || 'false',
          [INIT_DB_REBUILD_IF_NOT_EMPTY]: this.formValues[INIT_DB_REBUILD_IF_NOT_EMPTY] || 'false'
        };
        const payload = this.advancedMode ? { rawContent: this.rawContent, ...dbActionPayload } : { ...this.formValues };

        const endpoint = this.mode === 'dbOnly' ? this.$services.dmInitUpdateDbConfig : this.$services.dmInitApplyConfig;

        const res = await endpoint({ data: payload });
        if (res.success) {
          await this.$services.dmInitRestart();
          this.waitForRestart();
        }
      } catch (e) {
        console.error('Apply config failed', e);
        this.applying = false;
      }
    },

    async waitForRestart() {
      const maxRetries = 60;
      for (let i = 0; i < maxRetries; i++) {
        await new Promise((r) => setTimeout(r, 2000));
        try {
          const res = await this.$services.dmGlobalSettings();
          if (res.success && isDmSystemReady(res)) {
            this.$router.push({ name: 'Login' });
            return;
          }
        } catch (e) {
          // 服务还未就绪
        }
      }
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

.error-card {
  background: #fff;
  border-radius: 8px;
  padding: 48px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.error-icon {
  font-size: 48px;
  margin-bottom: 16px;
}
.error-icon::before {
  content: '\\26A0';
  font-size: 48px;
}

.error-detail {
  margin: 24px 0;
  text-align: left;
}

.error-message {
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

.wizard-steps {
  margin-bottom: 32px;
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

.wizard-footer-actions {
  margin-left: auto;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
