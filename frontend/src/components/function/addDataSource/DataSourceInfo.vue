<template>
  <div class="add-datasource-step1" :class="{ 'is-form-step': currentStep === 1 }">
    <Form
      v-if="currentStep === 0"
      ref="selectDsTypeForm"
      :model="addDataSourceForm"
      label-position="right"
      :label-width="110"
      :rules="selectDsTypeRules"
    >
      <FormItem class="datasource-type-form-item" prop="type" :label-width="0">
        <RadioGroup
          v-model="addDataSourceForm.type"
          type="button"
          class="datasource-type-radio-group radio-group-radius-warp-datasource custom-radio-group"
          @on-change="handleDataSourceChange"
        >
          <div class="datasource-type-group" v-for="(dataSourceGroup, index) of dataSourceTypes" :key="index">
            <Radio translate="no" class="datasource-type-radio custom-radio" v-for="type of dataSourceGroup" :label="type.dsKey" :key="type.dsKey">
              <span class="datasource-type-card">
                <DataSourceIcon class="datasource-type-icon" size="30px" :type="type.dsKey" leftMargin="0"></DataSourceIcon>
                <span class="datasource-type-name" :title="type.displayName">
                  {{ type.displayName }}
                </span>
              </span>
            </Radio>
          </div>
        </RadioGroup>
      </FormItem>
    </Form>
    <div v-if="currentStep === 1" class="add-datasource-form-stage">
      <Form ref="addLocalDs" :model="addDataSourceForm" label-position="right" :label-width="160" :rules="addDataSourceRule">
        <div class="add-ds-name-form">
          <FormItem prop="instanceDesc" :label="$t('shu-ju-yuan-ming-cheng')">
            <Input v-model.trim="addDataSourceForm.instanceDesc" class="add-ds-name-input" />
          </FormItem>
        </div>
        <div v-if="visibleAddDsPanels.length" class="add-ds-ui-panel-preview">
          <Spin v-if="addDsConfigLoading" fix />
          <Tabs v-model="activeAddDsPanelKey" :animated="false">
            <TabPane v-for="panel in visibleAddDsPanels" :key="panel.key" :name="panel.key" :label="panel.titleI18N || panel.key">
              <ui-form-field
                v-for="field in panel.visibleFields"
                :key="`${panel.key}-${field.field}`"
                :field="field"
                :form="addDsUiForm"
                :data-source-form="addDataSourceForm"
                :driver-family-map="driverFamilyMap"
                :env-list="envData"
                :cluster-list="queryClusterList"
                :current-query-cluster="currentQueryCluster"
                :current-step="currentStep"
                :show-query-config="showQueryConfig"
                @envChange="handleEnvChange"
                @clusterChange="handleChangeQueryCluster"
                @update:driverReady="handleAddDsDriverReady"
              />
            </TabPane>
          </Tabs>
        </div>
      </Form>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import { UiFormField } from '@/components/form';

const emptyHostList = () => [
  {
    type: 'public',
    display: true,
    host: '',
    port: ''
  },
  {
    type: 'public',
    display: false,
    host: '',
    port: ''
  }
];

const CERTIFICATE_CONFIGURED_VALUE = 'configured://certificate';

export default {
  name: 'DataSourceInfo',
  components: {
    DataSourceIcon,
    UiFormField
  },
  props: {
    addDataSourceForm: Object,
    currentStep: {
      type: Number,
      default: 0
    },
    showQueryConfig: {
      type: Boolean,
      default: false
    },
    setSecuritySetting: Function,
    driverFamilyMap: {
      type: Object,
      default: () => ({})
    },
    dsId: {
      type: Number,
      default: null
    },
    editMode: {
      type: Boolean,
      default: false
    }
  },
  emits: ['driver-status-change', 'config-loaded'],
  data() {
    return {
      currentAddDsConfig: {},
      activeAddDsPanelKey: '',
      addDsUiForm: {},
      addDsConfigLoading: false,
      addDsConfigRequestKey: '',
      currentDriverReady: false,
      securitySetting: [],
      envData: [],
      queryClusterList: [],
      envClusterTree: [],
      bindClusters: [],
      currentQueryCluster: {},
      dataSourceTypes: [],
      selectDsTypeRules: {
        type: [
          {
            required: true,
            message: this.$t('the-type-cannot-be-empty'),
            trigger: 'change'
          }
        ]
      },
      addDataSourceRule: {
        instanceDesc: [
          {
            required: true,
            message: this.$t('ming-cheng-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        envId: [
          {
            validator: (rule, value, callback) => {
              if (this.addDataSourceForm.envId === '' || this.addDataSourceForm.envId === null || this.addDataSourceForm.envId === undefined) {
                return callback(new Error(this.$t('huan-jing-bu-neng-wei-kong')));
              }
              return callback();
            },
            trigger: 'change'
          }
        ],
        queryClusterId: [
          {
            validator: (rule, value, callback) => {
              if (this.showQueryConfig && !value) {
                return callback(new Error(this.$t('bang-ding-ji-qun-bu-neng-wei-kong')));
              }
              return callback();
            },
            trigger: 'change'
          }
        ]
      }
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    currentDriverFamilies() {
      return this.driverFamilyMap[this.addDataSourceForm.type] || [];
    },
    currentAddDsPanels() {
      return Array.isArray(this.currentAddDsConfig?.panels) ? this.currentAddDsConfig.panels : [];
    },
    visibleAddDsPanels() {
      return this.currentAddDsPanels
        .map((panel) => ({
          ...panel,
          visibleFields: this.visibleAddDsPanelFields(panel)
        }))
        .filter((panel) => panel.visibleFields.length > 0);
    }
  },
  watch: {
    dmGlobalSetting() {
      this.listDataSourceTypes();
    },
    currentStep(step) {
      if (step === 1) {
        this.fetchAddDsConfig();
      }
    },
    dsId() {
      if (this.currentStep === 1) {
        this.fetchAddDsConfig();
      }
    },
    visibleAddDsPanels() {
      this.ensureActiveAddDsPanel();
    },
    currentDriverFamilies: {
      handler() {
        this.emitDriverStatusChange();
      },
      immediate: true
    }
  },
  created() {
    this.listDataSourceTypes();
    this.fetchBindInfo();
    if (this.currentStep === 1) {
      this.fetchAddDsConfig();
    }
  },
  methods: {
    validateSelectStep(callback) {
      this.$refs.selectDsTypeForm.validate((valid) => {
        callback(valid);
      });
    },
    isDriverReadyForSubmit() {
      return !this.currentDriverFamilies.length || this.currentDriverReady;
    },
    listDataSourceTypes() {
      const supportNames = this.dmGlobalSetting?.dsSupportNames || [];
      this.dataSourceTypes = Array.isArray(supportNames)
        ? supportNames
            .map((group) => (Array.isArray(group) ? group.map(this.normalizeDsSupportName).filter(Boolean) : []))
            .filter((group) => group.length > 0)
        : [];

      if (!this.dataSourceTypes.length) {
        return;
      }

      const allTypes = this.dataSourceTypes.flatMap((group) => group.map((type) => type.dsKey));
      if (!allTypes.includes(this.addDataSourceForm.type)) {
        this.addDataSourceForm.type = this.dataSourceTypes[0][0].dsKey;
      }
    },
    normalizeDsSupportName(type) {
      if (!type) {
        return null;
      }
      if (typeof type === 'string') {
        return {
          dsKey: type,
          displayName: type
        };
      }
      if (!type.dsKey) {
        return null;
      }
      return {
        dsKey: type.dsKey,
        displayName: type.displayName || type.dsKey
      };
    },
    async fetchBindInfo() {
      const res = await this.$services.dmDataSourceFetchBindInfo({ data: {} });
      if (!res.success) {
        return;
      }

      const data = res.data || {};
      const envs = Array.isArray(data.envs) ? data.envs : [];
      const clusters = Array.isArray(data.clusters) ? data.clusters : [];
      const envClusterTree = Array.isArray(data.envClusterTree) ? data.envClusterTree : [];

      this.bindClusters = clusters;
      this.envClusterTree = envClusterTree.length
        ? envClusterTree
        : envs.map((env) => ({
            ...env,
            children: clusters
          }));
      this.listEnv();
    },
    listEnv() {
      this.envData = this.envClusterTree.map((env) => ({
        id: env.id,
        ownerUid: env.ownerUid,
        envName: env.envName,
        description: env.description,
        queryLimit: env.queryLimit
      }));
      if (this.envData[0]) {
        const selectedEnv = this.envData.find((env) => env.id === this.addDataSourceForm.envId);
        if (!selectedEnv) {
          this.addDataSourceForm.envId = this.envData[0].id;
        }
        this.clearFieldValidate('envId');
      }
      this.listQueryBindCluster();
    },
    listQueryBindCluster() {
      const envNode = this.envClusterTree.find((env) => env.id === this.addDataSourceForm.envId);
      const clusters = envNode && Array.isArray(envNode.children) ? envNode.children : this.bindClusters;
      this.queryClusterList = Array.isArray(clusters) ? clusters : [];
      const selectedCluster = this.queryClusterList.find((cluster) => cluster.id === this.addDataSourceForm.queryClusterId);
      const defaultCluster = selectedCluster || this.queryClusterList.find((cluster) => cluster.runningCount > 0) || this.queryClusterList[0];
      if (defaultCluster) {
        this.addDataSourceForm.queryClusterId = defaultCluster.id;
        this.currentQueryCluster = defaultCluster;
        this.clearFieldValidate('queryClusterId');
      } else {
        this.addDataSourceForm.queryClusterId = '';
        this.currentQueryCluster = {};
      }
    },
    clearFieldValidate(field) {
      this.$nextTick(() => {
        if (this.$refs.addLocalDs) {
          this.$refs.addLocalDs.clearValidate(field);
        }
      });
    },
    handleEnvChange(value) {
      this.addDataSourceForm.envId = value;
      this.listQueryBindCluster();
      this.clearFieldValidate('envId');
    },
    handleChangeQueryCluster() {
      this.currentQueryCluster = this.queryClusterList.find((cluster) => cluster.id === this.addDataSourceForm.queryClusterId) || {};
      this.clearFieldValidate('queryClusterId');
      this.emitDriverStatusChange();
    },
    handleAddDsDriverReady(ready) {
      this.currentDriverReady = !!ready;
      this.emitDriverStatusChange();
    },
    emitDriverStatusChange() {
      const required = !!this.currentDriverFamilies.length;
      this.$emit('driver-status-change', {
        required,
        ready: !required || this.currentDriverReady
      });
    },
    async handleDataSourceChange() {
      const dataSourceType = this.addDataSourceForm.type;
      this.currentAddDsConfig = {};
      this.addDsUiForm = {};
      this.currentDriverReady = false;
      this.securitySetting = [];
      this.setSecuritySetting([]);

      Object.assign(this.addDataSourceForm, {
        dbName: '',
        noValidateDbName: '',
        driver: '',
        driverFamily: '',
        driverVersion: '',
        dsKvConfigs: [],
        hostList: emptyHostList(),
        host: '',
        port: '',
        publicHost: '',
        publicPort: '',
        resolvedHost: '',
        account: '',
        password: '',
        accessKey: '',
        secretKey: '',
        instanceDesc: '',
        type: dataSourceType
      });

      if (this.$refs.addLocalDs) {
        this.$refs.addLocalDs.resetFields();
      }
      if (this.currentStep === 1) {
        await this.fetchAddDsConfig();
      }
      this.emitDriverStatusChange();
    },
    async fetchAddDsConfig() {
      const dataSourceType = this.addDataSourceForm.type;
      if (!dataSourceType && !this.dsId) {
        this.applyAddDsConfig({});
        return;
      }

      const requestKey = `${dataSourceType || 'ds'}-${this.dsId || 'new'}-${Date.now()}`;
      this.addDsConfigRequestKey = requestKey;
      this.addDsConfigLoading = true;
      try {
        const res = await this.$services.dmDataSourceFetchDsConfig({ data: { dsType: dataSourceType || null, dsId: this.dsId } });
        if (this.addDsConfigRequestKey !== requestKey) {
          return;
        }
        this.applyAddDsConfig(res.success ? res.data || {} : {});
      } catch (e) {
        if (this.addDsConfigRequestKey === requestKey) {
          this.applyAddDsConfig({});
        }
      } finally {
        if (this.addDsConfigRequestKey === requestKey) {
          this.addDsConfigLoading = false;
        }
      }
    },
    applyAddDsConfig(addDsConfig) {
      this.currentAddDsConfig = addDsConfig || {};
      this.$emit('config-loaded', this.currentAddDsConfig);
      if (this.currentAddDsConfig.dsType) {
        this.addDataSourceForm.type = this.currentAddDsConfig.dsType;
      }
      if (this.currentAddDsConfig.envId) {
        this.addDataSourceForm.envId = this.currentAddDsConfig.envId;
      }
      if (this.currentAddDsConfig.clusterId) {
        this.addDataSourceForm.queryClusterId = this.currentAddDsConfig.clusterId;
      }
      if (this.envClusterTree.length) {
        this.listQueryBindCluster();
      }
      if ((this.editMode || !this.addDataSourceForm.instanceDesc) && this.currentAddDsConfig.instanceName) {
        this.addDataSourceForm.instanceDesc = this.currentAddDsConfig.instanceName;
        this.clearFieldValidate('instanceDesc');
      }
      this.initAddDsUiPanels();
      this.initDsKvConfigs();
      this.initSecurityOptions();
    },
    initDsKvConfigs() {
      this.addDataSourceForm.dsKvConfigs = Array.isArray(this.currentAddDsConfig?.configDef)
        ? this.currentAddDsConfig.configDef.map((config) => ({ ...config }))
        : [];
      this.addDataSourceForm.dsKvConfigs.forEach((config) => {
        if (config.defaultValue && config.confValType === 'BOOLEAN') {
          config.formatValue = JSON.parse(config.defaultValue);
        }
      });
    },
    initSecurityOptions() {
      const securityOptions = this.resolveAddDsSecurityOptions();
      this.securitySetting = securityOptions;
      this.setSecuritySetting(securityOptions);
      if (!securityOptions.length) {
        return;
      }
      const matchedSecurity = securityOptions.find((securityOption) => securityOption.securityType === this.addDataSourceForm.securityType);
      const defaultSecurity = securityOptions.find((securityOption) => securityOption.defaultCheck) || securityOptions[0];
      if (!matchedSecurity && defaultSecurity) {
        this.addDataSourceForm.securityType = defaultSecurity.securityType || defaultSecurity.value || '';
      }
    },
    initAddDsUiPanels() {
      const form = {};
      this.currentAddDsPanels.forEach((panel) => {
        this.collectAddDsFieldDefaults(panel.children || [], form);
      });
      const securityOptions = this.resolveAddDsSecurityOptions();
      if (securityOptions.length && !form.securityType) {
        const defaultSecurity = securityOptions.find((option) => option.defaultCheck) || securityOptions[0];
        form.securityType = defaultSecurity.securityType || defaultSecurity.value || '';
      }
      if (this.currentAddDsConfig?.instanceId && !form.instanceId) {
        form.instanceId = this.currentAddDsConfig.instanceId;
      }
      if (this.currentAddDsConfig?.dsId) {
        form.dsId = this.currentAddDsConfig.dsId;
      }
      this.addDsUiForm = form;
      this.ensureActiveAddDsPanel();
    },
    collectAddDsFieldDefaults(fields, form) {
      (fields || []).forEach((field) => {
        if (field.type === 'TransactionControl') {
          this.collectTransactionControlDefaults(field, form);
          return;
        }
        if (field.field && !Object.prototype.hasOwnProperty.call(form, field.field)) {
          form[field.field] = this.addDsFieldDefaultValue(field);
        }
        this.collectAddDsFieldDefaults(field.children || [], form);
        (field.options || []).forEach((option) => {
          this.collectAddDsFieldDefaults(option.children || [], form);
        });
      });
    },
    collectTransactionControlDefaults(field, form) {
      const defaults = field.defaultValue || {};
      if (!Object.prototype.hasOwnProperty.call(form, 'autoCommit')) {
        form.autoCommit = defaults.autoCommit ?? 'true';
      }
      if (!Object.prototype.hasOwnProperty.call(form, 'isolation')) {
        form.isolation = defaults.isolation ?? 'DEFAULT';
      }
    },
    addDsFieldDefaultValue(field) {
      if (field.type === 'Password') {
        return '';
      }
      if (field.defaultValue !== null && field.defaultValue !== undefined) {
        if (typeof field.defaultValue === 'object' && Object.prototype.hasOwnProperty.call(field.defaultValue, 'value')) {
          return field.defaultValue.value;
        }
        return field.defaultValue;
      }
      if (field.type === 'Check' || field.type === 'CheckBox') {
        return false;
      }
      if (field.type === 'MultipleOptions') {
        return [];
      }
      return '';
    },
    visibleAddDsPanelFields(panel) {
      return this.visibleAddDsFields(panel.children || []);
    },
    visibleAddDsFields(fields) {
      const result = [];
      (fields || []).forEach((field) => {
        if (field.hide || !this.isAddDsFieldActive(field)) {
          return;
        }
        result.push(field);
        result.push(...this.visibleAddDsFields(this.addDsFieldChildren(field)));
      });
      return result;
    },
    addDsFieldChildren(field) {
      if (field.type === 'Options' || field.type === 'MultipleOptions') {
        return this.selectedAddDsOptionChildren(field);
      }
      return field.children || [];
    },
    selectedAddDsOptionChildren(field) {
      if (field.type !== 'Options' && field.type !== 'MultipleOptions') {
        return [];
      }
      const selectedValue = this.addDsUiForm[field.field];
      const selectedValues = Array.isArray(selectedValue) ? selectedValue.map(String) : [String(selectedValue)];
      return (field.options || [])
        .filter((option) => selectedValues.includes(String(option.value ?? option.securityType)))
        .flatMap((option) => option.children || []);
    },
    isAddDsFieldActive(field) {
      const expr = field.activeExpr;
      if (!expr || !expr.field) {
        return true;
      }
      return String(this.addDsUiForm[expr.field]) === String(expr.eqValue);
    },
    resolveAddDsSecurityOptions() {
      const securityField = this.currentAddDsPanels
        .flatMap((panel) => panel.children || [])
        .find((field) => field.field === 'securityType' && Array.isArray(field.options));
      return securityField ? securityField.options : [];
    },
    ensureActiveAddDsPanel() {
      if (this.visibleAddDsPanels.some((panel) => panel.key === this.activeAddDsPanelKey)) {
        return;
      }
      this.activeAddDsPanelKey = this.visibleAddDsPanels[0]?.key || '';
    },
    syncAddDsUiFormToKvConfigs() {
      this.addDataSourceForm.dsKvConfigs.forEach((config) => {
        if (Object.prototype.hasOwnProperty.call(this.addDsUiForm, config.configName)) {
          config.currentCount = this.addDsUiForm[config.configName];
        }
      });
    },
    getAddDsConfigMap() {
      const configMap = {};
      Object.keys(this.addDsUiForm || {}).forEach((key) => {
        const value = this.addDsUiForm[key];
        if (value === undefined || value === null) {
          return;
        }
        if (value === CERTIFICATE_CONFIGURED_VALUE) {
          return;
        }
        if (this.isPasswordField(key) && String(value) === '') {
          return;
        }
        configMap[key] = String(value);
      });
      return configMap;
    },
    isPasswordField(fieldName) {
      return this.findAddDsField(fieldName)?.type === 'Password';
    },
    findAddDsField(fieldName) {
      const findInFields = (fields = []) => {
        for (const field of fields) {
          if (field.field === fieldName) {
            return field;
          }
          const child = findInFields(field.children || []);
          if (child) {
            return child;
          }
          for (const option of field.options || []) {
            const optionChild = findInFields(option.children || []);
            if (optionChild) {
              return optionChild;
            }
          }
        }
        return null;
      };
      for (const panel of this.currentAddDsPanels) {
        const field = findInFields(panel.children || []);
        if (field) {
          return field;
        }
      }
      return null;
    },
    buildDsSubmitPayload() {
      this.syncAddDsUiFormToKvConfigs();
      return {
        dsId: this.dsId,
        dsType: this.addDataSourceForm.type,
        clusterId: this.addDataSourceForm.queryClusterId,
        envId: this.addDataSourceForm.envId,
        instanceDesc: this.addDataSourceForm.instanceDesc,
        driver: this.addDataSourceForm.driver,
        configMap: this.getAddDsConfigMap()
      };
    }
  }
};
</script>

<style lang="less" scoped>
.add-datasource-step1 {
  padding: 16px 18px;

  &.is-form-step {
    padding: 0;
  }
}

.datasource-type-form-item {
  margin-bottom: 0;

  :deep(.ivu-form-item-content) {
    margin-left: 0 !important;
  }
}

.datasource-type-radio-group {
  display: block;
  width: 100%;
}

.datasource-type-group {
  display: flex;
  width: 100%;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 18px;

  &:last-child {
    margin-bottom: 0;
  }
}

.datasource-type-radio.custom-radio {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 160px;
  height: 52px;
  margin: 0 !important;
  padding: 0;
  border-radius: 4px !important;
  line-height: normal;
  text-align: center;
  vertical-align: top;
  white-space: normal;

  :deep(.ivu-radio) {
    display: none;
  }

  :deep(.ivu-radio-inner) {
    display: none;
  }
}

.add-ds-ui-panel-preview {
  position: relative;
  margin-top: 0;
  background: var(--bg-card);

  :deep(.ivu-tabs-bar) {
    height: 54px;
    margin-bottom: 0;
    padding: 0 24px;
    border-bottom: 1px solid var(--border-primary);
    background: var(--bg-secondary);
  }

  :deep(.ivu-tabs-nav-container),
  :deep(.ivu-tabs-nav-wrap),
  :deep(.ivu-tabs-nav-scroll),
  :deep(.ivu-tabs-nav) {
    height: 54px;
  }

  :deep(.ivu-tabs-bar .ivu-tabs-tab) {
    display: inline-flex !important;
    height: 54px;
    align-items: center;
    justify-content: center;
    margin-right: 6px;
    padding: 0 24px !important;
    border: none !important;
    border-radius: 0;
    background: transparent !important;
    color: var(--text-secondary) !important;
    font-size: 14px;
    line-height: normal !important;
  }

  :deep(.ivu-tabs-bar .ivu-tabs-tab-active) {
    position: relative;
    background: transparent !important;
    color: var(--primary-color) !important;
    font-weight: 500;

    &::after {
      position: absolute;
      right: 18px;
      bottom: 0;
      left: 18px;
      height: 2px;
      background: var(--primary-color);
      content: '';
    }
  }

  :deep(.ivu-tabs-ink-bar) {
    display: none !important;
  }

  :deep(.ivu-tabs-content) {
    overflow: visible;
    padding: 28px 32px 36px;
  }

  :deep(.ivu-tabs-tabpane) {
    overflow: visible;
  }
}

.add-ds-name-form {
  padding: 28px 32px 0;
  background: var(--bg-card);

  :deep(.ivu-form-item) {
    margin-bottom: 26px;
  }
}

.add-ds-name-input {
  width: 462px;
  max-width: 100%;
}

.datasource-type-card {
  display: flex;
  width: 100%;
  height: 100%;
  align-items: center;
  gap: 10px;
  padding: 8px 12px;
  text-align: left;
}

.datasource-type-icon {
  display: flex;
  width: 34px;
  flex: 0 0 34px;
  align-items: center;
  justify-content: center;
  height: 34px;
  line-height: 1;

  :deep(> div) {
    display: inline-flex !important;
    width: 34px;
    height: 34px;
    align-items: center;
    justify-content: center;
  }
}

.datasource-type-name {
  display: -webkit-box;
  flex: 1;
  min-width: 0;
  overflow: hidden;
  color: #17233d;
  font-size: 14px;
  line-height: 18px;
  white-space: normal;
  word-break: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
