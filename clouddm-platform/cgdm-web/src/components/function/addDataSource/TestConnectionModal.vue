<template>
  <CCModal
    :modelValue="visible"
    @update:modelValue="(val) => $emit('update:visible', val)"
    :title="$t('ce-shi-lian-jie')"
    :mask-closable="false"
    @on-cancel="_handleCloseModal"
    width="420px"
    footer-hide
  >
    <Form :model="form" label-position="right" :label-width="100">
      <FormItem :label="$t('chan-pin-ji-qun')" prop="productCluster" v-if="type === 'addDataSource' && getCcProductClusterList.length > 0">
        <Select v-model="form.clusterCode" style="width: 280px" filterable @on-change="handleChangeProductCluster">
          <Option v-for="cluster in getCcProductClusterList" :value="cluster.clusterCode" :key="cluster.id" :label="cluster.clusterDesc">
            <p>{{ cluster.clusterDesc }}</p>
          </Option>
        </Select>
      </FormItem>
      <FormItem :label="$t('bang-ding-ji-qun')" prop="clusterId">
        <Select v-model="form.clusterId" style="width: 280px" filterable @on-change="handleChangeCluster">
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
          <a class="text-cc-primary" :href="`/#/ccsystem/resource/${form.clusterId}`">
            {{ $t('guan-li-ji-qi') }}
          </a>
        </span>
      </FormItem>
      <FormItem>
        <Button :loading="loading" @click="testConnection" style="margin-right: 10px">
          {{ $t('ce-shi-lian-jie') }}
        </Button>
        <a v-if="showCancelConnection" style="margin-right: 10px; color: #999; font-size: 12px; cursor: pointer" @click="handleCancelConnection">
          {{ $t('qu-xiao-lian-jie') }}
        </a>
        <span v-if="hasTest">
          <Icon :type="testSuccess ? 'ios-checkmark-circle' : 'ios-close-circle'" :color="testSuccess ? 'green' : 'red'" />
          {{ testSuccess ? $t('ce-shi-lian-jie-cheng-gong') : $t('ce-shi-lian-jie-shi-bai') }}
        </span>
      </FormItem>
    </Form>
  </CCModal>
</template>

<script>
import { mapState } from 'vuex';
import { isDb2, isHana, isMongoDB } from '@/utils';
import DataSourceGroup from '@/views/dataSourceGroup.json';
import { clearAllPending } from '@/services/http/cancelRequest';

export default {
  name: 'TestConnectionModal',
  props: {
    visible: Boolean,
    datasource: Object,
    handleCloseModal: Function,
    type: {
      default: 'addDataSource'
    }
  },
  data() {
    return {
      taskInfo: {},
      clusterList: [],
      currentCluster: {},
      form: {
        clusterId: '',
        hostType: 'PUBLIC',
        clusterCode: null
      },
      hasTest: false,
      testSuccess: false,
      loading: false,
      useSSL: false,
      showCancelConnection: false,
      cancelConnectionTimer: null
    };
  },
  computed: {
    ...mapState(['productClusterList', 'selectCcProductCluster']),
    getCcProductClusterList() {
      const ccList = [];
      this.productClusterList.forEach((cluster) => {
        if (cluster.product === 'CloudCanal') {
          ccList.push(cluster);
        }
      });
      return ccList;
    }
  },
  methods: {
    handleChangeHostType() {
      this.hasTest = false;
      this.testSuccess = false;
      this.loading = false;
    },
    _handleCloseModal() {
      this.hasTest = false;
      this.testSuccess = false;
      this.loading = false;
      this.showCancelConnection = false;
      if (this.cancelConnectionTimer) {
        clearTimeout(this.cancelConnectionTimer);
        this.cancelConnectionTimer = null;
      }
      this.form = {
        clusterId: '',
        hostType: 'PUBLIC',
        clusterCode: null
      };
      this.handleCloseModal();
    },
    separatePort(type) {
      return !['Kudu', 'Redis', 'ClickHouse', 'ElasticSearch', 'RabbitMQ', 'RocketMQ', 'Kafka', 'MongoDB', 'PolarDbX'].includes(type);
    },
    testConnection() {
      // 每次重新测试时先清空上一次的测试结果文案
      this.hasTest = false;
      this.testSuccess = false;
      this.msg = '';

      if (this.type === 'addDataSource') {
        this.testConnectionBeforeAdd();
      } else {
        this.loading = true;
        this.$services
          .dmDataSourceTestEnableQuery({
            data: {
              dataSourceId: this.datasource.id,
              clusterId: this.form.clusterId,
              hostType: this.form.hostType
            }
          })
          .then((res) => {
            this.hasTest = true;
            if (res.success) {
              this.testSuccess = true;
              this.msg = this.$t('ce-shi-lian-jie-cheng-gong');
            } else {
              this.testSuccess = false;
              this.msg = this.$t('ce-shi-lian-jie-shi-bai');
            }
          })
          .catch((e) => {
            console.log(e);
          })
          .finally(() => {
            this.loading = false;
          });
      }
    },
    testConnectionBeforeAdd() {
      // parse useSSL from kvConfig
      this.extractUseSSL();
      // 清空上一次的测试结果文案
      this.hasTest = false;
      this.testSuccess = false;
      this.msg = '';
      const {
        asSysDba,
        host,
        port,
        publicHost,
        publicPort,
        account,
        password,
        type,
        securityType,
        connectType,
        dbName,
        noValidateDbName,
        connectTypeValue,
        driver,
        version,
        dsKvConfigs
      } = this.datasource;
      const { hostType, clusterId } = this.form;
      const isSeparate = this.separatePort(type);
      this.loading = true;
      const formData = {
        dbName: isDb2(type) || isHana(type) ? dbName : noValidateDbName,
        clusterId,
        dsType: type,
        securityType,
        connectType,
        host:
          hostType === 'PUBLIC'
            ? isSeparate
              ? `${publicHost}:${publicPort}${type === 'Oracle' ? `:${connectTypeValue}` : ''}`
              : publicHost
            : isSeparate
              ? `${host}:${port}${type === 'Oracle' ? `:${connectTypeValue}` : ''}`
              : host,
        userName: `${account}${asSysDba ? ' as SYSDBA' : ''}`,
        password,
        driver,
        version,
        useSSL: this.useSSL
      };

      if (dsKvConfigs) {
        console.log('dsKvConfigs');
        dsKvConfigs.forEach((dsKv) => {
          if (dsKv.configName === 'isAtlas') {
            if (isMongoDB(type)) {
              formData.isAtlas = JSON.parse(dsKv.currentCount || dsKv.defaultValue);
            }
          }
        });
      }

      this.$services
        .ccDataSourceTestConnectionBeforeAdd({
          data: formData
        })
        .then((res) => {
          this.hasTest = true;
          if (res.success) {
            this.testSuccess = true;
            this.msg = this.$t('ce-shi-lian-jie-cheng-gong');
          } else {
            this.testSuccess = false;
            this.msg = this.$t('ce-shi-lian-jie-shi-bai');
          }
        })
        .finally(() => {
          this.loading = false;
        });
    },
    handleCancelConnection() {
      clearAllPending();
      this.showCancelConnection = false;
      this.loading = false;
      if (this.cancelConnectionTimer) {
        clearTimeout(this.cancelConnectionTimer);
        this.cancelConnectionTimer = null;
      }
    },
    handleConnectionLoadingChange(loading) {
      if (loading) {
        this.showCancelConnection = false;
        if (this.cancelConnectionTimer) {
          clearTimeout(this.cancelConnectionTimer);
        }
        this.cancelConnectionTimer = setTimeout(() => {
          if (this.loading) {
            this.showCancelConnection = true;
          }
        }, 1000);
      } else {
        this.showCancelConnection = false;
        if (this.cancelConnectionTimer) {
          clearTimeout(this.cancelConnectionTimer);
          this.cancelConnectionTimer = null;
        }
      }
    },
    handleChangeProductCluster() {
      this.getCluster();
    },
    handleChangeCluster() {
      this.clusterList.forEach((cluster) => {
        if (cluster.id === this.form.clusterId) {
          this.currentCluster = cluster;
        }
      });
    },
    async getCluster() {
      this.clusterList = [];

      if (this.type === 'dataSourceList') {
        const res = await this.$services.dmDataSourceListDsBindCluster();
        if (res.code === '1') {
          this.clusterList = res.data || [];
        }
      } else {
        const res = await this.$services.ccClusterListByCondition({ data: {}, headers: { 'X-Product-Code': this.form.clusterCode } });
        if (res.success) {
          this.clusterList = res.data || [];
        }
      }

      for (let i = 0; i < this.clusterList.length; i++) {
        if (this.clusterList[i].runningCount > 0) {
          this.form.clusterId = this.clusterList[i].id;
          this.currentCluster = this.clusterList[i];
          return;
        }
      }

      if (this.clusterList.length > 0) {
        this.form.clusterId = this.clusterList[0].id;
        this.currentCluster = this.clusterList[0];
      }
    },
    getCurrentHostType() {
      this.form.hostType = this.type === 'addDataSource' ? 'PUBLIC' : this.datasource.publicHost ? 'PUBLIC' : 'PRIVATE';
    },
    extractUseSSL() {
      if (this.datasource.dsKvConfigs === undefined || this.datasource.dsKvConfigs.length === 0) {
        this.useSSL = false;
        return;
      }
      this.datasource.dsKvConfigs.forEach((config) => {
        if (config.configName === 'useSSL') {
          if (config.currentCount === undefined) {
            this.useSSL = config.defaultValue;
            return;
          }
          this.useSSL = config.currentCount;
        }
      });
    }
  },
  watch: {
    visible(val) {
      if (val) {
        // 清除上一次测试连接的状态
        this.hasTest = false;
        this.testSuccess = false;
        this.loading = false;

        this.form.clusterCode = this.type === 'addDataSource' ? this.selectCcProductCluster : null;
        this.getCluster();
        this.getCurrentHostType();
      }
    },
    loading(val) {
      this.handleConnectionLoadingChange(val);
    },
    datasource: {
      deep: true,
      handler(val) {
        this.form.hostType = this.type === 'addDataSource' ? 'PUBLIC' : val.publicHost ? 'PUBLIC' : 'PRIVATE';
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
