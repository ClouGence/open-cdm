<template>
  <div class="ssh-tunnel-field">
    <Select
      v-model="selectedTunnel"
      :disabled="disabled"
      :loading="loading"
      filterable
      transfer
      class="ssh-tunnel-select"
      @on-open-change="handleOpenChange"
    >
      <Option :value="disabledTunnelValue">
        {{ $t('jin-yong') }}
      </Option>
      <Option v-for="item in sshConfigs" :key="item.id" :value="String(item.id)">
        {{ sshConfigLabel(item) }}
      </Option>
    </Select>
    <Button :disabled="disabled" @click="handleSettings">
      {{ $t('she-zhi') }}
    </Button>
    <Button :loading="testing" :disabled="disabled || !normalizedClusterId || !sshEnabled || !selectedSshConfigId" @click="handleTest">
      {{ $t('ce-shi') }}
    </Button>
    <span v-if="testResultStatus === 'success'" class="ssh-tunnel-test-result ssh-tunnel-test-result--success">
      <Icon type="ios-checkmark-circle" class="ssh-tunnel-test-result__icon" />
    </span>
    <span v-else-if="testResultStatus === 'error'" class="ssh-tunnel-test-result ssh-tunnel-test-result--error">
      <Icon type="ios-close-circle" class="ssh-tunnel-test-result__icon" />
      <span>{{ testResultMessage }}</span>
    </span>
    <Modal
      v-model="settingsVisible"
      :title="field.titleI18N || 'SSH/SSL'"
      width="1180"
      footer-hide
      @on-visible-change="handleSettingsVisibleChange"
      @on-cancel="refreshSshConfigsAfterSettingsClosed"
    >
      <ssh-config-list
        v-if="settingsVisible"
        embedded
        :cluster-id="normalizedClusterId"
        :selected-config-id="selectedSshConfigId"
        @saved="handleConfigSaved"
        @deleted="loadSshConfigs"
      />
    </Modal>
  </div>
</template>

<script>
import SshConfigList from '@/views/sshConfig/index.vue';

export default {
  name: 'SshTunnelField',
  components: {
    SshConfigList
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
    disabled: {
      type: Boolean,
      default: false
    },
    clusterId: {
      type: [Number, String],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      testing: false,
      testResultStatus: '',
      testResultMessage: '',
      settingsVisible: false,
      refreshingAfterSettingsClosed: false,
      disabledTunnelValue: '__disabled__',
      sshConfigs: []
    };
  },
  computed: {
    normalizedClusterId() {
      if (this.clusterId === null || this.clusterId === undefined || this.clusterId === '') {
        return null;
      }
      return Number(this.clusterId);
    },
    sshEnabled: {
      get() {
        return this.form.sshProxyEnabled === true || String(this.form.sshProxyEnabled) === 'true';
      },
      set(value) {
        this.form.sshProxyEnabled = value ? 'true' : 'false';
        if (!value) {
          this.form.sshConfigId = '';
        }
      }
    },
    selectedTunnel: {
      get() {
        return this.sshEnabled && this.selectedSshConfigId ? this.selectedSshConfigId : this.disabledTunnelValue;
      },
      set(value) {
        if (!value || value === this.disabledTunnelValue) {
          this.sshEnabled = false;
          this.clearTestResult();
          return;
        }
        this.form.sshProxyEnabled = 'true';
        this.selectedSshConfigId = value;
        this.clearTestResult();
      }
    },
    selectedSshConfigId: {
      get() {
        const value = this.form.sshConfigId;
        return value === undefined || value === null ? '' : String(value);
      },
      set(value) {
        this.form.sshConfigId = value || '';
      }
    }
  },
  mounted() {
    this.loadSshConfigs();
  },
  watch: {
    normalizedClusterId() {
      this.selectedTunnel = this.disabledTunnelValue;
      this.clearTestResult();
      this.loadSshConfigs();
    },
    settingsVisible(value) {
      if (!value) {
        this.refreshSshConfigsAfterSettingsClosed();
      }
    }
  },
  methods: {
    async loadSshConfigs() {
      this.loading = true;
      try {
        const res = await this.$services.dmSshConfigList({ data: { search: '', clusterId: this.normalizedClusterId } });
        if (res.success) {
          this.sshConfigs = res.data || [];
        }
      } finally {
        this.loading = false;
      }
    },
    handleOpenChange(open) {
      if (open && !this.sshConfigs.length) {
        this.loadSshConfigs();
      }
    },
    sshConfigLabel(item) {
      const address = item.host ? `${item.host}${item.port ? `:${item.port}` : ''}` : '';
      return address ? `${item.name} (${address})` : item.name;
    },
    handleSettings() {
      if (!this.normalizedClusterId) {
        this.$Message.warning(this.$t('bang-ding-ji-qun-bu-neng-wei-kong'));
        return;
      }
      this.settingsVisible = true;
    },
    handleSettingsVisibleChange(visible) {
      if (!visible) {
        this.refreshSshConfigsAfterSettingsClosed();
      }
    },
    async refreshSshConfigsAfterSettingsClosed() {
      if (this.refreshingAfterSettingsClosed) {
        return;
      }
      this.refreshingAfterSettingsClosed = true;
      try {
        await this.loadSshConfigs();
      } finally {
        this.refreshingAfterSettingsClosed = false;
      }
    },
    handleConfigSaved(id) {
      this.loadSshConfigs();
      this.selectedTunnel = String(id);
    },
    async handleTest() {
      this.testing = true;
      this.clearTestResult();
      try {
        const res = await this.$services.dmSshConfigTestConnection({
          data: {
            clusterId: this.normalizedClusterId,
            sshConfigId: this.selectedSshConfigId
          }
        });
        if (res.success && res.data?.success) {
          this.testResultStatus = 'success';
          this.testResultMessage = res.data.message || this.$t('ce-shi-lian-jie-cheng-gong');
          return;
        }
        this.setTestError(res.data?.message || res.message || this.$t('ce-shi-lian-jie-shi-bai'));
      } catch (error) {
        this.setTestError(error?.message || this.$t('ce-shi-lian-jie-shi-bai'));
      } finally {
        this.testing = false;
      }
    },
    setTestError(message) {
      this.testResultStatus = 'error';
      this.testResultMessage = message;
    },
    clearTestResult() {
      this.testResultStatus = '';
      this.testResultMessage = '';
    }
  }
};
</script>

<style lang="less" scoped>
.ssh-tunnel-field {
  display: inline-flex;
  align-items: center;
  gap: 12px;
}

.ssh-tunnel-select {
  width: 280px;
}

.ssh-tunnel-test-result {
  min-width: 0;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  line-height: 1.4;
}

.ssh-tunnel-test-result__icon {
  font-size: 20px;
}

.ssh-tunnel-test-result--success {
  color: #19be6b;
}

.ssh-tunnel-test-result--error {
  color: #ed4014;
}
</style>
