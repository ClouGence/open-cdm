<template>
  <div class="add-datasource-step1" :class="{ 'is-form-step': currentStep === 1 }">
    <section v-if="currentStep === 1" class="add-datasource-card datasource-config-card-panel">
      <div class="datasource-config-layout">
        <nav class="datasource-config-menu">
          <div class="datasource-config-menu-group">
            <div class="datasource-config-menu-children">
              <button
                v-for="panel in visibleAddDsPanels"
                :key="panel.key"
                type="button"
                class="datasource-config-menu-child"
                :class="{ active: activeAddDsPanelKey === panel.key }"
                @click="activeAddDsPanelKey = panel.key"
              >
                {{ panel.titleI18N || panel.key }}
              </button>
            </div>
          </div>
        </nav>

        <div class="datasource-config-scroll">
          <Form
            ref="addLocalDs"
            class="datasource-config-form"
            :model="addDataSourceForm"
            label-position="right"
            :label-width="160"
            :rules="addDataSourceRule"
          >
            <div v-if="visibleAddDsPanels.length" class="add-ds-ui-panel-preview">
              <Spin v-if="addDsConfigLoading" fix />
              <div class="datasource-config-content">
                <div class="datasource-type-form-row">
                  <div class="datasource-type-form-label">{{ $t('shu-ju-yuan-lei-xing') }}</div>
                  <div class="datasource-type-form-control">
                    <span v-if="currentDataSourceType" class="datasource-type-form-value">
                      <DataSourceIcon size="20px" :type="currentDataSourceType.dsKey" leftMargin="0"></DataSourceIcon>
                      <span class="datasource-type-form-name" :title="currentDataSourceType.displayName">
                        {{ currentDataSourceType.displayName }}
                      </span>
                    </span>
                    <span v-else class="datasource-type-empty">{{ $t('zan-wu-shu-ju') }}</span>
                    <Button v-if="!editMode" class="datasource-type-switch-btn" size="small" @click="handleShowDataSourceTypeModal">
                      {{ $t('qie-huan') }}
                    </Button>
                  </div>
                </div>
                <div
                  v-for="(panel, panelIndex) in visibleAddDsPanels"
                  v-show="activeAddDsPanelKey === panel.key"
                  :key="panel.key"
                  class="datasource-config-pane"
                >
                  <div v-if="panelIndex === 0" class="add-ds-name-form">
                    <FormItem prop="instanceDesc" :label="$t('shu-ju-yuan-ming-cheng')" required>
                      <Input v-model.trim="addDataSourceForm.instanceDesc" class="add-ds-name-input" />
                    </FormItem>
                  </div>
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
                </div>
              </div>
            </div>
          </Form>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import { UiFormField } from '@/components/form';
import { flattenDsSupportNameGroups, normalizeDsSupportNameGroups } from '@/utils/datasourceSupport';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';

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
    flatDataSourceTypes() {
      return flattenDsSupportNameGroups(this.dataSourceTypes);
    },
    currentDataSourceType() {
      const currentType = this.addDataSourceForm.type;
      const matchedType = this.flatDataSourceTypes.find((type) => type.dsKey === currentType);
      if (matchedType) {
        return matchedType;
      }
      return currentType
        ? {
            dsKey: currentType,
            displayName: currentType
          }
        : null;
    },
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
      this.dataSourceTypes = normalizeDsSupportNameGroups(supportNames);

      if (!this.dataSourceTypes.length) {
        return;
      }

      const allTypes = this.dataSourceTypes.flatMap((group) => group.map((type) => type.dsKey));
      if (!allTypes.includes(this.addDataSourceForm.type)) {
        this.addDataSourceForm.type = this.dataSourceTypes[0][0].dsKey;
        if (this.currentStep === 1) {
          this.fetchAddDsConfig();
        }
      }
    },
    handleShowDataSourceTypeModal() {
      if (this.editMode) {
        return;
      }
      this.$bus.emit(EVENT_BUS_NAME_LIST.SHOW_ADD_DATASOURCE_TYPE_MODAL);
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
        const addLocalDsRef = Array.isArray(this.$refs.addLocalDs) ? this.$refs.addLocalDs[0] : this.$refs.addLocalDs;
        if (typeof addLocalDsRef?.clearValidate === 'function') {
          addLocalDsRef.clearValidate(field);
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
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  gap: 0;
  height: 100%;
  min-height: 0;
  padding: 0;
}

.add-datasource-card {
  background: #ffffff;
  overflow: hidden;
}

.datasource-config-layout {
  display: flex;
  flex: 1 1 auto;
  min-height: 0;
  background: #ffffff;
}

.datasource-type-empty {
  display: flex;
  align-items: center;
  color: #8b98a8;
  font-size: 13px;
}

.datasource-config-card-panel {
  display: flex;
  flex: 1 1 auto;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
  --datasource-form-control-width: 380px;
  --datasource-form-driver-family-width: 208px;
  --datasource-form-driver-version-label-width: 52px;
  --datasource-form-driver-version-width: 96px;
  --datasource-form-driver-status-width: 84px;
  --datasource-form-driver-message-width: 160px;
  --datasource-form-driver-row-width: calc(
    var(--datasource-form-control-width) + var(--datasource-form-inline-gap) + var(--datasource-form-driver-status-width) +
      var(--datasource-form-inline-gap) + var(--datasource-form-driver-message-width)
  );
  --datasource-form-inline-gap: 12px;
  --network-address-total-width: var(--datasource-form-control-width);
  --network-address-port-label-width: 52px;
  --network-address-port-width: 96px;
  --network-address-gap: var(--datasource-form-inline-gap);
}

.datasource-config-scroll {
  flex: 1 1 auto;
  height: auto;
  min-width: 0;
  min-height: 0;
  overflow: auto;
  border-radius: inherit;
}

.datasource-config-form {
  height: 100%;
  min-height: 0;
  border-radius: inherit;
}

.add-datasource-form-stage,
.datasource-config-card-panel {
  :deep(.ivu-form-item) {
    margin-bottom: 0;
  }

  :deep(.ivu-form-item-content) {
    padding-bottom: 18px;
  }

  :deep(.ivu-form-item-error-tip) {
    top: 30px;
    padding-top: 0;
    color: var(--error-color);
    font-size: 12px;
    line-height: 16px;
    white-space: nowrap;
  }

  :deep(.ivu-form-item-required .ivu-form-item-label::before),
  :deep(.ivu-form-item-label::before) {
    display: none;
    margin-right: 0;
    content: '';
  }

  :deep(.ivu-form-item-required .ivu-form-item-label::before) {
    display: inline-block !important;
    margin-right: 4px !important;
    color: var(--error-color);
    font-family: SimSun, sans-serif;
    line-height: 1;
    content: '*' !important;
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
  padding: 0 0 4px;
  background: #ffffff;

  :deep(.ivu-form-item) {
    margin-bottom: 0;
  }
}

.add-ds-name-input {
  width: var(--datasource-form-control-width);
  max-width: 100%;
}

.datasource-config-card-panel {
  :deep(.ui-input-field) {
    width: var(--datasource-form-control-width) !important;
  }

  :deep(.ivu-form-item:not(.driver-selection-form-item) > .ivu-form-item-content > .ivu-select),
  :deep(.cluster-select-field),
  :deep(.cluster-select-field > .ivu-select) {
    width: var(--datasource-form-control-width) !important;
  }

  :deep(.driver-selection-field),
  :deep(.driver-selection-row) {
    width: var(--datasource-form-driver-row-width) !important;
  }

  :deep(.driver-selection-row) {
    display: grid;
    grid-template-columns:
      var(--datasource-form-driver-family-width)
      var(--datasource-form-driver-version-label-width)
      var(--datasource-form-driver-version-width)
      var(--datasource-form-driver-status-width)
      minmax(0, var(--datasource-form-driver-message-width));
    column-gap: var(--datasource-form-inline-gap);
    align-items: center;
  }

  :deep(.driver-family-select) {
    width: var(--datasource-form-driver-family-width) !important;
    flex: 0 0 var(--datasource-form-driver-family-width);
  }

  :deep(.driver-version-label) {
    width: var(--datasource-form-driver-version-label-width) !important;
    flex: 0 0 var(--datasource-form-driver-version-label-width);
    justify-content: flex-end;
  }

  :deep(.driver-version-select) {
    width: var(--datasource-form-driver-version-width) !important;
    flex: 0 0 var(--datasource-form-driver-version-width);
  }

  :deep(.driver-status-detail) {
    grid-column: 5;
    max-width: var(--datasource-form-driver-message-width);
  }
}

.add-ds-ui-panel-preview {
  position: relative;
  display: flex;
  height: 100%;
  min-height: 0;
  margin-top: 0;
  background: #ffffff;
  border-radius: inherit;
}

.datasource-config-menu {
  position: relative;
  display: flex;
  align-self: stretch;
  flex: 0 0 188px;
  flex-direction: column;
  width: 188px;
  min-height: 100%;
  padding: 16px 10px;
  background: transparent;
  border-right: none;

  &::after {
    position: absolute;
    top: 20px;
    right: 0;
    bottom: 20px;
    width: 1px;
    background: var(--border-light);
    content: '';
    pointer-events: none;
  }
}

.datasource-config-menu-group {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.datasource-config-menu-children {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.datasource-config-menu-child {
  position: relative;
  display: flex;
  width: 100%;
  height: 38px;
  align-items: center;
  border: none;
  border-radius: 6px;
  padding: 0 14px 0 30px;
  background: transparent;
  color: var(--text-secondary);
  cursor: pointer;
  font-size: 14px;
  line-height: normal;
  text-align: left;
  transition:
    background-color 0.16s ease,
    color 0.16s ease;

  &.active {
    background: #effbf5;
    color: var(--text-primary);
    font-weight: 500;

    &::after {
      position: absolute;
      top: 8px;
      bottom: 8px;
      left: 14px;
      width: 3px;
      border-radius: 999px;
      background: var(--primary-color);
      content: '';
    }
  }
}

.datasource-config-content {
  flex: 1 1 auto;
  height: 100%;
  min-width: 0;
  box-sizing: border-box;
  overflow: visible;
  padding: 18px 32px 18px;
}

.datasource-type-form-row {
  display: grid;
  grid-template-columns: 160px minmax(0, 1fr);
  align-items: center;
  min-height: 36px;
  margin-bottom: 12px;
}

.datasource-type-form-label {
  padding: 7px 12px 7px 0;
  color: var(--text-secondary);
  font-size: 14px;
  line-height: 22px;
  text-align: right;
  white-space: nowrap;
}

.datasource-type-form-control {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 12px;
}

.datasource-type-form-value {
  display: inline-flex;
  max-width: var(--datasource-form-control-width);
  min-width: 0;
  align-items: center;
  gap: 8px;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
  line-height: 22px;
}

.datasource-type-form-name {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.datasource-type-switch-btn {
  flex: 0 0 auto;
  height: 30px;
  padding: 0 12px;
}

.datasource-config-pane {
  overflow: visible;
}

@media (max-width: 768px) {
  .datasource-config-menu {
    flex-basis: 164px;
    width: 164px;
  }

  .datasource-config-content {
    padding: 20px 20px 32px;
  }
}
</style>
