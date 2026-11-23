<template>
  <div class="machine-list">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="content">
          <div class="option border-radius-card">
            <div class="left">
              <div class="machine">
                <Input
                  v-model="search"
                  class="text"
                  :placeholder="$t('qing-shu-ru-ji-qi-id-ming-cheng-cha-xun')"
                  style="width: 280px; margin-right: 10px"
                  clearable
                />
                <Button class="search-btn" type="primary" ghost @click="handleQuery">
                  {{ $t('cha-xun') }}
                </Button>
              </div>
            </div>
            <div class="right">
              <Button v-if="myAuth.includes('DM_WORKER_MANAGE')" icon="md-add" style="margin-right: 10px" type="primary" @click="handleClickAddBtn">
                {{ $t('tian-jia-ji-qi') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table :columns="workerListColumns" :data="showWorkerList" size="small" border>
              <template #name="{ row }">
                {{ row.workerName }}
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
    <CCModal v-model="showConfigModal" :mask-closable="false" footerHide width="min(1080px, calc(100vw - 96px))" :title="$t('pei-zhi-wen-jian')">
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
import appLogger from '@/utils/logger';
import { cloneDeep } from '@/utils/lodash';
import AddMachineModal from '@/views/system/cluster/components/AddMachineModal';
// import { Modal } from 'view-ui-plus';
import { Modal } from 'ant-design-vue';
import { CLUSTER_ENV, DEPLOY_STATUS, HEALTH_LEVEL_COLOR, WORKER_STATE } from '@/const';
import { EVENT_BUS_NAME_LIST } from '@/utils/eventBusName';
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
      showEditWorkerNameModal: false
    };
  },
  computed: {
    ...mapState(['userInfo', 'myAuth'])
  },
  methods: {
    hideConfModal() {
      this.showConfModal = false;
    },
    async queryClusterById() {
      const res = await this.$services.dmClusterQueryById({ data: { clusterId: this.clusterId } });
      if (res.success && res.data) {
        try {
          localStorage.setItem(`dmcluster-${this.clusterId}`, JSON.stringify(res.data));
        } catch (e) {
          // ignore storage errors
        }
        this.$bus.emit(EVENT_BUS_NAME_LIST.SET_CLUSTER_BREADCRUMB_NAME, {
          clusterId: this.clusterId,
          clusterName: res.data.clusterName || ''
        });
      }
    },
    handleClickAddBtn() {
      this.showAddWorkerModal = true;
      appLogger.debug('showAddWorkerModal', this.showAddWorkerModal);
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
    }
  },
  created() {
    this.clusterId = this.$route.params.clusterId;
    this.queryClusterById();
    this.getWorkerList();
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
    border: 1px solid var(--border-secondary);
    background: var(--bg-secondary);
    color: var(--text-primary);
    padding: 10px;
    overflow-x: auto;
    font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, 'Liberation Mono', monospace;
    white-space: nowrap;
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
</style>
