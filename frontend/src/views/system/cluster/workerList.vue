<template>
  <div class="machine-list">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem to="/system/dmmachine">{{ $t('cha-xun-ji-qi') }}</BreadcrumbItem>
            <BreadcrumbItem>{{ $t('ji-qi-lie-biao') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <div class="machine">
                <div class="circle">
                  <cc-iconfont name="machine" size="16" />
                </div>
                <div class="content">
                  <div class="first">
                    <div>{{ cluster.clusterDesc }}</div>
                    <div>{{ cluster.clusterName }}</div>
                  </div>
                  <div class="second">
                    <div>
                      {{ CLUSTER_TYPE[cluster.cloudOrIdcName] && CLUSTER_TYPE[cluster.cloudOrIdcName].name }}
                    </div>
                    <div>{{ getRegionName }}</div>
                  </div>
                </div>
                <Input
                  v-model="search"
                  class="text"
                  :placeholder="$t('qing-shu-ru-ji-qi-id-ming-cheng-cha-xun')"
                  style="width: 280px; margin-right: 10px"
                  clearable
                />
                <Button class="search-btn" type="primary" @click="handleQuery">
                  {{ $t('cha-xun') }}
                </Button>
              </div>
            </div>
            <div class="right">
              <Button
                v-if="myAuth.includes('DM_WORKER_MANAGE')"
                ghost
                icon="md-add"
                style="margin-right: 10px"
                type="primary"
                @click="handleClickAddBtn"
              >
                {{ $t('tian-jia-ji-qi') }}
              </Button>
              <Button class="refresh" @click="getWorkerList">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="workerListColumns" :data="showWorkerList" size="small" border>
              <template #name="{ row }">
                {{ row.workerName }}
                <Poptip placement="right" transfer>
                  <cc-iconfont :size="12" name="detail" />
                  <template #content>
                    <div class="worker-detail">
                      <section>
                        <div class="title">{{ $t('chu-kou-ip') }}:</div>
                        <div>{{ row.publicIp }}</div>
                      </section>
                      <section>
                        <div class="title">{{ $t('di-qu') }}:</div>
                        <div>{{ $t('bu-xian') }}</div>
                      </section>
                      <section>
                        <div class="title">{{ $t('chuang-jian-shi-jian') }}:</div>
                        <div>{{ $filters.formatTime(row.gmtCreate, 'YYYY-MM-DD HH:mm:ss') }}</div>
                      </section>
                      <section>
                        <div class="title">{{ $t('ji-qi-lei-xing') }}:</div>
                        <div>{{ $t('xu-ni-ji') }}</div>
                      </section>
                      <section>
                        <div class="title">{{ $t('ji-qi-wei-yi-biao-shi-fu') }}:</div>
                        <div style="margin-right: 5px">{{ row.workerSeqNumber }}</div>
                        <cc-iconfont
                          :size="12"
                          name="copy"
                          @click="copyText(`${row.workerSeqNumber}`, $t('fu-zhi-ji-qi-wei-yi-biao-shi-fu-cheng-gong'))"
                        />
                      </section>
                    </div>
                  </template>
                </Poptip>
                <cc-iconfont
                  v-if="row.consoleTaskState === 'FAILED'"
                  @click="handleConsoleJob(row)"
                  name="job-error"
                  color="#FF6E0D"
                  size="16"
                  style="margin-left: 6px"
                />
              </template>
              <template #desc="{ row }">
                {{ row.workerDesc }}
                <cc-iconfont :size="8" name="edit" style="margin-right: 4px" @click="handleClickEditWorkerNameBtn(row)" />
              </template>
              <template #ip="{ row }">
                {{ row.privateIp }}
              </template>
              <template #status="{ row }">
                <div v-if="row.deployStatus !== 'INSTALLING' && row.deployStatus !== 'UNINSTALLING'" style="display: flex">
                  <cc-status :type="HEALTH_LEVEL_COLOR[row.healthLevel]" />
                  {{ WORKER_STATE[row.workerState].name }}
                </div>
                <div v-else-if="row.deployStatus === 'INSTALLING'">
                  <div v-if="row.consoleTaskState === 'FAILED'" style="display: flex">
                    <cc-status type="error" />
                    {{ $t('an-zhuang-shi-bai') }}
                  </div>
                  <div v-if="row.consoleTaskState !== 'FAILED'">
                    <a-icon type="loading" />
                    {{ $t('an-zhuang-zhong') }}
                  </div>
                </div>
                <div v-else-if="row.deployStatus === 'UNINSTALLING'">
                  <div v-if="row.consoleTaskState === 'FAILED'" style="display: flex">
                    <cc-status type="error" />
                    {{ $t('xie-zai-shi-bai') }}
                  </div>
                  <Icon v-if="row.consoleTaskState !== 'FAILED'" type="ios-loading" />
                  {{ $t('xie-zai-zhong') }}
                </div>
              </template>
              <template #actions="{ row }">
                <div class="work-list-table-actions">
                  <Button
                    :disabled="!canStart(row)"
                    size="small"
                    type="text"
                    v-if="myAuth.includes('DM_WORKER_MANAGE')"
                    @click="handleAction('start', row)"
                  >
                    {{ $t('qi-dong') }}
                  </Button>
                  <Button
                    :disabled="!canStop(row)"
                    size="small"
                    v-if="myAuth.includes('DM_WORKER_MANAGE')"
                    type="text"
                    @click="handleAction('stop', row)"
                  >
                    {{ $t('ting-zhi') }}
                  </Button>
                  <div v-if="row.cloudOrIdcName === CLUSTER_ENV.SELF_MAINTENANCE">
                    <Button size="small" type="text" @click="handleAction('conf', row)">
                      {{ $t('cha-kan-pei-zhi-wen-jian') }}
                    </Button>
                  </div>
                  <div v-else>
                    <Button
                      :disabled="!canInstall(row)"
                      v-if="myAuth.includes('DM_WORKER_MANAGE')"
                      size="small"
                      type="text"
                      @click="handleAction('install', row)"
                    >
                      {{ $t('an-zhuang') }}
                    </Button>
                    <Button
                      :disabled="!canUpdate(row)"
                      size="small"
                      v-if="myAuth.includes('DM_WORKER_MANAGE')"
                      type="text"
                      @click="handleAction('update', row)"
                    >
                      {{ $t('geng-xin') }}
                    </Button>
                    <Button
                      :disabled="!canUninstall(row)"
                      v-if="myAuth.includes('DM_WORKER_MANAGE')"
                      size="small"
                      type="text"
                      @click="handleAction('uninstall', row)"
                    >
                      {{ $t('xie-zai') }}
                    </Button>
                  </div>
                  <Poptip
                    :disabled="!canDelete(row)"
                    confirm
                    transfer
                    v-if="myAuth.includes('DM_WORKER_MANAGE')"
                    :title="$t('que-ding-shan-chu-gai-ji-qi-ma')"
                    @on-ok="handleAction('delete', row)"
                  >
                    <Button :disabled="!canDelete(row)" size="small" type="text">
                      {{ $t('shan-chu') }}
                    </Button>
                  </Poptip>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Page
          :total="total"
          show-total
          show-elevator
          @on-change="handlePageChange"
          show-sizer
          v-model="pageNum"
          :page-size="pageSize"
          @on-page-size-change="handlePageSizeChange"
        />
      </div>
    </div>
    <CCModal v-model="showAddWorkerModal" :mask-closable="false" footerHide :width="1260" :title="$t('tian-jia-ji-qi')">
      <add-machine-modal
        v-if="showAddWorkerModal"
        :clusterId="parseInt(clusterId, 10)"
        :handleAddWorker="handleAddWorker"
        :handleCancelAddWorker="handleCancelAddWorker"
      />
    </CCModal>
    <CCModal v-model="showDownloadModal" :mask-closable="false" :width="620" :title="$t('xia-zai-ke-hu-duan')">
      <div class="download-modal">
        <a-alert banner :message="$t('xia-zai-lian-jie-1-xiao-shi-nei-you-xiao-qing-jin-kuai-xia-zai')" style="margin-bottom: 10px" />
        <div class="url">
          {{ downloadUrl }}
        </div>
        <div class="btn-group">
          <a-button type="primary" @click="handleDownload">{{ $t('xia-zai') }}</a-button>
          <a-button type="primary" @click="copyText(downloadUrl, $t('fu-zhi-xia-zai-lian-jie-cheng-gong'))">
            {{ $t('fu-zhi-lian-jie') }}
          </a-button>
          <a-button @click="handleCancelDownload">{{ $t('qu-xiao') }}</a-button>
        </div>
      </div>
    </CCModal>
    <CCModal v-model="showConfigModal" :mask-closable="false" footerHide :width="630" :title="$t('pei-zhi-wen-jian')">
      <div v-if="showConfigModal" class="config-modal">
        <a-alert
          banner
          :message="$t('qing-jiang-xia-lie-pei-zhi-wan-zheng-fu-zhi-dao-clouddm-an-zhuang-mu-lu-xia-de-confglobalconfproperties-wen-jian-zhong')"
          style="margin-bottom: 10px"
        />
        <div class="config">
          <div>{{ workerConfig.userAkLabel }}={{ workerConfig.userAkValue }}</div>
          <div>{{ workerConfig.userSkLabel }}={{ workerConfig.userSkValue }}</div>
          <div>{{ workerConfig.wsnLabel }}={{ workerConfig.wsnValue }}</div>
          <div>{{ workerConfig.consoleHostLabel }}={{ workerConfig.consoleHostValue }}</div>
          <div>{{ workerConfig.consolePortLabel }}={{ workerConfig.consolePortValue }}</div>
        </div>
        <div class="btn-group">
          <a-button type="primary" @click="copyConfig">{{ $t('fu-zhi') }}</a-button>
          <a-button @click="showConfigModal = false">{{ $t('guan-bi') }}</a-button>
        </div>
      </div>
    </CCModal>
    <CCModal
      v-model="showEditWorkerNameModal"
      :mask-closable="false"
      :width="400"
      :cancelText="$t('qu-xiao')"
      :okText="$t('bao-cun')"
      :title="$t('xiu-gai-ming-cheng')"
      wrapClassName="have-footer"
      @ok="handleUpdateWorkerDesc"
    >
      <a-input v-model:value="selectedWorker.workerDesc" type="textarea" />
    </CCModal>
  </div>
</template>

<script lang="js">
import cloneDeep from 'lodash.clonedeep';
import AddMachineModal from '@/views/system/cluster/components/AddMachineModal';
// import { Modal } from 'view-ui-plus';
import { Modal } from 'ant-design-vue';
import { CLUSTER_ENV, CLUSTER_TYPE, DEPLOY_STATUS, HEALTH_LEVEL_COLOR, WORKER_STATE } from '@/const';
import { mapState } from 'vuex';
import copyMixin from '@/mixins/copyMixin';

export default {
  name: 'WorkerList',
  components: {
    AddMachineModal
  },
  mixins: [copyMixin],
  data() {
    return {
      CLUSTER_TYPE,
      cluster: {},
      DEPLOY_STATUS,
      WORKER_STATE,
      HEALTH_LEVEL_COLOR,
      CLUSTER_ENV,
      clusterId: '',
      downloadUrl: '',
      search: '',
      workerConfig: {
        consoleHostLabel: '',
        consoleHostValue: '',
        consolePortLabel: '',
        consolePortValue: '',
        userAkLabel: '',
        userAkValue: '',
        userSkLabel: '',
        userSkValue: '',
        wsnLabel: '',
        wsnValue: ''
      },
      showConfModal: false,
      showAddWorkerModal: false,
      showConfigModal: false,
      showDownloadModal: false,
      pageSize: 20,
      pageNum: 1,
      total: 0,
      workerListColumns: [
        {
          title: this.$t('ji-qi-id'),
          minWidth: 200,
          slot: 'name'
        },
        {
          title: this.$t('ji-qi-ming-cheng'),
          minWidth: 200,
          slot: 'desc'
        },
        {
          title: this.$t('ji-qi-ip'),
          width: 150,
          slot: 'ip'
        },
        {
          title: this.$t('zhuang-tai'),
          width: 100,
          slot: 'status'
        },
        {
          title: this.$t('fu-zai'),
          width: 100,
          key: 'workerLoad'
        },
        {
          title: this.$t('cpu-shi-yong-shuai'),
          width: 150,
          key: 'cpuUseRatio'
        },
        {
          title: this.$t('nei-cun-shi-yong-shuai'),
          width: 150,
          key: 'memUseRatio'
        },
        {
          title: this.$t('yi-shi-yong-hui-hua-shu'),
          width: 150,
          key: 'sessionPoolUse'
        },
        {
          title: this.$t('zui-da-hui-hua-shu'),
          width: 100,
          key: 'sessionPoolMax'
        },
        {
          title: this.$t('cao-zuo'),
          width: 300,
          key: 'action',
          fixed: 'right',
          slot: 'actions'
        }
      ],
      allWorkerList: [],
      workerList: [],
      showWorkerList: [],
      selectedWorker: {},
      showEditWorkerNameModal: false,
      regions: []
    };
  },
  computed: {
    ...mapState(['userInfo', 'regionList', 'myAuth']),
    getRegionName() {
      let regionName = '';
      this.regions.forEach((region) => {
        if (region.region === this.cluster.region) {
          regionName = region.i18nName;
        }
      });
      return regionName;
    }
  },
  methods: {
    hideConfModal() {
      this.showConfModal = false;
    },
    listDmRegions() {
      this.regions = [{ region: 'customer', i18nName: this.$t('bu-xian') }];
    },
    async queryClusterById() {
      const res = await this.$services.dmClusterQueryById({ data: { clusterId: this.clusterId } });
      if (res.success) {
        this.cluster = res.data;
      }
    },
    handleClickAddBtn() {
      this.showAddWorkerModal = true;
      console.log('showAddWorkerModal', this.showAddWorkerModal);
    },
    canStart(worker) {
      return (
        (worker.workerState === 'OFFLINE' || worker.workerState === 'WAIT_TO_OFFLINE') &&
        ((worker.deployStatus === 'INSTALLED' && worker.cloudOrIdcName !== 'SELF_MAINTENANCE') || worker.cloudOrIdcName === 'SELF_MAINTENANCE')
      );
    },
    canStop(worker) {
      return worker.workerState === 'ONLINE' || worker.workerState === 'WAIT_TO_ONLINE' || worker.workerState === 'ABNORMAL';
    },
    canInstall(worker) {
      return (
        (worker.workerState === 'OFFLINE' && (worker.deployStatus === 'UNINSTALLED' || worker.deployStatus === '')) ||
        (worker.workerState === 'WAIT_TO_OFFLINE' && worker.deployStatus === 'UNINSTALLED')
      );
    },
    canUpdate(worker) {
      return worker.deployStatus === 'INSTALLED';
    },
    canUninstall(worker) {
      return (worker.workerState === 'WAIT_TO_OFFLINE' || worker.workerState === 'OFFLINE') && worker.deployStatus === 'INSTALLED';
    },
    canDelete(worker) {
      return (
        ((worker.workerState === 'OFFLINE' || worker.workerState === 'WAIT_TO_OFFLINE') &&
          ((worker.deployStatus === 'UNINSTALLED' && worker.cloudOrIdcName !== 'SELF_MAINTENANCE') ||
            worker.cloudOrIdcName === 'SELF_MAINTENANCE')) ||
        (worker.cloudOrIdcName === 'SELF_MAINTENANCE' && !worker.privateIp) ||
        worker.healthLevel !== 'Health'
      );
    },
    handleClickEditWorkerNameBtn(worker) {
      this.selectedWorker = cloneDeep(worker);
      this.showEditWorkerNameModal = true;
    },
    async handleUpdateWorkerDesc() {
      const { id, workerDesc } = this.selectedWorker;
      const data = {
        desc: workerDesc,
        workerId: id
      };
      const res = await this.$services.dmWorkerUpdateWorkerDesc({
        data,
        msg: this.$t('xiu-gai-ji-qi-ming-cheng-cheng-gong')
      });

      if (res.success) {
        this.getWorkerList();
        this.showEditWorkerNameModal = false;
      }
    },
    copyConfig() {
      const {
        consoleHostLabel,
        consoleHostValue,
        consolePortLabel,
        consolePortValue,
        userAkLabel,
        userAkValue,
        userSkLabel,
        userSkValue,
        wsnLabel,
        wsnValue
      } = this.workerConfig;
      const value = `
      ${userAkLabel}=${userAkValue}\n
      ${userSkLabel}=${userSkValue}\n
      ${wsnLabel}=${wsnValue}\n
      ${consoleHostLabel}=${consoleHostValue}\n
      ${consolePortLabel}=${consolePortValue}
      `;
      this.copyText(value, this.$t('pei-zhi-wen-jian-fu-zhi-cheng-gong'));
    },
    handleQuery() {
      const workerList = this.allWorkerList.filter((worker) => worker.workerName.includes(this.search) || worker.workerDesc.includes(this.search));
      this.total = workerList.length;
      this.workerList = workerList;
      this.handlePageSizeChange(this.pageSize);
    },
    handleCancelDownload() {
      this.showDownloadModal = false;
    },
    async handleDownload() {
      window.open(this.downloadUrl);
    },
    async handleAddWorker(data = {}) {
      const res = await this.$services.dmWorkerCreateInitialWorker({
        data: {
          ...data,
          clusterId: this.clusterId,
          region: 'customer'
        },
        msg: this.$t('sheng-cheng-ji-qi-biao-shi-fu-cheng-gong')
      });

      if (res.success) {
        this.showAddWorkerModal = false;
        await this.getWorkerList();
      }
    },
    async getWorkerList() {
      const data = {
        clusterId: this.clusterId
      };
      const res = await this.$services.dmWorkerListWorkers({ data });
      if (res.success) {
        this.workerList = res.data;
        this.allWorkerList = res.data;
        this.total = res.data.length;
        this.setTableShowData();
      }
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.setTableShowData();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    setTableShowData() {
      const { pageNum, pageSize } = this;
      this.showWorkerList = this.workerList.slice((pageNum - 1) * pageSize, pageNum * pageSize);
    },
    handleCancelAddWorker() {
      this.showAddWorkerModal = false;
      this.getWorkerList();
    },
    async handleAction(action, worker) {
      let res;
      const { id: workerId } = worker;
      const data = {
        clusterId: this.clusterId
      };
      if (['install', 'uninstall', 'update'].includes(action)) {
        data.workerIds = [workerId];
      } else {
        data.workerId = workerId;
      }
      switch (action) {
        case 'install':
          res = await this.$services.dmAliyunEcsInstall({
            data,
            msg: this.$t('ji-qi-an-zhuang-cheng-gong')
          });
          if (res.success) {
            this.getWorkerList();
          }
          break;
        case 'uninstall':
          this.workerList.forEach((w) => {
            if (w.id === workerId) {
              w.deployStatus = 'UNINSTALLING';
            }
          });
          this.workerList = [...this.workerList];
          res = await this.$services.dmAliyunEcsUninstall({
            data,
            msg: this.$t('ji-qi-xie-zai-cheng-gong')
          });
          // if (res.success) {
          //   this.getWorkerList();
          // }
          break;
        case 'update':
          res = await this.$services.dmAliyunEcsUpgradeAll({
            data,
            msg: this.$t('ji-qi-geng-xin-cheng-gong')
          });
          if (res.success) {
            this.getWorkerList();
          }
          break;
        case 'start':
          res = await this.$services.dmWorkerWaitToOnline({
            data,
            msg: this.$t('ji-qi-qi-dong-cheng-gong')
          });
          if (res.success) {
            this.getWorkerList();
          }
          break;
        case 'stop':
          res = await this.$services.dmWorkerWaitToOffline({
            data,
            msg: this.$t('ji-qi-ting-zhi-cheng-gong')
          });

          if (res.success) {
            this.getWorkerList();
          }
          break;
        case 'download':
          res = await this.$services.dmWorkerDownloadClientUrl({ data });
          if (res.success) {
            this.downloadUrl = res.data;
            this.showDownloadModal = true;
          }
          break;
        case 'delete':
          res = await this.$services.dmWorkerDeleteWorker({ data });
          if (res.success) {
            this.getWorkerList();
          }
          break;
        case 'conf':
          this.selectedWorker = data;
          this.getWorkerConfig(data);
          break;
        default:
          return false;
      }
    },
    async getWorkerConfig(data = {}) {
      const res = await this.$services.dmWorkerClientCoreConfig({ data });
      if (res.success) {
        Modal.destroyAll();
        this.workerConfig = res.data;
        this.showConfigModal = true;
      }
    },
    handleConsoleJob(record) {
      this.$router.push({ path: `/system/console_job/${record.consoleJobId}` });
    }
  },
  created() {
    this.clusterId = this.$route.params.clusterId;
    this.queryClusterById();
    this.getWorkerList();
    this.listDmRegions();
  }
};
</script>

<style lang="less" scoped>
.machine-list {
  height: 100%;
  display: flex;
  flex-direction: column;

  .machine {
    display: flex;
    align-items: center;

    .circle {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background-color: rgba(255, 193, 92, 1);
      box-shadow: 0px 3px 12px 0px rgba(255, 192, 92, 1);
      text-align: center;
      line-height: 40px;
      margin-left: 20px;
    }

    .content {
      display: flex;
      flex-direction: column;
      margin: 0 10px;

      & > div {
        display: flex;
      }

      .first {
        display: flex;
        align-items: center;
        margin-bottom: 6px;

        & > div:first-child {
          font-size: 14px;
          font-weight: bold;
          margin-right: 10px;
        }
      }

      .second {
        & > div {
          border-radius: 14px;
          padding: 0 5px;
          background-color: rgba(224, 224, 224, 1);

          &:first-child {
            margin-right: 8px;
          }
        }
      }
    }
  }
}

.worker-detail {
  display: flex;
  flex-direction: column;
  padding: 10px 0;

  section {
    margin-left: 10px;
    width: 250px;
    display: flex;
    margin-bottom: 5px;

    .title {
      font-weight: bold;
    }
  }
}

.download-modal {
  .url {
    padding: 10px;
    border: 1px solid #ededed;
    background: #fafafa;
    word-wrap: break-word;
    margin-bottom: 20px;
  }

  .btn-group {
    display: flex;
    justify-content: center;

    button + button {
      margin-left: 10px;
    }
  }
}

.sms-modal {
  .code {
    display: flex;
    margin-top: 10px;
  }

  .btn-group {
    margin-top: 20px;
    display: flex;
    justify-content: center;

    button + button {
      margin-left: 10px;
    }
  }
}

.config-modal {
  .config {
    border: 1px solid #ededed;
    background: #fafafa;
    padding: 10px;
  }

  .btn-group {
    display: flex;
    justify-content: center;
    margin-top: 20px;

    button + button {
      margin-left: 10px;
    }
  }
}

.work-list-table-actions {
  display: flex;
}

.worker-detail {
  display: flex;
  flex-wrap: wrap;
  width: 600px;

  section {
    display: flex;
    height: 20px;
    line-height: 20px;

    .title {
      color: #888;
    }

    div:last-child {
      font-weight: bold;
    }

    &:not(:last-child) {
      width: 50%;
    }
  }
}
</style>
