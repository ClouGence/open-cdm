<template>
  <div class="approval">
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
              <Button v-if="canEdit" @click="goCreate" type="primary" style="margin-right: 10px" icon="md-add" :disabled="!hasUnaddedProvider">
                {{ $t('xin-zeng') }}
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
              <template #status="{ row }">
                <div class="status-cell">
                  <i-switch
                    :model-value="row.enabled"
                    true-color="#52C41A"
                    :loading="togglingType === row.type"
                    :disabled="!canEdit || togglingType === row.type"
                    :before-change="() => handleToggleEnable(row)"
                  />
                  <span class="status-text">{{ row.statusText }}</span>
                </div>
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
import { APPROVAL_PROVIDERS, getProviderByType, isConfigured, isEnabled } from './constant';

export default {
  name: 'ApprovalPage',
  data() {
    return {
      loading: false,
      searchText: '',
      appliedKeyword: '',
      configList: [],
      togglingType: ''
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
        { title: this.$t('approval-col-provider'), slot: 'provider', width: 180 },
        { title: this.$t('approval-col-primary'), slot: 'primary', minWidth: 240 },
        { title: this.$t('approval-col-status'), slot: 'status', width: 160 },
        { title: this.$t('cao-zuo'), slot: 'action', fixed: 'right', width: 120 }
      ];
    },
    rows() {
      return APPROVAL_PROVIDERS.filter((def) => isConfigured(this.configMap, def)).map((def) => {
        const enabled = isEnabled(this.configMap, def);
        const primaryConfig = this.configMap[def.primaryField];
        return {
          type: def.type,
          label: this.$t(def.labelKey),
          iconResource: def.iconResource,
          primaryValue: primaryConfig?.currentCount || primaryConfig?.configValue || '',
          enabled,
          statusText: this.$t(enabled ? 'approval-status-enabled' : 'approval-status-configured-not-enabled')
        };
      });
    },
    filteredRows() {
      const keyword = this.appliedKeyword;
      if (!keyword) return this.rows;
      return this.rows.filter(
        (row) =>
          (row.label && row.label.toLowerCase().includes(keyword)) || (row.primaryValue && String(row.primaryValue).toLowerCase().includes(keyword))
      );
    },
    hasUnaddedProvider() {
      return APPROVAL_PROVIDERS.some((def) => !isConfigured(this.configMap, def));
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
      this.$router.push('/integrations/approval/create');
    },
    goEdit(row) {
      this.$router.push(`/integrations/approval/${row.type}/edit`);
    },
    handleToggleEnable(row) {
      const def = getProviderByType(row.type);
      if (!def) return Promise.reject();
      const newVal = !row.enabled;
      const hasConfig = !!this.configMap[def.enableField];
      const updateConfigs = hasConfig ? { [def.enableField]: String(newVal) } : {};
      const needCreateConfigs = hasConfig ? {} : { [def.enableField]: String(newVal) };
      this.togglingType = row.type;
      return this.$services
        .rdpUserConfigUpsertUserConfigs({ data: { updateConfigs, needCreateConfigs } })
        .then((res) => {
          if (!res.success) {
            this.$Message.error(res.msg || this.$t('cao-zuo-shi-bai'));
            throw new Error('approval toggle failed');
          }
          this.$Message.success(this.$t('cao-zuo-cheng-gong'));
          // iView Switch 在我们的 Promise resolve 之后才内部 toggle，
          // toggle 时根据 currentValue 反转；若此刻 init 已把 :model-value
          // 同步成新值，反转结果反而是旧值。推迟到下一个宏任务刷新。
          setTimeout(() => this.init(), 0);
        })
        .finally(() => {
          this.togglingType = '';
        });
    }
  }
};
</script>

<style lang="less" scoped>
.approval {
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
}

.status-cell {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.status-text {
  color: #515a6e;
  font-size: 12px;
}
</style>
