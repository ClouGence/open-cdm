<template>
  <div class="measurement">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('ji-liang') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div>
            <div>
              <div class="sm:flex sm:items-center">
                <Select class="w-40 mr-4" v-model="searchData.type">
                  <Option value="id">{{ $t('ren-wu-id') }}</Option>
                  <Option value="range">{{ $t('shi-jian') }}</Option>
                  <Option value="dataTaskType">{{ $t('ren-wu-lei-xing-1') }}</Option>
                </Select>
                <Input
                  v-if="searchData.type === 'id'"
                  :placeholder="$t('qing-shu-ru-ren-wu-id')"
                  class="w-72 mr-4"
                  v-model="searchData.dataJobName"
                />
                <DatePicker
                  v-if="searchData.type === 'range'"
                  class="mr-4"
                  type="datetimerange"
                  v-model="timeRange"
                  :placeholder="$t('qing-xuan-ze-shi-jian-fan-wei')"
                  style="width: 300px"
                />
                <Select
                  v-if="searchData.type === 'dataTaskType'"
                  class="w-72 mr-4"
                  v-model="searchData.dataTaskType"
                  :placeholder="$t('qing-xuan-ze-ren-wu-lei-xing')"
                >
                  <Option value="BUILD_STRUCT">{{ $t('BUILD_STRUCT') }}</Option>
                  <Option value="FULL">{{ $t('FULL') }}</Option>
                  <Option value="INCREMENT">{{ $t('INCREMENT') }}</Option>
                  <Option value="CHECK">{{ $t('CHECK') }}</Option>
                  <Option value="REVISE">{{ $t('REVISE') }}</Option>
                </Select>
                <Button :loading="loading" @click="handleSearch" type="primary">
                  {{ $t('cha-xun') }}
                </Button>
              </div>
              <div class="mt-6 flow-root">
                <div class="overflow-x-auto">
                  <div class="min-w-full align-middle border border-solid border-gray-200 rounded">
                    <Table size="small" :columns="columns" :data="measurementList" :loading="loading"></Table>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="footer">
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
import store from '@/store';
import fecha from 'fecha';

export default {
  name: 'Measurement',
  data() {
    return {
      firstId: 0,
      lastId: 0,
      prevFirst: [],
      page: 1,
      noMoreData: false,
      timeRange: [new Date(new Date().getTime() - 7 * 24 * 3600 * 1000), new Date()],
      searchData: {
        type: 'id',
        dataJobName: '',
        dataTaskType: '',
        startTime: '',
        endTime: '',
        pageData: {
          startId: 0,
          pageSize: 10
        }
      },
      loading: false,
      columns: [
        {
          title: this.$t('ren-wu-id'),
          key: 'dataJobName'
        },
        {
          title: this.$t('ren-wu-lei-xing-1'),
          key: 'dataTaskType'
        },
        {
          title: this.$t('shi-jian'),
          key: 'calculateEndTime'
        },
        {
          title: this.$t('shu-liang'),
          key: 'totalCount'
        }
      ],
      measurementList: []
    };
  },
  async mounted() {
    this.handleSearch('next');
  },
  methods: {
    handleSearch(type) {
      if (this.timeRange.length > 0) {
        this.searchData.startTime =
          this.timeRange[0] && fecha.format(new Date(new Date(this.timeRange[0]).getTime() - 8 * 3600 * 1000), 'YYYY-MM-DDTHH:mm:ss.SSS');
        this.searchData.endTime =
          this.timeRange[1] && fecha.format(new Date(new Date(this.timeRange[1].getTime() - 8 * 3600 * 1000)), 'YYYY-MM-DDTHH:mm:ss.SSS');
      } else {
        this.searchData.startTime = '';
        this.searchData.endTime = '';
      }
      this.searchData.pageData.pageSize = 10;

      if (type !== 'next' && type !== 'prev') {
        this.page = 1;
        this.firstId = 0;
        this.lastId = 0;
        this.prevFirst = [];
        this.searchData.pageData.startId = 0;
        this.noMoreData = false;
      }

      this.listUserCals(type);
    },
    async listUserCals(type) {
      this.loading = true;
      const res = await this.$services.rdpCalculateListUserCals({
        data: {
          ccProductName: store.state.selectCcProductCluster,
          startTime: this.searchData.startTime,
          endTime: this.searchData.endTime,
          dataJobName: this.searchData.dataJobName,
          dataTaskType: this.searchData.dataTaskType,
          startId: this.searchData.pageData.startId,
          pageSize: this.searchData.pageData.pageSize
        }
      });
      if (res?.success) {
        this.measurementList = res.data;
        if (type === 'next') {
          if (!this.prevFirst[this.page - 1]) {
            this.prevFirst.push(this.firstId);
          }
        }
        if (this.measurementList.length > 0) {
          this.firstId = this.measurementList[0].id;
          this.lastId = this.measurementList[this.measurementList.length - 1].id;
        }
      }
      this.noMoreData = res.data.length < this.searchData.pageData.pageSize;
      this.loading = false;
    },
    handlePre() {
      this.page--;
      let startId = this.prevFirst[this.page - 1] === 0 ? this.prevFirst[this.page - 1] : this.prevFirst[this.page - 1] + 1;

      if (startId < 0) {
        startId = 0;
      }
      this.searchData.pageData.startId = startId;
      this.handleSearch('prev');
    },
    handleNext() {
      this.searchData.pageData.startId = this.lastId;
      this.handleSearch('next');
      this.page++;
    }
  }
};
</script>
<style scoped>
.measurement {
  height: 100%;
  display: flex;
  flex-direction: column;
}
</style>
