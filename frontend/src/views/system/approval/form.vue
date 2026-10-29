<template>
  <FlowEditor
    v-if="isEdit && selectedProviderType === 'Internal' && !loading"
    :definition="definitionDraft"
    :teams="configuration.teamConfig.teams"
    :participants="configuration.participants"
    :editable="canEdit"
    :saving="submitLoading"
    @save="saveInternal"
    @update:draft="definitionDraft = $event"
    @back="goBack"
  />
  <div v-else class="approval-form-page page-shell">
    <Spin v-if="loading" fix />
    <section class="approval-form-card page-shell__body">
      <Form v-if="selectedProvider" ref="approvalForm" class="approval-form" :model="formData" :rules="formRules" label-position="top">
        <FormItem :label="$t('lei-xing')" required>
          <div class="approval-type-list">
            <Tooltip
              v-for="provider in visibleProviders"
              :key="provider.type"
              :disabled="isEdit || !isProviderAdded(provider.type)"
              :content="$t('approval-provider-already-added')"
              placement="top"
              transfer
            >
              <button
                type="button"
                class="approval-type-card"
                :class="{
                  'is-selected': provider.type === selectedProvider.type,
                  'is-readonly': isEdit,
                  'is-disabled': !isEdit && isProviderAdded(provider.type)
                }"
                @click="handleSelectProvider(provider.type)"
              >
                <img v-if="provider.type === 'Internal'" class="approval-type-icon" :src="cloudDmMark" alt="CloudDM" />
                <CustomIcon v-else-if="provider.iconResource" :resource="provider.iconResource" :alt="$t(provider.labelKey)" size="24px" />
                <span>{{ $t(provider.labelKey) }}</span>
              </button>
            </Tooltip>
          </div>
        </FormItem>

        <div class="approval-form-grid">
          <FormItem v-for="field in currentProviderFields" :key="field.key" :label="$t(field.labelKey)" :prop="field.key">
            <Input v-if="field.password" v-model="formData[field.key]" type="password" password :placeholder="getPlaceholder(field)" />
            <Input v-else v-model="formData[field.key]" :placeholder="getPlaceholder(field)" />
          </FormItem>
        </div>
      </Form>

      <FlowEditor
        v-if="selectedProviderType === 'Internal' && !loading"
        ref="flowEditor"
        :definition="definitionDraft"
        :teams="configuration.teamConfig.teams"
        :participants="configuration.participants"
        :editable="canEdit"
        embedded
        @save="saveInternal"
        @update:draft="definitionDraft = $event"
      />

      <div v-if="!selectedProvider && !loading" class="approval-form-empty">{{ $t('zan-wu-shu-ju') }}</div>

      <div v-if="selectedProvider && selectedProvider.helpUrl" class="approval-form-help">
        <a @click="jumpToHelp">{{ $t('ru-he-dui-jie-x', [$t(selectedProvider.labelKey)]) }}</a>
      </div>
    </section>
    <div v-if="selectedProvider" class="approval-form-footer page-shell__footer">
      <div class="approval-form-footer__actions">
        <Button v-if="canEdit && selectedProviderType !== 'Internal'" :loading="testingConnection" :disabled="submitLoading" @click="testConnection">
          {{ $t('ce-shi-lian-jie') }}
        </Button>
        <Button @click="goBack">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" :loading="submitLoading" @click="handleSubmit">
          {{ isEdit ? $t('bao-cun') : $t('tian-jia') }}
        </Button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import cloudDmMark from '@/assets/logo-clouddm-mark.svg';
import FlowEditor from './FlowEditor.vue';
import { APPROVAL_PROVIDERS, getProviderByType, isConfigured } from './constant';

export default {
  name: 'ApprovalForm',
  components: { FlowEditor },
  data() {
    return {
      cloudDmMark,
      definitionDraft: null,
      configuration: { engineConfig: { version: 0, engines: [] }, teamConfig: { teams: [] }, participants: [], flows: [] },
      loading: false,
      submitLoading: false,
      testingConnection: false,
      configList: [],
      selectedProviderType: '',
      formData: {}
    };
  },
  computed: {
    ...mapState(['myAuth']),
    canEdit() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_W');
    },
    isEdit() {
      return this.$route.name === 'DMApprovalEdit' || this.$route.name === 'DMInternalApprovalEdit';
    },
    routeType() {
      return this.$route.params.type || '';
    },
    selectedProvider() {
      return getProviderByType(this.selectedProviderType);
    },
    configMap() {
      const map = {};
      this.configList.forEach((c) => {
        map[c.configName] = c;
      });
      return map;
    },
    visibleProviders() {
      if (this.isEdit) {
        return this.selectedProvider ? [this.selectedProvider] : [];
      }
      return APPROVAL_PROVIDERS;
    },
    isProviderAdded() {
      return (type) => {
        const def = getProviderByType(type);
        if (!def || type === 'Internal') return false;
        return isConfigured(this.configMap, def);
      };
    },
    currentProviderFields() {
      return this.selectedProvider ? this.selectedProvider.fields : [];
    },
    formRules() {
      const rules = {};
      this.currentProviderFields.forEach((field) => {
        if (field.required && !(this.isEdit && field.password)) {
          rules[field.key] = [
            {
              required: true,
              message: this.$t('sso-field-required', [this.$t(field.labelKey)]),
              trigger: 'blur'
            }
          ];
        }
      });
      return rules;
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    async init() {
      this.loading = true;
      try {
        await this.fetchConfigs();
        const response = await this.$services.dmApprovalConfigurationQuery();
        if (response.success) {
          this.configuration = response.data;
          this.bootstrapSelection();
        }
      } finally {
        this.loading = false;
      }
    },
    async fetchConfigs() {
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      if (res.success) {
        this.configList = res.data;
      }
    },
    bootstrapSelection() {
      if (this.$route.name === 'DMInternalApprovalEdit') {
        const id = this.$route.params.engineId;
        const engine = this.configuration.engineConfig.engines.find((e) => e.engineConfigId === id);
        const flow = this.configuration.flows.find((f) => f.engineConfigId === id);
        if (!engine || !flow) {
          this.$Message.error(this.$t('zan-wu-shu-ju'));
          this.goBack();
          return;
        }
        this.selectedProviderType = 'Internal';
        this.formData = { engineName: engine.engineName };
        this.definitionDraft = flow.definition;
        return;
      }
      if (this.isEdit) {
        const def = getProviderByType(this.routeType);
        if (!def || !isConfigured(this.configMap, def)) {
          this.$Message.error(this.$t('zan-wu-shu-ju'));
          this.goBack();
          return;
        }
        this.selectedProviderType = def.type;
        this.resetFormData(def.type, true);
        return;
      }
      const first = APPROVAL_PROVIDERS.find((p) => !isConfigured(this.configMap, p));
      if (!first) {
        this.goBack();
        return;
      }
      this.selectedProviderType = first.type;
      this.resetFormData(first.type, false);
    },
    handleSelectProvider(type) {
      if (this.isEdit) return;
      const def = getProviderByType(type);
      if (!def || (type !== 'Internal' && isConfigured(this.configMap, def))) return;
      this.selectedProviderType = type;
      this.resetFormData(type, false);
    },
    resetFormData(type, fillFromExisting) {
      const def = getProviderByType(type);
      const next = {};
      if (def) {
        def.fields.forEach((field) => {
          if (fillFromExisting && !field.password) {
            const c = this.configMap[field.key];
            next[field.key] = c?.configValue ?? '';
          } else {
            next[field.key] = '';
          }
        });
      }
      this.formData = next;
    },
    getPlaceholder(field) {
      if (field.required) return '';
      const config = this.configMap[field.key];
      const defaultVal = config?.defaultValue;
      return defaultVal ? this.$t('sso-default-placeholder', [defaultVal]) : '';
    },
    async testConnection() {
      const valid = await this.$refs.approvalForm.validate();
      if (!valid) return;
      const configs = {};
      this.selectedProvider.fields.forEach((field) => {
        if (this.isEdit && field.password && !this.formData[field.key]) return;
        configs[field.key] = this.formData[field.key] ?? '';
      });
      this.testingConnection = true;
      try {
        const response = await this.$services.dmApprovalTestConnection({ data: { provider: this.selectedProviderType, configs } });
        if (response.success) {
          this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
        }
      } finally {
        this.testingConnection = false;
      }
    },
    async handleSubmit() {
      const def = this.selectedProvider;
      if (!def) return;
      const valid = await this.$refs.approvalForm.validate();
      if (!valid) return;

      if (def.type === 'Internal') {
        this.$refs.flowEditor.save();
        return;
      }
      const payload = {};
      def.fields.forEach((field) => {
        if (this.isEdit && field.password && (this.formData[field.key] ?? '') === '') return;
        payload[field.key] = this.formData[field.key] ?? '';
      });

      this.submitLoading = true;
      try {
        const ok = await this.persistFields(def, payload);
        if (ok) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.goBack();
        }
      } finally {
        this.submitLoading = false;
      }
    },
    async persistFields(def, fieldValues) {
      const updateConfigs = {};
      const needCreateConfigs = {};

      def.fields.forEach((field) => {
        if (!Object.prototype.hasOwnProperty.call(fieldValues, field.key)) return;
        const value = fieldValues[field.key];
        const config = this.configMap[field.key];
        if (config) {
          updateConfigs[field.key] = value;
        } else {
          needCreateConfigs[field.key] = value;
        }
      });

      const res = await this.$services.rdpUserConfigUpsertUserConfigs({
        data: { updateConfigs, needCreateConfigs }
      });
      if (!res.success) {
        this.$Message.error(res.msg || this.$t('cao-zuo-shi-bai'));
        return false;
      }
      return true;
    },
    async saveInternal(definition) {
      if (!this.canEdit) return;
      const engineConfigId = this.$route.params.engineId || null;
      const flow = this.configuration.flows.find((f) => f.engineConfigId === engineConfigId);
      this.submitLoading = true;
      try {
        const res = await this.$services.dmApprovalSaveEngine({
          data: {
            engineConfigId,
            engineName: this.formData.engineName.trim(),
            configVersion: this.configuration.engineConfig.version,
            version: flow?.version || 0,
            definition
          }
        });
        if (res.success) {
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.goBack();
        }
      } finally {
        this.submitLoading = false;
      }
    },
    goBack() {
      this.$router.push('/integrations/approval');
    },
    jumpToHelp() {
      const url = this.selectedProvider?.helpUrl || '';
      if (url) {
        window.open(url, 'blank');
      }
    }
  }
};
</script>

<style lang="less" scoped>
.approval-form-page {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
}

.approval-form-card {
  display: flex;
  flex-direction: column;
  gap: 24px;
  flex: 1;
  min-height: 0;
  overflow: auto;
  box-sizing: border-box;

  padding: 16px 24px;
}

.approval-form {
  flex-shrink: 0;
  padding-top: 0;

  :deep(.ivu-form-item) {
    margin-bottom: 16px;
  }
}

.approval-type-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.approval-type-icon {
  width: 24px;
  height: 24px;
  object-fit: contain;
  flex-shrink: 0;
}

.approval-type-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 132px;
  height: 48px;
  padding: 0 14px;
  border: 1px solid var(--border-primary);
  border-radius: 6px;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
  background: var(--bg-primary);
  cursor: pointer;
  transition:
    border-color 0.18s ease,
    background 0.18s ease,
    color 0.18s ease;

  &:hover:not(.is-disabled):not(.is-readonly) {
    border-color: var(--primary-color);
    color: var(--primary-color);
  }

  &.is-selected {
    border-color: var(--primary-color);
    background: var(--bg-hover);
    color: var(--primary-color);
  }

  &.is-readonly {
    cursor: default;
  }

  &.is-disabled {
    cursor: not-allowed;
    background: var(--bg-secondary);
    border-color: var(--border-primary);
    color: var(--text-disabled);
  }
}

.approval-form-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  row-gap: 16px;
  max-width: 720px;

  :deep(.ivu-form-item) {
    margin-bottom: 0;
  }
}

.approval-form-empty {
  color: var(--text-secondary);
  font-size: 14px;
}

.approval-form-help {
  max-width: 720px;
}

.approval-form-footer {
  flex: none;
  padding: 16px 24px;
  border-top: 1px solid var(--border-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.approval-form-footer__actions {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

@media (max-width: 900px) {
  .approval-form-card {
    padding: 16px;
  }

  .approval-form-grid {
    grid-template-columns: 1fr;
  }

  .approval-form-footer {
    padding: 16px;
  }
}
</style>
