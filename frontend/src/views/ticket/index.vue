<template>
  <div class="ticket-container">
    <div class="table-list-layout">
      <AppPageTabs :model-value="ticketListType" :tabs="ticketTabs" @change="handleTabClick" />
      <div class="table-list">
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
              <Select v-model="searchKey.queryType" style="width: 140px; margin-right: 4px">
                <Option value="TITLE">{{ $t('biao-ti') }}</Option>
                <Option value="BIZ_ID">{{ $t('gong-dan-hao') }}</Option>
                <Option value="DESCRIPTION">{{ $t('miao-shu') }}</Option>
                <Option value="CONTENT">{{ $t('gong-dan-nei-rong-inline') }}</Option>
              </Select>
              <Input :placeholder="ticketQueryPlaceholder" v-model="searchKey.queryValue" style="width: 280px; margin-right: 4px" clearable />
              <Button type="primary" ghost class="ticket-search-btn" @click="listTickets">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button
                @click="handleShowTicketCreateModal"
                style="margin-right: 10px"
                type="primary"
                icon="md-add"
                v-if="myAuth.includes('RDP_WORKER_ORDER_REQUEST')"
              >
                {{ $t('ti-jiao-gong-dan') }}
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table size="small" :columns="ticketColumns" :data="ticketData" :scroll="ticketTableScroll" border :loading="loading">
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
                <div v-else-if="['DM_QUERY', 'DM_CHANGE'].includes(row.approBiz)" class="ticket-resource">
                  <DataSourceIcon
                    class="ticket-resource__icon"
                    size="24px"
                    :type="row.resourceType || 'DataBase'"
                    :instanceType="row.deployEnvType"
                    leftMargin="0"
                  />
                  <div class="ticket-resource__name" :title="row.resourceDesc || row.resourceName || '-'">
                    {{ row.resourceDesc || row.resourceName || '-' }}
                  </div>
                </div>
                <span v-else>
                  <CustomIcon :type="`icon-v2-${row.resourceType}`" :instanceType="row.deployEnvType"></CustomIcon>
                  {{ row.targetInfo }}
                </span>
              </template>
              <template #description="{ row }">
                <Poptip v-if="row.description" class="ticket-description-poptip" trigger="hover" transfer placement="top-start" :width="360">
                  <span class="ticket-description-trigger">{{ row.description }}</span>
                  <template #content>
                    <div class="ticket-description-popover">
                      <div class="ticket-description-popover__content">{{ row.description }}</div>
                      <div class="ticket-description-popover__actions">
                        <Button type="text" size="small" @click.stop="copyText(row.description)">
                          {{ $t('fu-zhi') }}
                        </Button>
                      </div>
                    </div>
                  </template>
                </Poptip>
                <span v-else>-</span>
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
          <Tooltip :content="rootAccountUnsupportedTip" :disabled="!isRootAccount" transfer placement="top">
            <div style="border: 1px solid #ccc" :class="authTicketClass" @click="handleChangeTicketType('auth')">
              <CustomIcon type="icon-v2-TicketAuth" size="48" />
              <div class="mt-[10px]">{{ $t('quan-xian-gong-dan-config') }}</div>
            </div>
          </Tooltip>
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
        <Button type="primary" @click="handleCreateTicket" :disabled="!ticketType">
          {{ $t('ti-jiao-gong-dan') }}
        </Button>
      </template>
    </CCModal>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import { TICKET_STATUS, TICKET_STATUS_COLOR } from '@/const';
import { APPROV_BIZ_MAP } from './constant';
import AppPageTabs from '@/components/layout/AppPageTabs';
import CustomIcon from '@/components/function/CustomIcon.vue';
import DataSourceIcon from '@/components/function/DataSourceIcon';
import copyMixin from '@/mixins/copyMixin';

export default {
  name: 'Ticket',
  components: { AppPageTabs, CustomIcon, DataSourceIcon },
  mixins: [copyMixin],
  data() {
    return {
      showTicketCreateModal: false,
      ticketType: '',
      loading: false,
      ticketData: [],
      ticketListType: 'WAIT_SELF_PROCESS',
      ticketTabs: [
        { name: 'WAIT_SELF_PROCESS', label: this.$t('dai-ban') },
        { name: 'SELF_CREATE', label: this.$t('wo-fa-qi-de') },
        { name: 'ALL', label: this.$t('quan-bu') }
      ],
      searchKey: {
        daterange: [],
        queryType: 'BIZ_ID',
        queryValue: '',
        type: ''
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
          width: 240
        },
        {
          title: this.$t('miao-shu'),
          slot: 'description',
          width: 280
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
      pageSize: 20,
      pageNum: 1,
      total: 0
    };
  },
  mounted() {
    this.listTickets();
  },
  computed: {
    ticketTableScroll() {
      return { x: 1587 };
    },
    ticketQueryPlaceholder() {
      switch (this.searchKey.queryType) {
        case 'BIZ_ID':
          return this.$t('qing-shu-ru-gong-dan-hao-cha-xun');
        case 'DESCRIPTION':
          return this.$t('qing-shu-ru-gong-dan-miao-shu-guan-jian-zi-cha-xun');
        case 'CONTENT':
          return this.$t('qing-shu-ru-gong-dan-inline-nei-rong-guan-jian-zi-cha-xun');
        default:
          return this.$t('qing-shu-ru-gong-dan-biao-ti-guan-jian-zi-cha-xun');
      }
    },
    TICKET_STATUS() {
      return TICKET_STATUS;
    },
    TICKET_STATUS_COLOR() {
      return TICKET_STATUS_COLOR;
    },
    isRootAccount() {
      return this.userInfo.accountType === 'PRIMARY_ACCOUNT';
    },
    rootAccountUnsupportedTip() {
      return '管理员账号不支持此操作';
    },
    authTicketClass() {
      return [
        'flex flex-col items-center p-[10px] px-[20px] rounded-[4px]',
        this.ticketType === 'auth' ? 'bg-[#ddd]' : '',
        this.isRootAccount ? 'cursor-not-allowed opacity-50' : 'cursor-pointer'
      ].join(' ');
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
        if (this.isRootAccount) {
          this.$Message.warning(this.rootAccountUnsupportedTip);
          return;
        }
        this.$router.push({ path: '/system/permission', query: { type: 'apply' } });
      }
      this.handleCloseTicketCreateModal();
    },
    handleChangeTicketType(ticketType) {
      if (ticketType === 'auth' && this.isRootAccount) {
        return;
      }
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
    handleTabClick(name) {
      if (this.ticketListType === name) {
        return;
      }
      this.ticketListType = name;
      this.pageNum = 1;
      this.listTickets();
    },
    handleShowTicketCreateModal() {
      this.showTicketCreateModal = true;
    },
    async listTickets() {
      this.loading = true;
      let ticketBizId = null;
      let ticketTitleName = null;
      let ticketDescription = null;
      let ticketContent = null;
      const queryValue = this.searchKey.queryValue.trim();
      switch (this.searchKey.queryType) {
        case 'BIZ_ID':
          ticketBizId = queryValue || null;
          break;
        case 'DESCRIPTION':
          ticketDescription = queryValue || null;
          break;
        case 'CONTENT':
          ticketContent = queryValue || null;
          break;
        default:
          ticketTitleName = queryValue || null;
      }
      const res = await this.$services.rdpTicketListBasic({
        data: {
          ticketId: null,
          userName: '',
          startTimeMs: new Date(this.searchKey.daterange[0]).getTime(),
          endTimeMs: new Date(this.searchKey.daterange[1]).getTime(),
          ticketBizId,
          ticketTitleName,
          ticketDescription,
          ticketContent,
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
  min-height: 0;
  overflow: hidden;
}

.ticket-resource {
  display: flex;
  min-width: 0;
  min-height: 32px;
  align-items: center;
  gap: 8px;
}

.ticket-resource__icon {
  display: inline-flex;
  width: 28px;
  flex: 0 0 28px;
  align-items: center;
  justify-content: center;
}

.ticket-resource__name {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
  line-height: 20px;
}

.ticket-description-poptip {
  display: block;
  width: 100%;
}

.ticket-description-poptip :deep(.ivu-poptip-rel) {
  display: block;
  width: 100%;
}

.ticket-description-trigger {
  display: block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ticket-description-popover__content {
  max-height: 240px;
  overflow: auto;
  white-space: pre-wrap;
  word-break: break-word;
  user-select: text;
}

.ticket-description-popover__actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}
</style>
