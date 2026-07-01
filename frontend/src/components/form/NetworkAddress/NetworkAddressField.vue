<template>
  <div class="network-address-field">
    <div class="network-address-row">
      <div class="network-address-control network-address-control--host">
        <Input
          class="network-address-input"
          :class="{ 'network-address-input--error': errors.address }"
          :model-value="hostValue"
          :placeholder="addressPlaceholder"
          :disabled="disabled"
          @update:model-value="updateHost"
        />
        <div v-if="errors.address" class="network-address-error">{{ errors.address }}</div>
      </div>
      <div v-if="shouldSeparatePort" class="network-address-port-label">{{ $t('duan-kou') }}</div>
      <div v-if="shouldSeparatePort" class="network-address-control network-address-control--port">
        <Input
          class="network-address-port-input"
          :class="{ 'network-address-input--error': errors.port }"
          :model-value="portValue"
          placeholder="port"
          :disabled="disabled"
          @update:model-value="updatePort"
        />
        <div v-if="errors.port" class="network-address-error">{{ errors.port }}</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'NetworkAddressField',
  props: {
    field: {
      type: Object,
      default: () => ({})
    },
    modelValue: {
      type: Object,
      default: () => ({})
    },
    disabled: {
      type: Boolean,
      default: false
    },
    addressResolver: {
      type: Function,
      default: null
    },
    resolverContext: {
      type: Object,
      default: () => ({})
    },
    errors: {
      type: Object,
      default: () => ({})
    }
  },
  emits: ['update:modelValue', 'change'],
  data() {
    return {
      portValue: '',
      hostValue: ''
    };
  },
  computed: {
    shouldSeparatePort() {
      return this.hasPortField();
    },
    addressPlaceholder() {
      if (!this.shouldSeparatePort) {
        return 'ip:port,domain:port';
      }
      return '';
    }
  },
  watch: {
    modelValue: {
      handler() {
        this.initAddress();
      },
      deep: true,
      immediate: true
    },
    field: {
      handler() {
        this.initAddress();
      },
      deep: true
    }
  },
  methods: {
    initAddress() {
      const defaultPort = this.fieldDefaultValue('port');
      const defaultHostValue = this.fieldDefaultValue('address') || this.defaultValue(this.field.defaultValue) || this.fieldDefaultValue('host');
      const sourceHost = this.fieldValueOrDefault(this.modelValue, 'host', defaultHostValue);
      const sourcePort = this.fieldValueOrDefault(this.modelValue, 'port', defaultPort);
      const parsed = this.parseHost(sourceHost, sourcePort);
      this.hostValue = parsed.host;
      this.portValue = parsed.port;
      this.syncHostValue();
    },
    fieldValueOrDefault(value, fieldName, defaultValue) {
      if (value && Object.prototype.hasOwnProperty.call(value, fieldName)) {
        return value[fieldName] ?? '';
      }
      return defaultValue || '';
    },
    hasPortField() {
      return (this.field.children || []).some((item) => item.field === 'port');
    },
    fieldDefaultValue(fieldName) {
      const child = (this.field.children || []).find((item) => item.field === fieldName);
      return this.defaultValue(child?.defaultValue);
    },
    defaultValue(defaultValue) {
      if (defaultValue && typeof defaultValue === 'object' && Object.prototype.hasOwnProperty.call(defaultValue, 'value')) {
        return defaultValue.value || '';
      }
      return defaultValue || '';
    },
    parseHost(host, port) {
      if (!this.shouldSeparatePort || !host || port) {
        return {
          host: host || '',
          port: port || ''
        };
      }

      const match = String(host).match(/^(.+):([^:]+)$/);
      if (!match) {
        return {
          host,
          port: ''
        };
      }
      return {
        host: match[1],
        port: match[2]
      };
    },
    resolvedAddress(host = this.hostValue, port = this.portValue) {
      if (this.addressResolver) {
        return (
          this.addressResolver({
            host,
            port,
            context: this.resolverContext
          }) || ''
        );
      }
      if (!this.shouldSeparatePort || !port) {
        return host || '';
      }
      if (!host) {
        return '';
      }
      return `${host}:${port}`;
    },
    resolvedAddressContext(host = this.hostValue, port = this.portValue) {
      return {
        host,
        port,
        context: this.resolverContext
      };
    },
    updateHost(value) {
      this.hostValue = value || '';
      this.syncHostValue();
    },
    updatePort(value) {
      this.portValue = value || '';
      this.syncHostValue();
    },
    syncHostValue() {
      const resolvedAddress = this.resolvedAddress();
      if (this.modelValue.host === this.hostValue && this.modelValue.port === this.portValue && this.modelValue.value === resolvedAddress) {
        return;
      }

      const value = {
        ...this.modelValue,
        host: this.hostValue,
        port: this.portValue,
        value: resolvedAddress
      };
      this.$emit('update:modelValue', value);
      this.$emit('change', {
        host: this.hostValue,
        port: this.portValue,
        resolvedAddress
      });
    }
  }
};
</script>

<style lang="less" scoped>
.network-address-field {
  display: inline-flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 0;
}

.network-address-row {
  display: inline-flex;
  align-items: flex-start;
}

.network-address-port-label {
  display: inline-flex;
  height: 36px;
  align-items: center;
  margin: 0 8px;
  color: #515a6e;
}

.network-address-control {
  display: inline-flex;
  flex-direction: column;
}

.network-address-input {
  width: 280px;
}

.network-address-port-input {
  width: 80px;
}

.network-address-input,
.network-address-port-input {
  :deep(.ivu-input-wrapper),
  :deep(.ivu-input) {
    width: 100%;
  }
}

.network-address-input--error {
  :deep(.ivu-input) {
    border-color: var(--error-color);
    box-shadow: inset 0 -1px 0 var(--error-color);
  }
}

.network-address-error {
  margin-top: 4px;
  color: var(--error-color);
  font-size: 12px;
  line-height: 1.2;
}
</style>
