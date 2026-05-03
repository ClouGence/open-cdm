<template>
  <div>
    <div>
      <Row :gutter="16">
        <Col span="24">
          <div style="margin-top: 10px">
            <Row>
              <Col span="6">
                <span style="font-weight: 500; margin-right: 5px">
                  {{ $t('cpu-luo-ji-he-shu') }}
                </span>
                {{ row.logicalCoreNum }}
              </Col>
              <Col span="6">
                <span style="font-weight: 500; margin-right: 5px">
                  {{ $t('wu-li-ci-pan-rong-liang') }}
                </span>
                {{ row.physicDiskGb }} GB
              </Col>
              <Col span="6">
                <span style="font-weight: 500; margin-right: 5px">
                  {{ $t('wu-li-nei-cun-rong-liang') }}
                </span>
                {{ row.physicMemMb }} MB
              </Col>
            </Row>
            <p style="margin-top: 10px">
              <Col span="6">
                <span style="font-weight: 500; margin-right: 5px">
                  {{ $t('kong-xian-nei-cun') }}
                </span>
                {{ row.freeMemMb }} MB
              </Col>
              <Col span="6">
                <span style="font-weight: 500; margin-right: 5px">
                  {{ $t('kong-xian-ci-pan') }}
                </span>
                {{ row.freeDiskGb }} GB
              </Col>
            </p>
          </div>
        </Col>
      </Row>
      <div v-if="row.taskScheduleVOs">
        <Divider />
        <div style="margin-top: 20px">
          <p style="margin-bottom: 10px; font-size: 13px; font-weight: 500">
            {{ $t('yun-hang-zai-gai-jie-dian-shang-de-task-lie-biao') }}
          </p>
          <Table :row-class-name="rowClassName" size="small" border :columns="taskColumn" :data="taskData"></Table>
        </div>
      </div>
    </div>
    <Modal v-model="showDispatch" :title="$t('zhong-xin-tiao-du')" @on-ok="handleDispatch">
      <p style="margin-bottom: 10px">
        {{ $t('xu-yao-ba-selectedrowtaskname-cong-selectedrowworkerip-tiao-du-dao', [selectedRow.taskName, selectedRow.workerIp]) }}
      </p>
      <Select v-model="workerToDispatch" style="width: 200px">
        <Option :disabled="item.workerIp === selectedRow.workerIp" v-for="item in getWorkerList(selectedRow)" :value="item.id" :key="item.workerIp">
          {{ item.workerIp }}
        </Option>
      </Select>
    </Modal>
  </div>
</template>
<script>
import fecha from 'fecha';

export default {
  props: {
    row: Object,
    index: Number,
    workerList: Array,
    getWorkList: Function,
    taskId: Number
  },
  mounted() {
    this.taskData = this.row.taskScheduleVOs;
  },
  data() {
    return {
      showDispatch: false,
      selectedRow: {},
      workerToDispatch: '',
      taskColumn: [
        {
          title: this.$t('task-ming-cheng'),
          key: 'taskName'
        },
        {
          title: this.$t('ren-wu-id'),
          key: 'jobName',
          render: (h, params) => h('span', params.row.jobName)
        },
        {
          title: this.$t('ren-wu-miao-shu'),
          key: 'jobDesc'
        },
        {
          title: this.$t('ren-wu-jvm-fgc-shu'),
          key: 'fgcCount',
          sortable: true
        },
        {
          title: this.$t('ren-wu-cpu-shi-yong-shuai'),
          key: 'userCpuUsage',
          sortable: true
        },
        {
          title: this.$t('gui-ge-0'),
          key: 'jvmHeapMb',
          render: (h, params) => h('div', {}, `${params.row.jvmHeapMb / 1024}G`)
        },
        {
          title: this.$t('chuang-jian-shi-jian'),
          key: 'gmtCreate',
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.gmtCreate), 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('cao-zuo'),
          width: 120,
          render: (h, params) =>
            h('div', [
              h(
                'pd-button',
                {
                  props: {
                    type: 'primary',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.showDispatch = true;
                      this.selectedRow = params.row;
                    }
                  }
                },
                this.$t('zhong-xin-tiao-du')
              )
            ])
        }
      ],
      taskData: []
    };
  },
  computed: {
    getWorkerList() {
      return (selectedRow) => {
        const list = [];

        this.workerList.map((item) => {
          if (item.workerIp !== selectedRow.workerIp && item.healthLevel === 'Health') {
            list.push(item);
          }
          return null;
        });
        return list;
      };
    }
  },
  methods: {
    handleDispatch() {
      const data = {
        oldWorkerId: this.selectedRow.workerId,
        targetWorkerId: this.workerToDispatch,
        dataTaskId: this.selectedRow.taskId
      };

      this.$services
        .ccScheduleManualSchedule({
          data
        })
        .then((res) => {
          if (res.success) {
            this.getWorkList();
            // console.log(this.taskData,this.row,this.index);
            // let that = this;
            // setTimeout(()=>{
            //     console.log(that.taskData,this.row,this.index);
            // },1000);
          }
        });
    },
    rowClassName(row) {
      if (this.taskId) {
        if (row.taskId.toString() === this.taskId.toString()) {
          return 'current-task-row';
        }
        return '';
      }
      return '';
    }
  }
};
</script>
<style>
.ivu-table .current-task-row td {
  background-color: rgba(45, 183, 245, 0.1);
}
</style>
