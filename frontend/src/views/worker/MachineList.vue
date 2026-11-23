<template>
  <div class="machine-list-page">
    <AppPageTabs :model-value="activeTab" :tabs="tabs" @change="changeTab" />
    <div class="machine-list-content">
      <Cluster v-if="activeTab === 'cluster'" />
      <div v-else class="console-list-container">
        <div class="table-list-layout">
          <div class="table-list">
            <div class="content">
              <div class="option">
                <div class="left">
                  <Input
                    v-model="consoleSearch"
                    style="width: 280px; margin-right: 10px"
                    clearable
                    :placeholder="$t('machine-list.search-console-placeholder')"
                    @on-enter="handleConsoleSearch"
                    @on-clear="handleConsoleSearch"
                  />
                  <Button type="primary" ghost @click="handleConsoleSearch">
                    {{ $t('cha-xun') }}
                  </Button>
                </div>
              </div>
              <div class="table-container">
                <Table border stripe :columns="consoleColumns" :data="pagedConsoles" :scroll="{ x: 560 }" size="small" :loading="consoleLoading">
                  <template #address="{ row }">{{ row.advertiseUrl || '—' }}</template>
                  <template #status="{ row }">
                    <div class="console-status">
                      <cc-status :type="consoleStatusTypes[row.status] || 'error'" :content="$t(consoleStatusLabels[row.status] || 'li-xian')" />
                    </div>
                  </template>
                </Table>
              </div>
            </div>
          </div>
          <div class="footer">
            <Page
              :total="filteredConsoles.length"
              show-total
              show-elevator
              show-sizer
              v-model="consolePageNum"
              :page-size="consolePageSize"
              @on-change="handleConsolePageChange"
              @on-page-size-change="handleConsolePageSizeChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AppPageTabs from '@/components/layout/AppPageTabs';
import Cluster from '@/views/worker/Cluster';

const consoleStatusLabels = {
  STARTING: 'machine-list.starting',
  READY: 'machine-list.ready',
  DRAINING: 'machine-list.draining',
  OFFLINE: 'li-xian'
};

const consoleStatusTypes = {
  STARTING: 'warning',
  READY: 'success',
  DRAINING: 'warning',
  OFFLINE: 'error'
};

export default {
  name: 'MachineList',
  components: { AppPageTabs, Cluster },
  data() {
    return {
      consoles: [],
      consoleLoading: false,
      consoleSearch: '',
      appliedConsoleSearch: '',
      consolePageNum: 1,
      consolePageSize: 20,
      consoleStatusLabels,
      consoleStatusTypes
    };
  },
  computed: {
    activeTab() {
      return this.$route.query.tab === 'console' ? 'console' : 'cluster';
    },
    tabs() {
      return [
        { name: 'cluster', label: this.$t('machine-list.worker-clusters') },
        { name: 'console', label: this.$t('machine-list.control-instances') }
      ];
    },
    consoleColumns() {
      return [
        { title: this.$t('shi-li-id'), key: 'consoleInstanceId', minWidth: 160, tooltip: true },
        { title: this.$t('machine-list.advertised-address'), slot: 'address', minWidth: 200 },
        { title: this.$t('zhuang-tai'), slot: 'status', width: 140 }
      ];
    },
    filteredConsoles() {
      const keyword = this.appliedConsoleSearch;
      if (!keyword) {
        return this.consoles;
      }
      return this.consoles.filter((item) => {
        const id = String(item.consoleInstanceId || '').toLowerCase();
        const address = String(item.advertiseUrl || '').toLowerCase();
        return id.includes(keyword) || address.includes(keyword);
      });
    },
    pagedConsoles() {
      const start = (this.consolePageNum - 1) * this.consolePageSize;
      return this.filteredConsoles.slice(start, start + this.consolePageSize);
    }
  },
  watch: {
    activeTab: {
      immediate: true,
      handler(tab) {
        if (tab === 'console') {
          this.loadConsoles();
        }
      }
    }
  },
  methods: {
    changeTab(tab) {
      const query = { ...(this.$route.query || {}) };
      if (tab === 'console') {
        query.tab = 'console';
      } else {
        delete query.tab;
      }
      this.$router.replace({
        path: '/data-access/cluster',
        query
      });
    },
    handleConsoleSearch() {
      this.appliedConsoleSearch = this.consoleSearch.trim().toLowerCase();
      this.consolePageNum = 1;
      this.loadConsoles();
    },
    handleConsolePageChange(pageNum) {
      this.consolePageNum = pageNum;
    },
    handleConsolePageSizeChange(pageSize) {
      this.consolePageSize = pageSize;
      this.consolePageNum = 1;
    },
    async loadConsoles() {
      if (this.consoleLoading) {
        return;
      }
      this.consoleLoading = true;
      try {
        const res = await this.$services.dmClusterListConsoleInstances();
        if (res.success) {
          this.consoles = res.data || [];
          const maxPage = Math.max(1, Math.ceil(this.filteredConsoles.length / this.consolePageSize));
          if (this.consolePageNum > maxPage) {
            this.consolePageNum = maxPage;
          }
        }
      } finally {
        this.consoleLoading = false;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.machine-list-page {
  height: 100%;
  min-height: 0;
  display: flex;
  flex-direction: column;
}

.machine-list-content,
.console-list-container {
  flex: 1;
  height: 100%;
  min-height: 0;
}

.machine-list-content :deep(.cluster-container.table-list-layout) {
  padding: 0;
}

.console-status {
  display: inline-flex;
  align-items: center;
}
</style>
