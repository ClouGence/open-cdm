<template>
  <div class="approval integration-list-page page-shell">
    <AppPageTabs v-model="tab" :tabs="tabs" @change="changeTab" />
    <Teams v-if="tab === 'teams'" :editable="canEdit" />
    <div v-else class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option">
            <div class="left">
              <Input
                v-model="searchText"
                style="width: 280px; max-width: 100%; margin-right: 10px"
                clearable
                :placeholder="$t('shu-ru-ming-cheng-cha-zhao')"
                @on-enter="handleQuery"
                @on-clear="handleQueryClear"
              />
              <Button type="primary" ghost @click="handleQuery">{{ $t('cha-xun') }}</Button>
            </div>
            <div class="right">
              <Button v-if="canEdit" @click="goCreate" type="primary" icon="md-add">
                {{ $t('xin-zeng') }}
              </Button>
            </div>
          </div>
          <div class="table-container integration-table-container">
            <Table
              class="integration-table"
              :columns="columns"
              :data="filteredRows"
              :loading="loading"
              :scroll="{ x: 740 }"
              :locale="{ emptyText: $t('zan-wu-shu-ju') }"
              size="small"
              border
            >
              <template #provider="{ row }">
                <div class="provider-cell">
                  <img v-if="row.type === 'Internal'" class="provider-icon" :src="cloudDmMark" alt="CloudDM" />
                  <CustomIcon v-else-if="row.iconResource" :resource="row.iconResource" :alt="row.label" size="20px" />
                  <span>{{ row.label }}</span>
                </div>
              </template>
              <template #primary="{ row }">
                <span>{{ row.primaryValue || '-' }}</span>
              </template>
              <template #action="{ row }">
                <div class="action">
                  <Button type="text" size="small" @click="goEdit(row)">{{ $t('pei-zhi') }}</Button>
                  <Button
                    v-if="canEdit && row.type !== 'Internal'"
                    type="text"
                    size="small"
                    :loading="testingProviders.includes(row.type)"
                    @click="testConnection(row)"
                  >
                    {{ $t('ce-shi-lian-jie') }}
                  </Button>
                  <Button v-if="canEdit" type="text" size="small" @click="handleDelete(row)">{{ $t('shan-chu') }}</Button>
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
import cloudDmMark from '@/assets/logo-clouddm-mark.svg';
import AppPageTabs from '@/components/layout/AppPageTabs.vue';
import Teams from './Teams.vue';
import { APPROVAL_PROVIDERS, getProviderByType, isConfigured } from './constant';

export default {
  name: 'ApprovalPage',
  components: { AppPageTabs, Teams },
  data() {
    return {
      cloudDmMark,
      tab: this.$route.query.tab === 'teams' ? 'teams' : 'engines',
      internalConfig: { version: 0, engines: [] },
      loading: false,
      searchText: '',
      appliedKeyword: '',
      configList: [],
      testingProviders: []
    };
  },
  computed: {
    ...mapState(['myAuth']),
    tabs() {
      return [
        { name: 'engines', label: this.$t('approval-engines') },
        { name: 'teams', label: this.$t('approval-teams') }
      ];
    },
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
        { title: this.$t('cao-zuo'), slot: 'action', fixed: 'right', width: 260 }
      ];
    },
    rows() {
      const providers = APPROVAL_PROVIDERS.filter((def) => def.type !== 'Internal' && isConfigured(this.configMap, def)).map((def) => {
        const primaryConfig = this.configMap[def.primaryField];
        return {
          type: def.type,
          label: this.$t(def.labelKey),
          iconResource: def.iconResource,
          primaryValue: primaryConfig?.configValue || ''
        };
      });
      return [
        ...this.internalConfig.engines.map((engine) => ({
          ...engine,
          type: 'Internal',
          label: this.$t('approval-internal-engine'),
          primaryValue: engine.engineName
        })),
        ...providers
      ];
    },
    filteredRows() {
      const keyword = this.appliedKeyword;
      if (!keyword) return this.rows;
      return this.rows.filter(
        (row) =>
          (row.label && row.label.toLowerCase().includes(keyword)) || (row.primaryValue && String(row.primaryValue).toLowerCase().includes(keyword))
      );
    }
  },
  mounted() {
    if (this.canRead) {
      this.init();
    }
  },
  methods: {
    changeTab(tab) {
      this.$router.replace({ query: { ...this.$route.query, tab } });
    },
    async init() {
      this.loading = true;
      try {
        const [res, configuration] = await Promise.all([
          this.$services.rdpUserConfigGetCurrUserConfigs(),
          this.$services.dmApprovalConfigurationQuery()
        ]);
        if (configuration.success) this.internalConfig = configuration.data.engineConfig;
        if (res.success) this.configList = res.data;
      } finally {
        this.loading = false;
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
      if (row.type === 'Internal') {
        this.$router.push({ path: `/integrations/approval/internal/${row.engineConfigId}/edit`, query: { engineName: row.engineName } });
        return;
      }
      this.$router.push(`/integrations/approval/${row.type.toLowerCase()}/edit`);
    },
    async testConnection(row) {
      if (this.testingProviders.includes(row.type)) return;
      this.testingProviders.push(row.type);
      try {
        const response = await this.$services.dmApprovalTestConnection({ data: { provider: row.type, configs: {} } });
        if (response.success) {
          this.$Message.success(this.$t('ce-shi-lian-jie-cheng-gong'));
        }
      } finally {
        this.testingProviders = this.testingProviders.filter((type) => type !== row.type);
      }
    },
    handleDelete(row) {
      if (row.type === 'Internal') {
        this.$Modal.confirm({
          title: this.$t('que-ren'),
          content: this.$t('approval-delete-engine-confirm', [row.engineName]),
          onOk: async () => {
            const res = await this.$services.dmApprovalDeleteEngine({
              data: { engineConfigId: row.engineConfigId, configVersion: this.internalConfig.version }
            });
            if (res.success) {
              this.$Message.success(this.$t('cao-zuo-cheng-gong'));
              await this.init();
            }
          }
        });
        return;
      }
      const def = getProviderByType(row.type);
      if (!def) {
        return;
      }
      this.$Modal.confirm({
        title: this.$t('que-ren'),
        content: this.$t('approval-confirm-delete-x', [this.$t(def.labelKey)]),
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
.approval {
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
}

.provider-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.provider-icon {
  width: 20px;
  height: 20px;
  flex-shrink: 0;
  object-fit: contain;
}

.action {
  display: inline-flex;
  align-items: center;
}
</style>
