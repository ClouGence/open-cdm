<template>
  <div class="option">
    <div class="left">
      <Form ref="formInline" :model="searchData" inline label-position="right">
        <FormItem>
          <Select v-model="searchType" style="width: 140px" @on-change="handleChangeSearchType">
            <Option value="clusterDesc" :label="$t('ji-qun-miao-shu')">
              <span>{{ $t('ji-qun-miao-shu') }}</span>
            </Option>
            <Option value="clusterName" :label="$t('ji-qun-ming-cheng')">
              <span>{{ $t('ji-qun-ming-cheng') }}</span>
            </Option>
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
          <Button :loading="refreshLoading" type="primary" ghost @click="_handleSearch(searchType, searchData)">
            {{ $t('cha-xun') }}
          </Button>
        </FormItem>
      </Form>
    </div>
    <div class="right">
      <Button v-if="hasManageAuth" type="primary" @click="handleAddCluster">
        <Icon type="md-add" />
        {{ $t('xin-zeng-ji-qun') }}
      </Button>
      <Button type="default" @click="_handleSearch(searchType, searchData)" :loading="refreshLoading">
        <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
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
    refreshLoading: Boolean,
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
