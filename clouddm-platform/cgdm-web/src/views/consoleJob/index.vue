<template>
  <div class="console-job-wrapper" style="padding: 16px">
    <a-breadcrumb style="margin-bottom: 14px">
      <a-breadcrumb-item>{{ $t('xiao-xi-zhong-xin') }}</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="page-header-container border-radius-card">
      <a-form style="padding-right: 300px" layout="inline">
        <a-form-item>
          <a-select v-model="searchType" style="width: 160px" @change="handleChangeSearchType">
            <a-select-option value="type" :label="$t('ren-wu-lei-xing')">
              <span>{{ $t('ren-wu-lei-xing') }}</span>
            </a-select-option>
            <a-select-option value="status" :label="$t('zhuang-tai')">
              <span>{{ $t('ren-wu-zhuang-tai') }}</span>
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-if="searchType === 'type'">
          <a-select v-model="searchKey.label" style="width: 250px">
            <a-select-option value="">{{ $t('quan-bu') }}</a-select-option>
            <a-select-option v-for="label of consoleJobLabels" :value="label" :key="label">
              {{ CONSOLE_TASK_STATE[label] }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-if="searchType === 'status'">
          <a-select v-model="searchKey.consoleTaskState" style="width: 250px">
            <a-select-option value="">{{ $t('quan-bu') }}</a-select-option>
            <a-select-option v-for="state of consoleTaskStates" :value="state" :key="state">
              {{ state }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="getConsoleJobList">{{ $t('cha-xun') }}</a-button>
        </a-form-item>
      </a-form>
      <div class="page-header-function">
        <a-button type="default" style="margin-right: 6px" @click="getConsoleJobList" :loading="refreshLoading">
          <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
        </a-button>
      </div>
    </div>
    <a-table stripe size="small" border :columns="asyncColumn" :data-source="showData" :pagination="false">
      <template #label="{ row }">
        {{ CONSOLE_JOB_NAME[row.label] }}
      </template>
      <template #resourceId="{ row }">
        <a @click="getJob(row)" v-if="row.dataJobName">{{ row.dataJobName }}</a>
        <span v-if="row.workerName">{{ row.workerName }}</span>
        <span v-if="row.dsInstanceId">{{ row.dsInstanceId }}</span>
      </template>
      <template #resourceDesc="{ row }">
        <span v-if="row.dataJobDesc">{{ row.dataJobDesc }}</span>
        <span v-if="row.workerDesc">{{ row.workerDesc }}</span>
        <span v-if="row.datasourceDesc">{{ row.datasourceDesc }}</span>
      </template>
      <template #resourceType="{ row }">
        {{ RESOURCE_TYPE[row.resourceType] }}
      </template>
      <template #launchTime="{ row }">
        {{ formatTime(row.launchTime, 'YYYY-MM-DD HH:mm:ss') }}
      </template>
      <template #action="{ row }">
        <a @click="handleGoDetail(row)" style="margin-right: 16px">{{ $t('xiang-qing') }}</a>
        <a v-if="row.taskState != 'SUCCESS' && row.taskState != 'CANCELED'" @click="handleCancelJob(row)">
          {{ $t('qu-xiao') }}
        </a>
      </template>
      <template #taskState="{ row }">
        <div
          :style="`color:${row.taskState === 'FAILED' ? '#FF1815' : row.taskState === 'SUCCESS' ? '#52c41a' : row.taskState === 'CANCELED' ? '#555555' : ''}`"
        >
          <span
            class="status-point"
            :style="`background-color:${
              row.taskState === 'FAILED' ? '#FF1815' : row.taskState === 'SUCCESS' ? '#52C41A' : row.taskState === 'CANCELED' ? '#555555' : ''
            }`"
          ></span>
          {{ CONSOLE_TASK_STATE[row.taskState] }}
        </div>
      </template>
    </a-table>
    <div class="page-footer-container">
      <div class="page-footer-paging">
        <Button :disabled="page === 1" style="font-size: 16px; padding: 0 16px 0 10px" @click="handlePre">
          <Icon type="ios-arrow-back" style="font-size: 16px" />
          {{ $t('shang-yi-ye') }}
        </Button>
        <span style="margin: 0 10px">{{ $t('di-page-ye', [page]) }}</span>
        <Button :disabled="noMoreData" style="font-size: 16px; padding: 0 16px 0 10px; margin-left: 5px" @click="handleNext">
          <Icon type="ios-arrow-forward" style="font-size: 16px" />
          {{ $t('xia-yi-ye') }}
        </Button>
      </div>
    </div>
  </div>
</template>
<script>
import { CONSOLE_JOB_NAME, CONSOLE_TASK_STATE, RESOURCE_TYPE } from '@/const';
import { Modal } from 'ant-design-vue';
import dayjs from 'dayjs';

export default {
  name: 'ConsoleJob',
  mounted() {
    this.getConsoleJobList();
  },
  data() {
    return {
      searchType: 'type',
      firstId: 0,
      lastId: 0,
      prevFirst: [],
      page: 1,
      noMoreData: false,
      refreshLoading: false,
      CONSOLE_JOB_NAME,
      CONSOLE_TASK_STATE,
      RESOURCE_TYPE,
      searchKey: {
        consoleTaskState: '',
        label: '',
        pageData: {
          startId: 0,
          pageSize: 20
        }
      },
      size: 20,
      consoleTaskStates: [],
      consoleJobLabels: [],
      showData: [{}],
      asyncColumn: [
        {
          title: this.$t('ming-cheng'),
          key: 'label',
          dataIndex: 'label',
          slots: { title: 'label' },
          scopedSlots: { customRender: 'label' }
        },
        {
          title: this.$t('fa-qi-zhe'),
          dataIndex: 'launcher',
          key: 'launcher',
          width: 100
        },
        {
          title: this.$t('fa-qi-shi-jian'),
          key: 'launchTime',
          slots: { title: 'launchTime' },
          scopedSlots: { customRender: 'launchTime' },
          width: 280
        },
        {
          title: this.$t('zhuang-tai'),
          key: 'taskState',
          dataIndex: 'taskState',
          width: 120,
          slots: { title: 'taskState' },
          scopedSlots: { customRender: 'taskState' }
        },
        {
          title: this.$t('lei-xing'),
          key: 'resourceType',
          dataIndex: 'resourceType',
          width: 120,
          slots: { title: 'resourceType' },
          scopedSlots: { customRender: 'resourceType' }
        },
        {
          title: this.$t('zi-yuan-id'),
          key: 'resourceId',
          width: 240,
          slots: { title: 'resourceId' },
          scopedSlots: { customRender: 'resourceId' }
        },
        {
          title: this.$t('zi-yuan-miao-shu'),
          key: 'resourceDesc',
          slots: { title: 'resourceDesc' },
          scopedSlots: { customRender: 'resourceDesc' }
        },
        {
          title: this.$t('cao-zuo'),
          key: 'action',
          slots: { title: 'action' },
          scopedSlots: { customRender: 'action' },
          width: 160
        }
      ],
      asyncData: []
    };
  },
  methods: {
    formatTime(time, formatStr) {
      if (!time) return '';
      return dayjs(time).format(formatStr);
    },
    handleChangeSearchType() {
      // 切换查询类型的时候，重置所有搜索的值
      this.searchKey = {
        consoleTaskState: '',
        label: '',
        pageData: {
          startId: 0,
          pageSize: 10
        }
      };
    },
    handleRefresh() {
      this.getConsoleJobList();
    },
    async cancelConsoleJob(row) {
      const data = {
        consoleJobId: row.id,
        consoleTaskId: ''
      };
      const res = await this.$services.dmConsoleJobCancel({ data });
      if (res.success) {
        this.getConsoleJobList();
      }
    },
    handleGoDetail(row) {
      this.$router.push({ path: `/system/console_job/${row.id}` });
    },
    async getConsoleJobList(type) {
      const data = {
        consoleTaskState: this.searchKey.consoleTaskState,
        label: this.searchKey.label,
        startId: this.searchKey.pageData.startId,
        pageSize: this.searchKey.pageData.pageSize
      };
      const res = await this.$services.dmConsoleJobList({ data });
      if (res.success) {
        this.asyncData = res.data;
        this.showData = this.asyncData;
        if (type === 'next') {
          if (!this.prevFirst[this.page - 1]) {
            this.prevFirst.push(this.firstId);
          }
        }
        if (this.showData && this.showData.length > 0) {
          this.firstId = this.showData[0].id;
          this.lastId = this.showData[this.showData.length - 1].id;
        }
        if (res.data.length < this.searchKey.pageData.pageSize) {
          this.noMoreData = true;
        } else {
          this.noMoreData = false;
        }
      }
      this.refreshLoading = false;
    },
    // listConsoleTaskStates() {
    //   listConsoleTaskStates().then((res) => {
    //     if (res.data.code === '1') {
    //       this.consoleTaskStates = res.data.data;
    //     }
    //   });
    // },
    // listConsoleJobLabels() {
    //   listConsoleJobLabels().then((res) => {
    //     if (res.data.code === '1') {
    //       this.consoleJobLabels = res.data.data;
    //     }
    //   });
    // },
    handlePre() {
      this.page--;
      let startId = this.prevFirst[this.page - 1] + 1;

      if (startId < 0) {
        startId = 0;
      }
      this.searchKey.pageData.startId = startId;
      this.getConsoleJobList('prev');
    },
    handleNext() {
      this.searchKey.pageData.startId = this.lastId;
      this.getConsoleJobList('next');
      this.page++;
    },
    handlePageChange(page) {
      this.page = page;
      this.showData = this.asyncData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handlePageSizeChange(size) {
      this.size = size;
      this.showData = this.asyncData.slice((this.page - 1) * this.size, this.page * this.size);
    },
    handleCancelJob(row) {
      Modal.confirm({
        title: this.$t('qu-xiao-yi-bu-ren-wu-que-ren'),
        content: this.$t('qu-xiao-zhi-hou-zheng-ge-ren-wu-jiang-shi-bai-bing-jie-shu-que-ren-yao-qu-xiao-gai-yi-bu-ren-wu-ma'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: async () => {
          const data = {
            consoleJobId: row.id,
            consoleTaskId: ''
          };
          const res = await this.$services.dmConsoleJobCancel({ data });
          if (res.success) {
            this.$Message.success(this.$t('qu-xiao-yi-bu-ren-wu-cheng-gong'));
            this.getConsoleJobList();
          }
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
.console-job-wrapper {
  position: relative;

  .page-header-container {
    position: relative;

    .page-header-function {
      position: absolute;
      right: 0;
      top: 0;
    }
  }

  .page-footer-container {
    position: fixed;
    bottom: 12px;
    text-align: center;
    width: 100%;
  }
}
</style>
