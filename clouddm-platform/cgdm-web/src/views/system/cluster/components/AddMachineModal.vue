<template>
  <div class="add-machine">
    <div class="header">
      <div class="cycle">
        <cc-iconfont :size="12" color="white" name="machine" />
      </div>
      <div class="choose">
        <div class="title">{{ $t('guan-kong-shu-ju-ku-serviceproduct0001') }}</div>
        <div class="content">
          <div style="display: flex">
            <cc-label required style="margin-right: 10px">
              {{ $t('di-qu') }}
            </cc-label>
            <cc-region-select v-if="isDefaultCluster" v-model="cluster.region" :env="cluster.cloudOrIdcName" />
            <div v-else>
              <a-button disabled style="width: 180px; text-align: left; color: #bbb !important" ghost>
                {{ cluster.region }}
              </a-button>
            </div>
            <a-button
              v-if="cluster.cloudOrIdcName === 'ALIBABA_CLOUD_HOSTED'"
              ghost
              style="width: 116px; margin-left: 10px"
              type="primary"
              @click="getEcsList()"
            >
              {{ $t('huo-qu-ecs-shi-li') }}
            </a-button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="cluster.cloudOrIdcName === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED" class="body">
      <div class="un-select">
        <div class="query">
          <div class="title">{{ $t('dai-xuan-ze-ji-qi') }}</div>
          <div class="condition">
            <a-input v-model="query" :placeholder="$t('qing-shu-ru-miao-shu-ming-cheng')" style="width: 200px; margin-right: 10px" />
            <a-button type="primary" @click="handleQuery">{{ $t('cha-xun') }}</a-button>
          </div>
        </div>
        <div class="table-container">
          <a-table
            :columns="unSelectedWorkerColumns"
            :data-source="unSelectedWorkers"
            :row-selection="rowSelection"
            :rowKey="(record) => record.instanceId"
            border
            size="small"
            style="flex: 1"
          >
            <template #name="record">
              {{ record.instanceId }}/{{ record.instanceName }}
              <a-popover>
                <cc-iconfont v-if="!record.supported" color="#FF6E0D" name="warning-ghost" />
                <template #content>
                  <div>
                    <div v-for="reason in record.unSupportedReasons" :key="reason">{{ reason }}</div>
                  </div>
                </template>
              </a-popover>
            </template>
            <template #ip="record">
              {{ record.allPublicIp[0] }}
            </template>
            <template #expiredTime="record">
              {{ $filters.formatTime(record.expiredTime, 'YYYY/MM/DD') }}
            </template>
          </a-table>
        </div>
      </div>
      <div class="selected">
        <div class="query">
          <div class="title">{{ $t('yi-xuan-ze-ji-qi') }}</div>
        </div>
        <div class="table-container">
          <a-table :columns="selectedWorkerColumns" :data-source="selectedWorkers" :rowKey="(record) => record.instanceId" :width="340" size="small">
            <template #name="record">{{ record.instanceId }}/{{ record.instanceName }}</template>
            <template #actions="record">
              <a-button type="link" @click="handleDeleteMachine(record.instanceId)">
                {{ $t('shan-chu') }}
              </a-button>
            </template>
          </a-table>
        </div>
      </div>
    </div>
    <div v-else class="body self">
      <div class="title">
        <span>{{ $t('zi-jian-shu-ju-ku-tian-jia-ji-qi-de-bu-zhou') }}</span>
        {{ $t('mu-qian-jin-zhi-chi-redhat-centos-ubuntu-macos-xi-tong') }}
      </div>
      <div class="content">
        <section>
          <div class="num">1</div>
          <div class="info">
            <cc-iconfont :size="120" name="bar-code" />
          </div>
          <a-button type="primary" @click="_handleAddMachine">
            {{ $t('sheng-cheng-ji-qi-wei-yi-biao-shi') }}
          </a-button>
        </section>
        <section>
          <div class="num">2</div>
          <img alt="" class="info" src="../../../../assets/example.png" />
          {{ $t('jiang-xia-zai-de-ke-hu-duan-ya-suo-bao-dao-qian-yi-ji-qi-bing-jie-ya-zhi-fei-yong-hu-zhu-mu-lu') }}
        </section>
        <section>
          <div class="num">3</div>
          <div class="info">
            <div>{{ $t('jiang-huo-qu-de-he-xin-pei-zhi-wen-jian-fu-zhi-dao') }}</div>
            <div>{{ $t('clouddmglobalconfconfproperties-zhong-ti-huan') }}</div>
            <div>{{ $t('shi-yong-fei-root-yong-hu-qi-dong-ke-hu-duan') }}</div>
            <div>{{ $t('ke-hu-duan-jie-ya-mu-lu-clouddmsidecarbinstartsidecarsh') }}</div>
          </div>
        </section>
      </div>
    </div>
    <div class="footer">
      <a-button
        v-if="cluster.cloudOrIdcName === CLUSTER_ENV.ALIBABA_CLOUD_HOSTED"
        style="width: 120px; margin-right: 16px"
        type="primary"
        @click="handleAddEcs"
      >
        {{ $t('tian-jia') }}
      </a-button>
      <Button style="width: 120px" @click="handleCancelAddWorker">
        {{ $t('guan-bi') }}
      </Button>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { CLUSTER_ENV } from '@/const';
import { Modal } from 'ant-design-vue';

export default {
  name: 'AddMachineModal',
  props: {
    handleCancelAddWorker: Function,
    handleAddWorker: Function,
    clusterId: Number
  },
  computed: {
    isSelfCluster() {
      return this.cluster.cloudOrIdcName === CLUSTER_ENV.SELF_MAINTENANCE;
    },
    ...mapState({
      deployEnvListMap: (state) => state.deployEnvListMap,
      regionList: (state) => state.regionList
    }),
    rowSelection() {
      return {
        selectedRowKeys: this.selectedWorkerKeys,
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedWorkers = selectedRows;
          this.selectedWorkerKeys = selectedRowKeys;
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: !record.supported
          }
        })
      };
    },
    unSelectedWorkerColumns() {
      const { query } = this.unSelectedWorkerFilter;
      const columns = [
        {
          title: this.$t('shi-li-id-ming-cheng'),
          filteredValue: [query] || null,
          key: 'name',
          onFilter: (value, record) => record.instanceId?.includes(value) || record.instanceName?.includes(value),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: 'IP',
          scopedSlots: { customRender: 'ip' }
        },
        {
          title: this.$t('cao-zuo-xi-tong'),
          key: 'osname',
          dataIndex: 'osname'
        },
        {
          title: 'CPU',
          key: 'cpuCount',
          dataIndex: 'cpuCount'
        },
        {
          title: this.$t('nei-cun'),
          key: 'memoryMb',
          dataIndex: 'memoryMb'
        },
        {
          title: this.$t('guo-qi-shi-jian'),
          scopedSlots: { customRender: 'expiredTime' }
        }
      ];
      return columns;
    }
  },
  data() {
    return {
      query: '',
      CLUSTER_ENV,
      isDefaultCluster: false,
      cluster: {
        cloudOrIdcName: CLUSTER_ENV.SELF_MAINTENANCE,
        region: ''
      },
      unSelectedWorkers: [{}],
      unSelectedWorkerFilter: {
        query: ''
      },
      selectedWorkerColumns: [
        {
          title: this.$t('shi-li-id-ming-cheng'),
          scopedSlots: { customRender: 'name' }
        },
        {
          title: this.$t('cao-zuo'),
          scopedSlots: { customRender: 'actions' }
        }
      ],
      selectedWorkers: [{}],
      selectedWorkerKeys: [],
      ecsList: []
    };
  },
  methods: {
    handleQuery() {
      this.unSelectedWorkerFilter = { query: this.query };
    },
    async getCluster() {
      const res = await this.$services.dmClusterQueryById({ data: { clusterId: this.clusterId } });
      if (res.success) {
        this.cluster = res.data;
        if (this.cluster.cloudOrIdcName === null) {
          this.isDefaultCluster = true;
          this.cluster.cloudOrIdcName = CLUSTER_ENV.SELF_MAINTENANCE;
        }
        if (!this.cluster.region) {
          this.cluster.region = 'customer';
        }
      }
    },
    async getEcsList() {
      if (!this.cluster.region) {
        Modal.error({
          content: this.$t('qing-xuan-ze-qu-yu')
        });
        return;
      }
      const res = await this.$services.dmAliyunEcsListEcs({
        data: {
          clusterId: this.clusterId,
          region: this.cluster.region
        }
      });

      if (res.success) {
        // console.log(res.data);
        this.unSelectedWorkers = res.data;
      }
    },
    async _handleAddMachine() {
      const { region, cloudOrIdcName } = this.cluster;
      if (region && cloudOrIdcName) {
        const data = {
          region,
          cloudOrIdcName
        };
        await this.handleAddWorker(data);
      } else {
        this.$Message.error(this.$t('qing-xuan-ze-qu-yu'));
      }
    },
    handleDeleteMachine(instanceKey) {
      this.selectedWorkerKeys = this.selectedWorkerKeys.filter((key) => key !== instanceKey);
      this.selectedWorkers = this.selectedWorkers.filter((worker) => worker.instanceId !== instanceKey);
    },
    async handleAddEcs() {
      const data = {
        clusterId: this.clusterId,
        ecsInstanceIds: this.selectedWorkerKeys,
        region: this.cluster.region
      };

      const res = await this.$services.dmAliyunEcsEcsAddAndInstall({
        data,
        msg: this.$t('tian-jia-ji-qi-cheng-gong')
      });
      if (res.success) {
        this.handleCancelAddWorker();
      }
    }
  },
  created() {
    this.getCluster();
  }
};
</script>

<style lang="less" scoped>
.add-machine {
  .header {
    background: @gray;
    height: 92px;
    display: flex;
    padding: 10px 20px;
    border: 1px solid #dadada;

    .cycle {
      .circle(24px, #FFC15C);
      .flex-center();
      box-shadow: 0 3px 12px #ffc05c;
    }

    .choose {
      margin-left: 15px;

      .title {
        color: @font-color !important;
        line-height: 24px;
        font-weight: bold;
      }

      .content {
        margin-top: 10px;
        .flex-center();
      }
    }
  }

  .body {
    margin-top: 20px;
    //min-height: 490px;
    //max-height: 490px;
    display: flex;
    justify-content: space-between;

    .title {
      font-weight: bold;
      line-height: 32px;
    }

    .table {
      flex: 1;
      display: flex;
      flex-direction: column;
      margin-top: 6px;

      .pagination {
        .flex-center();
        height: 48px;
        border: 1px solid #eaeaea;
        border-top: none;
      }
    }

    .un-select {
      width: 850px;
      margin-right: 10px;
      display: flex;
      flex-direction: column;

      .query {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .condition {
          display: flex;
          align-items: center;
        }
      }
    }

    .selected {
      flex: 1;
      display: flex;
      flex-direction: column;
      height: 490px;

      .table {
        min-width: 340px;

        .pagination {
          border: none;
        }
      }
    }
  }

  .self {
    height: 392px;
    border: 1px solid #dadada;
    border-top: none;
    margin-top: 0;
    display: flex;
    flex-direction: column;

    .title {
      margin-left: 54px;
      margin-top: 12px;
    }

    .content {
      margin-top: 23px;
      flex: 1;
      display: flex;
      justify-content: space-around;

      section {
        display: flex;
        flex-direction: column;
        align-items: center;

        .info {
          width: 277px;
          height: 183px;
          background: #deefff;
          margin-bottom: 16px;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        &:first-child {
          .info {
            width: 200px;
          }
        }

        &:last-child {
          .info {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            width: 320px;

            div:nth-child(odd) {
              font-weight: bold;
              margin-bottom: 10px;
              margin-top: 14px;
            }
          }
        }

        .num {
          width: 42px;
          height: 42px;
          border-radius: 50%;
          background: #deefff;
          font-size: 24px;
          line-height: 42px;
          text-align: center;
          margin-bottom: 26px;
        }
      }
    }
  }

  .footer {
    text-align: center;
  }
}
</style>
