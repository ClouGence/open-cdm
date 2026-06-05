<template>
  <div class="machine">
    <Breadcrumb style="margin-bottom: 14px">
      <BreadcrumbItem>{{ $t('ji-qi-guan-li') }}</BreadcrumbItem>
    </Breadcrumb>
    <!--    <query-header :handle-click-add-btn="handleClickAddBtn" :handle-query="handleQuery"-->
    <!--                  :handle-refresh="handleRefresh"-->
    <!--                  :query="queryForm" :select-options="Object.values(deployEnvListMap)"-->
    <!--                  :show-add-btn="userInfo.authArr.includes('CLUSTER_CREATE')"-->
    <!--                  :add-btn-text="$t('tian-jia-ji-qun')" :placeholder="$t('qing-shu-ru-ji-qun-id-ming-cheng-cha-xun')"/>-->
    <query-header
      :handle-click-add-btn="handleClickAddBtn"
      :handle-query="handleQuery"
      :handle-refresh="handleRefresh"
      :query="queryForm"
      :select-options="Object.values(deployEnvListMap)"
      :show-add-btn="true"
      :add-btn-text="$t('tian-jia-ji-qun')"
      :placeholder="$t('qing-shu-ru-ji-qun-id-ming-cheng-cha-xun')"
    />
    <a-table
      :columns="clusterColumns"
      :data-source="clusterList"
      :locale="{ emptyText: $t('zan-wu-shu-ju') }"
      :rowKey="(record) => record.id"
      size="small"
    >
      <template #cluster_name="record">
        <span>{{ record.clusterName }}/{{ record.clusterDesc }}</span>
        <cc-iconfont
          v-if="userInfo.authArr.includes('CLUSTER_UPDATE_DESC')"
          :size="8"
          name="edit"
          style="margin-right: 4px; margin-left: 4px"
          @click="handleClickEditClusterNameBtn(record)"
        />
      </template>
      <template #deployEnvType="record">
        <span>
          {{ deployEnvListMap[record.deployEnvType] && deployEnvListMap[record.deployEnvType].name }}
        </span>
      </template>
      <template #region="record">
        {{ regionList[record.region] }}
      </template>
      <template #num="record">
        <span>{{ record.runningCount }}/{{ record.workerCount }}</span>
      </template>
      <template #actions="record">
        <!--        <a-button v-if="userInfo.authArr.includes('WORKER_LIST')" size="small" type="link"-->
        <!--                  @click="goMachineList(record)">{{ $t('ji-qi-lie-biao') }}-->
        <!--        </a-button>-->
        <!--        <a-popconfirm v-if="userInfo.authArr.includes('CLUSTER_DELETE')" cancel-text="取消" ok-text="确认" :title="$t('que-ding-shan-chu-gai-ji-qun-ma')"-->
        <!--                      @confirm="handleDeleteCluster(record)">-->
        <!--          <a-button size="small" type="link">{{ $t('shan-chu') }}</a-button>-->
        <!--        </a-popconfirm>-->
        <a-button size="small" type="link" @click="goMachineList(record)">
          {{ $t('ji-qi-lie-biao') }}
        </a-button>
        <a-popconfirm
          :cancel-text="$t('qu-xiao')"
          :ok-text="$t('que-ren')"
          :title="$t('que-ding-shan-chu-gai-ji-qun-ma')"
          @confirm="handleDeleteCluster(record)"
        >
          <a-button size="small" type="link">{{ $t('shan-chu') }}</a-button>
        </a-popconfirm>
      </template>
    </a-table>
    <a-modal
      v-model="showEditClusterNameModal"
      :closable="false"
      :mask-closable="false"
      :width="320"
      :cancelText="$t('qu-xiao')"
      :okText="$t('bao-cun')"
      wrapClassName="have-footer"
      @ok="handleUpdateClusterName"
    >
      <div class="edit-cluster-name-modal">
        <a-input
          v-model="clusterDesc"
          :maxLength="256"
          :minLength="2"
          autoSize
          :placeholder="$t('qing-shu-ru-ji-qun-ming-zi')"
          style="width: 280px; margin-bottom: 10px; height: 62px"
          type="textarea"
        />
        <div>
          {{ $t('chang-du-wei-2256-ge-zi-fu-yi-da-xiao-zi-mu-huo-zhong-wen-kai-tou-ke-bao-han-shu-zi-huo') }}
        </div>
      </div>
    </a-modal>
    <a-modal v-model="showAddClusterModal" :closable="false" :mask-closable="false" :width="438" :title="$t('chuang-jian-ji-qun')">
      <div class="add-cluster-modal">
        <a-form-model ref="addClusterFormRef" :label-col="labelCol" :model="newClusterForm" :rules="newClusterFormRule" :wrapper-col="wrapperCol">
          <a-form-model-item :label="$t('ji-qun-ming-cheng')" prop="clusterDesc">
            <a-input v-model="newClusterForm.clusterDesc" style="width: 280px" />
          </a-form-model-item>
          <a-form-model-item :label="$t('yun-huo-ji-fang-ming-cheng')" prop="deployEnvType">
            <!--            <a-radio-group v-model="newClusterForm.deployEnvType" button-style="solid">-->
            <!--              <a-radio-button :value="env.value" :key="env.value" v-for="env in Object.values(deployEnvListMap)">-->
            <!--                {{ env.name }}-->
            <!--              </a-radio-button>-->
            <!--            </a-radio-group>-->
            <cc-cluster-type-select v-model="newClusterForm.deployEnvType" :deployEnvList="deployEnvList" />
          </a-form-model-item>
          <a-form-model-item :label="$t('di-qu')" prop="region">
            <cc-region-select v-model="newClusterForm.region" :env="newClusterForm.deployEnvType" />
          </a-form-model-item>
        </a-form-model>
        <div class="footer">
          <a-button type="primary" @click="handleAddCluster">{{ $t('bao-cun') }}</a-button>
          <a-button @click="showAddClusterModal = false">{{ $t('qu-xiao') }}</a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { ACTIONS_TYPE } from '@/store/actions';
import { CLUSTER_ENV } from '@/const';
import QueryHeader from '../components/QueryHeader';

export default {
  name: 'Cluster',
  components: { QueryHeader },
  data() {
    return {
      refreshLoading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 18 },
      value: '',
      searchType: 'all',
      searchInput: '',
      queryForm: {
        type: 'all',
        text: ''
      },
      showEditClusterNameModal: false,
      showAddClusterModal: false,
      clusterDesc: '',
      selectedCluster: {},
      clusterFilterInfo: {
        deployEnvType: '',
        query: ''
      },
      page: 1,
      pageSize: 20,
      clusterList: [{}],
      newClusterForm: {
        clusterDesc: '',
        deployEnvType: 'ALIBABA_CLOUD_HOSTED',
        region: ''
      },
      newClusterFormRule: {
        clusterDesc: [
          {
            required: true,
            message: this.$t('ming-cheng-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        deployEnvType: [
          {
            required: true,
            message: this.$t('huan-jing-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ],
        region: [
          {
            required: true,
            message: this.$t('di-qu-bu-neng-wei-kong'),
            trigger: 'blur'
          }
        ]
      }
    };
  },
  computed: {
    clusterColumns() {
      const { deployEnvType, query } = this.clusterFilterInfo;
      const columns = [
        {
          title: this.$t('ji-qun-id-ming-cheng'),
          filteredValue: [query] || null,
          key: 'cluster_name',
          onFilter: (value, record) =>
            (record.clusterName && record.clusterName.includes(value)) || (record.clusterDesc && record.clusterDesc.includes(value)),
          scopedSlots: { customRender: 'cluster_name' }
        },
        {
          title: this.$t('lei-xing'),
          key: 'deployEnvType',
          filteredValue: [deployEnvType] || null,
          onFilter: (value, record) => value === 'all' || (record.deployEnvType && record.deployEnvType.includes(value)),
          scopedSlots: { customRender: 'deployEnvType' }
        },
        {
          title: this.$t('qu-yu-0'),
          scopedSlots: { customRender: 'region' }
        },
        {
          title: this.$t('ke-yong-shu-liang-ji-qi-shu-liang'),
          scopedSlots: { customRender: 'num' }
        },
        {
          title: this.$t('cao-zuo'),
          fixed: 'right',
          scopedSlots: { customRender: 'actions' }
        }
      ];
      return columns;
    },
    ...mapState(['deployEnvListMap', 'userInfo', 'regionList', 'globalSetting']),
    deployEnvList() {
      return Object.values(this.deployEnvListMap).filter(
        (env) => !(this.globalSetting.outputDeployEnv && env.value === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED)
      );
    }
  },
  methods: {
    handlePageChange(page) {
      this.page = page;
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
    },
    handleQuery() {
      const { type, text } = this.queryForm;
      this.clusterFilterInfo = {
        deployEnvType: type,
        query: text
      };
    },
    async getClusterList() {
      const res = await this.$services.dmClusterListByCondition({ data: {} });
      if (res.success) {
        this.clusterList = res.data;
        this.handleQuery();
      }
    },
    handleClickAddBtn() {
      this.showAddClusterModal = true;
    },
    async handleAddCluster() {
      this.$refs.addClusterFormRef.validate(async (valid) => {
        if (valid) {
          const res = await this.$services.dmClusterCreate({
            data: this.newClusterForm,
            msg: this.$t('tian-jia-ji-qun-cheng-gong')
          });
          if (res.success) {
            this.showAddClusterModal = false;
            await this.getClusterList();
          }
        } else {
          this.$Message.error(this.$t('que-shao-can-shu'));
        }
      });
    },
    handleRefresh() {
      this.getClusterList();
    },
    async handleDeleteCluster(cluster) {
      const { id } = cluster;
      const data = {
        clusterId: id
      };
      const res = await this.$services.dmClusterDelete({
        data,
        msg: this.$t('shan-chu-ji-qun-cheng-gong')
      });

      if (res.success) {
        await this.getClusterList();
      }
    },
    goMachineList(cluster) {
      this.$router.push({
        name: 'System_Machine_List',
        params: { clusterId: cluster.id }
      });
    },
    handleClickEditClusterNameBtn(cluster) {
      this.selectedCluster = cluster;
      this.clusterDesc = cluster.clusterDesc;
      this.showEditClusterNameModal = true;
    },
    async handleUpdateClusterName() {
      const { ...cluster } = this.selectedCluster;
      if (this.clusterDesc) {
        const data = {
          clusterId: cluster.id,
          clusterDesc: this.clusterDesc
        };
        const res = await this.$services.dmClusterUpdateDesc({
          data,
          msg: this.$t('geng-xin-ming-cheng-cheng-gong')
        });

        if (res.success) {
          this.getClusterList();
          this.showEditClusterNameModal = false;
        }
      }
    }
  },
  created() {
    this.$store.dispatch(ACTIONS_TYPE.GET_DEPLOY_ENV_LIST);
    this.getClusterList();
  }
};
</script>

<style lang="less" scoped>
//.edit-cluster-name-modal {
//  padding: 20px;
//}
//
//.add-cluster-modal {
//  padding: 20px;
//}

.machine {
  .operation {
    display: flex;
    justify-content: space-between;
    margin-bottom: 12px;
  }
}
</style>
