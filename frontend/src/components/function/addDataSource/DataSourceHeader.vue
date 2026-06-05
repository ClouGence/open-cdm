<template>
  <div class="page-header-container border-radius-card">
    <Form ref="formInline" :model="searchKey" inline label-position="right" style="padding-right: 300px">
      <FormItem>
        <Select v-model="searchType" style="width: 160px" @on-change="handleChangeSearchType">
          <Option value="type" :label="$t('lei-xing')">
            <span>{{ $t('lei-xing') }}</span>
          </Option>
          <Option value="desc" :label="$t('shu-ju-yuan-miao-shu')">
            <span>{{ $t('shu-ju-yuan-miao-shu') }}</span>
          </Option>
          <Option value="deploy" :label="$t('bu-shu-lei-xing')">
            <span>{{ $t('bu-shu-lei-xing') }}</span>
          </Option>
          <Option value="host" :label="$t('host')">
            <span>{{ $t('host') }}</span>
          </Option>
          <Option value="dataSourceId" :label="$t('shu-ju-yuan-shu-zi-id')">
            <span>{{ $t('shu-ju-yuan-shu-zi-id') }}</span>
          </Option>
        </Select>
      </FormItem>
      <FormItem v-if="searchType === 'type'">
        <Select v-model="searchKey.dbType" style="width: 250px" filterable>
          <Option value="all">{{ $t('quan-bu') }}</Option>
          <Option v-for="type of dataSourceTypes" :value="type" :key="type">{{ type }}</Option>
        </Select>
      </FormItem>
      <FormItem v-if="searchType === 'dataSourceId'">
        <Input
          v-model="searchKey.dataSourceId"
          :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
          @on-keydown="handleEnterSearch"
          type="number"
          style="width: 250px"
        />
      </FormItem>
      <FormItem v-if="searchType === 'desc'">
        <Input
          v-model="searchKey.dataSourceDescLike"
          :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
          @on-keydown="handleEnterSearch"
          style="width: 250px"
        />
      </FormItem>
      <FormItem v-if="searchType === 'deploy'">
        <Select v-model="searchKey.deployType" style="width: 250px">
          <Option value="all">{{ $t('quan-bu') }}</Option>
          <Option value="SELF_MAINTENANCE">{{ $t('zi-jian') }}</Option>
          <Option value="ALIBABA_CLOUD_HOSTED">{{ $t('a-li-yun') }}</Option>
        </Select>
      </FormItem>
      <FormItem v-if="searchType === 'host'">
        <Input
          v-model="searchKey.dsHostLike"
          :placeholder="$t('shu-ru-guan-jian-zi-jin-hang-mo-hu-sou-suo')"
          @on-keydown="handleEnterSearch"
          style="width: 250px"
        />
      </FormItem>
      <FormItem>
        <Button :loading="refreshLoading" type="primary" @click="_handleSearch">
          {{ $t('cha-xun') }}
        </Button>
      </FormItem>
    </Form>
    <div class="page-header-function">
      <Button v-if="supportAdd" type="primary" ghost @click="handleShowAddDataSource">
        <Icon type="md-add" />
        {{ $t('xin-zeng-shu-ju-yuan') }}
      </Button>
      <Button type="default" style="margin-left: 6px" @click="_handleSearch" :loading="refreshLoading">
        <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
      </Button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'DataSourceHeader',
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
      allowedSearchTypes: ['type', 'desc', 'deploy', 'host', 'dataSourceId'],
      dataSourceTypes: []
    };
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
    this.$services.rdpConstantListFilterDsTypes().then((res) => {
      if (res.success) {
        this.dataSourceTypes = res.data;
      }
    });
  },
  methods: {
    _handleSearch() {
      sessionStorage.setItem('datasource_search_params', JSON.stringify({ searchType: this.searchType, ...this.searchKey }));
      this.handleSearch(this.searchKey, 'init');
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
  background: #ffffff;
  //border: 1px solid #EDEDED;
  height: 60px;
  line-height: 54px;
  //padding: 0 20px;
  position: relative;

  .ivu-form-inline .ivu-form-item {
    vertical-align: middle;
    margin-bottom: 0;
  }

  .ivu-form-item {
    margin-bottom: 0;
  }

  .page-header-function {
    position: absolute;
    right: 20px;
    top: 2px;

    a {
      color: #333;
      margin-right: 10px;
    }

    button {
      margin-left: 8px;
    }

    .ivu-tooltip {
      margin-left: 8px;
    }
  }
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
