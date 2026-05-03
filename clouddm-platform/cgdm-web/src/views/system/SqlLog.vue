<template>
  <div class="sql-log">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('sql-shen-ji') }}</BreadcrumbItem>
          </Breadcrumb>
        </div>
        <div class="content">
          <div class="option border-radius-card">
            <div class="left" style="align-items: center">
              {{ $t('cao-zuo-shi-jian') }}
              <DatePicker
                :editable="false"
                v-model="timeRange"
                type="datetimerange"
                format="yyyy-MM-dd HH:mm"
                style="width: 266px; margin-left: 10px; margin-right: 10px"
              ></DatePicker>
              <Select v-model="searchType" style="width: 100px; margin-right: 10px" @on-change="handleChangeSearchType">
                <Option value="user" :label="$t('cao-zuo-ren')">
                  <span>{{ $t('cao-zuo-ren') }}</span>
                </Option>
                <Option value="dsId" :label="$t('shu-ju-yuan')">
                  <span>{{ $t('shu-ju-yuan') }}</span>
                </Option>
                <Option value="resourcePath" :label="$t('cao-zuo-zi-yuan')">
                  <span>{{ $t('cao-zuo-zi-yuan') }}</span>
                </Option>
                <Option value="sqlKind" :label="$t('sql-lei-xing')">
                  <span>{{ $t('sql-lei-xing') }}</span>
                </Option>
                <Option value="requester" :label="$t('sql-lai-yuan')">
                  <span>{{ $t('sql-lai-yuan') }}</span>
                </Option>
                <Option value="status" :label="$t('sql-zhuang-tai')">
                  <span>{{ $t('sql-zhuang-tai') }}</span>
                </Option>
              </Select>
              <Select v-if="searchType === 'user'" v-model="searchData.userUid" style="width: 250px" clearable @on-change="handleRefresh">
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="user in operateUserList" :key="user.userUid" :value="user.userUid" :label="user.userName">
                  {{ user.userName }}
                </Option>
              </Select>
              <Input
                v-if="searchType === 'resourcePath'"
                v-model="searchData.resourcePath"
                @on-keydown="handleEnterSearch"
                style="width: 250px"
                clearable
              />
              <Select v-if="searchType === 'dsId'" v-model="searchData.dsId" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="ds in dsList" :key="ds.objId" :value="ds.objId" :label="ds.objAttr.dsInstance">
                  <CustomIcon :type="ds.objAttr.dsType" />
                  {{ ds.objAttr.dsInstance }}
                </Option>
              </Select>
              <Select v-if="searchType === 'sqlKind'" v-model="searchData.sqlKind" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option value="CREATE" label="CREATE">CREATE</Option>
                <Option value="ALTER" label="ALTER">ALTER</Option>
                <Option value="DROP" label="DROP">DROP</Option>
                <Option value="QUERY" label="QUERY">QUERY</Option>
                <Option value="DML" label="DML">DML</Option>
                <Option value="CALL" label="CALL">CALL</Option>
                <Option value="OTHER" label="OTHER">OTHER</Option>
              </Select>
              <Select v-if="searchType === 'requester'" v-model="searchData.requester" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option value="CONSOLE" :label="$t('sql-requester-console')">{{ $t('sql-requester-console') }}</Option>
                <Option value="TICKET" :label="$t('sql-requester-ticket')">{{ $t('sql-requester-ticket') }}</Option>
                <Option value="CHANGE" :label="$t('sql-requester-change')">{{ $t('sql-requester-change') }}</Option>
              </Select>
              <Select v-if="searchType === 'status'" v-model="searchData.status" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option value="RUNNING" label="RUNNING">RUNNING</Option>
                <Option value="SUCCESS" label="SUCCESS">SUCCESS</Option>
                <Option value="WAIT_CONFIRM" label="WAIT_CONFIRM">WAIT_CONFIRM</Option>
                <Option value="ROLLBACK" label="ROLLBACK">ROLLBACK</Option>
                <Option value="FAILURE" label="FAILURE">FAILURE</Option>
                <Option value="ERROR" label="ERROR">ERROR</Option>
              </Select>
              <Button type="primary" @click="handleRefresh" :loading="refreshLoading" style="margin-left: 10px">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Button type="default" style="margin-right: 6px" @click="handleRefresh" :loading="refreshLoading">
                <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
              </Button>
            </div>
          </div>
          <div class="table-container">
            <Table size="small" border :columns="logColumn" :data="logData" :loading="refreshLoading">
              <template #uid="{ row }">
                <div class="uid">
                  <span>{{ row.uid }}</span>
                  <cc-iconfont
                    :size="12"
                    name="copy"
                    class="copy"
                    @click.native="copyText(`${row.uid}`, $t('fu-zhi-uid-cheng-gong'))"
                    style="margin-left: 3px"
                  />
                </div>
              </template>
              <template #resource="{ row }">
                <Tooltip :content="row.resource" placement="top" transfer>
                  <span class="sql-log-resource-cell">
                    {{ row.resource }}
                  </span>
                </Tooltip>
              </template>
              <template #execSql="{ row }">
                <div class="sql-content">
                  <Button v-if="row.execSql" type="text" size="small" @click="showSqlDetail(row)">
                    {{ $t('cha-kan') }}
                  </Button>
                  <span v-else class="no-sql">{{ $t('wu-sql-nei-rong') }}</span>
                </div>
              </template>
            </Table>
          </div>
        </div>
      </div>
      <div class="footer">
        <Button :disabled="page === 1" style="font-size: 16px" @click="handlePre">
          <Icon type="ios-arrow-back" style="font-size: 16px" />
          {{ $t('shang-yi-ye') }}
        </Button>
        <span style="margin: 0 10px">{{ $t('di-page-ye', [page]) }}</span>
        <Button :disabled="noMoreData" style="font-size: 16px" @click="handleNext">
          <Icon type="ios-arrow-forward" style="font-size: 16px" />
          {{ $t('xia-yi-ye') }}
        </Button>
      </div>
    </div>
    <CCModal v-model="showSqlModal" title="SQL" width="1000px" @on-ok="handleCloseSqlModal" @on-cancel="handleCloseSqlModal">
      <div v-if="showSqlModal">
        <ReadOnlyDiffEditor
          v-if="selectedRow.rewrite"
          :modified-sql="selectedRow.execSql"
          :original-sql="selectedRow.originalSql"
          style="height: 400px"
        />
        <ReadOnlyEditor v-else :text="selectedRow?.execSql" style="height: 400px" />
      </div>
    </CCModal>
  </div>
</template>

<script>
import fecha from 'fecha';
import { mapState } from 'vuex';
import { h, resolveComponent } from 'vue';
import copyMixin from '@/mixins/copyMixin';
import ReadOnlyEditor from '@/components/editor/ReadOnlyEditor';
import ReadOnlyDiffEditor from '@/components/editor/ReadOnlyDiffEditor.vue';

export default {
  name: 'SqlLog',
  mixins: [copyMixin],
  components: { ReadOnlyDiffEditor, ReadOnlyEditor },
  data() {
    return {
      searchType: 'user',
      noMoreData: false,
      refreshLoading: false,
      firstId: 0,
      lastId: 0,
      prevFirst: {},
      page: 1,
      currentPageSize: 10,
      dsList: [],
      operateUserList: [],
      selectedRow: null,
      showSqlModal: false,
      timeRange: [new Date(new Date().getTime() - 24 * 3600 * 1000), new Date()],
      searchData: {
        dsId: null,
        userUid: null,
        sqlKind: null,
        requester: null,
        resourcePath: null,
        status: null,
        pageData: {
          startId: 0,
          pageSize: 10
        }
      },
      logColumn: [
        {
          title: this.$t('cao-zuo-zhe'),
          key: 'userName',
          width: 230
        },
        {
          title: this.$t('cao-zuo-shi-jian'),
          key: 'operateTime',
          width: 200,
          render: (_, params) => {
            const row = params.row || params;
            if (!row.operateTime) {
              return h('div', {}, '-');
            }
            const date = new Date(row.operateTime);
            if (isNaN(date.getTime())) {
              return h('div', {}, '-');
            }
            return h('div', {}, fecha.format(date, 'YYYY-MM-DD HH:mm:ss'));
          }
        },
        {
          title: this.$t('zhuang-tai'),
          key: 'status',
          width: 120,
          render: (_, params) => {
            const row = params.row || params;
            let color = '#ed4014';
            if (row.status === 'SUCCESS') {
              color = '#19be6b';
            } else if (row.status === 'RUNNING') {
              color = '#faad14';
            }
            const statusNode = h('div', { style: { color, display: 'flex', 'align-items': 'center' } }, [
              h('span', row.status),
              (row.status === 'FAILURE' || row?.status === 'ERROR') && row?.message
                ? h(
                    resolveComponent('Tooltip'),
                    {
                      content: row.message,
                      placement: 'top',
                      transfer: true
                    },
                    [
                      h(resolveComponent('CustomIcon'), {
                        type: 'help',
                        size: 16,
                        style: { color: '#aaa', marginLeft: '4px', cursor: 'pointer' }
                      })
                    ]
                  )
                : null
            ]);
            return statusNode;
          }
        },
        {
          title: this.$t('shu-ju-yuan'),
          key: 'dsDesc',
          width: 300,
          render: (_, params) => {
            const row = params.row || params;
            return h('div', { style: { display: 'flex', alignItems: 'center' } }, [
              h(resolveComponent('CustomIcon'), { type: row.dataSourceType }),
              h('span', { style: { marginLeft: '6px' } }, row.dsDesc)
            ]);
          }
        },
        {
          title: this.$t('cao-zuo-zi-yuan'),
          key: 'resource',
          width: 150,
          slot: 'resource'
        },
        {
          title: this.$t('sql-lai-yuan'),
          key: 'requester',
          width: 120,
          render: (_, params) => {
            const row = params.row || params;
            let text = row.requester;
            if (text === 'CONSOLE') text = this.$t('sql-requester-console');
            else if (text === 'TICKET') text = this.$t('sql-requester-ticket');
            else if (text === 'CHANGE') text = this.$t('sql-requester-change');
            else if (!text) text = this.$t('quan-bu');
            return h('span', text);
          }
        },
        {
          title: this.$t('sql-lei-xing'),
          key: 'sqlKind',
          width: 100
        },
        {
          title: this.$t('sql-zhi-hang-shi-jian'),
          key: 'cost',
          width: 150,
          render: (_, params) => {
            const row = params.row || params;
            return h('div', {}, `${row.cost || 0}ms`);
          }
        },
        {
          title: this.$t('ying-xiang-hang-shu'),
          key: 'affectLine',
          width: 120
        },
        {
          title: this.$t('cao-zuo-di-zhi'),
          key: 'clientIp',
          minWidth: 150
        },
        {
          title: this.$t('ri-zhi-di-zhi'),
          key: 'logIp',
          minWidth: 150
        },
        {
          title: this.$t('sql-nei-rong'),
          slot: 'execSql',
          width: 200,
          fixed: 'right'
        }
      ],
      logData: []
    };
  },
  computed: {
    ...mapState(['globalSetting'])
  },
  mounted() {
    this.getDsList();
    this.getOperateUserList();
    this.handleSearch();
    this.searchData.pageData.pageSize = 10;
  },
  methods: {
    async getDsList() {
      try {
        const res = await this.$services.dmAuditSqlAuditListDs();
        if (res.data && res.code === '1') {
          this.dsList = res.data || [];
        }
      } catch (error) {
        console.error('获取数据源列表失败:', error);
      }
    },

    async getOperateUserList() {
      try {
        const res = await this.$services.dmAuditSqlAuditOperateUser({
          data: {
            search: ''
          }
        });
        if (res.data && res.code === '1') {
          this.operateUserList = res.data || [];
        }
      } catch (error) {
        console.error('获取操作人列表失败:', error);
      }
    },

    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.handleRefresh();
      }
    },

    handleRefresh() {
      this.page = 1;
      this.firstId = 0;
      this.lastId = 0;
      this.prevFirst = {};
      this.currentPageSize = this.searchData.pageData.pageSize;
      this.searchData.pageData.startId = 0;
      this.handleSearch();
    },

    handleSearch(type) {
      this.refreshLoading = true;
      if (this.timeRange.length > 0) {
        this.searchData.opStart =
          this.timeRange[0] && fecha.format(new Date(new Date(this.timeRange[0]).getTime() - 8 * 3600 * 1000), 'YYYY-MM-DDTHH:mm:ss.SSS');
        this.searchData.opEnd =
          this.timeRange[1] && fecha.format(new Date(new Date(this.timeRange[1].getTime() - 8 * 3600 * 1000)), 'YYYY-MM-DDTHH:mm:ss.SSS');
      } else {
        this.searchData.opStart = '';
        this.searchData.opEnd = '';
      }
      this.searchData.pageData.pageSize = 10;

      if (this.currentPageSize !== this.searchData.pageData.pageSize) {
        this.page = 1;
        this.firstId = 0;
        this.lastId = 0;
        this.prevFirst = {};
        this.searchData.pageData.startId = 0;
        this.currentPageSize = this.searchData.pageData.pageSize;
      }

      this.$services
        .dmAuditSqlAuditQueryAll({
          data: this.searchData
        })
        .then((res) => {
          if (res.code === '1') {
            this.logData = res.data;

            if (this.logData.length > 0) {
              this.firstId = this.logData[0].id;
              this.lastId = this.logData[this.logData.length - 1].id;

              if (type === 'next') {
                this.prevFirst[this.page] = this.firstId;
              } else if (type === 'prev') {
                this.prevFirst[this.page] = this.firstId;
              } else {
                this.prevFirst[1] = this.firstId;
              }
            } else {
              this.firstId = 0;
              this.lastId = 0;
            }
          }
          this.refreshLoading = false;
          this.noMoreData = res.data.length < this.searchData.pageData.pageSize;
        })
        .catch(() => {
          this.refreshLoading = false;
        });
    },

    handlePre() {
      if (this.page <= 1) {
        return;
      }

      this.page--;
      let startId = 0;

      if (this.prevFirst[this.page] !== undefined) {
        startId = this.prevFirst[this.page] + 1;
      }

      if (startId < 0) {
        startId = 0;
      }

      this.searchData.pageData.startId = startId;
      this.handleSearch('prev');
    },

    handleNext() {
      if (this.noMoreData) {
        return;
      }

      this.searchData.pageData.startId = this.lastId;
      this.handleSearch('next');
      this.page++;
    },

    handleChangeSearchType() {
      this.page = 1;
      this.firstId = 0;
      this.lastId = 0;
      this.prevFirst = {};
      this.currentPageSize = 10;
      this.searchData = {
        uid: null,
        dsId: null,
        userUid: null,
        sqlKind: null,
        requester: null,
        status: null,
        pageData: {
          startId: 0,
          pageSize: 10
        }
      };
    },

    handleSearchUid(row) {
      this.searchType = 'uid';
      this.searchData.uid = row.uid;
      this.handleSearch();
    },

    showSqlDetail(row) {
      this.selectedRow = row;
      this.showSqlModal = true;
    },

    handleCloseSqlModal() {
      this.showSqlModal = false;
      this.selectedRow = null;
    }
  }
};
</script>

<style scoped lang="less">
.sql-log {
  height: 100%;
  display: flex;
  flex-direction: column;

  .uid {
    display: flex;
    align-items: center;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }

  .sql-content {
    .sql-text {
      display: inline-block;
      max-width: 180px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      cursor: pointer;

      &:hover {
        color: #2d8cf0;
      }
    }

    .no-sql {
      color: #999;
      font-style: italic;
    }
  }

  .sql-log-resource-cell {
    display: inline-block;
    max-width: 130px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}
</style>
