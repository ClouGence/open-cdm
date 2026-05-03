<template>
  <div>
    <CCModal v-model="localShow" :title="$t('tian-jia-bai-ming-dan')" width="430px">
      <div>
        <Alert style="margin-bottom: 20px" type="warning" show-icon>
          {{ $t('she-ding-fang-wen-ji-qi-tian-jia-ji-qi-dao-shu-ju-yuan-bai-ming-dan') }}
        </Alert>
        <Form label-position="right" :label-width="100" style="margin-top: 10px">
          <FormItem :label="$t('xuan-ze-chan-pin-ji-qun')" v-if="productClusterList.length > 0">
            <Select @on-change="handleGetWorkerCluster">
              <Option v-for="productCluster in getCcProductClusterList" :value="productCluster.clusterCode" :key="productCluster.clusterCode">
                {{ productCluster.clusterDesc }}
              </Option>
            </Select>
          </FormItem>
          <FormItem :label="$t('xuan-ze-ji-qi')">
            <div class="add-white-list-container">
              <SelectCluster
                :row="selectedRow"
                ref="selectCluster"
                :workerClusterList="workerClusterList"
                :handleChangeType="handleChangeType"
              ></SelectCluster>
            </div>
          </FormItem>
        </Form>
      </div>
      <template #footer>
        <Button @click="handleCancelEdit">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="confirmAddWhiteList">{{ $t('que-ding') }}</Button>
      </template>
    </CCModal>
    <Modal v-model="showAddWhiteListProgress" :title="$t('tian-jia-bai-ming-dan')" footer-hide>
      <div style="padding: 20px">
        <Progress
          :percent="currentPercentage"
          :stroke-width="20"
          :status="currentAddWhiteListStatus === 'SUCCESS' ? 'success' : currentAddWhiteListStatus === 'FAILED' ? 'wrong' : 'active'"
          text-inside
        />
        <p style="padding-left: 10px; text-align: center; margin-top: 16px; font-weight: 500">
          {{
            currentAddWhiteListStatus === 'SUCCESS'
              ? $t('tian-jia-cheng-gong')
              : currentAddWhiteListStatus === 'FAILED'
                ? $t('tian-jia-shi-bai')
                : $t('tian-jia-bai-ming-dan-zhong')
          }}
        </p>
      </div>
    </Modal>
    <StToken ref="stToken"></StToken>
    <AliyunAKSK ref="aliyunAKSK"></AliyunAKSK>
  </div>
</template>

<script>
import SelectCluster from '@/components/function/addDataSource/SelectCluster';
import StToken from '@/components/function/ApplyStToken';
import AliyunAKSK from '@/components/function/ApplyAKSK';
import { mapGetters, mapState } from 'vuex';
import store from '@/store';

export default {
  components: { AliyunAKSK, StToken, SelectCluster },
  props: {
    showAddWhiteList: Boolean,
    selectedRow: Object,
    handleCancelEdit: Function
  },
  computed: {
    ...mapState(['productClusterList']),
    ...mapGetters(['includesCC', 'includesDM']),
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
  data() {
    return {
      localShow: this.showAddWhiteList,
      workerClusterList: [],
      showAddWhiteListProgress: false,
      currentPercentage: 0,
      currentAddWhiteListStatus: ''
    };
  },
  mounted() {
    if (this.productClusterList.length === 0) {
      if (this.includesCC) {
        this.getAllClusterAndWorkers();
      }
    }
  },
  watch: {
    showAddWhiteList(newVal) {
      this.localShow = newVal;
    },
    localShow(newVal) {
      this.$emit('update:showAddWhiteList', newVal);
    }
  },
  methods: {
    handleGetWorkerCluster(data) {
      this.getAllClusterAndWorkers(data);
    },
    getAllClusterAndWorkers(data) {
      this.$services.ccClusterListWithWorkerNets({ data: {}, headers: { 'X-Product-Code': data } }).then((res) => {
        if (res.success) {
          this.workerClusterList = res.data;
          this.workerClusterList.map((item) => {
            item.selected = false;
            item.workerList = [
              {
                title: item.clusterName,
                expand: false,
                children: item.workersNet
              }
            ];
            if (item.workersNet.length === 0) {
              item._disabled = true;
            }
            return null;
          });
        }
      });
    },
    handleChangeType(type) {
      this.selectedRow.whiteListAddType = type;
    },
    confirmAddWhiteList() {
      this.handleCancelEdit();
      this.currentPercentage = 0;
      this.currentAddWhiteListStatus = '';
      const ids = [];
      const that = this;

      store.state.selectedCluster[this.selectedRow.instanceId].map((item) => {
        ids.push(item.id);
        return null;
      });
      this.$services
        .ccAliyunRdsAddClusterWhiteList({
          data: {
            dataSourceId: this.selectedRow.id,
            dataSourceType: this.selectedRow.dataSourceType,
            clusterIds: ids,
            whiteListAddType: this.selectedRow.whiteListAddType
          }
        })
        .then((res) => {
          if (res.success && this.dataIsNaN(res.data)) {
            this.showAddWhiteListProgress = true;

            this.querySourceAddWhiteListInterval = setInterval(() => {
              this.$services.ccConsoleJobQueryConsoleJob({ data: { consoleJobId: res.data } }).then((response) => {
                if (response.success) {
                  that.currentAddWhiteListStatus = response.data.taskState;
                  if (response.data.taskState === 'SUCCESS') {
                    clearInterval(that.querySourceAddWhiteListInterval);
                    that.sourceDisabled = false;
                    setTimeout(() => {
                      that.showAddWhiteListProgress = false;
                    }, 500);
                  } else if (response.data.taskState === 'FAILED') {
                    clearInterval(that.querySourceAddWhiteListInterval);
                    that.sourceDisabled = false;
                  }
                }
              });
            }, 3000);
            store.state.selectedCluster.selectedCluster = {};
          } else if (res.code === '6028') {
            this.$refs.stToken.handleShowAkSk();
          } else if (res.code === '2011') {
            this.$refs.aliyunAKSK.handleShowAkSk();
          } else if (res.success && !res.data) {
            this.$Modal.info({
              title: this.$i18n.global.t('cao-zuo-ti-shi'),
              content: this.$i18n.global.t('gai-shu-ju-yuan-yi-tian-jia-guo-dui-ying-ji-qi-de-bai-ming-dan')
            });
          }
        });

      this.getPercantage = setInterval(() => {
        if (that.currentAddWhiteListStatus === 'SUCCESS') {
          that.currentPercentage = 100;
          clearInterval(that.getPercantage);
          setTimeout(() => {
            that.showAddWhiteList = false;
          }, 500);
        } else if (that.currentAddWhiteListStatus === 'FAILED') {
          clearInterval(that.getPercantage);
        } else if (that.currentPercentage < 90) {
          that.currentPercentage += Math.floor(Math.random() * 3);
        } else if (that.currentPercentage < 97 && that.currentPercentage >= 90) {
          that.currentPercentage += Math.floor(Math.random() * 1.5);
        } else if (that.currentPercentage >= 97) {
          that.currentPercentage = 99;
        }
      }, 200);
    },
    dataIsNaN(value) {
      return typeof value === 'number' && !Number.isNaN(value);
    }
  }
};
</script>
