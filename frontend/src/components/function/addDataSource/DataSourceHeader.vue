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
        <Select
          v-model="searchKey.dbType"
          class="datasource-type-select"
          filterable
          clearable
          :placeholder="$t('quan-bu')"
          @on-clear="handleClearDataSourceType"
        >
          <Option v-for="type in dataSourceTypes" :key="type.dsKey" :value="type.dsKey" :label="type.displayName" translate="no">
            <span class="datasource-type-option">
              <DataSourceIcon class="datasource-type-icon" size="22px" :type="type.dsKey" leftMargin="0"></DataSourceIcon>
              <span class="datasource-type-name" :title="type.displayName">
                {{ type.displayName }}
              </span>
            </span>
          </Option>
        </Select>
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
import { normalizeDsSupportNameGroups } from '@/utils/datasourceSupport';

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
      dataSourceTypes: []
    };
  },
  computed: {
    ...mapState(['dmGlobalSetting'])
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
      if (nextSearchKey.dbType === 'all') {
        nextSearchKey.dbType = '';
      }
      if (nextSearchKey.deployType === 'all') {
        nextSearchKey.deployType = '';
      }
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
      this.dataSourceTypes = normalizeDsSupportNameGroups(this.dmGlobalSetting?.dsSupportNames).flatMap((group) => group);
    },
    _handleSearch() {
      sessionStorage.setItem('datasource_search_params', JSON.stringify({ searchType: this.searchType, ...this.searchKey }));
      this.handleSearch(this.searchKey, 'init');
    },
    handleClearDataSourceType() {
      this.searchKey.dbType = '';
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

.datasource-type-select {
  width: 300px;

  :deep(.ivu-select-selection) {
    min-height: 32px;
  }

  :deep(.ivu-select-placeholder),
  :deep(.ivu-select-selected-value) {
    color: var(--text-primary);
  }
}

.datasource-type-option {
  display: flex;
  min-width: 0;
  width: 280px;
  height: 32px;
  align-items: center;
  gap: 8px;
}

.datasource-type-icon {
  display: inline-flex;
  width: 26px;
  flex: 0 0 26px;
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
