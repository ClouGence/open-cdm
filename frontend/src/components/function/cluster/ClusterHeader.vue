<template>
  <div class="cluster-filter-bar option border-radius-card">
    <Form ref="formInline" class="cluster-filter-form" :model="searchData" inline label-position="right">
      <FormItem>
        <Select v-model="searchType" style="width: 140px" @on-change="handleChangeSearchType">
          <Option value="clusterDesc" :label="$t('ji-qun-miao-shu')">
            <span>{{ $t('ji-qun-miao-shu') }}</span>
          </Option>
          <Option value="clusterName" :label="$t('ji-qun-ming-cheng')">
            <span>{{ $t('ji-qun-ming-cheng') }}</span>
          </Option>
          <!--                    <Option value="owner" label="创建人">-->
          <!--                        <span>创建人</span>-->
          <!--                    </Option>-->
          <Option v-if="!isDmPage" value="type" :label="$t('lei-xing')">
            <span>{{ $t('lei-xing') }}</span>
          </Option>
        </Select>
      </FormItem>
      <FormItem v-if="searchType === 'owner'">
        <Input v-model="searchData.owner" style="width: 280px" @on-keydown="handleEnterSearch" />
      </FormItem>
      <FormItem v-if="searchType === 'clusterDesc'">
        <Input v-model="searchData.clusterDescLike" style="width: 280px" @on-keydown="handleEnterSearch" />
      </FormItem>
      <FormItem v-if="searchType === 'clusterName'">
        <Input v-model="searchData.clusterNameLike" style="width: 280px" @on-keydown="handleEnterSearch" />
      </FormItem>
      <FormItem v-if="searchType === 'type' && !isDmPage">
        <Select v-model="searchData.cloudOrIdcName" style="width: 250px">
          <Option value="ALIBABA_CLOUD" :label="$t('a-li-yun')">
            <span>{{ $t('a-li-yun') }}</span>
          </Option>
          <Option value="SELF_MAINTENANCE" :label="$t('zi-jian-ji-fang')">
            <span>{{ $t('zi-jian-ji-fang') }}</span>
          </Option>
          <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
        </Select>
      </FormItem>
      <FormItem>
        <Button type="primary" ghost @click="_handleSearch(searchType, searchData)">
          {{ $t('cha-xun') }}
        </Button>
      </FormItem>
    </Form>
    <div class="page-header-function">
      <Button v-if="hasManageAuth" type="primary" @click="handleAddCluster">
        {{ $t('xin-zeng-ji-qun') }}
      </Button>
    </div>
  </div>
</template>
<script>
import { mapState } from 'vuex';

export default {
  props: {
    handleSearch: Function,
    handleAddCluster: Function,
    params: Object
  },
  data() {
    return {
      ifClassfy: false,
      classfyType: '',
      searchType: 'clusterName',
      searchData: {
        cloudOrIdcName: '',
        clusterNameLike: '',
        clusterDescLike: ''
      },
      instanceList: []
    };
  },
  created() {
    const params = JSON.parse(sessionStorage.getItem('cluster_search_params'));
    if (params) {
      if (this.isDmPage && params.searchType === 'type') {
        params.searchType = 'clusterName';
        params.cloudOrIdcName = '';
      }
      this.searchData = params;
      this.searchType = params.searchType;
      this.handleSearch(params, 'init');
    } else {
      this.handleSearch(this.searchData, 'init');
    }
  },
  computed: {
    ...mapState(['myAuth']),
    isDmPage() {
      return this.$route.name === 'System_Machine';
    },
    hasManageAuth() {
      if (this.$route.name === 'System_Machine') {
        return this.myAuth.includes('DM_WORKER_MANAGE');
      } else {
        return this.myAuth.includes('CC_WORKER_MANAGE');
      }
    }
  },
  methods: {
    _handleSearch(searchType, searchData) {
      sessionStorage.setItem('cluster_search_params', JSON.stringify({ searchType, ...searchData }));
      this.handleSearch(searchData, 'init');
    },
    handleChangeSearchType() {
      // Reset all search values when switching query type
      this.searchData = {
        cloudOrIdcName: '',
        clusterNameLike: '',
        clusterDescLike: ''
      };
    },
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this._handleSearch(this.searchType, this.searchData);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.cluster-filter-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: nowrap;
  gap: 12px;
  min-height: 56px;
  height: auto;
  line-height: normal;
  padding: 12px 14px;
  position: relative;
  box-sizing: border-box;
  background: var(--bg-secondary);
  border: 1px solid var(--border-light);
  border-radius: 8px;

  .cluster-filter-form {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
    margin: 0;
  }

  .ivu-form-inline .ivu-form-item {
    display: flex;
    align-items: center;
    vertical-align: middle;
    margin-bottom: 0;
    margin-right: 8px;
  }

  .ivu-form-item {
    margin-bottom: 0;
  }

  :deep(.ivu-form-item-content) {
    display: flex;
    align-items: center;
    line-height: normal;
  }

  .page-header-function {
    position: static;
    flex: 0 0 auto;
    display: flex;
    align-items: center;
    margin-left: auto;

    a {
      color: #333;
      margin-right: 10px;
    }

    button {
      margin-left: 0;
    }

    .ivu-tooltip {
      margin-left: 8px;
    }
  }
}

@media (max-width: 960px) {
  .cluster-filter-bar {
    align-items: flex-start;
    flex-wrap: wrap;

    .page-header-function {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style>
