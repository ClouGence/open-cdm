<template>
  <div class="sso">
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
                <Button @click="goCreate" type="primary" style="margin-right: 10px" icon="md-add" :disabled="!hasUnenabledProvider">
                  {{ $t('xin-zeng') }}
                </Button>
              </a-tooltip>
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
                  <a @click="goEdit(row)">{{ $t('pei-zhi') }}</a>
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
import { SSO_PROVIDERS, ACCOUNT_AUTH_TYPE_KEY, PASSWORD_TYPE, getProviderByType, parseAuthTypes } from './constant';

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
        { title: this.$t('cao-zuo'), slot: 'action', fixed: 'right', width: 120 }
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
      return SSO_PROVIDERS.some((p) => !this.enabledTypes.includes(p.type) && !this.conflictingPeer(p.type));
    },
    conflictingPeer() {
      return (type) => {
        const def = getProviderByType(type);
        if (!def || !def.conflictsWith) return '';
        return def.conflictsWith.find((peer) => this.enabledTypes.includes(peer)) || '';
      };
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
