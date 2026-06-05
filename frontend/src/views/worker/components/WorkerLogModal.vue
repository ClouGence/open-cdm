<template>
  <CCModal v-model="showWorkerLogModal" :title="$t('ji-qi-ri-zhi')" footer-hide width="1200px" class-name="show-log-container">
    <Tabs type="card" v-model="logFileName" @on-click="handleSelectLog" :animated="false">
      <TabPane :label="log.fileName" :name="log.fileName" v-for="(log, index) in logData" :key="index">
        <div class="log-content">
          <Button class="refresh-btn" :loading="logLoading" @click="handleShowLog">
            <CustomIcon type="icon-v2-Refresh" v-if="!logLoading" />
          </Button>
          <p class="log-content-desc">
            <span>{{ $t('miao-shu-0') }}</span>
            <span class="point-content">{{ selectedLog.desc }}</span>
          </p>
          <p class="log-content-desc">
            <span>{{ $t('lu-jing-0') }}</span>
            <span class="point-content">{{ selectedLog.path }}</span>
          </p>
          <pre>{{ selectedLog.content }}</pre>
        </div>
      </TabPane>
    </Tabs>
    <template #footer>
      <Button style="margin-left: 5px" type="default" @click="handleCloseModal">
        {{ $t('guan-bi') }}
      </Button>
    </template>
  </CCModal>
</template>

<script>
export default {
  name: 'WorkerLogModal',
  props: {
    visible: Boolean,
    handleCloseModal: Function,
    worker: Object
  },
  data() {
    return {
      logFileName: '',
      logLoading: false,
      logData: [],
      selectedLog: {},
      showWorkerLogModal: false
    };
  },
  methods: {
    handleSelectLog(logName) {
      this.logData.map((item) => {
        if (item.fileName === logName) {
          this.selectedLog = item;
        }
        return null;
      });
    },
    handleShowLog() {
      this.logLoading = true;
      this.$services
        .ccLogViewTailWorkerLog({
          data: {
            workerId: this.worker.id,
            fetchedLogTypeList: ['WORKER_MAIN_LOG', 'WORKER_RSOCKET_ERROR_LOG', 'WORKER_RSOCKET_SLOW_LOG', 'WORKER_RSOCKET_LOG']
          }
        })
        .then((res) => {
          this.logLoading = false;
          if (res.success) {
            this.logData = res.data;
            if (this.selectedLog && this.selectedLog.fileName) {
              this.logData.map((item) => {
                if (item.fileName === this.selectedLog.fileName) {
                  this.selectedLog = item;
                }
                return null;
              });
            } else if (this.logData.length > 0) {
              this.selectedLog = this.logData[0];
              this.logFileName = this.selectedLog.fileName;
            }
          }
        });
    }
  },
  mounted() {
    this.handleShowLog();
  }
};
</script>

<style scoped>
.log-content {
  overflow-x: auto !important;
  overflow-y: auto;
  max-height: 500px;
}

.log-content pre {
  white-space: pre !important;
  overflow: visible !important;
  font-family: 'SF Mono', 'Monaco', 'Cascadia Code', 'Roboto Mono', Consolas, 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  color: #1e293b;
  background: transparent;
  border: none;
  padding: 20px;
  margin: 0;
}

.log-content::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.log-content::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 3px;
}

.log-content::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.log-content::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

.log-content-desc .point-content {
  max-width: 800px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
