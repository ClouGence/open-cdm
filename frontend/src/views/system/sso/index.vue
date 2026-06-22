<template>
  <div class="sso">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input v-model="searchText" style="width: 280px; margin-right: 10px" clearable :placeholder="$t('shu-ru-ming-cheng-cha-zhao')" />
            </div>
            <div class="right">
              <Button
                v-if="canEdit"
                @click="handleOpenAddDrawer"
                type="primary"
                style="margin-right: 10px"
                icon="md-add"
                :disabled="!hasUnenabledProvider"
              >
                {{ $t('sso-add-provider') }}
              </Button>
              <Button @click="init" :loading="loading">
                <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="columns" :data="filteredRows" :loading="loading" :locale="{ emptyText: $t('zan-wu-shu-ju') }" size="small" border>
              <template #provider="{ row }">
                <div class="provider-cell">
                  <CustomIcon v-if="row.iconResource" :resource="row.iconResource" :alt="row.label" size="20px" />
                  <span>{{ row.label }}</span>
                </div>
              </template>
              <template #primary="{ row }">
                <span>{{ row.primaryValue || '-' }}</span>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <a @click="handleOpenEditDrawer(row)" style="margin-right: 10px">{{ $t('pei-zhi') }}</a>
                  <a v-if="canEdit" class="error-link" @click="handleDelete(row)">{{ $t('shan-chu') }}</a>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>

    <Drawer :title="drawerTitle" width="480" class-name="sso-drawer" v-model="drawerShow" :mask-closable="false" @on-close="handleCloseDrawer">
      <div class="sso-drawer-body">
        <div class="sso-drawer-body__scroll">
          <Form v-if="drawerShow" ref="ssoForm" :label-width="140" label-position="right" :model="formData" :rules="formRules">
            <div class="provider-list">
              <Tooltip
                v-for="provider in providerCandidates"
                :key="provider.type"
                :disabled="editMode || !isProviderEnabled(provider.type)"
                :content="$t('sso-provider-already-added')"
                placement="top"
                transfer
              >
                <div
                  :class="[
                    'provider-card',
                    {
                      'is-selected': selectedProviderType === provider.type,
                      'is-readonly': editMode,
                      'is-disabled': !editMode && isProviderEnabled(provider.type)
                    }
                  ]"
                  @click="handleSelectProvider(provider.type)"
                >
                  <CustomIcon :resource="provider.iconResource" :alt="$t(provider.labelKey)" size="24px" />
                  <div>{{ $t(provider.labelKey) }}</div>
                  <span v-if="!editMode && isProviderEnabled(provider.type)" class="provider-card__badge">
                    {{ $t('sso-status-enabled') }}
                  </span>
                </div>
              </Tooltip>
            </div>

            <FormItem v-for="field in currentProviderFields" :key="field.key" :label="$t(field.labelKey)" :prop="field.key">
              <Select v-if="field.widget === 'roleSelect'" v-model="formData[field.key]" filterable clearable :placeholder="getPlaceholder(field)">
                <Option v-for="role in roleList" :key="role.roleName" :value="role.roleName" :label="role.aliasName || role.roleName">
                  <span>{{ role.aliasName || role.roleName }}</span>
                  <span v-if="role.aliasName && role.aliasName !== role.roleName" class="role-option-code">{{ role.roleName }}</span>
                </Option>
              </Select>
              <Input v-else-if="field.password" v-model="formData[field.key]" type="password" password :placeholder="getPlaceholder(field)" />
              <Input v-else v-model="formData[field.key]" :placeholder="getPlaceholder(field)" />
              <div v-if="field.hintKey" class="field-hint">{{ $t(field.hintKey) }}</div>
            </FormItem>
          </Form>
        </div>

        <div class="sso-drawer-body__footer">
          <Button @click="handleCloseDrawer" style="margin-right: 10px">{{ $t('qu-xiao') }}</Button>
          <Button type="primary" :loading="saving" @click="handleSave">
            {{ editMode ? $t('bao-cun') : $t('tian-jia') }}
          </Button>
        </div>
      </div>
    </Drawer>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { SSO_PROVIDERS, ACCOUNT_AUTH_TYPE_KEY, PASSWORD_TYPE, getProviderByType, parseAuthTypes, buildAuthTypeValue } from './constant';

export default {
  name: 'SsoPage',
  data() {
    return {
      loading: false,
      saving: false,
      searchText: '',
      configList: [],
      enabledTypes: [],
      drawerShow: false,
      editMode: false,
      selectedProviderType: '',
      formData: {},
      roleList: []
    };
  },
  computed: {
    ...mapState(['myAuth']),
    canRead() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_R');
    },
    canEdit() {
      return this.myAuth.includes('RDP_PRI_USER_KV_CONF_W');
    },
    configMap() {
      const map = {};
      this.configList.forEach((c) => {
        map[c.configName] = c;
      });
      return map;
    },
    columns() {
      return [
        { title: this.$t('sso-col-provider'), slot: 'provider', width: 180 },
        { title: this.$t('sso-col-primary'), slot: 'primary', minWidth: 240 },
        { title: this.$t('sso-col-status'), key: 'statusText', width: 120 },
        { title: this.$t('cao-zuo'), slot: 'action', fixed: 'right', width: 140 }
      ];
    },
    rows() {
      return this.enabledTypes
        .filter((t) => t !== PASSWORD_TYPE)
        .map((t) => {
          const def = getProviderByType(t);
          if (!def) return null;
          return {
            type: def.type,
            label: this.$t(def.labelKey),
            iconResource: def.iconResource,
            primaryValue: this.configMap[def.primaryField]?.currentCount || this.configMap[def.primaryField]?.configValue || '',
            statusText: this.$t('sso-status-enabled')
          };
        })
        .filter(Boolean);
    },
    filteredRows() {
      const keyword = this.searchText.trim().toLowerCase();
      if (!keyword) return this.rows;
      return this.rows.filter(
        (row) =>
          (row.label && row.label.toLowerCase().includes(keyword)) || (row.primaryValue && String(row.primaryValue).toLowerCase().includes(keyword))
      );
    },
    hasUnenabledProvider() {
      return SSO_PROVIDERS.some((p) => !this.enabledTypes.includes(p.type));
    },
    providerCandidates() {
      if (this.editMode) {
        const current = getProviderByType(this.selectedProviderType);
        return current ? [current] : [];
      }
      return SSO_PROVIDERS;
    },
    isProviderEnabled() {
      return (type) => this.enabledTypes.includes(type);
    },
    currentProviderFields() {
      const def = getProviderByType(this.selectedProviderType);
      return def ? def.fields : [];
    },
    formRules() {
      const rules = {};
      this.currentProviderFields.forEach((field) => {
        if (field.required) {
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
    },
    drawerTitle() {
      if (this.editMode) {
        const def = getProviderByType(this.selectedProviderType);
        return def ? this.$t('sso-edit-provider-x', [this.$t(def.labelKey)]) : this.$t('sso-edit-provider');
      }
      return this.$t('sso-add-provider');
    }
  },
  mounted() {
    if (this.canRead) {
      this.init();
      this.fetchRoleList();
    }
  },
  methods: {
    async fetchRoleList() {
      const res = await this.$services.rdpRoleListRole();
      if (res.success) {
        this.roleList = res.data || [];
      }
    },
    async init() {
      this.loading = true;
      const res = await this.$services.rdpUserConfigGetCurrUserConfigs();
      this.loading = false;
      if (res.success) {
        this.configList = res.data || [];
        const authConfig = this.configList.find((c) => c.configName === ACCOUNT_AUTH_TYPE_KEY);
        const value = authConfig?.currentCount || authConfig?.configValue || '';
        this.enabledTypes = parseAuthTypes(value);
      }
    },
    getPlaceholder(field) {
      const config = this.configMap[field.key];
      const defaultVal = config?.defaultValue;
      return defaultVal ? this.$t('sso-default-placeholder', [defaultVal]) : '';
    },
    handleOpenAddDrawer() {
      const first = SSO_PROVIDERS.find((p) => !this.enabledTypes.includes(p.type));
      if (!first) return;
      this.editMode = false;
      this.selectedProviderType = first.type;
      this.resetFormData(this.selectedProviderType, false);
      this.drawerShow = true;
    },
    handleOpenEditDrawer(row) {
      this.editMode = true;
      this.selectedProviderType = row.type;
      this.resetFormData(row.type, true);
      this.drawerShow = true;
    },
    handleSelectProvider(type) {
      if (this.editMode) return;
      if (this.enabledTypes.includes(type)) return;
      this.selectedProviderType = type;
      this.resetFormData(type, false);
    },
    resetFormData(type, fillFromExisting) {
      const def = getProviderByType(type);
      const next = {};
      if (def) {
        def.fields.forEach((field) => {
          if (fillFromExisting) {
            const config = this.configMap[field.key];
            next[field.key] = config?.currentCount ?? config?.configValue ?? '';
          } else {
            next[field.key] = '';
          }
        });
      }
      this.formData = next;
    },
    async handleSave() {
      const def = getProviderByType(this.selectedProviderType);
      if (!def) return;
      const valid = await this.$refs.ssoForm.validate();
      if (!valid) return;

      this.saving = true;
      const ok = await this.persistProvider(def, this.formData, [...this.enabledTypes, def.type]);
      this.saving = false;

      if (ok) {
        this.$Message.success(this.$t('cao-zuo-cheng-gong'));
        this.drawerShow = false;
        await this.init();
      }
    },
    async handleDelete(row) {
      if (!this.canEdit) return;
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('sso-confirm-delete-x', [row.label]),
        onOk: async () => {
          const def = getProviderByType(row.type);
          if (!def) return;
          const cleared = {};
          def.fields.forEach((field) => {
            cleared[field.key] = '';
          });
          const remaining = this.enabledTypes.filter((t) => t !== row.type);
          const ok = await this.persistProvider(def, cleared, remaining);
          if (ok) {
            this.$Message.success(this.$t('cao-zuo-cheng-gong'));
            await this.init();
          }
        }
      });
    },
    async persistProvider(def, fieldValues, nextTypesList) {
      const updateConfigs = {};
      const needCreateConfigs = {};

      def.fields.forEach((field) => {
        const value = fieldValues[field.key] ?? '';
        // 跳过未变更的密码空值，避免误把已有密码清空
        if (field.password && value === '' && this.editMode) return;
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
    handleCloseDrawer() {
      this.drawerShow = false;
      this.selectedProviderType = '';
      this.formData = {};
    }
  }
};
</script>

<style lang="less" scoped>
.sso {
  height: 100%;
}

.provider-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action {
  display: flex;
  align-items: center;
}

.error-link {
  color: #ed4014;
}

.sso-drawer-body {
  position: absolute;
  inset: 0;

  &__scroll {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 56px;
    overflow: auto;
    padding: 16px 24px;
  }

  &__footer {
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 56px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 10px 16px;
    border-top: 1px solid #e8e8e8;
    background: #fff;
  }
}

.provider-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 16px 0 20px;

  .provider-card {
    position: relative;
    cursor: pointer;
    width: 88px;
    height: 88px;
    border: 1px solid #dddddd;
    border-radius: 6px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 6px;
    font-size: 12px;
  }

  .provider-card.is-selected {
    border: 2px solid #43cf7c;
  }

  .provider-card.is-readonly {
    cursor: default;
  }

  .provider-card.is-disabled {
    cursor: not-allowed;
    background: #f5f5f5;
    color: #c5c8ce;
    opacity: 0.75;
  }

  .provider-card__badge {
    position: absolute;
    top: 4px;
    right: 4px;
    padding: 0 6px;
    font-size: 10px;
    line-height: 16px;
    border-radius: 8px;
    background: #e8eaec;
    color: #808695;
  }
}

.role-option-code {
  margin-left: 8px;
  color: var(--text-secondary, #999);
  font-size: 12px;
}

.field-hint {
  margin-top: 4px;
  color: var(--text-secondary, #888);
  font-size: 12px;
  line-height: 1.4;
}
</style>
