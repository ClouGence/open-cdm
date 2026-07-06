<template>
  <div class="sso-form-page">
    <Spin v-if="loading" fix />
    <section class="sso-form-card">
      <Form v-if="selectedProvider" ref="ssoForm" class="sso-form" :model="formData" :rules="formRules" label-position="top">
        <FormItem :label="$t('lei-xing')" required>
          <div class="sso-type-list">
            <Tooltip
              v-for="provider in visibleProviders"
              :key="provider.type"
              :disabled="isEdit || !isProviderEnabled(provider.type)"
              :content="providerTooltip(provider.type)"
              placement="top"
              transfer
            >
              <button
                type="button"
                class="sso-type-card"
                :class="{
                  'is-selected': provider.type === selectedProvider.type,
                  'is-readonly': isEdit,
                  'is-disabled': !isEdit && isProviderEnabled(provider.type)
                }"
                @click="handleSelectProvider(provider.type)"
              >
                <CustomIcon v-if="provider.iconResource" :resource="provider.iconResource" :alt="$t(provider.labelKey)" size="24px" />
                <span>{{ $t(provider.labelKey) }}</span>
              </button>
            </Tooltip>
          </div>
        </FormItem>

        <div class="sso-form-grid">
          <FormItem v-for="field in currentProviderFields" :key="field.key" :label="$t(field.labelKey)" :prop="field.key">
            <Select v-if="field.widget === 'roleSelect'" v-model="formData[field.key]" filterable clearable :placeholder="getPlaceholder(field)">
              <Option v-for="role in roleList" :key="role.roleName" :value="role.roleName" :label="role.aliasName || role.roleName">
                <span>{{ role.aliasName || role.roleName }}</span>
                <span v-if="role.aliasName && role.aliasName !== role.roleName" class="role-option-code">{{ role.roleName }}</span>
              </Option>
            </Select>
            <Input v-else-if="field.password" v-model="formData[field.key]" type="password" password :placeholder="getPlaceholder(field)" />
            <Input v-else v-model="formData[field.key]" :placeholder="getPlaceholder(field)" />
          </FormItem>
        </div>
      </Form>

      <div v-else-if="!loading" class="sso-form-empty">{{ $t('zan-wu-shu-ju') }}</div>

      <div v-if="selectedProvider && selectedProvider.helpUrl" class="sso-form-help">
        <a @click="jumpToHelp">{{ $t('ru-he-dui-jie-x', [$t(selectedProvider.labelKey)]) }}</a>
      </div>

      <div v-if="selectedProvider" class="sso-form-footer">
        <Button v-if="isEdit" type="error" @click="handleDelete">{{ $t('shan-chu') }}</Button>
        <div class="sso-form-footer__right">
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
import { SSO_PROVIDERS, ACCOUNT_AUTH_TYPE_KEY, getProviderByType, parseAuthTypes, buildAuthTypeValue } from './constant';

export default {
  name: 'SsoForm',
  data() {
    return {
      loading: false,
      submitLoading: false,
      configList: [],
      enabledTypes: [],
      roleList: [],
      selectedProviderType: '',
      formData: {}
    };
  },
  computed: {
    ...mapState(['myAuth']),
    isEdit() {
      return this.$route.name === 'DMSsoEdit';
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
      return SSO_PROVIDERS;
    },
    isProviderEnabled() {
      return (type) => this.enabledTypes.includes(type);
    },
    providerTooltip() {
      return (type) => {
        if (this.isProviderEnabled(type)) return this.$t('sso-provider-already-added');
        return '';
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
      await Promise.all([this.fetchConfigs(), this.fetchRoleList()]);
      this.bootstrapSelection();
      this.loading = false;
    },
    async fetchConfigs() {
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      if (res.success) {
        this.configList = res.data || [];
        const authConfig = this.configList.find((c) => c.configName === ACCOUNT_AUTH_TYPE_KEY);
        const value = authConfig?.currentCount || authConfig?.configValue || '';
        this.enabledTypes = parseAuthTypes(value);
      }
    },
    async fetchRoleList() {
      const res = await this.$services.rdpRoleListRole();
      if (res.success) {
        this.roleList = res.data || [];
      }
    },
    bootstrapSelection() {
      if (this.isEdit) {
        const def = getProviderByType(this.routeType);
        if (!def || !this.enabledTypes.includes(def.type)) {
          this.$Message.error(this.$t('zan-wu-shu-ju'));
          this.goBack();
          return;
        }
        this.selectedProviderType = def.type;
        this.resetFormData(def.type, true);
        return;
      }
      const first = SSO_PROVIDERS.find((p) => !this.enabledTypes.includes(p.type));
      if (!first) {
        this.goBack();
        return;
      }
      this.selectedProviderType = first.type;
      this.resetFormData(first.type, false);
    },
    handleSelectProvider(type) {
      if (this.isEdit) return;
      if (this.enabledTypes.includes(type)) return;
      this.selectedProviderType = type;
      this.resetFormData(type, false);
    },
    resetFormData(type, fillFromExisting) {
      const def = getProviderByType(type);
      const next = {};
      if (def) {
        def.fields.forEach((field) => {
          if (fillFromExisting && !field.password) {
            const config = this.configMap[field.key];
            next[field.key] = config?.currentCount ?? config?.configValue ?? '';
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
    async handleSubmit() {
      const def = this.selectedProvider;
      if (!def) return;
      const valid = await this.$refs.ssoForm.validate();
      if (!valid) return;

      const payload = { ...this.formData };
      if (this.isEdit) {
        def.fields.forEach((field) => {
          if (field.password && (payload[field.key] ?? '') === '') {
            delete payload[field.key];
          }
        });
      }

      this.submitLoading = true;
      const nextTypes = this.isEdit ? this.enabledTypes : [...this.enabledTypes, def.type];
      const ok = await this.persistProvider(def, payload, nextTypes);
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
        content: this.$t('sso-confirm-delete-x', [this.$t(def.labelKey)]),
        className: 'dm-modal-destructive',
        onOk: async () => {
          const cleared = {};
          def.fields.forEach((field) => {
            cleared[field.key] = '';
          });
          const remaining = this.enabledTypes.filter((t) => t !== def.type);
          this.submitLoading = true;
          const ok = await this.persistProvider(def, cleared, remaining);
          this.submitLoading = false;
          if (ok) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            this.goBack();
          }
        }
      });
    },
    async persistProvider(def, fieldValues, nextTypesList) {
      const updateConfigs = {};
      const needCreateConfigs = {};

      def.fields.forEach((field) => {
        if (!Object.prototype.hasOwnProperty.call(fieldValues, field.key)) return;
        const value = fieldValues[field.key] ?? '';
        const config = this.configMap[field.key];
        if (config) {
          updateConfigs[field.key] = value;
        } else {
          needCreateConfigs[field.key] = value;
        }
      });

      const authValue = buildAuthTypeValue(nextTypesList.filter((t) => getProviderByType(t)));
      const authConfig = this.configMap[ACCOUNT_AUTH_TYPE_KEY];
      if (authConfig) {
        updateConfigs[ACCOUNT_AUTH_TYPE_KEY] = authValue;
      } else {
        needCreateConfigs[ACCOUNT_AUTH_TYPE_KEY] = authValue;
      }

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
      this.$router.push('/integrations/sso');
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
.sso-form-page {
  height: 100%;
  min-height: 0;
  box-sizing: border-box;
  padding: 30px 36px 18px;
  overflow: auto;
}

.sso-form-card {
  box-sizing: border-box;
  min-height: 100%;
  padding: 20px 24px 22px;
}

.sso-form {
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

.sso-type-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.sso-type-card {
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

.sso-form-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr);
  row-gap: 12px;
  max-width: 720px;
}

.sso-form-empty {
  color: #667085;
  font-size: 14px;
}

.sso-form-help {
  max-width: 720px;
  margin-top: 18px;
}

.sso-form-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  max-width: 720px;
  margin-top: 18px;
  padding-top: 18px;
  border-top: 1px solid #edf2f7;
}

.sso-form-footer__right {
  display: inline-flex;
  align-items: center;
  gap: 10px;
}

.role-option-code {
  margin-left: 8px;
  color: var(--text-secondary, #999);
  font-size: 12px;
}

@media (max-width: 900px) {
  .sso-form-page {
    padding: 12px;
  }

  .sso-form-grid {
    grid-template-columns: 1fr;
  }

  .sso-form-footer {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
