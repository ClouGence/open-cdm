<template>
  <div class="step-db">
    <a-form layout="horizontal" class="step-db-form">
      <div v-if="jdbcUrlField" class="jdbc-generated-editor">
        <a-form-item :label="$t('initialization.jdbcDataSourceType')" required>
          <InitMysqlDriverStatus :active="driverStatusActive" @status-change="handleDriverStatusChange" />
        </a-form-item>

        <a-form-item :label="$t('initialization.jdbcHostPort')" required>
          <div class="jdbc-host-port-row">
            <div class="jdbc-inline-field jdbc-inline-field-host">
              <a-input
                :value="generatedState.host"
                :disabled="readonly"
                :placeholder="$t('initialization.jdbcHostPlaceholder')"
                @input="(value) => onGeneratedFieldChange('host', normalizeInputValue(value))"
              />
            </div>
            <div class="jdbc-inline-field jdbc-inline-field-port">
              <span class="jdbc-inline-label">{{ $t('initialization.jdbcPortLabel') }}</span>
              <a-input
                :value="generatedState.port"
                :disabled="readonly"
                :placeholder="$t('initialization.jdbcPortPlaceholder')"
                @input="(value) => onGeneratedFieldChange('port', normalizeInputValue(value))"
              />
            </div>
          </div>
        </a-form-item>

        <a-form-item v-if="dbUsernameField" :label="dbUsernameField.label" required class="jdbc-form-item-full">
          <a-input
            class="jdbc-full-width-control"
            :value="formValues[dbUsernameField.propertyKey] || ''"
            :disabled="readonly"
            :placeholder="dbUsernameField.description"
            @input="(value) => onChange(dbUsernameField.propertyKey, normalizeInputValue(value))"
          />
        </a-form-item>

        <a-form-item v-if="dbPasswordField" :label="dbPasswordField.label" :required="dbPasswordField.required" class="jdbc-form-item-full">
          <a-input-password
            class="jdbc-full-width-control"
            :value="formValues[dbPasswordField.propertyKey] || ''"
            :disabled="readonly"
            :placeholder="dbPasswordField.description"
            @input="(value) => onChange(dbPasswordField.propertyKey, normalizeInputValue(value))"
          />
        </a-form-item>

        <a-form-item :label="$t('initialization.jdbcDatabase')" required class="jdbc-form-item-full">
          <a-input
            class="jdbc-full-width-control"
            :value="generatedState.database"
            :disabled="readonly"
            :placeholder="$t('initialization.jdbcDatabasePlaceholder')"
            @input="(value) => onGeneratedFieldChange('database', normalizeInputValue(value))"
          >
            <template v-if="databaseStatusIndicator" #suffix>
              <span class="jdbc-database-status" :class="`jdbc-database-status-${databaseStatusIndicator.type}`">
                <PlusOutlined v-if="databaseStatusIndicator.type === 'new'" />
                <CheckCircleOutlined v-else />
                <span>{{ databaseStatusIndicator.label }}</span>
              </span>
            </template>
          </a-input>
        </a-form-item>

        <a-form-item v-if="showRebuildChoice" :label="$t('initialization.dbRebuildLabel')" required class="jdbc-form-item-full">
          <div class="db-rebuild-option">
            <div class="db-rebuild-line">
              <div class="db-rebuild-text">{{ dbTestResult.rebuildPrompt }}</div>
              <a-radio-group
                :value="formValues['clougence.init.db.rebuildIfNotEmpty'] || ''"
                :disabled="readonly"
                @change="(e) => onChange('clougence.init.db.rebuildIfNotEmpty', e.target.value)"
              >
                <a-radio :value="'true'">{{ $t('initialization.optionYes') }}</a-radio>
                <a-radio :value="'false'">{{ $t('initialization.optionNo') }}</a-radio>
              </a-radio-group>
            </div>
          </div>
        </a-form-item>
      </div>

      <a-form-item v-for="field in remainingFields" :key="field.propertyKey" :label="field.label" required>
        <a-input
          v-if="field.inputType === 'text'"
          :value="formValues[field.propertyKey] || ''"
          :disabled="readonly"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
        <a-input-password
          v-else-if="field.inputType === 'password'"
          :value="formValues[field.propertyKey] || ''"
          :disabled="readonly"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
        <a-input
          v-else-if="field.inputType === 'number'"
          :value="formValues[field.propertyKey]"
          :disabled="readonly"
          type="number"
          @input="(value) => onChange(field.propertyKey, normalizeInputValue(value))"
          :placeholder="field.description"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { CheckCircleOutlined, PlusOutlined } from '@ant-design/icons-vue';
import InitMysqlDriverStatus from './InitMysqlDriverStatus.vue';

const DEFAULT_GENERATED_STATE = Object.freeze({
  host: '',
  port: '3306',
  database: 'cdmgr',
  serverTimezone: 'Asia/Shanghai',
  connectTimeout: '3000',
  socketTimeout: '30000',
  extraOptions: [{ key: 'characterEncoding', value: 'utf8' }]
});

function createGeneratedState(overrides = {}) {
  return {
    host: overrides.host ?? DEFAULT_GENERATED_STATE.host,
    port: overrides.port ?? DEFAULT_GENERATED_STATE.port,
    database: overrides.database ?? DEFAULT_GENERATED_STATE.database,
    serverTimezone: overrides.serverTimezone ?? DEFAULT_GENERATED_STATE.serverTimezone,
    connectTimeout: overrides.connectTimeout ?? DEFAULT_GENERATED_STATE.connectTimeout,
    socketTimeout: overrides.socketTimeout ?? DEFAULT_GENERATED_STATE.socketTimeout,
    extraOptions: (overrides.extraOptions || DEFAULT_GENERATED_STATE.extraOptions).map((option) => ({ ...option }))
  };
}

function decodeJdbcValue(value) {
  try {
    return decodeURIComponent(value || '');
  } catch (e) {
    return value || '';
  }
}

function getInputValue(payload) {
  if (payload && typeof payload === 'object' && Object.prototype.hasOwnProperty.call(payload, 'target')) {
    return payload.target ? payload.target.value : '';
  }
  return payload;
}

function parseMysqlJdbcUrl(jdbcUrl) {
  if (!jdbcUrl || typeof jdbcUrl !== 'string') {
    return null;
  }

  const match = jdbcUrl.match(/^jdbc:mysql:\/\/([^/:?#]*)(?::(\d+))?\/([^?]+)(?:\?(.*))?$/i);
  if (!match) {
    return null;
  }

  const [, host, port, database, queryString] = match;
  const knownParams = {
    serverTimezone: DEFAULT_GENERATED_STATE.serverTimezone,
    connectTimeout: DEFAULT_GENERATED_STATE.connectTimeout,
    socketTimeout: DEFAULT_GENERATED_STATE.socketTimeout
  };
  const extraOptions = [];

  if (queryString) {
    queryString.split('&').forEach((pair) => {
      if (!pair) {
        return;
      }

      const [rawKey, rawValue = ''] = pair.split('=');
      const key = decodeJdbcValue(rawKey);
      const value = decodeJdbcValue(rawValue);

      if (Object.prototype.hasOwnProperty.call(knownParams, key)) {
        knownParams[key] = value;
      } else {
        extraOptions.push({ key, value });
      }
    });
  }

  return createGeneratedState({
    host,
    port: port || DEFAULT_GENERATED_STATE.port,
    database: decodeJdbcValue(database),
    serverTimezone: knownParams.serverTimezone,
    connectTimeout: knownParams.connectTimeout,
    socketTimeout: knownParams.socketTimeout,
    extraOptions
  });
}

function buildMysqlJdbcUrl(generatedState) {
  const host = generatedState.host || '';
  const port = generatedState.port || '';
  const database = generatedState.database || '';

  if (!host) {
    return '';
  }

  const params = [];

  if (generatedState.serverTimezone) {
    params.push(`serverTimezone=${encodeURIComponent(generatedState.serverTimezone)}`);
  }
  if (generatedState.connectTimeout) {
    params.push(`connectTimeout=${encodeURIComponent(generatedState.connectTimeout)}`);
  }
  if (generatedState.socketTimeout) {
    params.push(`socketTimeout=${encodeURIComponent(generatedState.socketTimeout)}`);
  }

  (generatedState.extraOptions || []).forEach((option) => {
    if (!option.key) {
      return;
    }
    params.push(`${encodeURIComponent(option.key)}=${encodeURIComponent(option.value || '')}`);
  });

  const queryString = params.length ? `?${params.join('&')}` : '';
  return `jdbc:mysql://${host}${port ? `:${port}` : ''}/${database}${queryString}`;
}

function serializeJdbcParams(generatedState) {
  const lines = [];

  if (generatedState.serverTimezone) {
    lines.push(`serverTimezone=${generatedState.serverTimezone}`);
  }
  if (generatedState.connectTimeout) {
    lines.push(`connectTimeout=${generatedState.connectTimeout}`);
  }
  if (generatedState.socketTimeout) {
    lines.push(`socketTimeout=${generatedState.socketTimeout}`);
  }

  (generatedState.extraOptions || []).forEach((option) => {
    if (!option.key) {
      return;
    }
    lines.push(`${option.key}=${option.value || ''}`);
  });

  return lines.join('\n');
}

function parseJdbcParamsText(text) {
  const nextState = {
    serverTimezone: '',
    connectTimeout: '',
    socketTimeout: '',
    extraOptions: []
  };

  (text || '')
    .split('\n')
    .map((line) => line.trim())
    .filter(Boolean)
    .forEach((line) => {
      const separatorIndex = line.indexOf('=');
      const key = separatorIndex >= 0 ? line.slice(0, separatorIndex).trim() : line;
      const value = separatorIndex >= 0 ? line.slice(separatorIndex + 1).trim() : '';

      if (!key) {
        return;
      }

      if (key === 'serverTimezone') {
        nextState.serverTimezone = value;
        return;
      }
      if (key === 'connectTimeout') {
        nextState.connectTimeout = value;
        return;
      }
      if (key === 'socketTimeout') {
        nextState.socketTimeout = value;
        return;
      }

      nextState.extraOptions.push({ key, value });
    });

  return nextState;
}

export default {
  name: 'StepDb',
  components: {
    CheckCircleOutlined,
    PlusOutlined,
    InitMysqlDriverStatus
  },
  props: {
    fieldDefs: { type: Array, default: () => [] },
    formValues: { type: Object, default: () => ({}) },
    dbTestResult: { type: Object, default: null },
    readonly: { type: Boolean, default: false },
    driverStatusActive: { type: Boolean, default: true }
  },
  data() {
    return {
      generatedState: createGeneratedState()
    };
  },
  computed: {
    jdbcUrlField() {
      return this.fieldDefs.find((field) => field.propertyKey === 'spring.datasource.jdbcurl') || null;
    },
    dbUsernameField() {
      return this.fieldDefs.find((field) => field.propertyKey === 'spring.datasource.username') || null;
    },
    dbPasswordField() {
      return this.fieldDefs.find((field) => field.propertyKey === 'spring.datasource.password') || null;
    },
    remainingFields() {
      const excludedKeys = ['spring.datasource.jdbcurl', 'spring.datasource.username', 'spring.datasource.password'];
      return this.fieldDefs.filter((field) => !excludedKeys.includes(field.propertyKey));
    },
    jdbcUrlValue() {
      return this.formValues['spring.datasource.jdbcurl'] || '';
    },
    showRebuildChoice() {
      return Boolean(this.dbTestResult && this.dbTestResult.showRebuildChoice);
    },
    databaseStatusIndicator() {
      if (!this.dbTestResult || !this.dbTestResult.success || !this.generatedState.database) {
        return null;
      }

      if (this.dbTestResult.databaseExists) {
        return {
          type: 'existing',
          label: this.$t('initialization.jdbcDatabaseExists')
        };
      }

      return {
        type: 'new',
        label: this.$t('initialization.jdbcDatabaseCreate')
      };
    },
    missingRequiredFields() {
      if (this.readonly) {
        return [];
      }

      const missingFields = [];

      if (!this.generatedState.host) {
        missingFields.push(this.$t('initialization.jdbcHostPort'));
      }
      if (!this.generatedState.port) {
        missingFields.push(this.$t('initialization.jdbcPortLabel'));
      }
      if (!(this.formValues['spring.datasource.username'] || '').trim()) {
        missingFields.push(this.dbUsernameField ? this.dbUsernameField.label : this.$t('initialization.dbUsernameFallback'));
      }
      if (this.dbPasswordField && this.dbPasswordField.required && !(this.formValues['spring.datasource.password'] || '').trim()) {
        missingFields.push(this.dbPasswordField ? this.dbPasswordField.label : this.$t('initialization.dbPasswordFallback'));
      }
      if (!this.generatedState.database) {
        missingFields.push(this.$t('initialization.jdbcDatabase'));
      }

      return missingFields;
    }
  },
  watch: {
    jdbcUrlValue: {
      immediate: true,
      handler(value) {
        this.syncJdbcState(value);
      }
    },
    missingRequiredFields: {
      immediate: true,
      handler(value) {
        this.$emit('validation-change', value);
      }
    }
  },
  methods: {
    handleDriverStatusChange(status) {
      this.$emit('driver-status-change', status || null);
    },
    normalizeInputValue(payload) {
      if (payload && typeof payload === 'object' && 'target' in payload) {
        return payload.target ? payload.target.value : '';
      }
      return payload;
    },
    onChange(key, value) {
      if (this.readonly) {
        return;
      }
      this.$emit('update:formValues', { [key]: value });
    },
    syncJdbcState(jdbcUrl) {
      const parsed = parseMysqlJdbcUrl(jdbcUrl);
      if (parsed) {
        this.generatedState = parsed;
        return;
      }

      if (!jdbcUrl) {
        this.generatedState = createGeneratedState();
      }
    },
    onGeneratedFieldChange(key, value) {
      if (this.readonly) {
        return;
      }
      this.generatedState = {
        ...this.generatedState,
        [key]: value
      };
      this.emitGeneratedJdbcUrl();
    },
    emitGeneratedJdbcUrl() {
      const jdbcUrl = buildMysqlJdbcUrl(this.generatedState);
      this.$emit('update:formValues', { 'spring.datasource.jdbcurl': jdbcUrl });
    }
  }
};
</script>

<style scoped>
.step-db-form :deep(.ant-form-item) {
  display: flex;
  align-items: flex-start;
  width: 100%;
}
.step-db-form :deep(.ant-form-item-row) {
  display: flex;
  width: 100%;
}
.step-db-form :deep(.ant-form-item-label) {
  flex: 0 0 120px;
  max-width: 120px;
  padding-right: 12px;
  text-align: left;
  line-height: 32px;
}
.step-db-form :deep(.ant-form-item-label > label) {
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  min-height: 32px;
  white-space: normal;
  text-align: left;
}
.step-db-form :deep(.ant-form-item-required::before) {
  display: none !important;
}
.step-db-form :deep(.ant-form-item-control-wrapper) {
  flex: 1;
  max-width: calc(100% - 120px);
}
.step-db-form :deep(.ant-form-item-control) {
  flex: 1 1 0;
  min-width: 0;
}
.step-db-form :deep(.ant-form-item-control-input) {
  flex: 1 1 auto;
  min-width: 0;
}
.jdbc-generated-editor {
  width: 100%;
}
.jdbc-host-port-row {
  display: flex;
  align-items: center;
  gap: 16px;
  width: 100%;
}
.jdbc-inline-field {
  display: flex;
  align-items: center;
}
.jdbc-inline-field-host {
  flex: 1;
  min-width: 0;
}
.jdbc-inline-field-port {
  width: auto;
  flex: 0 0 auto;
}
.jdbc-inline-field :deep(.ant-input) {
  width: 100%;
}
.jdbc-inline-field-port :deep(.ant-input) {
  width: 80px;
}
.jdbc-inline-label {
  margin-right: 8px;
  white-space: nowrap;
  color: rgba(0, 0, 0, 0.85);
}
.jdbc-form-item-full :deep(.ant-form-item-control-input),
.jdbc-form-item-full :deep(.ant-form-item-control-input-content),
.jdbc-form-item-full :deep(.ant-form-item-control-wrapper),
.jdbc-full-width-control,
.jdbc-full-width-control :deep(.ant-input),
.jdbc-full-width-control :deep(.ant-input-password) {
  width: 100%;
}
.jdbc-database-status {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  line-height: 1;
  white-space: nowrap;
}
.jdbc-database-status-new {
  color: #389e0d;
}
.jdbc-database-status-existing {
  color: #1677ff;
}
.db-rebuild-option {
  width: 100%;
}
.db-rebuild-line {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  width: 100%;
  flex-wrap: wrap;
}
.db-rebuild-text {
  flex: 1;
  min-width: 0;
  color: rgba(0, 0, 0, 0.85);
  line-height: 22px;
}
</style>
