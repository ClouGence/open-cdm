<template>
  <div class="sso integration-list-page">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                v-model="searchText"
                style="width: 280px; margin-right: 10px"
                clearable
                :placeholder="$t('shu-ru-ming-cheng-cha-zhao')"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <a-tooltip v-if="canEdit" :title="hasUnenabledProvider ? '' : $t('sso-all-providers-enabled')">
                <Button @click="goCreate" type="primary" icon="md-add" :disabled="!hasUnenabledProvider">
                  {{ $t('xin-zeng') }}
                </Button>
              </a-tooltip>
            </div>
          </div>
          <div class="table-container integration-table-container">
            <Table
              class="integration-table"
              :columns="columns"
              :data="filteredRows"
              :loading="loading"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
              size="small"
              border
            >
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
                  <a @click="goEdit(row)">{{ $t('pei-zhi') }}</a>
                  <a v-if="canEdit" @click="handleDelete(row)">{{ $t('shan-chu') }}</a>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
    </div>
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
      searchText: '',
      appliedKeyword: '',
      configList: [],
      enabledTypes: []
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
        { title: this.$t('cao-zuo'), slot: 'action', fixed: 'right', width: 160 }
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
      const keyword = this.appliedKeyword;
      if (!keyword) return this.rows;
      return this.rows.filter(
        (row) =>
          (row.label && row.label.toLowerCase().includes(keyword)) || (row.primaryValue && String(row.primaryValue).toLowerCase().includes(keyword))
      );
    },
    hasUnenabledProvider() {
      return SSO_PROVIDERS.some((p) => !this.enabledTypes.includes(p.type));
    }
  },
  mounted() {
    if (this.canRead) {
      this.init();
    }
  },
  methods: {
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
    handleQuery() {
      this.appliedKeyword = this.searchText.trim().toLowerCase();
    },
    handleQueryClear() {
      this.searchText = '';
      this.appliedKeyword = '';
    },
    goCreate() {
      this.$router.push('/integrations/sso/create');
    },
    goEdit(row) {
      this.$router.push(`/integrations/sso/${row.type.toLowerCase()}/edit`);
    },
    handleDelete(row) {
      const def = getProviderByType(row.type);
      if (!def) {
        return;
      }
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('sso-confirm-delete-x', [this.$t(def.labelKey)]),
        className: 'dm-modal-destructive',
        onOk: async () => {
          const updateConfigs = {};
          const needCreateConfigs = {};
          def.fields.forEach((field) => {
            const config = this.configMap[field.key];
            if (config) {
              updateConfigs[field.key] = '';
            } else {
              needCreateConfigs[field.key] = '';
            }
          });
          const remaining = this.enabledTypes.filter((type) => type !== def.type);
          const authValue = buildAuthTypeValue(remaining.filter((type) => getProviderByType(type)));
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
            return;
          }
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          this.init();
        }
      });
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
  display: inline-flex;
  align-items: center;
  gap: 12px;

  a:hover {
    border-bottom: none;
    box-shadow: inset 0 -1px 0 currentColor;
  }
}
</style>
