<template>
  <div class="operation-log">
    <div class="table-list-layout">
      <div class="table-list">
        <div class="header">
          <Breadcrumb>
            <BreadcrumbItem>{{ $t('cao-zuo-shen-ji') }}</BreadcrumbItem>
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
                <Option value="resourceType" :label="$t('zi-yuan-lei-xing')">
                  <span>{{ $t('zi-yuan-lei-xing') }}</span>
                </Option>
                <Option value="auditType" :label="$t('cao-zuo-dong-zuo')">
                  <span>{{ $t('cao-zuo-dong-zuo') }}</span>
                </Option>
                <Option value="uid" label="uid">
                  <span>uid</span>
                </Option>
              </Select>
              <Input v-if="searchType === 'user'" v-model="searchData.userNameLike" @on-keydown="handleEnterSearch" style="width: 250px" clearable />
              <Input v-if="searchType === 'uid'" v-model="searchData.uid" @on-keydown="handleEnterSearch" style="width: 250px" clearable />
              <Select v-if="searchType === 'resourceType'" v-model="searchData.resourceType" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in resourceTypeList" :value="item.resourceType" :key="item.resourceType">
                  {{ item.alias }}
                </Option>
              </Select>
              <Select v-if="searchType === 'auditType'" v-model="searchData.auditType" filterable style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in auditTypeList" :value="item.auditType" :key="item.auditType">
                  {{ item.alias }}
                </Option>
              </Select>
              <Button type="primary" @click="handleRefresh" :loading="refreshLoading" style="margin-left: 10px">
                {{ $t('cha-xun') }}
              </Button>
            </div>
            <div class="right">
              <Tooltip transfer :content="$t('dao-chu')" placement="bottom">
                <Button type="default" style="margin-right: 6px" @click="handleExport">
                  <CustomIcon type="icon-v2-daochu" />
                </Button>
              </Tooltip>
              <Tooltip transfer :content="$t('shua-xin')" placement="bottom">
                <Button type="default" style="margin-right: 6px" @click="handleRefresh" :loading="refreshLoading">
                  <CustomIcon type="icon-v2-Refresh" v-if="!refreshLoading" />
                </Button>
              </Tooltip>
            </div>
          </div>
          <div class="table-container">
            <Table size="small" border :columns="logColumn" :data="logData" :loading="refreshLoading">
              <template #resourceValue="{ row }">
                <p v-if="row.resourceType !== 'PURE_URL'">
                  {{ row.resourceVO && row.resourceVO.resourceFlag }}
                </p>
                <p v-if="row.resourceType === 'PURE_URL'">
                  {{ row.operationUri || row.resourceValue }}
                </p>
              </template>
              <template #uid="{ row }">
                <div class="uid">
                  <a @click="handleSearchUid(row)">{{ row.uid }}</a>
                  <cc-iconfont
                    :size="12"
                    name="copy"
                    class="copy"
                    @click="copyText(`${row.uid}`, $t('fu-zhi-uid-cheng-gong'))"
                    style="margin-left: 3px"
                  />
                </div>
              </template>
              <template #detail="{ row }">
                <div>
                  <a v-if="row.isExistsLog" @click="handleGetAuditDetail(row)">
                    {{ $t('cha-kan') }}
                  </a>
                  <a disabled v-if="!row.isExistsLog">{{ $t('cha-kan') }}</a>
                </div>
              </template>
            </Table>
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
    <CCModal v-model="showAuditDetail" :title="$t('cha-kan-ri-zhi')" width="1200px">
      <div>
        <div class="log-content">
          <p class="log-content-desc">
            <span>{{ $t('miao-shu-0') }}</span>
            <span class="point-content">{{ auditLogDetail.desc }}</span>
          </p>
          <p class="log-content-desc">
            <span>{{ $t('lu-jing-0') }}</span>
            <span class="point-content">{{ auditLogDetail.path }}</span>
          </p>
          <div class="warn-text" v-if="isParseError">
            <CustomIcon type="icon-v2-WarnColorful" rightMargin />
            <div>{{ $t('dang-qian-can-shu-guo-da-jian-yi-fu-zhi-hou-zi-hang-cha-kan') }}</div>
          </div>
          <div class="detail">
            <div style="padding-bottom: 20px" v-if="auditLogDetail.content">
              <pre>{{ getLogDetail(auditLogDetail.content) }}</pre>
            </div>
            <div v-if="!auditLogDetail.content && selectedRow.uuidKey">
              <p>{{ $t('ri-zhi-yi-gui-dang-qing-zhi-fu-wu-qi-cha-kan') }}</p>
              <pre>{{ JSON.parse(selectedRow.uuidKey) }}</pre>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <div>
          <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
          <Button v-if="isParseError" @click="copyText(auditLogDetail.content, $t('fu-zhi-can-shu-cheng-gong'))" type="primary">
            {{ $t('fu-zhi-can-shu') }}
          </Button>
        </div>
      </template>
    </CCModal>
    <CCModal v-model="showExport" :title="$t('dao-chu-cao-zuo-shen-ji')" width="850px">
      <div>
        <div>
          <Form :label-width="100">
            <FormItem :label="$t('shai-xuan-tiao-jian')">
              <DatePicker
                disabled
                :editable="false"
                v-model="timeRange"
                type="datetimerange"
                format="yyyy-MM-dd HH:mm"
                style="width: 266px; margin-right: 10px"
              ></DatePicker>
              <Select disabled v-model="searchType" style="width: 100px; margin-right: 10px" @on-change="handleChangeSearchType">
                <Option value="user" :label="$t('cao-zuo-ren')">
                  <span>{{ $t('cao-zuo-ren') }}</span>
                </Option>
                <Option value="resourceType" :label="$t('zi-yuan-lei-xing')">
                  <span>{{ $t('zi-yuan-lei-xing') }}</span>
                </Option>
                <Option value="auditType" :label="$t('cao-zuo-dong-zuo')">
                  <span>{{ $t('cao-zuo-dong-zuo') }}</span>
                </Option>
                <Option value="uid" label="uid">
                  <span>uid</span>
                </Option>
              </Select>
              <Input disabled v-if="searchType === 'user'" v-model="searchData.userNameLike" style="width: 250px" clearable />
              <Input disabled v-if="searchType === 'uid'" v-model="searchData.uid" style="width: 250px" clearable />
              <Select disabled v-if="searchType === 'resourceType'" v-model="searchData.resourceType" style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in resourceTypeList" :value="item.resourceType" :key="item.resourceType">
                  {{ item.alias }}
                </Option>
              </Select>
              <Select disabled v-if="searchType === 'auditType'" v-model="searchData.auditType" filterable style="width: 200px" clearable>
                <Option value="" :label="$t('quan-bu')">{{ $t('quan-bu') }}</Option>
                <Option v-for="item in auditTypeList" :value="item.auditType" :key="item.auditType">
                  {{ item.alias }}
                </Option>
              </Select>
            </FormItem>
            <FormItem>
              <p style="color: red">{{ $t('dan-ci-dao-chu-zui-da-hang-shu-wei') }}{{ globalSetting.maxExportSize }}</p>
            </FormItem>
          </Form>
        </div>
      </div>
      <template #footer>
        <div>
          <Button @click="handleCancel">{{ $t('guan-bi') }}</Button>
          <Button :loading="exportLoading" type="primary" @click="handleConfirmExport">
            {{ $t('dao-chu') }}
          </Button>
        </div>
      </template>
    </CCModal>
  </div>
</template>
<script>
import fecha from 'fecha';
import Mapping from '@/views/util';
import { mapGetters, mapState } from 'vuex';
import copyMixin from '@/mixins/copyMixin';
import { resolveComponent } from 'vue';

export default {
  name: 'OperationLog',
  mixins: [copyMixin],
  data() {
    return {
      resourceType: Mapping.resourceType,
      searchType: 'user',
      noMoreData: false,
      refreshLoading: false,
      showAuditDetail: false,
      showExport: false,
      exportLoading: false,
      firstId: 0,
      lastId: 0,
      prevFirst: [],
      page: 1,
      timeRange: [new Date(new Date().getTime() - 24 * 3600 * 1000), new Date()],
      searchData: {
        uid: '',
        userName: '',
        opStart: '',
        opEnd: '',
        // securityLevel:'',
        pageData: {
          startId: 0,
          pageSize: 20
        }
      },
      logColumn: [
        {
          title: this.$t('cao-zuo-zhe'),
          key: 'userName',
          width: 150
        },
        {
          title: 'uid',
          slot: 'uid',
          width: 200
        },
        {
          title: this.$t('cao-zuo-shi-jian'),
          key: 'operateDate',
          width: 200,
          render: (h, params) => h('div', {}, fecha.format(new Date(params.row.operateDate), 'YYYY-MM-DD HH:mm:ss'))
        },
        {
          title: this.$t('zi-yuan-lei-xing'),
          key: 'resourceTypeDesc',
          width: 150
        },
        {
          title: this.$t('cao-zuo-dong-zuo'),
          key: 'auditTypeDesc',
          width: 200
        },
        {
          title: this.$t('cao-zuo-zi-yuan'),
          slot: 'resourceValue',
          minWidth: 200
        },
        {
          title: this.$t('cao-zuo-di-zhi'),
          key: 'sourceIp',
          width: 150
        },
        {
          title: this.$t('ri-zhi-di-zhi'),
          key: 'logPathWorkerIp',
          width: 150
        },
        {
          title: this.$t('an-quan-deng-ji'),
          key: 'securityLevel',
          width: 150,
          render: (h, params) =>
            h(
              'div',
              {
                style: {
                  color: params.row.securityLevel === 'NORMAL' ? '#19be6b' : params.row.securityLevel === 'HIGH' ? '#ed4014' : ''
                }
              },
              params.row.securityLevel === 'NORMAL' ? this.$t('pu-tong') : params.row.securityLevel === 'HIGH' ? this.$t('gao-feng-xian') : ''
            ),
          filterMultiple: false,
          filters: [
            {
              label: this.$t('pu-tong'),
              value: 'NORMAL'
            },
            {
              label: this.$t('gao-feng-xian'),
              value: 'HIGH'
            }
          ],
          filterRemote(value) {
            this.searchData.securityLevel = value[0];
            this.handleSearch();
          }
        },
        {
          title: this.$t('ri-zhi-wei-yi-xin-xi'),
          key: 'uuidKey',
          width: 520
        },
        {
          title: this.$t('e-wai-can-shu'),
          slot: 'detail',
          width: 130,
          fixed: 'right',
          renderHeader: this.renderHeaderName
        }
      ],
      logData: [],
      type: 'cc',
      auditTypeList: [],
      resourceTypeList: [],
      auditLogDetail: {},
      selectedRow: {},
      exportPageSize: 1000,
      isParseError: false
    };
  },
  computed: {
    ...mapState(['globalSetting'])
  },
  created() {
    if (this.$route.name === 'rdpOperationLog') {
      this.type = 'rdp';
      this.rdpQueryOperationListCondition();
    } else if (this.$route.name === 'operationLog') {
      this.type = 'cc';
      this.ccQueryOperationListCondition();
    }
  },
  mounted() {
    this.handleSearch();
    this.searchData.pageData.pageSize = 20;
  },
  methods: {
    async auditCtrlQueryAll() {
      const res = await this.$services.rdpAuditCtrlQueryAll();

      if (res.success) {
        console.log(res.data);
      }
    },
    handleEnterSearch(e) {
      if (e.code === 'Enter') {
        e.preventDefault();
        this.handleRefresh();
      }
    },

    getLogDetail(detail) {
      try {
        const res = JSON.parse(detail);
        this.isParseError = false;
        return res;
      } catch (e) {
        this.isParseError = true;
        return detail;
      }
    },
    getResourceTypeI18n(type) {
      let alias = '';
      this.resourceTypeList.forEach((resource) => {
        if (type === resource.resourceType) {
          alias = resource.alias;
        }
      });
      return alias;
    },
    getAuditTypeI18n(type) {
      let alias = '';
      this.auditTypeList.forEach((audit) => {
        if (type === audit.auditType) {
          alias = audit.alias;
        }
      });
      return alias;
    },
    handleExport() {
      this.showExport = true;
    },
    async handleConfirmExport() {
      this.exportLoading = true;
      if (this.timeRange.length > 0) {
        this.searchData.opStart =
          this.timeRange[0] && fecha.format(new Date(new Date(this.timeRange[0]).getTime() - 8 * 3600 * 1000), 'YYYY-MM-DDTHH:mm:ss.SSS');
        this.searchData.opEnd =
          this.timeRange[1] && fecha.format(new Date(new Date(this.timeRange[1].getTime() - 8 * 3600 * 1000)), 'YYYY-MM-DDTHH:mm:ss.SSS');
      } else {
        this.searchData.opStart = '';
        this.searchData.opEnd = '';
      }
      const data = { ...this.searchData };
      data.exportType = 'EXCEL';
      data.pageData = null;
      let res = null;
      if (this.type === 'rdp') {
        res = await this.$services.rdpAuditExport({
          data,
          responseType: 'blob',
          modal: false
        });
      } else if (this.type === 'cc') {
        res = await this.$services.ccAuditExport({
          data,
          responseType: 'blob',
          modal: false
        });
      }
      if (res && res.headers) {
        const contentDisposition = res.headers['content-disposition'];

        let fileName = '';
        if (contentDisposition) {
          const fileNameMatch = contentDisposition.match(/filename\*=UTF-8''(.+)|filename\*=(.+)|filename=(.+)/);
          if (fileNameMatch && fileNameMatch[1]) {
            fileName = decodeURIComponent(fileNameMatch[1]);
          } else if (fileNameMatch && fileNameMatch[2]) {
            fileName = decodeURIComponent(`${fileNameMatch[2].replace(/\+/g, ' ')}`);
          } else if (fileNameMatch && fileNameMatch[3]) {
            fileName = fileNameMatch[3];
          }
        }

        const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = fileName;
        document.body.appendChild(link); // 需要将链接添加到文档中
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(link.href);
      }
      this.showExport = false;
      this.exportLoading = false;
    },
    handleRefresh() {
      this.page = 1;
      this.firstId = 0;
      this.lastId = 0;
      this.searchData.pageData.startId = 0;
      this.handleSearch();
    },
    ccQueryOperationListCondition() {
      this.$services.ccAuditQueryListCondition().then((res) => {
        if (res.success) {
          this.auditTypeList = res.data.auditTypeVOS;
          this.resourceTypeList = res.data.resourceTypeVOS;
        }
      });
    },
    rdpQueryOperationListCondition() {
      this.$services.rdpAuditQueryListCondition().then((res) => {
        if (res.success) {
          this.auditTypeList = res.data.auditTypeVOS;
          this.resourceTypeList = res.data.resourceTypeVOS;
        }
      });
    },
    async handleSearch(type) {
      const ctrlRes = await this.$services.rdpAuditCtrlQueryAll();

      if (!ctrlRes.success) {
        return;
      }
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
      this.searchData.pageData.pageSize = 20;
      let apiName = this.$services.ccAuditQueryAll;
      if (this.type === 'rdp') {
        apiName = this.$services.rdpAuditQueryAll;
      }
      apiName({ data: this.searchData })
        .then((res) => {
          if (res.success) {
            this.logData = res.data;
            if (type === 'next') {
              if (!this.prevFirst[this.page - 1]) {
                this.prevFirst.push(this.firstId);
              }
            }
            this.firstId = this.logData[0].id;
            this.lastId = this.logData[this.logData.length - 1].id;
          }
          this.refreshLoading = false;
          this.noMoreData = res.data.length < this.searchData.pageData.pageSize;
        })
        .catch(() => {
          this.refreshLoading = false;
        });
    },
    handlePre() {
      this.page--;
      let startId = this.prevFirst[this.page - 1] + 1;

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
    },
    handleChangeSize() {
      this.handleSearch('next');
    },
    handleChangeSearchType() {
      // 切换查询类型的时候，重置所有搜索的值
      this.searchData = {
        uid: '',
        userName: '',
        opStart: '',
        opEnd: '',
        // securityLevel:'',
        pageData: {
          startId: 0,
          pageSize: 20
        }
      };
    },
    handleSearchUid(row) {
      this.searchType = 'uid';
      this.searchData.uid = row.uid;
      this.handleSearch();
    },
    handleGetAuditDetail(row) {
      let apiName = null;
      if (this.type === 'cc') {
        apiName = this.$services.ccLogViewGrepOperationLog;
      } else if (this.type === 'rdp') {
        apiName = this.$services.rdpLogViewGrepOperationLog;
      }
      apiName({
        data: { operationId: row.id }
      }).then((res) => {
        if (res.success) {
          console.log('res', res);
          this.auditLogDetail = res.data;
          this.selectedRow = row;
          this.showAuditDetail = true;
        }
      });
    },
    handleCancel() {
      this.showAuditDetail = false;
      this.showExport = false;
    },
    formatAuditContent(data) {
      return JSON.parse(`[${data.split('] ')[1]}`);
    },
    renderHeaderName(h) {
      return h('div', [
        h(
          'span',
          {
            style: {
              fontFamily: 'PingFangSC-Medium',
              fontWeight: '500'
            }
          },
          this.$t('e-wai-can-shu')
        ),
        h(
          resolveComponent('Tooltip'),
          {
            style: {
              color: '#888888',
              marginLeft: '8px',
              fontWeight: 400
            },
            content: this.$t('zhi-zhi-chi-zai-xian-cha-kan-dang-tian-de-ri-zhi-ru-xu-cha-kan-yi-gui-dang-de-qing'),
            placement: 'left',
            transfer: true
          },
          {
            default: () => [
              h(resolveComponent('Icon'), {
                type: 'ios-help-circle-outline'
              })
            ]
          }
        )
      ]);
    }
  }
};
</script>

<style scoped lang="less">
.operation-log {
  height: 100%;
  display: flex;
  flex-direction: column;

  .uid {
    display: flex;
    cursor: pointer;

    .copy {
      display: none;
    }

    &:hover {
      .copy {
        display: block;
      }
    }
  }
}

.warn-text {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}
</style>
