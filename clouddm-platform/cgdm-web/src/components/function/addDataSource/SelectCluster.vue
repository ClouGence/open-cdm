<template>
  <div>
    <Poptip transfer placement="bottom" width="700">
      <div @click="handleShowClusterList" style="width: 250px; height: 20px" class="show-cluster-btn">
        <span class="show-cluster-name" style="position: absolute; left: 0">
          {{ $t('yi-xuan-ze-showselectclusterrow-tai-ji-qi', [showSelectCluster(row)]) }}
        </span>
        <Icon type="ios-arrow-down"></Icon>
      </div>
      <template #content>
        <p class="white-list-net">
          <span style="margin-right: 10px; display: inline-block; line-height: 36px; vertical-align: middle">
            <span style="color: #ff1815">*</span>
            {{ $t('bai-ming-dan-lei-xing') }}
          </span>
          <RadioGroup v-model="row.whiteListAddType" @on-change="handleChangeWhiteType($event, row)">
            <Radio label="ADD_ALL" :disabled="!row.publicHost">{{ $t('quan-bu') }}</Radio>
            <Radio label="PRIVATE_IP_ONLY">{{ $t('nei-wang-ip') }}</Radio>
            <Radio label="PUBLIC_IP_ONLY" :disabled="!row.publicHost">
              {{ $t('gong-wang-chu-kou-ip') }}
            </Radio>
          </RadioGroup>
        </p>
        <div style="overflow: auto; margin-top: 6px">
          <Table max-height="300" border ref="selection" :columns="clusterColumns" :data="workerClusterList" @on-selection-change="handleSelect">
            <template #clusterName="{ row }">
              <div>
                <p>{{ row.clusterName }}</p>
                <p v-if="row.workerCount === 0" style="color: #ff6e0d; margin-top: 2px">
                  {{ $t('qing-zai-ji-qi-guan-li-zhong-tian-jia-ji-qi') }}
                </p>
              </div>
            </template>
          </Table>
        </div>
        <p v-if="workerClusterList.length === 0">{{ $t('zan-wu-ji-qun') }}</p>
      </template>
    </Poptip>
  </div>
</template>
<script>
import store from '@/store/index';

export default {
  props: {
    row: Object,
    handleChangeType: Function,
    handleChangeCluster: Function,
    workerClusterList: Array
  },
  data() {
    return {
      store,
      showClusterList: false,
      clusterList: [],
      whiteListAddType: '',
      clusterColumns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          type: 'expand',
          width: 50,
          render: (h, params) => {
            if (params.row.workersNet.length > 0) {
              return h(
                'div',
                params.row.workersNet.map((item) => {
                  if (item.privateIp) {
                    return h(
                      'p',
                      {
                        style: {
                          lineHeight: '30px'
                        }
                      },
                      [
                        h('i', {
                          class: 'iconfont iconmachine',
                          style: {
                            marginRight: '6px'
                          }
                        }),
                        h(
                          'span',
                          {},
                          this.whiteListAddType === 'PRIVATE_IP_ONLY'
                            ? item.privateIp
                            : this.whiteListAddType === 'PUBLIC_IP_ONLY'
                              ? item.publicIp
                              : `${item.publicIp},${item.privateIp}`
                        )
                      ]
                    );
                  }
                  return null;
                })
              );
            }
          }
        },
        {
          title: this.$t('ji-qun-ming-cheng'),
          key: 'clusterName',
          width: 220,
          slot: 'clusterName'
        },
        {
          title: this.$t('bei-zhu'),
          key: 'clusterDesc'
        },
        {
          title: this.$t('ji-qi-shu-liang'),
          key: 'workerCount',
          width: 100,
          render: (h, params) => h('div', {}, params.row.workersNet.length)
        }
      ]
    };
  },
  created() {
    // console.log('workerClusterList', this.workerClusterList);
    // if (this.workerClusterList) {
    //   this.clusterList = this.workerClusterList;
    // } else {
    //   this.clusterList = store.state.clusterList;
    // }
    // if (store.state.firstAddDataSource) {
    //   if (this.productClusterList.length === 0) {
    //     if (this.includesDM) {
    //       console.log('no worker list');
    //       this.getAllClusterAndWorkers();
    //     } else if (this.includesCC) {
    //       this.getAllClusterAndWorkers();
    //     }
    //   } else {
    //     this.getAllClusterAndWorkers();
    //   }
    // } else {
    //   Object.keys(store.state.selectedCluster)
    //     .map((item) => {
    //       store.state.selectedCluster[item].map((selectedCluster) => {
    //         this.clusterList.map((cluster) => {
    //           if (selectedCluster.id === cluster.id) {
    //             cluster._checked = true;
    //             cluster.selected = true;
    //           }
    //           return null;
    //         });
    //         return null;
    //       });
    //       return null;
    //     });
    // }
  },
  mounted() {
    this.whiteListAddType = this.row.whiteListAddType;
  },
  methods: {
    getAllClusterAndWorkers() {
      this.$services.ccClusterListWithWorkerNets().then((res) => {
        if (res.success) {
          this.clusterList = res.data;
          this.clusterList.map((item) => {
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
    handleShowClusterList() {
      this.showClusterList = !this.showClusterList;
      this.clusterList.push('');
      this.clusterList.pop();
    },
    handleSelectCluster(cluster, row, data) {
      let alreadyHas = false;
      let currentIndex = -1;

      if (!store.state.selectedCluster[row.instanceId]) {
        store.state.selectedCluster[row.instanceId] = [];
      }
      store.state.selectedCluster[row.instanceId].map((item, index) => {
        if (item.clusterName === cluster.clusterName) {
          alreadyHas = true;
          currentIndex = index;
        }
        return null;
      });
      if (data) {
        if (!alreadyHas) {
          store.state.selectedCluster[row.instanceId].push(cluster);
        }
      } else if (alreadyHas) {
        store.state.selectedCluster[row.instanceId].splice(currentIndex, 1);
      }
      store.state.selectedCluster = { ...store.state.selectedCluster };
      if (this.handleChangeCluster) {
        this.handleChangeCluster(row, store.state.selectedCluster);
      }
    },
    showSelectCluster(row) {
      let count = 0;

      if (store.state.selectedCluster[row.instanceId]) {
        store.state.selectedCluster[row.instanceId].map((item) => {
          count += item.workersNet.length;
          return null;
        });
      }
      return count;
    },
    handleChangeWhiteType(data, row) {
      this.whiteListAddType = data;
      this.handleChangeType(data, row);
    },
    handleSelect(selection) {
      if (!store.state.selectedCluster[this.row.instanceId]) {
        store.state.selectedCluster[this.row.instanceId] = [];
      }
      store.state.selectedCluster[this.row.instanceId] = selection;
      store.state.selectedCluster = { ...store.state.selectedCluster };
      if (this.handleChangeCluster) {
        this.handleChangeCluster(this.row, store.state.selectedCluster);
      }
    },
    updateSelectedCluster(list) {
      store.state.selectedCluster = list;
    }
  },
  watch: {
    'row.whiteListAddType': {
      handler(val) {
        this.whiteListAddType = val;
      },
      deep: true
    },
    workerClusterList(val) {
      console.log('val', val);
    }
  }
};
</script>
<style lang="less">
.show-cluster-btn {
  display: inline-block;
  width: 100%;
  text-align: right;
  cursor: pointer;
  /*position: relative;*/
  /*.ivu-icon{*/
  /*    position: absolute;*/
  /*    right: 0;*/
  /*    top: -10px;*/
  /*}*/

  .show-cluster-name {
    width: 90%;
    display: inline-block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    vertical-align: middle;
    text-align: left;
  }

  .ivu-select-dropdown {
    padding-top: 0 !important;
  }
}

.white-list-cluster-item {
  padding: 0 16px;

  .ivu-tree {
    display: inline-block;
    vertical-align: top;
  }

  .ivu-checkbox-wrapper {
    vertical-align: top;
    margin-top: 10px;
  }
}

.white-list-net {
  border-bottom: 1px solid #dadada;
  padding: 0 16px;
  height: 40px;

  .ivu-radio-group {
    line-height: 36px;
  }
}
</style>
