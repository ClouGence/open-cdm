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
                :field-error="dynamicFieldErrors[field.field] || ''"
                :field-errors="dynamicFieldErrors"
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
      dynamicFieldErrors: {},
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
    validateAddDsForm(callback) {
      this.syncCompositeAddDsFields();
      this.$refs.addLocalDs.validate((valid) => {
        const dynamicValid = this.validateDynamicAddDsFields();
        callback(valid && dynamicValid);
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
      this.dynamicFieldErrors = {};
      this.syncCompositeAddDsFields();
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
    visibleAddDsFieldsFlat() {
      return this.visibleAddDsPanels.flatMap((panel) => panel.visibleFields || []);
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
        .filter((option) => selectedValues.includes(String(this.optionValue(option))))
        .flatMap((option) => option.children || []);
    },
    optionValue(option) {
      if (!option || typeof option !== 'object') {
        return option;
      }
      return option.value ?? option.securityType ?? option.defaultValue ?? '';
    },
    validateDynamicAddDsFields() {
      this.syncCompositeAddDsFields();
      const errors = {};
      this.visibleAddDsFieldsFlat().forEach((field) => {
        if (!this.isDynamicFieldRequired(field) || this.skipDynamicFieldValidate(field)) {
          return;
        }
        const message = this.requiredFieldMessage(field);
        if (field.type === 'NetworkAddress') {
          this.validateNetworkAddressField(field, errors, message);
          return;
        }
        if (this.isEmptyAddDsFieldValue(this.addDsUiForm[field.field], field)) {
          errors[field.field] = message;
        }
      });
      this.dynamicFieldErrors = errors;
      this.switchToFirstDynamicErrorPanel(errors);
      return Object.keys(errors).length === 0;
    },
    switchToFirstDynamicErrorPanel(errors) {
      const errorFields = Object.keys(errors);
      if (!errorFields.length) {
        return;
      }
      const errorPanel = this.visibleAddDsPanels.find((panel) => (panel.visibleFields || []).some((field) => errorFields.includes(field.field)));
      if (errorPanel) {
        this.activeAddDsPanelKey = errorPanel.key;
      }
    },
    skipDynamicFieldValidate(field) {
      if (this.editMode && field.type === 'Password' && String(this.addDsUiForm[field.field] || '') === '') {
        return true;
      }
      return field.field === 'securityType' || ['EnvironmentSelect', 'ClusterSelect', 'DriverSelection'].includes(field.type);
    },
    isDynamicFieldRequired(field) {
      return field.require === true || field.required === true || field.valueRequire === true || field.type === 'CertificateInput';
    },
    validateNetworkAddressField(field, errors, message) {
      const addressField = this.findChildField(field, 'address');
      const portField = this.findChildField(field, 'port');
      const address = this.addDsUiForm.address || '';
      const port = this.addDsUiForm.port || '';
      const host = this.addDsUiForm[field.field] || '';
      const shouldValidatePort =
        !!portField ||
        Object.prototype.hasOwnProperty.call(this.addDsUiForm, 'port') ||
        Object.prototype.hasOwnProperty.call(this.addDataSourceForm, 'port');
      let hasError = false;
      if (addressField && this.isEmptyAddDsFieldValue(address, addressField)) {
        errors[`${field.field}.address`] = this.requiredFieldMessage(addressField);
        hasError = true;
      }
      if (shouldValidatePort && this.isEmptyAddDsFieldValue(port, portField || field)) {
        errors[`${field.field}.port`] = this.requiredFieldMessage(portField || field);
        hasError = true;
      }
      if (!addressField && !portField && this.isEmptyAddDsFieldValue(host, field)) {
        errors[`${field.field}.address`] = message;
        hasError = true;
      }
      if (hasError) {
        errors[field.field] = message;
      }
    },
    findChildField(field, childName) {
      return (field.children || []).find((child) => child.field === childName);
    },
    isEmptyAddDsFieldValue(value, field) {
      if (field.type === 'Check') {
        return value !== true;
      }
      if (field.type === 'CertificateInput') {
        return !value || String(value).trim() === '';
      }
      if (Array.isArray(value)) {
        return value.length === 0;
      }
      return value === undefined || value === null || String(value).trim() === '';
    },
    requiredFieldMessage(field) {
      return this.$t('bu-neng-wei-kong');
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
      this.syncCompositeAddDsFields();
      this.addDataSourceForm.dsKvConfigs.forEach((config) => {
        if (Object.prototype.hasOwnProperty.call(this.addDsUiForm, config.configName)) {
          config.currentCount = this.addDsUiForm[config.configName];
        }
      });
    },
    syncCompositeAddDsFields() {
      this.visibleAddDsFieldsFlat().forEach((field) => {
        if (field.type === 'NetworkAddress') {
          this.syncNetworkAddressField(field);
        }
      });
    },
    syncNetworkAddressField(field) {
      const address = this.formValueOrDefault(this.addDsUiForm, 'address', this.addDataSourceForm.address);
      const port = this.formValueOrDefault(this.addDsUiForm, 'port', this.addDataSourceForm.port);
      let host = this.formValueOrDefault(this.addDsUiForm, field.field, this.addDataSourceForm.host);
      if (address) {
        host = port ? `${address}:${port}` : address;
      }
      this.addDsUiForm.address = address;
      this.addDsUiForm.port = port;
      this.addDsUiForm[field.field] = host;
      this.addDataSourceForm.address = address;
      this.addDataSourceForm.port = port;
      this.addDataSourceForm.host = host;
    },
    formValueOrDefault(form, fieldName, defaultValue) {
      if (form && Object.prototype.hasOwnProperty.call(form, fieldName)) {
        return form[fieldName] ?? '';
      }
      return defaultValue || '';
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
  padding: 24px 28px;

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
  gap: 8px;
  margin-bottom: 12px;

  &:last-child {
    margin-bottom: 0;
  }
}

.datasource-type-radio.custom-radio {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 148px;
  height: 46px;
  margin: 0 !important;
  padding: 0;
  border: 1px solid var(--border-primary) !important;
  border-radius: 6px !important;
  background: var(--bg-card);
  line-height: normal;
  text-align: center;
  transition:
    border-color 0.16s ease,
    box-shadow 0.16s ease,
    background-color 0.16s ease;
  vertical-align: top;
  white-space: normal;

  :deep(.ivu-radio) {
    display: none;
  }

  :deep(.ivu-radio-inner) {
    display: none;
  }

  &:hover {
    border-color: var(--border-secondary) !important;
    background: var(--bg-secondary);
  }

  &.ivu-radio-wrapper-checked {
    border-color: var(--primary-color) !important;
    background: var(--bg-card);
    box-shadow: inset 0 0 0 1px var(--primary-color);
  }
}

.add-ds-ui-panel-preview {
  position: relative;
  margin-top: 0;
  background: var(--bg-card);

  :deep(.ivu-tabs-bar) {
    height: 48px;
    margin-bottom: 0;
    padding: 0 24px;
    border-bottom: 1px solid var(--border-light);
    background: var(--bg-card);
  }

  :deep(.ivu-tabs-nav-container),
  :deep(.ivu-tabs-nav-wrap),
  :deep(.ivu-tabs-nav-scroll),
  :deep(.ivu-tabs-nav) {
    height: 48px;
  }

  :deep(.ivu-tabs-bar .ivu-tabs-tab) {
    display: inline-flex !important;
    height: 48px;
    align-items: center;
    justify-content: center;
    margin-right: 6px;
    padding: 0 18px !important;
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
    color: var(--text-primary) !important;
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
    padding: 24px 32px 40px;
  }

  :deep(.ivu-tabs-tabpane) {
    overflow: visible;
  }
}

.add-datasource-form-stage {
  :deep(.ivu-form-item-required .ivu-form-item-label::before),
  :deep(.ivu-form-item-label::before) {
    display: none !important;
    margin-right: 0 !important;
    content: '' !important;
  }

  :deep(.ivu-form-item-required .ivu-input),
  :deep(.ivu-form-item-required .ivu-select-selection),
  :deep(.ivu-form-item-required .ivu-btn),
  :deep(.ivu-form-item-required .ivu-radio-group),
  :deep(.ivu-form-item-required .ivu-checkbox-inner) {
    box-shadow: inset 0 -1px 0 var(--error-color);
  }

  :deep(.ivu-form-item-required .ivu-input:focus),
  :deep(.ivu-form-item-required .ivu-select-visible .ivu-select-selection) {
    box-shadow:
      inset 0 -1px 0 var(--error-color),
      0 0 0 2px rgba(62, 207, 142, 0.15);
  }
}

.add-ds-name-form {
  padding: 24px 32px 4px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-light);

  :deep(.ivu-form-item) {
    margin-bottom: 20px;
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
  gap: 8px;
  padding: 7px 10px;
  text-align: left;
}

.datasource-type-icon {
  display: flex;
  width: 30px;
  flex: 0 0 30px;
  align-items: center;
  justify-content: center;
  height: 30px;
  line-height: 1;

  :deep(> div) {
    display: inline-flex !important;
    width: 30px;
    height: 30px;
    align-items: center;
    justify-content: center;
  }
}

.datasource-type-name {
  display: -webkit-box;
  flex: 1;
  min-width: 0;
  overflow: hidden;
  color: var(--text-primary);
  font-size: 13px;
  line-height: 17px;
  white-space: normal;
  word-break: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
</style>
