<template>
  <div class="ticket-container">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('gong-dan') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <Tabs v-model="ticketListType" @on-click="handleChangeTicketListType">
          <TabPane name="WAIT_SELF_PROCESS" :label="$t('dai-ban')" />
          <TabPane name="SELF_CREATE" :label="$t('wo-fa-qi-de')" />
          <TabPane name="ALL" :label="$t('quan-bu')" />
        </Tabs>
        <div class="content">
          <div class="option">
            <div class="left">
              <DatePicker
                v-model="searchKey.daterange"
                format="yyyy-MM-dd HH:mm:ss"
                type="datetimerange"
                :placeholder="$t('qing-xuan-ze-shi-jian')"
                style="width: 300px; margin-right: 4px"
                clearable
              />
              <Select style="width: 120px; margin-right: 4px" v-model="searchKey.ticketStatus" clearable>
                <Option v-for="(status, key) in ticketStatusList" :value="key" :key="key">
                  {{ status }}
                </Option>
              </Select>
              <Input
                :placeholder="$t('qing-shu-ru-gong-dan-biao-ti-guan-jian-zi-cha-xun')"
                v-model="searchKey.ticketTitleName"
                style="width: 280px; margin-right: 4px"
                clearable
              />
              <Button type="primary" class="ticket-search-btn" @click="listTickets">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button
                @click="handleShowTicketCreateModal"
                style="margin-right: 10px"
                type="primary"
                ghost
                icon="md-add"
                v-if="myAuth.includes('RDP_WORKER_ORDER_REQUEST')"
              >
                {{ $t('ti-jiao-gong-dan') }}
              </Button>
              <Button type="default" @click="listTickets">
                <CustomIcon type="icon-v2-Refresh" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table size="small" :columns="ticketColumns" :data="ticketData" border :loading="loading">
              <template #ticketStatus="{ row }">
                <div :style="`display: flex;color:${TICKET_STATUS_COLOR[row.ticketStatus]}`">
                  <div style="margin-right: 3px">{{ TICKET_STATUS[row.ticketStatus] }}</div>
                </div>
              </template>
              <template #targetInfo="{ row }">
                <span v-if="row.approBiz === 'DATA_SOURCE_AUTH'">
                  <CustomIcon type="icon-v2-TicketAuth" />
                  {{ row.targetInfo }}
                </span>
                <span v-else>
                  <CustomIcon :type="`icon-v2-${row.resourceType}`" :instanceType="row.deployEnvType"></CustomIcon>
                  {{ row.targetInfo }}
                </span>
              </template>
              <template #time="{ row }">
                {{ row.gmtCreate }}
              </template>
              <template #action="{ row }">
                <router-link :to="`/ticket/${row.id}`">{{ $t('cha-kan') }}</router-link>
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
    <CCModal v-model="showTicketCreateModal" :title="$t('xuan-ze-gong-dan-lei-xing')" width="400px">
      <div class="flex mt-[20px]">
        <div class="flex-1 flex justify-center items-center">
          <div
            style="border: 1px solid #ccc"
            :class="`flex flex-col items-center p-[10px] px-[20px] cursor-pointer rounded-[4px] ${ticketType === 'auth' ? 'bg-[#ddd]' : ''}`"
            @click="handleChangeTicketType('auth')"
          >
            <CustomIcon type="icon-v2-TicketAuth" size="48" />
            <div class="mt-[10px]">{{ $t('quan-xian-gong-dan-config') }}</div>
          </div>
        </div>
        <div class="flex-1 flex justify-center items-center">
          <div
            style="border: 1px solid #ccc"
            :class="`flex flex-col items-center p-[10px] px-[20px] cursor-pointer rounded-[4px] ${ticketType === 'sql' ? 'bg-[#ddd]' : ''}`"
            @click="handleChangeTicketType('sql')"
          >
            <CustomIcon type="icon-v2-shenhe" size="48" />
            <div class="mt-[10px]">{{ $t('sql-gong-dan-config') }}</div>
          </div>
        </div>
      </div>
      <template #footer>
        <Button @click="handleCloseTicketCreateModal">{{ $t('qu-xiao') }}</Button>
        <Button type="primary" @click="handleCreateTicket" :disabled="!ticketType">{{ $t('ti-jiao-gong-dan') }}</Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { TICKET_STATUS, TICKET_STATUS_COLOR, TICKET_WAIT_STATUS } from '@/const';
import { APPROV_BIZ_MAP } from './constant';
import CustomIcon from '@/components/function/CustomIcon.vue';

export default {
  name: 'Ticket',
  components: { CustomIcon },
  data() {
    return {
      showTicketCreateModal: false,
      ticketType: '',
      loading: false,
      ticketData: [],
      ticketListType: 'WAIT_SELF_PROCESS',
      searchKey: {
        daterange: [],
        ticketStatus: '',
        type: '',
        ticketTitleName: ''
      },
      ticketColumns: [
        {
          title: this.$t('zhuang-tai'),
          slot: 'ticketStatus',
          width: 100,
          align: 'center'
        },
        {
          title: this.$t('gong-dan-hao'),
          key: 'bizId',
          width: 160
        },
        {
          title: this.$t('lei-xing'),
          key: 'approBiz',
          render: (h, params) => h('div', APPROV_BIZ_MAP[params.row?.approBiz]),
          width: 85,
          align: 'center'
        },
        {
          title: this.$t('biao-ti'),
          key: 'ticketTitle',
          minWidth: 200
        },
        {
          title: this.$t('zi-yuan'),
          slot: 'targetInfo',
          width: 340
        },
        {
          title: this.$t('shen-qing-ren'),
          key: 'userName',
          width: 100
        },
        {
          title: this.$t('shi-jian-1'),
          slot: 'time',
          width: 182
        },
        {
          title: this.$t('cao-zuo-0'),
          width: 100,
          fixed: 'right',
          slot: 'action'
        }
      ],
      pageSize: 40,
      pageNum: 1,
      total: 0
    };
  },
  mounted() {
    this.listTickets();
  },
  computed: {
    ticketStatusList() {
      return this.ticketListType === 'WAIT_SELF_PROCESS' ? TICKET_WAIT_STATUS : TICKET_STATUS;
    },
    styleVar() {
      return (ticketStatus) => ({
        '--status-color': TICKET_STATUS_COLOR[ticketStatus]
      });
    },
    TICKET_STATUS() {
      return TICKET_STATUS;
    },
    TICKET_STATUS_COLOR() {
      return TICKET_STATUS_COLOR;
    },
    ...mapState(['userInfo', 'myCatLog', 'myAuth'])
  },
  methods: {
    handleCloseTicketCreateModal() {
      this.showTicketCreateModal = false;
    },
    handleCreateTicket() {
      if (this.ticketType === 'sql') {
        this.$router.push('/ticket_create');
      } else if (this.ticketType === 'auth') {
        this.$router.push({ path: '/system/permission', query: { type: 'apply' } });
      }
      this.handleCloseTicketCreateModal();
    },
    handleChangeTicketType(ticketType) {
      this.ticketType = ticketType;
    },
    handlePageChange(pageNum) {
      this.pageNum = pageNum;
      this.listTickets();
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handlePageChange(1);
    },
    handleChangeTicketListType() {
      this.pageNum = 1;
      this.listTickets();
    },
    handleShowTicketCreateModal() {
      this.showTicketCreateModal = true;
    },
    async listTickets() {
      this.loading = true;
      const res = await this.$services.rdpTicketListBasic({
        data: {
          ticketId: null,
          userName: '',
          startTimeMs: new Date(this.searchKey.daterange[0]).getTime(),
          endTimeMs: new Date(this.searchKey.daterange[1]).getTime(),
          ticketTitleName: this.searchKey.ticketTitleName,
          ticketStatus: this.searchKey.ticketStatus,
          ticketListType: this.ticketListType,
          page: {
            pageSize: this.pageSize,
            pageNum: this.pageNum
          }
        }
      });
      this.loading = false;
      if (res.success) {
        this.ticketData = res.data.records;
        this.total = res.data.total;
      }
    }
  }
};
</script>

<style lang="less" scoped>
.ticket-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: auto;
}
</style>
