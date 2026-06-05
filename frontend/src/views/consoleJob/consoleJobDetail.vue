<template>
  <div class="console-job-detail-wrapper">
    <a-breadcrumb style="margin-bottom: 14px">
      <a-breadcrumb-item>
        <a href="/#/system/info_center">{{ $t('xiao-xi-zhong-xin') }}</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>{{ $t('xiang-qing') }}</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="console-job-detail-container">
      <div class="console-job-detail-title">
        <span class="console-job-detail-label">{{ $t('yi-bu-ren-wu-ming-cheng') }}:</span>
        <span class="console-job-detail-value">{{ CONSOLE_JOB_NAME[consoleJobInfo.label] }}</span>
        <span class="console-job-detail-label">{{ $t('lei-xing') }}:</span>
        <span class="console-job-detail-value">
          {{ RESOURCE_TYPE[consoleJobInfo.resourceType] }}
        </span>
        <span class="console-job-detail-label">{{ $t('zi-yuan-id') }}:</span>
        <span class="console-job-detail-value">
          <span v-if="consoleJobInfo.dataJobName">
            <a @click="getJob(consoleJobInfo)">{{ consoleJobInfo.dataJobName }}</a>
          </span>
          <span v-if="consoleJobInfo.workerName">{{ consoleJobInfo.workerName }}</span>
          <span v-if="consoleJobInfo.dsInstanceId">{{ consoleJobInfo.dsInstanceId }}</span>
        </span>
        <span class="console-job-detail-label">{{ $t('zi-yuan-miao-shu') }}:</span>
        <span class="console-job-detail-value">
          <span v-if="consoleJobInfo.dataJobDesc">{{ consoleJobInfo.dataJobDesc }}</span>
          <span v-if="consoleJobInfo.workerDesc">{{ consoleJobInfo.workerDesc }}</span>
          <span v-if="consoleJobInfo.datasourceDesc">{{ consoleJobInfo.datasourceDesc }}</span>
        </span>
        <a-button
          v-if="consoleJobInfo.taskState !== 'SUCCESS' && consoleJobInfo.taskState !== 'CANCELED'"
          class="cancel-btn"
          @click="handleCancelJob"
        >
          {{ $t('qu-xiao') }}
        </a-button>
        <a-button class="refresh-btn" @click="getConsoleJobInfo" :loading="loading">
          <CustomIcon type="icon-v2-Refresh" v-if="!loading" />
        </a-button>
      </div>
      <a-table size="small" :columns="consoleTaskColumns" :data-source="consoleTaskData" :pagination="false">
        <template v-slot:action="row">
          <span v-if="row.taskState === 'FAILED'" class="console-task-action">
            <a style="margin-right: 16px" @click="handleRetryTask(row)">{{ $t('zhong-shi') }}</a>
            <a @click="handleSkipTask(row)">{{ $t('hu-lve-bing-ji-xu') }}</a>
          </span>
        </template>
        <template v-slot:executeTime="row">
          {{ row.executeTime ? formatTime(row.executeTime, 'YYYY-MM-DD HH:mm:ss') : '' }}
        </template>
        <template v-slot:taskState="row">
          <span
            class="console-job-detail-value"
            :style="`color:${row.taskState === 'SUCCESS' ? '#52C41A' : row.taskState === 'FAILED' ? '#FF1815' : '#FFA30E'}`"
          >
            {{ CONSOLE_TASK_STATE[row.taskState] }}
          </span>
        </template>
      </a-table>
    </div>
  </div>
</template>
<script>
import { CONSOLE_JOB_NAME, CONSOLE_TASK_STATE, RESOURCE_TYPE } from '@/const';
import { Modal } from 'ant-design-vue';
import { formatTime } from '../../utils/index';

export default {
  name: 'ConsoleJobDetail',
  mounted() {
    this.consoleJobId = this.$route.params.id;
    this.getConsoleJobInfo();
  },
  data() {
    return {
      consoleJobId: 0,
      consoleJobInfo: {},
      loading: false,
      CONSOLE_TASK_STATE,
      CONSOLE_JOB_NAME,
      RESOURCE_TYPE,
      formatTime,
      consoleTaskColumns: [
        {
          title: 'ID',
          key: 'id',
          dataIndex: 'id'
        },
        {
          title: this.$t('bu-zhou-ming-cheng'),
          key: 'stepName',
          dataIndex: 'stepName'
        },
        {
          title: this.$t('zhu-ji-di-zhi'),
          key: 'host',
          dataIndex: 'host'
        },
        {
          title: this.$t('zhuang-tai'),
          key: 'taskState',
          slots: { title: 'taskState' },
          scopedSlots: { customRender: 'taskState' }
        },
        {
          title: this.$t('zhi-hang-shi-jian'),
          key: 'executeTime',
          slots: { title: 'executeTime' },
          scopedSlots: { customRender: 'executeTime' }
        },
        {
          title: this.$t('cao-zuo'),
          key: 'action',
          slots: { title: 'action' },
          scopedSlots: { customRender: 'action' }
        }
      ],
      consoleTaskData: [{}]
    };
  },
  methods: {
    async handleRetryTask(task) {
      const data = {
        consoleJobId: this.consoleJobId,
        consoleTaskId: task.id
      };
      const res = await this.$services.dmConsoleJobRetryTask({ data });
      if (res.success) {
        this.getConsoleJobInfo();
      }
    },
    async handleSkipTask(task) {
      const data = {
        consoleJobId: this.consoleJobId,
        consoleTaskId: task.id
      };
      const res = await this.$services.dmConsoleJobSkipTask({ data });
      if (res.success) {
        this.getConsoleJobInfo();
      }
    },
    async getConsoleJobInfo() {
      const data = {
        consoleJobId: this.consoleJobId
      };
      const res = await this.$services.dmConsoleJobQueryConsoleJob({ data });
      if (res.success) {
        this.consoleJobInfo = res.data;
        this.consoleTaskData = this.consoleJobInfo.taskVOList;
      }
    },
    async handleCancelJob() {
      Modal.confirm({
        title: this.$t('qu-xiao-yi-bu-ren-wu-que-ren'),
        content: this.$t('qu-xiao-zhi-hou-zheng-ge-ren-wu-jiang-shi-bai-bing-jie-shu-que-ren-yao-qu-xiao-gai-yi-bu-ren-wu-ma'),
        okText: this.$t('que-ding'),
        cancelText: this.$t('qu-xiao'),
        onOk: async () => {
          const data = {
            consoleJobId: this.consoleJobInfo.id,
            consoleTaskId: ''
          };
          const res = await this.$services.dmConsoleJobCancel({ data });
          if (res.success) {
            this.$Message.success(this.$t('qu-xiao-yi-bu-ren-wu-cheng-gong'));
            this.getConsoleJobInfo();
          }
        }
      });
    }
  }
};
</script>
<style lang="less" scoped>
.console-job-detail-wrapper {
  //padding: 16px;
}

.console-job-detail-container {
  width: 100%;

  .console-job-detail-title {
    border: 1px solid #dadada;
    height: 50px;
    line-height: 50px;
    background-color: #deefff;
    padding-left: 15px;
    position: relative;

    .refresh-btn {
      position: absolute;
      right: 20px;
      top: 8px;
    }

    .cancel-btn {
      position: absolute;
      right: 80px;
      top: 8px;
    }
  }

  .console-job-detail-body {
    background-color: #ffffff;
    padding: 18px 16px 18px 30px;

    .console-job-detail-wrapper {
      position: relative;

      .step-line {
        width: 1px;
        height: 100%-18px;
        background-color: #dadada;
        position: absolute;
        left: 6px;
        top: 12px;
        z-index: 0;
      }
    }
  }

  .console-job-detail-label {
  }

  .console-job-detail-value {
    font-family: PingFangSC-Semibold;
    font-weight: 500;
    margin-right: 60px;
  }

  .console-job-step {
    margin-bottom: 20px;
    position: relative;

    .error-msgContent-container {
      background-color: #fafafa;
      border: 1px solid #ededed;
      margin: 10px 0 10px 22px;
      padding: 10px;
    }

    .console-task-action {
      position: absolute;
      right: 0;
      top: 0;
    }
  }

  .task-status-point {
    display: inline-block;
    width: 12px;
    height: 12px;
    border-radius: 50%;
    background-color: #999999;
    margin-right: 8px;
    z-index: 1;
    position: relative;
  }

  .task-status-success {
    background-color: #52c41a;
  }

  .task-status-error {
    background-color: #ff1815;
  }
}
</style>
