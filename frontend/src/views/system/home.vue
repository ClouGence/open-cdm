<template>
  <div class="home">
    <a-alert banner closable type="warning" style="margin-bottom: 10px">
      <template #message>
        <div class="alert-warning-msgContent">
          <span>{{ $t('cao-zuo-ti-shi-di-yi-bu') }}</span>
          {{ $t('chuang-jian-ji-qun-tian-jia-ji-qi') }}
          <span>{{ $t('di-er-bu') }}</span>
          {{ $t('tian-jia-shu-ju-yuan-0') }}
        </div>
      </template>
    </a-alert>
    <div v-if="userInfo.authArr.includes('SYSTEM_GET_RESOURCE_LIMIT')">
      <div class="title">{{ $t('xi-tong-zi-yuan-zhuang-kuang') }}</div>
      <div class="resource">
        <card
          v-if="!!userInfo.subAccount"
          :handleClickBtn="() => handleClickAddBtn('/system/permission')"
          :num="summary.dsAuthCounts"
          :show-btn="!!userInfo.subAccount"
          :btn-text="$t('shen-qing')"
          name="worker"
          :quantifier="$t('tiao')"
          :title="$t('wo-de-quan-xian')"
          router-name="/system/permission"
        />
        <card
          :handleClickBtn="() => handleClickAddBtn('/system/dmmachine')"
          :num="summary.workerCounts"
          :show-btn="userInfo.menuItems.includes('/ccsystem/machine')"
          :btn-text="$t('tian-jia')"
          name="worker"
          :quantifier="$t('tai')"
          :title="$t('fang-wen-ji-qi')"
          router-name="/system/dmmachine"
        />
        <card
          :handleClickBtn="() => handleClickAddBtn('/system/ccdatasource')"
          :num="summary.dsCounts"
          :show-btn="userInfo.menuItems.includes('/ccsystem/datasource')"
          :btn-text="$t('tian-jia')"
          name="datasource"
          :quantifier="$t('ge')"
          :title="$t('shu-ju-yuan')"
          router-name="/system/ccdatasource"
        />
        <card
          :handleClickBtn="() => handleClickAddBtn('/system/account')"
          :num="summary.subAccountCounts"
          :show-btn="userInfo.menuItems.includes('/system/account')"
          :btn-text="$t('chuang-jian')"
          name="subaccount"
          :quantifier="$t('ge')"
          :title="$t('zi-zhang-hao')"
          router-name="/system/account"
        />
      </div>
    </div>
    <div v-if="userInfo.authArr.includes('SYSTEM_GET_DS_OP_AUDITS')">
      <div class="title">{{ $t('zui-jin-shi-yong-ji-lu') }}</div>
      <a-form layout="inline" :label-col="{ span: 5 }" :wrapper-col="{ span: 12 }">
        <a-form-item>
          <a-input style="width: 200px" :placeholder="$t('qing-shu-ru-yong-hu-ming')" v-model="searchAuditKey.userName" />
        </a-form-item>
        <a-form-item>
          <a-range-picker show-time format="YYYY-MM-DD HH:mm" @change="handleChangeTimeRanger" />
        </a-form-item>
        <a-form-item>
          <Button type="primary" @click="handleSearch">{{ $t('cha-xun') }}</Button>
        </a-form-item>
      </a-form>
      <a-table
        :columns="recordColumns"
        :data-source="recordList"
        :pagination="false"
        :row-key="(record) => record.id"
        size="small"
        :scroll="{ y: 500 }"
      >
        <template #time="record">
          {{ $filters.formatTime(record.executionDate, 'YYYY-MM-DD hh:mm:ss') }}
        </template>
        <template #executionSql="record">
          <a-tooltip placement="right">
            <template #title>
              <span>{{ record.executionSql }}</span>
            </template>
            <span class="content-need-omit">{{ record.executionSql }}</span>
          </a-tooltip>
        </template>
        <template #success="record">
          <span :style="`color:${record.success ? '#52C41A' : '#FF1815'}`">
            {{ record.success ? $t('cheng-gong') : $t('shi-bai') }}
          </span>
        </template>
        <template #dsAuthKindI8n="record">
          <span :style="`color:${record.dsAuthKindI8n !== $t('jie-gou-bian-geng') ? '#52C41A' : '#FF1815'}`">
            {{ record.dsAuthKindI8n }}
          </span>
        </template>
        <template #execType="record">
          {{ record.execType === 'ONLINE_EXEC' ? $t('zai-xian-zhi-hang') : record.execType === 'DATA_EXPORT' ? $t('yi-bu-zhi-hang') : '' }}
        </template>
      </a-table>
      <cc-pagination @onChange="handlePageChange" style="float: right" :page="page" :hasNext="hasNext" />
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import Card from './components/Card';

export default {
  name: 'System_Home',
  components: {
    Card
  },
  data() {
    return {
      summary: {},
      recordColumns: [
        {
          title: this.$t('sql-zhi-hang-shi-jian'),
          scopedSlots: { customRender: 'time' },
          width: 180
        },
        {
          title: this.$t('zhi-hang-yu-ju'),
          scopedSlots: { customRender: 'executionSql' },
          minWidth: 300
        },
        {
          title: this.$t('zhi-hang-ren'),
          dataIndex: 'username',
          width: 100
        },
        {
          title: this.$t('huan-jing-ming-cheng'),
          dataIndex: 'envName',
          width: 100
        },
        {
          title: this.$t('shi-li-id'),
          dataIndex: 'instanceId',
          width: 180
        },
        {
          title: this.$t('she-ji-quan-xian'),
          dataIndex: 'authPath'
        },
        {
          title: this.$t('zhuang-tai'),
          scopedSlots: { customRender: 'success' },
          width: 60
        },
        {
          title: this.$t('zhi-hang-hao-shi-ms'),
          dataIndex: 'elapseMs',
          width: 120
        },
        {
          title: this.$t('ying-xiang-hang-shu'),
          dataIndex: 'affectRows',
          width: 90
        },
        {
          title: this.$t('sql-lei-xing'),
          scopedSlots: { customRender: 'dsAuthKindI8n' },
          width: 100
        },
        {
          title: this.$t('zhi-hang-lei-xing'),
          scopedSlots: { customRender: 'execType' },
          width: 100
        }
      ],
      searchAuditKey: {
        userName: '',
        startExecDate: '',
        endExecDate: '',
        pageSize: 10,
        startId: 0
      },
      preStartIds: [],
      page: 1,
      hasNext: true,
      recordList: [{}],
      hasSummaryAuth: false,
      hasDsOpAuth: false
    };
  },
  computed: {
    ...mapState({
      userInfo: (state) => state.userInfo
    })
  },
  watch: {
    'userInfo.authArr': {
      handler(newVal, oldVal) {
        if (newVal.length !== oldVal.length) {
          this.requestAuthApi(newVal);
        }
      },
      deep: true
    }
  },
  created() {
    this.requestAuthApi(this.userInfo.authArr);
  },
  methods: {
    requestAuthApi(authArr) {
      const hasSummaryAuth = authArr.includes('SYSTEM_GET_RESOURCE_LIMIT');
      const hasDsOpAuth = authArr.includes('SYSTEM_GET_DS_OP_AUDITS');

      if (hasSummaryAuth) {
        this.getSummary();
      }

      if (hasDsOpAuth) {
        this.getDbOpAudits();
      }

      this.hasSummaryAuth = hasSummaryAuth;
      this.hasDsOpAuth = hasDsOpAuth;
    },
    handleClickAddBtn(path) {
      this.$router.push({ path });
      this.$bus.emit('changeSidebar', path);
    },
    async getSummary() {
      const res = await this.$services.rdpUserResourceSummary();
      if (res.success) {
        this.summary = res.data;
      }
    },
    async getDbOpAudits(type) {
      if (type) {
        if (type === 'left') {
          this.page--;
          this.searchAuditKey.startId = this.preStartIds[this.page - 1];
        }

        if (this.page < 0) {
          this.page = 1;
          this.searchAuditKey.startId = 0;
        }

        if (type === 'right') {
          this.searchAuditKey.startId = this.recordList[this.searchAuditKey.pageSize - 1].id;
          this.page++;
        }
      }
      const res = await this.$services.rdpUserDbOpAudits({
        data: {
          pageSize: this.searchAuditKey.pageSize,
          startId: this.searchAuditKey.startId,
          userName: this.searchAuditKey.userName || null,
          startExecDate: this.searchAuditKey.startExecDate || null,
          endExecDate: this.searchAuditKey.endExecDate || null
        }
      });
      if (res.success) {
        if (res.data.length) {
          if (type === 'right') {
            if (!this.preStartIds[this.page - 1]) {
              this.preStartIds.push(this.recordList[0].id);
            }
          }
          this.recordList = res.data;
          if (this.recordList.length >= this.searchAuditKey.pageSize) {
            this.hasNext = true;
          } else {
            this.hasNext = false;
          }
        } else {
          this.page--;
        }
      }
    },
    handleChangeTimeRanger(data) {
      this.searchAuditKey.startExecDate = data[0];
      this.searchAuditKey.endExecDate = data[1];
    },
    handlePageChange(type) {
      this.getDbOpAudits(type);
    },
    handleSearch() {
      this.page = 1;
      this.searchAuditKey.startId = 0;
      this.preStartIds = [];
      this.getDbOpAudits();
    }
  }
};
</script>

<style lang="less" scoped>
.home {
  width: 100%;

  .alert-warning-msgContent {
    span {
      font-weight: bold;
    }
  }

  .title {
    font-size: 12px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  .resource {
    margin-bottom: 20px;
    padding-left: 40px;
    height: 136px;
    width: 100%;
    display: flex;
    justify-content: space-around;
  }
}

.content-need-omit {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  -o-text-overflow: ellipsis;
  display: block;
}
</style>
