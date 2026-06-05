<template>
  <CCModal
    :modelValue="visible"
    @update:modelValue="(val) => $emit('update:visible', val)"
    :title="$t('qi-yong-shu-ju-guan-li')"
    :mask-closable="false"
    width="500px"
    @on-cancel="handleCloseEnableQueryModal"
  >
    <Form :model="form">
      <FormItem :label="$t('bang-ding-ji-qun')" prop="clusterId">
        <Select v-model="form.clusterId" style="width: 420px" filterable @on-change="handleChangeCluster">
          <Option
            v-for="cluster in clusterList"
            :value="cluster.id"
            :key="cluster.id"
            :label="cluster.clusterDesc ? cluster.clusterDesc : cluster.clusterName"
            :style="`${cluster.runningCount ? '' : 'cursor: not-allowed'}`"
          >
            <p>{{ cluster.clusterName }}</p>
            <p style="color: #ccc; margin: 5px 0">
              {{ cluster.clusterDesc }}
              <span style="margin-left: 8px">{{ cluster.runningCount }}/{{ cluster.workerCount }}</span>
            </p>
          </Option>
        </Select>
      </FormItem>
      <FormItem label="" v-if="this.currentCluster.runningCount === 0">
        <span>
          <span style="color: #ff6e0c">
            <i style="margin-left: 10px; margin-right: 8px" class="iconfont iconTIP"></i>
            {{ $t('gai-ji-qun-wu-cun-huo-ji-qi') }}
          </span>
          <a class="text-cc-primary" :href="`/#/system/dmmachine/list/${form.clusterId}`">
            {{ $t('guan-li-ji-qi') }}
          </a>
        </span>
      </FormItem>
      <FormItem>
        <div style="display: flex; align-items: center">
          <Button @click="handleTestConnection" :loading="testConnectionLoading">
            {{ $t('ce-shi-lian-jie') }}
          </Button>
          <div style="display: flex; margin-left: 5px; align-items: center" v-if="testDsMsg">
            <Icon type="ios-checkmark-circle" v-if="testDsMsg === $t('ce-shi-lian-jie-cheng-gong')" size="20" color="green" />
            <Icon type="ios-close-circle" v-else size="20" color="red" />
            <div style="margin-left: 5px">{{ testDsMsg }}</div>
          </div>
        </div>
      </FormItem>
    </Form>
    <template #footer>
      <div>
        <Button @click="handleCloseEnableQueryModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleEnableQuery" :loading="testConnectionLoading">
          {{ $t('que-ding') }}
        </Button>
      </div>
    </template>
  </CCModal>
</template>

<script>
export default {
  name: 'EnableQueryModal',
  props: {
    visible: Boolean,
    datasource: Object,
    handleCloseModal: Function,
    getDsList: Function,
    type: {
      default: 'addDataSource'
    }
  },
  data() {
    return {
      testConnectionLoading: false,
      testDsMsg: '',
      taskInfo: {},
      clusterList: [],
      currentCluster: {},
      form: {
        clusterId: '',
        hostType: 'PUBLIC'
      },
      hasTest: false,
      testSuccess: false,
      loading: false,
      useSSL: false
    };
  },
  methods: {
    async handleTestConnection() {
      this.testConnectionLoading = true;
      const res = await this.$services.dmDataSourceTestEnableQuery({
        data: {
          dataSourceId: this.datasource.id,
          clusterId: this.form.clusterId,
          hostType: this.form.hostType
        }
      });
      this.testConnectionLoading = false;
      if (res.success) {
        if (res.success) {
          this.testDsMsg = this.$t('ce-shi-lian-jie-cheng-gong');
        } else {
          this.testDsMsg = this.$t('ce-shi-lian-jie-shi-bai');
        }
      }
    },
    async handleEnableQuery() {
      const res = await this.$services.dmDataSourceEnableDsQuery({
        data: {
          dataSourceId: this.datasource.id,
          clusterId: this.form.clusterId,
          hostType: this.form.hostType
        }
      });
      if (res.success) {
        this.$message.success(this.$t('yi-qi-yong-shu-ju-guan-li-gong-neng', [this.datasource.instanceId]));
        this.getDsList();
        this.handleCloseEnableQueryModal();
      } else {
        this.handleCloseEnableQueryModal();
      }
      console.log('res', res);
    },
    handleCloseEnableQueryModal() {
      this.datasource.enableQuery = false;
      this.testDsMsg = '';
      this.hasTest = false;
      this.testSuccess = false;
      this.loading = false;
      this.form = {
        clusterId: '',
        hostType: 'PUBLIC'
      };
      this.handleCloseModal();
    },
    handleChangeHostType() {
      this.hasTest = false;
      this.testSuccess = false;
      this.loading = false;
    },
    handleChangeCluster() {
      this.clusterList.forEach((cluster) => {
        if (cluster.id === this.form.clusterId) {
          this.currentCluster = cluster;
        }
      });
    },
    async listDsBindCluster() {
      const res = await this.$services.dmDataSourceListDsBindCluster();
      if (res.code === '1') {
        this.clusterList = res.data;
        for (let i = 0; i < this.clusterList.length; i++) {
          if (this.clusterList[i].runningCount > 0) {
            this.form.clusterId = this.clusterList[i].id;
            return;
          }
        }
      }
    },
    getCurrentHostType() {
      this.form.hostType = this.datasource.publicHost ? 'PUBLIC' : 'PRIVATE';
    }
  },
  watch: {
    visible(val) {
      console.warn(123123123, val);

      if (val) {
        this.listDsBindCluster();
        this.getCurrentHostType();
      }
    },
    datasource: {
      deep: true,
      handler(val) {
        this.form.hostType = val.publicHost ? 'PUBLIC' : 'PRIVATE';
      }
    }
  }
};
</script>

<style scoped lang="less">
.footer {
  width: 100%;
  display: flex;
  justify-content: center;
}
</style>
