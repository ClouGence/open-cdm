<template>
  <div class="page-header-container datasource-header-panel">
    <Form ref="formInline" class="datasource-search-form" :model="searchKey" inline label-position="right">
      <FormItem>
        <Select v-model="searchType" class="datasource-search-type" @on-change="handleChangeSearchType">
          <Option value="type" :label="$t('lei-xing')">
            <span>{{ $t('lei-xing') }}</span>
          </Option>
          <Option value="desc" :label="$t('ming-cheng')">
            <span>{{ $t('ming-cheng') }}</span>
          </Option>
          <Option value="host" :label="$t('di-zhi')">
            <span>{{ $t('di-zhi') }}</span>
          </Option>
        </Select>
      </FormItem>
      <FormItem v-if="searchType === 'type'">
        <Poptip v-model="typePanelVisible" trigger="click" placement="bottom-start" transfer :width="760" class="datasource-type-poptip">
          <Button class="datasource-type-trigger">
            <span class="datasource-type-trigger-label" :title="selectedTypeLabel">{{ selectedTypeLabel }}</span>
            <Icon type="ios-arrow-down" class="datasource-type-trigger-icon" :class="{ 'is-open': typePanelVisible }" />
          </Button>
          <template #content>
            <div class="datasource-type-panel">
              <button
                type="button"
                class="datasource-type-all"
                :class="{ 'is-active': selectedDbType === 'all' }"
                @click="handleSelectDataSourceType('all')"
              >
                {{ $t('quan-bu') }}
              </button>
              <div class="datasource-type-panel-body">
                <div class="datasource-type-group" v-for="(dataSourceGroup, index) of dataSourceTypes" :key="index">
                  <button
                    translate="no"
                    type="button"
                    class="datasource-type-card"
                    :class="{ 'is-active': selectedDbType === type.dsKey }"
                    v-for="type of dataSourceGroup"
                    :key="type.dsKey"
                    @click="handleSelectDataSourceType(type.dsKey)"
                  >
                    <DataSourceIcon class="datasource-type-icon" size="24px" :type="type.dsKey" leftMargin="0"></DataSourceIcon>
                    <span class="datasource-type-name" :title="type.displayName">
                      {{ type.displayName }}
                    </span>
                  </button>
                </div>
              </div>
            </div>
          </template>
        </Poptip>
      </FormItem>
      <FormItem v-if="searchType === 'desc'">
        <Input
          v-model="searchKey.dataSourceDescLike"
          :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
          @on-keydown="handleEnterSearch"
          class="datasource-search-value"
        />
      </FormItem>
      <FormItem v-if="searchType === 'host'">
        <Input
          v-model="searchKey.dsHostLike"
          :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
          @on-keydown="handleEnterSearch"
          class="datasource-search-value"
        />
      </FormItem>
      <FormItem>
        <Button :loading="refreshLoading" type="primary" ghost @click="_handleSearch">
          {{ $t('cha-xun') }}
        </Button>
      </FormItem>
    </Form>
    <div class="page-header-function">
      <Button v-if="supportAdd" type="primary" @click="handleShowAddDataSource">
        {{ $t('xin-zeng') }}
      </Button>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex';
import DataSourceIcon from '@/components/function/DataSourceIcon';

export default {
  name: 'DataSourceHeader',
  components: {
    DataSourceIcon
  },
  emits: ['update-search-key'],
  props: {
    handleSearch: Function,
    handleShowAddDataSource: Function,
    refreshLoading: Boolean,
    searchKey: Object,
    handleChangeSearchType: Function,
    supportAdd: Boolean
  },
  data() {
    return {
      searchType: 'type',
      allowedSearchTypes: ['type', 'desc', 'host'],
      dataSourceTypes: [],
      typePanelVisible: false
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting']),
    flatDataSourceTypes() {
      return this.dataSourceTypes.flatMap((group) => group);
    },
    selectedDbType() {
      return this.searchKey?.dbType || 'all';
    },
    selectedTypeLabel() {
      if (this.selectedDbType === 'all') {
        return this.$t('quan-bu');
      }
      return this.flatDataSourceTypes.find((type) => type.dsKey === this.selectedDbType)?.displayName || this.selectedDbType;
    }
  },
  watch: {
    dmGlobalSetting() {
      this.refreshDataSourceTypes();
    }
  },
  mounted() {
    const params = JSON.parse(sessionStorage.getItem('datasource_search_params'));
    if (params) {
      const nextSearchType = this.allowedSearchTypes.includes(params.searchType) ? params.searchType : 'type';
      const nextSearchKey = {
        ...this.searchKey,
        ...params,
        searchType: undefined,
        hostType: undefined
      };
      this.$emit('update-search-key', nextSearchKey);
      this.searchType = nextSearchType;
      this.handleSearch(nextSearchKey, 'init');
    } else {
      this.handleSearch(this.searchKey, 'init');
    }
    this.refreshDataSourceTypes();
  },
  methods: {
    refreshDataSourceTypes() {
      this.dataSourceTypes = Array.isArray(this.dmGlobalSetting?.dsSupportNames)
        ? this.dmGlobalSetting.dsSupportNames
            .map((group) => (Array.isArray(group) ? group : [group]).map(this.normalizeDsSupportName).filter(Boolean))
            .filter((group) => group.length > 0)
        : [];
    },
    normalizeDsSupportName(type) {
      if (!type) {
        return null;
      }
      if (typeof type === 'string') {
        return {
          dsKey: type,
          displayName: type
        };
      }
      if (!type.dsKey) {
        return null;
      }
      return {
        dsKey: type.dsKey,
        displayName: type.displayName || type.dsKey
      };
    },
    _handleSearch() {
      sessionStorage.setItem('datasource_search_params', JSON.stringify({ searchType: this.searchType, ...this.searchKey }));
      this.handleSearch(this.searchKey, 'init');
    },
    handleSelectDataSourceType(dsKey) {
      this.searchKey.dbType = dsKey;
      this.typePanelVisible = false;
    },
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this._handleSearch();
      }
    }
  }
};
</script>
<style lang="less" scoped>
.page-header-container {
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: 8px;
  height: auto;
  line-height: normal;
  min-height: 56px;
  padding: 12px 14px;
  position: relative;
}

.datasource-header-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 14px;
}

.datasource-search-form {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
  flex-wrap: wrap;
  gap: 8px;
  margin: 0;

  :deep(.ivu-form-item) {
    margin-right: 8px;
    margin-bottom: 0;
    vertical-align: middle;
  }

  :deep(.ivu-form-item-content) {
    display: flex;
    align-items: center;
    line-height: normal;
  }
}

.page-header-function {
  position: static;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  margin-left: auto;

  a {
    color: #333;
    margin-right: 10px;
  }

  button {
    margin-left: 0;
    margin-right: 10px;
  }

  .ivu-tooltip {
    margin-left: 8px;
  }
}

.datasource-search-type {
  width: 150px;
}

.datasource-search-value {
  width: 280px;
}

.datasource-type-poptip {
  display: inline-flex;
}

.datasource-type-trigger {
  display: inline-flex;
  width: 280px;
  height: 32px;
  align-items: center;
  justify-content: space-between;
  padding: 0 12px;
  border-color: var(--border-primary);
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 400;
  text-align: left;

  &:hover,
  &:focus {
    border-color: var(--primary-color);
    color: var(--text-primary);
  }
}

.datasource-type-trigger-label {
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.datasource-type-trigger-icon {
  flex: 0 0 auto;
  margin-left: 8px;
  color: var(--text-secondary);
  transition: transform 0.2s ease;

  &.is-open {
    transform: rotate(180deg);
  }
}

.datasource-type-panel {
  width: 100%;
  max-height: 360px;
  padding: 6px 0 2px;
  overflow-y: auto;
}

.datasource-type-all {
  display: flex;
  width: 100%;
  height: 34px;
  align-items: center;
  margin-bottom: 8px;
  padding: 0 12px;
  border: 1px solid transparent;
  border-radius: 4px;
  background: transparent;
  color: var(--text-primary);
  cursor: pointer;
  font-size: 14px;
  text-align: left;

  &:hover,
  &.is-active {
    border-color: var(--primary-color);
    background: var(--bg-hover);
    color: var(--primary-color);
  }
}

.datasource-type-panel-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.datasource-type-group {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
}

.datasource-type-card {
  display: flex;
  min-width: 0;
  height: 44px;
  align-items: center;
  gap: 8px;
  padding: 6px 10px;
  border: 1px solid var(--border-light);
  border-radius: 4px;
  background: #ffffff;
  color: var(--text-primary);
  cursor: pointer;
  text-align: left;
  transition:
    border-color 0.2s ease,
    background-color 0.2s ease,
    box-shadow 0.2s ease;

  &:hover,
  &.is-active {
    border-color: var(--primary-color);
    background: var(--bg-hover);
  }

  &.is-active {
    box-shadow: 0 0 0 1px var(--primary-color) inset;
  }
}

.datasource-type-icon {
  display: inline-flex;
  width: 28px;
  flex: 0 0 28px;
  align-items: center;
  justify-content: center;
}

.datasource-type-name {
  min-width: 0;
  overflow: hidden;
  color: var(--text-primary);
  font-size: 13px;
  line-height: 16px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.data-job-mode-switch {
  width: 32px;
  height: 32px;
  border: 1px solid #babdc5;
  display: inline-block;
  border-radius: 4px;
  font-size: 12px;
  vertical-align: middle;
  position: relative;

  &:hover {
    cursor: pointer;
  }

  .icon {
    position: absolute;
    right: 9px;
    top: 9px;
  }
}
</style>
