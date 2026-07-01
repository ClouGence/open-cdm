<template>
  <FormItem v-if="field.type === 'EnvironmentSelect'" :label="fieldLabel" :required="field.require" prop="envId" :error="fieldError">
    <environment-select-field :model-value="dataSourceForm.envId" :env-list="envList" @change="updateEnvironment" />
  </FormItem>
  <FormItem
    v-else-if="field.type === 'ClusterSelect' && showQueryConfig"
    :label="fieldLabel"
    :required="field.require"
    prop="queryClusterId"
    :error="fieldError"
  >
    <cluster-select-field
      :model-value="dataSourceForm.queryClusterId"
      :cluster-list="clusterList"
      :current-cluster="currentQueryCluster"
      @change="updateCluster"
    />
  </FormItem>
  <FormItem v-else-if="field.type === 'DriverSelection' && hasDriverFamilies" class="driver-selection-form-item" :label="fieldLabel">
    <driver-selection-field
      :data-source-type="dataSourceForm.type"
      :driver-family-map="driverFamilyMap"
      :query-cluster-id="dataSourceForm.queryClusterId"
      :require-cluster="showQueryConfig"
      :current-query-cluster="currentQueryCluster"
      :current-step="currentStep"
      v-model:driverFamily="dataSourceForm.driverFamily"
      v-model:driverVersion="dataSourceForm.driverVersion"
      v-model:driverValue="dataSourceForm.driver"
      @update:driverFamily="updateDriverFamily"
      @update:driverVersion="updateDriverVersion"
      @update:driverReady="$emit('update:driverReady', $event)"
    />
  </FormItem>
  <FormItem v-else-if="field.type === 'NetworkAddress'" :label="fieldLabel" :required="field.require" :error="''">
    <network-address-field
      :model-value="networkAddressValue"
      :field="field"
      :disabled="isFieldDisabled(field)"
      :address-resolver="addressResolver"
      :resolver-context="dataSourceForm"
      :errors="networkAddressErrors"
      @update:modelValue="updateNetworkAddress"
    />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'MaxComputeEndpoint'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <max-compute-endpoint-field :field="field" :form="form" :data-source-form="dataSourceForm" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem
    v-else-if="field.type === 'Options' || field.type === 'MultipleOptions'"
    :label="fieldLabel"
    :required="field.require"
    :error="fieldError"
  >
    <options-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'TransactionControl'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <transaction-control-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'SshTunnel'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <ssh-tunnel-field :field="field" :form="form" :cluster-id="dataSourceForm.queryClusterId" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'CertificateInput'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <certificate-input-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'Check'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <check-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
  </FormItem>
  <FormItem v-else-if="field.type === 'TextArea'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <text-area-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
  <FormItem v-else-if="field.type === 'Input' || field.type === 'Password'" :label="fieldLabel" :required="field.require" :error="fieldError">
    <input-field :field="field" :form="form" :disabled="isFieldDisabled(field)" />
    <span v-if="field.descI18N" class="ui-form-field-desc" v-html="field.descI18N"></span>
  </FormItem>
</template>
<script>
import CheckField from './Check/CheckField.vue';
import CertificateInputField from './CertificateInput/CertificateInputField.vue';
import ClusterSelectField from './ClusterSelect/ClusterSelectField.vue';
import DriverSelectionField from './DriverSelection/DriverSelectionField.vue';
import EnvironmentSelectField from './EnvironmentSelect/EnvironmentSelectField.vue';
import InputField from './Input/InputField.vue';
import MaxComputeEndpointField from './MaxComputeEndpoint/MaxComputeEndpointField.vue';
import NetworkAddressField from './NetworkAddress/NetworkAddressField.vue';
import OptionsField from './Options/OptionsField.vue';
import SshTunnelField from './SshTunnel/SshTunnelField.vue';
import TextAreaField from './TextArea/TextAreaField.vue';
import TransactionControlField from './TransactionControl/TransactionControlField.vue';

export default {
  name: 'UiFormField',
  components: {
    CheckField,
    CertificateInputField,
    ClusterSelectField,
    DriverSelectionField,
    EnvironmentSelectField,
    InputField,
    MaxComputeEndpointField,
    NetworkAddressField,
    OptionsField,
    SshTunnelField,
    TextAreaField,
    TransactionControlField
  },
  props: {
    field: {
      type: Object,
      required: true
    },
    form: {
      type: Object,
      required: true
    },
    fieldError: {
      type: String,
      default: ''
    },
    fieldErrors: {
      type: Object,
      default: () => ({})
    },
    dataSourceForm: {
      type: Object,
      required: true
    },
    driverFamilyMap: {
      type: Object,
      default: () => ({})
    },
    currentQueryCluster: {
      type: Object,
      default: () => ({})
    },
    envList: {
      type: Array,
      default: () => []
    },
    clusterList: {
      type: Array,
      default: () => []
    },
    showQueryConfig: {
      type: Boolean,
      default: false
    },
    currentStep: {
      type: Number,
      default: 0
    },
    addressResolver: {
      type: Function,
      default: null
    }
  },
  computed: {
    fieldLabel() {
      return this.field.titleI18N || this.field.field;
    },
    hasDriverFamilies() {
      return !!(this.driverFamilyMap[this.dataSourceForm.type] || []).length;
    },
    networkAddressValue() {
      const value = this.dataSourceForm.hostList?.[0] || {};
      return {
        ...value,
        host: this.formValueOrDefault('address', value.host ?? this.form[this.field.field] ?? ''),
        port: this.formValueOrDefault('port', value.port ?? '')
      };
    },
    networkAddressErrors() {
      return {
        address: this.fieldErrors[`${this.field.field}.address`] || this.fieldError || '',
        port: this.fieldErrors[`${this.field.field}.port`] || ''
      };
    }
  },
  emits: ['update:driverReady', 'envChange', 'clusterChange'],
  methods: {
    isFieldDisabled(field) {
      return field.readOnly || field.addReadOnly;
    },
    updateEnvironment(value) {
      this.dataSourceForm.envId = value;
      this.$emit('envChange', value);
    },
    updateCluster(value) {
      this.dataSourceForm.queryClusterId = value;
      this.$emit('clusterChange', value);
    },
    updateDriverFamily(value) {
      this.form.driverFamily = value || '';
    },
    updateDriverVersion(value) {
      this.form.driverVersion = value || '';
    },
    formValueOrDefault(fieldName, defaultValue) {
      if (Object.prototype.hasOwnProperty.call(this.form, fieldName)) {
        return this.form[fieldName] ?? '';
      }
      return defaultValue || '';
    },
    updateNetworkAddress(value) {
      const hostList = Array.isArray(this.dataSourceForm.hostList) ? [...this.dataSourceForm.hostList] : [];
      hostList[0] = {
        type: 'public',
        display: true,
        ...(hostList[0] || {}),
        ...value
      };
      this.dataSourceForm.hostList = hostList;
      this.dataSourceForm.address = value.host || '';
      this.dataSourceForm.host = value.value || '';
      this.dataSourceForm.port = value.port || '';
      this.dataSourceForm.resolvedHost = value.value || '';
      this.form.address = value.host || '';
      this.form[this.field.field] = value.value || '';
      this.form.port = value.port || '';
    }
  }
};
</script>

<style lang="less" scoped>
.driver-selection-form-item {
  margin-bottom: 24px;
}

.ui-form-field-desc {
  margin-left: 12px;
  color: #808695;
  white-space: nowrap;
}
</style>
