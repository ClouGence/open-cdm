<template>
  <div v-if="visible" class="test-connection-field">
    <Button :loading="loading" @click="handleTestConnection">{{ $t('ce-shi-lian-jie') }}</Button>
    <span v-if="hasResult" class="test-connection-result">
      <Icon :type="success ? 'ios-checkmark-circle' : 'ios-close-circle'" :color="success ? 'green' : 'red'" />
      {{ message }}
    </span>
    <test-connection-modal
      v-model:visible="showTestConnectionModal"
      :test-connection="testConnection"
      :datasource="datasource"
      :handle-close-modal="hideTestConnectionModal"
    />
  </div>
</template>

<script>
import TestConnectionModal from '@/components/function/addDataSource/TestConnectionModal';

export default {
  name: 'TestConnectionField',
  components: {
    TestConnectionModal
  },
  props: {
    datasource: {
      type: Object,
      required: true
    },
    testSecurityTypes: {
      type: Array,
      default: () => []
    },
    validate: {
      type: Function,
      default: null
    },
    inlineWhenClusterSelected: {
      type: Boolean,
      default: true
    }
  },
  emits: ['result'],
  data() {
    return {
      loading: false,
      hasResult: false,
      success: false,
      message: '',
      showTestConnectionModal: false
    };
  },
  computed: {
    visible() {
      return this.testSecurityTypes.includes(this.datasource.securityType);
    },
    clusterId() {
      return this.normalizeClusterId(this.datasource.queryClusterId);
    },
    useInlineTestConnection() {
      return this.inlineWhenClusterSelected && !!this.clusterId;
    }
  },
  methods: {
    normalizeClusterId(clusterId) {
      const normalized = Number(clusterId);
      return Number.isFinite(normalized) && normalized > 0 ? normalized : null;
    },
    buildConnectDsPayload(clusterId) {
      const { account, password, type, securityType, dbName, noValidateDbName, instanceDesc, envId } = this.datasource;
      const host = this.resolveHost();

      return {
        dsType: type,
        clusterId,
        envId,
        instanceDesc,
        driver: this.datasource.driver,
        configMap: {
          dataSourceType: type,
          host,
          securityType,
          userName: account,
          password,
          defaultCatalog: dbName || noValidateDbName,
          ...this.resolveConfigMap()
        }
      };
    },
    resolveHost() {
      if (this.datasource.resolvedHost) {
        return this.datasource.resolvedHost;
      }
      if (this.datasource.host && this.datasource.port) {
        return `${this.datasource.host}:${this.datasource.port}`;
      }
      return this.datasource.host || '';
    },
    resolveConfigMap() {
      const configMap = {};
      (this.datasource.dsKvConfigs || []).forEach((config) => {
        if (!config.configName) {
          return;
        }
        const value = config.currentCount !== undefined && config.currentCount !== null ? config.currentCount : config.defaultValue;
        if (value !== undefined && value !== null) {
          configMap[config.configName] = String(value);
        }
      });
      return configMap;
    },
    async testConnection() {
      if (!this.clusterId) {
        this.$Message.warning(this.$t('bang-ding-ji-qun-bu-neng-wei-kong'));
        return;
      }

      this.loading = true;
      this.hasResult = false;
      this.success = false;
      this.message = '';

      try {
        const res = await this.$services.dmDataSourceConnectDs({
          data: this.buildConnectDsPayload(this.clusterId)
        });
        const result = res.data || {};
        const connectSuccess = res.success && result.success !== false;
        const connectMessage = result.message || res.msg || '';
        this.hasResult = true;
        this.success = connectSuccess;
        this.message = connectSuccess
          ? this.$t('ce-shi-lian-jie-cheng-gong')
          : connectMessage || this.$t('lian-jie-shi-bai-qing-jian-cha-shu-ju-yuan-deng-ru-xin-xi');

        if (connectSuccess) {
          this.$Message.success(this.message);
        } else {
          this.$Message.error(this.message);
        }
        this.$emit('result', { success: this.success, message: this.message });
      } catch (error) {
        this.hasResult = true;
        this.success = false;
        this.message = error?.message || this.$t('ce-shi-lian-jie-shi-bai');
        this.$Message.error(this.message);
        this.$emit('result', { success: false, message: this.message });
      } finally {
        this.loading = false;
      }
    },
    runAfterValidate(callback) {
      if (!this.validate) {
        callback();
        return;
      }

      this.validate((valid) => {
        if (valid) {
          callback();
        }
      });
    },
    handleTestConnection() {
      this.runAfterValidate(() => {
        if (this.useInlineTestConnection) {
          this.testConnection();
          return;
        }
        this.showTestConnectionModal = true;
      });
    },
    hideTestConnectionModal() {
      this.showTestConnectionModal = false;
    }
  }
};
</script>

<style lang="less" scoped>
.test-connection-result {
  margin-left: 12px;
}
</style>
