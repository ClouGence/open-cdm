<template>
  <div class="approval-form-page">
    <Spin v-if="loading" fix />
    <section class="approval-form-card">
      <Form v-if="selectedProvider" ref="approvalForm" class="approval-form" :model="formData" :rules="formRules" label-position="top">
        <FormItem :label="$t('approval-col-provider')" prop="providerType">
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
                <CustomIcon v-if="provider.iconResource" :resource="provider.iconResource" :alt="$t(provider.labelKey)" size="24px" />
                <span>{{ $t(provider.labelKey) }}</span>
              </button>
            </Tooltip>
          </div>
        </FormItem>

        <FormItem v-if="enableField" :label="$t(enableField.labelKey)" :prop="enableField.key" class="approval-form-enable">
          <i-switch v-model="formData[enableField.key]" true-color="#52C41A" />
        </FormItem>

        <div class="approval-form-grid">
          <FormItem v-for="field in dataFields" :key="field.key" :label="$t(field.labelKey)" :prop="field.key">
            <Input v-if="field.password" v-model="formData[field.key]" type="password" password :placeholder="getPlaceholder(field)" />
            <Input v-else v-model="formData[field.key]" :placeholder="getPlaceholder(field)" />
          </FormItem>
        </div>
      </Form>

      <div v-else-if="!loading" class="approval-form-empty">{{ $t('zan-wu-shu-ju') }}</div>

      <div v-if="selectedProvider" class="approval-form-footer">
        <Button v-if="isEdit" type="error" @click="handleDelete">{{ $t('shan-chu') }}</Button>
        <div class="approval-form-footer__right">
          <Button @click="goBack">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" :loading="submitLoading" @click="handleSubmit">
            {{ isEdit ? $t('bao-cun') : $t('tian-jia') }}
          </Button>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { APPROVAL_PROVIDERS, getProviderByType, isConfigured } from './constant';

export default {
  name: 'ApprovalForm',
  data() {
    return {
      loading: false,
      submitLoading: false,
      configList: [],
      selectedProviderType: '',
      formData: {}
    };
  },
  computed: {
    ...mapState(['myAuth']),
    isEdit() {
      return this.$route.name === 'DMApprovalEdit';
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
        if (!def) return false;
        return isConfigured(this.configMap, def);
      };
    },
    currentProviderFields() {
      return this.selectedProvider ? this.selectedProvider.fields : [];
    },
    enableField() {
      return this.currentProviderFields.find((f) => f.widget === 'switch') || null;
    },
    dataFields() {
      return this.currentProviderFields.filter((f) => f.widget !== 'switch');
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
      await this.fetchConfigs();
      this.bootstrapSelection();
      this.loading = false;
    },
    async fetchConfigs() {
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      if (res.success) {
        this.configList = res.data || [];
      }
    },
    bootstrapSelection() {
      if (this.isEdit) {
        const def = getProviderByType(this.routeType);
        if (!def || !isConfigured(this.configMap, def)) {
          this.$Message.error(this.$t('zan-wu-shu-ju'));
          this.goBack();
          return;
        }
        this.selectedProviderType = this.routeType;
        this.resetFormData(this.routeType, true);
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
      if (!def || isConfigured(this.configMap, def)) return;
      this.selectedProviderType = type;
      this.resetFormData(type, false);
    },
    resetFormData(type, fillFromExisting) {
      const def = getProviderByType(type);
      const next = {};
      if (def) {
        def.fields.forEach((field) => {
          if (field.widget === 'switch') {
            if (fillFromExisting) {
              const c = this.configMap[field.key];
              next[field.key] = (c?.currentCount ?? c?.configValue ?? '') === 'true';
            } else {
              next[field.key] = false;
            }
            return;
          }
          if (fillFromExisting && !field.password) {
            const c = this.configMap[field.key];
            next[field.key] = c?.currentCount ?? c?.configValue ?? '';
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
    serializeFieldValue(field, value) {
      if (field.widget === 'switch') return String(Boolean(value));
      return value ?? '';
    },
    async handleSubmit() {
      const def = this.selectedProvider;
      if (!def) return;
      const valid = await this.$refs.approvalForm.validate();
      if (!valid) return;

      const payload = {};
      def.fields.forEach((field) => {
        if (this.isEdit && field.password && (this.formData[field.key] ?? '') === '') return;
        payload[field.key] = this.serializeFieldValue(field, this.formData[field.key]);
      });

      this.submitLoading = true;
      const ok = await this.persistFields(def, payload);
      this.submitLoading = false;

      if (ok) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.goBack();
      }
    },
    async handleDelete() {
      const def = this.selectedProvider;
      if (!def) return;
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('approval-confirm-delete-x', [this.$t(def.labelKey)]),
        onOk: async () => {
          const cleared = {};
          def.fields.forEach((field) => {
            cleared[field.key] = '';
          });
          this.submitLoading = true;
          const ok = await this.persistFields(def, cleared);
          this.submitLoading = false;
          if (ok) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.goBack();
          }
        }
      });
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
    goBack() {
      this.$router.push('/integrations/approval');
    }
  }
};
</script>

<style lang="less" scoped>
.approval-form-page {
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 30px 36px 18px;
  background: #f6f9fc;
  overflow: auto;
}

.approval-form-card {
  box-sizing: border-box;
  min-height: 100%;
  padding: 20px 24px 22px;
  border: 1px solid #e3eaf2;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 10px 28px rgba(31, 41, 55, 0.04);
}

.approval-form {
  padding-top: 0;

  :deep(.ivu-form-item-label) {
    display: inline-flex;
    align-items: center;
    min-height: 22px;
    padding: 0 0 8px;
    color: #5f6f87;
    font-size: 14px;
    font-weight: 600;
    line-height: 22px;
  }

  :deep(.ivu-form-item-required .ivu-form-item-label::before) {
    margin-right: 4px;
  }
}

.approval-type-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.approval-type-card {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  min-width: 132px;
  height: 48px;
  padding: 0 14px;
  border: 1px solid #d8e4ef;
  border-radius: 7px;
  color: #1f2937;
  font-size: 14px;
  font-weight: 700;
  background: #fff;
  cursor: pointer;
  transition:
    border-color 0.18s ease,
    background 0.18s ease,
    color 0.18s ease;

  &:hover:not(.is-disabled):not(.is-readonly) {
    border-color: #13a86a;
    color: #0f9f55;
  }

  &.is-selected {
    border-color: #13a86a;
    background: #effbf5;
    color: #0f9f55;
  }

  &.is-readonly {
    cursor: default;
  }

  &.is-disabled {
    cursor: not-allowed;
    background: #f5f7fa;
    border-color: #e5e9f0;
    color: #b6bec9;
  }
}

.approval-form-enable {
  max-width: 960px;
}

.approval-form-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
  column-gap: 24px;
  row-gap: 12px;
  max-width: 960px;
}

.approval-form-empty {
  color: #667085;
  font-size: 14px;
}

.approval-form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  max-width: 960px;
  margin-top: 28px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.approval-form-footer__right {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  margin-left: auto;
}

@media (max-width: 900px) {
  .approval-form-page {
    padding: 12px;
  }

  .approval-form-card {
    padding: 18px;
  }

  .approval-form-grid {
    grid-template-columns: 1fr;
  }

  .approval-form-footer {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
